package in.todob.todobin.exception;

public class TodoNotFoundException extends RuntimeException {
    public TodoNotFoundException(String shortId) {
        super(String.format("Todo with ID '%s' not found.", shortId));
    }
}
