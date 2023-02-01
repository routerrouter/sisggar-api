package dev.router.sisggarapi.core.service.impl;

import dev.router.sisggarapi.adapter.mapper.UserMapper;
import dev.router.sisggarapi.core.domain.Role;
import dev.router.sisggarapi.core.domain.User;
import dev.router.sisggarapi.adapter.request.authentication.UserRequest;
import dev.router.sisggarapi.core.domain.enums.RoleType;
import dev.router.sisggarapi.core.domain.enums.Status;
import dev.router.sisggarapi.core.domain.enums.UserType;
import dev.router.sisggarapi.core.repository.RoleRepository;
import dev.router.sisggarapi.core.repository.UserRepository;
import dev.router.sisggarapi.core.service.UserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Log4j2
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    UserMapper mapper;


    @Override
    public List<User> findAll() {
        return null;
    }

    @Override
    public Optional<User> findById(UUID userId) {
        return Optional.empty();
    }


    @Override
    public boolean existsByUsername(String username) {
        return false;
    }

    @Override
    public boolean existsByEmail(String email) {
        return false;
    }

    @Override
    public Page<User> findAll(Specification<User> spec, Pageable pageable) {
        return null;
    }

    @Override
    public User saveUser(UserRequest userRequest) {

        log.debug("POST registerUser userDto received {} ", userRequest.toString());
        if(userRepository.existsByUsername(userRequest.getUsername())){
            log.warn("Username {} is Already Taken ", userRequest.getUsername());
            new ResponseStatusException(HttpStatus.CONFLICT,"Error: Username is Already Taken!");
        }
        if(userRepository.existsByEmail(userRequest.getEmail())){
            log.warn("Email {} is Already Taken ", userRequest.getEmail());
            new ResponseStatusException(HttpStatus.CONFLICT,"Error: Email is Already Taken!");
        }
        Role role = roleRepository.findByRoleName(userRequest.getRoleType())
                .orElseThrow(() -> new RuntimeException("Error: Role is Not Found."));
        var user = mapper.toUser(userRequest);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setUserStatus(Status.ACTIVE);
        user.setUserType(user.getUserType());
        user.setCreationDate(LocalDateTime.now(ZoneId.of("UTC")));
        user.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
        user.getRoles().add(role);
        userRepository.save(user);
        log.debug("POST registerUser userId saved {} ", user.getUserId());
        log.info("User saved successfully userId {} ", user.getUserId());
        return user;
    }

    @Override
    public void deleteUser(UUID userId) {
        var userModel = userRepository.findById(userId)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"User not found."));
        userModel.setUserStatus(Status.INACTIVE);
        userRepository.save(userModel);
    }

    @Override
    public User updateUser(User userRequest) {
        var userModel = userRepository.findById(userRequest.getUserId())
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"User not found."));
        BeanUtils.copyProperties(userRequest,userModel);
        userModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
        userModel = userRepository.save(userModel);
        BeanUtils.copyProperties(userModel, userRequest);
        return userRequest;
    }

    @Override
    public void updatePassword(UUID userId,UserRequest userRequest) {
        var optUser = userRepository.findById(userId);
        if(!optUser.isPresent()){
            new ResponseStatusException(HttpStatus.CONFLICT,"User not found");
        } if(!optUser.get().getPassword().equals(userRequest.getOldPassword())){
            new ResponseStatusException(HttpStatus.CONFLICT,"Error: Mismatched old password!");
        } else {
            var user = optUser.get();
            user.setPassword(userRequest.getPassword());
            user.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
            userRepository.save(user);
        }
    }

    @Override
    public User updateImage(User userRequest) {
        var userModel = userRepository.findById(userRequest.getUserId())
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.CONFLICT,"User not found") );

        userModel.setImageUrl(userRequest.getImageUrl());
        userModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
        userModel = userRepository.save(userModel);
        BeanUtils.copyProperties(userModel, userRequest);
        return userRequest;
    }
}
