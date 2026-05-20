package com.electro.service;

import com.electro.model.*;
import com.electro.repository.DeviceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeviceService {

    @Autowired
    private DeviceRepository deviceRepository;

    public List<Device> getAllDevices() {
        return deviceRepository.findAll();
    }

    public List<Device> searchByHarga(double harga) {
        return deviceRepository.findByHarga(harga);
    }

    public List<Device> sortByHarga() {
        return deviceRepository.findAllByOrderByHargaAsc();
    }

        public void tambahStok(Long id, int jumlah) {
        Device device = deviceRepository.findById(id).orElseThrow();
        device.setStok(device.getStok() + jumlah);
        deviceRepository.save(device);
    }

    public void kurangiStok(Long id, int jumlah) {
        Device device = deviceRepository.findById(id).orElseThrow();
        int stokBaru = device.getStok() - jumlah;
        if (stokBaru < 0) throw new IllegalArgumentException("Stok tidak mencukupi");
        device.setStok(stokBaru);
        deviceRepository.save(device);
    }

    public void tambahDevice(String kategori, String nama, double harga, int stok, String merk) {
        Device device;
        switch (kategori) {
            case "Hp"     -> device = new Hp(nama, harga, stok, merk);
            case "Laptop" -> device = new Laptop(nama, harga, stok, merk);
            default       -> device = new Tablet(nama, harga, stok, merk);
        }
        deviceRepository.save(device);
    }

    public void editDevice(Long id, String nama, double harga, int stok, String merk) {
        Device device = deviceRepository.findById(id).orElseThrow();
        device.setNama(nama);
        device.setHarga(harga);
        device.setStok(stok);
        device.setMerk(merk);
        deviceRepository.save(device);
    }

    public void hapusDevice(Long id) {
        deviceRepository.deleteById(id);
    }
}