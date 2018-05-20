package com.example.demo;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class UserResponse {
    private boolean success = true;
    private Map<String, Object> data = new HashMap<>();
}
