package br.edu.ifpr.pgua.eic.tads.models.repository;

import java.util.Collections;
import java.util.List;

import com.github.hugoperlin.results.Resultado;

import br.edu.ifpr.pgua.eic.tads.models.Aluno;
import br.edu.ifpr.pgua.eic.tads.models.Disciplina;
import br.edu.ifpr.pgua.eic.tads.models.daos.DisciplinaDAO;
import br.edu.ifpr.pgua.eic.tads.models.daos.InformacaoDisciplinaDAO;

public class DisciplinaRepository {

    private DisciplinaDAO dao;
    private InformacaoDisciplinaDAO informacaoDao;

    public DisciplinaRepository(DisciplinaDAO dao, InformacaoDisciplinaDAO informacaoDao){ 
        this.dao = dao;
        this.informacaoDao = informacaoDao;
    }

    public Resultado<Disciplina> cadastrar(String nome, String descricao, Aluno aluno){

        if (aluno == null || aluno.getId() <= 0) {
            return Resultado.erro("Aluno inválido. É preciso estar logado.");
        }

        Disciplina disciplina = new Disciplina(nome, descricao);
        Resultado<Disciplina> resultadoDisciplina = dao.salvar(disciplina);

        if (resultadoDisciplina.foiSucesso()) {

            Disciplina disciplinaSalva = resultadoDisciplina.comoSucesso().getObj();

            Resultado<Void> resultadoRelacao = informacaoDao.salvarRelacao(disciplinaSalva.getId(), aluno.getId());

            // Em caso de erro, desfaz o cadastro da disciplina
            if (resultadoRelacao.foiErro()) {
            dao.deletar(disciplinaSalva.getId()); 
            return Resultado.erro("Falha ao associar a Disciplina ao Aluno. Cadastro desfeito. Erro: " + resultadoRelacao.getMsg());
            }

            return Resultado.sucesso(resultadoDisciplina.getMsg() + " e associada ao Aluno.", disciplinaSalva);

        } else {
            return resultadoDisciplina;
        }
    }

    public Resultado<List<Disciplina>> listar(){
        return dao.listarTodos();
    }

    public Resultado<List<Disciplina>> listarPorAluno(int idAluno){
        return dao.listarDisciplinasPorAluno(idAluno);
    }

    public Resultado<Disciplina> buscarPorId(int idDisciplina) {
        return dao.buscarPorId(idDisciplina);
    }
    
    public List<Disciplina> getListar(){
        Resultado<List<Disciplina>> resultado = listar();
        if (resultado.foiSucesso()) {
            return resultado.comoSucesso().getObj();
        }
        return Collections.emptyList();
    }

    public Resultado<Void> atualizar(Disciplina disciplina){
        return dao.atualizar(disciplina);
    }
    
    public Resultado<Void> excluir(int id){
        return dao.deletar(id);
    }
}