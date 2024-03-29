package in.todob.todobin.model;

import in.todob.todobin.util.ShortIdMapper;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Todo {
    @Id
    @Column(updatable = false, nullable = false)
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    @Transient
    private String shortId;

    @ManyToOne(fetch = FetchType.LAZY, cascade=CascadeType.ALL)
    @JoinColumn(name = "todolist_id", nullable = false)
    private Todolist todolist;

    @NotNull
    @Column(nullable = false)
    private String title;

    private String notes;

    private boolean status = false;

    public String getShortId() {
        if (this.id != null)
            return ShortIdMapper.encode(this.getId());
        return null;
    }
}
