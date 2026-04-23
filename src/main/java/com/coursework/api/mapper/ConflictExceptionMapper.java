package com.coursework.api.mapper;

import com.coursework.api.exception.ConflictException;
import com.coursework.api.model.ErrorResponse;
import java.time.Instant;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class ConflictExceptionMapper implements ExceptionMapper<ConflictException> {

    @Context
    private UriInfo uriInfo;

    @Override
    public Response toResponse(ConflictException exception) {
        ErrorResponse response = new ErrorResponse(
                Instant.now().toString(),
                Response.Status.CONFLICT.getStatusCode(),
                Response.Status.CONFLICT.getReasonPhrase(),
                exception.getMessage(),
                uriInfo.getPath());

        return Response.status(Response.Status.CONFLICT)
                .entity(response)
                .build();
    }
}
