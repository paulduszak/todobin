package in.todob.todobin.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TodobinUser {
    @Id
    @Column(updatable = false, nullable = false)
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    @Column(updatable = false, nullable = false, unique = true)
    private String username;

    @Column(updatable = false, nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String role;

    @OneToMany(fetch = FetchType.LAZY, mappedBy="todobinUser", cascade = {CascadeType.ALL})
    private List<Todolist> todolists;

    private boolean enabled;
}
