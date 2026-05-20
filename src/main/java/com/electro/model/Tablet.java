package com.electro.model;

import jakarta.persistence.*;

@Entity
@DiscriminatorValue("Tablet")
public class Tablet extends Device {
    public Tablet() {}
    public Tablet(String nama, double harga, int stok, String merk) { super(nama, harga, stok, merk); }

    @Override
    public String getKategori() { return "Tablet"; }
}