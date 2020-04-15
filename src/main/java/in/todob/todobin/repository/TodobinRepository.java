package in.todob.todobin.repository;

import in.todob.todobin.model.Todo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TodobinRepository extends JpaRepository<Todo, Long> {

}
