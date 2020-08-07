package in.todob.todobin.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
public class TodolistRequest {
    private String title;
    private String notes;
    private List<TodoRequest> todos;
}
