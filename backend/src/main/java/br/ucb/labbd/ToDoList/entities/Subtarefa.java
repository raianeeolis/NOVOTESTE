package br.ucb.labbd.ToDoList.entities;

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
@Table(name = "subtarefas")
public class Subtarefa {
    @Id
    @Column(name = "id_subtarefa", length = 20)
    private String idSubtarefa;

    @ManyToOne
    @JoinColumn(name = "id_tarefa")
    private Tarefa tarefa;

    private String titulo;

    @Enumerated(EnumType.STRING)
    private Status status;

    private LocalDateTime dataCriacao;
    private LocalDateTime dataConclusao;

    public enum Status { Pendente, Concluída }

    @PrePersist
    public void prePersist(){ setDataCriacao(LocalDateTime.now()); if(getStatus()==null) setStatus(Status.Pendente); }

    public String getIdSubtarefa() {
        return idSubtarefa;
    }

    public void setIdSubtarefa(String idSubtarefa) {
        this.idSubtarefa = idSubtarefa;
    }

    public Tarefa getTarefa() {
        return tarefa;
    }

    public void setTarefa(Tarefa tarefa) {
        this.tarefa = tarefa;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public LocalDateTime getDataConclusao() {
        return dataConclusao;
    }

    public void setDataConclusao(LocalDateTime dataConclusao) {
        this.dataConclusao = dataConclusao;
    }
    
}
