package com.coursework.api.resource;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("/debug")
public class DiagnosticsResource {

    @GET
    @Path("/crash")
    public String triggerCrash() {
        throw new IllegalStateException("Demonstration failure to validate global mapper.");
    }
}
