package com.taskmaster.taskmaster.ExceptionHandler;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.taskmaster.taskmaster.model.response.ErrorResponse;
import com.taskmaster.taskmaster.model.response.FieldErrorDetailResponse;
import com.taskmaster.taskmaster.model.response.WebResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException exception) {
        List<FieldErrorDetailResponse> errors = new ArrayList<>();
        exception.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.add(new FieldErrorDetailResponse(fieldName, errorMessage));
        });

        ErrorResponse response = new ErrorResponse("Validation failed", errors);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidFormatException.class)
    public ResponseEntity<WebResponse<String>> invalidFormatException() {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(WebResponse.<String>builder()
                .code(HttpStatus.BAD_REQUEST.value())
                .message(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .errors("invalid format data")
                .build());
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<WebResponse<String>> handleApiException(ResponseStatusException exception) {
        return ResponseEntity.status(exception.getStatus())
            .body(WebResponse.<String>builder()
                .code(exception.getRawStatusCode())
                .message(exception.getStatus().getReasonPhrase())
                .errors(exception.getReason())
                .build());
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<WebResponse<String>> handleNullPointerException(NullPointerException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(WebResponse.<String>builder()
                .code(HttpStatus.BAD_REQUEST.value())
                .message(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .errors("Null value encountered in the request")
                .build());
    }

}
