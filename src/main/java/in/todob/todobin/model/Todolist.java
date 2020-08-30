package in.todob.todobin.model;

import in.todob.todobin.util.ShortIdMapper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Todolist {
    @Id
    @Column(updatable = false, nullable = false)
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    @Transient
    private String shortId;

    @NotNull
    @Column(nullable = false)
    private String title;

    private String notes;

    private boolean authRequired;

    @OneToMany(fetch = FetchType.LAZY, mappedBy="todolist", cascade = {CascadeType.ALL})
    private List<Todo> todos;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "todobin_user_id")
    private TodobinUser todobinUser;

    public String getShortId() {
        if (this.id != null)
            return ShortIdMapper.encode(this.getId());
        return null;
    }
}
