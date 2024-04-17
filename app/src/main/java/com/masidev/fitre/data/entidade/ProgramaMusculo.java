package com.masidev.fitre.data.entidade;

/**
 * Created by jmasiero on 15/04/16.
 */
public class ProgramaMusculo {
    private Integer Id;
    private Integer IdPrograma;
    private Integer IdMusculo;
    private Integer TipoMusculo;
    private Integer OrdemMusculo;

    public ProgramaMusculo(){

    }

    public ProgramaMusculo(Integer idPrograma, Integer idMusculo, Integer tipoMusculo, Integer ordemMusculo) {
        IdPrograma = idPrograma;
        IdMusculo = idMusculo;
        TipoMusculo = tipoMusculo;
        OrdemMusculo = ordemMusculo;
    }

    public ProgramaMusculo(Integer id, Integer idPrograma, Integer idMusculo, Integer tipoMusculo, Integer ordemMusculo) {
        Id = id;
        IdPrograma = idPrograma;
        IdMusculo = idMusculo;
        TipoMusculo = tipoMusculo;
        OrdemMusculo = ordemMusculo;
    }

    public Integer getId() {
        return Id;
    }

    public void setId(Integer id) {
        Id = id;
    }

    public Integer getIdPrograma() {
        return IdPrograma;
    }

    public void setIdPrograma(Integer idPrograma) {
        IdPrograma = idPrograma;
    }

    public Integer getIdMusculo() {
        return IdMusculo;
    }

    public void setIdMusculo(Integer idMusculo) {
        IdMusculo = idMusculo;
    }

    public Integer getTipoMusculo() {
        return TipoMusculo;
    }

    public void setTipoMusculo(Integer tipoMusculo) {
        TipoMusculo = tipoMusculo;
    }

    public Integer getOrdemMusculo() {
        return OrdemMusculo;
    }

    public void setOrdemMusculo(Integer ordemMusculo) {
        OrdemMusculo = ordemMusculo;
    }

}
