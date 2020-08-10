package in.todob.todobin.dto;

import in.todob.todobin.model.Todo;
import in.todob.todobin.model.Todolist;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
public class TodoRequest {
    private String title;
    private String notes;
    private boolean status;
}
