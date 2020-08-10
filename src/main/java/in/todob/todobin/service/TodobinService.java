package in.todob.todobin.service;

import in.todob.todobin.dto.TodoRequest;

import in.todob.todobin.exception.BadRequest;
import in.todob.todobin.exception.TodoNotFoundException;
import in.todob.todobin.model.Todo;
import in.todob.todobin.model.Todolist;
import in.todob.todobin.repository.TodobinRepository;
import in.todob.todobin.util.ShortIdMapper;
import in.todob.todobin.util.TodoMapper;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TodobinService {

    private TodobinRepository todobinRepository;
    private TodolistService todolistService;
    private TodoMapper mapper;

    public TodobinService(TodobinRepository todobinRepository, TodolistService todolistService) {
        this.todobinRepository = todobinRepository;
        this.todolistService = todolistService;
        mapper = Mappers.getMapper(TodoMapper.class);
    }

    public Todo createTodo(String listId, TodoRequest todoRequest) {
        if (todoRequest == null || todoRequest.getTitle() == null || listId.isEmpty())
            throw new BadRequest();

        Todolist todolist = todolistService.getTodolist(listId);
        Todo todo = mapper.mapTodoRequestToTodo(todoRequest);

        todo.setTodolist(todolist);

        return todobinRepository.save(todo);
    }

    public Todo patchTodo(String listId, String todoId, TodoRequest todoRequest) {
        Todo existingTodo = getTodo(listId, todoId);
        Todo patch = mapper.mapTodoRequestToTodo(todoRequest);

        if (existingTodo != null) {
            if (patch.getTitle() != null) existingTodo.setTitle(patch.getTitle());
            if (patch.getNotes() != null) existingTodo.setNotes(patch.getNotes());
            existingTodo.setStatus(patch.isStatus());
        }

        return todobinRepository.save(existingTodo);
    }

    public Todo getTodo(String listId, String todoId) {
        validateTodoBelongsToTodolist(listId, todoId);

        Optional<Todo> todo = todobinRepository.findById(ShortIdMapper.decode(todoId));

        if (todo.isPresent())
            return todo.get();
        else
            throw new TodoNotFoundException(todoId);
    }

    public void deleteTodo(String listId, String todoId) {
        if (getTodo(listId, todoId) != null)
            todobinRepository.deleteById(ShortIdMapper.decode(todoId));
    }

    private void validateTodoBelongsToTodolist(String listId, String todoId) {
        Todolist todolist = todolistService.getTodolist(listId);

        List<String> todolistTodoIds = todolist.getTodos().stream()
                                               .map(todo -> todo.getShortId()).collect(Collectors.toList());

        if (!todolistTodoIds.contains(todoId)) throw new TodoNotFoundException(todoId);
    }
}
