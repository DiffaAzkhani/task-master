package com.taskmaster.taskmaster.repository;

import com.taskmaster.taskmaster.entity.Order;
import com.taskmaster.taskmaster.entity.Study;
import com.taskmaster.taskmaster.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    boolean existsByUserAndStudy(User user, Study study);

}
