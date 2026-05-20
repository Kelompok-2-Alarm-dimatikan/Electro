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

    public Electronic() {}

    public Electronic(String nama, double harga) {
        this.nama = nama;
        this.harga = harga;
    }

    public Long getId() { return id; }
    public String getNama() { return nama; }
    public void setNama(String nama) { this.nama = nama; }
    public double getHarga() { return harga; }
    public void setHarga(double harga) { this.harga = harga; }

    public abstract String getKategori();
}