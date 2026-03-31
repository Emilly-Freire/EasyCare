package br.edu.ifpr.pgua.eic.tads.controllers;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import com.github.hugoperlin.results.Resultado;

import br.edu.ifpr.pgua.eic.tads.models.Topico;
import br.edu.ifpr.pgua.eic.tads.models.repository.AtividadeRepository;
import br.edu.ifpr.pgua.eic.tads.models.repository.DisciplinaRepository;
import br.edu.ifpr.pgua.eic.tads.models.repository.TopicoRepository;
import io.javalin.http.Context;
import io.javalin.http.Handler;


public class CadastroAtividadeController {

    private AtividadeRepository atividadeRepository;
    private TopicoRepository topicoRepository;
    private DisciplinaRepository disciplinaRepository;

    public CadastroAtividadeController(AtividadeRepository atividadeRepository, TopicoRepository topicoRepository, DisciplinaRepository disciplinaRepository) {
        this.atividadeRepository = atividadeRepository;
        this.topicoRepository = topicoRepository;
        this.disciplinaRepository = disciplinaRepository;
    }

    public Handler getCadastrar = (Context ctx) -> {
        Map<String, Object> dados = new HashMap<>();

        // Busca todas as disciplinas que o aluno cadastrou 
        dados.put("disciplinas", disciplinaRepository.getListar());
        dados.put("titulo", "Cadastro de Nova Atividade");

        ctx.render("cadastro_atividade.html", dados);

    };

    public Handler postCadastar = (Context ctx) -> {
        String descricao = ctx.formParam("descricao");
        String dataEntregaStr = ctx.formParam("data_entrega");

        // valida id_topicos
        String idTopicoStr = ctx.formParam("id_topicos");

        if (idTopicoStr == null || idTopicoStr.isBlank()) {
            ctx.status(400).result("id_topicos ausente no formulário");
            return;
        }

        int idTopico = Integer.parseInt(idTopicoStr);

        LocalDate dataEntrega = LocalDate.parse(dataEntregaStr);
        Topico topico = new Topico("","", null);
        topico.setId(idTopico);

        var resultadoCadastro = atividadeRepository.salvar(descricao, topico);

        if (resultadoCadastro.foiErro()) {
            ctx.sessionAttribute("erro", resultadoCadastro.getMsg());
        } else {
            ctx.sessionAttribute("sucesso", resultadoCadastro.getMsg());
        }

        // Redireciona para /disciplinas
        ctx.redirect("/disciplinas");
    };

    
    public Handler postAlternarStatus = (Context ctx) -> {
        int idAtividade = Integer.parseInt(ctx.pathParam("id"));

        String statusAtualStr = ctx.formParam("status_atual");
        boolean statusAtual = "true".equalsIgnoreCase(statusAtualStr) || "1".equals(statusAtualStr);
        
        Resultado<Void> resultado;
        
        if (statusAtual) {
            resultado = atividadeRepository.desmarcarAtividade(idAtividade);
        } else {
            resultado = atividadeRepository.concluirAtividade(idAtividade);
        }
        
    
        String urlRedirecionamento = "/disciplinas";
        
        try {
            // Tentativa de obter o ID da Disciplina para redirecionar 
            int idDisciplina = Integer.parseInt(ctx.formParam("id_disciplina_redirecionar"));
            urlRedirecionamento = "/disciplinas/" + idDisciplina;
        } catch (NullPointerException e) {
        }
        
        if (resultado.foiSucesso()) {
            ctx.sessionAttribute("sucesso", resultado.getMsg());
        } else {
            ctx.sessionAttribute("erro", resultado.getMsg());
        }

        ctx.redirect(urlRedirecionamento);
    };

    public Handler postExcluir = (Context ctx) -> {
        int idAtividade = Integer.parseInt(ctx.pathParam("id"));

        Resultado<Void> resultado = atividadeRepository.deletar(idAtividade);

        if (resultado.foiSucesso()) {
            ctx.sessionAttribute("sucesso", resultado.getMsg());
        } else {
            ctx.sessionAttribute("erro", resultado.getMsg());
        }

        // Redireciona de volta para a disciplina onde estava a atividade
        int idDisciplina = Integer.parseInt(ctx.formParam("id_disciplina_redirecionar"));
        ctx.redirect("/disciplinas/" + idDisciplina);
    };
}