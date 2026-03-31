package br.edu.ifpr.pgua.eic.tads.models.repository;

import java.util.List;

import com.github.hugoperlin.results.Resultado;

import br.edu.ifpr.pgua.eic.tads.models.Disciplina;
import br.edu.ifpr.pgua.eic.tads.models.Topico;
import br.edu.ifpr.pgua.eic.tads.models.daos.TopicoDAO;

public class TopicoRepository {

    TopicoDAO dao;

    public TopicoRepository(TopicoDAO dao){
        this.dao = dao;
    }

    public Resultado<Topico> cadastrar(String titulo, String descricao, Disciplina disciplina){
        Topico topico = new Topico(titulo, descricao, disciplina);
        return dao.salvar(topico);

    }

    public Resultado<List<Topico>> listar(){
        return dao.listarTodos();
    }

    public List<Topico> getListar(){
        return null;
    }

    public Resultado<List<Topico>> listarPorDisciplina(int idDisciplina) {
        return dao.listarPorDisciplina(idDisciplina);
    }
    
    public Resultado<Topico> buscarPorId(int id){
        return dao.buscarPorId(id);
    }

    public Resultado<Void> atualizar(Topico topico){
        return dao.atualizar(topico);
    }

    public Resultado<Void> excluir(int id){
        return dao.deletar(id);
    }
}