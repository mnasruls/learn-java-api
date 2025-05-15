package javabasicapi.restful.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import jakarta.validation.ConstraintViolationException;
import javabasicapi.restful.dto.WebResponse;

@RestControllerAdvice
public class ErrorController {

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<WebResponse<String>> handleConstraintViolationException(ConstraintViolationException ex) {
        WebResponse<String> response = WebResponse.<String>builder().errors(ex.getMessage()).build();
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<WebResponse<String>> handleResponseStatusException(ResponseStatusException ex) {
        WebResponse<String> response = WebResponse.<String>builder().errors(ex.getMessage()).build();
        return ResponseEntity.status(ex.getStatusCode()).body(response);
    }
}