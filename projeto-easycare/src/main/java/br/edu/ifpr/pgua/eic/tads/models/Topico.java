package br.edu.ifpr.pgua.eic.tads.models;

import java.util.ArrayList;
import java.util.List;

public class Topico {
    
    private int id;
    private String titulo;
    private String descricao;
    private Disciplina disciplina;
    private List<Atividade> atividades; 

    // Construtor 
    public Topico(int id, String titulo, String descricao, Disciplina disciplina) {
        this.id = id;
        this.titulo = titulo;
        this.descricao = descricao;
        this.disciplina = disciplina;
        this.atividades = new ArrayList<>(); 
    }

    
    public Topico(String titulo, String descricao, Disciplina disciplina) {
        // -1 ainda não foi salvo no banco
        this(-1, titulo, descricao, disciplina); 
    }

    // Getters e Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Disciplina getDisciplina() {
        return disciplina;
    }

    public void setDisciplina(Disciplina disciplina) {
        this.disciplina = disciplina;
    }
    
    public List<Atividade> getAtividades() {
        return atividades;
    }

    // Opcional: Setter
    public void setAtividades(List<Atividade> atividades) {
        this.atividades = atividades;
    }


    @Override
    public String toString() {
        return "ID: " + id + 
            " | Título: " + titulo + 
            " | Descrição: " + descricao + 
            " | Disciplina: " + (disciplina != null ? disciplina.getNome() : "N/A"); 
    }
}