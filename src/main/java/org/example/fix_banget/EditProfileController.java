package org.example.fix_banget;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EditProfileController {

    @FXML
    private Button simpan;

    @FXML
    private TextField txtemail;

    @FXML
    private TextField txtpassword;

    @FXML
    private TextField txtusername;

    private int userID;

    public void setUserID(int userID) {
        this.userID = userID;
        loadUserData();
    }

    private void loadUserData() {
        try (Connection connection = Database.connect();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT username, email, password FROM users WHERE id_user =?")) {
            preparedStatement.setInt(1, userID);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    txtusername.setText(resultSet.getString("username"));
                    txtemail.setText(resultSet.getString("email"));
                    txtpassword.setText(resultSet.getString("password"));

                    // Set TextFields to be read-only
                    txtusername.setEditable(false);
                    txtemail.setEditable(false);
                    txtpassword.setEditable(false);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle the error appropriately
        }
    }

    // Method to handle the back button action
    @FXML
    public void btnBack(ActionEvent event) {
        try {
            // Load the View Membership.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("View Membership.fxml"));
            Parent root = loader.load();

            // Get the current stage
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Set the new scene
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the error appropriately
        }
    }
}
