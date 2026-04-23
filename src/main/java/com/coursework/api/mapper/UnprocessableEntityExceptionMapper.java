package com.coursework.api.mapper;

import com.coursework.api.exception.UnprocessableEntityException;
import com.coursework.api.model.ErrorResponse;
import java.time.Instant;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

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
                uriInfo.getPath());

        return Response.status(422)
                .entity(response)
                .build();
    }
}
