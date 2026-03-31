package br.edu.ifpr.pgua.eic.tads.controllers;

import io.javalin.http.Handler;

public class LogoutController {

    // Handler para GET /logout
    public Handler logout = (ctx) -> {
        
        // Remove a sessão do aluno logado
        ctx.req().getSession().invalidate();
    
        ctx.sessionAttribute("sucesso", "Você desconectou com sucesso.");
        
        // Redireciona para a página inicial
        ctx.redirect("/"); 
    };
}