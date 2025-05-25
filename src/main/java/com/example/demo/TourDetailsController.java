package com.example.demo;

import com.example.demo.models.Tour;
import com.example.demo.models.TourLog;
import com.example.demo.services.RouteService;
import com.example.demo.services.TourLogService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class TourDetailsController {

    @FXML private Label nameLabel;
    @FXML private Label descriptionLabel;
    @FXML private Label fromLabel;
    @FXML private Label toLabel;
    @FXML private Label transportLabel;
    @FXML private Label distanceLabel;
    @FXML private Label timeLabel;
    @FXML private ListView<TourLog> logListView;

    private Tour currentTour;
    private final RouteService routeService = new RouteService();

    /**
     * Wird von StartViewController aufgerufen, um diese Detail-Ansicht
     * mit einer Tour zu füllen.
     */
    public void setTour(Tour tour) {
        this.currentTour = tour;

        // 1) Statische Felder
        nameLabel.setText("Name: " + tour.getName());
        descriptionLabel.setText("Description: " + tour.getDescription());
        fromLabel.setText("From: " + tour.getFrom());
        toLabel.setText("To: " + tour.getTo());
        transportLabel.setText("Transport: " + tour.getTransportType());

        // 2) Dynamische Route (Distanz, Zeit – keine Karte!)
        String profile = tour.getTransportType();
        try {
            RouteService.RouteInfo info = routeService.getRoute(profile, tour.getFrom(), tour.getTo());
            distanceLabel.setText(String.format("Distance: %.2f km", info.distanceKm));
            timeLabel.setText(String.format("Estimated Time: %.2f h", info.durationH));
        } catch (Exception e) {
            showAlert("Fehler beim Laden der Route", e.getMessage());
            distanceLabel.setText("Distance: –");
            timeLabel.setText("Estimated Time: –");
        }

        // 3) Tour-Logs laden
        logListView.setItems(TourLogService.getInstance().getLogsForTour(tour));
    }

    @FXML
    private void handleAddLog() {
        openLogForm(null);
    }

    @FXML
    private void handleEditLog() {
        TourLog sel = logListView.getSelectionModel().getSelectedItem();
        if (sel == null) {
            showAlert("No log selected", "Please select a log to edit.");
            return;
        }
        openLogForm(sel);
    }

    @FXML
    private void handleDeleteLog() {
        TourLog sel = logListView.getSelectionModel().getSelectedItem();
        if (sel == null) {
            showAlert("No log selected", "Please select a log to delete.");
            return;
        }
        TourLogService.getInstance().deleteTourLog(sel);
        logListView.setItems(TourLogService.getInstance().getLogsForTour(currentTour));
    }

    private void openLogForm(TourLog logToEdit) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("tour-log-view.fxml"));
            Parent root = loader.load();

            TourLogController ctrl = loader.getController();
            ctrl.setTour(currentTour);
            ctrl.setEditingLog(logToEdit);

            Stage st = new Stage();
            st.setTitle(logToEdit != null ? "Edit Tour Log" : "Add Tour Log");
            st.setScene(new Scene(root));
            st.initModality(Modality.APPLICATION_MODAL);
            st.showAndWait();

            // Liste nach dem Schließen aktualisieren
            logListView.setItems(TourLogService.getInstance().getLogsForTour(currentTour));
        } catch (IOException ex) {
            ex.printStackTrace();
            showAlert("Error", "Could not open tour-log form.");
        }
    }

    private void showAlert(String title, String content) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle(title);
        a.setHeaderText(null);
        a.setContentText(content);
        a.showAndWait();
    }
}
