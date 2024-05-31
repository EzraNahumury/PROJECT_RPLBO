package org.example.fix_banget;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CreateMembershipController {

    @FXML
    private TextField NameMembership;

    @FXML
    private TextField NamePerusahaan;

    @FXML
    private TextField Harga;

    @FXML
    private TextArea txtAreaDeskripsi;



    @FXML
    protected void addNewMembership(ActionEvent event) {
        // Mendapatkan input dari field
        String namaMembership = NameMembership.getText();
        String perusahaan = NamePerusahaan.getText();
        String harga = Harga.getText();
        String deskripsi = txtAreaDeskripsi.getText();

        try (Connection conn = Database.connect()) {
            // Buat pernyataan SQL untuk menyisipkan data baru ke dalam tabel AdminMembership
            String sql = "INSERT INTO AdminMembership (namaMembership, perusahaan, harga, deskripsi) VALUES (?, ?, ?, ?)";
            try (PreparedStatement stmt = ((Connection) conn).prepareStatement(sql)) {
                // Set parameter untuk nilai kolom
                stmt.setString(1, namaMembership);
                stmt.setString(2, perusahaan);
                stmt.setString(3, harga);
                stmt.setString(4, deskripsi);

                // Eksekusi pernyataan SQL
                stmt.executeUpdate();
            }

            // Tampilkan pesan sukses
            showAlert(Alert.AlertType.INFORMATION, "Membership Added", "New membership added successfully!");
            // Menutup stage
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.close();

            // Load Utama.fxml when "ADD NEW MEMBERSHIP" button is clicked
//            loadUtamaFXML(event);
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to add new membership to the database: " + e.getMessage());
            e.printStackTrace();
        }
    }


//    // Method to load Utama.fxml
//    private void loadUtamaFXML(ActionEvent event) {
//        try {
//            // Load Utama.fxml
//            Parent root = FXMLLoader.load(getClass().getResource("Utama.fxml"));
//
//            // Get the current stage
//            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
//
//            // Set the scene to the stage
//            stage.setScene(new Scene(root));
//
//            // Show the stage
//            stage.show();
//        } catch (IOException e) {
//            showAlert(Alert.AlertType.ERROR, "Navigation Error", "Failed to navigate to the main screen.");
//            e.printStackTrace();
//        }
//    }


    // Method untuk menampilkan dialog alert
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}