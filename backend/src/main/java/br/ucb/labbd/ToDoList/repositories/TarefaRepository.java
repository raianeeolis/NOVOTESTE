package br.ucb.labbd.ToDoList.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import br.ucb.labbd.ToDoList.entities.Tarefa;
import java.util.List;
import java.util.Optional;

public interface TarefaRepository extends JpaRepository<Tarefa, String> {
    List<Tarefa> findByUsuarioIdUsuario(String idUsuario);
    
    Optional<Tarefa> findByIdTarefaAndUsuarioIdUsuario(String idTarefa, String idUsuario);
}
