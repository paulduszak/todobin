package in.todob.todobin.util;

import in.todob.todobin.dto.TodoRequest;
import in.todob.todobin.dto.TodoResponse;
import in.todob.todobin.model.Todo;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TodoMapper {
    TodoResponse mapTodoToTodoResponse(Todo todo);

    Todo mapTodoRequestToTodo(TodoRequest todoRequest);

    List<TodoResponse> mapTodoListToTodoResponseList(List<Todo> todos);
}
