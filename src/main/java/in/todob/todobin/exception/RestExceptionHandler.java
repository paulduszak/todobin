package in.todob.todobin.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(TodoNotFoundException.class)
    public ResponseEntity todoNotFoundExceptionHandler(TodoNotFoundException e) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorInfo(HttpStatus.NOT_FOUND, e.getMessage()));
    }

    @ExceptionHandler(TodolistNotFoundException.class)
    public ResponseEntity todolistNotFoundExceptionHandler(TodolistNotFoundException e) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorInfo(HttpStatus.NOT_FOUND, e.getMessage()));
    }

    @ExceptionHandler(BadRequest.class)
    public ResponseEntity badRequestExceptionHandler(BadRequest e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorInfo(HttpStatus.BAD_REQUEST, e.getMessage()));
    }

}