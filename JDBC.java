# Atividade---Java-JDBC
package aluno;

import dao.AlunoDAO;
import factory.CreateDB;
import model.Aluno;


public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        CreateDB createDB = new CreateDB();
        createDB.criarTabelaAluno();
        
        // teste de insercao do aluno
        
        Aluno aluno = new Aluno();
        aluno.setMatricula(213);
        aluno.setNome("Jorge");
        aluno.setInstituicao("FMM");
        
        // Cria o dao para conexao com banco de dados      
        AlunoDAO alunoDAO = new AlunoDAO();
        
        // salva os dados no banco
        alunoDAO.createAluno(aluno);
        
        //Atualiza as informações do aluno
        aluno.setNome("Jorge Fernandes"); // gerencia a aplicação 
        alunoDAO.updateAluno(aluno); //gerencia o banco de dados
        
        //Busca as informações cadastradas no banco de dados
        alunoDAO.readAluno(aluno.getMatricula()); 

        //Remove o aluno
        alunoDAO.deleteAluno(aluno);
        
        //Verifica se as informações foram mesmo removidas
        alunoDAO.readAluno(aluno.getMatricula());        
    }
    
}

package dao;

import factory.ConnectionFactory;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import model.Aluno;


public class AlunoDAO {
    
    
    public void createAluno(Aluno aluno){
        
        try{
            
            ConnectionFactory connectionFactory = new ConnectionFactory();
            Connection connection = connectionFactory.createConnection();
            
            String sql = "INSERT INTO tbl_aluno (matricula, nome, instituicao) values (?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            
            statement.setLong(1, aluno.getMatricula());
            statement.setString(2, aluno.getNome());
            statement.setString(3, aluno.getInstituicao());
            
            statement.execute();
            statement.close();
            
            System.out.println("O aluno " + aluno.getNome() + " foi gravado BD.");
            connection.close();

            
        }catch(SQLException e ){
            System.out.println("Erro na comunicacao com o BD");
            e.printStackTrace();
        }
        
    }
    
    public Aluno readAluno(long matricula){
        
        try{
            
            ConnectionFactory connectionFactory = new ConnectionFactory();
            Connection connection = connectionFactory.createConnection();
            
            String sql = "SELECT * FROM tbl_aluno WHERE matricula = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, matricula);
            
            ResultSet resultSet = preparedStatement.executeQuery();
            
            if (resultSet.next()){
                Aluno aluno = new Aluno();
                aluno.setNome(resultSet.getString("nome"));
                aluno.setMatricula(resultSet.getInt("matricula"));
                aluno.setInstituicao(resultSet.getString("instituicao"));
                
                System.out.println("O aluno " + aluno.getNome() + ", matricula n. " + aluno.getMatricula() + ", estuda no " + aluno.getInstituicao() + ".\n");
                return aluno;
                
            }else{
                
                System.out.println("Matricula nao encontrada");
                
            }
            
            connection.close();
            return null;
            
        }catch(SQLException e ){
            System.out.println("Erro na comunicacao com o BD");
            return null;
        }
        
    }
    
    public void updateAluno(Aluno aluno){
        
        try{
            
            ConnectionFactory connectionFactory = new ConnectionFactory();
            Connection connection = connectionFactory.createConnection();
            
            String sql = "UPDATE tbl_aluno set nome = ?, instituicao = ? WHERE matricula = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
                        
            preparedStatement.setString(1, aluno.getNome());
            preparedStatement.setString(2, aluno.getInstituicao());
            preparedStatement.setLong(3, aluno.getMatricula());
            
            preparedStatement.executeUpdate();
            preparedStatement.close();
            
            System.out.println("O Aluno " + aluno.getNome() + " foi atualizado no BD");
        }catch(SQLException e ){
            System.out.println("Erro na comunicacao com o BD");
        }
        
    }
    
    public void deleteAluno(Aluno aluno){
        
        try{
            
            ConnectionFactory connectionFactory = new ConnectionFactory();
            Connection connection = connectionFactory.createConnection();
            
            String sql = "DELETE FROM tbl_aluno WHERE matricula = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement = connection.prepareStatement(sql);
                        
            preparedStatement.setLong(1, aluno.getMatricula());
            
            preparedStatement.execute();
            preparedStatement.close();
            
            System.out.println("O Aluno " + aluno.getNome() + " foi removido do BD");
        }catch(SQLException e ){
            System.out.println("Erro na comunicacao com o BD");
        }
        
    }
    
    
}

package factory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {
    
    public ConnectionFactory() { } 
    
    public Connection createConnection() throws SQLException{
        
        String url = "jdbc:sqlite:banco_de_dados/banco_sqlite.db";
        return DriverManager.getConnection(url);
        
    }
    
}package factory;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;


public class CreateDB {
    
    
    
    public void criarTabelaAluno(){
        
        String sql = "CREATE TABLE IF NOT EXISTS tbl_aluno"
                + "("
                + "matricula integer PRIMARY KEY,"
                + "nome text NOT NULL,"
                + "instituicao text" 
                + ");";
        
        // executando o sql de criar tabelas
        
        
        try{
            
            ConnectionFactory connectionFactory = new ConnectionFactory();
            Connection connection = connectionFactory.createConnection();
            
            Statement statement = connection.createStatement();
            statement.execute(sql);
            
            System.out.println("Tabela Aluno Criada");
        }catch(SQLException e){
            //Mensagem de erro na criacao da tabela
            
            }
        }
    }
    
    package model;


public class Aluno {
    private long matricula;
    private String nome;
    private String instituicao;
  
    public Aluno() { }
    
    public long getMatricula() {
        return matricula;
    }

    public void setMatricula(long matricula) {
        this.matricula = matricula;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getInstituicao() {
        return instituicao;
    }

    public void setInstituicao(String instituicao) {
        this.instituicao = instituicao;
    }    
    
}
