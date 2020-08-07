package in.todob.todobin.util;

import in.todob.todobin.dto.TodolistRequest;
import in.todob.todobin.dto.TodolistResponse;
import in.todob.todobin.model.Todolist;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TodolistMapper {
    TodolistResponse mapTodolistToTodolistResponse(Todolist todolist);

    Todolist mapTodolistRequestToTodolist(TodolistRequest todolistRequest);

    List<TodolistResponse> mapTodolistListToTodolistResponseList(List<Todolist> todolists);
}
