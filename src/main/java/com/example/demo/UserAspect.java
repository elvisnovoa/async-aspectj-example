package com.example.demo;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class UserAspect {

    private final GitHubLookupService gitHubLookupService;

    public UserAspect(GitHubLookupService gitHubLookupService) {
        this.gitHubLookupService = gitHubLookupService;
    }

    @Around(value = "execution(* com.example.demo.UserController+.*(..)) && args(userRequest,..)")
    public UserResponse afterReturn(ProceedingJoinPoint pjp, UserRequest userRequest) throws Throwable {
        log.info("Around 1: " + pjp + " with value: " + userRequest);

        // async call.. add .getNow() at the end to
        gitHubLookupService.findUser(userRequest.getUserId());

        UserResponse obj = (UserResponse) pjp.proceed();

        log.info("Around 2: " + pjp + " with value: " + userRequest);
        return obj;
    }
}
