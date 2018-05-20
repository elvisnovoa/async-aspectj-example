package com.example.demo;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    @MyAnnotation(async = false)
    @RequestMapping(method = RequestMethod.POST, path = "/synchronous")
    public UserResponse synchronous(@RequestBody UserRequest userRequest) {
        return new UserResponse();
    }

    @MyAnnotation(async = true)
    @RequestMapping(method = RequestMethod.POST, path = "/asynchronous")
    public UserResponse asynchronous(@RequestBody UserRequest userRequest) {
        return new UserResponse();
    }

    @RequestMapping(method = RequestMethod.POST, path = "/notadvised")
    public UserResponse notAdvised(@RequestBody UserRequest userRequest) {
        return new UserResponse();
    }
}
