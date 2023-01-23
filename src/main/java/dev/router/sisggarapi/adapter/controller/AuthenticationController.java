package dev.router.sisggarapi.adapter.controller;


import com.fasterxml.jackson.annotation.JsonView;
import dev.router.sisggarapi.adapter.dto.authentication.UserDto;
import dev.router.sisggarapi.core.service.RoleService;
import dev.router.sisggarapi.core.service.UserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/*import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;*/

@Log4j2
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/api/auth/v1")
public class AuthenticationController {

    @Autowired
    UserService userService;

    @Autowired
    RoleService roleService;

    /*@Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    JwtProvider jwtProvider;

    @Autowired
    AuthenticationManager authenticationManager;*/


    /*@PostMapping("/login")
    public ResponseEntity<JwtDto> authenticateUser(@Valid @RequestBody LoginDto loginDto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtProvider.generateJwt(authentication);
        return ResponseEntity.ok(new JwtDto(jwt));
    }*/

    @GetMapping("/")
    public String index(){
        log.trace("TRACE");
        log.debug("DEBUG");
        log.info("INFO");
        log.warn("WARN");
        log.error("ERROR");
        return "Logging Spring Boot...";
    }

    @PostMapping("/create")
    public ResponseEntity<Object> registerUser(@RequestBody @Validated(UserDto.UserView.RegistrationPost.class)
                                                    @JsonView(UserDto.UserView.RegistrationPost.class) UserDto userDto){
        log.debug("POST registerUser userDto received {} ", userDto.toString());
        return  ResponseEntity.status(HttpStatus.CREATED).body(userService.saveUser(userDto));
    }
}
