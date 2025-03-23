package io.spring.exception;

import io.spring.dto.ErrorDetails;
import io.spring.response.ApiResponse;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.InvalidClaimException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<ErrorDetails>> handleResourceNotFoundException(ResourceNotFoundException exception, WebRequest webRequest) {
        ErrorDetails errorDetails = new ErrorDetails(new Date(), exception.getMessage(), webRequest.getDescription(false));
        ApiResponse<ErrorDetails> response = new ApiResponse<>(false, exception.getMessage(), errorDetails);
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<ApiResponse<ErrorDetails>> handleExpiredJwtException(ExpiredJwtException exception, WebRequest webRequest) {
        ErrorDetails errorDetails = new ErrorDetails(new Date(), "JWT token is expired", webRequest.getDescription(false));
        ApiResponse<ErrorDetails> response = new ApiResponse<>(false, "JWT token is expired", errorDetails);
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(SignatureException.class)
    public ResponseEntity<ApiResponse<ErrorDetails>> handleSignatureException(SignatureException exception, WebRequest webRequest) {
        ErrorDetails errorDetails = new ErrorDetails(new Date(), "Invalid JWT signature", webRequest.getDescription(false));
        ApiResponse<ErrorDetails> response = new ApiResponse<>(false, "Invalid JWT signature", errorDetails);
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(MalformedJwtException.class)
    public ResponseEntity<ApiResponse<ErrorDetails>> handleMalformedJwtException(MalformedJwtException exception, WebRequest webRequest) {
        ErrorDetails errorDetails = new ErrorDetails(new Date(), "Malformed JWT token", webRequest.getDescription(false));
        ApiResponse<ErrorDetails> response = new ApiResponse<>(false, "Malformed JWT token", errorDetails);
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(UnsupportedJwtException.class)
    public ResponseEntity<ApiResponse<ErrorDetails>> handleUnsupportedJwtException(UnsupportedJwtException exception, WebRequest webRequest) {
        ErrorDetails errorDetails = new ErrorDetails(new Date(), "Unsupported JWT token", webRequest.getDescription(false));
        ApiResponse<ErrorDetails> response = new ApiResponse<>(false, "Unsupported JWT token", errorDetails);
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(InvalidClaimException.class)
    public ResponseEntity<ApiResponse<ErrorDetails>> handleInvalidClaimException(InvalidClaimException exception, WebRequest webRequest) {
        ErrorDetails errorDetails = new ErrorDetails(new Date(), "Invalid claim in JWT token", webRequest.getDescription(false));
        ApiResponse<ErrorDetails> response = new ApiResponse<>(false, "Invalid claim in JWT token", errorDetails);
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<ErrorDetails>> handleIllegalArgumentException(IllegalArgumentException exception, WebRequest webRequest) {
        ErrorDetails errorDetails = new ErrorDetails(new Date(), "Invalid JWT token format", webRequest.getDescription(false));
        ApiResponse<ErrorDetails> response = new ApiResponse<>(false, "Invalid JWT token format", errorDetails);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiResponse<ErrorDetails>> handleBadCredentialsException(BadCredentialsException exception, WebRequest webRequest) {
        ErrorDetails errorDetails = new ErrorDetails(new Date(), "Invalid credentials provided", webRequest.getDescription(false));
        ApiResponse<ErrorDetails> response = new ApiResponse<>(false, "Invalid credentials provided", errorDetails);
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<ApiResponse<ErrorDetails>> handleDisabledException(DisabledException exception, WebRequest webRequest) {
        ErrorDetails errorDetails = new ErrorDetails(new Date(), "User account is disabled", webRequest.getDescription(false));
        ApiResponse<ErrorDetails> response = new ApiResponse<>(false, "User account is disabled", errorDetails);
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(LockedException.class)
    public ResponseEntity<ApiResponse<ErrorDetails>> handleLockedException(LockedException exception, WebRequest webRequest) {
        ErrorDetails errorDetails = new ErrorDetails(new Date(), "User account is locked", webRequest.getDescription(false));
        ApiResponse<ErrorDetails> response = new ApiResponse<>(false, "User account is locked", errorDetails);
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(AccountExpiredException.class)
    public ResponseEntity<ApiResponse<ErrorDetails>> handleAccountExpiredException(AccountExpiredException exception, WebRequest webRequest) {
        ErrorDetails errorDetails = new ErrorDetails(new Date(), "User account has expired", webRequest.getDescription(false));
        ApiResponse<ErrorDetails> response = new ApiResponse<>(false, "User account has expired", errorDetails);
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiResponse<ErrorDetails>> handleAuthenticationException(AuthenticationException exception, WebRequest webRequest) {
        ErrorDetails errorDetails = new ErrorDetails(new Date(), "Authentication failed", webRequest.getDescription(false));
        ApiResponse<ErrorDetails> response = new ApiResponse<>(false, "Authentication failed", errorDetails);
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(InsufficientAuthenticationException.class)
    public ResponseEntity<ApiResponse<ErrorDetails>> handleInsufficientAuthenticationException(InsufficientAuthenticationException exception, WebRequest webRequest) {
        ErrorDetails errorDetails = new ErrorDetails(new Date(), "Insufficient authentication", webRequest.getDescription(false));
        ApiResponse<ErrorDetails> response = new ApiResponse<>(false, "Insufficient authentication", errorDetails);
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse<ErrorDetails>> handleAccessDeniedException(AccessDeniedException exception, WebRequest webRequest) {
        ErrorDetails errorDetails = new ErrorDetails(new Date(), "Access denied", webRequest.getDescription(false));
        ApiResponse<ErrorDetails> response = new ApiResponse<>(false, "Access denied", errorDetails);
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiResponse<ErrorDetails>> handleJwtBadCredentialsException(BadCredentialsException exception, WebRequest webRequest) {
        ErrorDetails errorDetails = new ErrorDetails(new Date(), "Invalid JWT token", webRequest.getDescription(false));
        ApiResponse<ErrorDetails> response = new ApiResponse<>(false, "Invalid JWT token", errorDetails);
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<ErrorDetails>> handleGeneralException(Exception exception, WebRequest webRequest) {
        ErrorDetails errorDetails = new ErrorDetails(new Date(), "An unexpected error occurred", webRequest.getDescription(false));
        ApiResponse<ErrorDetails> response = new ApiResponse<>(false, "An unexpected error occurred", errorDetails);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
