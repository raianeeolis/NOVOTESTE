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
@CrossOrigin(origins = "*")
public class TarefaController {

    private final TarefaService tarefaService;
    private final SessionRepository sessionRepo;

    @Value("${app.auth.header:X-Auth-Token}")
    private String AUTH_HEADER;

    public TarefaController(TarefaService tarefaService, SessionRepository sessionRepo) {
        this.tarefaService = tarefaService;
        this.sessionRepo = sessionRepo;
    }

    private String getUserIdFromToken(String token) {
        if (token == null || token.isEmpty()) return null;
        String cleanToken = token.replace("Bearer ", "");
        Optional<SessionDoc> session = sessionRepo.findById(cleanToken);
        return session.map(SessionDoc::getUserId).orElse(null);
    }

    @GetMapping
    public ResponseEntity<List<Tarefa>> getAllUserTasks(@RequestHeader(name = "X-Auth-Token") String token) {
        try {
            String userId = getUserIdFromToken(token);
            if (userId == null) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            List<Tarefa> tarefas = tarefaService.findAllByUserId(userId);
            return ResponseEntity.ok(tarefas);
        } catch (Exception e) {
            e.printStackTrace(); 
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping
    public ResponseEntity<Tarefa> createTask(@RequestBody Tarefa tarefa, @RequestHeader(name = "X-Auth-Token") String token) {
        String userId = getUserIdFromToken(token);
        if (userId == null) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        try {
            Tarefa t = tarefaService.createTarefa(tarefa, userId);
            return new ResponseEntity<>(t, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Tarefa> updateTask(@PathVariable String id, @RequestBody Tarefa tarefa, @RequestHeader(name = "X-Auth-Token") String token) {
        String userId = getUserIdFromToken(token);
        if (userId == null) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        return tarefaService.updateTarefa(id, tarefa, userId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable String id, @RequestHeader(name = "X-Auth-Token") String token) {
        String userId = getUserIdFromToken(token);
        if (userId == null) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        boolean deleted = tarefaService.deleteTarefa(id, userId);
        return deleted ? new ResponseEntity<>(HttpStatus.NO_CONTENT) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}