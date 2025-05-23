package javabasicapi.restful.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javabasicapi.restful.dto.LoginRequest;
import javabasicapi.restful.dto.TokenResponse;
import javabasicapi.restful.dto.WebResponse;
import javabasicapi.restful.entity.User;
import javabasicapi.restful.service.AuthService;

@RestController
public class AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping(
        path = "api/login",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<TokenResponse> login(@RequestBody LoginRequest request) {
        TokenResponse tokenResponse = authService.login(request);
        return WebResponse.<TokenResponse>builder().data(tokenResponse).build();
    }

    @PostMapping(
        path = "api/logout",
        produces = MediaType.APPLICATION_JSON_VALUE
)
public WebResponse<String> logout(User userContext) {
    authService.logout(userContext);
    return WebResponse.<String>builder().data("OK").build();
}

}
