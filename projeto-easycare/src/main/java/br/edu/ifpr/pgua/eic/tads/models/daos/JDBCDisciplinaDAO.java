package br.edu.ifpr.pgua.eic.tads.models.daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement; 
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.github.hugoperlin.results.Resultado;

import br.edu.ifpr.pgua.eic.tads.models.Disciplina;
import br.edu.ifpr.pgua.eic.tads.models.FabricaConexoes; 

public class JDBCDisciplinaDAO implements DisciplinaDAO {

    private FabricaConexoes fabrica;

    public JDBCDisciplinaDAO(FabricaConexoes fabrica){
        this.fabrica = fabrica;
    }

    @Override
    public Resultado<Disciplina> salvar(Disciplina disciplina){

        String sql = "INSERT INTO ConcluiAi_Disciplinas (nome, descricao) VALUES (?, ?)";


        try (Connection con = fabrica.getConnection();
            PreparedStatement pstm = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstm.setString(1, disciplina.getNome());
            pstm.setString(2, disciplina.getDescricao());

            int rows = pstm.executeUpdate();

            if (rows == 1) {
                //  Busca o ID gerado pelo banco
                try (ResultSet rs = pstm.getGeneratedKeys()) {
                    if (rs.next()) {
                        disciplina.setId(rs.getInt(1)); 
                    }
                }
                return Resultado.sucesso("Disciplina cadastrada!", disciplina);
            } else{
                return Resultado.erro("Problema ao cadastrar disciplina!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Resultado.erro(e.getMessage());
        }
    }

    @Override
    public Resultado<List<Disciplina>> listarTodos(){
        List<Disciplina> lista = new ArrayList<>();
        String sql = "SELECT id_disciplinas, nome, descricao FROM ConcluiAi_Disciplinas"; 

        try (Connection con = fabrica.getConnection();
             PreparedStatement pstm = con.prepareStatement(sql);
             ResultSet result = pstm.executeQuery()) {
            
            while (result.next()) {
                int id = result.getInt("id_disciplinas"); // Usando id_disciplinas
                String nome = result.getString("nome");
                String descricao = result.getString("descricao");

                Disciplina disciplina = new Disciplina(id, nome, descricao);
                lista.add(disciplina);
            }

            return Resultado.sucesso("Lista de Disciplinas", Collections.unmodifiableList(lista));

        } catch (Exception e) {
            e.printStackTrace();
            return Resultado.erro(e.getMessage());
        }
    }

    @Override
    public Resultado<Void> atualizar(Disciplina disciplina){
        
        String sql = "UPDATE ConcluiAi_Disciplinas SET nome = ?, descricao = ? WHERE id_disciplinas = ?";

        try (Connection con = fabrica.getConnection();
             PreparedStatement pstm = con.prepareStatement(sql)) { 
            
            pstm.setString(1, disciplina.getNome());
            pstm.setString(2, disciplina.getDescricao());
            pstm.setInt(3, disciplina.getId());

            int rows = pstm.executeUpdate();

            if (rows == 1) {
                return Resultado.sucesso("Disciplina atualizada com sucesso!", null);
            } else {
                return Resultado.erro("Disciplina não encontrada ou problema na atualização!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Resultado.erro(e.getMessage());
        }
    }

    @Override
    public Resultado<Void> deletar(int id){
    
        String sql = "DELETE FROM ConcluiAi_Disciplinas WHERE id_disciplinas = ?";

        try (Connection con = fabrica.getConnection();
             PreparedStatement pstm = con.prepareStatement(sql)) {

            pstm.setInt(1, id);

            int rows = pstm.executeUpdate();
            
            if (rows == 1) {
                return Resultado.sucesso("Disciplina excluída com sucesso!", null);
            } else {
                return Resultado.erro("Disciplina não encontrada ou não pôde ser excluída.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            if (e.getMessage().contains("FOREIGN KEY")) {
                //Tratamento de erro de Chave Estrangeira
                return Resultado.erro("Não é possível excluir esta disciplina, pois existem Tópicos ou Atividades associadas a ela.");
            }
            return Resultado.erro(e.getMessage());
        }
    }

    @Override
    public Resultado<List<Disciplina>> listarDisciplinasPorAluno(int idAluno){
        List<Disciplina> lista = new ArrayList<>();
        
        String sql = "SELECT d.id_disciplinas, d.nome, d.descricao "
                + "FROM ConcluiAi_Disciplinas d "
                + "JOIN ConcluiAi_Informa_Disciplina id ON d.id_disciplinas = id.id_disciplina "
                + "WHERE id.id_usuario = ?"; 

        try (Connection con = fabrica.getConnection();
            PreparedStatement pstm = con.prepareStatement(sql)) {

            pstm.setInt(1, idAluno);

            try (ResultSet result = pstm.executeQuery()) {
                while (result.next()) {
                    int id = result.getInt("id_disciplinas");
                    String nome = result.getString("nome");
                    String descricao = result.getString("descricao");

                    Disciplina disciplina = new Disciplina(id, nome, descricao);
                    lista.add(disciplina);
                }
            }
            return Resultado.sucesso("Lista de Disciplinas do Aluno", Collections.unmodifiableList(lista));

        } catch (Exception e) {
            e.printStackTrace();
            return Resultado.erro("Erro ao listar disciplinas por aluno: " + e.getMessage());
        }
    }

    @Override
    public Resultado<Disciplina> buscarPorId(int idDisciplina) {
       
        String sql = "SELECT id_disciplinas, nome, descricao FROM ConcluiAi_Disciplinas WHERE id_disciplinas = ?";

        try (Connection con = fabrica.getConnection();
            PreparedStatement pstm = con.prepareStatement(sql)) {

            pstm.setInt(1, idDisciplina);

            try (ResultSet rs = pstm.executeQuery()) {
                if (rs.next()) {
                    int id = rs.getInt("id_disciplinas");
                    String nome = rs.getString("nome");
                    String descricao = rs.getString("descricao");

                    Disciplina disciplina = new Disciplina(id, nome, descricao);
                    return Resultado.sucesso("Disciplina encontrada com sucesso!", disciplina);
                } else {
                    return Resultado.erro("Disciplina com ID " + idDisciplina + " não encontrada.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Resultado.erro("Erro ao buscar a Disciplina: " + e.getMessage());
        }
    }
}