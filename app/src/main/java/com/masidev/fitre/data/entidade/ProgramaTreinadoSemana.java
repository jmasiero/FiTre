package com.masidev.fitre.data.entidade;

import java.io.Serializable;

/**
 * Created by jmasiero on 30/09/15.
 */
public class ProgramaTreinadoSemana implements Serializable{

    private Integer Id;
    private Aluno Aluno;
    private ProgramaTreino ProgramaTreino;
    private Integer Treinou;

    private Integer OrdemPrograma;

    public ProgramaTreinadoSemana() {

    }

    public ProgramaTreinadoSemana(Integer id, com.masidev.fitre.data.entidade.Aluno aluno, com.masidev.fitre.data.entidade.ProgramaTreino programaTreino, Integer treinou) {
        Id = id;
        Aluno = aluno;
        ProgramaTreino = programaTreino;
        Treinou = treinou;
    }

    public ProgramaTreinadoSemana(Integer id, com.masidev.fitre.data.entidade.Aluno aluno, com.masidev.fitre.data.entidade.ProgramaTreino programaTreino, Integer treinou, Integer OrdemPrograma) {
        Id = id;
        Aluno = aluno;
        ProgramaTreino = programaTreino;
        Treinou = treinou;
        this.OrdemPrograma = OrdemPrograma;
    }

    public Integer getOrdemPrograma() {
        return OrdemPrograma;
    }

    public void setOrdemPrograma(Integer ordemPrograma) {
        OrdemPrograma = ordemPrograma;
    }

    public Integer getId() {
        return Id;
    }

    public void setId(Integer id) {
        Id = id;
    }

    public com.masidev.fitre.data.entidade.Aluno getAluno() {
        return Aluno;
    }

    public void setAluno(com.masidev.fitre.data.entidade.Aluno aluno) {
        Aluno = aluno;
    }

    public com.masidev.fitre.data.entidade.ProgramaTreino getProgramaTreino() {
        return ProgramaTreino;
    }

    public void setProgramaTreino(com.masidev.fitre.data.entidade.ProgramaTreino programaTreino) {
        ProgramaTreino = programaTreino;
    }

    public Integer getTreinou() {
        return Treinou;
    }

    public void setTreinou(Integer treinou) {
        Treinou = treinou;
    }

}
