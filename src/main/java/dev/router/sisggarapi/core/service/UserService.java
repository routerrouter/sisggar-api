package dev.router.sisggarapi.core.service;

import dev.router.sisggarapi.core.domain.User;
import dev.router.sisggarapi.adapter.dto.authentication.UserDto;
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
    UserDto saveUser(UserDto userDto);
    void deleteUser(UUID userId);
    UserDto updateUser(UserDto userDto);
    void updatePassword(UserDto userDto);
    UserDto updateImage(UserDto userDto);

}
