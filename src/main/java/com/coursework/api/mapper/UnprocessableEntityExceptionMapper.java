package com.coursework.api.mapper;

import com.coursework.api.exception.UnprocessableEntityException;
import com.coursework.api.model.ErrorResponse;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.time.Instant;

@Provider
public class UnprocessableEntityExceptionMapper implements ExceptionMapper<UnprocessableEntityException> {

    @Context
    private UriInfo uriInfo;

    @Override
    public Response toResponse(UnprocessableEntityException exception) {
        ErrorResponse response = new ErrorResponse(
                Instant.now().toString(),
                422,
                "Unprocessable Entity",
                exception.getMessage(),
                safePath());

        return Response.status(422)
                .entity(response)
                .build();
    }

    private String safePath() {
        return uriInfo == null ? "" : uriInfo.getPath();
    }
}
