package com.electro.model;
import jakarta.persistence.*;

@Entity
@DiscriminatorValue("Headphone")
public class Headphone extends Electronic {
    public Headphone() {}
    public Headphone(String nama, double harga, int stok, String merk) { super(nama, harga, stok, merk); }

    @Override
    public String getKategori() { return "Headphone"; }
}
