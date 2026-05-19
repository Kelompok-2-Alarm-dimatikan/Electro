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

    public void tambahElectro(String kategori, String nama, double harga) {
        Electronic electro;
        switch (kategori) {
            case "Hp"     -> electro = new Hp(nama, harga);
            case "Laptop" -> electro = new Laptop(nama, harga);
            default       -> electro = new Tablet(nama, harga);
        }
        electroRepository.save(electro);
    }

    public void editElectro(Long id, String nama, double harga) {
        Electronic electro = electroRepository.findById(id).orElseThrow();
        electro.setNama(nama);
        electro.setHarga(harga);
        electroRepository.save(electro);
    }

    public void hapusElectro(Long id) {
        electroRepository.deleteById(id);
    }
}