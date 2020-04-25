package in.todob.todobin.controller;

import in.todob.todobin.model.Todo;
import in.todob.todobin.service.TodobinService;
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
    public ResponseEntity<Todo> createTodo(@RequestBody Todo todo) {
        Todo savedTodo = todobinService.createTodo(todo);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                                                  .path("/{id}")
                                                  .buildAndExpand(savedTodo.getId()).toUri();

        return ResponseEntity.created(location).contentType(MediaType.APPLICATION_JSON).body(savedTodo);
    }

    @PatchMapping("/todo/{id}")
    public ResponseEntity<Todo> patchTodo(@PathVariable("id") Long id, @RequestBody Todo patch) {
        Todo patchedTodo = todobinService.patchTodo(id, patch);

        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(patchedTodo);
    }

    @GetMapping("/todo")
    public ResponseEntity<List<Todo>> getTodos() {
        List<Todo> todos = todobinService.getTodos();

        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(todos);
    }

    @GetMapping("/todo/{id}")
    public ResponseEntity<Todo> getTodo(@PathVariable("id") Long id) {
        Todo todo = todobinService.getTodo(id);

        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(todo);
    }

    @DeleteMapping("/todo/{id}")
    public ResponseEntity<Void> deleteTodo(@PathVariable("id") Long id) {
        todobinService.deleteTodo(id);

        return ResponseEntity.ok().build();
    }

}
