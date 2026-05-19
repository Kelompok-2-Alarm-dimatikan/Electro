package com.electro.model;

import jakarta.persistence.*;

@Entity
@DiscriminatorValue("Laptop")
public class Laptop extends Electronic {
    public Laptop() {}
    public Laptop(String nama, double harga) { super(nama, harga); }

    @Override
    public String getKategori() { return "Laptop"; }
}