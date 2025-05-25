package com.example.demo;

import com.example.demo.models.Tour;
import com.example.demo.models.TourLog;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class TourLogServiceTest {

    private Tour tour;
    private DummyTourLogService logService;

    @BeforeEach
    void setUp() {
        tour = new Tour(1, "Demo", "desc", "Wien", "Graz", "Bike", 180, 6, "popular", 4);
        logService = new DummyTourLogService();
    }

    @Test
    void addTourLog_shouldAddLogToList() {
        // Arrange
        int initialSize = logService.getLogsForTour(tour).size();

        // Act
        TourLog log = logService.addTourLog(tour, LocalDate.now(), "Test-Log", "Easy", 20, 1.5f, 5);

        // Assert
        assertNotNull(log);
        assertEquals(initialSize + 1, logService.getLogsForTour(tour).size());
        assertEquals("Test-Log", log.getComment());
    }

    @Test
    void deleteTourLog_shouldRemoveLogFromList() {
        // Arrange
        TourLog log = logService.addTourLog(tour, LocalDate.now(), "To Delete", "Hard", 10, 1, 3);
        assertFalse(logService.getLogsForTour(tour).isEmpty());

        // Act
        logService.deleteTourLog(log);

        // Assert
        assertTrue(logService.getLogsForTour(tour).isEmpty());
    }

    @Test
    void updateTourLog_shouldChangeAllRelevantFields() {
        // Arrange
        TourLog log = logService.addTourLog(tour, LocalDate.now(), "Old", "Medium", 50, 2, 4);

        // Act
        logService.updateTourLog(log, LocalDate.now(), "Updated", "Hard", 55, 2.5f, 2);

        // Assert
        assertEquals("Updated", log.getComment());
        assertEquals("Hard", log.getDifficulty());
        assertEquals(55f, log.getDistance());
        assertEquals(2.5f, log.getTime());
        assertEquals(2, log.getRating());
    }

    @Test
    void getLogsForTour_shouldReturnOnlyMatchingTourLogs() {
        // Arrange
        Tour otherTour = new Tour(2, "Other", "desc", "Linz", "Salzburg", "Train", 100, 2, "quiet", 3);
        logService.addTourLog(tour, LocalDate.now(), "Log1", "Easy", 10, 1, 4);
        logService.addTourLog(otherTour, LocalDate.now(), "Log2", "Hard", 20, 2, 3);

        // Act
        ObservableList<TourLog> logsForTour1 = logService.getLogsForTour(tour);
        ObservableList<TourLog> logsForTour2 = logService.getLogsForTour(otherTour);

        // Assert
        assertEquals(1, logsForTour1.size());
        assertEquals("Log1", logsForTour1.get(0).getComment());

        assertEquals(1, logsForTour2.size());
        assertEquals("Log2", logsForTour2.get(0).getComment());
    }

    @Test
    void addedLogs_shouldHaveIncrementalIds() {
        // Act
        TourLog log1 = logService.addTourLog(tour, LocalDate.now(), "Log1", "Easy", 10, 1, 5);
        TourLog log2 = logService.addTourLog(tour, LocalDate.now(), "Log2", "Easy", 20, 2, 4);

        // Assert
        assertTrue(log2.getId() > log1.getId());
    }

    //  Dummy-Testservice – DB-unabhängig
    static class DummyTourLogService {
        private final ObservableList<TourLog> logs = FXCollections.observableArrayList();
        private int idCounter = 1;

        public ObservableList<TourLog> getLogsForTour(Tour tour) {
            return logs.filtered(log -> log.getTour().getId() == tour.getId());
        }

        public TourLog addTourLog(Tour tour, LocalDate date, String comment, String difficulty, float distance, float time, int rating) {
            TourLog log = new TourLog(idCounter++, tour, date, comment, difficulty, distance, time, rating);
            logs.add(log);
            return log;
        }

        public void deleteTourLog(TourLog log) {
            logs.remove(log);
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
}
