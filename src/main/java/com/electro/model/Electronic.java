package com.electro.model;
import jakarta.persistence.*;

@Entity
@Table(name = "electronic")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "kategori", discriminatorType = DiscriminatorType.STRING)
public abstract class Electronic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nama;
    private double harga;
    private int stok;
    private String merk;

    @Column(length = 1000)
    private String imageUrl;

    @Column(length = 3000)
    private String deskripsi;

    @Column(length = 2000)
    private String spesifikasi;

    public Electronic() {}

    public Electronic(String nama, double harga, int stok, String merk) {
        this.nama = nama;
        this.harga = harga;
        this.stok = stok;
        this.merk = merk;
    }

    public Long getId() { return id; }
    public String getNama() { return nama; }
    public void setNama(String nama) { this.nama = nama; }
    public double getHarga() { return harga; }
    public void setHarga(double harga) { this.harga = harga; }
    public int getStok() { return stok; }
    public void setStok(int stok) { this.stok = stok; }
    public String getMerk() { return merk; }
    public void setMerk(String merk) { this.merk = merk; }
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public String getDeskripsi() { return deskripsi; }
    public void setDeskripsi(String deskripsi) { this.deskripsi = deskripsi; }
    public String getSpesifikasi() { return spesifikasi; }
    public void setSpesifikasi(String spesifikasi) { this.spesifikasi = spesifikasi; }

    public abstract String getKategori();
}