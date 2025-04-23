package com.alouzou.sondage.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.server.ResponseStatusException;

import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<String> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body("Entrée invalide. Assurez-vous que les données correspondent au format attendu");
    }

    @ExceptionHandler(ForbiddenActionException.class)
    public ResponseEntity<String> handleForbiddenActionException(ForbiddenActionException ex) {
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error -> {
            errors.put(error.getField(), error.getDefaultMessage());
        });
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(errors);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body("Format invalide pour un champ booléen. Utilisez true ou false.");
    }

    @ExceptionHandler(ResourceAlreadyUsedException.class)
    public ResponseEntity<String> handleResourceAlreadyUsedException(ResourceAlreadyUsedException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ex.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ex.getMessage());
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handleEntityNotFoundException(EntityNotFoundException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ex.getMessage());
    }

    @ExceptionHandler(CategoryInactiveException.class)
    public ResponseEntity<String> handleCategoryInactiveException(CategoryInactiveException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ex.getMessage());
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Object> handleAccessDeniedException(AccessDeniedException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.FORBIDDEN.value());
        body.put("error", "Accès refusé");
        body.put("message", ex.getMessage());

        return new ResponseEntity<>(body, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(AuthenticationCredentialsNotFoundException.class)
    public ResponseEntity<Object> handleAuthenticationException(AuthenticationCredentialsNotFoundException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.UNAUTHORIZED.value());
        body.put("error", "Non authentifié");
        body.put("message", ex.getMessage());

        return new ResponseEntity<>(body, HttpStatus.UNAUTHORIZED);
    }

}
