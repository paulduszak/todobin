package in.todob.todobin.exception;

public class TodolistNotFoundException extends RuntimeException {
    public TodolistNotFoundException(String shortId) {
        super(String.format("Todolist with ID '%s' not found.", shortId));
    }
}
