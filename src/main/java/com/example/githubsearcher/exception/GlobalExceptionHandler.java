
package com.example.githubsearcher.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Handle request validation errors
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationException(
            MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );

        return ResponseEntity.badRequest().body(errors);
    }

    // Handle GitHub API HTTP errors, including rate limits
    @ExceptionHandler(RestClientResponseException.class)
    public ResponseEntity<Map<String, String>> handleGitHubException(
            RestClientResponseException ex) {

        Map<String, String> error = new HashMap<>();

        error.put("message", "GitHub API request failed");
        error.put("details", ex.getStatusText());

        return ResponseEntity.status(ex.getStatusCode()).body(error);
    }

    // Handle GitHub connection errors
    @ExceptionHandler(RestClientException.class)
    public ResponseEntity<Map<String, String>> handleRestClientException(
            RestClientException ex) {

        Map<String, String> error = new HashMap<>();

        error.put("message", "Unable to connect to GitHub API");

        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(error);
    }

    // Handle invalid GitHub API responses
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<Map<String, String>> handleInvalidResponse(
            IllegalStateException ex) {

        Map<String, String> error = new HashMap<>();

        error.put("message", "Invalid response received from GitHub API");

        return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                .body(error);
    }
}
