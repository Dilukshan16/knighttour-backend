package com.knighttour.backend.exception;

import com.knighttour.model.Dto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidMoveException.class)
    public ResponseEntity<Dto.ApiError> handleInvalidMove(InvalidMoveException ex) {
        log.warn("Invalid move: {}", ex.getMessage());
        return ResponseEntity.badRequest().body(buildError(400, "Invalid Move", ex.getMessage()));
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Dto.ApiError> handleNotFound(ResourceNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(buildError(404, "Not Found", ex.getMessage()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Dto.ApiError> handleIllegalArg(IllegalArgumentException ex) {
        return ResponseEntity.badRequest().body(buildError(400, "Bad Request", ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Dto.ApiError> handleValidation(MethodArgumentNotValidException ex) {
        String msg = ex.getBindingResult().getFieldErrors().stream()
                .map(e -> e.getField() + ": " + e.getDefaultMessage())
                .collect(Collectors.joining("; "));
        return ResponseEntity.badRequest().body(buildError(400, "Validation Failed", msg));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Dto.ApiError> handleGeneral(Exception ex) {
        log.error("Unexpected error", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(buildError(500, "Internal Server Error", "An unexpected error occurred."));
    }

    private Dto.ApiError buildError(int status, String error, String message) {
        return Dto.ApiError.builder()
                .status(status)
                .error(error)
                .message(message)
                .timestamp(LocalDateTime.now())
                .build();
    }
}
