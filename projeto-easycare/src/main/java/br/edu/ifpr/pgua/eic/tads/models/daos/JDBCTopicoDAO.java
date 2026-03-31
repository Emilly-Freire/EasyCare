package br.edu.ifpr.pgua.eic.tads.models.daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.github.hugoperlin.results.Resultado;

import br.edu.ifpr.pgua.eic.tads.models.Atividade; 
import br.edu.ifpr.pgua.eic.tads.models.Disciplina;
import br.edu.ifpr.pgua.eic.tads.models.FabricaConexoes;
import br.edu.ifpr.pgua.eic.tads.models.Topico;

public class JDBCTopicoDAO implements TopicoDAO {

    private FabricaConexoes fabrica;
    
    public JDBCTopicoDAO(FabricaConexoes fabrica) {
        this.fabrica = fabrica;
    }

    @Override
    public Resultado<Topico> salvar(Topico topico) {
       
        String sql = "INSERT INTO ConcluiAi_Topicos (titulo, descricao, id_disciplinas) VALUES (?, ?, ?)";
        
        try (Connection con = fabrica.getConnection();
            PreparedStatement pstm = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) { 
            
            // Verifica a disciplina antes de tentar inserir
            if (topico.getDisciplina() == null || topico.getDisciplina().getId() <= 0) {
                return Resultado.erro("O Tópico deve estar associado a uma Disciplina válida para ser salvo.");
            }

            pstm.setString(1, topico.getTitulo());
            pstm.setString(2, topico.getDescricao());
            pstm.setInt(3, topico.getDisciplina().getId()); 
            pstm.executeUpdate();

            // Pega o ID gerado
            try (ResultSet rs = pstm.getGeneratedKeys()) {
                if (rs.next()) {
                    topico.setId(rs.getInt(1)); 
                }
            }
            return Resultado.sucesso("Tópico cadastrado com sucesso!", topico);

        } catch (Exception e) {
            e.printStackTrace();
            return Resultado.erro("Erro ao salvar tópico: " + e.getMessage());
        }
    }
    
    @Override
    public Resultado<Topico> buscarPorId(int id) {
        String sql = "SELECT t.id_topicos, t.titulo, t.descricao, d.id_disciplinas, d.nome AS nome_disciplina, d.descricao AS desc_disciplina " +
                    "FROM ConcluiAi_Topicos t " +
                    "JOIN ConcluiAi_Disciplinas d ON t.id_disciplinas = d.id_disciplinas " + 
                    "WHERE t.id_topicos = ?";

        try (Connection con = fabrica.getConnection();
            PreparedStatement pstm = con.prepareStatement(sql)) { 

            pstm.setInt(1, id);

            try (ResultSet result = pstm.executeQuery()) {
                if (result.next()) {
                    int idDisc = result.getInt("id_disciplinas");
                    String nomeDisciplina = result.getString("nome_disciplina");
                    String descDisciplina = result.getString("desc_disciplina");
                    Disciplina disciplina = new Disciplina(idDisc, nomeDisciplina, descDisciplina); 

                    int idTopico = result.getInt("id_topicos"); 
                    String titulo = result.getString("titulo");
                    String descricao = result.getString("descricao");
                    
                    Topico topico = new Topico(idTopico, titulo, descricao, disciplina); 
                    
                    return Resultado.sucesso("Tópico encontrado.", topico);
                } else {
                    return Resultado.erro("Tópico não encontrado.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Resultado.erro("Erro ao buscar tópico: " + e.getMessage());
        }
    }
    
    @Override
    public Resultado<List<Topico>> listarPorDisciplina(int idDisciplina) {
        Map<Integer, Topico> mapTopicos = new LinkedHashMap<>(); 
        
        // SQL para buscar Tópicos e suas Atividades associadas
        String sql = "SELECT "
                + "t.id_topicos, t.titulo, t.descricao AS desc_topico, "
                + "d.id_disciplinas, d.nome AS nome_disciplina, d.descricao AS desc_disciplina, "
                + "a.id_atividades, a.descricao AS desc_atividade, a.status AS status_atividade, a.data_entrega "
                + "FROM ConcluiAi_Topicos t "
                + "JOIN ConcluiAi_Disciplinas d ON t.id_disciplinas = d.id_disciplinas " 
                + "LEFT JOIN ConcluiAi_Atividades a ON t.id_topicos = a.id_topicos " 
                + "WHERE d.id_disciplinas = ? "
                + "ORDER BY t.id_topicos, a.id_atividades";

        try (Connection con = fabrica.getConnection();
            PreparedStatement pstm = con.prepareStatement(sql)) { 

            pstm.setInt(1, idDisciplina);

            try (ResultSet rs = pstm.executeQuery()) {
                while (rs.next()) {
                    int idTopico = rs.getInt("id_topicos");
                    
                    // Lógica de Agregação do Tópico
                    if (!mapTopicos.containsKey(idTopico)) {
                        
                        // Mapeamento da Disciplina
                        int idDisc = rs.getInt("id_disciplinas");
                        Disciplina disciplina = new Disciplina(idDisc, rs.getString("nome_disciplina"), rs.getString("desc_disciplina"));
                        
                        // Criação do Tópico
                        Topico novoTopico = new Topico(
                            idTopico, 
                            rs.getString("titulo"), 
                            rs.getString("desc_topico"), 
                            disciplina
                        ); 
                        mapTopicos.put(idTopico, novoTopico);
                    }
                    
                    Topico topicoAtual = mapTopicos.get(idTopico); 
                    
                    // Lógica de Mapeamento da Atividade
                    int idAtividade = rs.getInt("id_atividades");
                    if (idAtividade != 0) { 
                        
                        boolean statusAtividadeJava = (rs.getInt("status_atividade") == 1); 

                        Atividade atividade = new Atividade(
                            idAtividade,
                            rs.getString("desc_atividade"), 
                            statusAtividadeJava,
                            rs.getDate("data_entrega"), 
                            topicoAtual 
                        );
                        
                        topicoAtual.getAtividades().add(atividade); 
                    }
                }
            }

            return Resultado.sucesso("Lista de Tópicos e Atividades da Disciplina", new ArrayList<>(mapTopicos.values()));

        } catch (Exception e) {
            e.printStackTrace();
            return Resultado.erro("Erro ao listar tópicos por disciplina: " + e.getMessage());
        }
    } 

    @Override
    public Resultado<List<Topico>> listarTodos() {
        List<Topico> lista = new ArrayList<>();

        String sql = "SELECT "
        + "t.id_topicos, t.titulo, t.descricao, "
        + "d.id_disciplinas, d.nome AS nome_disciplina, d.descricao AS desc_disciplina "
        + "FROM ConcluiAi_Topicos t "
        + "JOIN ConcluiAi_Disciplinas d ON t.id_disciplinas = d.id_disciplinas"; 

        try (Connection con = fabrica.getConnection();
        PreparedStatement pstm = con.prepareStatement(sql);
            ResultSet result = pstm.executeQuery()) { 

            while (result.next()) {
                int idTopico = result.getInt("id_topicos"); 
                String titulo = result.getString("titulo");
                String descricao = result.getString("descricao");

                int idDisciplina = result.getInt("id_disciplinas");
                String nomeDisciplina = result.getString("nome_disciplina");
                String descDisciplina = result.getString("desc_disciplina");

                Disciplina disciplina = new Disciplina(idDisciplina, nomeDisciplina, descDisciplina);
                Topico topico = new Topico(idTopico, titulo, descricao, disciplina); 
                lista.add(topico);
            }

            return Resultado.sucesso("Lista de Tópicos e Disciplinas", Collections.unmodifiableList(lista));

        } catch (Exception e) {
            e.printStackTrace();
            return Resultado.erro(e.getMessage());
        }
    }

    @Override
    public Resultado<Void> atualizar(Topico topico){
        String sql = "UPDATE ConcluiAi_Topicos SET titulo = ?, descricao = ? WHERE id_topicos = ?";

        try (Connection con = fabrica.getConnection();
            PreparedStatement pstm = con.prepareStatement(sql)) { 

            pstm.setString(1, topico.getTitulo());
            pstm.setString(2, topico.getDescricao());
            pstm.setInt(3, topico.getId()); 

            int rows = pstm.executeUpdate();

            if (rows == 1) {
                return Resultado.sucesso("Tópico atualizado com sucesso!", null);
            } else {
                return Resultado.erro("Tópico não encontrado ou problema na atualização!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Resultado.erro(e.getMessage());
        }
    }

    @Override
    public Resultado<Void> deletar(int id){
        // Remove apenas o Tópico
        String sqlDeleteTopico = "DELETE FROM ConcluiAi_Topicos WHERE id_topicos = ?";

        try (Connection con = fabrica.getConnection();
            PreparedStatement pstmTopico = con.prepareStatement(sqlDeleteTopico)) {
            
            pstmTopico.setInt(1, id);
            int rows = pstmTopico.executeUpdate();

            if (rows == 1) {
                return Resultado.sucesso("Tópico excluído com sucesso!", null);
            } else {
                return Resultado.erro("Tópico não encontrado ou não pôde ser excluído.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            return Resultado.erro("Erro ao excluir tópico: " + e.getMessage());
        }
    }
}