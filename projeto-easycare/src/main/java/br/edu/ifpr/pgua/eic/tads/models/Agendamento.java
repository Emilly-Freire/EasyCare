package br.edu.ifpr.pgua.eic.tads.models;

import java.sql.Date;

import kotlin.contracts.ReturnsNotNull;

public class Agendamento {
    private int idAgendamento;
    private Paciente idpaciente;
    private Date data; //mudar para data e hora
    private String medico;
    private String crm;
    private String descricao;

    public Agendamento() {
        this.data = null;
    }

    public Agendamento(int idAgendamento, Paciente idpaciente, Date data, String medico, 
                        String crm, String descricao) {

        this.idAgendamento = idAgendamento;
        this.idpaciente = idpaciente;
        this.data = data;
        this.medico = medico;
        this.crm = crm;
        this.descricao = descricao;
    }

    //Getters

    public int getIdAgendamento() {
        return idAgendamento;
    }

    public Paciente getIdpaciente() {
        return idpaciente;
    }

    public Date getData() {
        return data;
    }

    public String getMedico() {
        return medico;
    }

    public String getCrm() {
        return crm;
    }

    public String getDescricao() {
        return descricao;
    }

    //Setters

    public void setIdAgendamento() {
        this.idAgendamento = idAgendamento;
    }

    public void setIdpaciente() {
        this.idpaciente = idpaciente;
    }

    public void setData() {
        this.data = data;
    }

    public void setMedico() {
        this.medico = medico;
    }

    public void setCrm() {
        this.crm = crm;
    }

    public void setDescricao() {
        this.descricao = descricao;
    }

}
