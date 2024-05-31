package org.example.fix_banget;

import java.sql.Date;

public class Membership {
    private int id;
    private String namaMembership;
    private String perusahaan;
    private String harga;
    private String deskripsi;
    private Date startDate;
    private Date endDate;

    public Membership(int id, String namaMembership, String perusahaan, String harga, String deskripsi, Date startDate, Date endDate) {
        this.id = id;
        this.namaMembership = namaMembership;
        this.perusahaan = perusahaan;
        this.harga = harga;
        this.deskripsi = deskripsi;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public int getId() {
        return id;
    }

    public String getNamaMembership() {
        return namaMembership;
    }

    public String getPerusahaan() {
        return perusahaan;
    }

    public String getHarga() {
        return harga;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }
}
