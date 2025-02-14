package com.julant7.userservice.config;

import com.julant7.userservice.model.User;
import com.julant7.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.sql.ast.tree.expression.Over;
import org.jboss.aerogear.security.otp.Totp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;

//@RequiredArgsConstructor
@Slf4j
public class CustomAuthenticationProvider extends DaoAuthenticationProvider {
    @Autowired
    private UserRepository userRepository;

    @Override
    public Authentication authenticate(Authentication auth) {
        CustomWebAuthenticationDetails details = (CustomWebAuthenticationDetails) auth.getDetails();
        String verificationCode;
        if (details != null) verificationCode = details.getVerificationCode();
        else verificationCode = null;
        log.info("мяяяяууу " + auth.getName());
        User user = userRepository.findByUsername(auth.getName()).orElseThrow(() ->
                new IllegalArgumentException("Invalid email or password."));
        if (user.isUsing2FA()) {
            Totp totp = new Totp(user.getSecret());
            if (!isValidLong(verificationCode) || !totp.verify(verificationCode)) {
                throw new BadCredentialsException("Invalid verification Code");
            }
        }
        Authentication result = super.authenticate(auth);
        return new UsernamePasswordAuthenticationToken(
                user, result.getCredentials(), result.getAuthorities());
    }

    private boolean isValidLong(String code) {
        try {
            Long.parseLong(code);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
