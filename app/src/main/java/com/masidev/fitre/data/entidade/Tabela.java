package com.masidev.fitre.data.entidade;

/**
 * Created by jmasiero on 05/04/16.
 */
public class Tabela {
    private Integer Id;
    private String Nome;
    private String TipoChave;

    public Tabela(){

    }

    public Tabela(Integer id, String nome, String tipoChave){
        this.Id = id;
        this.Nome = nome;
        this.TipoChave = tipoChave;
    }

    public Integer getId() {
        return Id;
    }

    public void setId(Integer id) {
        Id = id;
    }

    public String getNome() {
        return Nome;
    }

    public void setNome(String nome) {
        Nome = nome;
    }

    public String getTipoChave() {
        return TipoChave;
    }

    public void setTipoChave(String tipoChave) {
        TipoChave = tipoChave;
    }
}
