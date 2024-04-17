package com.masidev.fitre.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.masidev.fitre.R;
import com.masidev.fitre.adapter.AdapterAluno;
import com.masidev.fitre.adapter.AdapterAlunoEmTeste;
import com.masidev.fitre.constante.Constantes;
import com.masidev.fitre.data.dao.AlunoDAO;
import com.masidev.fitre.data.dao.TreinoBaseDAO;
import com.masidev.fitre.data.entidade.Aluno;
import com.masidev.fitre.data.entidade.Treino;
import com.masidev.fitre.data.entidade.TreinoBase;
import com.masidev.fitre.mascara.MaskTreinoPiramide;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by jmasiero on 10/05/16.
 */
public class FragmentDialogSelecionaAlunoParaTestePt2 extends DialogFragment {
    private Aluno alunoSelecionado;

    private Spinner spnTreinoBase;
    private ArrayAdapter<TreinoBase> adapterSpnTreinoBase;

    private Spinner spnDuracao;
    private ArrayAdapter<String> adapterSpnDuracao;

    private Spinner spnRMS1;
    private ArrayAdapter<String> adapterRMS1;
    private Spinner spnRMS2;
    private ArrayAdapter<String> adapterRMS2;
    private Spinner spnRMS3;
    private ArrayAdapter<String> adapterRMS3;

    private Spinner spnTentativas;
    private ArrayAdapter<String> adapterTentativas;

    private EditText edtPesoInico;
    private EditText edtPesoIdeal;
    private EditText edtObjetivo;
    private EditText edtRepeticoes;
    private String dataAtual;
    private CheckBox chkTreinoProgressivo;

    private TreinoBase treinoBase;

    public Aluno getAlunoSelecionado() {
        return alunoSelecionado;
    }

    public void setAlunoSelecionado(Aluno alunoSelecionado) {
        this.alunoSelecionado = alunoSelecionado;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_dialog_dados_treino_teste, null);
        builder.setView(view);

        iniciaListeners(view);
        return builder.create();
    }

    private void iniciaListeners(final View view) {
        final LinearLayout parte1 = (LinearLayout) view.findViewById(R.id.area1);
        final LinearLayout parte2 = (LinearLayout) view.findViewById(R.id.area2);
        Button btnProximo = (Button) view.findViewById(R.id.btnProximo);
        Button btnIniciar = (Button) view.findViewById(R.id.btnIniciar);
        preencheCamposPt1(view);
        preencheCamposPt2(view);

        btnProximo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validacaoDeCamposPt1()) {
                    parte1.setVisibility(View.GONE);
                    parte2.setVisibility(View.VISIBLE);

                    if(chkTreinoProgressivo.isChecked()){
                        spnRMS1.setSelection(12);
                        spnRMS1.setEnabled(false);
                        spnRMS2.setEnabled(false);
                        spnRMS3.setEnabled(false);
                        edtRepeticoes.setText("1");
                        edtRepeticoes.setEnabled(false);
                    }
                }
            }
        });

        btnIniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validacaoDeCamposPt2()) {
                    Treino t = new Treino();
                    t.setPesoInicio(Double.valueOf(edtPesoIdeal.getText().toString()));
                    t.setPesoIdeal(Double.valueOf(edtPesoIdeal.getText().toString()));
                    t.setObjetivo(edtObjetivo.getText().toString());
                    t.setTentativas(spnTentativas.getSelectedItemPosition());

                    if(spnRMS1.getSelectedItemPosition() > 0){
                        t.getArrRMS().add(spnRMS1.getSelectedItem().toString());
                    }

                    if(spnRMS2.getSelectedItemPosition() > 0){
                        t.getArrRMS().add(spnRMS2.getSelectedItem().toString());
                    }

                    if(spnRMS3.getSelectedItemPosition() > 0){
                        t.getArrRMS().add(spnRMS3.getSelectedItem().toString());
                    }

                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-d");
                    dataAtual = sdf.format(System.currentTimeMillis());
                    int posicaoDuracao = spnDuracao.getSelectedItemPosition();
                    String[] arrDataAtual = dataAtual.split("-");
                    String dataFim = "";
                    dataFim = retornaDataFinal(arrDataAtual, posicaoDuracao);
                    t.setDataFim(dataFim);

                    setTreinoBase((TreinoBase) spnTreinoBase.getSelectedItem());
                    copiaListaMusculoDoTreinoBaseParaTreino(t);
                    if(chkTreinoProgressivo.isChecked()){
                        t.setProgressivo(true);
                    }
                    t.setTipoTreino(getTreinoBase().getTipoTreino());
                    t.setRepeticoesTesteRMS(edtRepeticoes.getText().toString());
                    alunoSelecionado.setTreino(t);
                    atualizaTela();
                }
            }
        });

    }

    public void setTreinoBase(TreinoBase treinoBase) {
        this.treinoBase = treinoBase;
    }

    public TreinoBase getTreinoBase() {
        return treinoBase;
    }

    private void copiaListaMusculoDoTreinoBaseParaTreino(Treino treino){
        ArrayList<Integer> arrMusculos = new ArrayList<>();
        arrMusculos.add(0);
        arrMusculos.add(1);
        arrMusculos.add(2);
        arrMusculos.add(3);
        arrMusculos.add(4);
        arrMusculos.add(5);
        arrMusculos.add(6);
        arrMusculos.add(7);
        arrMusculos.add(8);

        setTreinoBase(TreinoBaseDAO.getInstance(getContext()).buscarTreinoBaseComMusculosFiltrados(getTreinoBase(), arrMusculos));

        treino.setLstTodosMusculos(getTreinoBase().getLstTodosMusculos());
    }

    private String retornaDataFinal(String[] arrDataAtual, int posicaoDuracao) {
        int mes = Integer.parseInt(arrDataAtual[1]);
        int ano = Integer.parseInt(arrDataAtual[0]);

        for (int i = 1; i <= posicaoDuracao; i++){
            if(mes != 12){
                mes++;
            }else{
                ano++;
                mes = 1;
            }
        }

        Calendar ca = new GregorianCalendar();

        ca.set(Calendar.DAY_OF_MONTH, 1);
        ca.set(Calendar.MONTH, mes - 1);
        ca.set(Calendar.YEAR, ano);
        int ultimoDiaDoMes = ca.getActualMaximum(Calendar.DAY_OF_MONTH);

        String dataFinal = String.valueOf(ano) + "-" + String.valueOf(mes) + "-" + String.valueOf(ultimoDiaDoMes);

        return dataFinal;
    }

    private void atualizaTela() {
        RecyclerView recyclerView = (RecyclerView) getFragmentManager()
                .findFragmentByTag(Constantes.TAG_FRAGMENT_ALUNOS_EM_TREINAMENTO)
                .getView()
                .findViewById(R.id.listTeste);

        ArrayList<Aluno> lstAlunos = ((AdapterAlunoEmTeste) recyclerView.getAdapter()).retornaListaAlunos();
        lstAlunos.add(alunoSelecionado);
        recyclerView.getAdapter().notifyDataSetChanged();
        recyclerView.setVisibility(View.VISIBLE);
        getDialog().dismiss();
    }

    private void preencheCamposPt1(View v){
        spnDuracao = (Spinner) v.findViewById(R.id.spnDuracao);
        String[] arrDuracao = Constantes.ARRAY_DURACAO_MESES_COM_TITULO;
        adapterSpnDuracao = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, arrDuracao);
        spnDuracao.setAdapter(adapterSpnDuracao);

        edtPesoInico = (EditText) v.findViewById(R.id.edtPesoInicio);
        edtPesoIdeal = (EditText) v.findViewById(R.id.edtPesoIdeal);
        edtObjetivo = (EditText) v.findViewById(R.id.edtObjetivo);

        chkTreinoProgressivo = (CheckBox) v.findViewById(R.id.chkTreinoProgressivo);
    }

    private void preencheCamposPt2(View v){
        spnRMS1 = (Spinner) v.findViewById(R.id.spnRMS1);
        String[] arrRMS1 = Constantes.ARRAY_RMS1;
        adapterRMS1 = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, arrRMS1);
        spnRMS1.setAdapter(adapterRMS1);

        spnRMS2 = (Spinner) v.findViewById(R.id.spnRMS2);
        String[] arrRMS2 = Constantes.ARRAY_RMS2;
        adapterRMS2 = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, arrRMS2);
        spnRMS2.setAdapter(adapterRMS2);

        spnRMS3 = (Spinner) v.findViewById(R.id.spnRMS3);
        String[] arrRMS3 = Constantes.ARRAY_RMS3;
        adapterRMS3 = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, arrRMS3);
        spnRMS3.setAdapter(adapterRMS3);

        spnTreinoBase = (Spinner) v.findViewById(R.id.spnTreinoBase);
        List<TreinoBase> lstTreinoBase = TreinoBaseDAO.getInstance(getContext()).listarSemMusculos(true, getContext().getString(R.string.treino_base));
        adapterSpnTreinoBase = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, lstTreinoBase);
        spnTreinoBase.setAdapter(adapterSpnTreinoBase);

        spnTentativas = (Spinner) v.findViewById(R.id.spnTentativas);
        String[] arrTentativas = Constantes.ARRAY_TENTATIVAS;
        adapterTentativas = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, arrTentativas);
        spnTentativas.setAdapter(adapterTentativas);

        iniciaMascaras(v);
    }

    private void iniciaMascaras(View v) {
        edtRepeticoes = (EditText) v.findViewById(R.id.edtRepeticoes);
        edtRepeticoes.addTextChangedListener(MaskTreinoPiramide.insert("###########", edtRepeticoes));
    }

    private boolean validacaoDeCamposPt1(){
        boolean retorno = true;
        String mensagem = "";

        if(retorno && edtPesoInico.getText().toString().trim().equals("")){
            retorno = false;
            mensagem = "ERRO! Campo Peso Inicio vazio!";
        }

        if(retorno && edtPesoIdeal.getText().toString().trim().equals("")){
            retorno = false;
            mensagem = "ERRO! Campo Peso Ideal vazio!";
        }

        if(retorno && edtObjetivo.getText().toString().trim().equals("")){
            retorno = false;
            mensagem = "ERRO! Campo Objetivo vazio!";
        }

        if(retorno && spnDuracao.getSelectedItemPosition() == 0){
            retorno = false;
            mensagem = "ERRO! Campo Duração vazio!";
        }

        if(!retorno){
            mensagemErro(mensagem);
        }

        return retorno;
    }

    private boolean validacaoDeCamposPt2() {
        boolean retorno = true;
        String mensagem = "";

        if(retorno &&
           spnRMS1.getSelectedItemPosition() == 0 &&
           spnRMS2.getSelectedItemPosition() == 0 &&
           spnRMS3.getSelectedItemPosition() == 0
           ){
            retorno = false;
            mensagem = "ERRO! Pelo menos 1 RMS deve ser selecionado!";
        }

        if(retorno && spnTreinoBase.getSelectedItemPosition() == 0){
            retorno = false;
            mensagem = "ERRO! Treino Base não selecionado!";
        }

        if(retorno && spnTentativas.getSelectedItemPosition() == 0){
            retorno = false;
            mensagem = "ERRO! Tentativas não selecionadas!";
        }

        if(retorno && !edtRepeticoes.getText().toString().equals("")){
            if(edtRepeticoes.getText().toString().split("-").length > 0){
                String[] arrRepeticoes = edtRepeticoes.getText().toString().split("-");
                String repeticoes = "";

                for (int i = 0; i < arrRepeticoes.length; i++){

                    if(!arrRepeticoes[i].equals("") && i == 0){
                        repeticoes += arrRepeticoes[i];
                    }else if(!arrRepeticoes[i].equals("")){
                        repeticoes += "-" + arrRepeticoes[i];
                    }
                }

                edtRepeticoes.setText(repeticoes);

            }else{
                retorno = false;
                mensagem = "ERRO! Campo Repetições vazio!";
            }

        }else if(retorno && edtRepeticoes.getText().toString().equals("")){
            retorno = false;
            mensagem = "ERRO! Campo Repetições vazio!";
        }

        if(!retorno){
            mensagemErro(mensagem);
        }

        return retorno;
    }

    public void mensagemErro(String mensagem){
        Toast t = Toast.makeText(getContext(), mensagem, Toast.LENGTH_LONG);
        t.setGravity(Gravity.CENTER, Gravity.CENTER_HORIZONTAL,Gravity.CENTER_VERTICAL);
        t.show();
    }

}
