package com.example.demo;

import com.example.demo.models.Tour;
import com.example.demo.models.TourLog;
import com.example.demo.services.TourLogService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class TourLogServiceTest {

    private Tour tour;
    private TourLogService logService;

    @BeforeEach
    void setUp() {
        logService = TourLogService.getInstance();
        tour = new Tour(1, "Demo", "desc", "Wien", "Graz", "Bike", 180, 6);
        logService.getAllTourLogs().clear(); // Tests immer mit leerem Zustand
    }

    @Test
    void addTourLog_shouldAddLog() {
        logService.addTourLog(tour, LocalDate.now(), "Test-Log", "Easy", 20, 1.5f, 5);
        assertEquals(1, logService.getLogsForTour(tour).size());
    }

    @Test
    void deleteTourLog_shouldRemoveLog() {
        TourLog log = logService.addTourLog(tour, LocalDate.now(), "To Delete", "Hard", 10, 1, 3);
        logService.deleteTourLog(log);
        assertTrue(logService.getLogsForTour(tour).isEmpty());
    }

    @Test
    void updateTourLog_shouldModifyFields() {
        TourLog log = logService.addTourLog(tour, LocalDate.now(), "Old", "Medium", 50, 2, 4);
        logService.updateTourLog(log, LocalDate.now(), "Updated", "Hard", 55, 2.5f, 2);

        assertEquals("Updated", log.getComment());
        assertEquals("Hard", log.getDifficulty());
        assertEquals(55f, log.getDistance());
        assertEquals(2.5f, log.getTime());
        assertEquals(2, log.getRating());
    }
}
