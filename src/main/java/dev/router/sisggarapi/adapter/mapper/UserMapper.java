package dev.router.sisggarapi.adapter.mapper;


import dev.router.sisggarapi.adapter.request.authentication.UserRequest;
import dev.router.sisggarapi.adapter.response.authentication.UserResponse;
import dev.router.sisggarapi.core.domain.User;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class UserMapper {

    private final ModelMapper mapper;

    public User toUser(UserRequest request){
        return mapper.map(request, User.class);
    }

    public UserResponse toUserResponse(User User) {
        return mapper.map(User, UserResponse.class);
    }

    public List<UserResponse> toUserResponseList(List<User> Users) {
        return Users.stream()
                .map(this::toUserResponse)
                .collect(Collectors.toList());
    }
}
