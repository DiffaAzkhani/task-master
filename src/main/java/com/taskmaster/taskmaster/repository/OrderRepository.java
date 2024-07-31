package com.taskmaster.taskmaster.repository;

import com.taskmaster.taskmaster.entity.Order;
import com.taskmaster.taskmaster.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, String> {

    Optional<Order> findByUserAndId(User user, String orderCode);

    Page<Order> findByUser(User user, Pageable pageable);

}
