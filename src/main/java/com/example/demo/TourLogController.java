package com.example.demo;

import com.example.demo.models.Tour;
import com.example.demo.models.TourLog;
import com.example.demo.services.TourLogService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.time.LocalDate;

public class TourLogController {
    @FXML private DatePicker datePicker;
    @FXML private TextArea commentField;
    @FXML private ComboBox<String> difficultyBox;
    @FXML private TextField distanceField;
    @FXML private TextField timeField;
    @FXML private Spinner<Integer> ratingSpinner;

    private Tour tour;
    private TourLog editingLog;

    public void setTour(Tour tour) {
        this.tour = tour;
    }

    public void setEditingLog(TourLog log) {
        this.editingLog = log;

        if (log != null) {
            datePicker.setValue(log.getDate());
            commentField.setText(log.getComment());
            difficultyBox.setValue(log.getDifficulty());
            distanceField.setText(String.valueOf(log.getDistance()));
            timeField.setText(String.valueOf(log.getTime()));
            ratingSpinner.getValueFactory().setValue(log.getRating());
        }
    }

    public void initialize() {
        difficultyBox.getItems().addAll("Easy", "Medium", "Hard");
        ratingSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 5, 3));
    }

    public void handleSaveLog() {
        LocalDate date = datePicker.getValue();
        String comment = commentField.getText();
        String difficulty = difficultyBox.getValue();

        float distance, time;
        int rating = ratingSpinner.getValue();

        try {
            distance = Float.parseFloat(distanceField.getText());
            time = Float.parseFloat(timeField.getText());
        } catch (NumberFormatException e) {
            showAlert("Invalid input", "Distance and time must be numbers.");
            return;
        }

        if (date == null || comment.isEmpty() || difficulty == null) {
            showAlert("Missing input", "Please fill all required fields.");
            return;
        }

        if (editingLog != null) {
            TourLogService.getInstance().updateTourLog(editingLog, date, comment, difficulty, distance, time, rating);
        } else {
            TourLogService.getInstance().addTourLog(tour, date, comment, difficulty, distance, time, rating);
        }

        closeWindow();
    }

    public void handleCancel() {
        closeWindow();
    }

    private void closeWindow() {
        Stage stage = (Stage) datePicker.getScene().getWindow();
        stage.close();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
