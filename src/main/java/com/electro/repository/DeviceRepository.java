package com.electro.repository;

import com.electro.model.Device;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeviceRepository extends JpaRepository<Device, Long> {
    List<Device> findByHarga(double harga);
    List<Device> findAllByOrderByHargaAsc();
}