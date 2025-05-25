package com.example.demo.services;

import com.example.demo.dtos.*;
import com.example.demo.models.Tour;
import com.example.demo.models.TourLog;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class TourLogService {
    private static TourLogService instance = new TourLogService();
    private ObservableList<TourLog> tourLogs = FXCollections.observableArrayList();
    private int nextId = 1;
    private final HttpClient client = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();

    private TourLogService() {
    }

    public static TourLogService getInstance() {
        return instance;
    }

    public ObservableList<TourLog> getAllTourLogs() {
        return tourLogs;
    }

    private LocalDate parseDateTimeOrThrow(String input) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
            return LocalDate.parse(input, formatter);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid date format. Expected: yyyy-MM-dd'T'HH:mm");
        }
    }

    public ObservableList<TourLog> getLogsForTour(Tour tour) {
        List<TourLog> tourList = new ArrayList<>();
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/tours/" + tour.getId() + "/tour-logs"))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                List<TourLogDto> dtos = objectMapper.readValue(response.body(), new TypeReference<>() {
                });
                for (TourLogDto dto : dtos) {
                    TourLog tourLog = new TourLog(
                            dto.getId().intValue(),
                            tour,
                            parseDateTimeOrThrow(dto.getDateTime()),
                            dto.getComment(),
                            dto.getDifficulty(),
                            dto.getTotalDistance(),
                            dto.getTotalTime(),
                            dto.getRating()
                    );
                    tourList.add(tourLog);
                }
            } else {
                System.err.println("Failed to fetch tours: " + response.statusCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return FXCollections.observableArrayList(tourList);
    }

    public TourLog addTourLog(Tour tour, LocalDate date, String comment, String difficulty, float distance, float time, int rating) {
        try {
            CreateTourLogDto dto = new CreateTourLogDto();
            dto.setComment(comment);
            dto.setDateTime(date.toString());
            dto.setDifficulty(difficulty);
            dto.setTotalDistance(distance);
            dto.setTotalTime(time);
            dto.setRating(rating);

            ObjectMapper objectMapper = new ObjectMapper();
            String json = objectMapper.writeValueAsString(dto);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/tours/" + tour.getId() + "/tour-logs"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            System.out.println("Response status: " + response.statusCode());

            if (response.statusCode() == 201) {
                TourLogDto createdLog = objectMapper.readValue(response.body(), TourLogDto.class);
                System.out.println("Tour log created with ID: " + createdLog.getId());
                return new TourLog(
                        createdLog.getId().intValue(),
                        tour,
                        parseDateTimeOrThrow(createdLog.getDateTime()),
                        createdLog.getComment(),
                        createdLog.getDifficulty(),
                        createdLog.getTotalDistance(),
                        createdLog.getTotalTime(),
                        createdLog.getRating()
                );
            } else {
                System.out.println("Failed to create tour log");
                System.out.println("Response body: " + response.body());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void deleteTourLog(TourLog log) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/tours/" + log.getTour().getId() + "/tour-logs/" + log.getId()))
                    .DELETE()
                    .build();

            HttpResponse<Void> response = client.send(request, HttpResponse.BodyHandlers.discarding());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateTourLog(TourLog log, LocalDate date, String comment, String difficulty, float distance, float time, int rating) {
        try {
            UpdateTourLogDto dto = new UpdateTourLogDto();
            dto.setComment(comment);
            dto.setDifficulty(difficulty);
            dto.setTotalDistance(distance);
            dto.setTotalTime(time);
            dto.setDateTime(date.toString());
            dto.setRating(rating);

            ObjectMapper objectMapper = new ObjectMapper();
            String json = objectMapper.writeValueAsString(dto);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/tours/" + log.getTour().getId() + "/tour-logs/" + log.getId()))
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

    public void getReport(Path savePath) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/tours/report"))
                    .GET()
                    .build();

            HttpResponse<InputStream> response = client.send(request, HttpResponse.BodyHandlers.ofInputStream());

            if (response.statusCode() == 200) {
                try (InputStream in = response.body();
                     FileOutputStream out = new FileOutputStream(savePath.toFile())) {
                    in.transferTo(out);
                }
                System.out.println("PDF downloaded to " + savePath);
            } else {
                System.err.println("Failed to download PDF. Status: " + response.statusCode());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}