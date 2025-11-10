package br.ucb.labbd.ToDoList.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import br.ucb.labbd.ToDoList.entities.GrupoUsuarios;

public interface GrupoUsuarioRepository extends JpaRepository<GrupoUsuarios, String> {
    GrupoUsuarios findByNome(String nome);
}
