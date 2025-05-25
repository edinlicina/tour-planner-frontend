package com.example.demo.models;

import javafx.beans.property.*;

public class Tour {
    private final LongProperty id = new SimpleLongProperty();
    private final StringProperty name = new SimpleStringProperty();
    private final StringProperty description = new SimpleStringProperty();
    private final StringProperty from = new SimpleStringProperty();
    private final StringProperty to = new SimpleStringProperty();
    private final StringProperty transportType = new SimpleStringProperty();
    private final FloatProperty distance = new SimpleFloatProperty();
    private final FloatProperty estTime = new SimpleFloatProperty();
    private final StringProperty routeImagePath = new SimpleStringProperty();
    private final StringProperty popularity = new SimpleStringProperty();
    private final FloatProperty avgRating = new SimpleFloatProperty();


    public Tour(long id, String name, String description, String from, String to, String transportType, float distance, float estTime, String popularity, float avgRating) {
        this.id.set(id);
        this.name.set(name);
        this.description.set(description);
        this.from.set(from);
        this.to.set(to);
        this.transportType.set(transportType);
        this.distance.set(distance);
        this.estTime.set(estTime);
        this.popularity.set(popularity);
        this.avgRating.set(avgRating);
    }

    public int getId() {
        return Math.toIntExact(id.get());
    }

    public LongProperty idProperty() {
        return id;
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public String getDescription() {
        return description.get();
    }

    public StringProperty descriptionProperty() {
        return description;
    }

    public void setDescription(String description) {
        this.description.set(description);
    }

    public String getFrom() {
        return from.get();
    }

    public StringProperty fromProperty() {
        return from;
    }

    public void setFrom(String from) {
        this.from.set(from);
    }

    public String getTo() {
        return to.get();
    }

    public StringProperty toProperty() {
        return to;
    }

    public void setTo(String to) {
        this.to.set(to);
    }

    public String getTransportType() {
        return transportType.get();
    }

    public StringProperty transportTypeProperty() {
        return transportType;
    }

    public void setTransportType(String transportType) {
        this.transportType.set(transportType);
    }

    public float getDistance() {
        return distance.get();
    }

    public FloatProperty distanceProperty() {
        return distance;
    }

    public void setDistance(float distance) {
        this.distance.set(distance);
    }

    public float getEstTime() {
        return estTime.get();
    }

    public FloatProperty estTimeProperty() {
        return estTime;
    }

    public void setEstTime(float estTime) {
        this.estTime.set(estTime);
    }

    public String getRouteImagePath() {
        return routeImagePath.get();
    }

    public StringProperty routeImagePathProperty() {
        return routeImagePath;
    }

    public void setRouteImagePath(String path) {
        this.routeImagePath.set(path);
    }

    public String getPopularity() {
        return popularity.get();
    }

    public StringProperty popularityProperty() {
        return popularity;
    }

    public float getAvgRating() {
        return avgRating.get();
    }

    public FloatProperty avgRatingProperty() {
        return avgRating;
    }

    @Override
    public String toString() {
        return name.get();
    }
}