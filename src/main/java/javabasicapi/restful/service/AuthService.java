package javabasicapi.restful.service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import jakarta.transaction.Transactional;
import javabasicapi.restful.dto.LoginRequest;
import javabasicapi.restful.dto.TokenResponse;
import javabasicapi.restful.repository.UserRepository;
import javabasicapi.restful.security.BCrypt;

@Service
public class AuthService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ValidationService validationService;

    @Transactional
    public TokenResponse login(LoginRequest request) {
        validationService.validate(request);

        // get user by username
        var user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Username or password is incorrect"));

        // compare password
        if (BCrypt.checkpw(request.getPassword(), user.getPassword())) {
            user.setToken(UUID.randomUUID().toString());
            user.setTokenExpiredAt(getNext15Days());
            userRepository.save(user);
            return TokenResponse.builder()
                    .token(user.getToken())
                    .expiredAt(user.getTokenExpiredAt().toString())
                    .refreshToken(null)
                    .build(
                   );
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Username or password is incorrect");
        }
    }
    
    private Long getNext15Days() {
        return System.currentTimeMillis() + (15 * 24 * 60 * 60 * 1000);
    }
    
    // this will use redis to save user session
    // private String generateToken() {
        
    // }
}