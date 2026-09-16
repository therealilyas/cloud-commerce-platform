package dev.ilyas.commerce.product;

import org.springframework.http.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import java.time.Instant;
import java.util.Map;

@RestControllerAdvice
class ApiExceptionHandler {
    @ExceptionHandler(ProductNotFoundException.class)
    ResponseEntity<ApiError> notFound(ProductNotFoundException ex) {
        return response(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(DuplicateSkuException.class)
    ResponseEntity<ApiError> conflict(DuplicateSkuException ex) {
        return response(HttpStatus.CONFLICT, ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<ApiError> invalid(MethodArgumentNotValidException ex) {
        var details = ex.getBindingResult().getFieldErrors().stream()
                .collect(java.util.stream.Collectors.toMap(e -> e.getField(), e -> e.getDefaultMessage(), (a, b) -> a));
        return ResponseEntity.badRequest().body(new ApiError(Instant.now(), 400, "Validation failed", details));
    }

    private ResponseEntity<ApiError> response(HttpStatus status, String message) {
        return ResponseEntity.status(status).body(new ApiError(Instant.now(), status.value(), message, Map.of()));
    }

    record ApiError(Instant timestamp, int status, String message, Map<String, String> details) {}
}
