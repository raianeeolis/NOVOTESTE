package br.ucb.labbd.ToDoList.dao;

import java.sql.Connection;


public interface ConexaoBD {
    Connection obterConexao() throws Exception;
    
    void fecharConexao(Connection conexao);
}
