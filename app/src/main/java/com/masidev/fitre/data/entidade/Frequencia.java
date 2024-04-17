package com.masidev.fitre.data.entidade;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by jmasiero on 30/09/15.
 */
public class Frequencia implements Serializable{

    private Integer Id;
    private String Data;
    private ArrayList<String> lstMusculosTreinados;
    private ArrayList<String> lstOrdemProgramaTreino;
    private Aluno Aluno;


    public Frequencia(int id,
                      String data,
                      ArrayList<String> lstMusculosTreinados,
                      ArrayList<String> lstOrdemProgramaTreino,
                      Aluno aluno) {
        this.Id = id;
        this.Data = data;
        this.lstMusculosTreinados = lstMusculosTreinados;
        this.lstOrdemProgramaTreino = lstOrdemProgramaTreino;
        this.Aluno = aluno;
    }

    public Frequencia() {

    }

    public Integer getId() {
        return Id;
    }

    public void setId(Integer id) {
        Id = id;
    }

    public String getData() {
        return Data;
    }

    public void setData(String data) {
        Data = data;
    }

    public ArrayList<String> getLstMusculosTreinados() {
        if(lstMusculosTreinados == null){
            lstMusculosTreinados = new ArrayList<>();
        }
        return lstMusculosTreinados;
    }

    public void setLstMusculosTreinados(ArrayList<String> lstMusculosTreinados) {
        this.lstMusculosTreinados = lstMusculosTreinados;
    }

    public ArrayList<String> getLstOrdemProgramaTreino() {
        if(lstOrdemProgramaTreino == null){
            lstOrdemProgramaTreino = new ArrayList<>();
        }
        return lstOrdemProgramaTreino;
    }

    public void setLstOrdemProgramaTreino(ArrayList<String> lstOrdemProgramaTreino) {
        this.lstOrdemProgramaTreino = lstOrdemProgramaTreino;
    }

    public com.masidev.fitre.data.entidade.Aluno getAluno() {
        return Aluno;
    }

    public void setAluno(com.masidev.fitre.data.entidade.Aluno aluno) {
        Aluno = aluno;
    }

    public String retornaListaEmStringMusculosTreinados(){
        String retorno = "";

        for(int i = 0; i < getLstMusculosTreinados().size(); i++){
            if(i > 0){
                retorno += "-" + getLstMusculosTreinados().get(i);
            }else{
                retorno += getLstMusculosTreinados().get(i);
            }
        }

        return retorno;
    }

    public String retornaListaEmStringOrdemProgramaTreino(){
        String retorno = "";

        for(int i = 0; i < getLstOrdemProgramaTreino().size(); i++){
            if(i > 0){
                retorno += "-" + getLstOrdemProgramaTreino().get(i);
            }else{
                retorno += getLstOrdemProgramaTreino().get(i);
            }
        }

        return retorno;
    }

}
