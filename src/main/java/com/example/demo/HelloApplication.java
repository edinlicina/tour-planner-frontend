package com.example.demo;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        // Lade die FXML-Datei
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("start-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1080, 720);

        // Binde die CSS-Datei ein (liegt im selben Package wie die FXML)
        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());

        // Setze Titel und zeige das Fenster
        stage.setTitle("Tourplanner");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
