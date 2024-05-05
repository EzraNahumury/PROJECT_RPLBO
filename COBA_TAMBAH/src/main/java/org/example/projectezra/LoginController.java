package org.example.projectezra;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    @FXML
    private Button btnLogin;
    @FXML
    private Button btnSignUp; // Mengubah tipe menjadi Button
    @FXML
    private TextField txtUsername;
    @FXML
    private TextField txtPassword;

    private Connection con;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            koneksiDB(); // Panggil method untuk menghubungkan ke database saat inisialisasi
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    protected void koneksiDB() throws SQLException, ClassNotFoundException {
        try {
            Class.forName("org.sqlite.JDBC");
            // Parameter koneksi ke database SQLite
            String connectionString = "jdbc:sqlite:user2.db"; // Ubah koneksi ke jalur yang Anda berikan
            con = DriverManager.getConnection(connectionString);
            System.out.println("Koneksi ke database berhasil!");
        } catch (SQLException | ClassNotFoundException e) {
            System.out.println("Koneksi ke database gagal: " + e.getMessage());
            throw e; // Melempar kembali eksepsi agar dapat ditangkap oleh pemanggil method
        }
    }

    @FXML
    protected void login() {
        try {
            // Pastikan koneksi sudah terbentuk sebelum melakukan login
            if (con == null || con.isClosed()) {
                koneksiDB();
            }

            String username = txtUsername.getText();
            String password = txtPassword.getText();

            // Query untuk memeriksa keberadaan user
            String query = "SELECT id FROM profil WHERE username = ? AND password = ?"; // Ubah nama tabel menjadi profil
            PreparedStatement statement = con.prepareStatement(query);
            statement.setString(1, username);
            statement.setString(2, password);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                // Jika ada hasil dari query, berarti login berhasil
                int profilId = resultSet.getInt("id");
                showAlert("Login Successful", "Welcome, " + username + "!");
                // Menuju ke tampilan ViewMembership.fxml setelah login berhasil
                navigateToViewMembership(profilId);
            } else {
                showAlert("Login Failed", "Invalid username or password.");
            }

            statement.close();
            resultSet.close();
        } catch (SQLException | ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }
    }

    private void navigateToViewMembership(int profilId) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("View Membership.fxml"));
        Parent root = loader.load();

        // Mengirimkan ID profil pengguna ke controller ViewMembershipController
        viewController controller = loader.getController();
        controller.setProfilId(profilId, con);

        Stage stage = (Stage) btnLogin.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    protected void signUp() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("SignUp.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) btnSignUp.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
