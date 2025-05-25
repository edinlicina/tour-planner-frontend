package com.example.demo.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.InputStream;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class RouteService {

    private final String apiKey;
    private final String geocodeUrl;
    private final String directionsUrlTemplate;

    private final HttpClient client = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_1_1)
            .build();
    private final ObjectMapper mapper = new ObjectMapper();

    public RouteService() {
        try (InputStream in = getClass().getResourceAsStream("/config.properties")) {
            if (in == null) {
                throw new RuntimeException("config.properties nicht gefunden im Classpath!");
            }
            Properties cfg = new Properties();
            cfg.load(in);

            apiKey = Objects.requireNonNull(cfg.getProperty("ors.api.key"));
            geocodeUrl = Objects.requireNonNull(cfg.getProperty("ors.geocode.url"));
            directionsUrlTemplate = Objects.requireNonNull(cfg.getProperty("ors.directions.url"));
        } catch (Exception e) {
            throw new RuntimeException("config.properties konnte nicht geladen werden", e);
        }
    }

    /** Enthält Distanz (km) und Dauer (h). */
    public static class RouteInfo {
        public final double distanceKm;
        public final double durationH;
        public RouteInfo(double d, double h) {
            this.distanceKm = d;
            this.durationH  = h;
        }
    }

    /** 1) Freitext → [lon, lat] */
    private double[] geocode(String text) throws Exception {
        String url = geocodeUrl
                + "?text=" + URLEncoder.encode(text, StandardCharsets.UTF_8);

        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Accept", "application/json, application/geo+json")
                .header("Authorization", apiKey)
                .GET()
                .build();

        String body = client.send(req, HttpResponse.BodyHandlers.ofString()).body();
        JsonNode features = mapper.readTree(body).path("features");
        if (!features.isArray() || features.isEmpty()) {
            throw new IllegalArgumentException("Kein Geocodergebnis für '" + text + "'");
        }
        JsonNode coords = features.get(0).path("geometry").path("coordinates");
        return new double[]{ coords.get(0).asDouble(), coords.get(1).asDouble() };
    }

    /** 2) Directions-API für Distanz & Dauer */
    public RouteInfo getRoute(String profile, String from, String to) throws Exception {
        double[] s = geocode(from);
        double[] e = geocode(to);

        String url = String.format(directionsUrlTemplate, profile)
                + "?start=" + s[0] + "," + s[1]
                + "&end=" + e[0] + "," + e[1];

        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Accept", "application/json, application/geo+json")
                .header("Authorization", apiKey)
                .GET()
                .build();

        String body = client.send(req, HttpResponse.BodyHandlers.ofString()).body();
        JsonNode feat = mapper.readTree(body).path("features");
        if (!feat.isArray() || feat.isEmpty()) {
            throw new IllegalArgumentException(
                    "Keine Route gefunden von '" + from + "' nach '" + to + "'");
        }
        JsonNode summary = feat.get(0).path("properties").path("summary");
        double distM = summary.path("distance").asDouble();
        double durS  = summary.path("duration").asDouble();

        return new RouteInfo(distM / 1000.0, durS / 3600.0);
    }

    /** 3) Koordinaten der Route für Karte */
    public List<double[]> getRouteCoordinates(String profile, String from, String to) throws Exception {
        double[] s = geocode(from);
        double[] e = geocode(to);

        String url = String.format(directionsUrlTemplate, profile)
                + "?start=" + s[0] + "," + s[1]
                + "&end=" + e[0] + "," + e[1];

        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Accept", "application/json, application/geo+json")
                .header("Authorization", apiKey)
                .GET()
                .build();

        String body = client.send(req, HttpResponse.BodyHandlers.ofString()).body();
        JsonNode coords = mapper.readTree(body)
                .path("features").get(0)
                .path("geometry").path("coordinates");

        List<double[]> list = new ArrayList<>();
        for (JsonNode node : coords) {
            list.add(new double[]{ node.get(0).asDouble(), node.get(1).asDouble() });
        }

        return list;
    }
}
