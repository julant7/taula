package com.julant7.clientservice.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

public class MainController {
    @GetMapping("/")
    @ResponseBody
    public String sayHello() {
        return "Hello, this is Client Service!";
    }
}
