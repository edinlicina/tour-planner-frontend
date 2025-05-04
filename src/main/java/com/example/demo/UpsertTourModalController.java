package com.example.demo;

import com.example.demo.models.Tour;
import com.example.demo.services.RouteService;
import com.example.demo.services.TourService;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.converter.NumberStringConverter;

public class UpsertTourModalController {

    @FXML private TextField tourNameTextField;
    @FXML private TextField tourDescriptionField;
    @FXML private TextField tourFromField;
    @FXML private TextField toField;
    @FXML private TextField tourTransportField;
    @FXML private TextField tourDistanceField;
    @FXML private TextField tourTimeField;
    @FXML private ImageView routeImageView;

    private TourService tourService;
    private Tour editingTour;

    public void setTourService(TourService service) {
        this.tourService = service;
    }

    public void setTour(Tour tour) {
        if (tour == null) {
            this.editingTour = new Tour(0, "", "", "", "", "", 0, 0);
        } else {
            this.editingTour = tour;
        }

        // Bindings
        tourNameTextField.textProperty().bindBidirectional(editingTour.nameProperty());
        tourDescriptionField.textProperty().bindBidirectional(editingTour.descriptionProperty());
        tourFromField.textProperty().bindBidirectional(editingTour.fromProperty());
        toField.textProperty().bindBidirectional(editingTour.toProperty());
        tourTransportField.textProperty().bindBidirectional(editingTour.transportTypeProperty());
        tourDistanceField.textProperty().bindBidirectional(editingTour.distanceProperty(), new NumberStringConverter());
        tourTimeField.textProperty().bindBidirectional(editingTour.estTimeProperty(), new NumberStringConverter());

        showRoutePreview(editingTour.getFrom(), editingTour.getTo());
    }

    public void onSaveTourButtonClicked() {
        if (editingTour.getName().isBlank() ||
                editingTour.getDescription().isBlank() ||
                editingTour.getFrom().isBlank() ||
                editingTour.getTo().isBlank() ||
                editingTour.getTransportType().isBlank()) {

            System.out.println("Alle Felder müssen ausgefüllt sein.");
            return;
        }

        if (editingTour.getId() == 0) {
            tourService.createTour(
                    editingTour.getName(),
                    editingTour.getDescription(),
                    editingTour.getFrom(),
                    editingTour.getTo(),
                    editingTour.getTransportType(),
                    editingTour.getDistance(),
                    editingTour.getEstTime()
            );
        } else {
            tourService.editTour(editingTour);
        }

        Stage stage = (Stage) tourNameTextField.getScene().getWindow();
        stage.close();
    }

    private void showRoutePreview(String from, String to) {
        RouteService routeService = new RouteService();
        Image image = routeService.generateRouteImage(from, to);
        routeImageView.setImage(image);
    }
}
