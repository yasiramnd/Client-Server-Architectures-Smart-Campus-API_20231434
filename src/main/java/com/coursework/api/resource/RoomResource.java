package com.coursework.api.resource;

import com.coursework.api.model.Room;
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
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

@Path("/rooms")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RoomResource {

    private final InMemoryStore store = InMemoryStore.getInstance();

    @GET
    public List<Room> getAllRooms() {
        return store.getAllRooms();
    }

    @GET
    @Path("/{id}")
    public Room getRoomById(@PathParam("id") int id) {
        return store.getRoomById(id);
    }

    @POST
    public Response createRoom(Room room, @Context UriInfo uriInfo) {
        Room created = store.createRoom(room);
        URI location = uriInfo.getAbsolutePathBuilder().path(String.valueOf(created.getId())).build();
        return Response.created(location).entity(created).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteRoom(@PathParam("id") int id) {
        store.deleteRoom(id);
        return Response.ok().entity("{\"message\":\"Room deleted successfully.\"}").build();
    }
}
