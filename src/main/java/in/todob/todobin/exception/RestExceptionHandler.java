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

}