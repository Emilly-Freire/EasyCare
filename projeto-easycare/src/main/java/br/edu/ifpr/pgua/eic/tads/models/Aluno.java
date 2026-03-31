package br.edu.ifpr.pgua.eic.tads.models;

public class Aluno {

    private int id;
    private String nome;
    private String email;
    private String senha;

    // Construtores
    public Aluno() {
    }

    public Aluno(String nome, String email, String senha) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
    }

    public Aluno(int id, String nome, String email, String senha) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.senha = senha;
    }

    // --- Getters ---
    public int getId() {
        return id;
    }
    public String getNome() { 
        return nome; 
    }
    public String getEmail() { 
        return email; 
    }
    public String getSenha() { 
        return senha; 
    }
    // --- Setters ---
    public void setId(int id) { 
        this.id = id; 
    }
    public void setNome(String nome) { 
        this.nome = nome; 
    }
    public void setEmail(String email) { 
        this.email = email; 
    }
    public void setSenha(String senha) { 
        this.senha = senha; 
    }

    @Override
    public String toString() {
        return "Nome: " + nome + " | Email: " + email;
    }
    
}
