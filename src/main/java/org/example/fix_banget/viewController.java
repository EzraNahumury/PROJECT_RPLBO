package org.example.fix_banget;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class viewController implements Initializable {

    @FXML
    private Button about;

    @FXML
    private Button about12;

    @FXML
    private Button addMembershipButton;

    @FXML
    private Button btnDelete;

    @FXML
    private Label emailLabel;

    @FXML
    private TableColumn<Membership, Integer> id;

    @FXML
    private Button logoutButton;

    @FXML
    private TableColumn<Membership, Date> mulaiExp;

    @FXML
    private TableColumn<Membership, String> namaMembership;

    @FXML
    private TableColumn<Membership, String> namaVendor;

    @FXML
    private TextField pencarian;

    @FXML
    private ImageView picture;

    @FXML
    private TableColumn<Membership, Date> selesaiExp;

    @FXML
    private TableView<Membership> tabelMember;

    @FXML
    private Label usernameLabel;

    private String loggedInUsername;
    public static int idUser;
    private ObservableList<Membership> masterData = FXCollections.observableArrayList(); // Untuk Pencarian

    public void setUserData(String username, String email) {
        usernameLabel.setText(username);
        emailLabel.setText(email);
    }

    @FXML
    void btnDelete(ActionEvent event) {
        // Implement delete functionality
        Membership selectedMembership = tabelMember.getSelectionModel().getSelectedItem();
        if (selectedMembership != null) {
            try (Connection conn = DriverManager.getConnection("jdbc:sqlite:userMember")) {
                String query = "DELETE FROM UserMembership WHERE id = ?";
                try (PreparedStatement statement = conn.prepareStatement(query)) {
                    statement.setInt(1, selectedMembership.getId());
                    int rowsDeleted = statement.executeUpdate();
                    if (rowsDeleted > 0) {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Berhasil Hapus");
                        alert.setHeaderText(null);
                        alert.setContentText("Berhasil Menghapus Membership.");
                        alert.showAndWait();
                        masterData.setAll(loadAllData());
                    } else {
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setTitle("Gagal Hapus");
                        alert.setHeaderText(null);
                        alert.setContentText("Gagal menghapus membership.");
                        alert.showAndWait();
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Tidak Dipilih");
            alert.setHeaderText(null);
            alert.setContentText("Silahkan pilih membership terlebih dahulu.");
            alert.showAndWait();
        }
    }

    @FXML
    void btnDetail(ActionEvent event) {
        Membership selectedMembership = tabelMember.getSelectionModel().getSelectedItem();
        if (selectedMembership != null) {
            String detailMessage = "ID: " + selectedMembership.getId() + "\n"
                    + "Nama Membership: " + selectedMembership.getNamaMembership() + "\n"
                    + "Perusahaan: " + selectedMembership.getPerusahaan() + "\n"
                    + "Harga: " + selectedMembership.getHarga() + "\n"
                    + "Deskripsi: " + selectedMembership.getDeskripsi() + "\n"
                    + "Start Date: " + selectedMembership.getStartDate() + "\n"
                    + "End Date: " + selectedMembership.getEndDate();

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Membership Detail");
            alert.setHeaderText(null);
            alert.setContentText(detailMessage);
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Selection");
            alert.setHeaderText(null);
            alert.setContentText("Please select a membership from the table.");
            alert.showAndWait();
        }
    }

    @FXML
    void btnUpdate(ActionEvent event) {
        Membership selectedMembership = tabelMember.getSelectionModel().getSelectedItem();

        if (selectedMembership != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("EditMembership.fxml"));
                Parent root = loader.load();

                EditMembershipController controller = loader.getController();
                controller.setData(selectedMembership);

                // Set the modality
                Stage stage = new Stage();
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.setScene(new Scene(root));
                stage.showAndWait(); // Menunggu sampai dialog ditutup

                // Setelah dialog ditutup, perbarui tabel dengan data yang diperbarui
                masterData.setAll(loadAllData());
            } catch (IOException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } else {
            // Tampilkan pesan jika tidak ada baris yang dipilih
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Tidak Dipilih");
            alert.setHeaderText(null);
            alert.setContentText("Silahkan pilih membership terlebih dahulu.");
            alert.showAndWait();
        }
    }

    @FXML
    void showAboutPage(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("About.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    void tambahMembership(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("PenambahanMembership.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) addMembershipButton.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    List<Membership> loadAllData() throws SQLException {
        String query = "SELECT * FROM UserMembership WHERE id_user =" + this.idUser;
        Connection conn = DriverManager.getConnection("jdbc:sqlite:userMember");
        List<Membership> result = new ArrayList<>();

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                Membership mb = new Membership(
                        rs.getInt("id"),
                        rs.getString("namaMembership"),
                        rs.getString("perusahaan"),
                        rs.getString("harga"),
                        rs.getString("deskripsi"),
                        rs.getDate("Start_Date"),
                        rs.getDate("END_Date")
                );
                result.add(mb);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        id.setCellValueFactory(cellData -> new javafx.beans.property.SimpleObjectProperty<>(cellData.getValue().getId()));
        namaMembership.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getNamaMembership()));
        namaVendor.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getPerusahaan()));
        mulaiExp.setCellValueFactory(cellData -> new javafx.beans.property.SimpleObjectProperty<>(cellData.getValue().getStartDate()));
        selesaiExp.setCellValueFactory(cellData -> new javafx.beans.property.SimpleObjectProperty<>(cellData.getValue().getEndDate()));

        try {
            masterData.addAll(loadAllData());
            FilteredList<Membership> filteredData = new FilteredList<>(masterData, p -> true);

            pencarian.textProperty().addListener((observable, oldValue, newValue) -> {
                filteredData.setPredicate(membership -> {
                    if (newValue == null || newValue.isEmpty()) {
                        return true;
                    }

                    String lowerCaseFilter = newValue.toLowerCase();

                    if (membership.getNamaMembership().toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    } else if (membership.getPerusahaan().toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    }
                    return false;
                });
            });

            SortedList<Membership> sortedData = new SortedList<>(filteredData);
            sortedData.comparatorProperty().bind(tabelMember.comparatorProperty());

            tabelMember.setItems(sortedData);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void btnLogOut(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Login.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) logoutButton.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void btnLihatprofile(ActionEvent event) {
        try {
            // Muat file FXML
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("editProfile.fxml"));
            Parent root = fxmlLoader.load();

            // Dapatkan controller dan set userID
            EditProfileController controller = fxmlLoader.getController();
            controller.setUserID(idUser);

            // Dapatkan stage saat ini (untuk menutupnya nanti)
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Membuat dan menampilkan stage baru
            Stage stage = new Stage();
            stage.setTitle("Edit Profil");
            stage.setScene(new Scene(root));
            stage.show();

            // Tutup stage saat ini
            currentStage.close();
        } catch (IOException e) {
            e.printStackTrace();
            // Handle error di sini
        }
    }

}
