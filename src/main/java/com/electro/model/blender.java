package com.electro.model;
import jakarta.persistence.*;

@Entity
@DiscriminatorValue("Blender")
public class Blender extends Electronic {
    public Blender() {}
    public Blender(String nama, double harga, int stok, String merk) { super(nama, harga, stok, merk); }

    @Override
    public String getKategori() { return "Blender"; }
}