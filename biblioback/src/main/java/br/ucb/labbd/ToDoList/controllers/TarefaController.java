package br.ucb.labbd.ToDoList.controllers;

import org.springframework.web.bind.annotation.*;
import br.ucb.labbd.ToDoList.entities.Tarefa;
import br.ucb.labbd.ToDoList.entities.SessionDoc;
import br.ucb.labbd.ToDoList.services.TarefaService;
import br.ucb.labbd.ToDoList.repositories.SessionRepository;
import org.springframework.http.ResponseEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/api/tarefas")
public class TarefaController {
    private final TarefaService tarefaService;
    private final SessionRepository sessionRepo;

    // Nome do cabeçalho de autenticação (pode ser "Authorization" ou customizado)
    @Value("${app.auth.header:X-Auth-Token}")
    private String AUTH_HEADER;

    public TarefaController(TarefaService tarefaService, SessionRepository sessionRepo) {
        this.tarefaService = tarefaService;
        this.sessionRepo = sessionRepo;
    }

    /**
     * Função auxiliar para obter o ID do usuário a partir do token de sessão.
     * CORREÇÃO: Tipo de retorno alterado de Long para String.
     */
    private String getUserIdFromToken(String token) {
        if (token == null || token.isEmpty()) return null;
        
        // Remove prefixos comuns como "Bearer "
        String cleanToken = token.replace("Bearer ", "");
        Optional<SessionDoc> session = sessionRepo.findById(cleanToken);
        
        // Verifica se a sessão existe e se não expirou
        if (session.isPresent()) {
            // Verifica se o token está expirado (opcional, se TTL não estiver configurado no MongoDB)
            // if (session.get().getExpiresAt().isBefore(Instant.now())) { return null; }
            // CORREÇÃO: Retorna o ID do usuário como String
            return session.get().getUserId();
        }
        return null;
    }

    // Endpoint para LISTAR todas as tarefas do usuário logado
    @GetMapping
    public ResponseEntity<List<Tarefa>> getAllUserTasks(
        @RequestHeader(name = "X-Auth-Token", required = true) String authToken) {
        
        // CORREÇÃO: Variável userId agora é String
        String userId = getUserIdFromToken(authToken);
        if (userId == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        
        List<Tarefa> tarefas = tarefaService.findAllByUserId(userId);
        return ResponseEntity.ok(tarefas);
    }
    
    // Endpoint para BUSCAR uma tarefa específica (com verificação de propriedade)
    @GetMapping("/{id}")
    public ResponseEntity<Tarefa> getTaskById(
        @PathVariable("id") String id, 
        @RequestHeader(name = "X-Auth-Token", required = true) String authToken) {
        
        // CORREÇÃO: Variável userId agora é String
        String userId = getUserIdFromToken(authToken);
        if (userId == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        
        Optional<Tarefa> tarefa = tarefaService.findByIdAndUserId(id, userId);
        return tarefa.map(ResponseEntity::ok)
                     .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Endpoint para CRIAR uma nova tarefa
    @PostMapping
    public ResponseEntity<Tarefa> createTask(
        @RequestBody Tarefa tarefa,
        @RequestHeader(name = "X-Auth-Token", required = true) String authToken) {
        
        // CORREÇÃO: Variável userId agora é String
        String userId = getUserIdFromToken(authToken);
        if (userId == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        try {
            Tarefa createdTarefa = tarefaService.createTarefa(tarefa, userId);
            return new ResponseEntity<>(createdTarefa, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    
    // Endpoint para ATUALIZAR uma tarefa existente (com verificação de propriedade)
    @PutMapping("/{id}")
    public ResponseEntity<Tarefa> updateTask(
        @PathVariable("id") String id,
        @RequestBody Tarefa tarefaDetails,
        @RequestHeader(name = "X-Auth-Token", required = true) String authToken) {
        
        // CORREÇÃO: Variável userId agora é String
        String userId = getUserIdFromToken(authToken);
        if (userId == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        Optional<Tarefa> updatedTarefa = tarefaService.updateTarefa(id, tarefaDetails, userId);
        
        return updatedTarefa.map(ResponseEntity::ok)
                            .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    
    // Endpoint para EXCLUIR uma tarefa (com verificação de propriedade)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(
        @PathVariable("id") String id,
        @RequestHeader(name = "X-Auth-Token", required = true) String authToken) {
        
        // CORREÇÃO: Variável userId agora é String
        String userId = getUserIdFromToken(authToken);
        if (userId == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        boolean deleted = tarefaService.deleteTarefa(id, userId);
        if (deleted) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}