package com.masidev.fitre.data.entidade;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by jmasiero on 22/09/15.
 */
public class Aluno implements Serializable {
    private String CPF;
    private String Nome;
    private String DtNascimento;
    private String Observacao;
    private Treino Treino;
    //parte de organização do trienamento progressivo
    private Treino TreinoClone;
    private TreinoProgressivo treinoProgressivo;
    private Integer variacaoSelecionada;
    //fim treino progressivo
    private Integer TipoTreino;
    private ArrayList<Integer> arrTipoMusculos;

    private Integer musculoEmTreinamento;
    //Ele conta as vezes que o tipo musculo foi requisitado, e quando se tornar maior que a quantidade  de musculos em seu array
    // ele incrementa o contador de tipo musculo, assim na listagem o musculo é pulado, dpois se esse chegar ao fim e não houver
    // mais nenhum , ele volta ao musculo inicial;
    private Integer contadorTipoMusculo;
    //M - Masculinho / F - Feminino
    private String Sexo;
    //private String Foto
    private ArrayList<ProgramaTreino> lstProgramaTreino;
    private ProgramaTreino programaSelecionado;
    private Boolean treinoProgramado;

    public Integer getVariacaoSelecionada() {
        if(variacaoSelecionada == null){
            variacaoSelecionada = 0;
        }
        return variacaoSelecionada;
    }

    public void setVariacaoSelecionada(Integer variacaoSelecionada) {
        this.variacaoSelecionada = variacaoSelecionada;
    }

    public ProgramaTreino getProgramaSelecionado() {
        return programaSelecionado;
    }
    public TreinoProgressivo getTreinoProgressivo() {
        return treinoProgressivo;
    }

    public void setTreinoProgressivo(TreinoProgressivo treinoProgressivo) {
        this.treinoProgressivo = treinoProgressivo;
    }

    public void setProgramaSelecionado(ProgramaTreino programaSelecionado) {
        this.programaSelecionado = programaSelecionado;
    }

    public Boolean getTreinoProgramado() {
        if(getLstProgramaTreino().size() > 0){
            treinoProgramado = true;
        }else {
            treinoProgramado = false;
        }
        return treinoProgramado;
    }


    public com.masidev.fitre.data.entidade.Treino getTreinoClone() {
        return TreinoClone;
    }

    public void setTreinoClone(com.masidev.fitre.data.entidade.Treino treinoClone) {
        TreinoClone = treinoClone;
    }

    public void setTreinoProgramado(Boolean treinoProgramado) {
        this.treinoProgramado = treinoProgramado;
    }


    @Override
    public String toString() {
        return Nome;
    }

    public Aluno(){

    }

    public Aluno(String Cpf){
        this.CPF = Cpf;
        contadorTipoMusculo = 0;
    }

    public Aluno(String Cpf, String Nome, String DtNascimento, String Observacao, String Sexo){
        this.CPF = Cpf;
        this.Nome = Nome;
        this.DtNascimento = DtNascimento;
        this.Observacao = Observacao;
        this.Sexo = Sexo;
        contadorTipoMusculo = 0;
    }

    public String getCPF() {
        return CPF;
    }

    public void setCPF(String cpf) {
        CPF = cpf;
    }

    public String getNome() {
        return Nome;
    }

    public void setNome(String nome) {
        Nome = nome;
    }

    public String getDtNascimento() {
        return DtNascimento;
    }

    public void setDtNascimento(String dtNascimento) {
        DtNascimento = dtNascimento;
    }

    public String getObservacao() {
        return Observacao;
    }

    public void setObservacao(String observacao) {
        Observacao = observacao;
    }

    public Treino getTreino() {
        return Treino;
    }

    public void setTreino(Treino treino) {
        this.Treino = treino;
    }

    public String getSexo() {
        return Sexo;
    }

    public void setSexo(String sexo) {
        Sexo = sexo;
    }

    public Integer getTipoTreino() {
        return TipoTreino;
    }

    public void setTipoTreino(Integer tipoTreino) {
        TipoTreino = tipoTreino;
    }

    public ArrayList<Integer> getArrTipoMusculos() {
        return arrTipoMusculos;
    }

    public void setArrTipoMusculos(ArrayList<Integer> arrTipoMusculos) {
        this.arrTipoMusculos = arrTipoMusculos;
    }

    public Integer getMusculoEmTreinamento() {
        if(musculoEmTreinamento == null){
            musculoEmTreinamento = getArrTipoMusculos().get(0);
        }

        return musculoEmTreinamento;
    }

    public void setMusculoEmTreinamento(Integer musculoEmTreinamento) {
        this.musculoEmTreinamento = musculoEmTreinamento;
    }

    public Integer getProximoMusculoTreinamento(){
        Integer retorno = 0;
        Integer primeiroIndice = -1;

        for (ArrayList<Musculo> musculos : getTreino().getLstTodosMusculos() ) {
            if(musculos.size() > 0) {
                if ((getTreino().getLstTodosMusculos().get(getTreino().getLstTodosMusculos().indexOf(musculos)) != null)
                        && (primeiroIndice < 0)) {
                    primeiroIndice = getTreino().getLstTodosMusculos().indexOf(musculos);
                }

                if ((getTreino().getLstTodosMusculos().get(getTreino().getLstTodosMusculos().indexOf(musculos)) != null)
                        && (getTreino().getLstTodosMusculos().indexOf(musculos) > musculoEmTreinamento)) {
                    retorno = getTreino().getLstTodosMusculos().indexOf(musculos);
                    break;
                }
            }
        }

        if(getTreino().getLstTodosMusculos().get(retorno).size() == 0){
            retorno = primeiroIndice;
        }

        return retorno;
    }

    public void incrementaContadorTipoMusculo(){
        if(contadorTipoMusculo == null){
            contadorTipoMusculo = 0;
        }
        contadorTipoMusculo++;
    }

    public Integer getContadorTipoMusculo(){
        if(contadorTipoMusculo == null){
            contadorTipoMusculo = 1;
        }
        return contadorTipoMusculo;
    }

    public void setContadorTipoMusculo(Integer contador){
        this.contadorTipoMusculo = contador;
    }

    public ArrayList<ProgramaTreino> getLstProgramaTreino() {
        if(lstProgramaTreino == null){
            lstProgramaTreino = new ArrayList<>();
        }
        return lstProgramaTreino;
    }

    public void setLstProgramaTreino(ArrayList<ProgramaTreino> lstProgramaTreino) {
        this.lstProgramaTreino = lstProgramaTreino;
    }
}
