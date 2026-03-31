package br.edu.ifpr.pgua.eic.tads.controllers;

import com.github.hugoperlin.results.Resultado;

import br.edu.ifpr.pgua.eic.tads.models.Disciplina;
import br.edu.ifpr.pgua.eic.tads.models.Topico;
import br.edu.ifpr.pgua.eic.tads.models.repository.DisciplinaRepository;
import br.edu.ifpr.pgua.eic.tads.models.repository.TopicoRepository;
import io.javalin.http.Context;
import io.javalin.http.Handler;

public class CadastroTopicoController {
    
    private TopicoRepository topicoRepository;
    private DisciplinaRepository disciplinaRepository; 

    public CadastroTopicoController(TopicoRepository topicoRepository, DisciplinaRepository disciplinaRepository) {
        this.topicoRepository = topicoRepository;
        this.disciplinaRepository = disciplinaRepository;
    }
    
    public Handler post = (Context ctx)->{
        String titulo = ctx.formParam("titulo"); 
        String descricao = ctx.formParam("descricao");
        
        int idDisciplina;
        try {
            // Passa ID da disciplina
            idDisciplina = Integer.parseInt(ctx.formParam("idDisciplina"));
        } catch (NumberFormatException e) {
            ctx.sessionAttribute("erro", "ID da Disciplina inválido.");
            ctx.redirect("/disciplinas"); 
            return;
        }
        
        Resultado<Disciplina> resultadoDisciplina = disciplinaRepository.buscarPorId(idDisciplina);
        
        if (resultadoDisciplina.foiErro()) {
            ctx.sessionAttribute("erro", resultadoDisciplina.getMsg());
            ctx.redirect("/disciplinas");
        }

        Disciplina disciplinaSelecionada = resultadoDisciplina.comoSucesso().getObj();
        
        Resultado<Topico> resultado = topicoRepository.cadastrar(titulo, descricao, disciplinaSelecionada);

        if (resultado.foiSucesso()) {
            ctx.sessionAttribute("sucesso", resultado.getMsg());
        } else {
            ctx.sessionAttribute("erro", resultado.getMsg());
            ctx.sessionAttribute("tituloErro", titulo);
            ctx.sessionAttribute("descricaoErro", descricao);
        }
        
        
        ctx.redirect("/disciplinas/" + disciplinaSelecionada.getId()); 
    };

    public Handler getEditar = (Context ctx) -> {
        int idTopico = Integer.parseInt(ctx.pathParam("id"));
        
        Resultado<Topico> resultadoTopico = topicoRepository.buscarPorId(idTopico);
        
        if (resultadoTopico.foiErro()) {
            ctx.sessionAttribute("erro", resultadoTopico.getMsg());
            ctx.redirect("/disciplinas"); 
            return;
        }

        Topico topico = resultadoTopico.comoSucesso().getObj();
        int idDisciplinaRedirecionar = topico.getDisciplina().getId();

        // Armazena o ID do tópico na sessão
        ctx.sessionAttribute("topicoIdParaEdicao", idTopico);
        

        ctx.redirect("/disciplinas/" + idDisciplinaRedirecionar); 
    };

    public Handler postEditar = (Context ctx) -> {
        int idTopico = Integer.parseInt(ctx.pathParam("id"));
        String novoTitulo = ctx.formParam("titulo");
        String novaDescricao = ctx.formParam("descricao");
        
        // Busca o Tópico original para obter o objeto completo
        Resultado<Topico> buscaOriginal = topicoRepository.buscarPorId(idTopico);
        
        if (buscaOriginal.foiErro()) {
            ctx.sessionAttribute("erro", buscaOriginal.getMsg());
            ctx.redirect("/disciplinas"); 
            return;
        }

        Topico topicoOriginal = buscaOriginal.comoSucesso().getObj();
        int idDisciplinaRedirecionar = topicoOriginal.getDisciplina().getId();
        
        // Atualiza os dados do objeto Topico
        topicoOriginal.setTitulo(novoTitulo);
        topicoOriginal.setDescricao(novaDescricao);
        
        // Persiste a atualização
        Resultado<Void> resultado = topicoRepository.atualizar(topicoOriginal);

        if (resultado.foiSucesso()) {
            ctx.sessionAttribute("sucesso", "Tópico atualizado com sucesso!");
            ctx.sessionAttribute("topicoIdParaEdicao", null); 
        } else {
            // Em caso de erro, armazena o erro E o id do tópico na sessão
            ctx.sessionAttribute("erro", resultado.getMsg());
            ctx.sessionAttribute("topicoIdParaEdicao", idTopico); 
        }
        
        // Redireciona para o checklist da disciplina
        ctx.redirect("/disciplinas/" + idDisciplinaRedirecionar);
    };

    public Handler postExclusao = (Context ctx) -> {
        int idTopico = Integer.parseInt(ctx.pathParam("id"));

        // Busca a disciplina original antes de deletar
        int idDisciplinaRedirecionar = -1;
        Resultado<Topico> buscaOriginal = topicoRepository.buscarPorId(idTopico);
        if (buscaOriginal.foiSucesso()) {
            idDisciplinaRedirecionar = buscaOriginal.comoSucesso().getObj().getDisciplina().getId();
        }

        Resultado<Void> resultado = topicoRepository.excluir(idTopico);

        if (resultado.foiSucesso()) {
            ctx.sessionAttribute("sucesso", resultado.getMsg());
        } else {
            ctx.sessionAttribute("erro", resultado.getMsg());
        }

        // Redireciona para o checklist ou para a lista geral 
        if (idDisciplinaRedirecionar != -1) {
            ctx.redirect("/disciplinas/" + idDisciplinaRedirecionar);
        } else {
            ctx.redirect("/disciplinas");
        }
    };
}