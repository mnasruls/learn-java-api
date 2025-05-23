package javabasicapi.restful.service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import org.springframework.transaction.annotation.Transactional;
import javabasicapi.restful.dto.RegisterRequest;
import javabasicapi.restful.dto.UpdateUserRequest;
import javabasicapi.restful.dto.UserResponse;
import javabasicapi.restful.entity.User;
import javabasicapi.restful.repository.UserRepository;
import javabasicapi.restful.security.BCrypt;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ValidationService validationService;

    @Transactional
    public void register(RegisterRequest request) {
        validationService.validate(request);
        
        // check username
        var user = userRepository.findByUsername(request.getUsername());
        if (user.isPresent()) {
            throw new ResponseStatusException( HttpStatus.BAD_REQUEST,"Username already exists");
        }
        
        User newUser = new User();
        newUser.setName(request.getName());
        newUser.setUsername(request.getUsername());
        newUser.setPassword(BCrypt.hashpw(request.getPassword(), BCrypt.gensalt()));
        newUser.setCreatedAt(LocalDateTime.now(ZoneOffset.UTC));
        newUser.setUpdatedAt(LocalDateTime.now(ZoneOffset.UTC));
        userRepository.save(newUser);
    }
    
    @Transactional(readOnly = true)
    public UserResponse findUser(User user) {
        return UserResponse.builder()
                .username(user.getUsername())
                .name(user.getName())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }

    @Transactional
    public UserResponse updateUser(User user, UpdateUserRequest request) {
        if (request != null) {
            if (request.getName() != null) {
                user.setName(request.getName());
            }
            if (request.getPassword() != null) {
                user.setPassword(BCrypt.hashpw(request.getPassword(), BCrypt.gensalt()));
            }
            user.setUpdatedAt(LocalDateTime.now(ZoneOffset.UTC));
            userRepository.save(user);
        }

        return findUser(user);
    }
    
    
}
