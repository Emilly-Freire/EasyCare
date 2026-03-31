package br.edu.ifpr.pgua.eic.tads.models;

import io.javalin.http.sse.Emitter;

public class Paciente {
    private int idPaciente;
    private String nome;
    private String telefone;
    private String email;
    private String endereco;

    //Construtor vazio
    public Paciente() {
    }

    //Construtor vizualição no sistema
    public Paciente(String nome, String telefone, String email) {
        this.nome = nome;
        this.telefone = telefone;
        this.email = email;
    }

    //Construtor de cadastro
    public Paciente(int idPaciente, String nome, String telefone, String email, String endereco) {
        this.idPaciente = idPaciente;
        this.nome = nome;
        this.telefone = telefone;
        this.email = email;
        this.endereco = endereco;

    }

    //Getters
    public int getIdPaciente() {
        return idPaciente;
    }

    public String getNome() {
        return nome;
    }

    public String getTelefone() {
        return telefone;
    }

    public String getEmail() {
        return email;
    }

    public String getEndereco() {
        return endereco;
    }

    //Setters

    public void setIdPaciente() {
        this.idPaciente = idPaciente;
    }

    public void setNome() {
        this.nome = nome;
    }

    public void setTelefone() {
        this.telefone = telefone;
    }

    public void setEmail() {
        this.email = email;
    }

    public void setEndereco() {
        this.endereco = endereco;
    }

    @Override
    public String toString() {
        return "Nome: " + nome + "| Telefone: " + telefone + " | Email: " + email;
    }
}
