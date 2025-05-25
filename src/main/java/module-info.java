module com.example.demo {
    // JavaFX
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.swing;   // für SwingFXUtils
    // AWT/Swing-Klassen (BufferedImage, ImageIO)
    requires java.desktop;
    // HTTP-Client (HttpClient, HttpRequest, …)
    requires java.net.http;
    // Jackson (ObjectMapper, JsonNode, …)
    requires com.fasterxml.jackson.databind;
    requires javafx.web;

    // Erlaube FXML-Loadern und Jackson via Reflection Zugriff...
    opens com.example.demo to javafx.fxml, com.fasterxml.jackson.databind;
    opens com.example.demo.services to com.fasterxml.jackson.databind;
    opens com.example.demo.models to com.fasterxml.jackson.databind;
    opens com.example.demo.dtos to com.fasterxml.jackson.databind;

    // Exporte dein Haupt-Paket, damit JavaFX deine Application-Klasse finden kann
    exports com.example.demo;
}
