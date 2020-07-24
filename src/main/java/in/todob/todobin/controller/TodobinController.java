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
public class TodobinController {

    private TodobinService todobinService;

    public TodobinController(TodobinService todobinService) {
        this.todobinService = todobinService;
    }

    @PostMapping("/todo")
    public ResponseEntity<TodoResponse> createTodo(@RequestBody TodoRequest todo) {
        Todo savedTodo = todobinService.createTodo(todo);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                                                  .path("/{id}")
                                                  .buildAndExpand(savedTodo.getShortId()).toUri();

        return ResponseEntity.created(location)
                             .contentType(MediaType.APPLICATION_JSON)
                             .body(TodoMapper.mapToTodoResponse(savedTodo));
    }

    @PatchMapping("/todo/{id}")
    public ResponseEntity<TodoResponse> patchTodo(@PathVariable("id") String id, @RequestBody TodoRequest patch) {
        Todo patchedTodo = todobinService.patchTodo(id, patch);

        return ResponseEntity.ok()
                             .contentType(MediaType.APPLICATION_JSON)
                             .body(TodoMapper.mapToTodoResponse(patchedTodo));
    }

    @GetMapping("/todo")
    public ResponseEntity<List<TodoResponse>> getTodos() {
        List<Todo> todos = todobinService.getTodos();

        return ResponseEntity.ok()
                             .contentType(MediaType.APPLICATION_JSON)
                             .body(TodoMapper.mapToTodoResponseList(todos));
    }

    @GetMapping("/todo/{id}")
    public ResponseEntity<TodoResponse> getTodo(@PathVariable("id") String id) {
        Todo todo = todobinService.getTodo(id);

        return ResponseEntity.ok()
                             .contentType(MediaType.APPLICATION_JSON)
                             .body(TodoMapper.mapToTodoResponse(todo));
    }

    @DeleteMapping("/todo/{id}")
    public ResponseEntity<Void> deleteTodo(@PathVariable("id") String id) {
        todobinService.deleteTodo(id);

        return ResponseEntity.ok().build();
    }

}
