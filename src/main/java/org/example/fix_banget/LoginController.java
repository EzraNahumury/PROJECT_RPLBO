package org.example.fix_banget;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;

public class LoginController {
    @FXML
    private TextField usernameField;
    @FXML
    private TextField passwordField;
    public static String tampung;
    private Connection conn;

    public void setConnection() throws SQLException {
        this.conn = DriverManager.getConnection("jdbc:sqlite:MEMBERSHIP.db");
    }

    public void initialize() throws SQLException {
        setConnection();
    }

    @FXML
    protected void login(ActionEvent event) throws SQLException {
        final String username = usernameField.getText();
        final String password = passwordField.getText();
        tampung = username;

        if (username.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Form Error", "Please enter your username and password.");
            return;
        }

        if (username.equals("admin") && password.equals("admin")) {
            navigateToUtama(event);
        } else {
            try {
                String query = "SELECT * FROM users WHERE username = ? AND password = ?";
                PreparedStatement statement = conn.prepareStatement(query);
                statement.setString(1, username);
                statement.setString(2, password);
                ResultSet rs = statement.executeQuery();

                if (rs.next()) {
                    int idUser = rs.getInt("id_USER");
                    String email = rs.getString("email");
                    viewController.idUser = idUser;
                    navigateToHome(event, username, email);
                } else {
                    showAlert(Alert.AlertType.ERROR, "Login Error", "Invalid username or password.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("Error logging in: " + e.getMessage());
            }
        }
    }

    private void navigateToUtama(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("utama.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Navigation Error", "Failed to navigate to the main page.");
        }
    }

    private void navigateToHome(ActionEvent event, String username, String email) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("View Membership.fxml"));
            Parent root = loader.load();

            viewController controller = loader.getController();
            controller.setUserData(username, email);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Navigation Error", "Failed to navigate to the home page.");
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    protected void signUp(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("SignUp.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            System.out.println("IO Exception: " + e.getMessage());
            showAlert(Alert.AlertType.ERROR, "Navigation Error", "Failed to navigate to the sign-up page.");
        }
    }
}
