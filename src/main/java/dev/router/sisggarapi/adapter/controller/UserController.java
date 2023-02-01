package dev.router.sisggarapi.adapter.controller;


import com.fasterxml.jackson.annotation.JsonView;
import dev.router.sisggarapi.adapter.config.secutity.AuthenticationCurrentUserService;
import dev.router.sisggarapi.adapter.config.secutity.UserDetailsImpl;
import dev.router.sisggarapi.adapter.mapper.UserMapper;
import dev.router.sisggarapi.adapter.request.authentication.UserRequest;
import dev.router.sisggarapi.adapter.response.authentication.UserResponse;
import dev.router.sisggarapi.adapter.specifications.SpecificationTemplate;
import dev.router.sisggarapi.core.domain.User;
import dev.router.sisggarapi.core.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

/*import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;*/

@Log4j2
@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/api/users")
@Tag(name = "User", description = "The User API. Contains all the operations that can be performed on a user.")
public class UserController {

    private final UserService userService;
    private final UserMapper mapper;
    private final AuthenticationCurrentUserService authenticationCurrentUserService;

    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping
    public ResponseEntity<Page<User>> getAllUsers(SpecificationTemplate.UserSpec spec,
                                                  @PageableDefault(page = 0, size = 10, sort = "userId", direction = Sort.Direction.ASC) Pageable pageable
                                                  ,Authentication authentication
    ){
        UserDetails userDetails = (UserDetailsImpl) authentication.getPrincipal();
        log.info("Authentication {}",userDetails.getUsername());
        Page<User> userModelPage = userService.findAll(spec, pageable);
        if(!userModelPage.isEmpty()){
            for(User user : userModelPage.toList()){
                //user.add(linkTo(methodOn(UserController.class).getOneUser(user.getUserId())).withSelfRel());
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body(userModelPage);
    }

    @PreAuthorize("hasAnyRole('STUDENT')")
    @GetMapping("/{userId}")
    public ResponseEntity<Object> getOneUser(@PathVariable(value = "userId") UUID userId){
       UUID currentUserId = authenticationCurrentUserService.getCurrentUser().getUserId();
        if(currentUserId.equals(userId)) {
            Optional<User> userModelOptional = userService.findById(userId);
            if (!userModelOptional.isPresent()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
            } else {
                return ResponseEntity.status(HttpStatus.OK).body(userModelOptional.get());
            }
        }else {
            throw new AccessDeniedException("Forbidden");
        }
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Object> deleteUser(@PathVariable(value = "userId") UUID userId){
        userService.deleteUser(userId);
        return  ResponseEntity.status(HttpStatus.OK).body("User deleted successfully.");
    }

    //@PreAuthorize("hasAnyRole('ADMIN')")
    @PostMapping
    public ResponseEntity<UserResponse> createUser(@RequestBody @Validated(UserRequest.UserView.RegistrationPost.class)
                                             @JsonView(UserRequest.UserView.RegistrationPost.class) UserRequest request){
        log.debug("POST createUser request received {} ", request.toString());
        var user = userService.saveUser(request);
        var response = Stream.of(user)
                .map(mapper::toUserResponse)
                .findFirst()
                .get();
        log.debug("POST createUser userId saved {} ", response.getUserId());
        log.info("User create successfully userId {} ", response.getUserId());
        return  ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @PutMapping("/{userId}")
    public ResponseEntity<Object> updateUser(@RequestBody @Validated(UserRequest.UserView.UserPut.class)
                                             @JsonView(UserRequest.UserView.UserPut.class) UserRequest request){
        log.debug("PUT updateUser userDto received {} ", request.toString());
        return  null;
        //return  ResponseEntity.status(HttpStatus.OK).body(userService.updateUser(request));
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @PutMapping("/password")
    public ResponseEntity<Object> updatePassword(@RequestBody @Validated(UserRequest.UserView.PasswordPut.class)
                                                 @JsonView(UserRequest.UserView.PasswordPut.class) UserRequest request){
        log.debug("PUT updatePassword userDto received {} ", request.toString());

        //userService.updatePassword(request);
        return  ResponseEntity.status(HttpStatus.OK).body("Password updated successfully.");

    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @PutMapping("/image")
    public ResponseEntity<Object> updateImage(@RequestBody @Validated(UserRequest.UserView.ImagePut.class)
                                              @JsonView(UserRequest.UserView.ImagePut.class) UserRequest userRequest){

            return  null;
            //return  ResponseEntity.status(HttpStatus.NOT_FOUND).body(userService.updateImage(userRequest));

    }

}
