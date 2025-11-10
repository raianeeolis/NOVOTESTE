package br.ucb.labbd.ToDoList.entities;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "grupos_usuarios")
public class GrupoUsuarios implements Serializable {
    @Id
    @Column(name = "id_grupo", length = 20)
    private String idGrupo;

    @Column(nullable = false, unique = true)
    private String nome;

    private String descricao;

    public String getIdGrupo() { return idGrupo; }
    public void setIdGrupo(String idGrupo) { this.idGrupo = idGrupo; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
}
