package com.masidev.fitre.data.entidade;

import java.util.ArrayList;

/**
 * Created by jmasiero on 05/04/16.
 */
public class TreinoProgressivo {
    private Integer Id;
    private Integer IdTreino;
    private Integer Mes;

    private ArrayList<SemanaTreinoProgressivo> lstSemanas;

    public TreinoProgressivo() { }

    public TreinoProgressivo(Integer idTreino, Integer mes) {
        IdTreino = idTreino;
        Mes = mes;
    }

    public TreinoProgressivo(Integer id, Integer idTreino, Integer mes) {
        Id = id;
        IdTreino = idTreino;
        Mes = mes;
    }

    public Integer getId() {
        return Id;
    }

    public void setId(Integer id) {
        Id = id;
    }

    public Integer getIdTreino() {
        return IdTreino;
    }

    public void setIdTreino(Integer idTreino) {
        IdTreino = idTreino;
    }

    public Integer getMes() {
        return Mes;
    }

    public void setMes(Integer mes) {
        Mes = mes;
    }

    public ArrayList<SemanaTreinoProgressivo> getLstSemanas() {
        if (lstSemanas == null){
            lstSemanas = new ArrayList<>();
        }

        return lstSemanas;
    }

    public void setLstSemanas(ArrayList<SemanaTreinoProgressivo> lstSemanas) {
        this.lstSemanas = lstSemanas;
    }

}
