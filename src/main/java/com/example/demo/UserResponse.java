package com.example.demo;

import lombok.Data;

@Data
public class UserResponse {
    private boolean success = true;
    private UserBean user;
}
