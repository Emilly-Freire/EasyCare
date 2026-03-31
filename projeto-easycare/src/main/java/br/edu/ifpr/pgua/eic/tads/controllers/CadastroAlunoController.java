package br.edu.ifpr.pgua.eic.tads.controllers;

import java.util.HashMap;
import java.util.Map;

import com.github.hugoperlin.results.Resultado;

import br.edu.ifpr.pgua.eic.tads.models.repository.AlunoRepository;
import io.javalin.http.Context;
import io.javalin.http.Handler;

public class CadastroAlunoController {

    private AlunoRepository repositorio;

    public CadastroAlunoController(AlunoRepository repositorio) {
        this.repositorio = repositorio;
    }


    public Handler get = (Context ctx) -> {
        // Se já estiver logado, redireciona para a lista de disciplinas
        if (ctx.sessionAttribute("aluno") != null) {
            ctx.redirect("/disciplinas");
            return;
        }
        ctx.render("cadastro_aluno.html");
    };

    public Handler post = (Context ctx) -> {
        String nome = ctx.formParam("nome");
        String email = ctx.formParam("email");
        String senha = ctx.formParam("senha");

        Resultado resultado = repositorio.cadastrar(nome, email, senha);
        
        if (resultado.foiSucesso()) {
            // Redireciona para Login com a mensagem
            ctx.sessionAttribute("sucesso", resultado.getMsg() + " Faça login para começar!");
            ctx.redirect("/login");
        } else {
            // Erro renderiza a página com o erro e os dados preenchidos
            Map<String, Object> dados = new HashMap<>();
            dados.put("erro", resultado.getMsg());
            dados.put("nomeValido", nome);
            dados.put("emailValido", email); 
            
            ctx.render("cadastro_aluno.html", dados);
        }
    };
}