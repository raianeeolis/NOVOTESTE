package br.ucb.labbd.ToDoList.services;

import org.springframework.stereotype.Service;
import br.ucb.labbd.ToDoList.repositories.UsuarioRepository;
import br.ucb.labbd.ToDoList.repositories.GrupoUsuarioRepository;
import br.ucb.labbd.ToDoList.entities.Usuario;
import br.ucb.labbd.ToDoList.repositories.SessionRepository;
import br.ucb.labbd.ToDoList.entities.SessionDoc;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.UUID;

import jakarta.transaction.Transactional;
import java.util.List;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Service
public class UsuarioService {
    private final UsuarioRepository usuarioRepo;
    private final GrupoUsuarioRepository grupoRepo; 
    private final SessionRepository sessionRepo;
    private final BCryptPasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepo,
                          GrupoUsuarioRepository grupoRepo,
                          SessionRepository sessionRepo) {
        this.usuarioRepo = usuarioRepo;
        this.grupoRepo = grupoRepo;
        this.sessionRepo = sessionRepo;
        // Inicializa o encoder de senhas para hashing
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    // Busca um usuário pelo email
    public Optional<Usuario> findByEmail(String email) {
        return usuarioRepo.findByEmail(email);
    }

    // Cria um novo usuário, hashando a senha antes de salvar
    @Transactional
    public Usuario createUser(Usuario u) {
        if (u.getSenhaHash() != null) {
            // Criptografa a senha fornecida
            u.setSenhaHash(passwordEncoder.encode(u.getSenhaHash())); 
        }
        return usuarioRepo.save(u);
    }

    /**
     * Tenta autenticar o usuário, cria uma sessão no MongoDB se for bem-sucedido e retorna o token.
     * @param email Email do usuário.
     * @param senha Senha em texto simples.
     * @return O token de sessão (String) ou null se a autenticação falhar.
     */
    public String login(String email, String senha) {
        Optional<Usuario> ou = usuarioRepo.findByEmail(email);
        
        // 1. Verifica se o usuário existe
        if (ou.isEmpty()) return null;

        Usuario u = ou.get();

        // 2. Verifica se a senha corresponde ao hash armazenado
        if (!passwordEncoder.matches(senha, u.getSenhaHash())) {
            return null; // Senha incorreta
        }

        // 3. Cria token e documento de sessão
        String token = UUID.randomUUID().toString();
        SessionDoc s = new SessionDoc();
        s.setId(token);
        
        // CORREÇÃO: Passa o ID do usuário como String, já que a entidade Usuario foi corrigida para usar String
        // ATENÇÃO: É necessário garantir que SessionDoc.setUserId() aceite String.
        s.setUserId(u.getIdUsuario()); // u.getIdUsuario() retorna String

        s.setUserEmail(u.getEmail());
        // Define o grupo, usando "Comum" como padrão
        s.setGrupo(u.getGrupo() != null ? u.getGrupo().getNome() : "Comum");
        s.setCreatedAt(Instant.now());
        // Define a expiração da sessão para 8 horas
        s.setExpiresAt(Instant.now().plus(8, ChronoUnit.HOURS));
        
        // 5. Salva o documento de sessão no MongoDB
        sessionRepo.save(s);

        return token;
    }

    // Encerra a sessão removendo o documento pelo token
    public void logout(String token) {
        sessionRepo.deleteById(token);
    }

    // Retorna todos os usuários
    public List<Usuario> findAll() {
        return usuarioRepo.findAll();
    }
}