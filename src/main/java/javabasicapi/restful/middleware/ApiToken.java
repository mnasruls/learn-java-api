package javabasicapi.restful.middleware;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.server.ResponseStatusException;

import jakarta.servlet.http.HttpServletRequest;
import javabasicapi.restful.entity.User;
import javabasicapi.restful.pkg.Redis;
import javabasicapi.restful.repository.UserRepository;
import javabasicapi.restful.security.JwtUtil;


@Component
public class ApiToken implements HandlerMethodArgumentResolver {
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private Redis redis;
    
    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return User.class.equals(parameter.getParameterType());
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest httpRequest = (HttpServletRequest) webRequest.getNativeRequest();
        String token = httpRequest.getHeader("X-API-TOKEN");
        
        if (token == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
        }
        
        // Validate JWT token
        if (!jwtUtil.validateToken(token)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid or expired token");
        }
        
        // Extract user ID from JWT
        String userId = jwtUtil.extractUserId(token);
        if (userId == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid token payload");
        }

        String id = jwtUtil.extractId(token);
        if (id == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid token payload");
        }
        
        // First check if token exists in Redis
        String redisKey = "token:" + userId;
        String redisToken = (String) redis.get(redisKey);
        
        if (!redisToken.equals(token)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
        }
        
        // Fallback to database check if not in Redis
        User user = userRepository.findFirstByToken(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized"));
        
        // Verify that the token in the database matches
        if (!userId.equals(user.getId().toString())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
        }
        
        if (user.getTokenExpiredAt() < System.currentTimeMillis()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token expired");
        }
        
        // Store token in Redis for future requests
        long expirationSeconds = (user.getTokenExpiredAt() - System.currentTimeMillis()) / 1000;
        if (expirationSeconds > 0) {
            redis.save(redisKey, token, expirationSeconds);
        }
        
        return user;
    }
}
