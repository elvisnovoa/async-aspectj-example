package com.example.demo;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Aspect
@Component
public class UserAspect {

    private final GitHubLookupService gitHubLookupService;

    @Autowired
    private HttpServletRequest request;

    public UserAspect(GitHubLookupService gitHubLookupService) {
        this.gitHubLookupService = gitHubLookupService;
    }

    @Around(value = "@annotation(com.example.demo.MyAnnotation) && args(userRequest,..)")
    public UserResponse afterReturn(ProceedingJoinPoint pjp, UserRequest userRequest) throws Throwable {
        UserBean userBean = null;
        boolean isAsync = getAnnotationValue(pjp);
        log.info("Around 1: " + pjp + " with value: " + userRequest);

        // Enqueue the call for execution
        CompletableFuture<UserBean> future = gitHubLookupService.findUser(userRequest.getUserId());

        // If method is flagged as not async, wait for the call to finish
        if (!isAsync) {
            userBean = future.get();
        }

        // Continue with the original call
        UserResponse userResponse = (UserResponse) pjp.proceed();

        // Add the result of the async call to the response for demonstration purposes
        addData(userResponse, userBean, isAsync);

        log.info("Around 2: " + pjp + " with value: " + userRequest);
        return userResponse;
    }

    private boolean getAnnotationValue(ProceedingJoinPoint pjp) {
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Method method = signature.getMethod();

        MyAnnotation myAnnotation = method.getAnnotation(MyAnnotation.class);
        return myAnnotation.async();
    }

    private void addData(UserResponse userResponse, UserBean userBean, boolean isAsync) {
        final String customHeaderName = "Custom-Header";
        final String paramName = "param";
        Map<String, Object> map = userResponse.getData();
        map.put("isAsync", isAsync);
        map.put("user", userBean);
        map.put(customHeaderName, request.getHeader(customHeaderName));
        map.put(paramName, request.getParameter(paramName));
    }
}
