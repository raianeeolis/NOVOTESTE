package br.ucb.labbd.ToDoList.services;

import org.springframework.stereotype.Service;
import br.ucb.labbd.ToDoList.repositories.UsuarioRepository;
import br.ucb.labbd.ToDoList.repositories.GrupoUsuarioRepository;
import br.ucb.labbd.ToDoList.repositories.SessionRepository;
import br.ucb.labbd.ToDoList.entities.Usuario;
import br.ucb.labbd.ToDoList.entities.SessionDoc;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.UUID;
import java.util.List;

import jakarta.transaction.Transactional;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepo;
    private final GrupoUsuarioRepository grupoRepo;
    private final SessionRepository sessionRepo;
    private final BCryptPasswordEncoder passwordEncoder;

    public UsuarioService(
            UsuarioRepository usuarioRepo,
            GrupoUsuarioRepository grupoRepo,
            SessionRepository sessionRepo
    ) {
        this.usuarioRepo = usuarioRepo;
        this.grupoRepo = grupoRepo;
        this.sessionRepo = sessionRepo;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public Optional<Usuario> findByEmail(String email) {
        return usuarioRepo.findByEmail(email);
    }

    @Transactional
    public Usuario createUser(Usuario u) {
        if (u.getSenhaHash() != null) {
            u.setSenhaHash(passwordEncoder.encode(u.getSenhaHash())); // ✅ senhas sempre em hash
        }
        return usuarioRepo.save(u);
    }

    /**
     * Autentica usuário, cria sessão no Mongo e retorna token.
     */
    public String login(String email, String senha) {

        Optional<Usuario> ou = usuarioRepo.findByEmail(email);

        if (ou.isEmpty()) {
            return null;
        }

        Usuario u = ou.get();

        // ✅ compara senha com o hash BCrypt
        if (!passwordEncoder.matches(senha, u.getSenhaHash())) {
            return null;
        }

        // ✅ cria token de sessão
        String token = UUID.randomUUID().toString();

        SessionDoc s = new SessionDoc();
        s.setId(token); // token vira o _id no Mongo
        s.setUserId(u.getIdUsuario()); // ID do usuário do banco relacional
        s.setUserEmail(u.getEmail());
        s.setGrupo(u.getGrupo() != null ? u.getGrupo().getNome() : "Comum");
        s.setCreatedAt(Instant.now());
        s.setExpiresAt(Instant.now().plus(8, ChronoUnit.HOURS)); // expira em 8h

        sessionRepo.save(s);

        return token;
    }

    public void logout(String token) {
        sessionRepo.deleteById(token);
    }

    public List<Usuario> findAll() {
        return usuarioRepo.findAll();
    }
}
