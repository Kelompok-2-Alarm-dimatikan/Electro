package com.electro.model;
import jakarta.persistence.*;

@Entity
@DiscriminatorValue("Blender")
public class Blender extends Electronic {
    public Blender() {}
    public Blender(String nama, double harga) { super(nama, harga); }

    @Override
    public String getKategori() { return "Blender"; }
}