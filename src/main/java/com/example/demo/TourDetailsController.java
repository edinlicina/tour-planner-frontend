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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
    @FXML private ImageView routeImageView;
    @FXML private ListView<TourLog> logListView;

    private Tour currentTour;

    public void setTour(Tour tour) {
        this.currentTour = tour;
        nameLabel.setText("Name: " + tour.getName());
        descriptionLabel.setText("Description: " + tour.getDescription());
        fromLabel.setText("From: " + tour.getFrom());
        toLabel.setText("To: " + tour.getTo());
        transportLabel.setText("Transport: " + tour.getTransportType());
        distanceLabel.setText("Distance: " + tour.getDistance() + " km");
        timeLabel.setText("Estimated Time: " + tour.getEstTime() + " h");

        RouteService routeService = new RouteService();
        Image image = routeService.generateRouteImage(tour.getFrom(), tour.getTo());
        routeImageView.setImage(image);

        logListView.setItems(TourLogService.getInstance().getLogsForTour(tour));
    }

    @FXML
    private void handleAddLog() {
        openLogForm(null);
    }

    @FXML
    private void handleEditLog() {
        TourLog selected = logListView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("No log selected", "Please select a log to edit.");
            return;
        }
        openLogForm(selected);
    }

    @FXML
    private void handleDeleteLog() {
        TourLog selected = logListView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("No log selected", "Please select a log to delete.");
            return;
        }
        TourLogService.getInstance().deleteTourLog(selected);
        logListView.setItems(TourLogService.getInstance().getLogsForTour(currentTour));
    }

    private void openLogForm(TourLog logToEdit) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("tour-log-view.fxml"));
            Parent root = loader.load();

            TourLogController controller = loader.getController();
            controller.setTour(currentTour);
            controller.setEditingLog(logToEdit);

            Stage stage = new Stage();
            stage.setTitle(logToEdit != null ? "Edit Tour Log" : "Add Tour Log");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

            logListView.setItems(TourLogService.getInstance().getLogsForTour(currentTour));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}