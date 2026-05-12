package com.library.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * Centralized exception handling for all REST controllers.
 *
 * All responses follow a consistent JSON shape:
 *   { "status": <http_code>, "message": <description> }
 *
 * For validation errors the "message" value is a map of
 *   { "fieldName": "validation error message" }.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    // ------------------------------------------------------------------
    // 404 – Resource Not Found
    // ------------------------------------------------------------------

    /**
     * Handles cases where a Book, Member, or IssueRecord does not exist.
     * Returns HTTP 404 with a descriptive message.
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleResourceNotFound(ResourceNotFoundException ex) {
        return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    // ------------------------------------------------------------------
    // 400 – Illegal State (business rule violations)
    // ------------------------------------------------------------------

    /**
     * Handles business rule violations (e.g., issuing an already-issued book).
     * Returns HTTP 400 with the exception message.
     */
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalState(IllegalStateException ex) {
        return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    // ------------------------------------------------------------------
    // 400 – Bean Validation failures (@Valid / @Validated)
    // ------------------------------------------------------------------

    /**
     * Handles Jakarta Bean Validation errors on request bodies.
     * The "message" field in the response is a map of field → error description.
     *
     * Example response:
     * {
     *   "status": 400,
     *   "message": { "title": "Title must not be blank", "author": "Author must not be blank" }
     * }
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationErrors(MethodArgumentNotValidException ex) {
        Map<String, String> fieldErrors = new HashMap<>();
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            fieldErrors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }

        Map<String, Object> body = new HashMap<>();
        body.put("status", HttpStatus.BAD_REQUEST.value());
        body.put("message", fieldErrors);

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    // ------------------------------------------------------------------
    // Helper
    // ------------------------------------------------------------------

    private ResponseEntity<Map<String, Object>> buildResponse(HttpStatus status, String message) {
        Map<String, Object> body = new HashMap<>();
        body.put("status", status.value());
        body.put("message", message);
        return new ResponseEntity<>(body, status);
    }
}
