package org.example.fix_banget;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

public class EditMembershipController implements Initializable {

    @FXML
    private ComboBox<String> NameMembership;

    @FXML
    private TextField TxtHarga;

    @FXML
    private Button btnsimpan;

    @FXML
    private TextArea txtArea;

    @FXML
    private TextField txtCompany;

    @FXML
    private DatePicker txtExpDate;

    @FXML
    private DatePicker txtStartDate;

    @FXML
    void BtnMembership(ActionEvent event) {
        ADMIN admn = new ADMIN();
        List<Member> l = admn.loadMembershipData();

        for (Member m : l) {
            if (m.getNama().equals(NameMembership.getValue())) {
                txtCompany.setText(m.getPerusahaan());
                TxtHarga.setText(m.getHarga());
                txtArea.setText(m.getDeskripsi());
            }
        }
    }

    @FXML
    void simpanmembership(ActionEvent event) {
        String namaVendor = txtCompany.getText();
        LocalDate mulaiExp = txtStartDate.getValue();
        LocalDate selesaiExp = txtExpDate.getValue();
        String harga = TxtHarga.getText();
        String detailMembership = txtArea.getText();
        String namaMembership = NameMembership.getValue();

        LocalDate mulaiExpPlusOne = mulaiExp.plusDays(1);
        LocalDate selesaiExpPlusOne = selesaiExp.plusDays(1);

        if (namaMembership == null || namaMembership.trim().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Peringatan", "Nama Membership tidak boleh kosong.");
            return;
        }

        int idMembership = selectedMembership.getId();

        String url = "jdbc:sqlite:userMember";
        // Simpan perubahan ke database
        try (Connection conn = DriverManager.getConnection(url)){
            String query = "UPDATE UserMembership SET perusahaan=?, harga=?, deskripsi=?, START_DATE=?, END_DATE=?, namaMembership=? WHERE id=?";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, namaVendor);
            statement.setString(2, harga);
            statement.setString(3, detailMembership);
            statement.setDate(4, java.sql.Date.valueOf(mulaiExpPlusOne));
            statement.setDate(5, java.sql.Date.valueOf(selesaiExpPlusOne));
            statement.setString(6, namaMembership);
            statement.setInt(7, idMembership);

            int affectedRows = statement.executeUpdate();
            if (affectedRows > 0) {
                showAlert(Alert.AlertType.INFORMATION, "Informasi", "Membership berhasil diperbarui.");
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Gagal memperbarui membership.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Gagal memperbarui membership: " + e.getMessage());
        }
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
            ADMIN adm = new ADMIN();
            List<Member> l = adm.loadMembershipData();
            for (Member m : l) {
                NameMembership.getItems().add(m.getNama());
            }

    }

    private void loadMembershipNames() {
        // Mendapatkan nama-nama membership dari database dan menambahkannya ke combo box
        try (Connection conn = Database.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT DISTINCT namaMembership FROM UserMembership")) {

            while (rs.next()) {
                NameMembership.getItems().add(rs.getString("namaMembership"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Gagal memuat nama membership: " + e.getMessage());
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private Membership selectedMembership;

    public void setData(Membership membership) {
        // Set data ke bidang input di EditMembership.fxml
        selectedMembership = membership;
        // Contoh pengaturan data
        NameMembership.setValue(selectedMembership.getNamaMembership());
        txtCompany.setText(selectedMembership.getPerusahaan());
        TxtHarga.setText(selectedMembership.getHarga());
        txtArea.setText(selectedMembership.getDeskripsi());
        txtStartDate.setValue(selectedMembership.getStartDate().toLocalDate());
        txtExpDate.setValue(selectedMembership.getEndDate().toLocalDate());
    }
}
