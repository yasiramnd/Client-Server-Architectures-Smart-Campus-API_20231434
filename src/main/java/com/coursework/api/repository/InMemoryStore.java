package com.coursework.api.repository;

import com.coursework.api.exception.LinkedResourceNotFoundException;
import com.coursework.api.exception.NotFoundException;
import com.coursework.api.exception.RoomNotEmptyException;
import com.coursework.api.exception.SensorUnavailableException;
import com.coursework.api.exception.UnprocessableEntityException;
import com.coursework.api.model.Room;
import com.coursework.api.model.Sensor;
import com.coursework.api.model.SensorReading;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class InMemoryStore {

    private static final InMemoryStore INSTANCE = new InMemoryStore();

    private final Map<String, Room> rooms = new LinkedHashMap<String, Room>();
    private final Map<String, Sensor> sensors = new LinkedHashMap<String, Sensor>();
    private final Map<String, List<SensorReading>> readingsBySensorId = new LinkedHashMap<String, List<SensorReading>>();

    private final AtomicInteger roomSequence = new AtomicInteger(101);
    private final AtomicInteger sensorSequence = new AtomicInteger(1);

    private InMemoryStore() {
        seedData();
    }

    public static InMemoryStore getInstance() {
        return INSTANCE;
    }

    private synchronized void seedData() {
        Room library = createRoom(new Room("LIB-301", "Library Quiet Study", 80));
        Room lab = createRoom(new Room("LAB-101", "IoT Teaching Lab", 40));

        createSensor(new Sensor("TEMP-001", "Temperature", "ACTIVE", 22.7, library.getId()));
        Sensor maintenanceSensor = createSensor(new Sensor("CO2-001", "CO2", "MAINTENANCE", 0.0, lab.getId()));
        addReading("TEMP-001", new SensorReading(null, System.currentTimeMillis(), 22.7));
        readingsBySensorId.put(maintenanceSensor.getId(), new ArrayList<SensorReading>());
    }

    public synchronized List<Room> getAllRooms() {
        return new ArrayList<Room>(rooms.values());
    }

    public synchronized Room getRoomById(String roomId) {
        Room room = rooms.get(roomId);
        if (room == null) {
            throw new NotFoundException("Room not found for id=" + roomId);
        }
        return room;
    }

    public synchronized Room createRoom(Room room) {
        validateRoomPayload(room);
        String id = normalizeOrGenerateRoomId(room.getId());
        if (rooms.containsKey(id)) {
            throw new UnprocessableEntityException("Room id already exists: " + id);
        }

        Room newRoom = new Room(id, room.getName().trim(), room.getCapacity());
        rooms.put(id, newRoom);
        return newRoom;
    }

    public synchronized void deleteRoom(String roomId) {
        Room room = getRoomById(roomId);
        if (!room.getSensorIds().isEmpty()) {
            throw new RoomNotEmptyException("Cannot delete room " + roomId + " because sensors are still assigned to it.");
        }
        rooms.remove(roomId);
    }

    public synchronized List<Sensor> getAllSensors(String type) {
        List<Sensor> allSensors = new ArrayList<Sensor>(sensors.values());
        if (type == null || type.trim().isEmpty()) {
            return allSensors;
        }

        List<Sensor> filtered = new ArrayList<Sensor>();
        String expectedType = type.trim().toLowerCase();
        for (Sensor sensor : allSensors) {
            if (sensor.getType() != null && sensor.getType().toLowerCase().equals(expectedType)) {
                filtered.add(sensor);
            }
        }
        return filtered;
    }

    public synchronized Sensor getSensorById(String sensorId) {
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
            throw new LinkedResourceNotFoundException("Cannot register sensor. roomId=" + sensor.getRoomId() + " does not exist.");
        }

        String id = normalizeOrGenerateSensorId(sensor.getId());
        if (sensors.containsKey(id)) {
            throw new UnprocessableEntityException("Sensor id already exists: " + id);
        }

        String status = normalizeStatus(sensor.getStatus());
        Sensor newSensor = new Sensor(id, sensor.getType().trim(), status, sensor.getCurrentValue(), sensor.getRoomId());
        sensors.put(id, newSensor);
        room.getSensorIds().add(id);
        readingsBySensorId.put(id, new ArrayList<SensorReading>());
        return newSensor;
    }

    public synchronized List<SensorReading> getReadingsForSensor(String sensorId) {
        getSensorById(sensorId);
        List<SensorReading> readings = readingsBySensorId.get(sensorId);
        if (readings == null) {
            return new ArrayList<SensorReading>();
        }
        return new ArrayList<SensorReading>(readings);
    }

    public synchronized SensorReading addReading(String sensorId, SensorReading reading) {
        Sensor sensor = getSensorById(sensorId);
        if ("MAINTENANCE".equalsIgnoreCase(sensor.getStatus())) {
            throw new SensorUnavailableException("Sensor " + sensorId + " is in MAINTENANCE mode and cannot accept new readings.");
        }
        if (reading == null) {
            throw new UnprocessableEntityException("Reading payload is required.");
        }

        SensorReading newReading = new SensorReading(
                reading.getId() == null || reading.getId().trim().isEmpty() ? UUID.randomUUID().toString() : reading.getId().trim(),
                reading.getTimestamp() <= 0 ? System.currentTimeMillis() : reading.getTimestamp(),
                reading.getValue());

        List<SensorReading> readings = readingsBySensorId.get(sensorId);
        if (readings == null) {
            readings = new ArrayList<SensorReading>();
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
        if (room.getCapacity() <= 0) {
            throw new UnprocessableEntityException("Room capacity must be greater than zero.");
        }
    }

    private void validateSensorPayload(Sensor sensor) {
        if (sensor == null) {
            throw new UnprocessableEntityException("Sensor payload is required.");
        }
        if (sensor.getType() == null || sensor.getType().trim().isEmpty()) {
            throw new UnprocessableEntityException("Sensor type is required.");
        }
        if (sensor.getRoomId() == null || sensor.getRoomId().trim().isEmpty()) {
            throw new UnprocessableEntityException("roomId is required.");
        }
    }

    private String normalizeOrGenerateRoomId(String roomId) {
        if (roomId == null || roomId.trim().isEmpty()) {
            return String.format("ROOM-%03d", roomSequence.getAndIncrement());
        }
        return roomId.trim().toUpperCase();
    }

    private String normalizeOrGenerateSensorId(String sensorId) {
        if (sensorId == null || sensorId.trim().isEmpty()) {
            return String.format("SENSOR-%03d", sensorSequence.getAndIncrement());
        }
        return sensorId.trim().toUpperCase();
    }

    private String normalizeStatus(String status) {
        if (status == null || status.trim().isEmpty()) {
            return "ACTIVE";
        }
        String normalized = status.trim().toUpperCase();
        if (!"ACTIVE".equals(normalized) && !"MAINTENANCE".equals(normalized) && !"OFFLINE".equals(normalized)) {
            throw new UnprocessableEntityException("Sensor status must be ACTIVE, MAINTENANCE, or OFFLINE.");
        }
        return normalized;
    }
}
