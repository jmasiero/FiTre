package com.masidev.fitre.data.entidade;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by jmasiero on 30/09/15.
 */
public class Musculo implements Serializable {

    private Integer Id;
    private int Ordem;
    private String Exercicio;
    private int Series;
    private int TmpExecIni;
    private int TmpExecFim;
    private int Dias;
    private int Intervalos;
    private String Repeticoes;
    private String Carga;
    private int TipoMusculo;
    private Integer IdTreino;
    private Integer IdTreinoBase;
    private Boolean calculoValido;

    private Boolean jaFez;

    public Boolean getCalculoValido() {
        if(calculoValido == null){
            calculoValido = false;
        }

        return calculoValido;
    }

    public void setCalculoValido(Boolean calculoValido) {
        this.calculoValido = calculoValido;
    }

    private ArrayList<ArrayList<String>> arrTentativas;

    public ArrayList<ArrayList<String>> getArrTentativas(Integer numTentativas) {
        if(arrTentativas == null){
            arrTentativas = new ArrayList<>();
            for (int i = 0; i < numTentativas; i++){
                ArrayList<String> tentativa = new ArrayList<String>();
                tentativa.add("");
                tentativa.add("");
                arrTentativas.add(tentativa);
            }
        }

        return arrTentativas;
    }

    public void setArrTentativas(ArrayList<ArrayList<String>> arrTentativas) {
        this.arrTentativas = arrTentativas;
    }

    public Musculo(){

    }


    public Musculo(Integer id,
                   int ordem,
                   String exercicio,
                   int series,
                   int tmpExecIni,
                   int tmpExecFim,
                   int intervalos,
                   String repeticoes,
                   String carga,
                   int tipoMusculo,
                   Integer IdTreino,
                   Integer IdTreinoBase) {
        this.Id = id;
        this.Ordem = ordem;
        this.Exercicio = exercicio;
        this.Series = series;
        this.TmpExecIni = tmpExecIni;
        this.TmpExecFim = tmpExecFim;
        this.Intervalos = intervalos;
        this.Repeticoes = repeticoes;
        this.Carga = carga;
        this.TipoMusculo = tipoMusculo;
        this.IdTreino = IdTreino;
        this.IdTreinoBase = IdTreinoBase;

    }

    public Boolean jaFez() {
        if(jaFez == null){
            jaFez = false;
        }

        return jaFez;
    }

    public void jaFez(Boolean jaFez) {
        this.jaFez = jaFez;
    }

    public Integer getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public int getOrdem() {
        return Ordem;
    }

    public void setOrdem(int ordem) {
        Ordem = ordem;
    }

    public String getExercicio() {
        return Exercicio;
    }

    public void setExercicio(String exercicio) {
        Exercicio = exercicio;
    }

    public int getSeries() {
        return Series;
    }

    public void setSeries(int series) {
        Series = series;
    }

    public int getTmpExecIni() {
        return TmpExecIni;
    }

    public void setTmpExecIni(int tmpExecIni) {
        TmpExecIni = tmpExecIni;
    }

    public int getTmpExecFim() {
        return TmpExecFim;
    }

    public void setTmpExecFim(int tmpExecFim) {
        TmpExecFim = tmpExecFim;
    }

    public int getIntervalos() {
        return Intervalos;
    }

    public void setIntervalos(int intervalos) {
        Intervalos = intervalos;
    }

    public String getRepeticoes() {
        return Repeticoes;
    }

    public void setRepeticoes(String repeticoes) {
        Repeticoes = repeticoes;
    }

    public String getCarga() {
        return Carga;
    }

    public void setCarga(String carga) {
        Carga = carga;
    }

    public int getTipoMusculo() {
        return TipoMusculo;
    }

    public void setTipoMusculo(int tipoMusculo) {
        TipoMusculo = tipoMusculo;
    }

    public Integer getIdTreino() {
        return IdTreino;
    }

    public void setIdTreino(Integer idTreino) {
        IdTreino = idTreino;
    }


    public Integer getIdTreinoBase() {
        return IdTreinoBase;
    }

    public void setIdTreinoBase(Integer idTreinoBase) {
        IdTreinoBase = idTreinoBase;
    }
}
