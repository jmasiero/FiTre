package com.masidev.fitre.data.entidade;

import java.io.Serializable;

/**
 * Created by jmasiero on 22/09/15.
 */
public class Personal implements Serializable{
    private String CPF;
    private String Nome;
    private String CREF;
    private String Senha;

    private Academia academia;

    @Override
    public String toString() {
        return Nome;
    }

    public Personal(){
    }

    public Personal(String Cpf){
        this.CPF = Cpf;
    }

    public Personal(String Cpf, String Nome, String Cref){
        this.CPF = Cpf;
        this.Nome = Nome;
        this.CREF = Cref;
    }

    public Personal(String Cpf, String Nome, String Cref, String Senha){
        this.CPF = Cpf;
        this.Nome = Nome;
        this.CREF = Cref;
        this.Senha = Senha;
    }

    public String getCPF() {
        return CPF;
    }

    public void setCPF(String CPF) {
        this.CPF = CPF;
    }

    public String getNome() {
        return Nome;
    }

    public void setNome(String nome) {
        Nome = nome;
    }

    public String getCREF() {
        return CREF;
    }

    public void setCREF(String CREF) {
        this.CREF = CREF;
    }

    public String getSenha() {
        return Senha;
    }

    public void setSenha(String senha) {
        Senha = senha;
    }

    public Academia getAcademia() {
        return academia;
    }

    public void setAcademia(Academia academia) {
        this.academia = academia;
    }
}
