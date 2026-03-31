package br.edu.ifpr.pgua.eic.tads.models.daos;

import java.util.List;

import com.github.hugoperlin.results.Resultado;

import br.edu.ifpr.pgua.eic.tads.models.Disciplina;

public interface DisciplinaDAO {
    
    Resultado<Disciplina> salvar(Disciplina disciplina);
    Resultado<List<Disciplina>> listarDisciplinasPorAluno(int idUsuario);
    Resultado<List<Disciplina>> listarTodos();

    Resultado<Void> atualizar(Disciplina disciplina);
    Resultado<Void> deletar(int id);
    Resultado<Disciplina> buscarPorId(int idDisciplina);
    

}