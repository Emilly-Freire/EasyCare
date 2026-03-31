package br.edu.ifpr.pgua.eic.tads.models.daos;
import java.util.List;

import com.github.hugoperlin.results.Resultado;

import br.edu.ifpr.pgua.eic.tads.models.Atividade;

public interface AtividadeDAO {
    
    Resultado<Atividade> salvar(Atividade atividade); 
    Resultado<List<Atividade>> listarTodos();
    Resultado<Void> atualizarStatus(Atividade atividade);
    Resultado<Void> atualizar(Atividade atividade);
    Resultado<Void> deletar(int id);

}