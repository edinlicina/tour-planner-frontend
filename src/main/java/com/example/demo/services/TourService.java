package com.example.demo.services;


import com.example.demo.dtos.CreateTourDto;
import com.example.demo.dtos.TourDto;
import com.example.demo.dtos.UpdateTourDto;
import com.example.demo.models.Tour;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public class TourService {
    private final ObservableList<Tour> tours = FXCollections.observableArrayList();
    private final HttpClient client = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public void createTour(String name, String description, String from, String to, String transportType, float distance, float estTime) {
        try {
            CreateTourDto dto = new CreateTourDto();
            dto.setName(name);
            dto.setDescription(description);
            dto.setFrom(from);
            dto.setTo(to);
            dto.setTransportType(transportType);
            dto.setDistance(distance);
            dto.setEstTime(estTime);

            ObjectMapper objectMapper = new ObjectMapper();
            String json = objectMapper.writeValueAsString(dto);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/tours"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            System.out.println("Response status: " + response.statusCode());
            System.out.println("Response body: " + response.body());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void editTour(Tour updatedTour) {
        try {
            UpdateTourDto dto = new UpdateTourDto();
            dto.setName(updatedTour.getName());
            dto.setDescription(updatedTour.getDescription());
            dto.setFrom(updatedTour.getFrom());
            dto.setTo(updatedTour.getTo());
            dto.setTransportType(updatedTour.getTransportType());
            dto.setDistance(updatedTour.getDistance());
            dto.setEstTime(updatedTour.getEstTime());

            ObjectMapper objectMapper = new ObjectMapper();
            String json = objectMapper.writeValueAsString(dto);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/tours/"+ updatedTour.getId()))
                    .header("Content-Type", "application/json")
                    .PUT(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            System.out.println("Response status: " + response.statusCode());
            System.out.println("Response body: " + response.body());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteTour(Tour tour) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/tours/" + tour.getId()))
                    .DELETE()
                    .build();

            HttpResponse<Void> response = client.send(request, HttpResponse.BodyHandlers.discarding());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ObservableList<Tour> getTours() {
        List<Tour> tourList = new ArrayList<>();
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/tours"))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                List<TourDto> dtos = objectMapper.readValue(response.body(), new TypeReference<>() {
                });
                for (TourDto dto : dtos) {
                    Tour tour = new Tour(
                            dto.getId(),
                            dto.getName(),
                            dto.getDescription(),
                            dto.getFrom(),
                            dto.getTo(),
                            dto.getTransportType(),
                            dto.getDistance(),
                            dto.getEstTime()
                    );
                    tourList.add(tour);
                }
            } else {
                System.err.println("Failed to fetch tours: " + response.statusCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return FXCollections.observableArrayList(tourList);
    }
}
