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
@RequestMapping("/api/v1/list")
public class TodolistController {

    private TodolistService todolistService;
    private TodolistMapper todolistMapper;

    public TodolistController(TodolistService todolistService, TodolistMapper todolistMapper) {
        this.todolistService = todolistService;
        this.todolistMapper = todolistMapper;
    }

    @PostMapping
    public ResponseEntity<TodolistResponse> createTodolist(@RequestBody TodolistRequest todolist) {
        Todolist savedTodolist = todolistService.createTodolist(todolist);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                                                  .path("/{listId}")
                                                  .buildAndExpand(savedTodolist.getShortId()).toUri();

        return ResponseEntity.created(location)
                             .contentType(MediaType.APPLICATION_JSON)
                             .body(todolistMapper.mapTodolistToTodolistResponse(savedTodolist));
    }

    @PatchMapping("/{listId}")
    public ResponseEntity<TodolistResponse> patchTodolist(@PathVariable("listId") String listId, @RequestBody TodolistRequest patch) {
        Todolist patchedTodolist = todolistService.patchTodolist(listId, patch);

        return ResponseEntity.ok()
                             .contentType(MediaType.APPLICATION_JSON)
                             .body(todolistMapper.mapTodolistToTodolistResponse(patchedTodolist));
    }

    @GetMapping
    public ResponseEntity<List<TodolistResponse>> getTodolists() {
        List<Todolist> todolists = todolistService.getTodolists();

        return ResponseEntity.ok()
                             .contentType(MediaType.APPLICATION_JSON)
                             .body(todolistMapper.mapTodolistListToTodolistResponseList(todolists));
    }

    @GetMapping("/{listId}")
    public ResponseEntity<TodolistResponse> getTodolist(@PathVariable("listId") String listId) {
        Todolist todolist = todolistService.getTodolist(listId);

        return ResponseEntity.ok()
                             .contentType(MediaType.APPLICATION_JSON)
                             .body(todolistMapper.mapTodolistToTodolistResponse(todolist));
    }

    @DeleteMapping("/{listId}")
    public ResponseEntity<Void> deleteTodolist(@PathVariable("listId") String listId) {
        todolistService.deleteTodolist(listId);

        return ResponseEntity.ok().build();
    }

}
