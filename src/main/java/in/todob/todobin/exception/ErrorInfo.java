package in.todob.todobin.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorInfo {
    private HttpStatus status;
    private String message;
    private Date timestamp;

    public ErrorInfo(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
        this.timestamp = new Date();
    }
}