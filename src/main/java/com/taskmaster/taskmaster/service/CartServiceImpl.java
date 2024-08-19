package com.taskmaster.taskmaster.service;

import com.taskmaster.taskmaster.entity.Cart;
import com.taskmaster.taskmaster.entity.CartItem;
import com.taskmaster.taskmaster.entity.Study;
import com.taskmaster.taskmaster.entity.User;
import com.taskmaster.taskmaster.mapper.CartMapper;
import com.taskmaster.taskmaster.model.request.AddCartItemRequest;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

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
    @Transactional
    public void addCart(AddCartItemRequest request) {
        String currentUser = validationService.getCurrentUser();
        validationService.validateUser(currentUser);

        User user = userRepository.findByUsername(currentUser)
            .orElseThrow(() -> {
                log.info("User with username:{}, not found!", currentUser);
                return new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found!");
            });

        Cart cart = cartRepository.findByUser(user)
            .orElseGet(() -> {
                Cart newCart = Cart.builder()
                    .user(user)
                    .build();

                return cartRepository.save(newCart);
            });

        Study study = studyRepository.findByCode(request.getStudyCode())
            .orElseThrow(() -> {
                log.info("Study with code:{}, not found!", request.getStudyCode());
                return new ResponseStatusException(HttpStatus.NOT_FOUND, "Study code not found!");
            });

        if (userRepository.existsByUsernameAndStudies(currentUser, study)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "This study is already in user cart!");
        }

        CartItem cartItem = CartItem.builder()
            .study(study)
            .quantity(1)
            .cart(cart)
            .build();

        cart.getItems().add(cartItem);

        cartRepository.save(cart);
        log.info("Success to save cart!");
    }

    @Override
    @Transactional
    public void deleteCartItem(Long cartItemId) {
        String currentUser = validationService.getCurrentUser();
        validationService.validateUser(currentUser);

        CartItem cartItem = cartItemRepository.findById(cartItemId)
            .orElseThrow(() -> {
                log.info("CartItem in id:{}, not found!", cartItemId);
                return new ResponseStatusException(HttpStatus.NOT_FOUND, "Cart item not found!");
            });

        if (!cartItem.getCart().getUser().getUsername().equals(currentUser)) {
            log.info("User with username: {} does not have the cart item with id: {}", currentUser, cartItemId);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User does not own this cart item!");
        }

        cartItemRepository.delete(cartItem);
        log.info("Success to delete chosen study in user cart!");
    }

    @Override
    @Transactional(readOnly = true)
    public Page<GetCartItemsResponse> getUserCartItems(int page, int size) {
        String currentUser = validationService.getCurrentUser();
        validationService.validateUser(currentUser);

        User user = userRepository.findByUsername(currentUser)
            .orElseThrow(() -> {
                log.info("User with username:{}, not found!", currentUser);
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
