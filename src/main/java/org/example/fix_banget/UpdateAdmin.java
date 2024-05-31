package org.example.fix_banget;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UpdateAdmin {

    @FXML
    private TextField NameMembership;

    @FXML
    private TextField NamePerusahaan;

    @FXML
    private TextField Harga;

    @FXML
    private TextArea txtAreaDeskripsi;
    private final ADMIN adminController; // Instance of ADMIN

    // Constructor to initialize the adminController
    public UpdateAdmin() {
        adminController = new ADMIN();
    }

    @FXML
    private TableView<Member> tabelMember;

    // Metode untuk mengatur tabel anggota
    public void setMemberTable(TableView<Member> tabelMember) {
        this.tabelMember = tabelMember;
    }




    private Member member; // Menyimpan data member

    // Method untuk mengatur data member
    public void setMember(Member member) {
        this.member = member;
        // Set nilai dari field sesuai dengan data member yang diterima

        NameMembership.setText(member.getNama());
        NamePerusahaan.setText(member.getPerusahaan());
        Harga.setText(member.getHarga());
        txtAreaDeskripsi.setText(member.getDeskripsi());
    }



    @FXML
    protected void addNewMembership(ActionEvent event) {
        if (member == null) {
            showAlert(Alert.AlertType.ERROR, "Data Error", "No member data set.");
            return;
        }

        // Mendapatkan input dari field
        String namaMembership = NameMembership.getText();
        String perusahaan = NamePerusahaan.getText();
        String harga = Harga.getText();
        String deskripsi = txtAreaDeskripsi.getText();

        try (Connection conn = Database.connect()) {
            // Buat pernyataan SQL untuk mengupdate data anggota di tabel AdminMembership
            String sql = "UPDATE AdminMembership SET namaMembership=?, perusahaan=?, harga=?, deskripsi=? WHERE id=?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                // Set parameter untuk nilai kolom yang akan diubah
                stmt.setString(1, namaMembership);
                stmt.setString(2, perusahaan);
                stmt.setString(3, harga);
                stmt.setString(4, deskripsi);
                stmt.setInt(5, member.getId()); // Anda perlu menambahkan metode getId() dalam kelas Member

                // Eksekusi pernyataan SQL
                stmt.executeUpdate();

                // Tampilkan pesan sukses
                showAlert(Alert.AlertType.INFORMATION, "Membership Updated", "Membership updated successfully!");

                // Panggil method loadMembershipData untuk memperbarui data tabel
//                adminController.loadMembershipData();

                // Tutup jendela update setelah berhasil
                Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
                stage.close();
            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to update membership: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Method untuk menampilkan dialog alert
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}