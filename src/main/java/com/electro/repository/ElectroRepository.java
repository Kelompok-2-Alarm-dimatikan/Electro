package com.electro.repository;

import com.electro.model.Electronic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ElectroRepository extends JpaRepository<Electronic, Long> {
    List<Electronic> findByHarga(double harga);
    List<Electronic> findAllByOrderByHargaAsc();
}