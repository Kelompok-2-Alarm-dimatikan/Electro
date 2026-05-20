package com.electro.model;

import jakarta.persistence.*;

@Entity
@DiscriminatorValue("Laptop")
public class Laptop extends Device {
    public Laptop() {}
    public Laptop(String nama, double harga, int stok, String merk) { super(nama, harga, stok, merk); }

    @Override
    public String getKategori() { return "Laptop"; }
}