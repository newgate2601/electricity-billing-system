package com.example.electricitybillingsystem.vo.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthResponse {

    private UserResponse user;

    private String token;

}
