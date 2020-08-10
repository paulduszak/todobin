package in.todob.todobin.controller;

import in.todob.todobin.dto.TodoRequest;
import in.todob.todobin.dto.TodoResponse;
import in.todob.todobin.model.Todo;
import in.todob.todobin.service.TodobinService;
import in.todob.todobin.util.TodoMapper;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/list/{listId}/todo")
public class TodobinController {

    private TodobinService todobinService;
    private TodoMapper todoMapper;

    public TodobinController(TodobinService todobinService, TodoMapper todoMapper) {
        this.todobinService = todobinService;
        this.todoMapper = todoMapper;
    }

    @PostMapping
    public ResponseEntity<TodoResponse> createTodo(@PathVariable("listId") String listId, @RequestBody TodoRequest todoRequest) {
        Todo savedTodo = todobinService.createTodo(listId, todoRequest);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                                                  .path("/{todoId}")
                                                  .buildAndExpand(savedTodo.getShortId()).toUri();

        return ResponseEntity.created(location)
                             .contentType(MediaType.APPLICATION_JSON)
                             .body(todoMapper.mapTodoToTodoResponse(savedTodo));
    }

    @PatchMapping("/{todoId}")
    public ResponseEntity<TodoResponse> patchTodo(@PathVariable("listId") String listId, @PathVariable("todoId") String todoId, @RequestBody TodoRequest patch) {
        Todo patchedTodo = todobinService.patchTodo(listId, todoId, patch);

        return ResponseEntity.ok()
                             .contentType(MediaType.APPLICATION_JSON)
                             .body(todoMapper.mapTodoToTodoResponse(patchedTodo));
    }

    @GetMapping("/{todoId}")
    public ResponseEntity<TodoResponse> getTodo(@PathVariable("listId") String listId, @PathVariable("todoId") String todoId) {
        Todo todo = todobinService.getTodo(listId, todoId);

        return ResponseEntity.ok()
                             .contentType(MediaType.APPLICATION_JSON)
                             .body(todoMapper.mapTodoToTodoResponse(todo));
    }

    @DeleteMapping("/{todoId}")
    public ResponseEntity<Void> deleteTodo(@PathVariable("listId") String listId, @PathVariable("todoId") String todoId) {
        todobinService.deleteTodo(listId, todoId);

        return ResponseEntity.ok().build();
    }

}
