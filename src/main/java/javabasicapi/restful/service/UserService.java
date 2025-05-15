package javabasicapi.restful.service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import javabasicapi.restful.dto.RegisterRequest;
import javabasicapi.restful.entity.User;
import javabasicapi.restful.repository.UserRepository;
import javabasicapi.restful.security.BCrypt;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private Validator validator;

    @Transactional
    public void register(RegisterRequest request) {
        Set<ConstraintViolation<RegisterRequest>> violations = validator.validate(request);
        if (violations.size() != 0) {
            throw new ConstraintViolationException(violations);
        }
        
        // check username
        Optional<User> user = userRepository.findByUsername(request.getUsername());
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
}
