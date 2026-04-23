package com.coursework.api.config;

import javax.ws.rs.ApplicationPath;
import org.glassfish.jersey.server.ResourceConfig;

@ApplicationPath("/api/v1")
public class ApiApplication extends ResourceConfig {

    public ApiApplication() {
        packages("com.coursework.api");
    }
}
