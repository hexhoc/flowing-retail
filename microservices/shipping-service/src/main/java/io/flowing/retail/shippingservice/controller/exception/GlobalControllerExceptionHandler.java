package io.flowing.retail.shippingservice.controller.exception;

import lombok.extern.java.Log;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.logging.Level;
import java.util.stream.Collectors;

@RestControllerAdvice
@Log
public class GlobalControllerExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ApiError> processValidationError(IllegalArgumentException e) {
        log.log(Level.INFO, "Returning HTTP 400 Bad Request", e);

        ApiError apiError = new ApiError("Returning HTTP 400 Bad Request", e.getMessage());
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);

    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ApiError> processMethodArgumentNotValid(MethodArgumentNotValidException e) {
        log.log(Level.INFO, "Returning HTTP 400 Bad Request", e);

        List<String> errors = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(x -> x.getDefaultMessage())
                .collect(Collectors.toList());

       ApiError apiError = new ApiError("Method Argument Not Valid", e.getMessage(), errors);

        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected ResponseEntity<ApiError> handleAllExceptions(Exception e) {
        log.log(Level.INFO, "Returning HTTP 500 INTERNAL SERVER ERROR", e);

        ApiError apiError = new ApiError("Internal Exception", e.getMessage());
        return new ResponseEntity<>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);
    }


}