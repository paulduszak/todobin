package in.todob.todobin.controller;

import in.todob.todobin.dto.TodolistRequest;
import in.todob.todobin.dto.TodolistResponse;
import in.todob.todobin.model.Todolist;
import in.todob.todobin.service.TodolistService;
import in.todob.todobin.util.TodolistMapper;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
public class TodolistController {

    private TodolistService todolistService;
    private TodolistMapper todolistMapper;

    public TodolistController(TodolistService todolistService, TodolistMapper todolistMapper) {
        this.todolistService = todolistService;
        this.todolistMapper = todolistMapper;
    }

    @PostMapping("/list")
    public ResponseEntity<TodolistResponse> createTodolist(@RequestBody TodolistRequest todolist) {
        Todolist savedTodolist = todolistService.createTodolist(todolist);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                                                  .path("/{id}")
                                                  .buildAndExpand(savedTodolist.getShortId()).toUri();

        return ResponseEntity.created(location)
                             .contentType(MediaType.APPLICATION_JSON)
                             .body(todolistMapper.mapTodolistToTodolistResponse(savedTodolist));
    }

    @PatchMapping("/list/{id}")
    public ResponseEntity<TodolistResponse> patchTodolist(@PathVariable("id") String id, @RequestBody TodolistRequest patch) {
        Todolist patchedTodolist = todolistService.patchTodolist(id, patch);

        return ResponseEntity.ok()
                             .contentType(MediaType.APPLICATION_JSON)
                             .body(todolistMapper.mapTodolistToTodolistResponse(patchedTodolist));
    }

    @GetMapping("/list")
    public ResponseEntity<List<TodolistResponse>> getTodolists() {
        List<Todolist> todolists = todolistService.getTodolists();

        return ResponseEntity.ok()
                             .contentType(MediaType.APPLICATION_JSON)
                             .body(todolistMapper.mapTodolistListToTodolistResponseList(todolists));
    }

    @GetMapping("/list/{id}")
    public ResponseEntity<TodolistResponse> getTodolist(@PathVariable("id") String id) {
        Todolist todolist = todolistService.getTodolist(id);

        return ResponseEntity.ok()
                             .contentType(MediaType.APPLICATION_JSON)
                             .body(todolistMapper.mapTodolistToTodolistResponse(todolist));
    }

    @DeleteMapping("/list/{id}")
    public ResponseEntity<Void> deleteTodolist(@PathVariable("id") String id) {
        todolistService.deleteTodolist(id);

        return ResponseEntity.ok().build();
    }

}
