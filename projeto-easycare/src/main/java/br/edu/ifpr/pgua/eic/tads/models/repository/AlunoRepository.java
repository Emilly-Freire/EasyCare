package br.edu.ifpr.pgua.eic.tads.models.repository;

import java.util.List;

import com.github.hugoperlin.results.Resultado;

import br.edu.ifpr.pgua.eic.tads.models.Aluno;
import br.edu.ifpr.pgua.eic.tads.models.Disciplina;
import br.edu.ifpr.pgua.eic.tads.models.daos.AlunoDAO;

public class AlunoRepository {
    
    private AlunoDAO dao;

    public AlunoRepository(AlunoDAO dao){
        this.dao = dao;
    }

    public Resultado<Aluno> cadastrar(String nome, String email, String senha){
        
        Aluno aluno = new Aluno(nome, email, senha);
        
        return dao.salvar(aluno);
    }
    
    public Resultado<Aluno> autenticar(String email, String senha){
        return dao.buscarPorEmailESenha(email, senha);
    }

    public Resultado<List<Aluno>> listar(){
        return dao.listarTodos();
    }

    public List<Disciplina> getListar(){
        return null;
    }
    

    public Resultado<Void> atualizar(Aluno aluno) {
        return dao.atualizar(aluno);
    }
    
    public Resultado<Void> deletar(int id) {
        return dao.deletar(id);
    }
    
}