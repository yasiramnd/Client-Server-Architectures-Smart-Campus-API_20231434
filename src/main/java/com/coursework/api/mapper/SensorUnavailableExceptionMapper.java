package com.coursework.api.mapper;

import com.coursework.api.exception.SensorUnavailableException;
import com.coursework.api.model.ErrorResponse;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.time.Instant;

@Provider
public class SensorUnavailableExceptionMapper implements ExceptionMapper<SensorUnavailableException> {

    @Context
    private UriInfo uriInfo;

    @Override
    public Response toResponse(SensorUnavailableException exception) {
        ErrorResponse response = new ErrorResponse(
                Instant.now().toString(),
                Response.Status.FORBIDDEN.getStatusCode(),
                Response.Status.FORBIDDEN.getReasonPhrase(),
                exception.getMessage(),
                safePath());

        return Response.status(Response.Status.FORBIDDEN)
                .entity(response)
                .build();
    }

    private String safePath() {
        return uriInfo == null ? "" : uriInfo.getPath();
    }
}
