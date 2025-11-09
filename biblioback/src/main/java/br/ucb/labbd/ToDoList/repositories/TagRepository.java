package br.ucb.labbd.ToDoList.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import br.ucb.labbd.ToDoList.entities.Tag;

public interface TagRepository extends JpaRepository<Tag, String> {
    Tag findByNome(String nome);
}
