package com.masidev.fitre.data.entidade;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by jmasiero on 15/04/16.
 */
public class ProgramaTreino implements Serializable{
    private Integer Id;
    private Aluno Aluno;
    private Integer Ordem;
    private String NomePrograma;
    private Boolean Misto;
    private Boolean Simples;
    private Boolean Treinou;

    private ArrayList<Musculo> lstMusculos;

    public ProgramaTreino(){

    }

    public ProgramaTreino(Integer id){
        this.Id = id;
    }

    public ProgramaTreino(Aluno aluno, Integer ordem, String nomePrograma, Boolean misto, Boolean simples) {
        Aluno = aluno;
        Ordem = ordem;
        NomePrograma = nomePrograma;
        Misto = misto;
        Simples = simples;
    }

    public ProgramaTreino(Integer id, Aluno aluno, Integer ordem, String nomePrograma, Boolean misto, Boolean simples) {
        Id = id;
        Aluno = aluno;
        Ordem = ordem;
        NomePrograma = nomePrograma;
        Misto = misto;
        Simples = simples;
    }

    public ProgramaTreino(Integer id, Aluno aluno, Integer ordem, String nomePrograma, Boolean misto, Boolean simples, Boolean treinou) {
        Id = id;
        Aluno = aluno;
        Ordem = ordem;
        NomePrograma = nomePrograma;
        Misto = misto;
        Simples = simples;
        Treinou = treinou;
    }

    public Integer getId() {
        return Id;
    }

    public void setId(Integer id) {
        Id = id;
    }

    public Aluno getAluno() {
        return Aluno;
    }

    public void setAluno(Aluno aluno) {
        Aluno = aluno;
    }

    public Integer getOrdem() {
        return Ordem;
    }

    public void setOrdem(Integer ordem) {
        Ordem = ordem;
    }

    public String getNomePrograma() {
        return NomePrograma;
    }

    public void setNomePrograma(String nomePrograma) {
        NomePrograma = nomePrograma;
    }

    public Boolean getMisto() {
        if(Misto == null){
            Misto = false;
        }
        return Misto;
    }

    public void setMisto(Boolean misto) {
        Misto = misto;
    }

    public Boolean getSimples() {
        if(Simples == null){
            Simples = false;
        }
        return Simples;
    }

    public void setSimples(Boolean simples) {
        Simples = simples;
    }

    public ArrayList<Musculo> getLstMusculos() {
        if(lstMusculos == null){
            lstMusculos = new ArrayList<>();
        }
        return lstMusculos;
    }

    public void setLstMusculos(ArrayList<Musculo> lstMusculos) {
        this.lstMusculos = lstMusculos;
    }

    public Boolean getTreinou() {
        if(Treinou == null){
            Treinou = false;
        }
        return Treinou;
    }

    public void setTreinou(Boolean treinou) {
        Treinou = treinou;
    }
}
