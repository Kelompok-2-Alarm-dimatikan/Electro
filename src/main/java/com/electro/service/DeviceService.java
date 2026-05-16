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

   
    public List<Device> searchDevices(String nama) {
        List<Device> devices;
        if (nama != null && !nama.isBlank()) {
            devices = deviceRepository.findByNamaContainingIgnoreCase(nama.trim());
        } else {
            devices = deviceRepository.findAll();
        }
        return devices;
    }

  
    public List<Device> sortByHargaAsc() {
        return deviceRepository.findAllByOrderByHargaAsc();
    }

    public List<Device> sortByHargaDesc() {
        return deviceRepository.findAllByOrderByHargaDesc();
    }

    
    public Device tambahDevice(String kategori, String nama, double harga) {

        if (nama == null || nama.isBlank()) {
            throw new IllegalArgumentException("Nama device tidak boleh kosong");
        }

        if (harga <= 0) {
            throw new IllegalArgumentException("Harga harus lebih besar dari 0");
        }

        Device device = switch (kategori) {
            case "Hp"     -> new Hp(nama.trim(), harga);
            case "Laptop" -> new Laptop(nama.trim(), harga);
            default       -> new Tablet(nama.trim(), harga);
        };
        return deviceRepository.save(device);
    }

    public void editDevice(Long id, String nama, double harga) {
        
        if (nama == null || nama.isBlank()) {
            throw new IllegalArgumentException("Nama device tidak boleh kosong");
        }

        if (harga <= 0) {
            throw new IllegalArgumentException("Harga harus lebih besar dari 0");
        }

        Device device = deviceRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Device dengan id " + id + " tidak ditemukan"));
        device.setNama(nama.trim());
        device.setHarga(harga);
        deviceRepository.save(device);
    }

    
    public void hapusDevice(Long id) {
        deviceRepository.deleteById(id);
    }
}