package dev.router.sisggarapi.adapter.controller;


import com.fasterxml.jackson.annotation.JsonView;
import dev.router.sisggarapi.adapter.config.secutity.JwtProvider;
import dev.router.sisggarapi.adapter.mapper.UserMapper;
import dev.router.sisggarapi.adapter.request.authentication.JwtRequest;
import dev.router.sisggarapi.adapter.request.authentication.LoginRequest;
import dev.router.sisggarapi.adapter.request.authentication.UserRequest;
import dev.router.sisggarapi.adapter.response.authentication.UserResponse;
import dev.router.sisggarapi.core.service.RoleService;
import dev.router.sisggarapi.core.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.stream.Stream;


@Log4j2
@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "The User API. Contains all the operations that can be performed on a user.")
public class AuthenticationController {


    private final UserService userService;
    private final RoleService roleService;
    private final UserMapper mapper;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    JwtProvider jwtProvider;

    @Autowired
    AuthenticationManager authenticationManager;


    @PostMapping("/login")
    public ResponseEntity<JwtRequest> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtProvider.generateJwt(authentication);
        return ResponseEntity.ok(new JwtRequest(jwt));
    }


    @PostMapping("/logout")
    public ResponseEntity<UserResponse> registerUser(@RequestBody @Validated(UserRequest.UserView.RegistrationPost.class)
                                                    @JsonView(UserRequest.UserView.RegistrationPost.class) UserRequest userRequest){
        log.debug("POST registerUser userDto received {} ", userRequest.toString());
        var user = userService.saveUser(userRequest);
        var response = Stream.of(user)
                .map(mapper::toUserResponse)
                .findFirst()
                .get();
        log.debug("POST createUser userId saved {} ", response.getUserId());
        log.info("User create successfully userId {} ", response.getUserId());
        return  ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
