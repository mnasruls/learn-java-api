package javabasicapi.restful.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javabasicapi.restful.dto.RegisterRequest;
import javabasicapi.restful.dto.UserResponse;
import javabasicapi.restful.dto.WebResponse;
import javabasicapi.restful.entity.User;
import javabasicapi.restful.service.UserService;

@RestController
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping(
                path = "api/register",
                consumes = MediaType.APPLICATION_JSON_VALUE,
                produces = MediaType.APPLICATION_JSON_VALUE          
    )

    public WebResponse<String> register(@RequestBody RegisterRequest request) {
        userService.register(request);
        return WebResponse.<String>builder().data("OK").build();
    }
    
    @GetMapping(
                path = "api/users/me",
                produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<UserResponse> getUser(User userContext) {
        var user = userService.findUser(userContext);
        return WebResponse.<UserResponse>builder().data(user).build();
    }
}
