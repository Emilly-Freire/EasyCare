package br.edu.ifpr.pgua.eic.tads.models.daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.github.hugoperlin.results.Resultado;

import br.edu.ifpr.pgua.eic.tads.models.FabricaConexoes;

public class JDBCInformacaoDisciplinaDAO implements InformacaoDisciplinaDAO {
    private FabricaConexoes fabrica;

    public JDBCInformacaoDisciplinaDAO(FabricaConexoes fabrica) {
        this.fabrica = fabrica;
    }

    @Override
    public Resultado<Void> salvarRelacao(int id_disciplina, int id_usuario) {
        // SQL para a tabela intermediária
        String sql = "INSERT INTO ConcluiAi_Informa_Disciplina (id_disciplina, id_usuario) VALUES (?, ?)";

        try (Connection con = fabrica.getConnection();
            PreparedStatement pstm = con.prepareStatement(sql)) {

            pstm.setInt(1, id_disciplina);
            pstm.setInt(2, id_usuario);

            int rows = pstm.executeUpdate();

            if (rows == 1) {
                return Resultado.sucesso("Relação Disciplina-Aluno cadastrada!", null);
            } else {
                return Resultado.erro("Problema ao cadastrar a relação Disciplina-Aluno.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return Resultado.erro("Erro ao tentar ligar a Disciplina ao Aluno: " + e.getMessage());
        }
    }
}