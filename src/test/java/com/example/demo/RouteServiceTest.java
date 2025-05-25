package com.example.demo;

import java.util.ArrayList;
import java.util.List;
import com.example.demo.services.RouteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class RouteServiceTest {

    private DummyRouteService routeService;

    @BeforeEach
    void setUp() {
        routeService = new DummyRouteService();
    }

    @Test
    void getRoute_returnsValidDistanceAndDuration() {
        RouteService.RouteInfo info = routeService.getRoute("driving-car", "Wien", "Graz");

        assertNotNull(info);
        assertEquals(123.45, info.distanceKm);
        assertEquals(2.5, info.durationH);
    }

    @Test
    void getRouteCoordinates_returnsFixedCoordinates() {
        List<double[]> coords = routeService.getRouteCoordinates("driving-car", "Wien", "Graz");

        assertEquals(2, coords.size());
        assertArrayEquals(new double[]{16.37, 48.21}, coords.get(0));
        assertArrayEquals(new double[]{15.44, 47.07}, coords.get(1));
    }

    @Test
    void getRoute_throwsExceptionForInvalidLocation() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            routeService.getRoute("driving-car", "invalid", "Graz");
        });

        assertTrue(exception.getMessage().contains("Kein Geocodergebnis"));
    }

class DummyRouteService extends RouteService {

    public DummyRouteService() {
        // Kein Aufruf von super(); vermeidet config.properties-Abhängigkeit
    }

    @Override
    public RouteInfo getRoute(String profile, String from, String to) {
        if (from.equals("invalid") || to.equals("invalid")) {
            throw new IllegalArgumentException("Kein Geocodergebnis für '" + from + "'");
        }
        return new RouteInfo(123.45, 2.5);
    }

    @Override
    public List<double[]> getRouteCoordinates(String profile, String from, String to) {
        List<double[]> coords = new ArrayList<>();
        coords.add(new double[]{16.37, 48.21}); // Wien
        coords.add(new double[]{15.44, 47.07}); // Graz
        return coords;
    }
    }
}



