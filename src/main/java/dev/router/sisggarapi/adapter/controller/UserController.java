package dev.router.sisggarapi.adapter.controller;


import com.fasterxml.jackson.annotation.JsonView;
import dev.router.sisggarapi.core.domain.User;
import dev.router.sisggarapi.adapter.dto.authentication.UserDto;
import dev.router.sisggarapi.core.service.UserService;
import dev.router.sisggarapi.adapter.specifications.SpecificationTemplate;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/*import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;*/

@Log4j2
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/api/users/v1")
public class UserController {

    @Autowired
    UserService userService;

   /* @Autowired
    AuthenticationCurrentUserService authenticationCurrentUserService;*/

    //@PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping
    public ResponseEntity<Page<User>> getAllUsers(SpecificationTemplate.UserSpec spec,
                                                  @PageableDefault(page = 0, size = 10, sort = "userId", direction = Sort.Direction.ASC) Pageable pageable
                                                  // Authentication authentication
    ){
        /*UserDetails userDetails = (UserDetailsImpl) authentication.getPrincipal();
        log.info("Authentication {}",userDetails.getUsername());*/
        Page<User> userModelPage = userService.findAll(spec, pageable);
        if(!userModelPage.isEmpty()){
            for(User user : userModelPage.toList()){
                //user.add(linkTo(methodOn(UserController.class).getOneUser(user.getUserId())).withSelfRel());
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body(userModelPage);
    }

   // @PreAuthorize("hasAnyRole('STUDENT')")
    @GetMapping("/{userId}")
    public ResponseEntity<Object> getOneUser(@PathVariable(value = "userId") UUID userId){
       /* UUID currentUserId = authenticationCurrentUserService.getCurrentUser().getUserId();
        if(currentUserId.equals(userId)) {
            Optional<UserModel> userModelOptional = userService.findById(userId);
            if (!userModelOptional.isPresent()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
            } else {
                return ResponseEntity.status(HttpStatus.OK).body(userModelOptional.get());
            }
        }else {
            throw new AccessDeniedException("Forbidden");
        }*/
        return null;
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Object> deleteUser(@PathVariable(value = "userId") UUID userId){
        userService.deleteUser(userId);
        return  ResponseEntity.status(HttpStatus.OK).body("User deleted successfully.");
    }

    @PostMapping
    public ResponseEntity<Object> createUser(@RequestBody @Validated(UserDto.UserView.RegistrationPost.class)
                                             @JsonView(UserDto.UserView.RegistrationPost.class) UserDto userDto){
        log.debug("POST createUser userDto received {} ", userDto.toString());
        var userModel = userService.saveUser(userDto);
        log.debug("POST createUser userId saved {} ", userModel.getUserId());
        log.info("User create successfully userId {} ", userModel.getUserId());
        return  ResponseEntity.status(HttpStatus.OK).body(userModel);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<Object> updateUser(@RequestBody @Validated(UserDto.UserView.UserPut.class)
                                             @JsonView(UserDto.UserView.UserPut.class) UserDto userDto){
        log.debug("PUT updateUser userDto received {} ", userDto.toString());
        return  ResponseEntity.status(HttpStatus.OK).body(userService.updateUser(userDto));
    }

    @PutMapping("/password")
    public ResponseEntity<Object> updatePassword(@RequestBody @Validated(UserDto.UserView.PasswordPut.class)
                                                 @JsonView(UserDto.UserView.PasswordPut.class) UserDto userDto){
        log.debug("PUT updatePassword userDto received {} ", userDto.toString());

        userService.updatePassword(userDto);
        return  ResponseEntity.status(HttpStatus.OK).body("Password updated successfully.");

    }

    @PutMapping("/image")
    public ResponseEntity<Object> updateImage(@RequestBody @Validated(UserDto.UserView.ImagePut.class)
                                              @JsonView(UserDto.UserView.ImagePut.class) UserDto userDto){

            return  ResponseEntity.status(HttpStatus.NOT_FOUND).body(userService.updateImage(userDto));

    }

}
