package org.example.fix_banget;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Member {
    private final StringProperty nama;
    private final StringProperty perusahaan;
    private final StringProperty harga;
    private final StringProperty deskripsi;
    private int id;

    public Member(int id , String nama, String perusahaan, String harga, String deskripsi) {
        this.id = id;
        this.nama = new SimpleStringProperty(nama);
        this.perusahaan = new SimpleStringProperty(perusahaan);
        this.harga = new SimpleStringProperty(harga);
        this.deskripsi = new SimpleStringProperty(deskripsi);
    }

    public String getNama() {
        return nama.get();
    }

    public StringProperty namaProperty() {
        return nama;
    }

    public String getPerusahaan() {
        return perusahaan.get();
    }

    public StringProperty perusahaanProperty() {
        return perusahaan;
    }

    public String getHarga() {
        return harga.get();
    }

    public void setNama(String nama) {
        this.nama.set(nama);
    }

    public void setPerusahaan(String perusahaan) {
        this.perusahaan.set(perusahaan);
    }

    public void setHarga(String harga) {
        this.harga.set(harga);
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi.set(deskripsi);
    }

    public StringProperty hargaProperty() {
        return harga;
    }

    public String getDeskripsi() {
        return deskripsi.get();
    }

    public StringProperty deskripsiProperty() {
        return deskripsi;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}

