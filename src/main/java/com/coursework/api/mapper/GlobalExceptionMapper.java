package com.coursework.api.mapper;

import com.coursework.api.model.ErrorResponse;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.time.Instant;

@Provider
public class GlobalExceptionMapper implements ExceptionMapper<Throwable> {

    @Context
    private UriInfo uriInfo;

    @Override
    public Response toResponse(Throwable exception) {
        if (exception instanceof WebApplicationException) {
            WebApplicationException webException = (WebApplicationException) exception;
            int status = webException.getResponse() == null ? 500 : webException.getResponse().getStatus();
            Response.StatusType statusInfo = webException.getResponse() == null
                    ? Response.Status.INTERNAL_SERVER_ERROR
                    : webException.getResponse().getStatusInfo();

            ErrorResponse response = new ErrorResponse(
                    Instant.now().toString(),
                    status,
                    statusInfo.getReasonPhrase(),
                    sanitizeMessage(webException.getMessage(), statusInfo.getReasonPhrase()),
                    safePath());

            return Response.status(status)
                    .entity(response)
                    .build();
        }

        ErrorResponse response = new ErrorResponse(
                Instant.now().toString(),
                Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(),
                Response.Status.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                "An unexpected error occurred. Please contact support.",
                safePath());

        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(response)
                .build();
    }

    private String sanitizeMessage(String message, String fallback) {
        if (message == null || message.trim().isEmpty()) {
            return fallback;
        }
        return message;
    }

    private String safePath() {
        return uriInfo == null ? "" : uriInfo.getPath();
    }
}
