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

    public void tambahDevice(String kategori, String nama, double harga) {
        Device device;
        switch (kategori) {
            case "Hp"     -> device = new Hp(nama, harga);
            case "Laptop" -> device = new Laptop(nama, harga);
            default       -> device = new Tablet(nama, harga);
        }
        deviceRepository.save(device);
    }

    public void editDevice(Long id, String nama, double harga) {
        Device device = deviceRepository.findById(id).orElseThrow();
        device.setNama(nama);
        device.setHarga(harga);
        deviceRepository.save(device);
    }

    public void hapusDevice(Long id) {
        deviceRepository.deleteById(id);
    }
}