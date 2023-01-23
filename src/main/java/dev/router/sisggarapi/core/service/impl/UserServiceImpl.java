package dev.router.sisggarapi.core.service.impl;

import dev.router.sisggarapi.core.domain.Role;
import dev.router.sisggarapi.core.domain.User;
import dev.router.sisggarapi.adapter.dto.authentication.UserDto;
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
    public UserDto saveUser(UserDto userDto) {

        log.debug("POST registerUser userDto received {} ", userDto.toString());
        if(userRepository.existsByUsername(userDto.getUsername())){
            log.warn("Username {} is Already Taken ", userDto.getUsername());
            new ResponseStatusException(HttpStatus.CONFLICT,"Error: Username is Already Taken!");
        }
        if(userRepository.existsByEmail(userDto.getEmail())){
            log.warn("Email {} is Already Taken ", userDto.getEmail());
            new ResponseStatusException(HttpStatus.CONFLICT,"Error: Email is Already Taken!");
        }
        Role role = roleRepository.findByRoleName(RoleType.ROLE_ADMIN)
                .orElseThrow(() -> new RuntimeException("Error: Role is Not Found."));

        userDto.setPassword(userDto.getPassword());
        var userModel = new User();
        BeanUtils.copyProperties(userDto, userModel);
        userModel.setUserStatus(Status.ACTIVE);
        userModel.setUserType(UserType.ADMIN);
        userModel.setCreationDate(LocalDateTime.now(ZoneId.of("UTC")));
        userModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
        userModel.getRoles().add(role);
        userRepository.save(userModel);
        log.debug("POST registerUser userId saved {} ", userModel.getUserId());
        log.info("User saved successfully userId {} ", userModel.getUserId());
        BeanUtils.copyProperties(userModel,userDto);
        return  userDto;
    }

    @Override
    public void deleteUser(UUID userId) {
        var userModel = userRepository.findById(userId)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"User not found."));
        userModel.setUserStatus(Status.INACTIVE);
        userRepository.save(userModel);
    }

    @Override
    public UserDto updateUser(UserDto userDto) {
        var userModel = userRepository.findById(userDto.getUserId())
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"User not found."));
        BeanUtils.copyProperties(userDto,userModel);
        userModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
        userModel = userRepository.save(userModel);
        BeanUtils.copyProperties(userModel,userDto);
        return  userDto;
    }

    @Override
    public void updatePassword(UserDto userDto) {
        var userModelOptional = userRepository.findById(userDto.getUserId());
        if(!userModelOptional.isPresent()){
            new ResponseStatusException(HttpStatus.CONFLICT,"User not found");
        } if(!userModelOptional.get().getPassword().equals(userDto.getOldPassword())){
            new ResponseStatusException(HttpStatus.CONFLICT,"Error: Mismatched old password!");
        } else {
            var userModel = userModelOptional.get();
            userModel.setPassword(userDto.getPassword());
            userModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
            userRepository.save(userModel);
        }
    }

    @Override
    public UserDto updateImage(UserDto userDto) {
        var userModel = userRepository.findById(userDto.getUserId())
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.CONFLICT,"User not found") );

        userModel.setImageUrl(userDto.getImageUrl());
        userModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
        userModel = userRepository.save(userModel);
        BeanUtils.copyProperties(userModel,userDto);
        return userDto;
    }
}
