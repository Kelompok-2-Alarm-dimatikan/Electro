package com.electro.repository;
import com.electro.model.Complaint;
import com.electro.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ComplaintRepository extends JpaRepository<Complaint, Long> {
    List<Complaint> findByUserOrderByTanggalDesc(User user);
    List<Complaint> findAllByOrderByTanggalDesc();
    void deleteByUser(User user);
}