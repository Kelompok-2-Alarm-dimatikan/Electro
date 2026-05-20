package com.electro.model;
import jakarta.persistence.*;

@Entity
@DiscriminatorValue("Hp")
public class Hp extends Electronic {
    public Hp() {}
    public Hp(String nama, double harga) { super(nama, harga); }

    @Override
    public String getKategori() { return "Hp"; }
}