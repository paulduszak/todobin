package in.todob.todobin.exception;

public class TodoNotFoundException extends RuntimeException {
    public TodoNotFoundException(long id) {
        super(String.format("Todo with ID '%d' not found.", id));
    }
}
