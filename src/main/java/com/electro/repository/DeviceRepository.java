package com.electro.repository;

import com.electro.model.Device;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface DeviceRepository extends JpaRepository<Device, Long> {
    List<Device> findByNamaContainingIgnoreCase(String nama);
    List<Device> findAllByOrderByHargaAsc();
    List<Device> findAllByOrderByHargaDesc();
}