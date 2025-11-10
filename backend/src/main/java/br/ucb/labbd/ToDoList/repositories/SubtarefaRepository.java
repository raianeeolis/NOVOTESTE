package br.ucb.labbd.ToDoList.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import br.ucb.labbd.ToDoList.entities.Subtarefa;
import java.util.List;

public interface SubtarefaRepository extends JpaRepository<Subtarefa, String> {
    List<Subtarefa> findByTarefa_IdTarefa(String idTarefa);    
}
