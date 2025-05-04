package com.example.demo.models;

import javafx.beans.property.*;
import java.time.LocalDate;

public class TourLog {
    private final IntegerProperty id = new SimpleIntegerProperty();
    private final ObjectProperty<LocalDate> date = new SimpleObjectProperty<>();
    private final StringProperty comment = new SimpleStringProperty();
    private final StringProperty difficulty = new SimpleStringProperty();
    private final FloatProperty distance = new SimpleFloatProperty();
    private final FloatProperty time = new SimpleFloatProperty();
    private final IntegerProperty rating = new SimpleIntegerProperty();
    private final ObjectProperty<Tour> tour = new SimpleObjectProperty<>();

    public TourLog(int id, Tour tour, LocalDate date, String comment, String difficulty, float distance, float time, int rating) {
        this.id.set(id);
        this.tour.set(tour);
        this.date.set(date);
        this.comment.set(comment);
        this.difficulty.set(difficulty);
        this.distance.set(distance);
        this.time.set(time);
        this.rating.set(rating);
    }

    public int getId() { return id.get(); }
    public IntegerProperty idProperty() { return id; }

    public LocalDate getDate() { return date.get(); }
    public ObjectProperty<LocalDate> dateProperty() { return date; }

    public String getComment() { return comment.get(); }
    public StringProperty commentProperty() { return comment; }

    public String getDifficulty() { return difficulty.get(); }
    public StringProperty difficultyProperty() { return difficulty; }

    public float getDistance() { return distance.get(); }
    public FloatProperty distanceProperty() { return distance; }

    public float getTime() { return time.get(); }
    public FloatProperty timeProperty() { return time; }

    public int getRating() { return rating.get(); }
    public IntegerProperty ratingProperty() { return rating; }

    public Tour getTour() { return tour.get(); }
    public ObjectProperty<Tour> tourProperty() { return tour; }
    public void setTour(Tour tour) { this.tour.set(tour); }

    public void setDate(LocalDate date) { this.date.set(date); }
    public void setComment(String comment) { this.comment.set(comment); }
    public void setDifficulty(String difficulty) { this.difficulty.set(difficulty); }
    public void setDistance(float distance) { this.distance.set(distance); }
    public void setTime(float time) { this.time.set(time); }
    public void setRating(int rating) { this.rating.set(rating); }

    @Override
    public String toString() {
        return date.get() + " - " + comment.get() + " (" + rating.get() + "/5)";
    }
}