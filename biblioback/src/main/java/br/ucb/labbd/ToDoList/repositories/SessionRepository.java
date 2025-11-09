package br.ucb.labbd.ToDoList.repositories;

import java.util.List;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import br.ucb.labbd.ToDoList.entities.SessionDoc;

public interface SessionRepository extends MongoRepository<SessionDoc, String> {
    List<SessionDoc> findByUserId(Long userId);

    // Busca uma sessão específica pelo e-mail do usuário.
    // Isso pode ser útil em cenários onde o e-mail é usado para rastreamento,
    // embora geralmente o ID seja preferido.
    List<SessionDoc> findByUserEmail(String userEmail);

    // Busca opcionalmente uma sessão que pertença a um usuário específico e a um grupo.
    Optional<SessionDoc> findByUserIdAndGrupo(Long userId, String grupo);
    
}
