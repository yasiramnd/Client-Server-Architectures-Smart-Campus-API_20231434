package com.coursework.api.resource;

import com.coursework.api.model.Sensor;
import com.coursework.api.repository.InMemoryStore;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.List;

@Path("/sensors")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SensorResource {

    private final InMemoryStore store = InMemoryStore.getInstance();

    @GET
    public List<Sensor> getAllSensors(@QueryParam("type") String type) {
        return store.getAllSensors(type);
    }

    @GET
    @Path("/{sensorId}")
    public Sensor getSensorById(@PathParam("sensorId") String sensorId) {
        return store.getSensorById(sensorId);
    }

    @POST
    public Response createSensor(Sensor sensor, @Context UriInfo uriInfo) {
        Sensor created = store.createSensor(sensor);
        URI location = uriInfo.getAbsolutePathBuilder().path(created.getId()).build();
        return Response.created(location).entity(created).build();
    }

    @Path("/{sensorId}/readings")
    public SensorReadingResource sensorReadingResource(@PathParam("sensorId") String sensorId) {
        return new SensorReadingResource(sensorId, store);
    }
}
