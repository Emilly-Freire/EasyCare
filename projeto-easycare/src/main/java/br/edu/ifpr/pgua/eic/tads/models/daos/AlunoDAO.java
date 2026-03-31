package br.edu.ifpr.pgua.eic.tads.models.daos;

import java.util.List;

import com.github.hugoperlin.results.Resultado;

import br.edu.ifpr.pgua.eic.tads.models.Aluno;

public interface AlunoDAO {

    Resultado<Aluno> salvar(Aluno aluno);
    Resultado<List<Aluno>> listarTodos();
    Resultado<Void> atualizar(Aluno aluno); 
    Resultado<Void> deletar(int id);
    Resultado<Aluno> buscarPorEmailESenha(String email, String senha);
}