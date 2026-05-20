package com.electro.service;

import com.electro.model.*;
import com.electro.repository.ElectroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ElectroService {

    @Autowired
    private ElectroRepository electroRepository;

    public List<Electronic> getAllElectro() {
        return electroRepository.findAll();
    }

    public List<Electronic> searchByHarga(double harga) {
        return electroRepository.findByHarga(harga);
    }

    public List<Electronic> sortByHarga() {
        return electroRepository.findAllByOrderByHargaAsc();
    }

    public void tambahStok(Long id, int jumlah) {
        Electronic electro = electroRepository.findById(id).orElseThrow();
        electro.setStok(electro.getStok() + jumlah);
        electroRepository.save(electro);
    }

    public void kurangiStok(Long id, int jumlah) {
        Electronic electro = electroRepository.findById(id).orElseThrow();
        int stokBaru = electro.getStok() - jumlah;
        if (stokBaru < 0) throw new IllegalArgumentException("Stok tidak mencukupi");
        electro.setStok(stokBaru);
        electroRepository.save(electro);
    }

    public void tambahElectro(String kategori, String nama, double harga, int stok, String merk) {
        Electronic electro;
        switch (kategori) {
            case "Hp"     -> electro = new Hp(nama, harga, stok, merk);
            case "Laptop" -> electro = new Laptop(nama, harga, stok, merk);
            case "Tablet" -> electro = new Tablet(nama, harga, stok, merk);
            case "Tv"     -> electro = new Tv(nama, harga, stok, merk);
            case "Ac"     -> electro = new Ac(nama, harga, stok, merk);
            case "Blender"-> electro = new Blender(nama, harga, stok, merk);
            case "Kulkas" -> electro = new Kulkas(nama, harga, stok, merk);
            default       -> electro = new Hp(nama, harga, stok, merk);
        }
        electroRepository.save(electro);
    }

    public void editElectro(Long id, String nama, double harga, int stok, String merk) {
        Electronic electro = electroRepository.findById(id).orElseThrow();
        electro.setNama(nama);
        electro.setHarga(harga);
        electro.setStok(stok);
        electro.setMerk(merk);
        electroRepository.save(electro);
    }

    public void hapusElectro(Long id) {
        electroRepository.deleteById(id);
    }
}
