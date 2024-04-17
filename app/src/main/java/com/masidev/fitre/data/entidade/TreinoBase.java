package com.masidev.fitre.data.entidade;

import com.masidev.fitre.constante.Constantes;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by jmasiero on 04/03/16.
 */
public class TreinoBase implements Serializable {
    private Integer Id;
    private String TipoTreino;
    private Personal personal;
    private Boolean visualizar;
    private ArrayList<ArrayList<Musculo>> lstTodosMusculos;
    private ArrayList<Musculo> musculosRemovidos;
    private Boolean alterar;

    public Boolean getAlterar() {
        if(alterar == null){
            alterar = false;
        }
        return alterar;
    }

    public void setAlterar(Boolean alterar) {
        this.alterar = alterar;
    }

    public ArrayList<Musculo> getMusculosRemovidos() {
        return musculosRemovidos;
    }

    public void setMusculosRemovidos(ArrayList<Musculo> musculosRemovidos) {
        this.musculosRemovidos = musculosRemovidos;
    }

    public Personal getPersonal() {
        return personal;
    }

    public void setPersonal(Personal personal) {
        this.personal = personal;
    }

    public Integer getId() {
        return Id;
    }

    public void setId(Integer id) {
        Id = id;
    }

    @Override
    public String toString() {
        return TipoTreino;
    }

    public String getTipoTreino() {
        return TipoTreino;
    }

    public void setTipoTreino(String tipoTreino) {
        TipoTreino = tipoTreino;
    }

    public Boolean getVisualizar() {
        if(visualizar == null){
            visualizar = false;
        }

        return visualizar;
    }

    public void setVisualizar(Boolean visualizar) {
        this.visualizar = visualizar;
    }

    public TreinoBase(){
    }

    public TreinoBase(Integer Id, String TipoTreino, Personal personal){
        this.Id = Id;
        this.TipoTreino = TipoTreino;
        this.personal = personal;
    }

    //Lista com cada tipo de musculo
    public ArrayList<Musculo> getLstMusculos(int tipoMusculo) {
        lstTodosMusculos = getLstTodosMusculos();

        return lstTodosMusculos.get(tipoMusculo);
    }

    //Lista com lista de todos os tipos de musculos
    public ArrayList<ArrayList<Musculo>> getLstTodosMusculos() {
        if(lstTodosMusculos == null){
            lstTodosMusculos = new ArrayList<ArrayList<Musculo>>();
        }

        if(lstTodosMusculos.size() == 0){

            lstTodosMusculos.add(Constantes.BICEPS, new ArrayList<Musculo>());
            lstTodosMusculos.add(Constantes.TRICEPS, new ArrayList<Musculo>());
            lstTodosMusculos.add(Constantes.PEITO, new ArrayList<Musculo>());
            lstTodosMusculos.add(Constantes.OMBRO, new ArrayList<Musculo>());
            lstTodosMusculos.add(Constantes.COSTAS, new ArrayList<Musculo>());
            lstTodosMusculos.add(Constantes.PERNA, new ArrayList<Musculo>());
            lstTodosMusculos.add(Constantes.GLUTEOS, new ArrayList<Musculo>());
            lstTodosMusculos.add(Constantes.ANTEBRACO, new ArrayList<Musculo>());
            lstTodosMusculos.add(Constantes.ABDOMINAIS, new ArrayList<Musculo>());
        }

        return lstTodosMusculos;
    }

    public void setLstTodosMusculos(ArrayList<ArrayList<Musculo>> lstTodosMusculos) {
        this.lstTodosMusculos = lstTodosMusculos;
    }

    public void addLstMusculos(ArrayList<Musculo> lstMusculos, int tipoMusculo) {
        getLstTodosMusculos().set(tipoMusculo, lstMusculos);
    }

}
