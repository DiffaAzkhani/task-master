package com.taskmaster.taskmaster.service;

import com.taskmaster.taskmaster.entity.Role;
import com.taskmaster.taskmaster.entity.User;
import com.taskmaster.taskmaster.enums.UserRole;
import com.taskmaster.taskmaster.model.request.LoginRequest;
import com.taskmaster.taskmaster.model.request.RegisterRequest;
import com.taskmaster.taskmaster.model.response.RegisterResponse;
import com.taskmaster.taskmaster.model.response.UserResponse;
import com.taskmaster.taskmaster.repository.RoleRepository;
import com.taskmaster.taskmaster.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Slf4j
@AllArgsConstructor
@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    @Override
    @Transactional(readOnly = true)
    public UserResponse login(LoginRequest loginRequest) {
        User user = userRepository.findByUsernameAndPassword(loginRequest.getUsername(), loginRequest.getPassword())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"User Not Found!"));

        if (!user.isEnabled()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"Please verify your email first!");
        }

        return UserServiceImpl.toUserResponse(user);
    }

    @Override
    public RegisterResponse register(RegisterRequest request) {
        if (userRepository.existsByUsernameAndEmail(request.getUsername(), request.getEmail())) {
            log.warn("User with username: {} and email: {} already exist", request.getUsername(), request.getEmail());
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User Already Exists!");
        }

        User user = User.builder()
            .username(request.getUsername())
            .password(request.getPassword())
            .email(request.getEmail())
            .enabled(true)
            .created_at(LocalDateTime.now())
            .updated_at(LocalDateTime.now())
            .build();

        Set<Role> roleSet = new HashSet<>();
        Role userRole = roleRepository.findByName(UserRole.USER);
        roleSet.add(userRole);
        user.setRoles(roleSet);

        userRepository.save(user);
        log.info("Success to register user with username : {}", request.getUsername());

        return toRegisterResponse(user);
    }

    private RegisterResponse toRegisterResponse(User user) {
        return RegisterResponse.builder()
            .email(user.getEmail())
            .username(user.getUsername())
            .build();
    }
}
