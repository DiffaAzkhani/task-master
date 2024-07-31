package com.taskmaster.taskmaster.service;

import com.taskmaster.taskmaster.entity.Cart;
import com.taskmaster.taskmaster.entity.CartItem;
import com.taskmaster.taskmaster.entity.Study;
import com.taskmaster.taskmaster.entity.User;
import com.taskmaster.taskmaster.model.request.AddCartRequest;
import com.taskmaster.taskmaster.model.request.CartItemRequest;
import com.taskmaster.taskmaster.repository.CartRepository;
import com.taskmaster.taskmaster.repository.StudyRepository;
import com.taskmaster.taskmaster.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class CartServiceImpl implements CartService{

    private UserRepository userRepository;

    private CartRepository cartRepository;

    private StudyRepository studyRepository;

    @Override
    public void addCart(AddCartRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
            .orElseThrow(() -> {
                log.info("User with username:{}, not found!", request.getUsername());
                return new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found!");
            });

        Cart cart = Cart.builder()
            .user(user)
            .build();

        List<CartItem> cartItems = new ArrayList<>();

        for (CartItemRequest cartItemsRequest : request.getItems()) {
            Study study = studyRepository.findByCode(cartItemsRequest.getStudyCode())
                .orElseThrow(() -> {
                    log.info("Study with code:{}, not found!", cartItemsRequest.getStudyCode());
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, "Study code not found!");
                });

            if (userRepository.existsByUsernameAndStudies(request.getUsername(), study)) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "This study is already in user cart!");
            }

            CartItem cartItem = CartItem.builder()
                .study(study)
                .quantity(1)
                .build();

            cartItem.setCart(cart);
            cartItems.add(cartItem);
        }

        cart.setItems(cartItems);

        cartRepository.save(cart);
        log.info("Success to save cart!");
    }

}
