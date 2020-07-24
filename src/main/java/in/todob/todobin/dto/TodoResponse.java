package in.todob.todobin.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TodoResponse {
    private String shortId;
    private String title;
    private String notes;
}
