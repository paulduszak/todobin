package in.todob.todobin.service;

import in.todob.todobin.exception.TodoNotFoundException;
import in.todob.todobin.model.Todo;
import in.todob.todobin.repository.TodobinRepository;
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

    public List<Todo> getTodos() {
        return todobinRepository.findAll();
    }

    public Todo getTodo(long id) {
        Optional<Todo> todo = todobinRepository.findById(id);

        if (todo.isPresent())
            return todo.get();
        else
            throw new TodoNotFoundException(id);
    }

    public void deleteTodo(long id) {
        if (getTodo(id) != null)
            todobinRepository.deleteById(id);
    }
}
