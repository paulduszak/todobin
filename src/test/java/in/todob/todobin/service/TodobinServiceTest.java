package in.todob.todobin.service;

import in.todob.todobin.model.Todo;
import in.todob.todobin.repository.TodobinRepository;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TodobinServiceTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Mock
    private TodobinRepository mockTodobinRepository;

    private TodobinService todobinService;

    @Before
    public void setup() {
        todobinService = new TodobinService(mockTodobinRepository);
    }

    @Test
    public void createTodo_persistsTodo_whenPassedTodo() {
        Todo todo = Todo.builder()
                        .id(1L)
                        .title("A todo")
                        .description("A todo description")
                        .build();

        when(mockTodobinRepository.save(any(Todo.class))).thenReturn(todo);

        Todo result = todobinService.createTodo(todo);

        assertThat(result).isEqualToComparingFieldByField(todo);
        verify(mockTodobinRepository).save(todo);
    }

}
