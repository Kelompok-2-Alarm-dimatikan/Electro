package com.electro.model;

import jakarta.persistence.*;

@Entity
@DiscriminatorValue("Kulkas")
public class Kulkas extends Electronic {
    public Kulkas() {}
    public Kulkas(String nama, double harga, int stok, String merk) { super(nama, harga, stok, merk); }

    @Override
    public String getKategori() { return "Kulkas"; }
}