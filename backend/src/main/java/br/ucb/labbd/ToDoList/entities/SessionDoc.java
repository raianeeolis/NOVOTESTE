package br.ucb.labbd.ToDoList.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.Instant; 

@Document(collection = "sessions")
public class SessionDoc {

    @Id
    private String token; 
    
    private String userId;
    private String userEmail;     
    private String grupo;         
    private Instant createdAt;   
    private Instant expiresAt;   

    public SessionDoc() {}

    public String getToken() { return token; }
    public String getUserId() { return userId; }
    public String getUserEmail() { return userEmail; }
    public String getGrupo() { return grupo; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getExpiresAt() { return expiresAt; }

    public void setToken(String token) { this.token = token; }
    public void setUserId(String userId) { this.userId = userId; }
    public void setUserEmail(String userEmail) { this.userEmail = userEmail; }  
    public void setGrupo(String grupo) { this.grupo = grupo; }                  
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; } 
    public void setExpiresAt(Instant expiresAt) { this.expiresAt = expiresAt; } 
}