package in.todob.todobin.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TodoRequest {
    private String title;
    private String notes;
}
