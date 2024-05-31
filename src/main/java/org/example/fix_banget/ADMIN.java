package org.example.fix_banget;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ADMIN implements Initializable {

    @FXML
    private MenuItem onLogOutClick;

    @FXML
    private TableView<Member> tabelMember;

    @FXML
    private TableColumn<Member, String> kolomNama;

    @FXML
    private TableColumn<Member, String> kolomPerusahaan;

    @FXML
    private TableColumn<Member, String> kolomHarga;

    @FXML
    private TableColumn<Member, String> kolomdeskripsi;

    @FXML
    private TableColumn<Member, Void> kolomAksi;

    @FXML
    private TextField searchField;
    private FilteredList<Member> filteredMembers;
    private ObservableList<Member> memberObservableList;




    @FXML
    protected void search(ActionEvent event) {
        String keyword = searchField.getText().trim();
        if (!keyword.isEmpty()) {
            filteredMembers.setPredicate(member ->
                    member.getNama().toLowerCase().contains(keyword.toLowerCase()) ||
                            member.getPerusahaan().toLowerCase().contains(keyword.toLowerCase()) ||
                            member.getHarga().toLowerCase().contains(keyword.toLowerCase()) ||
                            member.getDeskripsi().toLowerCase().contains(keyword.toLowerCase())
            );
        } else {
            filteredMembers.setPredicate(null);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        memberObservableList = FXCollections.observableArrayList();
        filteredMembers = new FilteredList<>(FXCollections.observableList(FXCollections.observableArrayList()));
        tabelMember.setItems(filteredMembers);

        getObservableList().addAll(loadMembershipData());

        // Initialize the table columns
        kolomNama.setCellValueFactory(cellData -> cellData.getValue().namaProperty());
        kolomPerusahaan.setCellValueFactory(cellData -> cellData.getValue().perusahaanProperty());
        kolomHarga.setCellValueFactory(cellData -> cellData.getValue().hargaProperty());
        kolomdeskripsi.setCellValueFactory(cellData -> cellData.getValue().deskripsiProperty());

        // Set the cell factory for the action column
        kolomAksi.setCellFactory(new ButtonCellFactory(this));

        // Initialize filtered list
//        filteredMembers = new FilteredList<>(tabelMember.getItems());



        // Load data from the database
//        loadMembershipData();

        // Set filtered list as the items for the table
//        tabelMember.setItems(filteredMembers);

    }


    @FXML
    protected void onLogOutClick(ActionEvent event) {
        try {
            // Get the source node
            MenuItem menuItem = (MenuItem) event.getSource();

            // Get the current stage
            Stage stage = (Stage) menuItem.getParentPopup().getOwnerWindow();

            // Load the Login.fxml file
            Parent root = FXMLLoader.load(getClass().getResource("Login.fxml"));

            // Set the scene to the stage
            stage.setScene(new Scene(root));

            // Show the stage
            stage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Navigation Error", "Failed to navigate to the login screen.");
            e.printStackTrace();
        }
    }



    @FXML
    protected void newMembership(ActionEvent event) {
        try {
            // Load file Create.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Create.fxml"));
            Parent root = loader.load();

            // Buat stage baru
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.showAndWait(); // Tunggu sampai stage ditutup

            // Panggil updateTable untuk memperbarui tabel
            updateTable();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to open Create.fxml: " + e.getMessage());
            e.printStackTrace();
        }
    }




    @FXML
    protected void onDetailClick(ActionEvent event) {
        // Handle detail button click action
        showAlert(Alert.AlertType.INFORMATION, "Detail Clicked", "Detail button was clicked!");
    }

    public List<Member> loadMembershipData() {
        List<Member> memberList = new ArrayList<>();
        try (Connection conn = Database.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM AdminMembership")) {

            System.out.println("Loading membership data..."); // Logging

            // Clear existing items from the table
            // tabelMember.getItems().clear();

            while (rs.next()) {
                Member member = new Member(
                        rs.getInt("id"),
                        rs.getString("namaMembership"),
                        rs.getString("perusahaan"),
                        rs.getString("harga"),
                        rs.getString("deskripsi")
                );
                memberList.add(member);
                System.out.println("Loaded member: " + member.getNama()); // Logging
            }
        } catch (Exception e) {
            System.out.println("Error loading membership data: " + e.getMessage());
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to load membership data.");
        }
        return memberList;
    }



    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void updateTable() {
        memberObservableList = FXCollections.observableArrayList();
        filteredMembers = new FilteredList<>(FXCollections.observableList(FXCollections.observableArrayList()));
        tabelMember.setItems(filteredMembers);

        getObservableList().addAll(loadMembershipData());

        // Initialize the table columns
        kolomNama.setCellValueFactory(cellData -> cellData.getValue().namaProperty());
        kolomPerusahaan.setCellValueFactory(cellData -> cellData.getValue().perusahaanProperty());
        kolomHarga.setCellValueFactory(cellData -> cellData.getValue().hargaProperty());
        kolomdeskripsi.setCellValueFactory(cellData -> cellData.getValue().deskripsiProperty());

        // Set the cell factory for the action column
        kolomAksi.setCellFactory(new ButtonCellFactory(this));
    }

    private ObservableList<Member> getObservableList() {
        return (ObservableList<Member>) filteredMembers.getSource();
    }

//    public void closeConnection() {
//        try {
//            if (conn != null && !conn.isClosed()) {
//                conn.close();
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }

}

