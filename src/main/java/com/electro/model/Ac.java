package com.electro.model;

import jakarta.persistence.*;

@Entity
@DiscriminatorValue("Ac")
public class Ac extends Electronic {
    public Ac() {}
    public Ac(String nama, double harga, int stok, String merk) { super(nama, harga, stok, merk); }

    @Override
    public String getKategori() { return "Ac"; }
}