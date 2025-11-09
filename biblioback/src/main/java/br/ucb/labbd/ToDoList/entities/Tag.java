package br.ucb.labbd.ToDoList.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "tags")
public class Tag {
    @Id
    @Column(name="id_tag", length=20)
    private String idTag;
    @Column(nullable=false, unique=true)
    private String nome;

    public String getIdTag(){return idTag;}
    public void setIdTag(String id){this.idTag=id;}
    public String getNome(){return nome;}
    public void setNome(String n){this.nome=n;}
}
