package com.masidev.fitre.data.entidade;

import java.util.ArrayList;

/**
 * Created by jmasiero on 15/04/16.
 */
public class Academia {
    private String cnpj;
    private String NomeFantasia;
    private String Telefone;
    private Integer UltimoIdInsert;
    private Integer UltimoIdDelete;
    private Integer UltimoIdUpdate;
    private Boolean Pagou;
    private String DtUltimoPagamento;

    private ArrayList<Personal> lstPersonais;
    public Academia(String cnpj) {
        this.cnpj = cnpj;
    }

    public Boolean getPagou() {
        if(Pagou == null){
            Pagou = false;
        }

        return Pagou;
    }

    public void setPagou(Boolean pagou) {
        Pagou = pagou;
    }

    public String getDtUltimoPagamento() {
        return DtUltimoPagamento;
    }

    public void setDtUltimoPagamento(String dtUltimoPagamento) {
        DtUltimoPagamento = dtUltimoPagamento;
    }

    public Academia(String cnpj, String nomeFantasia, String telefone, Boolean pagou, String dtUltimoPagamento) {
        this.cnpj = cnpj;
        NomeFantasia = nomeFantasia;
        Telefone = telefone;
        Pagou = pagou;
        DtUltimoPagamento = dtUltimoPagamento;
    }

    public Academia(String cnpj, String nomeFantasia, String telefone, Integer UltimoIdInsert, Integer UltimoIdDelete, Integer UltimoIdUpdate) {
        this.cnpj = cnpj;
        NomeFantasia = nomeFantasia;
        Telefone = telefone;
        this.UltimoIdInsert = UltimoIdInsert;
        this.UltimoIdDelete = UltimoIdDelete;
        this.UltimoIdUpdate = UltimoIdUpdate;
    }

    public Academia(String cnpj, String nomeFantasia, String telefone, Integer UltimoIdInsert, Integer UltimoIdDelete, Integer UltimoIdUpdate, Boolean pagou, String dtUltimoPagamento) {
        this.cnpj = cnpj;
        NomeFantasia = nomeFantasia;
        Telefone = telefone;
        this.UltimoIdInsert = UltimoIdInsert;
        this.UltimoIdDelete = UltimoIdDelete;
        this.UltimoIdUpdate = UltimoIdUpdate;
        Pagou = pagou;
        DtUltimoPagamento = dtUltimoPagamento;
    }

    @Override
    public String toString() {
        return NomeFantasia;
    }

    public ArrayList<Personal> getLstPersonais() {
        return lstPersonais;
    }

    public void setLstPersonais(ArrayList<Personal> lstPersonais) {
        this.lstPersonais = lstPersonais;
    }

    public void setTelefone(String telefone) {
        Telefone = telefone;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getNomeFantasia() {
        return NomeFantasia;
    }

    public void setNomeFantasia(String nomeFantasia) {
        NomeFantasia = nomeFantasia;
    }

    public String getTelefone() {
        return Telefone;
    }

    public Integer getUltimoIdInsert() {
        if(UltimoIdInsert == null){
            UltimoIdDelete = 0;
        }

        return UltimoIdInsert;
    }

    public void setUltimoIdInsert(Integer ultimoIdInsert) {
        UltimoIdInsert = ultimoIdInsert;
    }

    public Integer getUltimoIdDelete() {
        if(UltimoIdDelete == null){
            UltimoIdDelete = 0;
        }
        return UltimoIdDelete;
    }

    public void setUltimoIdDelete(Integer ultimoIdDelete) {
        UltimoIdDelete = ultimoIdDelete;
    }

    public Integer getUltimoIdUpdate() {
        if(UltimoIdUpdate == null){
            UltimoIdUpdate = 0;
        }

        return UltimoIdUpdate;
    }

    public void setUltimoIdUpdate(Integer ultimoIdUpdate) {
        UltimoIdUpdate = ultimoIdUpdate;
    }
}
