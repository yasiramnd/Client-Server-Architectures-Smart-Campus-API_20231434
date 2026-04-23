package com.coursework.api.model;

public class SensorReading {

    private int id;
    private int sensorId;
    private double value;
    private String capturedAt;

    public SensorReading() {
    }

    public SensorReading(int id, int sensorId, double value, String capturedAt) {
        this.id = id;
        this.sensorId = sensorId;
        this.value = value;
        this.capturedAt = capturedAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSensorId() {
        return sensorId;
    }

    public void setSensorId(int sensorId) {
        this.sensorId = sensorId;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public String getCapturedAt() {
        return capturedAt;
    }

    public void setCapturedAt(String capturedAt) {
        this.capturedAt = capturedAt;
    }
}
