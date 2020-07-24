package in.todob.todobin.util;

import in.todob.todobin.dto.TodoRequest;
import in.todob.todobin.dto.TodoResponse;
import in.todob.todobin.model.Todo;

import java.util.List;
import java.util.stream.Collectors;

public final class TodoMapper {
    public static TodoResponse mapToTodoResponse(Todo todo) {
        return mapTodoResponse(todo);
    }

    public static List<TodoResponse> mapToTodoResponseList(List<Todo> todos) {
        return todos.stream().map(TodoMapper::mapTodoResponse).collect(Collectors.toList());
    }

    public static Todo mapToTodo(TodoRequest todoRequest) {
        return mapTodo(todoRequest);
    }

    private static TodoResponse mapTodoResponse(Todo todo) {
        TodoResponse todoResponse = new TodoResponse();
        todoResponse.setTitle(todo.getTitle());
        todoResponse.setDescription(todo.getDescription());
        todoResponse.setShortId(todo.getShortId());

        return todoResponse;
    }

    private static Todo mapTodo(TodoRequest todoRequest) {
        return Todo.builder().title(todoRequest.getTitle()).description(todoRequest.getDescription()).build();
    }
}
