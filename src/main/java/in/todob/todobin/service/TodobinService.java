package in.todob.todobin.service;

import in.todob.todobin.dto.TodoRequest;
import in.todob.todobin.exception.TodoNotFoundException;
import in.todob.todobin.model.Todo;
import in.todob.todobin.repository.TodobinRepository;
import in.todob.todobin.util.ShortIdMapper;
import in.todob.todobin.util.TodoMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TodobinService {

    private TodobinRepository todobinRepository;

    public TodobinService(TodobinRepository todobinRepository) {
        this.todobinRepository = todobinRepository;
    }

    public Todo createTodo(TodoRequest todoRequest) {
        Todo todo = TodoMapper.mapToTodo(todoRequest);

        return todobinRepository.save(todo);
    }

    public Todo patchTodo(String shortId, TodoRequest todoRequest) {
        Todo existingTodo = getTodo(shortId);
        Todo patch = TodoMapper.mapToTodo(todoRequest);

        if (existingTodo != null) {
            if (patch.getTitle() != null) existingTodo.setTitle(patch.getTitle());
            if (patch.getNotes() != null) existingTodo.setNotes(patch.getNotes());
        }

        return todobinRepository.save(existingTodo);
    }

    public List<Todo> getTodos() {
        List<Todo> todos = todobinRepository.findAll();

        return todos;
    }

    public Todo getTodo(String shortId) {
        Optional<Todo> todo = todobinRepository.findById(ShortIdMapper.decode(shortId));

        if (todo.isPresent())
            return todo.get();
        else
            throw new TodoNotFoundException(shortId);
    }

    public void deleteTodo(String shortId) {
        if (getTodo(shortId) != null)
            todobinRepository.deleteById(ShortIdMapper.decode(shortId));
    }
}
