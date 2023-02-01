package dev.router.sisggarapi.core.service;

import dev.router.sisggarapi.adapter.request.authentication.UserRequest;
import dev.router.sisggarapi.core.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService {
    List<User> findAll();
    Optional<User> findById(UUID userId);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    Page<User> findAll(Specification<User> spec, Pageable pageable);
    User saveUser(UserRequest user);
    void deleteUser(UUID userId);
    User updateUser(User user);
    void updatePassword(UUID userId,UserRequest user);
    User updateImage(User user);

}
