package in.todob.todobin.service;

import in.todob.todobin.exception.TodoNotFoundException;
import in.todob.todobin.model.Todo;
import in.todob.todobin.repository.TodobinRepository;
import in.todob.todobin.util.ShortIdMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TodobinService {

    private TodobinRepository todobinRepository;

    public TodobinService(TodobinRepository todobinRepository) {
        this.todobinRepository = todobinRepository;
    }

    public Todo createTodo(Todo todoRequest) {
        return todobinRepository.save(todoRequest);
    }

    public Todo patchTodo(String shortId, Todo patch) {
        Todo existingTodo = getTodo(shortId);

        if (existingTodo != null) {
            if (patch.getTitle() != null) existingTodo.setTitle(patch.getTitle());
            if (patch.getDescription() != null) existingTodo.setDescription(patch.getDescription());
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
