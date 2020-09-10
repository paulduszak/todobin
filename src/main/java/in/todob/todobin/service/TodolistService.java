package in.todob.todobin.service;

import in.todob.todobin.dto.TodolistRequest;
import in.todob.todobin.exception.BadRequest;
import in.todob.todobin.exception.TodolistNotFoundException;
import in.todob.todobin.model.TodobinUser;
import in.todob.todobin.model.Todolist;
import in.todob.todobin.repository.TodolistRepository;
import in.todob.todobin.util.ShortIdMapper;
import in.todob.todobin.util.TodolistMapper;
import org.mapstruct.factory.Mappers;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TodolistService {

    private TodolistRepository todolistRepository;
    private TodolistMapper todolistMapper;

    public TodolistService(TodolistRepository todolistRepository) {
        this.todolistRepository = todolistRepository;
        todolistMapper = Mappers.getMapper(TodolistMapper.class);
    }

    public Todolist createTodolist(TodolistRequest todolistRequest) {
        if (todolistRequest == null || todolistRequest.getTitle() == null || todolistRequest.getTodos() == null)
            throw new BadRequest();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Todolist todolist = todolistMapper.mapTodolistRequestToTodolist(todolistRequest);

        todolist.getTodos()
                .stream()
                .forEach(todo -> todo.setTodolist(todolist));

        if (authentication != null && !(authentication instanceof AnonymousAuthenticationToken))
            todolist.setTodobinUser((TodobinUser) authentication.getPrincipal());

        return todolistRepository.save(todolist);
    }

    public Todolist patchTodolist(String shortId, TodolistRequest todolistRequest) {
        Todolist existingTodo = getTodolist(shortId);
        Todolist patch = todolistMapper.mapTodolistRequestToTodolist(todolistRequest);

        if (existingTodo != null) {
            if (patch.getTitle() != null) existingTodo.setTitle(patch.getTitle());
            if (patch.getNotes() != null) existingTodo.setNotes(patch.getNotes());
            //if (patch.getTodos() != null) existingTodo.setTodos(patch.getTodos());
        }

        return todolistRepository.save(existingTodo);
    }

    //@PostFilter("!returnObject.authRequired")
    public List<Todolist> getTodolists() {
        List<Todolist> todolists = todolistRepository.findAll();


        return todolists;
    }

    @PostAuthorize("!returnObject.authRequired or (isAuthenticated() and principal.id == returnObject.todobinUser.id)")
    public Todolist getTodolist(String shortId) {
        Optional<Todolist> list = todolistRepository.findById(ShortIdMapper.decode(shortId));

        if (list.isPresent()) {
            return list.get();
        } else
            throw new TodolistNotFoundException(shortId);
    }

    public void deleteTodolist(String shortId) {
        if (getTodolist(shortId) != null)
            todolistRepository.deleteById(ShortIdMapper.decode(shortId));
    }
}
