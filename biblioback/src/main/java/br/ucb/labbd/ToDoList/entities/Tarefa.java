package br.ucb.labbd.ToDoList.entities;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

@Entity
@Table(name = "tarefas")
public class Tarefa {

    // Construtor vazio obrigatório para JPA
    public Tarefa() {} 
    
    // Construtor parcial (mantido por compatibilidade, mas idealmente usar setters)
    public Tarefa(String idTarefa, String titulo, Prioridade prioridade, Status status, String descricao) {
        this.idTarefa = idTarefa;
        this.titulo = titulo;
        this.prioridade = prioridade;
        this.status = status;
        this.descricao = descricao;
    }
    
    @Id
    @Column(name = "id_tarefa", length = 15) // DDL usa VARCHAR(15)
    private String idTarefa;

    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private Usuario usuario; // Usuário agora usa String ID

    private String titulo;

    @Column(columnDefinition = "TEXT")
    private String descricao;

    @Enumerated(EnumType.STRING)
    private Prioridade prioridade;

    @Enumerated(EnumType.STRING)
    private Status status;

    private LocalDate dataVencimento;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataConclusao;

    @PrePersist
    public void prePersist(){
        dataCriacao = LocalDateTime.now();
        // CORREÇÃO FINAL: Usando PENDENTE (ALL CAPS, sem acento)
        if (status==null) status = Status.PENDENTE;
        // CORREÇÃO FINAL: Usando MEDIA (ALL CAPS, sem acento)
        if (prioridade==null) prioridade = Prioridade.MEDIA;
    }

    // CORREÇÃO FINAL: ENUMS usando nomenclatura padrão Java (ALL CAPS, sem acento/espaço)
    // Prioridade ENUM('Baixa', 'Média', 'Alta')
    public enum Prioridade { BAIXA, MEDIA, ALTA }
    
    // Status ENUM('Pendente', 'Em Andamento', 'Concluída')
    public enum Status { PENDENTE, EM_ANDAMENTO, CONCLUIDA }

    // Construtores, Getters e Setters
    public String getIdTarefa(){return idTarefa;}
    public void setIdTarefa(String id){this.idTarefa=id;}
    public Usuario getUsuario(){return usuario;}
    public void setUsuario(Usuario usuario){this.usuario=usuario;} 
    public String getTitulo(){return titulo;}
    public void setTitulo(String t){this.titulo=t;}
    public String getDescricao(){return descricao;}
    public void setDescricao(String d){this.descricao=d;}
    public Prioridade getPrioridade(){return prioridade;}
    public void setPrioridade(Prioridade p){this.prioridade=p;}
    public Status getStatus(){return status;}
    public void setStatus(Status s){this.status=s;}
    public LocalDate getDataVencimento(){return dataVencimento;}
    public void setDataVencimento(LocalDate d){this.dataVencimento=d;}
    public LocalDateTime getDataCriacao(){return dataCriacao;}
    public LocalDateTime getDataConclusao(){return dataConclusao;}
    public void setDataConclusao(LocalDateTime d){this.dataConclusao=d;}
}