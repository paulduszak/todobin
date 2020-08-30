package in.todob.todobin.service;

import in.todob.todobin.dto.TodoRequest;
import in.todob.todobin.dto.TodolistRequest;
import in.todob.todobin.exception.BadRequest;
import in.todob.todobin.exception.TodolistNotFoundException;
import in.todob.todobin.model.Todo;
import in.todob.todobin.model.TodobinUser;
import in.todob.todobin.model.Todolist;
import in.todob.todobin.repository.TodolistRepository;
import in.todob.todobin.util.ShortIdMapper;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.Optional;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TodolistServiceTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Mock
    private TodolistRepository mockTodolistRepository;

    @Mock
    private Authentication authentication;

    @Mock
    private SecurityContext securityContext;

    private TodolistService todolistService;

    @Before
    public void setup() {
        todolistService = new TodolistService(mockTodolistRepository);
    }


    @Test
    public void createTodolist_persistsTodolist_whenPassedTodolist() {

        TodoRequest todoRequest = new TodoRequest();
        todoRequest.setTitle("A todo");
        todoRequest.setNotes("A todo description");

        TodolistRequest todolistRequest = new TodolistRequest();
        todolistRequest.setTitle("A todolist");
        todolistRequest.setNotes("A todolist description");
        todolistRequest.setTodos(asList(todoRequest));

        when(mockTodolistRepository.save(any(Todolist.class)))
                .thenReturn(Todolist.builder()
                                    .id(1L)
                                    .title("A todolist")
                                    .notes("A todolist description")
                                    .todos(asList(Todo.builder()
                                                      .id(1L)
                                                      .title("A todo")
                                                      .notes("A todo description")
                                                      .build()))
                                    .build());

        Todolist result = todolistService.createTodolist(todolistRequest);

        assertThat(result.getTitle()).isEqualTo("A todolist");
        assertThat(result.getNotes()).isEqualTo("A todolist description");
        assertThat(result.getShortId()).isEqualTo(ShortIdMapper.encode(result.getId()));
        assertThat(result.getTodos().size()).isEqualTo(1);
        assertThat(result.getTodos().get(0).getTitle()).isEqualTo("A todo");
        assertThat(result.getTodos().get(0).getNotes()).isEqualTo("A todo description");
        assertThat(result.getTodos().get(0).getShortId()).isEqualTo(ShortIdMapper.encode(result.getTodos().get(0).getId()));
    }

    @Test
    public void createTodolist_setsTodobinUser_whenSecurityContext_isNotAnonymousAuthenticationToken() {
        ArgumentCaptor<Todolist> acTodolist = ArgumentCaptor.forClass(Todolist.class);

        TodoRequest todoRequest = new TodoRequest();
        todoRequest.setTitle("A todo");
        todoRequest.setNotes("A todo description");

        TodolistRequest todolistRequest = new TodolistRequest();
        todolistRequest.setTitle("A todolist");
        todolistRequest.setNotes("A todolist description");
        todolistRequest.setTodos(asList(todoRequest));

        TodobinUser todobinUser = TodobinUser.builder().id(1L).username("testUser").build();

        when(securityContext.getAuthentication()).thenReturn(authentication);

        SecurityContextHolder.setContext(securityContext);

        when(SecurityContextHolder.getContext().getAuthentication()).thenReturn(authentication);
        when(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).thenReturn(todobinUser);

        todolistService.createTodolist(todolistRequest);

        verify(mockTodolistRepository).save(acTodolist.capture());

        assertThat(acTodolist.getValue().getTodobinUser().getId()).isEqualTo(1L);
        assertThat(acTodolist.getValue().getTodobinUser().getUsername()).isEqualTo("testUser");
    }

    @Test
    public void createTodolist_doesNotSetTodobinUser_whenSecurityContext_isAnonymousAuthenticationToken() {
        ArgumentCaptor<Todolist> acTodolist = ArgumentCaptor.forClass(Todolist.class);

        TodoRequest todoRequest = new TodoRequest();
        todoRequest.setTitle("A todo");
        todoRequest.setNotes("A todo description");

        TodolistRequest todolistRequest = new TodolistRequest();
        todolistRequest.setTitle("A todolist");
        todolistRequest.setNotes("A todolist description");
        todolistRequest.setTodos(asList(todoRequest));

        TodobinUser todobinUser = TodobinUser.builder().id(1L).username("testUser").build();

        when(securityContext.getAuthentication()).thenReturn(authentication);

        SecurityContextHolder.setContext(securityContext);

        when(SecurityContextHolder.getContext().getAuthentication()).thenReturn(null);

        todolistService.createTodolist(todolistRequest);

        verify(mockTodolistRepository).save(acTodolist.capture());

        assertThat(acTodolist.getValue().getTodobinUser()).isNull();
    }

    @Test
    public void deleteTodolist_deletesTodolist_whenPassedValidTodolistId() {
        when(mockTodolistRepository.findById(3022L)).thenReturn(Optional.ofNullable(Todolist.builder().build()));

        todolistService.deleteTodolist("2x");

        verify(mockTodolistRepository).deleteById(3022L);
    }

    @Test
    public void deleteTodolist_throwsTodolistNotFoundException_whenTodolistIdForDeletionDoesNotExist() {
        thrown.expect(TodolistNotFoundException.class);
        thrown.expectMessage("Todolist with ID '4c' not found.");

        todolistService.deleteTodolist("4c");
    }

    @Test
    public void getTodolists_retrievesPersistedTodolists() {
        List<Todolist> todolistList = asList(
                Todolist.builder().id(1L).build(),
                Todolist.builder().id(2L).build()
        );

        List<Todolist> expected = asList(
                Todolist.builder()
                        .id(1L)
                        .shortId("u")
                        .build(),
                Todolist.builder()
                        .id(2L)
                        .shortId("l")
                        .build()
        );

        when(mockTodolistRepository.findAll()).thenReturn(todolistList);

        List<Todolist> result = todolistService.getTodolists();

        assertThat(result).isEqualTo(expected);
    }

    @Test
    public void getTodolist_returnsTodolistIfExists() {
        Todolist todolist = Todolist.builder()
                                    .id(3022L)
                                    .shortId("2x")
                                    .title("A todolist")
                                    .notes("A todolist description")
                                    .build();

        when(mockTodolistRepository.findById(3022L)).thenReturn(Optional.ofNullable(todolist));

        Todolist result = todolistService.getTodolist("2x");

        assertThat(result).isEqualTo(todolist);
    }

    @Test
    public void patchTodolist_patchesTodolist_whenPassedFieldsWithUpdatedValues() {
        Todolist existingTodolist = Todolist.builder()
                                            .id(3022L)
                                            .shortId("2x")
                                            .title("A todolist")
                                            .notes("A todolist description")
                                            .build();

        TodolistRequest todolistRequest = new TodolistRequest();
        todolistRequest.setTitle("An updated todolist");
        todolistRequest.setNotes("An updated todolist description");

        Todolist expected = Todolist.builder()
                            .id(3022L)
                            .shortId("2x")
                            .title("An updated todolist")
                            .notes("An updated todolist description")
                            .build();

        when(mockTodolistRepository.findById(3022L)).thenReturn(Optional.ofNullable(existingTodolist));
        when(mockTodolistRepository.save(expected)).thenReturn(expected);

        Todolist result = todolistService.patchTodolist("2x", todolistRequest);

        assertThat(result).isEqualTo(expected);
        verify(mockTodolistRepository).save(expected);
    }

    @Test
    public void createTodolist_throwsInvalidRequest_whenTodoRequestBodyMissingRequiredFields() {
        thrown.expect(BadRequest.class);
        thrown.expectMessage("Request body malformed.");

        TodolistRequest todolistRequest = new TodolistRequest();

        todolistService.createTodolist(todolistRequest);
    }

    @Test
    public void getTodolist_throwsTodolistNotFoundException_ifTodolistDoesNotExist() {
        thrown.expect(TodolistNotFoundException.class);
        thrown.expectMessage("Todolist with ID '3c' not found.");

        todolistService.getTodolist("3c");
    }

    @Test
    public void patchTodolist_throwsTodolistNotFoundException_ifTodolistDoesNotExist() {
        thrown.expect(TodolistNotFoundException.class);
        thrown.expectMessage("Todolist with ID '5c' not found.");

        todolistService.patchTodolist("5c", new TodolistRequest());
    }

}