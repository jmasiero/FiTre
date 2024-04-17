package com.masidev.fitre.data.entidade;

import com.masidev.fitre.constante.Constantes;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by jmasiero on 30/09/15.
 */
public class Treino implements Serializable {

    private Integer Id;
    private String TipoTreino;
    private double PesoInicio;
    private double PesoIdeal;
    private String DataInicio;
    private String DataFim;
    private String Objetivo;
    private String Anotacoes;
    private Aluno Aluno;
    private Boolean Atual;
    private Personal Personal;
    private Personal PersonalCriador;
    private ArrayList<ArrayList<Musculo>> lstTodosMusculos;
    private Integer Ordem;
    private Boolean visualizar;
    private ArrayList<Musculo> musculosRemovidos;
    private Boolean Progressivo;
    private Integer Tentativas;
    private ArrayList<String> arrRMS;
    private String RepeticoesTesteRMS;

    public String getRepeticoesTesteRMS() {
        return RepeticoesTesteRMS;
    }

    public void setRepeticoesTesteRMS(String repeticoesTesteRMS) {
        RepeticoesTesteRMS = repeticoesTesteRMS;
    }


    public ArrayList<String> getArrRMS() {
        if(arrRMS == null){
            arrRMS = new ArrayList<>();
        }
        return arrRMS;
    }

    public void setArrRMS(ArrayList<String> arrRMS) {
        this.arrRMS = arrRMS;
    }

    public Integer getTentativas() {
        return Tentativas;
    }

    public void setTentativas(Integer tentativas) {
        Tentativas = tentativas;
    }

    public void setId(Integer id) {
        Id = id;
    }

    public Boolean getProgressivo() {
        if(Progressivo == null){
            Progressivo = false;
        }

        return Progressivo;
    }

    public void setProgressivo(Boolean progressivo) {
        Progressivo = progressivo;
    }

    public ArrayList<Musculo> getMusculosRemovidos() {
        return musculosRemovidos;
    }

    public void setMusculosRemovidos(ArrayList<Musculo> musculosRemovidos) {
        this.musculosRemovidos = musculosRemovidos;
    }


    public  Treino(){
    }

    public  Treino(Integer Id){
        this.Id = Id;
    }

    public Treino(  Integer Id,
                    String TipoTreino,
                    double PesoInicio,
                    double PesoIdeal,
                    String DataInicio,
                    String DataFim,
                    String Objetivo,
                    String Anotacoes,
                    Aluno Aluno,
                    Personal Personal,
                    Boolean Atual,
                    Boolean Progressivo,
                    Personal PersonalCriador){

        this.Id = Id;
        this.TipoTreino = TipoTreino;
        this.PesoInicio = PesoInicio;
        this.PesoIdeal = PesoIdeal;
        this.DataInicio = DataInicio;
        this.DataFim = DataFim;
        this.Objetivo = Objetivo;
        this.Anotacoes = Anotacoes;
        this.Aluno = Aluno;
        this.Personal = Personal;
        this.Atual = Atual;
        this.Progressivo = Progressivo;
        this.PersonalCriador = PersonalCriador;
    }

    public Integer getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getTipoTreino() {
        return TipoTreino;
    }

    public void setTipoTreino(String tipoTreino) {
        TipoTreino = tipoTreino;
    }

    public double getPesoInicio() {
        return PesoInicio;
    }

    public void setPesoInicio(double pesoInicio) {
        PesoInicio = pesoInicio;
    }

    public double getPesoIdeal() {
        return PesoIdeal;
    }

    public void setPesoIdeal(double pesoIdeal) {
        PesoIdeal = pesoIdeal;
    }

    public String getDataInicio() {
        return DataInicio;
    }

    public void setDataInicio(String dataInicio) {
        DataInicio = dataInicio;
    }

    public String getDataFim() {
        return DataFim;
    }

    public void setDataFim(String dataFim) {
        DataFim = dataFim;
    }

    public String getObjetivo() {
        return Objetivo;
    }

    public void setObjetivo(String objetivo) {
        Objetivo = objetivo;
    }

    public String getAnotacoes() {
        if (Anotacoes == null){
            Anotacoes = "";
        }
        return Anotacoes;
    }

    public void setAnotacoes(String anotacoes) {
        Anotacoes = anotacoes;
    }

    public Aluno getAluno() {
        return Aluno;
    }

    public void setAluno(Aluno Aluno) {
        this.Aluno = Aluno;
    }

    public Personal getPersonal() {
        return Personal;
    }

    public void setPersonal(Personal Personal) {
        this.Personal = Personal;
    }

    //Retorna o musculo a ser treinado e o adiciona na lista para ser excluido se o usuário clicar em proximo
    public Musculo getMusculoParaTreinamento(int tipoMusculo, boolean proximo) {
        ArrayList<Musculo> lstMusculos = getLstMusculos(tipoMusculo);
//        this.lstMusculosExcluidos = new ArrayList<>();
        Musculo musculoRetorno = null;

        if(lstMusculos.size() > 0){
            for ( Musculo m : lstMusculos ) {
                if(lstMusculos.get(lstMusculos.size()-1).getOrdem() < (getOrdem()+1)){
                    setOrdem(0);
                }
                if(m.getOrdem() >= (getOrdem()+1)){
                    musculoRetorno = m;
                    break;
                }
//                else if(proximo){
//                    lstMusculosExcluidos.add(m);
//                }
            }
        }

        return musculoRetorno;
    }

    //Lista com cada tipo de musculo
    public ArrayList<Musculo> getLstMusculos(int tipoMusculo) {
        lstTodosMusculos = getLstTodosMusculos();

        return lstTodosMusculos.get(tipoMusculo);
    }

    public void addLstMusculos(ArrayList<Musculo> lstMusculos, int tipoMusculo) {
        getLstTodosMusculos().set(tipoMusculo, lstMusculos);
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


    public Boolean getAtual() {
        return Atual;
    }

    public void setAtual(Boolean atual) {
        Atual = atual;
    }

    public Integer getOrdem() {
        if(Ordem == null){
            Ordem = 0;
        }

        return Ordem;
    }

    public void setOrdem(Integer ordem) {
        Ordem = ordem;
    }

    public void incrementaOrdem() {
        Ordem++;
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

    public com.masidev.fitre.data.entidade.Personal getPersonalCriador() {
        return PersonalCriador;
    }

    public void setPersonalCriador(com.masidev.fitre.data.entidade.Personal personalCriador) {
        PersonalCriador = personalCriador;
    }

//    public ArrayList<Musculo> getLstMusculosExcluidos() {
//        return lstMusculosExcluidos;
//    }
//
//    public void setLstMusculosExcluidos(ArrayList<Musculo> lstMusculosExcluidos) {
//        this.lstMusculosExcluidos = lstMusculosExcluidos;
//    }

//    public void apagaMusculosExcluidos(int tipoMusculo){
//        for (Musculo m : getLstMusculosExcluidos()) {
//            getLstMusculos(tipoMusculo).remove(m);
//        }
//    }
}
