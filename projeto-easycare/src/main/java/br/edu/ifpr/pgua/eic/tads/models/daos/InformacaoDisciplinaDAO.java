package br.edu.ifpr.pgua.eic.tads.models.daos;

import com.github.hugoperlin.results.Resultado;

public interface InformacaoDisciplinaDAO {
    
    //Salva a relação entre uma Disciplina e um Aluno na tabela ConcluiAi_Informa_Disciplina
    
    Resultado<Void> salvarRelacao(int idDisciplina, int idUsuario);
}