package com.library.exception;

/**
 * Thrown when a requested resource (Book, Member, IssueRecord) is not found in the DB.
 * The GlobalExceptionHandler maps this to an HTTP 404 response.
 *
 * Usage example:
 *   throw new ResourceNotFoundException("Book not found with id: " + bookId);
 */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException(String resourceName, String fieldName, Object fieldValue) {
        super(String.format("%s not found with %s: '%s'", resourceName, fieldName, fieldValue));
    }
}
