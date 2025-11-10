package br.ucb.labbd.ToDoList.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import br.ucb.labbd.ToDoList.entities.Usuario;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, String> {
    Optional<Usuario> findByIdUsuario(String idUsuario);

    Optional<Usuario> findByEmail(String email);
}
