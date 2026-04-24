package com.coursework.api.config;

import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;

import javax.ws.rs.ApplicationPath;

@ApplicationPath("/api/v1")
public class ApiApplication extends ResourceConfig {

    public ApiApplication() {
        packages("com.coursework.api");
        register(JacksonFeature.class);
    }
}
