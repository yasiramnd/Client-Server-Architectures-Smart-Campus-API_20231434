package com.coursework.api.repository;

import com.coursework.api.exception.ConflictException;
import com.coursework.api.exception.ForbiddenOperationException;
import com.coursework.api.exception.NotFoundException;
import com.coursework.api.exception.UnprocessableEntityException;
import com.coursework.api.model.Room;
import com.coursework.api.model.Sensor;
import com.coursework.api.model.SensorReading;
import java.time.Instant;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class InMemoryStore {

    private static final InMemoryStore INSTANCE = new InMemoryStore();

    private final Map<Integer, Room> rooms = new LinkedHashMap<>();
    private final Map<Integer, Sensor> sensors = new LinkedHashMap<>();
    private final Map<Integer, List<SensorReading>> readingsBySensorId = new LinkedHashMap<>();

    private final AtomicInteger roomIdCounter = new AtomicInteger(1);
    private final AtomicInteger sensorIdCounter = new AtomicInteger(1);
    private final AtomicInteger readingIdCounter = new AtomicInteger(1);

    private InMemoryStore() {
        seedData();
    }

    public static InMemoryStore getInstance() {
        return INSTANCE;
    }

    private synchronized void seedData() {
        Room room = createRoom(new Room(0, "Server Room", "Level 2"));
        Sensor sensor = createSensor(new Sensor(0, room.getId(), "temperature", 23.5));
        addReading(sensor.getId(), new SensorReading(0, sensor.getId(), 23.5, Instant.now().toString()));
    }

    public synchronized List<Room> getAllRooms() {
        return new ArrayList<>(rooms.values());
    }

    public synchronized Room getRoomById(int roomId) {
        Room room = rooms.get(roomId);
        if (room == null) {
            throw new NotFoundException("Room not found for id=" + roomId);
        }
        return room;
    }

    public synchronized Room createRoom(Room room) {
        validateRoomPayload(room);
        int id = roomIdCounter.getAndIncrement();
        Room newRoom = new Room(id, room.getName().trim(), room.getLocation().trim());
        rooms.put(id, newRoom);
        return newRoom;
    }

    public synchronized void deleteRoom(int roomId) {
        Room room = getRoomById(roomId);
        if (!room.getSensorIds().isEmpty()) {
            throw new ConflictException("Cannot delete room " + roomId + " because it still has sensors.");
        }
        rooms.remove(roomId);
    }

    public synchronized List<Sensor> getAllSensors(String type) {
        List<Sensor> all = new ArrayList<>(sensors.values());
        if (type == null || type.trim().isEmpty()) {
            return all;
        }

        List<Sensor> filtered = new ArrayList<>();
        String expected = type.trim().toLowerCase();
        for (Sensor sensor : all) {
            if (sensor.getType() != null && sensor.getType().toLowerCase().equals(expected)) {
                filtered.add(sensor);
            }
        }
        return filtered;
    }

    public synchronized Sensor getSensorById(int sensorId) {
        Sensor sensor = sensors.get(sensorId);
        if (sensor == null) {
            throw new NotFoundException("Sensor not found for id=" + sensorId);
        }
        return sensor;
    }

    public synchronized Sensor createSensor(Sensor sensor) {
        validateSensorPayload(sensor);
        Room room = rooms.get(sensor.getRoomId());
        if (room == null) {
            throw new UnprocessableEntityException("Cannot register sensor. roomId="
                    + sensor.getRoomId() + " does not exist.");
        }

        int id = sensorIdCounter.getAndIncrement();
        Sensor newSensor = new Sensor(id, sensor.getRoomId(), sensor.getType().trim().toLowerCase(), sensor.getCurrentValue());
        sensors.put(id, newSensor);

        room.getSensorIds().add(id);
        readingsBySensorId.put(id, new ArrayList<SensorReading>());
        return newSensor;
    }

    public synchronized void deleteSensor(int sensorId) {
        Sensor sensor = getSensorById(sensorId);
        List<SensorReading> history = readingsBySensorId.get(sensorId);
        if (history != null && !history.isEmpty()) {
            throw new ForbiddenOperationException("Deletion blocked. Sensor " + sensorId
                    + " has reading history and is protected.");
        }

        sensors.remove(sensorId);
        readingsBySensorId.remove(sensorId);

        Room room = rooms.get(sensor.getRoomId());
        if (room != null) {
            room.getSensorIds().remove(sensorId);
        }
    }

    public synchronized List<SensorReading> getReadingsForSensor(int sensorId) {
        getSensorById(sensorId);
        List<SensorReading> readings = readingsBySensorId.get(sensorId);
        if (readings == null) {
            return new ArrayList<SensorReading>();
        }
        return new ArrayList<>(readings);
    }

    public synchronized SensorReading addReading(int sensorId, SensorReading reading) {
        Sensor sensor = getSensorById(sensorId);
        if (reading == null) {
            throw new UnprocessableEntityException("Reading payload is required.");
        }

        int id = readingIdCounter.getAndIncrement();
        String capturedAt = reading.getCapturedAt() == null || reading.getCapturedAt().trim().isEmpty()
                ? Instant.now().toString()
                : reading.getCapturedAt();

        SensorReading newReading = new SensorReading(id, sensorId, reading.getValue(), capturedAt);

        List<SensorReading> readings = readingsBySensorId.get(sensorId);
        if (readings == null) {
            readings = new ArrayList<>();
            readingsBySensorId.put(sensorId, readings);
        }
        readings.add(newReading);

        sensor.setCurrentValue(newReading.getValue());
        return newReading;
    }

    private void validateRoomPayload(Room room) {
        if (room == null) {
            throw new UnprocessableEntityException("Room payload is required.");
        }
        if (room.getName() == null || room.getName().trim().isEmpty()) {
            throw new UnprocessableEntityException("Room name is required.");
        }
        if (room.getLocation() == null || room.getLocation().trim().isEmpty()) {
            throw new UnprocessableEntityException("Room location is required.");
        }
    }

    private void validateSensorPayload(Sensor sensor) {
        if (sensor == null) {
            throw new UnprocessableEntityException("Sensor payload is required.");
        }
        if (sensor.getType() == null || sensor.getType().trim().isEmpty()) {
            throw new UnprocessableEntityException("Sensor type is required.");
        }
        if (sensor.getRoomId() <= 0) {
            throw new UnprocessableEntityException("roomId must be a positive integer.");
        }
    }
}
