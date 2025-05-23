package javabasicapi.restful.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import org.springframework.transaction.annotation.Transactional;
import javabasicapi.restful.dto.LoginRequest;
import javabasicapi.restful.dto.TokenResponse;
import javabasicapi.restful.pkg.Redis;
import javabasicapi.restful.repository.UserRepository;
import javabasicapi.restful.security.BCrypt;
import javabasicapi.restful.security.JwtUtil;

@Service
public class AuthService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ValidationService validationService;
    
    @Autowired
    private Redis redis;
    
    @Autowired
    private JwtUtil jwtUtil;

    @Transactional
    public TokenResponse login(LoginRequest request) {
        validationService.validate(request);

        // get user by username
        var user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Username or password is incorrect"));

        // compare password
        if (BCrypt.checkpw(request.getPassword(), user.getPassword())) {
            // Generate JWT token
            String token = generateToken(user.getId().toString());
            Long expiredAt = System.currentTimeMillis() + jwtUtil.getExpirationTimeInMillis();
            
            // Update user entity
            user.setToken(token);
            user.setTokenExpiredAt(expiredAt);
            userRepository.save(user);
            
            return TokenResponse.builder()
                    .token(token)
                    .expiredAt(expiredAt.toString())
                    .refreshToken(null)
                    .build();
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Username or password is incorrect");
        }
    }
    
    // Use JWT to generate token and Redis to store session
    private String generateToken(String userId) {
        // Generate JWT token
        String jwtToken = jwtUtil.generateToken(userId);
        
        // Store in Redis with user ID as value
        // Use JWT token as key in Redis
        redis.save("token:" + jwtToken, userId, jwtUtil.getExpirationTimeInMillis() / 1000);
        
        return jwtToken;
    }
}