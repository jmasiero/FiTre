package com.masidev.fitre.data.entidade;

/**
 * Created by jmasiero on 05/04/16.
 */
public class Alteracao {
    private Integer Id;
    private Tabela Tabela;
    private String Acao;
    private String Chave;
    private Integer Ativo;
    private Personal Personal;
    private String Data;
    private Academia Academia;

    public Alteracao(){

    }

    public Alteracao(Integer id,
                    Tabela tabela,
                    String acao,
                    String chave,
                    Integer ativo,
                    Personal personal,
                    String data,
                    Academia academia){

        this.Id = id;
        this.Tabela = tabela;
        this.Acao = acao;
        this.Chave = chave;
        this.Ativo = ativo;
        this.Personal = personal;
        this.Data = data;
        this.Academia = academia;
    }

    public Integer getId() {
        return Id;
    }

    public void setId(Integer id) {
        Id = id;
    }

    public Tabela getTabela() {
        return Tabela;
    }

    public void setTabela(Tabela tabela) {
        Tabela = tabela;
    }

    public String getAcao() {
        return Acao;
    }

    public void setAcao(String acao) {
        Acao = acao;
    }

    public String getChave() {
        return Chave;
    }

    public void setChave(String chave) {
        Chave = chave;
    }

    public Integer getAtivo() {
        return Ativo;
    }

    public void setAtivo(Integer ativo) {
        Ativo = ativo;
    }

    public Personal getPersonal() {
        return Personal;
    }

    public void setPersonal(Personal personal) {
        Personal = personal;
    }

    public String getData() {
        return Data;
    }

    public void setData(String data) {
        Data = data;
    }

    public com.masidev.fitre.data.entidade.Academia getAcademia() {
        return Academia;
    }

    public void setAcademia(com.masidev.fitre.data.entidade.Academia academia) {
        Academia = academia;
    }
}
