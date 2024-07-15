package com.taskmaster.taskmaster.service;

import com.taskmaster.taskmaster.entity.Role;
import com.taskmaster.taskmaster.entity.User;
import com.taskmaster.taskmaster.enums.UserRole;
import com.taskmaster.taskmaster.mapper.UserMapper;
import com.taskmaster.taskmaster.model.request.DeleteUserRequest;
import com.taskmaster.taskmaster.model.request.RegisterRequest;
import com.taskmaster.taskmaster.model.response.RegisterResponse;
import com.taskmaster.taskmaster.repository.RoleRepository;
import com.taskmaster.taskmaster.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashSet;
import java.util.Set;

@Slf4j
@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    private final UserMapper userMapper;

    @Override
    @Transactional
    public RegisterResponse register(RegisterRequest request) {
        if (userRepository.existsByUsernameAndEmail(request.getUsername(), request.getEmail())) {
            log.warn("User with username: {} and email: {} already exist", request.getUsername(), request.getEmail());
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User Already Exists!");
        }

        User user = User.builder()
            .username(request.getUsername())
            .password(passwordEncoder.encode(request.getPassword()))
            .email(request.getEmail())
            .enabled(true)
            .build();

        Set<Role> roleSet = new HashSet<>();
        Role userRole = roleRepository.findByName(UserRole.USER);
        roleSet.add(userRole);
        user.setRoles(roleSet);

        userRepository.save(user);
        log.info("Success to register user with username : {}", request.getUsername());

        return userMapper.toRegisterResponse(user);
    }

    @Override
    @Transactional
    public void deleteUserAccount(DeleteUserRequest request) {
        User user = userRepository.findByUsernameOrEmail(request.getUsernameOrEmail(), request.getUsernameOrEmail())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"User or email not found"));

        if (!"DELETE".equals(request.getConfirmationWord())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Confirmation password is invalid");
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid password");
        }

        userRepository.delete(user);
    }

}
