package org.example.projectezra;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;

public class PenambahanMembership {
    @FXML
    private TextField txtCompany;
    @FXML
    private DatePicker txtExpDate;
    @FXML
    private TextArea txtArea;
    @FXML
    private TextField txtNameMembership;
    @FXML
    private Button btnAddNew;

    @FXML
    private  DatePicker txtStartDate;

    private Connection conn;
    private int id;
    @FXML
    public void initialize() {
//        try {
//            // Membuat koneksi database
//            koneksiDB();
//        } catch (SQLException | ClassNotFoundException e) {
//            e.printStackTrace();
//            System.out.println("Gagal terhubung ke database: " + e.getMessage());
//        }


        btnAddNew.setOnAction(event -> {
            addNewMembership();
        });
    }
//
//    private void koneksiDB() throws SQLException, ClassNotFoundException {
//        try {
//            Class.forName("org.sqlite.JDBC");
//            String connectionString = "jdbc:sqlite:user2.db";
//            conn = DriverManager.getConnection(connectionString);
//
//            if (conn != null) {
//                System.out.println("Koneksi ke database berhasil!");
//            } else {
//                System.out.println("Gagal terhubung ke database.");
//            }
//        } catch (SQLException | ClassNotFoundException e) {
//            System.out.println("Koneksi ke database gagal: " + e.getMessage());
//            throw e;
//        }
//    }
    public void setConnection(int id,Connection con){
        this.conn = con;
        this.id = id;
    }
//    @FXML
//    private void addNewMembership() {
//        String company = txtCompany.getText();
//        LocalDate expDate = txtExpDate.getValue();
//        String notes = txtArea.getText();
//        String nameMembership = txtNameMembership.getText();
//
//        try {
//            // Query untuk memasukkan data membership baru ke dalam tabel
//            String query = "INSERT INTO view (id_jembatan, namaVendor, Start, End, namaMembership) VALUES (?, ?, ?, ?, ?)";
//            PreparedStatement statement = conn.prepareStatement(query);
//            statement.setInt(1, 1); // Menggantinya dengan id_jembatan yang sesuai
//            statement.setString(2, company);
//            statement.setDate(3, java.sql.Date.valueOf(expDate));
//            statement.setDate(4, java.sql.Date.valueOf(expDate.plusYears(1))); // Contoh: Exp Date + 1 year
//            statement.setString(5, nameMembership);
//
//            int rowsInserted = statement.executeUpdate();
//            if (rowsInserted > 0) {
//                System.out.println("Membership baru berhasil ditambahkan!");
//                // Refresh tabel view setelah menambahkan membership baru
//
//                navigateToView(this.id);
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//            System.out.println("Gagal menambahkan membership: " + e.getMessage());
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }

    @FXML
    private void addNewMembership() {
        // Ambil data dari inputan
        String company = txtCompany.getText();
        LocalDate expDate = txtExpDate.getValue();
        LocalDate startDate = txtStartDate.getValue(); // Tambahkan pengambilan data untuk Start Date
        String notes = txtArea.getText();
        String nameMembership = txtNameMembership.getText();

        try {
            // Query untuk memasukkan data membership baru ke dalam tabel
            String query = "INSERT INTO view (id_jembatan, namaVendor, Start, End, namaMembership) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setInt(1, 1); // Menggantinya dengan id_jembatan yang sesuai
            statement.setString(2, company);
            statement.setDate(3, java.sql.Date.valueOf(startDate)); // Menggunakan Start Date yang diambil dari inputan
            statement.setDate(4, java.sql.Date.valueOf(expDate));
            statement.setString(5, nameMembership);

            // Eksekusi query
            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Membership baru berhasil ditambahkan!");
                // Refresh tabel view setelah menambahkan membership baru
                navigateToView(this.id);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Gagal menambahkan membership: " + e.getMessage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private void navigateToView(int profilId) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("View Membership.fxml"));
        Parent root = loader.load();

        // Mengirimkan ID profil pengguna ke controller ViewMembershipController
        viewController controller = loader.getController();
        controller.setProfilId(profilId, conn);

        Stage stage = (Stage) txtArea.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }
}