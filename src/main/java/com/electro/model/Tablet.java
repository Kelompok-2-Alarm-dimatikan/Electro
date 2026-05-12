package com.electro.model;

import jakarta.persistence.*;

@Entity
@DiscriminatorValue("Tablet")
public class Tablet extends Device {
    public Tablet() {}
    public Tablet(String nama, double harga) { super(nama, harga); }

    @Override
    public String getKategori() { return "Tablet"; }
}