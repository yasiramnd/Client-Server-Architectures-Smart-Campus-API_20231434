package com.coursework.api.mapper;

import com.coursework.api.exception.ForbiddenOperationException;
import com.coursework.api.model.ErrorResponse;
import java.time.Instant;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class ForbiddenOperationExceptionMapper implements ExceptionMapper<ForbiddenOperationException> {

    @Context
    private UriInfo uriInfo;

    @Override
    public Response toResponse(ForbiddenOperationException exception) {
        ErrorResponse response = new ErrorResponse(
                Instant.now().toString(),
                Response.Status.FORBIDDEN.getStatusCode(),
                Response.Status.FORBIDDEN.getReasonPhrase(),
                exception.getMessage(),
                uriInfo.getPath());

        return Response.status(Response.Status.FORBIDDEN)
                .entity(response)
                .build();
    }
}
