package com.example.demo;

import com.example.demo.services.TourService;
import com.example.demo.models.Tour;
import javafx.collections.ObservableList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.example.demo.models.Tour;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import static org.junit.jupiter.api.Assertions.*;

public class TourServiceTest {

    private DummyTourService tourService;

    @BeforeEach
    void setUp() {
        tourService = new DummyTourService();
    }

    @Test
    void createTour_shouldAddNewTour() {
        tourService.createTour("TestTour", "Desc", "Wien", "Graz", "Car", 200, 3);
        ObservableList<Tour> tours = tourService.getTours();
        assertEquals(1, tours.size());
        assertEquals("TestTour", tours.get(0).getName());
    }

    @Test
    void deleteTour_shouldRemoveTour() {
        Tour tour = new Tour(1, "DeleteMe", "Desc", "A", "B", "Bike", 100, 2, "low", 3);
        tourService.getTours().add(tour);
        assertEquals(1, tourService.getTours().size());

        tourService.deleteTour(tour);
        assertTrue(tourService.getTours().isEmpty());
    }

    @Test
    void getTours_shouldReturnList() {
        tourService.createTour("Alpha", "test", "X", "Y", "Train", 300, 4);
        ObservableList<Tour> tours = tourService.getTours();
        assertNotNull(tours);
        assertFalse(tours.isEmpty());
    }

    public class DummyTourService extends TourService {
        private final ObservableList<Tour> dummyTours = FXCollections.observableArrayList();

        @Override
        public void createTour(String name, String description, String from, String to, String transportType, float distance, float estTime) {
            Tour newTour = new Tour(999, name, description, from, to, transportType, distance, estTime, "popular", 4);
            dummyTours.add(newTour);
        }

        @Override
        public void deleteTour(Tour tour) {
            dummyTours.remove(tour);
        }

        @Override
        public ObservableList<Tour> getTours() {
            return dummyTours;
        }
    }

}
