package br.edu.ifpr.pgua.eic.tads.models;

import java.sql.Date; 

public class Atividade { 
    
    private int id; 
    private Topico topico; 
    private String descricao; 
    private boolean status;
    private Date dataEntrega;

    // Construtor VAZIO
    public Atividade() { 
        this.status = false;
        this.dataEntrega = null;
    }
    
    // CONSTRUTOR PRINCIPAL
    public Atividade(int id, String descricao, boolean status, Date dataEntrega, Topico topico) {
        this.id = id;
        this.descricao = descricao;
        this.status = status;
        this.dataEntrega = dataEntrega;
        this.topico = topico;
    }

    // Construtor para CRIAR
    public Atividade(String descricao, Topico topico) {
        this(-1, descricao, false, null, topico);
    }

    // --- Getters ---
    public int getId() { 
        return id; 
    }
    
    public Topico getTopico() { 
        return topico; 
    }
    
    public String getDescricao() { 
        return descricao; 
    }
    
    public boolean isStatus() { 
        return status; 
    }
    
    public Date getDataEntrega() { 
        return dataEntrega; 
    }

    // --- Setters ---
    public void setId(int id) { 
        this.id = id; 
    }
    
    public void setTopico(Topico topico) { 
        this.topico = topico; 
    }
    
    public void setDescricao(String descricao) { 
        this.descricao = descricao; 
    }
    
    public void setStatus(boolean status) { 
        this.status = status; 
    }
    
    public void setDataEntrega(Date dataEntrega) { 
        this.dataEntrega = dataEntrega; 
    }

    @Override
    public String toString() {
        String dataStr = dataEntrega != null ? dataEntrega.toString() : "N/A";
        return "Descrição: " + descricao + 
            " | Entrega: " + dataStr + 
            " | Status: " + (status ? "Concluída" : "Pendente");
    }
}