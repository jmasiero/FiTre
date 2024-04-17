package com.masidev.fitre.data.entidade;

import java.io.Serializable;

/**
 * Created by jmasiero on 30/09/15.
 */
public class MusculoTreinadoSemana implements Serializable{

    private Integer Id;
    private Integer TipoMusculo;
    private Integer Treinou;
    private Aluno Aluno;

    public MusculoTreinadoSemana(int id,
                                 int tipoMusculo,
                                 int treinou,
                                 Aluno aluno) {
        this.Id = id;
        this.TipoMusculo = tipoMusculo;
        this.Treinou = treinou;
        this.Aluno = aluno;
    }

    public MusculoTreinadoSemana() {

    }

    public Integer getId() {
        return Id;
    }

    public void setId(Integer id) {
        Id = id;
    }

    public Integer getTipoMusculo() {
        return TipoMusculo;
    }

    public void setTipoMusculo(Integer tipoMusculo) {
        TipoMusculo = tipoMusculo;
    }

    public Integer getTreinou() {
        return Treinou;
    }

    public void setTreinou(Integer treinou) {
        Treinou = treinou;
    }

    public Aluno getAluno() {
        return Aluno;
    }

    public void setAluno(Aluno aluno) {
        Aluno = aluno;
    }
}
