package br.edu.ifpr.pgua.eic.tads.controllers;

import com.github.hugoperlin.results.Resultado;

import br.edu.ifpr.pgua.eic.tads.models.Aluno; 
import br.edu.ifpr.pgua.eic.tads.models.Disciplina;
import br.edu.ifpr.pgua.eic.tads.models.repository.DisciplinaRepository;
import io.javalin.http.Context;
import io.javalin.http.Handler;

public class CadastroDisciplinaController {

    private DisciplinaRepository repositorio;

    public CadastroDisciplinaController(DisciplinaRepository repositorio) {
        this.repositorio = repositorio;
    }

    public Handler postCadastrar = (Context ctx)->{ 
        String nome = ctx.formParam("nome");
        String descricao = ctx.formParam("descricao");
                        
        Aluno alunoLogado = ctx.sessionAttribute("aluno");

        if (alunoLogado == null) {
            ctx.redirect("/login");
            return;
        }

        Resultado<Disciplina> resultado = repositorio.cadastrar(nome, descricao, alunoLogado);

        // Se o cadastro foi um sucesso, redirecionamos para a lista
        if (resultado.foiSucesso()) { 
            ctx.sessionAttribute("sucesso", resultado.getMsg());
            ctx.redirect("/disciplinas");
        } else{
            // Se falhou, armazenamos o erro na sessão e redirecionamos para /disciplinas
            ctx.sessionAttribute("erro", resultado.getMsg());
            ctx.sessionAttribute("nomeErro", nome);
            ctx.sessionAttribute("descricaoErro", descricao);
            ctx.redirect("/disciplinas");
        }
    };
    
    public Handler getEditar = (Context ctx) -> {
        int idDisciplina = Integer.parseInt(ctx.pathParam("id"));
        
        // Coloca o ID da disciplina que será editada na sessão
        ctx.sessionAttribute("disciplinaIdParaEdicao", idDisciplina);
        
        // Redireciona para a rota principal 
        ctx.redirect("/disciplinas"); 
    };

    public Handler postEditar = (Context ctx) -> {
        int idDisciplina = Integer.parseInt(ctx.pathParam("id"));
        String novoNome = ctx.formParam("nome");
        String novaDescricao = ctx.formParam("descricao");
        
        // Busca a disciplina original 
        Resultado<Disciplina> buscaOriginal = repositorio.buscarPorId(idDisciplina);
        
        if (buscaOriginal.foiErro()) {
            ctx.sessionAttribute("erro", "Erro ao encontrar disciplina para atualização.");
            ctx.redirect("/disciplinas"); 
            return;
        }
        
        Disciplina disciplinaAtualizada = buscaOriginal.comoSucesso().getObj();
        
        // Atualiza os dados do objeto
        disciplinaAtualizada.setNome(novoNome);
        disciplinaAtualizada.setDescricao(novaDescricao);
        
        Resultado<Void> resultado = repositorio.atualizar(disciplinaAtualizada);

        if (resultado.foiSucesso()) {
            ctx.sessionAttribute("sucesso", resultado.getMsg());
            ctx.redirect("/disciplinas");
            ctx.sessionAttribute("disciplinaIdParaEdicao", null); 
        } else {
            ctx.sessionAttribute("erro", resultado.getMsg());
            ctx.sessionAttribute("disciplinaIdParaEdicao", idDisciplina); 
            ctx.redirect("/disciplinas");
        }
    };

    public Handler postExcluir = (Context ctx) -> {
        int idDisciplina = Integer.parseInt(ctx.pathParam("id"));

        // Executa a exclusão
        Resultado<Void> resultado = repositorio.excluir(idDisciplina);

        if (resultado.foiSucesso()) {
            ctx.sessionAttribute("sucesso", resultado.getMsg());
        } else {
            // Em caso de erro.
            ctx.sessionAttribute("erro", resultado.getMsg()); 
        }

        // Redireciona para a lista principal
        ctx.redirect("/disciplinas");
    };
}