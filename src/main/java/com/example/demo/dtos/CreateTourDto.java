package com.example.demo.dtos;

public class CreateTourDto {
    private String name;
    private String description;
    private String from;
    private String to;
    private String transportType;
    private float distance;
    private float estTime;

    public CreateTourDto() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getTransportType() {
        return transportType;
    }

    public void setTransportType(String transportType) {
        this.transportType = transportType;
    }

    public float getDistance() {
        return distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }

    public float getEstTime() {
        return estTime;
    }

    public void setEstTime(float estTime) {
        this.estTime = estTime;
    }
}
