package org.example.projectezra;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.sql.*;

public class SignUpController {

    @FXML
    private TextField txtUsername;

    @FXML
    private TextField txtPassword;

    @FXML
    private TextField txtEmail;

    // Method untuk menambahkan data pengguna ke dalam database
    // Method untuk menambahkan data pengguna ke dalam database
    private void addUser(String username, String password, String email) {
        String url = "jdbc:sqlite:user2.db";

        try (Connection conn = DriverManager.getConnection(url)) {
            conn.setAutoCommit(false); // Mulai transaksi

            // Tambahkan pengguna ke tabel profil
            String sqlProfil = "INSERT INTO profil (username, password, email) VALUES (?, ?, ?)";
            PreparedStatement pstmtProfil = conn.prepareStatement(sqlProfil);
            pstmtProfil.setString(1, username);
            pstmtProfil.setString(2, password);
            pstmtProfil.setString(3, email);
            pstmtProfil.executeUpdate();

            // Ambil ID pengguna yang baru saja ditambahkan
            int profilId = -1;
            String sqlGetProfilId = "SELECT last_insert_rowid() AS id FROM profil";
            PreparedStatement pstmtGetProfilId = conn.prepareStatement(sqlGetProfilId);
            ResultSet rs = pstmtGetProfilId.executeQuery();
            if (rs.next()) {
                profilId = rs.getInt("id");
            }

            // Tambahkan entri ke tabel profil_id
            String sqlProfilId = "INSERT INTO jembatan (id_profil) VALUES (?)";
            PreparedStatement pstmtProfilId = conn.prepareStatement(sqlProfilId);
            pstmtProfilId.setInt(1, profilId);
            pstmtProfilId.executeUpdate();


            conn.commit(); // Selesai transaksi
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


    // Method untuk menangani klik tombol "Sign Up"
    @FXML
    private void btnSignUp(ActionEvent event) {
        String username = txtUsername.getText();
        String password = txtPassword.getText();
        String email = txtEmail.getText();

        // Panggil method addUser untuk menambahkan data pengguna ke dalam database
        addUser(username, password, email);

        // Kembali ke halaman Login.fxml setelah pendaftaran berhasil
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Login.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root);
            Stage stage = (Stage) txtUsername.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void goBackToLogin(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Login.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root);
            Stage stage = (Stage) txtUsername.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
