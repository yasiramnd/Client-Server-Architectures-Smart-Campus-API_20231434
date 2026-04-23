package com.coursework.api.mapper;

import com.coursework.api.exception.NotFoundException;
import com.coursework.api.model.ErrorResponse;
import java.time.Instant;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class NotFoundExceptionMapper implements ExceptionMapper<NotFoundException> {

    @Context
    private UriInfo uriInfo;

    @Override
    public Response toResponse(NotFoundException exception) {
        ErrorResponse response = new ErrorResponse(
                Instant.now().toString(),
                Response.Status.NOT_FOUND.getStatusCode(),
                Response.Status.NOT_FOUND.getReasonPhrase(),
                exception.getMessage(),
                uriInfo.getPath());

        return Response.status(Response.Status.NOT_FOUND)
                .entity(response)
                .build();
    }
}
