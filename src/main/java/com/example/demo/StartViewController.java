package com.example.demo;

import com.example.demo.models.Tour;
import com.example.demo.services.RouteService;
import com.example.demo.services.TourLogService;
import com.example.demo.services.TourService;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.util.List;

public class StartViewController {

    @FXML
    private ListView<Tour> toursListView;
    @FXML
    private TextField searchField;
    @FXML
    private WebView previewWebView;
    @FXML
    private Label previewDistanceLabel;
    @FXML
    private Label previewTimeLabel;

    private final TourService tourService = new TourService();
    private final RouteService routeService = new RouteService();
    private ObservableList<Tour> allTours;
    private FilteredList<Tour> filteredTours;

    @FXML
    public void initialize() {
        System.out.println("▶ initialize() called in StartViewController");

        allTours = FXCollections.observableArrayList(tourService.getTours());
        filteredTours = new FilteredList<>(allTours, p -> true);
        toursListView.setItems(filteredTours);

        searchField.textProperty().addListener((obs, oldV, newV) -> {
            String kw = newV.toLowerCase();
            filteredTours.setPredicate(t ->
                    t.getName().toLowerCase().contains(kw) ||
                            t.getDescription().toLowerCase().contains(kw) ||
                            t.getFrom().toLowerCase().contains(kw) ||
                            t.getTo().toLowerCase().contains(kw) ||
                            t.getTransportType().toLowerCase().contains(kw)
            );
        });

        toursListView.getSelectionModel().selectedItemProperty().addListener((obs, oldT, newT) -> {
            if (newT != null) {
                showRoutePreview(newT);
            } else {
                clearPreview();
            }
        });

        clearPreview();
    }

    public void onCreateTourButtonClicked() throws IOException {
        openUpsertModal(null);
    }

    public void onEditTourButtonClicked() throws IOException {
        Tour sel = toursListView.getSelectionModel().getSelectedItem();
        if (sel != null) openUpsertModal(sel);
    }

    public void onDeleteTourButtonClicked() {
        Tour sel = toursListView.getSelectionModel().getSelectedItem();
        if (sel != null) {
            tourService.deleteTour(sel);
            allTours.remove(sel);
            clearPreview();
        }
    }

    public void onRefreshListClicked() {
        List<Tour> fresh = tourService.getTours();
        allTours.setAll(fresh);
        clearPreview();
    }

    public void onShowDetailsButtonClicked() throws IOException {
        Tour sel = toursListView.getSelectionModel().getSelectedItem();
        if (sel == null) return;

        FXMLLoader loader = new FXMLLoader(getClass().getResource("tour-details-view.fxml"));
        Parent root = loader.load();
        TourDetailsController ctrl = loader.getController();
        ctrl.setTour(sel);
        Stage st = new Stage();
        st.setScene(new Scene(root));
        st.setTitle("Tour Details");
        st.show();
    }

    private void openUpsertModal(Tour tour) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("upsert-tour-modal-view.fxml"));
        Parent root = loader.load();
        UpsertTourModalController ctrl = loader.getController();
        ctrl.setTourService(tourService);
        ctrl.setTour(tour);

        Stage st = new Stage();
        st.initModality(Modality.APPLICATION_MODAL);
        st.setScene(new Scene(root));
        st.setTitle(tour == null ? "Create Tour" : "Edit Tour");
        st.showAndWait();

        List<Tour> fresh = tourService.getTours();
        allTours.setAll(fresh);
        toursListView.getSelectionModel().selectLast();
    }

    private void showRoutePreview(Tour tour) {
        String from = tour.getFrom();
        String to = tour.getTo();
        String profile = tour.getTransportType();

        if (from == null || from.isBlank() || to == null || to.isBlank()) {
            clearPreview();
            return;
        }

        try {
            RouteService.RouteInfo info = routeService.getRoute(profile, from, to);
            List<double[]> coords = routeService.getRouteCoordinates(profile, from, to);

            previewDistanceLabel.setText(String.format("Distance: %.2f km", info.distanceKm));
            previewTimeLabel.setText(String.format("Estimated Time: %.2f h", info.durationH));
            displayRouteOnMap(coords);

        } catch (IllegalArgumentException iae) {
            clearPreview();
            showAlert(iae.getMessage(), Alert.AlertType.WARNING);
        } catch (Exception e) {
            clearPreview();
            e.printStackTrace();
            showAlert("Fehler beim Laden der Route:\n" + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void displayRouteOnMap(List<double[]> coordinates) {
        WebEngine engine = previewWebView.getEngine();
        URL mapUrl = getClass().getResource("/com/example/demo/map.html");

        if (mapUrl == null) {
            System.err.println("❌ map.html nicht gefunden!");
            return;
        }
        engine.load(mapUrl.toExternalForm());
        engine.documentProperty().addListener((obs, oldDoc, newDoc) -> {
            if (newDoc != null) {
                try {
                    String json = new ObjectMapper().writeValueAsString(coordinates);
                    engine.executeScript("showRoute(" + json + ")");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void clearPreview() {
        previewDistanceLabel.setText("Distance: –");
        previewTimeLabel.setText("Estimated Time: –");
        previewWebView.getEngine().loadContent("");
    }

    private void showAlert(String msg, Alert.AlertType type) {
        new Alert(type, msg, ButtonType.OK).showAndWait();
    }

    public void onReportToursButtonClicked(ActionEvent actionEvent) {
        Stage stage = new Stage();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Tour Report PDF");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("PDF Files", "*.pdf")
        );
        fileChooser.setInitialFileName("tours-report.pdf");

        File file = fileChooser.showSaveDialog(stage);
        if (file != null) {
            Path savePath = file.toPath();
            TourLogService.getInstance().getReport(savePath);

        } else {
            System.out.println("❌ User canceled the file save dialog.");
        }

    }
}
