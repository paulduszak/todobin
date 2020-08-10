package in.todob.todobin.controller;

import in.todob.todobin.dto.TodoRequest;
import in.todob.todobin.dto.TodoResponse;
import in.todob.todobin.model.Todo;
import in.todob.todobin.service.TodoService;
import in.todob.todobin.util.TodoMapper;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/list/{listId}/todo")
public class TodoController {

    private TodoService todoService;
    private TodoMapper todoMapper;

    public TodoController(TodoService todoService, TodoMapper todoMapper) {
        this.todoService = todoService;
        this.todoMapper = todoMapper;
    }

    @PostMapping
    public ResponseEntity<TodoResponse> createTodo(@PathVariable("listId") String listId, @RequestBody TodoRequest todoRequest) {
        Todo savedTodo = todoService.createTodo(listId, todoRequest);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                                                  .path("/{todoId}")
                                                  .buildAndExpand(savedTodo.getShortId()).toUri();

        return ResponseEntity.created(location)
                             .contentType(MediaType.APPLICATION_JSON)
                             .body(todoMapper.mapTodoToTodoResponse(savedTodo));
    }

    @PatchMapping("/{todoId}")
    public ResponseEntity<TodoResponse> patchTodo(@PathVariable("listId") String listId, @PathVariable("todoId") String todoId, @RequestBody TodoRequest patch) {
        Todo patchedTodo = todoService.patchTodo(listId, todoId, patch);

        return ResponseEntity.ok()
                             .contentType(MediaType.APPLICATION_JSON)
                             .body(todoMapper.mapTodoToTodoResponse(patchedTodo));
    }

    @GetMapping("/{todoId}")
    public ResponseEntity<TodoResponse> getTodo(@PathVariable("listId") String listId, @PathVariable("todoId") String todoId) {
        Todo todo = todoService.getTodo(listId, todoId);

        return ResponseEntity.ok()
                             .contentType(MediaType.APPLICATION_JSON)
                             .body(todoMapper.mapTodoToTodoResponse(todo));
    }

    @DeleteMapping("/{todoId}")
    public ResponseEntity<Void> deleteTodo(@PathVariable("listId") String listId, @PathVariable("todoId") String todoId) {
        todoService.deleteTodo(listId, todoId);

        return ResponseEntity.ok().build();
    }

}
