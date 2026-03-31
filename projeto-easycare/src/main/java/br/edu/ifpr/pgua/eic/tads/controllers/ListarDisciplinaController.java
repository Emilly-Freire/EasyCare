package br.edu.ifpr.pgua.eic.tads.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.github.hugoperlin.results.Resultado;

import br.edu.ifpr.pgua.eic.tads.models.Aluno;
import br.edu.ifpr.pgua.eic.tads.models.Disciplina;
import br.edu.ifpr.pgua.eic.tads.models.Topico;
import br.edu.ifpr.pgua.eic.tads.models.repository.DisciplinaRepository;
import br.edu.ifpr.pgua.eic.tads.models.repository.TopicoRepository;
import io.javalin.http.Context;
import io.javalin.http.Handler;

public class ListarDisciplinaController {

    private DisciplinaRepository disciplinaRepository;
    private TopicoRepository topicoRepository; 

    public ListarDisciplinaController(DisciplinaRepository disciplinaRepository, TopicoRepository topicoRepository) {
        this.disciplinaRepository = disciplinaRepository;
        this.topicoRepository = topicoRepository;
    }

    public Handler getListar = (Context ctx) -> {
        Map<String, Object> dados = new HashMap<>();
        Aluno aluno = ctx.sessionAttribute("aluno");

        if (aluno == null) {
            ctx.sessionAttribute("erro", "Sessão expirada. Faça login novamente.");
            ctx.redirect("/login");
            return;
        }

        int idAluno = aluno.getId();

        // Busca as disciplinas
        Resultado<List<Disciplina>> resultadoLista = disciplinaRepository.listarPorAluno(idAluno); 

        List<Disciplina> disciplinas = null;
        if (resultadoLista.foiSucesso()) {
            disciplinas = resultadoLista.comoSucesso().getObj();
        } else {
            dados.put("erro", resultadoLista.getMsg());
        }

        // Verifica se há uma disciplina em edição na sessão
        Integer idDisciplinaParaEdicao = ctx.sessionAttribute("disciplinaIdParaEdicao");
        
        if (idDisciplinaParaEdicao != null) {
            Resultado<Disciplina> resultadoEdicao = disciplinaRepository.buscarPorId(idDisciplinaParaEdicao);
            if (resultadoEdicao.foiSucesso()) {
                // Injeta a disciplina para preencher o formulário fixo
                dados.put("disciplinaEditando", resultadoEdicao.comoSucesso().getObj());
            } else {
                // Não conseguir buscar a disciplina
                dados.put("aviso", "Erro ao carregar dados para edição: " + resultadoEdicao.getMsg());
            }
        }

        // Verifica mensagens de sucesso/erro da sessão
        String sucesso = ctx.sessionAttribute("sucesso");
        String erro = ctx.sessionAttribute("erro");
        
        if (sucesso != null) {
            dados.put("sucesso", sucesso);
            ctx.sessionAttribute("sucesso", null);
        }
        if (erro != null) {
            dados.put("erro", erro);
            ctx.sessionAttribute("erro", null);
        }

        dados.put("disciplinas", disciplinas);
        dados.put("titulo", "Minhas Disciplinas");

        // Renderiza a tela de listagem
        ctx.render("disciplinas.html", dados);
    };


    public Handler getDetalhe = (Context ctx) -> {
        Map<String, Object> dados = new HashMap<>();
        Aluno aluno = ctx.sessionAttribute("aluno");

        if (aluno == null) {
            ctx.sessionAttribute("erro", "Sessão expirada. Faça login novamente.");
            ctx.redirect("/login");
            return;
        }

        // Obtém o ID da Disciplina
        int idDisciplina;
        try {
            idDisciplina = Integer.parseInt(ctx.pathParam("id"));
        } catch ( IllegalArgumentException e) {
            ctx.sessionAttribute("erro", "ID de disciplina inválido.");
            ctx.redirect("/disciplinas"); 
            return;
        }


        // Busca os dados da Disciplina
        Resultado<Disciplina> resultadoDisciplina = disciplinaRepository.buscarPorId(idDisciplina);

        if (resultadoDisciplina.foiErro()) {
            ctx.sessionAttribute("erro", resultadoDisciplina.getMsg());
            ctx.redirect("/disciplinas");
            return;
        }

        Disciplina disciplina = resultadoDisciplina.comoSucesso().getObj();

        // Busca a lista de Tópicos associados a Disciplina
        Resultado<List<Topico>> resultadoTopicos = topicoRepository.listarPorDisciplina(idDisciplina);

        List<Topico> topicos = null;
        if (resultadoTopicos.foiSucesso()) {
            topicos = resultadoTopicos.comoSucesso().getObj();
        } else {
            dados.put("aviso", resultadoTopicos.getMsg());
        }

        // Verifica se há um Tópico em edição na sessão
        Integer idTopicoParaEdicao = ctx.sessionAttribute("topicoIdParaEdicao");
        
        if (idTopicoParaEdicao != null) {
            Resultado<Topico> resultadoEdicao = topicoRepository.buscarPorId(idTopicoParaEdicao);
            if (resultadoEdicao.foiSucesso()) {
                // Injeta o tópico para preencher o formulário fixo
                dados.put("topicoEditando", resultadoEdicao.comoSucesso().getObj());
            } else {
                dados.put("aviso", "Erro ao carregar dados do tópico para edição: " + resultadoEdicao.getMsg());
            }
        }

        // Verifica mensagens de sucesso/erro da sessão
        String sucesso = ctx.sessionAttribute("sucesso");
        String erro = ctx.sessionAttribute("erro");
        
        if (sucesso != null) {
            dados.put("sucesso", sucesso);
            ctx.sessionAttribute("sucesso", null);
        }
        if (erro != null) {
            dados.put("erro", erro);
            ctx.sessionAttribute("erro", null);
        }
        
        // Preenche o mapa de dados para o template e renderiza
        dados.put("disciplina", disciplina);
        dados.put("topicos", topicos);
        dados.put("titulo", "Checklist: " + disciplina.getNome());

        // Renderiza a tela de detalhe
        ctx.render("topicos.html", dados); 
    };
}