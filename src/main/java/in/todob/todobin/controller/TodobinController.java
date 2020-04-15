package in.todob.todobin.controller;

import in.todob.todobin.model.Todo;
import in.todob.todobin.service.TodobinService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

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

        return ResponseEntity.created(location)
                             .contentType(MediaType.APPLICATION_JSON)
                             .body(savedTodo);
    }
}
