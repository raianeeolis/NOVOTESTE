package br.ucb.labbd.ToDoList.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import br.ucb.labbd.ToDoList.entities.Usuario;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, String> {
    // Buscar usuário pelo ID (necessário para TarefaService)
    Optional<Usuario> findByIdUsuario(String idUsuario);

    // Buscar usuário pelo email (para login)
    Optional<Usuario> findByEmail(String email);
}
