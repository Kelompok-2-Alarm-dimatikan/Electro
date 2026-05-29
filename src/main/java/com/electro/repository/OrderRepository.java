package com.electro.repository;
import com.electro.model.Order;
import com.electro.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findAllByOrderByTanggalDesc();
    List<Order> findByUser(User user);
}