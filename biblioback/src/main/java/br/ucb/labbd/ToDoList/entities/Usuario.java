package br.ucb.labbd.ToDoList.entities;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "usuarios")
public class Usuario {
    @Id
    @Column(name = "id_usuario", length = 15) // DDL usa VARCHAR(15)
    // CORREÇÃO: Alterado de Long para String
    private String idUsuario;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(name = "senha_hash", nullable = false)
    private String senhaHash;

    @Column(name = "data_nasc")
    private LocalDate dataNasc; // A coluna data_nasc deve ser mapeada aqui

    @ManyToOne
    @JoinColumn(name = "id_grupo")
    private GrupoUsuarios grupo;

    @Column(name = "ativo")
    private Boolean ativo = true;

    // Construtores, Getters e Setters
    public String getIdUsuario(){return idUsuario;}
    public void setIdUsuario(String id){this.idUsuario=id;}
    public String getNome(){return nome;}
    public void setNome(String n){this.nome=n;}
    public String getEmail(){return email;}
    public void setEmail(String e){this.email=e;}
    public String getSenhaHash(){return senhaHash;}
    public void setSenhaHash(String s){this.senhaHash=s;}
    public LocalDate getDataNasc(){return dataNasc;}
    public void setDataNasc(LocalDate d){this.dataNasc=d;}
    public GrupoUsuarios getGrupo(){return grupo;}
    public void setGrupo(GrupoUsuarios g){this.grupo=g;}
    public Boolean getAtivo(){return ativo;}
    public void setAtivo(Boolean a){this.ativo=a;}
}