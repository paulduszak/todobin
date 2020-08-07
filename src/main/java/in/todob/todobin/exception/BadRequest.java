package in.todob.todobin.exception;

public class BadRequest extends RuntimeException {
    public BadRequest() {
        super(String.format("Request body malformed."));
    }
}