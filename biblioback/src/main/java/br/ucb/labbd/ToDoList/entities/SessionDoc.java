package br.ucb.labbd.ToDoList.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.Instant;

/**
 * Entidade que representa uma Sessão de Usuário armazenada no MongoDB.
 * O ID do documento é o próprio token de sessão (UUID).
 */
@Document(collection = "sessions")
public class SessionDoc {

    // O token (UUID) gerado no login é o ID do documento no Mongo
    @Id
    private String id; // O token em si (UUID)

    private String userId; // ID do usuário no banco de dados relacional (PostgreSQL/MySQL)
    private String userEmail;
    private String grupo;

    // Define o tempo de expiração. O índice garante remoção automática pelo Mongo.
    @Indexed(expireAfterSeconds = 0)
    private Instant expiresAt; 
    
    private Instant createdAt;

    // Construtor padrão
    public SessionDoc() {}

    // Getters e Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getGrupo() {
        return grupo;
    }

    public void setGrupo(String grupo) {
        this.grupo = grupo;
    }

    public Instant getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(Instant expiresAt) {
        this.expiresAt = expiresAt;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}
