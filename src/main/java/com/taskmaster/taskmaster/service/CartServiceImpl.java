package com.taskmaster.taskmaster.service;

import com.taskmaster.taskmaster.entity.Cart;
import com.taskmaster.taskmaster.entity.CartItem;
import com.taskmaster.taskmaster.entity.Study;
import com.taskmaster.taskmaster.entity.User;
import com.taskmaster.taskmaster.mapper.CartMapper;
import com.taskmaster.taskmaster.model.request.AddCartRequest;
import com.taskmaster.taskmaster.model.request.CartItemRequest;
import com.taskmaster.taskmaster.model.response.GetCartItemsResponse;
import com.taskmaster.taskmaster.repository.CartItemRepository;
import com.taskmaster.taskmaster.repository.CartRepository;
import com.taskmaster.taskmaster.repository.StudyRepository;
import com.taskmaster.taskmaster.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class CartServiceImpl implements CartService{

    private UserRepository userRepository;

    private CartRepository cartRepository;

    private StudyRepository studyRepository;

    private CartItemRepository cartItemRepository;

    private CartMapper cartMapper;

    private ValidationService validationService;

    @Override
    public void addCart(AddCartRequest request) {
        validationService.validateUser(request.getUsername());

        User user = userRepository.findByUsername(request.getUsername())
            .orElseThrow(() -> {
                log.info("User with username:{}, not found!", request.getUsername());
                return new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found!");
            });

        Cart cart = cartRepository.findByUser(user)
            .orElseGet(() -> {
                Cart newCart = Cart.builder()
                    .user(user)
                    .build();

                return cartRepository.save(newCart);
            });

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

        cart.getItems().addAll(cartItems);

        cartRepository.save(cart);
        log.info("Success to save cart!");
    }

    @Override
    public void deleteCartItem(String username, String studyCode) {
        validationService.validateUser(username);

        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> {
                log.info("User with username:{}, user not found!", username);
                return new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found!");
            });

        Study study = studyRepository.findByCode(studyCode)
            .orElseThrow(() -> {
                log.info("Study with code:{}, not found!", studyCode);
                return new ResponseStatusException(HttpStatus.NOT_FOUND, "Study not found!");
            });

        Cart cart = cartRepository.findByUser(user)
            .orElseThrow(() -> {
                log.info("Cart with user:{}, not found!", user.getUsername());
                return new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found in cart!");
            });

        CartItem cartItem = cartItemRepository.findByCartAndStudy(cart, study)
            .orElseThrow(() -> {
                log.info("Cart item with cartId:{}, and study:{}, not found!", cart.getId(), study.getCode());
                return new ResponseStatusException(HttpStatus.NOT_FOUND, "Cart items not found!");
            });

        cartItemRepository.delete(cartItem);
        log.info("Success to delete chosen study in user cart!");
    }

    @Override
    public Page<GetCartItemsResponse> getAllCartItems(String username, int page, int size) {
        validationService.validateUser(username);

        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> {
                log.info("User with username:{}, not found!", username);
                return new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found!");
            });

        PageRequest pageRequest = PageRequest.of(page, size);
        Page<CartItem> cartItemPage = cartItemRepository.findByCart_User(user, pageRequest);

        List<GetCartItemsResponse> getCartItemsResponses = cartItemPage.getContent()
            .stream()
            .map(cartMapper::toCartItemsResponse)
            .collect(Collectors.toList());

        return new PageImpl<>(getCartItemsResponses, pageRequest, cartItemPage.getTotalElements());
    }

}
