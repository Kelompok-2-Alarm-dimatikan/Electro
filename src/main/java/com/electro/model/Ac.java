package com.electro.model;
import jakarta.persistence.*;

@Entity
@DiscriminatorValue("Ac")
public class Ac extends Electronic {
    public Ac() {}
    public Ac(String nama, double harga) { super(nama, harga); }

    @Override
    public String getKategori() { return "Ac"; }
}