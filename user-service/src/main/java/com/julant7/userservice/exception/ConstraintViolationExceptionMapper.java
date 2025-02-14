package com.julant7.userservice.exception;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.UriInfo;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.Set;


@Provider
public class ConstraintViolationExceptionMapper implements ExceptionMapper<ConstraintViolationException> {
    @Context
    UriInfo uriInfo;

    @Override
    public Response toResponse(final ConstraintViolationException exception) {
        Set<ConstraintViolation<?>> constraintViolations = exception.getConstraintViolations();

        final var jsonObject = Json.createObjectBuilder()
                .add("host", uriInfo.getAbsolutePath().getHost())
                .add("resource", uriInfo.getAbsolutePath().getPath())
                .add("title", "Validation Errors");

        final var jsonArray = Json.createArrayBuilder();

        for (final var constraint : constraintViolations) {
            String className = constraint.getLeafBean().toString().split("@")[0];
            String message = constraint.getMessage();
            String propertyPath = constraint.getPropertyPath().toString().split("\\.")[2];

            JsonObject jsonError = Json.createObjectBuilder()
                    .add("class", className)
                    .add("field", propertyPath)
                    .add("violationMessage", message)
                    .build();
            jsonArray.add(jsonError);
        }
        JsonObject errorJsonEntity = jsonObject.add("errors", jsonArray.build()).build();
        return Response.status(Response.Status.BAD_REQUEST).entity(errorJsonEntity).build();
    }

}
