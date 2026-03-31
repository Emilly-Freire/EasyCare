package br.edu.ifpr.pgua.eic.tads.models.repository;

import java.sql.Date; 
import java.time.LocalDate;
import java.util.List;

import com.github.hugoperlin.results.Resultado;

import br.edu.ifpr.pgua.eic.tads.models.Atividade;
import br.edu.ifpr.pgua.eic.tads.models.Topico;
import br.edu.ifpr.pgua.eic.tads.models.daos.AtividadeDAO;

public class AtividadeRepository {

    private AtividadeDAO dao;

    public AtividadeRepository(AtividadeDAO dao){
        this.dao = dao;
    }

    public Resultado<Atividade> salvar(String descricao, Topico topico) {
        if (descricao == null || descricao.isBlank()) {
            return Resultado.erro("A descrição da atividade é obrigatória.");
        }
        if (topico == null || topico.getId() <= 0) {
            return Resultado.erro("A atividade deve ser associada a um Tópico válido.");
        }
        
        Atividade novaAtividade = new Atividade(descricao, topico);

        return dao.salvar(novaAtividade); 
    }

 
    public Resultado<List<Atividade>> listarTodos(){
        return dao.listarTodos();
    }
    
    public List<Atividade> getListarAtividades() {
        Resultado<List<Atividade>> resultado = listarTodos();
        if (resultado.foiSucesso()) {
            return resultado.comoSucesso().getObj();
        }
        return java.util.Collections.emptyList();
    }
    
    public Resultado<Void> concluirAtividade(int idAtividade) {
        boolean status = true;
        
        Date dataEntrega = Date.valueOf(LocalDate.now()); 
        Atividade atividade = new Atividade(idAtividade, null, status, dataEntrega, null); 
      
        return dao.atualizarStatus(atividade); 
    }
    
    public Resultado<Void> desmarcarAtividade(int idAtividade) {
        boolean status = false;
        Date dataEntrega = null;
        
        Atividade atividade = new Atividade(idAtividade, null, status, dataEntrega, null);
        
        return dao.atualizarStatus(atividade);
    }
    
    public Resultado<Void> deletar(int id) {
        return dao.deletar(id);
    }
}