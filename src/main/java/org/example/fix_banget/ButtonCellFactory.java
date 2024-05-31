package org.example.fix_banget;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.scene.Scene;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import org.example.fix_banget.ADMIN;




public class ButtonCellFactory implements Callback<TableColumn<Member, Void>, TableCell<Member, Void>> {

    private final ADMIN adminController;

    public ButtonCellFactory(ADMIN adminController) {
        this.adminController = adminController;
    }
    @Override
    public TableCell<Member, Void> call(final TableColumn<Member, Void> param) {
        return new TableCell<>() {
            private final Button viewButton = new Button("View");
            private final Button editButton = new Button("Edit");
            private final Button deleteButton = new Button("Delete");

            {
                viewButton.setOnAction(event -> {
                    Member member = getTableView().getItems().get(getIndex());
                    // Handle view action
                    showMemberDetails(member);
                });

                editButton.setOnAction(event -> {
                    Member member = getTableView().getItems().get(getIndex());
                    // Handle edit action
                    editMember(member);
                });

                deleteButton.setOnAction(event -> {

                    Member member = getTableView().getItems().get(getIndex());
                    if (member != null) {
                        // Panggil metode untuk menghapus anggota dari database
                        deleteMemberFromDatabase(member);

                        // Hapus anggota dari daftar item TableView
//                        getTableView().getItems().remove(member);
                        showAlert("Delete", "Deleted " + member.getNama());
                    }

                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    HBox hBox = new HBox(viewButton, editButton, deleteButton);
                    hBox.setSpacing(10);
                    setGraphic(hBox);
                }
            }
        };
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showMemberDetails(Member member) {
        // Create a new alert to display member details
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Membership Details");
        alert.setHeaderText("Membership Details for " + member.getNama());
        alert.setContentText("Nama: " + member.getNama() + "\n" +
                "Perusahaan: " + member.getPerusahaan() + "\n" +
                "Harga: " + member.getHarga() + "\n" +
                "Deskripsi: " + member.getDeskripsi());
        alert.showAndWait();
    }




    private void editMember(Member member) {
        try {
            // Load file Update.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Update.fxml"));
            Parent root = loader.load();

            // Dapatkan instance ADMIN
//            ADMIN adminController = new ADMIN();


            // Set the member to the controller
            UpdateAdmin controller = loader.getController();
            controller.setMember(member);

            // Buat stage baru
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.showAndWait();
            adminController.loadMembershipData();
            adminController.updateTable();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void deleteMemberFromDatabase(Member member) {
        try (Connection conn = Database.connect()) {
            String sql = "DELETE FROM AdminMembership WHERE id=?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, member.getId());
                int affectedRows = stmt.executeUpdate();
                if (affectedRows > 0) {
                    showAlert("Success", "Member " + member.getNama() + " deleted successfully from the database.");
                    adminController.loadMembershipData();
                    adminController.updateTable();
                } else {
                    showAlert("Error", "Failed to delete member " + member.getNama() + " from the database.");
                }
            }
        } catch (SQLException e) {
            showAlert("Database Error", "Failed to delete membership: " + e.getMessage());
            e.printStackTrace();
        }
    }




}
