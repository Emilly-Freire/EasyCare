package br.edu.ifpr.pgua.eic.tads.models.daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Collections;
import java.util.List;

import com.github.hugoperlin.results.Resultado;

import br.edu.ifpr.pgua.eic.tads.models.Atividade;
import br.edu.ifpr.pgua.eic.tads.models.FabricaConexoes;

public class JDBCAtividadeDAO implements AtividadeDAO {

    private FabricaConexoes fabrica;

    public JDBCAtividadeDAO(FabricaConexoes fabrica) {
        this.fabrica = fabrica;
    }
    
    @Override
    public Resultado<Atividade> salvar(Atividade atividade) {

        String sql = "INSERT INTO ConcluiAi_Atividades (id_topicos, descricao, status, data_entrega) VALUES (?, ?, 0, NULL)";

        try (Connection con = fabrica.getConnection();
            PreparedStatement pstm = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            // Pega o ID do Tópico
            pstm.setInt(1, atividade.getTopico().getId()); 
            pstm.setString(2, atividade.getDescricao());
            pstm.executeUpdate();

            // Pega o ID gerado
            try (ResultSet rs = pstm.getGeneratedKeys()) {
                if (rs.next()) {
                    atividade.setId(rs.getInt(1)); 
                }
            }
            
            return Resultado.sucesso("Atividade cadastrada com sucesso!", atividade);

        } catch (Exception e) {
            e.printStackTrace();
            return Resultado.erro("Erro ao cadastrar atividade: " + e.getMessage()); 
        }
    }


    @Override
    public Resultado<Void> atualizarStatus(Atividade atividade) {

        String sql = "UPDATE ConcluiAi_Atividades SET status = ?, data_entrega = ? WHERE id_atividades = ?";

        try (Connection con = fabrica.getConnection();
            PreparedStatement pstm = con.prepareStatement(sql)) {

            pstm.setBoolean(1, atividade.isStatus());
            pstm.setDate(2, atividade.getDataEntrega()); 
            pstm.setInt(3, atividade.getId());

            int rows = pstm.executeUpdate();

            if (rows == 1) {
                return Resultado.sucesso("Status da atividade atualizado com sucesso!", null);
            } else {
                return Resultado.erro("Atividade não encontrada para atualização (ID: " + atividade.getId() + ")");
            }

        } catch (Exception e) {
            e.printStackTrace();
            return Resultado.erro("Erro ao atualizar status da atividade: " + e.getMessage());
        }
    }
    
    @Override
    public Resultado<List<Atividade>> listarTodos() {
        
        return Resultado.sucesso("Listagem de todas as atividades não implementada ou não necessária.", Collections.emptyList());
    }

    @Override
    public Resultado<Void> deletar(int id) {
        String sql = "DELETE FROM ConcluiAi_Atividades WHERE id_atividades = ?";

        try (Connection con = fabrica.getConnection();
            PreparedStatement pstm = con.prepareStatement(sql)) {

            pstm.setInt(1, id);
            int rows = pstm.executeUpdate();
            
            if (rows == 1) {
                return Resultado.sucesso("Atividade excluída com sucesso!", null);
            } else {
                return Resultado.erro("Atividade não encontrada ou não pôde ser excluída.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Resultado.erro("Erro ao deletar a atividade: " + e.getMessage());
        }
    }


    @Override
    public Resultado<Void> atualizar(Atividade atividade) {

        return Resultado.erro("Atualização completa de Atividade não implementada.");
    }
    

}