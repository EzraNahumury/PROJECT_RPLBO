package org.example.projectezra;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class viewController implements Initializable {
    @FXML
    private Label usernameLabel;
    @FXML
    private Label emailLabel;
    @FXML
    private Button logoutButton;
    @FXML
    private Button addMembershipButton;
    @FXML
    private Button about;
    @FXML
    private  Button btnDelete;

    public TableView<viewmembership> tabelMember;
    public TableColumn<viewmembership, Integer> id;
    public TableColumn<viewmembership, String> namaVendor;
    public TableColumn<viewmembership, Date> Start;
    public TableColumn<viewmembership, Date> End;
    public TableColumn<viewmembership, String> namaMembership;

    private Connection conn;

    private int profilId; // ID profil pengguna yang login=

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tabelMember.setEditable(false);
        id.setCellValueFactory(new PropertyValueFactory<>("id"));
        namaVendor.setCellValueFactory(new PropertyValueFactory<>("namaVendor"));
        Start.setCellValueFactory(new PropertyValueFactory<>("Start"));
        End.setCellValueFactory(new PropertyValueFactory<>("End"));
        namaMembership.setCellValueFactory(new PropertyValueFactory<>("namaMembership"));
        try {
            koneksiDB();
            // Menetapkan listener pada item yang dipilih di dalam TableView
            tabelMember.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
                if (newSelection != null) {
                    int selectedId = newSelection.getId();
                    String selectedNamaVendor = newSelection.getNamaVendor();
                    // Lanjutkan dengan menampilkan nilai-nilai tersebut di UI sesuai kebutuhan

                    // Menambahkan kolom baru ke dalam UI
                    // Misalnya, menampilkan nilai dari kolom baru di dalam sebuah label
                    String newValueFromDatabase = ""; // Mendapatkan nilai dari database sesuai dengan selectedId
                    // Ganti labelKolomBaru dengan nama label yang sesuai
                    tabelMember.setAccessibleText(newValueFromDatabase);
                }
            });
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            System.out.println("Gagal mengambil data dari database: " + e.getMessage());
        }

        // Menambahkan event handler untuk tombol logout
        logoutButton.setOnAction(event -> {
            // Panggil metode untuk melakukan logout
            logout();
        });
        about.setOnAction(event -> {
            // Panggil method untuk menampilkan halaman About
            showAboutPage();
        });
    }

    public void setUserData(String username, String email) {
        usernameLabel.setText(username);
        emailLabel.setText(email);
    }

    public void setProfilId(int profilId, Connection con) {
        this.profilId = profilId;
        this.conn = con;
        try {
            // Query untuk mengambil username dan email dari tabel profil berdasarkan id
            String query = "SELECT username, email FROM profil WHERE id =?";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setInt(1, profilId);
            ResultSet resultSet = statement.executeQuery();

            String username = "";
            String email = "";
            if (resultSet.next()) {
                username = resultSet.getString("username");
                email = resultSet.getString("email");
            }

            // Set username dan email ke label yang sesuai
            setUserData(username, email);

            // Lanjutkan dengan mengambil data dari tabel membership atau melakukan operasi lain yang diperlukan
            String membershipQuery = "SELECT * FROM view";
            ObservableList<viewmembership> data = getDataFromTable(membershipQuery);
            tabelMember.setItems(data); // Atur data baru ke tabelMember
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Gagal mengambil data dari database: " + e.getMessage());
        }
    }

    private ObservableList<viewmembership> getDataFromTable(String query) throws SQLException {
        ResultSet rs;
        PreparedStatement preparedStatement = conn.prepareStatement(query);
        rs = preparedStatement.executeQuery();
        ObservableList<viewmembership> member = FXCollections.observableArrayList();
        while (rs.next()) {
            viewmembership p = new viewmembership(rs.getInt("id_view"), rs.getString("namaVendor"), rs.getDate("Start"), rs.getDate("End"), rs.getString("namaMembership"));
            member.add(p);
        }
        return member;
    }

    private void koneksiDB() throws SQLException, ClassNotFoundException {
        try {
            Class.forName("org.sqlite.JDBC");
            String connectionString = "jdbc:sqlite:user2.db";
            conn = DriverManager.getConnection(connectionString);

            if (conn != null) {
                System.out.println("Koneksi ke database berhasil!");
            } else {
                System.out.println("Gagal terhubung ke database.");
            }
        } catch (SQLException | ClassNotFoundException e) {
            System.out.println("Koneksi ke database gagal: " + e.getMessage());
            throw e;
        }
    }

    private ObservableList<viewmembership> getDataFromTable(String query, int idJembatan) throws SQLException {
        ResultSet rs;
        String select = query;
        PreparedStatement preparedStatement = conn.prepareStatement(select);
        preparedStatement.setInt(1, idJembatan);
        rs = preparedStatement.executeQuery();
        ObservableList<viewmembership> member = FXCollections.observableArrayList();
        while (rs.next()) {
            viewmembership p = new viewmembership(rs.getInt("id_view"), rs.getString("namaVendor"), rs.getDate("Start"), rs.getDate("End"), rs.getString("namaMembership"));
            member.add(p);
        }
        return member;
    }

    private void logout() {
        try {
            // Load file FXML untuk halaman login
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Login.fxml"));
            Parent root = loader.load();

            // Dapatkan stage dari root node
            Stage stage = (Stage) logoutButton.getScene().getWindow();

            // Set scene dengan halaman login
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void addMembership() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("PenambahanMembership.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) addMembershipButton.getScene().getWindow();

            Scene scene = new Scene(root);
            stage.setScene(scene);
            PenambahanMembership controller = loader.getController();
            controller.setConnection(this.profilId, conn);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void showAboutPage() {
        try {
            // Load file FXML untuk halaman About
            FXMLLoader loader = new FXMLLoader(getClass().getResource("About.fxml"));
            Parent root = loader.load();

            // Tampilkan halaman About
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();

            // Set the current stage as a property in the AboutController
            AboutController aboutController = loader.getController();
            aboutController.setPreviousStage(stage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void btnDelete() {
        int selectedId = tabelMember.getSelectionModel().getSelectedItem().getId();
        try {
            deleteRow(selectedId);
            updateTableView();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Gagal menghapus data dari database: " + e.getMessage());
        }
    }

    private void deleteRow(int id) throws SQLException {
        String deleteQuery = "DELETE FROM view WHERE id_view = ?";
        PreparedStatement preparedStatement = conn.prepareStatement(deleteQuery);
        preparedStatement.setInt(1, id);
        preparedStatement.executeUpdate();
    }

    private void updateTableView() {
        try {
            String query = "SELECT * FROM view";
            ObservableList<viewmembership> data = getDataFromTable(query);
            tabelMember.setItems(data);
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Gagal mengambil data dari database: " + e.getMessage());
        }
    }
}
