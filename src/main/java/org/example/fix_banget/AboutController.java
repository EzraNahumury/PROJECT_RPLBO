package org.example.fix_banget;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class AboutController {
    private Stage previousStage;
    private Stage currentStage;

    public void setPreviousStage(Stage previousStage) {
        this.previousStage = previousStage;
    }

    public void setCurrentStage(Stage currentStage) {
        this.currentStage = currentStage;
    }

    @FXML
    public void handleBackButton(ActionEvent event) {
        try {
            // Load the new FXML file for the view you want to navigate to
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ViewMembership.fxml"));
            Parent root = loader.load();

            // Set up the scene with the loaded FXML
            Scene scene = new Scene(root);

            // Optionally, you can set the title of the stage
            previousStage.setTitle("Membership View");

            // Set the new scene to the previous stage and show it
            previousStage.setScene(scene);
            previousStage.show();

            // Close the current stage
            currentStage.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
