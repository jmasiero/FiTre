package com.masidev.fitre.data.entidade;

/**
 * Created by jmasiero on 15/04/16.
 */
public class PersonalAcademia {
    private Integer id;
    private String cnpjAcademia;
    private String cpfPersonal;

    public PersonalAcademia(String cnpjAcademia, String cpfPersonal) {
        this.cnpjAcademia = cnpjAcademia;
        this.cpfPersonal = cpfPersonal;
    }

    public PersonalAcademia(Integer id, String cnpjAcademia, String cpfPersonal) {
        this.id = id;
        this.cnpjAcademia = cnpjAcademia;
        this.cpfPersonal = cpfPersonal;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCnpjAcademia() {
        return cnpjAcademia;
    }

    public void setCnpjAcademia(String cnpjAcademia) {
        this.cnpjAcademia = cnpjAcademia;
    }

    public String getCpfPersonal() {
        return cpfPersonal;
    }

    public void setCpfPersonal(String cpfPersonal) {
        this.cpfPersonal = cpfPersonal;
    }



}
