package br.ucb.labbd.ToDoList.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexaoMySQL implements ConexaoBD {
    private static final String URL = "jdbc:mysql://localhost:3306/sistematarefas";
    private static final String USUARIO = "web_admin"; 
    private static final String SENHA = "senha123"; 
    
    @Override
    public Connection obterConexao() throws Exception {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            Connection conexao = DriverManager.getConnection(URL, USUARIO, SENHA);
            
            System.out.println("Conexão com o banco de dados estabelecida com sucesso!");
            return conexao;
        } catch (ClassNotFoundException e) {
            throw new Exception("Driver JDBC do MySQL não encontrado: " + e.getMessage());
        } catch (SQLException e) {
            throw new Exception("Erro ao conectar ao banco de dados: " + e.getMessage());
        }
    }
    
    @Override
    public void fecharConexao(Connection conexao) {
        if (conexao != null) {
            try {
                conexao.close();
                System.out.println("Conexão com o banco de dados fechada com sucesso!");
            } catch (SQLException e) {
                System.out.println("Erro ao fechar conexão com o banco de dados: " + e.getMessage());
            }
        }
    }
}
