package com.masidev.fitre.data.entidade;

/**
 * Created by jmasiero on 05/04/16.
 */
public class SemanaTreinoProgressivo {

    private Integer Id;
    private String Repeticoes;
    private String PorcentagemCargas;
    private Integer Semana;


    //vriaveis de ajuda
    private String porcentagemCarga1;
    private String porcentagemCarga2;
    private String porcentagemCarga3;

    private Integer IdTreinoProgressivo;

    public SemanaTreinoProgressivo() {
    }

    public SemanaTreinoProgressivo(Integer id, String repeticoes, String porcentagemCargas, Integer semana) {
        Id = id;
        Repeticoes = repeticoes;
        PorcentagemCargas = porcentagemCargas;
        Semana = semana;
    }

    public SemanaTreinoProgressivo(String repeticoes, String porcentagemCargas, Integer semana) {
        Repeticoes = repeticoes;
        PorcentagemCargas = porcentagemCargas;
        Semana = semana;
    }

    public String getPorcentagemCarga1() {
        if(porcentagemCarga1 == null){
            porcentagemCarga1 = "";
        }
        return porcentagemCarga1;
    }

    public void setPorcentagemCarga1(String porcentagemCarga1) {
        this.porcentagemCarga1 = porcentagemCarga1;
    }

    public String getPorcentagemCarga2() {
        if(porcentagemCarga2 == null){
            porcentagemCarga2 = "";
        }
        return porcentagemCarga2;
    }

    public void setPorcentagemCarga2(String porcentagemCarga2) {
        this.porcentagemCarga2 = porcentagemCarga2;
    }

    public String getPorcentagemCarga3() {
        if(porcentagemCarga3 == null){
            porcentagemCarga3 = "";
        }
        return porcentagemCarga3;
    }

    public void setPorcentagemCarga3(String porcentagemCarga3) {
        this.porcentagemCarga3 = porcentagemCarga3;
    }

    public Integer getIdTreinoProgressivo() {
        return IdTreinoProgressivo;
    }

    public void setIdTreinoProgressivo(Integer idTreinoProgressivo) {
        IdTreinoProgressivo = idTreinoProgressivo;
    }

    public Integer getId() {
        return Id;
    }

    public void setId(Integer id) {
        Id = id;
    }

    public String getRepeticoes() {
        return Repeticoes;
    }

    public void setRepeticoes(String repeticoes) {
        Repeticoes = repeticoes;
    }

    public String getPorcentagemCargas() {
        return PorcentagemCargas;
    }

    public void setPorcentagemCargas(String porcentagemCargas) {
        PorcentagemCargas = porcentagemCargas;
    }

    public Integer getSemana() {
        return Semana;
    }

    public void setSemana(Integer semana) {
        Semana = semana;
    }

}
