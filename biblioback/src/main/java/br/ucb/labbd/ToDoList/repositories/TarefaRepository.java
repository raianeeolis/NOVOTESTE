package br.ucb.labbd.ToDoList.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import br.ucb.labbd.ToDoList.entities.Tarefa;
import java.util.List;
import java.util.Optional;

public interface TarefaRepository extends JpaRepository<Tarefa, String> {
    // Buscar todas as tarefas de um usuário específico
    List<Tarefa> findByUsuarioIdUsuario(String idUsuario);
    
    // Buscar uma tarefa específica de um usuário específico
    Optional<Tarefa> findByIdTarefaAndUsuarioIdUsuario(String idTarefa, String idUsuario);
}
