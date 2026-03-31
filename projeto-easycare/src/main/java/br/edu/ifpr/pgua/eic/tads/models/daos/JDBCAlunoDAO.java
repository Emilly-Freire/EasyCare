package br.edu.ifpr.pgua.eic.tads.models.daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement; 
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.github.hugoperlin.results.Resultado;

import br.edu.ifpr.pgua.eic.tads.models.Aluno;
import br.edu.ifpr.pgua.eic.tads.models.FabricaConexoes;


public class JDBCAlunoDAO implements AlunoDAO {
    
    private FabricaConexoes fabrica;
    
    public JDBCAlunoDAO(FabricaConexoes fabrica){
        this.fabrica = fabrica;
    }

    @Override
    public Resultado<Aluno> salvar(Aluno aluno){

        try(Connection con = fabrica.getConnection();
            PreparedStatement pstm = con.prepareStatement(
                "INSERT INTO ConcluiAi_Usuario (nome, email, senha) VALUES (?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS) 
        ){
            pstm.setString(1, aluno.getNome());
            pstm.setString(2, aluno.getEmail());
            pstm.setString(3, aluno.getSenha()); 

            int rows = pstm.executeUpdate();

            if (rows == 1) { 
                try(ResultSet rs = pstm.getGeneratedKeys()){
                    if(rs.next()){
                        aluno.setId(rs.getInt(1)); 
                    }
                }
                return Resultado.sucesso("Aluno cadastrado com ID " + aluno.getId(), aluno);
            } else {
                return Resultado.erro("Problema ao cadastrar aluno!");
            }
        } catch (SQLException e){
            e.printStackTrace();
            return Resultado.erro(e.getMessage());
        }
    }

    @Override
    public Resultado<List<Aluno>> listarTodos() {
        List<Aluno> lista = new ArrayList<>();
        
        try (Connection con = fabrica.getConnection();
            PreparedStatement pstm = con.prepareStatement("SELECT * FROM ConcluiAi_Usuario");
            ResultSet result = pstm.executeQuery()) {

            while (result.next()) {
                int id = result.getInt("id_usuario"); 
                String nome = result.getString("nome");
                String email = result.getString("email");
                String senha = result.getString("senha"); 

                Aluno aluno = new Aluno(id, nome, email, senha);
                lista.add(aluno);
            }

            return Resultado.sucesso("Lista de Alunos", Collections.unmodifiableList(lista));
        } catch (SQLException e){
            e.printStackTrace();
            return Resultado.erro(e.getMessage());
        }
    }

    
    @Override
    public Resultado<Aluno> buscarPorEmailESenha(String email, String senha) {
    
        String sql = "SELECT id_usuario, nome, email FROM ConcluiAi_Usuario WHERE email = ? AND senha = ?";
        
        try (Connection con = fabrica.getConnection();
            PreparedStatement pstm = con.prepareStatement(sql)) {

            pstm.setString(1, email);
            pstm.setString(2, senha); 
            
            try (ResultSet rs = pstm.executeQuery()) {
                if (rs.next()) {
                    int id = rs.getInt("id_usuario");
                    String nome = rs.getString("nome");
                    String emailRetornado = rs.getString("email");
                    
                    Aluno aluno = new Aluno(id, nome, emailRetornado, null); 
                    return Resultado.sucesso("Login realizado com sucesso!", aluno);
                } else {
                    return Resultado.erro("E-mail ou senha inválidos.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return Resultado.erro("Erro ao tentar autenticar: " + e.getMessage());
        }
    }

    @Override
    public Resultado<Void> atualizar(Aluno aluno){
        String sql = "UPDATE ConcluiAi_Usuario SET nome = ?, email = ?, senha = ? WHERE id_usuario = ?";

        try (Connection con = fabrica.getConnection();
            PreparedStatement pstm = con.prepareStatement(sql)) { 
            
            pstm.setString(1, aluno.getNome());
            pstm.setString(2, aluno.getEmail());
            pstm.setString(3, aluno.getSenha());
            pstm.setInt(4, aluno.getId()); 

            int rows = pstm.executeUpdate();

            if (rows == 1) {
                return Resultado.sucesso("Aluno atualizado com sucesso!", null);
            } else {
                return Resultado.erro("Aluno não encontrado ou problema na atualização!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return Resultado.erro(e.getMessage());
        }
    }

    @Override
    public Resultado<Void> deletar(int id){
        String sql = "DELETE FROM ConcluiAi_Usuario WHERE id_usuario = ?";

        try (Connection con = fabrica.getConnection();
            PreparedStatement pstm = con.prepareStatement(sql)) {

            pstm.setInt(1, id);

            int rows = pstm.executeUpdate();
            
            if (rows == 1) {
                return Resultado.sucesso("Aluno excluído com sucesso!", null);
            } else {
                return Resultado.erro("Aluno não encontrado ou não pôde ser excluído.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            //alunho possui dados associados no checklist
            if (e.getMessage().contains("FOREIGN KEY")) {
                return Resultado.erro("Não é possível excluir o Aluno, pois ele possui dados associados no checklist.");
            }
            return Resultado.erro(e.getMessage());
        }
    }
}