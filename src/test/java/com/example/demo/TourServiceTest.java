package com.example.demo;

import com.example.demo.models.Tour;
import com.example.demo.services.TourService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TourServiceTest {

    @Test
    void createTour_shouldAddNewTour() {
        TourService service = new TourService();
        int initialSize = service.getTours().size();

        service.createTour("Test", "Beschreibung", "Wien", "Graz", "Auto", 150, 2);

        assertEquals(initialSize + 1, service.getTours().size());
        Tour created = service.getTours().getLast();
        assertEquals("Test", created.getName());
        assertEquals("Wien", created.getFrom());
    }
}
