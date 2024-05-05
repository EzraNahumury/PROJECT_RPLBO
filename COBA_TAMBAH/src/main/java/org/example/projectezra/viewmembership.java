package org.example.projectezra;

import java.util.Date;

public class viewmembership {
    private int id; // Sesuaikan dengan nama kolom yang ada di tabel view
    private String namaVendor;
    private Date Start;
    private Date End;
    private String namaMembership;

    public viewmembership(int id, String namaVendor, Date Start, Date End, String namaMembership) {
        this.id = id;
        this.namaVendor = namaVendor;
        this.Start = Start;
        this.End = End;
        this.namaMembership = namaMembership;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNamaVendor() {
        return namaVendor;
    }

    public void setNamaVendor(String namaVendor) {
        this.namaVendor = namaVendor;
    }

    public Date getStart() {
        return Start;
    }

    public void setStart(Date Start) {
        this.Start = Start;
    }

    public Date getEnd() {
        return End;
    }

    public void setEnd(Date End) {
        this.End = End;
    }

    public String getNamaMembership() {
        return namaMembership;
    }

    public void setNamaMembership(String namaMembership) {
        this.namaMembership = namaMembership;
    }
}
