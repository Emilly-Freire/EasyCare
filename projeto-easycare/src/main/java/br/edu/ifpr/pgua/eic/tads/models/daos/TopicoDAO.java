package br.edu.ifpr.pgua.eic.tads.models.daos;

import java.util.List;

import com.github.hugoperlin.results.Resultado;

import br.edu.ifpr.pgua.eic.tads.models.Topico;

public interface TopicoDAO {

    // Crud
    Resultado<Topico> salvar(Topico topico);
    Resultado<List<Topico>> listarTodos();
    Resultado<Void> atualizar(Topico topico);
    Resultado<Void> deletar(int id);
    Resultado<List<Topico>> listarPorDisciplina(int idDisciplina);
    Resultado<Topico> buscarPorId(int id);
}