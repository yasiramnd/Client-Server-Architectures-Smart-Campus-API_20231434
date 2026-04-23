package com.coursework.api.model;

import java.util.LinkedHashSet;
import java.util.Set;

public class Room {

    private int id;
    private String name;
    private String location;
    private Set<Integer> sensorIds = new LinkedHashSet<>();

    public Room() {
    }

    public Room(int id, String name, String location) {
        this.id = id;
        this.name = name;
        this.location = location;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Set<Integer> getSensorIds() {
        return sensorIds;
    }

    public void setSensorIds(Set<Integer> sensorIds) {
        this.sensorIds = sensorIds;
    }
}
