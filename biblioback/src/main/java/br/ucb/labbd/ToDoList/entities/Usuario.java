package br.ucb.labbd.ToDoList.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id
    @Column(name = "id_usuario", length = 20)
    private String idUsuario;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(name = "senha_hash", nullable = false)
    private String senhaHash;

    @Column(name = "data_nasc")
    private LocalDate dataNasc;

    @ManyToOne
    @JoinColumn(name = "id_grupo")
    private GrupoUsuarios grupo;

    @Column(name = "ativo")
    private Boolean ativo = true;

    @OneToMany(mappedBy = "usuario")
    @JsonIgnore // evita loop infinito ao serializar tarefas
    private List<Tarefa> tarefas;

    // Getters e Setters
    public String getIdUsuario(){ return idUsuario; }
    public void setIdUsuario(String id){ this.idUsuario=id; }
    public String getNome(){ return nome; }
    public void setNome(String n){ this.nome=n; }
    public String getEmail(){ return email; }
    public void setEmail(String e){ this.email=e; }
    public String getSenhaHash(){ return senhaHash; }
    public void setSenhaHash(String s){ this.senhaHash=s; }
    public LocalDate getDataNasc(){ return dataNasc; }
    public void setDataNasc(LocalDate d){ this.dataNasc=d; }
    public GrupoUsuarios getGrupo(){ return grupo; }
    public void setGrupo(GrupoUsuarios g){ this.grupo=g; }
    public Boolean getAtivo(){ return ativo; }
    public void setAtivo(Boolean a){ this.ativo=a; }
    public List<Tarefa> getTarefas(){ return tarefas; }
    public void setTarefas(List<Tarefa> tarefas){ this.tarefas = tarefas; }
}
