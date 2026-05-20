package com.electro.model;
import jakarta.persistence.*;

@Entity
@DiscriminatorValue("Kulkas")
public class Kulkas extends Electronic {
    public Kulkas() {}
    public Kulkas(String nama, double harga) { super(nama, harga); }

    @Override
    public String getKategori() { return "Kulkas"; }
}