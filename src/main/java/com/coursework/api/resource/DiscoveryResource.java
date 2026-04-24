package com.coursework.api.resource;

import com.coursework.api.model.DiscoveryResponse;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.LinkedHashMap;
import java.util.Map;

@Path("")
@Produces(MediaType.APPLICATION_JSON)
public class DiscoveryResource {

    @GET
    public DiscoveryResponse getDiscovery() {
        Map<String, String> resources = new LinkedHashMap<String, String>();
        resources.put("rooms", "/api/v1/rooms");
        resources.put("roomById", "/api/v1/rooms/{roomId}");
        resources.put("sensors", "/api/v1/sensors");
        resources.put("sensorById", "/api/v1/sensors/{sensorId}");
        resources.put("sensorReadings", "/api/v1/sensors/{sensorId}/readings");
        resources.put("debugCrash", "/api/v1/debug/crash");

        return new DiscoveryResponse(
                "Smart Campus Sensor and Room Management API",
                "v1",
                "w2120184@westminster.ac.uk",
                "/api/v1",
                resources);
    }
}
