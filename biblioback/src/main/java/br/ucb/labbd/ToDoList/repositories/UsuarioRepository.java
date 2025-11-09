package br.ucb.labbd.ToDoList.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.ucb.labbd.ToDoList.entities.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, String> {
    /**
     * Busca um usuário pelo seu email. Usado principalmente para autenticação (login).
     */
    Optional<Usuario> findByEmail(String email);

    /**
     * Busca um usuário pelo seu ID customizado (id_usuario).
     * Essencial para serviços como TarefaService para associar uma tarefa a um usuário logado.
     */
    Optional<Usuario> findByIdUsuario(String idUsuario);
}
