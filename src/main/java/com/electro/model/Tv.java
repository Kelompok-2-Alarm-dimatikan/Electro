package com.electro.model;
import jakarta.persistence.*;

@Entity
@DiscriminatorValue("Tv")
public class Tv extends Electronic {
    public Tv() {}
    public Tv(String nama, double harga, int stok, String merk) { super(nama, harga, stok, merk); }

    @Override
    public String getKategori() { return "Tv"; }
}