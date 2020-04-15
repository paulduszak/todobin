package in.todob.todobin.service;

import in.todob.todobin.model.Todo;
import in.todob.todobin.repository.TodobinRepository;
import org.springframework.stereotype.Service;

@Service
public class TodobinService {

    private TodobinRepository todobinRepository;

    public TodobinService(TodobinRepository todobinRepository) {
        this.todobinRepository = todobinRepository;
    }

    public Todo createTodo(Todo todoRequest) {
        return todobinRepository.save(todoRequest);
    }

}
