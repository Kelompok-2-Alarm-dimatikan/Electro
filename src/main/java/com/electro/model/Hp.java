package com.electro.model;
import jakarta.persistence.*;

@Entity
@DiscriminatorValue("Hp")
public class Hp extends Electronic {
    public Hp() {}
    public Hp(String nama, double harga, int stok, String merk) { super(nama, harga, stok, merk); }

    @Override
    public String getKategori() { return "Hp"; }
}