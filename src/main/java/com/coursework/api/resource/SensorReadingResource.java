package com.coursework.api.resource;

import com.coursework.api.model.SensorReading;
import com.coursework.api.repository.InMemoryStore;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.List;

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SensorReadingResource {

    private final String sensorId;
    private final InMemoryStore store;

    public SensorReadingResource(String sensorId, InMemoryStore store) {
        this.sensorId = sensorId;
        this.store = store;
    }

    @GET
    public List<SensorReading> getReadingHistory() {
        return store.getReadingsForSensor(sensorId);
    }

    @POST
    public Response addReading(SensorReading reading, @Context UriInfo uriInfo) {
        SensorReading created = store.addReading(sensorId, reading);
        URI location = uriInfo.getAbsolutePathBuilder().path(created.getId()).build();
        return Response.created(location).entity(created).build();
    }
}
