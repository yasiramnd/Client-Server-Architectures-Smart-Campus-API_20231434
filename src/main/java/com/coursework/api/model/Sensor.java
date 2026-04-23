package com.coursework.api.model;

public class Sensor {

    private int id;
    private int roomId;
    private String type;
    private double currentValue;

    public Sensor() {
    }

    public Sensor(int id, int roomId, String type, double currentValue) {
        this.id = id;
        this.roomId = roomId;
        this.type = type;
        this.currentValue = currentValue;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getCurrentValue() {
        return currentValue;
    }

    public void setCurrentValue(double currentValue) {
        this.currentValue = currentValue;
    }
}
