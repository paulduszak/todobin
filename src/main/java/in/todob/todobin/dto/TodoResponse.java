package in.todob.todobin.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
public class TodoResponse {
    private String shortId;
    private String title;
    private String notes;
    private boolean status;
}
