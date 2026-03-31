package br.edu.ifpr.pgua.eic.tads.models;

public class Disciplina { 
    
    
    private int id; 
    private String nome;
    private String descricao;

    public Disciplina() { 
    }

    public Disciplina(String nome, String descricao) {
        this.nome = nome;
        this.descricao = descricao;
    }

    public Disciplina(int id, String nome, String descricao) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
    }

    // --- Getters ---
    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getDescricao() {
        return descricao;
    }
    
    // --- Setters ---
    public void setId(int id) {
        this.id = id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    @Override
    public String toString() {
        return "Nome: " + nome + " | Descrição: " + descricao;
    }
}