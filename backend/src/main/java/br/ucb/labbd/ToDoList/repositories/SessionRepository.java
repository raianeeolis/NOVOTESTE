package br.ucb.labbd.ToDoList.repositories;

import java.util.List;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import br.ucb.labbd.ToDoList.entities.SessionDoc;

public interface SessionRepository extends MongoRepository<SessionDoc, String> {
    List<SessionDoc> findByUserId(Long userId);

    List<SessionDoc> findByUserEmail(String userEmail);

    Optional<SessionDoc> findByUserIdAndGrupo(Long userId, String grupo);
    
}
