package br.service;

import br.edu.ifpr.pgua.eic.tads.models.Aluno;
import io.javalin.http.Handler;

public class AuthHandler {

    public static Handler eAutenticado (){
        return ctx -> {
            Aluno aluno = ctx.sessionAttribute("aluno"); // Tenta obter o aluno da sessão
            
            if(aluno == null){
                // 1. Define mensagem de erro para ser exibida na tela de login
                ctx.sessionAttribute("erro", "Acesso restrito. Por favor, faça login para acessar esta página." ); 
                
                // 2. Redireciona
                ctx.redirect("/login");
    
            } 
        };
    }   
}