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
    @Getter(AccessLevel.NONE)
    private String shortId;

    @NotNull
    @Column(nullable = false)
    private String title;

    private String description;

    public String getShortId() {
        if (this.id != null)
            return ShortIdMapper.encode(this.getId());
        return null;
    }
}
