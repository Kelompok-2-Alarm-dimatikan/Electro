package com.electro.repository;
import com.electro.model.Electronic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ElectroRepository extends JpaRepository<Electronic, Long> {
}
