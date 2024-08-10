package com.taskmaster.taskmaster.service;

import com.taskmaster.taskmaster.entity.Role;
import com.taskmaster.taskmaster.entity.Study;
import com.taskmaster.taskmaster.entity.User;
import com.taskmaster.taskmaster.enums.UserRole;
import com.taskmaster.taskmaster.mapper.StudyMapper;
import com.taskmaster.taskmaster.mapper.UserMapper;
import com.taskmaster.taskmaster.model.request.RegisterRequest;
import com.taskmaster.taskmaster.model.request.UpdateUserProfileRequest;
import com.taskmaster.taskmaster.model.response.GetAllEnrolledUSerStudyResponse;
import com.taskmaster.taskmaster.model.response.GetAllUsersResponse;
import com.taskmaster.taskmaster.model.response.GetUserForAdminResponse;
import com.taskmaster.taskmaster.model.response.GetUserProfileResponse;
import com.taskmaster.taskmaster.model.response.RegisterResponse;
import com.taskmaster.taskmaster.model.response.UpdateUserProfileResponse;
import com.taskmaster.taskmaster.repository.RoleRepository;
import com.taskmaster.taskmaster.repository.StudyRepository;
import com.taskmaster.taskmaster.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final StudyRepository studyRepository;

    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    private final UserMapper userMapper;

    private final StudyMapper studyMapper;

    private final ValidationService validationService;

    @Override
    @Transactional
    public RegisterResponse register(RegisterRequest request) {
        if (userRepository.existsByUsernameAndEmail(request.getUsername(), request.getEmail())) {
            log.warn("User with username: {} and email: {} already exist", request.getUsername(), request.getEmail());
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User Already Exists!");
        }

        User user = User.builder()
            .username(request.getUsername())
            .firstName(request.getFirstName())
            .lastName(request.getLastName())
            .password(passwordEncoder.encode(request.getPassword()))
            .email(request.getEmail())
            .phone(request.getPhone())
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
    public void deleteUserAccountForAdmin(String username) {
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"User or email not found"));

        userRepository.delete(user);
    }

    @Override
    @Transactional
    public void deleteUserAccountForUser(String password) {
        String currentUser = validationService.getCurrentUser();
        validationService.validateUser(currentUser);

        User user = userRepository.findByUsername(currentUser)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"User not found!"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid password");
        }

        userRepository.delete(user);
    }

    @Override
    @Transactional
    public UpdateUserProfileResponse updateUserProfileForAdmin(Long userId, UpdateUserProfileRequest request) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> {
                log.info("User with id:{} not found!", userId);
                return new ResponseStatusException(HttpStatus.NOT_FOUND, "username not found!");
            });

        updateUserProperties(user, request);

        userRepository.save(user);
        log.info("Success to update user!");

        return userMapper.toUpdateUserResponse(user);
    }

    @Override
    @Transactional
    public UpdateUserProfileResponse updateUserProfileForUser(UpdateUserProfileRequest request) {
        String currentUser = validationService.getCurrentUser();
        validationService.validateUser(currentUser);

        User user = userRepository.findByUsername(currentUser)
            .orElseThrow(() -> {
                log.info("User with username:{} not found!", currentUser);
                return new ResponseStatusException(HttpStatus.NOT_FOUND, "username not found!");
            });

        updateUserProperties(user, request);

        userRepository.save(user);
        log.info("Success to update user!");

        return userMapper.toUpdateUserResponse(user);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<GetAllUsersResponse> getAllUsers(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<User> userPage = userRepository.findAll(pageRequest);

        List<GetAllUsersResponse> getAllUsersResponses = userPage.getContent().stream()
            .map(userMapper::toGetAllUsersResponse)
            .collect(Collectors.toList());

        log.info("Success to get all users!");

        return new PageImpl<>(getAllUsersResponses, pageRequest, userPage.getTotalElements());
    }

    @Override
    @Transactional(readOnly = true)
    public GetUserForAdminResponse getUserForAdmin(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> {
                log.info("User with id:{}, not found!", userId);
                return new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found!");
            });

        return userMapper.toGetUserForAdminResponse(user);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<GetAllEnrolledUSerStudyResponse> getEnrolledUserStudy(int page, int size) {
        String currentUser = validationService.getCurrentUser();
        validationService.validateUser(currentUser);

        if (!userRepository.existsByUsername(currentUser)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found!");
        }

        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Study> studyPage = studyRepository.findByUsers_Username(currentUser, pageRequest);

        List<GetAllEnrolledUSerStudyResponse> allEnrolledUSerStudyList = studyPage.getContent().stream()
            .map(studyMapper::toGetEnrolledUserStudyResponse)
            .collect(Collectors.toList());

        log.info("Success to get all user enrolled study!");

        return new PageImpl<>(allEnrolledUSerStudyList, pageRequest, studyPage.getTotalElements());
    }

    @Override
    @Transactional(readOnly = true)
    public GetUserProfileResponse getUserProfile() {
        String currentUser = validationService.getCurrentUser();
        validationService.validateUser(currentUser);

        User user = userRepository.findByUsername(currentUser)
            .orElseThrow(() -> {
                log.info("User with username:{}, not found!", currentUser);
                return new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found!");
            });

        return userMapper.toGetUserProfile(user);
    }

    private void updateUserProperties(User user, UpdateUserProfileRequest request) {
        if (Objects.nonNull(request.getEmail())) {
            if (userRepository.existsByEmail(request.getEmail())) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already exists!");
            }

            user.setEmail(request.getEmail());
        }

        if (Objects.nonNull(request.getFirstName())) {
            user.setFirstName(request.getFirstName());
        }

        if (Objects.nonNull(request.getLastName())) {
            user.setLastName(request.getLastName());
        }

        if (Objects.nonNull(request.getPhone())) {
            if (user.getPhone().equals(request.getPhone())) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Phone number cannot be the same as before!");
            }

            if (userRepository.existsByPhone(request.getPhone())) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Phone number already exists!");
            }

            user.setPhone(request.getPhone());
        }

        if (Objects.nonNull(request.getProfileImage())) {
            user.setProfileImage(request.getProfileImage());
        }
    }

}
