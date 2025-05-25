package com.example.demo;

import com.example.demo.models.Tour;
import com.example.demo.services.RouteService;
import com.example.demo.services.TourService;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.util.converter.NumberStringConverter;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class UpsertTourModalController implements Initializable {

    @FXML private TextField tourNameTextField;
    @FXML private TextField tourDescriptionField;
    @FXML private TextField tourFromField;
    @FXML private TextField toField;
    @FXML private ComboBox<String> tourTransportField;
    @FXML private TextField tourDistanceField;
    @FXML private TextField tourTimeField;
    @FXML private WebView webView;

    private TourService tourService;
    private Tour editingTour;
    private boolean isEditMode = false;
    private final RouteService routeService = new RouteService();

    public void setTourService(TourService service) {
        this.tourService = service;
    }

    public void setTour(Tour tour) {
        if (tour == null) {
            this.editingTour = new Tour(0, "", "", "", "", "", 0, 0);
            this.isEditMode = false;
        } else {
            this.editingTour = tour;
            this.isEditMode = true;
        }

        tourNameTextField.textProperty().bindBidirectional(editingTour.nameProperty());
        tourDescriptionField.textProperty().bindBidirectional(editingTour.descriptionProperty());
        tourFromField.textProperty().bindBidirectional(editingTour.fromProperty());
        toField.textProperty().bindBidirectional(editingTour.toProperty());
        tourTransportField.valueProperty().bindBidirectional(editingTour.transportTypeProperty());
        tourDistanceField.textProperty().bindBidirectional(editingTour.distanceProperty(), new NumberStringConverter());
        tourTimeField.textProperty().bindBidirectional(editingTour.estTimeProperty(), new NumberStringConverter());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        tourTransportField.getItems().addAll(
                "driving-car",
                "driving-hgv",
                "cycling-regular",
                "cycling-mountain",
                "cycling-electric",
                "foot-walking",
                "foot-hiking"
        );
    }

    @FXML
    private void onPreviewButtonClicked() {
        String from = tourFromField.getText().trim();
        String to = toField.getText().trim();
        String profile = tourTransportField.getValue();

        if (from.isBlank() || to.isBlank()) {
            showAlert("Bitte „From“ und „To“ ausfüllen.", Alert.AlertType.WARNING);
            return;
        }
        if (profile == null || profile.isBlank()) {
            showAlert("Bitte einen Transport Type auswählen.", Alert.AlertType.WARNING);
            return;
        }

        try {
            // 1) Route-Infos holen
            RouteService.RouteInfo info = routeService.getRoute(profile, from, to);

            // 2) Distanz & Zeit anzeigen
            tourDistanceField.setText(String.format("%.2f", info.distanceKm));
            tourTimeField.setText(String.format("%.2f", info.durationH));

            // 3) Koordinaten abrufen und auf Karte anzeigen
            List<double[]> coords = routeService.getRouteCoordinates(profile, from, to);
            displayRouteOnMap(coords);

        } catch (IllegalArgumentException iae) {
            showAlert(iae.getMessage(), Alert.AlertType.WARNING);
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Fehler beim Laden der Route:\n" + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void displayRouteOnMap(List<double[]> coordinates) {
        WebEngine engine = webView.getEngine();
        engine.load(getClass().getResource("/com/example/demo/map.html").toExternalForm());

        engine.documentProperty().addListener((obs, oldDoc, newDoc) -> {
            if (newDoc != null) {
                try {
                    ObjectMapper mapper = new ObjectMapper();
                    String json = mapper.writeValueAsString(coordinates);
                    engine.executeScript("showRoute(" + json + ")");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @FXML
    private void onSaveTourButtonClicked() {
        if (editingTour.getName().isBlank() ||
                editingTour.getDescription().isBlank() ||
                editingTour.getFrom().isBlank() ||
                editingTour.getTo().isBlank() ||
                editingTour.getTransportType().isBlank()) {

            showAlert("Alle Pflichtfelder müssen ausgefüllt sein.", Alert.AlertType.WARNING);
            return;
        }

        if (isEditMode) {
            tourService.editTour(editingTour);
        } else {
            tourService.createTour(
                    editingTour.getName(),
                    editingTour.getDescription(),
                    editingTour.getFrom(),
                    editingTour.getTo(),
                    editingTour.getTransportType(),
                    editingTour.getDistance(),
                    editingTour.getEstTime()
            );
        }

        Stage stage = (Stage) tourNameTextField.getScene().getWindow();
        stage.close();
    }

    private void showAlert(String msg, Alert.AlertType type) {
        new Alert(type, msg, ButtonType.OK).showAndWait();
    }
}
