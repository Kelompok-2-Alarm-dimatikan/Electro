package com.electro.model;
import jakarta.persistence.*;

@Entity
@DiscriminatorValue("Tv")
public class Tv extends Electronic {
    public Tv() {}
    public Tv(String nama, double harga) { super(nama, harga); }

    @Override
    public String getKategori() { return "Tv"; }
}