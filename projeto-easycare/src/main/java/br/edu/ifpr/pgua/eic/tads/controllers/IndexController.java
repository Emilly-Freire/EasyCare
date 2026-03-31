package br.edu.ifpr.pgua.eic.tads.controllers;

import java.util.HashMap;
import java.util.Map;

import br.edu.ifpr.pgua.eic.tads.models.Aluno; 
import io.javalin.http.Context;
import io.javalin.http.Handler;

public class IndexController {
    
    public Handler get = (Context ctx) -> {
        
        Map<String, Object> dados = new HashMap<>();

        // Coleta mensagens de sucesso/erro da sessão
        String sucesso = ctx.sessionAttribute("sucesso");
        String erro = ctx.sessionAttribute("erro");
        
        // Coloca no mapa para que o FreeMarker possa acessar
        dados.put("sucesso", sucesso);
        dados.put("erro", erro);

        // Limpa as mensagens da sessão
        ctx.sessionAttribute("sucesso", null);
        ctx.sessionAttribute("erro", null);
        
        // Coleta dados da sessão
        Aluno alunoLogado = ctx.sessionAttribute("aluno"); 
        if (alunoLogado != null) {
            dados.put("aluno", alunoLogado);
        }
        
        ctx.render("index.html", dados);
    };
}