package com.myshop.orderservice.api.dto;

import com.myshop.orderservice.repository.model.Role;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserDto {
    private String login;
    private String name;
    private Role role;
}
