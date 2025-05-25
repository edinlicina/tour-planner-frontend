package com.example.demo.dtos;

public class TourDto {
    private Long id;
    private String name;
    private String description;
    private String from;
    private String to;
    private String transportType;
    private float distance;
    private float estTime;

    public TourDto() {
    }

    public TourDto(Long id, String name, String description, String from, String to, String transportType, float distance, float estTime) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.from = from;
        this.to = to;
        this.transportType = transportType;
        this.distance = distance;
        this.estTime = estTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
