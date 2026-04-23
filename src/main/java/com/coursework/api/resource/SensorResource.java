package com.coursework.api.resource;

import com.coursework.api.model.Sensor;
import com.coursework.api.repository.InMemoryStore;
import java.net.URI;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
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
    @Path("/{id}")
    public Sensor getSensorById(@PathParam("id") int id) {
        return store.getSensorById(id);
    }

    @POST
    public Response createSensor(Sensor sensor, @Context UriInfo uriInfo) {
        Sensor created = store.createSensor(sensor);
        URI location = uriInfo.getAbsolutePathBuilder().path(String.valueOf(created.getId())).build();
        return Response.created(location).entity(created).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteSensor(@PathParam("id") int id) {
        store.deleteSensor(id);
        return Response.ok().entity("{\"message\":\"Sensor deleted successfully.\"}").build();
    }

    @Path("/{sensorId}/readings")
    public SensorReadingResource sensorReadingResource(@PathParam("sensorId") int sensorId) {
        return new SensorReadingResource(sensorId, store);
    }
}
