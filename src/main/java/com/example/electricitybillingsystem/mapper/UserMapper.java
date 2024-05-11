package com.example.electricitybillingsystem.mapper;

import com.example.electricitybillingsystem.model.User;
import com.example.electricitybillingsystem.vo.request.RegisterRequest;
import com.example.electricitybillingsystem.vo.response.UserResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserResponse toUserResponse(User user);

    User toUser(RegisterRequest registerRequest);

}
