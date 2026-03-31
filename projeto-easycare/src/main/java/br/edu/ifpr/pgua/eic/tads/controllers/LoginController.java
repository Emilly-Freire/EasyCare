package br.edu.ifpr.pgua.eic.tads.controllers;

import com.github.hugoperlin.results.Resultado;

import br.edu.ifpr.pgua.eic.tads.models.Aluno;
import br.edu.ifpr.pgua.eic.tads.models.repository.AlunoRepository;
import io.javalin.http.Handler;

public class LoginController {

    private AlunoRepository repositorio;

    public LoginController(AlunoRepository repositorio){
        this.repositorio = repositorio;
    }

    public Handler get = (ctx)->{
        // Pega mensagens de erro da sessão e as remove
        String erro = ctx.sessionAttribute("erro");
        if (erro != null) {
            ctx.attribute("erro", erro);
            ctx.sessionAttribute("erro", null); // Limpa o erro da sessão
        }
        
        ctx.render("login.html");
    };

    public Handler login = (ctx) -> {
        // pega os dados do formulário
        String email = ctx.formParam("email");
        String senha = ctx.formParam("senha");

        Resultado<Aluno> resultado = repositorio.autenticar(email, senha);

        if(resultado.foiSucesso()){
            Aluno alunoLogado = resultado.comoSucesso().getObj();
            
            // Salva o aluno na sessão
            ctx.sessionAttribute("aluno", alunoLogado); 
            
            // Redireciona para a página principal
            ctx.redirect("/disciplinas"); 
        } else {
            // Salva a mensagem de erro na sessão pq falhou
            ctx.sessionAttribute("erro", resultado.getMsg());
            
            // Redireciona para o handler GET /login 
            ctx.redirect("/login"); 
        }
    };

}