package com.taskmaster.taskmaster.repository;

import com.taskmaster.taskmaster.entity.Cart;
import com.taskmaster.taskmaster.entity.CartItem;
import com.taskmaster.taskmaster.entity.Study;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    Optional<CartItem> findByCartAndStudy(Cart cart, Study study);

}
