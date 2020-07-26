package in.todob.todobin.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
public class TodoRequest {
    private String title;
    private String notes;
    //private boolean status;
}
