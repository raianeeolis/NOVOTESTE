package br.ucb.labbd.ToDoList.services;

import org.springframework.stereotype.Service;
import br.ucb.labbd.ToDoList.repositories.UsuarioRepository;
import br.ucb.labbd.ToDoList.repositories.GrupoUsuarioRepository;
import br.ucb.labbd.ToDoList.repositories.SessionRepository;
import br.ucb.labbd.ToDoList.entities.Usuario;
import br.ucb.labbd.ToDoList.entities.GrupoUsuarios; 
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
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(); 

    public UsuarioService(
            UsuarioRepository usuarioRepo,
            GrupoUsuarioRepository grupoRepo,
            SessionRepository sessionRepo
    ) {
        this.usuarioRepo = usuarioRepo;
        this.grupoRepo = grupoRepo;
        this.sessionRepo = sessionRepo;
    }

    public Optional<Usuario> findByEmail(String email) {
        return usuarioRepo.findByEmail(email);
    }
    
    public Optional<Usuario> findById(String id) {
        return usuarioRepo.findById(id); 
    }

    public List<Usuario> findAll() {
        return usuarioRepo.findAll();
    }

    @Transactional
    public Usuario createUser(Usuario u) {
        
        if (u.getSenhaHash() == null || u.getSenhaHash().isEmpty()) {
            throw new IllegalArgumentException("A senha é obrigatória.");
        }
        
        String idGrupo;
        try {
             idGrupo = u.getGrupo().getIdGrupo(); 
        } catch (NullPointerException e) {
             throw new IllegalArgumentException("O ID do grupo é obrigatório e deve ser fornecido corretamente.");
        }

        GrupoUsuarios grupo = grupoRepo.findById(idGrupo)
            .orElseThrow(() -> new IllegalArgumentException("Grupo de usuário não encontrado com o ID: " + idGrupo));
            
        u.setGrupo(grupo); 
        
        u.setSenhaHash(passwordEncoder.encode(u.getSenhaHash()));  
        
        return usuarioRepo.save(u);
    }

    @Transactional
    public String login(String email, String senha) {

        System.out.println("\n-------------------------------------");
        System.out.println("DEBUG LOGIN: Tentativa para: " + email); 

        Optional<Usuario> ou = usuarioRepo.findByEmail(email);

        if (ou.isEmpty()) {
            System.out.println("DEBUG LOGIN: Usuário NÃO encontrado no MySQL."); 
            return null; 
        }

        Usuario u = ou.get();

        System.out.println("DEBUG LOGIN: Senha digitada: " + senha);
        System.out.println("DEBUG LOGIN: Hash no Banco: " + u.getSenhaHash());
        System.out.println("-------------------------------------\n");

        if (!passwordEncoder.matches(senha, u.getSenhaHash())) {
            System.out.println("DEBUG LOGIN: SENHA INCORRETA. Falha BCrypt."); 
            return null; 
        }
        
        System.out.println("DEBUG LOGIN: LOGIN BEM-SUCEDIDO."); 

        String token = UUID.randomUUID().toString();

        SessionDoc s = new SessionDoc();
        s.setToken(token);  
        
        s.setUserId(u.getIdUsuario()); 
        s.setUserEmail(u.getEmail());
        
        s.setGrupo(u.getGrupo() != null ? u.getGrupo().getNome() : "Comum"); 
        
        s.setCreatedAt(Instant.now());
        s.setExpiresAt(Instant.now().plus(8, ChronoUnit.HOURS)); 

        sessionRepo.save(s);

        return token;
    }

    public void logout(String token) {
        sessionRepo.deleteById(token);
    }
}