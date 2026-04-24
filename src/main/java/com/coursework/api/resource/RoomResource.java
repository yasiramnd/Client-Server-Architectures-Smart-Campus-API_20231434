package com.coursework.api.resource;

import com.coursework.api.model.Room;
import com.coursework.api.repository.InMemoryStore;

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
import java.net.URI;
import java.util.List;

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
    @Path("/{roomId}")
    public Room getRoomById(@PathParam("roomId") String roomId) {
        return store.getRoomById(roomId);
    }

    @POST
    public Response createRoom(Room room, @Context UriInfo uriInfo) {
        Room created = store.createRoom(room);
        URI location = uriInfo.getAbsolutePathBuilder().path(created.getId()).build();
        return Response.created(location).entity(created).build();
    }

    @DELETE
    @Path("/{roomId}")
    public Response deleteRoom(@PathParam("roomId") String roomId) {
        store.deleteRoom(roomId);
        return Response.noContent().build();
    }

    @GET
    @Path("/crash")
    public Response triggerCrash() {
        throw new IllegalStateException("Intentional test failure for global exception mapping.");
    }
}
