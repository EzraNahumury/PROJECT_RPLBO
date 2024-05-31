package org.example.fix_banget;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class PenambahanMembershipController {
    @FXML
    public ComboBox<String> NameMembership;

    @FXML
    private TextArea txtArea;

    @FXML
    private TextField txtCompany;

    @FXML
    private TextField TxtHarga;

    @FXML
    private DatePicker txtExpDate;

    @FXML
    private DatePicker txtStartDate;

    @FXML
    private Button btnAddNew;

    private Connection conn;

    public void initialize() {
        try {
            setConnection();
            ADMIN adm = new ADMIN();
            List<Member> l = adm.loadMembershipData();

            for (Member m : l) {
                NameMembership.getItems().add(m.getNama());
            }

//            btnAddNew.setOnAction(event -> addNewMembership());

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setConnection() throws SQLException {
        // Mengatur timeout untuk koneksi SQLite
        this.conn = DriverManager.getConnection("jdbc:sqlite:userMember");
    }

    @FXML
    private synchronized void addNewMembership(Event event) {
//        openConnection();
        String namaVendor = txtCompany.getText();
        LocalDate mulaiExp = txtStartDate.getValue();
        LocalDate selesaiExp = txtExpDate.getValue();
        String harga = TxtHarga.getText();
        String detailMembership = txtArea.getText();
        String namaMembership = NameMembership.getValue();

        if (namaMembership == null || namaMembership.trim().isEmpty()) {
            System.out.println("Nama Membership tidak boleh kosong.");
            return;
        }

        try {
            String query = "INSERT INTO UserMembership (namaMembership, perusahaan, harga, deskripsi, START_DATE, END_DATE, id_user) VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, namaMembership);
            statement.setString(2, namaVendor);
            statement.setString(3, harga);
            statement.setString(4, detailMembership);
            statement.setDate(5, java.sql.Date.valueOf(mulaiExp));
            statement.setDate(6, java.sql.Date.valueOf(selesaiExp));
            statement.setInt(7, viewController.idUser);

            statement.execute();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("\"Membership berhasil ditambahkan.\"");
            alert.showAndWait();
            loadFXML((ActionEvent) event);

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Gagal menambahkan membership: " + e.getMessage());
        }
    }

    public void BtnMembership(ActionEvent event) {
        ADMIN adm = new ADMIN();
        List<Member> l = adm.loadMembershipData();

        for (Member m : l) {
            if (m.getNama().equals(NameMembership.getValue())) {
                txtCompany.setText(m.getPerusahaan());
                TxtHarga.setText(m.getHarga());
                txtArea.setText(m.getDeskripsi());
            }
        }
    }

    private void loadFXML(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("View Membership.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}