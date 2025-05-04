package com.example.demo.services;

import com.example.demo.models.Tour;
import com.example.demo.models.TourLog;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDate;

public class TourLogService {
    private static TourLogService instance = new TourLogService();
    private ObservableList<TourLog> tourLogs = FXCollections.observableArrayList();
    private int nextId = 1;

    private TourLogService() {}

    public static TourLogService getInstance() {
        return instance;
    }

    public ObservableList<TourLog> getAllTourLogs() {
        return tourLogs;
    }

    public ObservableList<TourLog> getLogsForTour(Tour tour) {
        return tourLogs.filtered(log -> log.getTour().equals(tour));
    }

    public TourLog addTourLog(Tour tour, LocalDate date, String comment, String difficulty, float distance, float time, int rating) {
        TourLog newLog = new TourLog(nextId++, tour, date, comment, difficulty, distance, time, rating);
        tourLogs.add(newLog);
        return newLog;
    }

    public void deleteTourLog(TourLog log) {
        tourLogs.remove(log);
    }

    public void updateTourLog(TourLog log, LocalDate date, String comment, String difficulty, float distance, float time, int rating) {
        log.setDate(date);
        log.setComment(comment);
        log.setDifficulty(difficulty);
        log.setDistance(distance);
        log.setTime(time);
        log.setRating(rating);
    }
}