package com.masidev.fitre.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;

import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;

import com.masidev.fitre.R;
import com.masidev.fitre.adapter.AdapterExercicioEmTreinamento;
import com.masidev.fitre.constante.Constantes;
import com.masidev.fitre.data.dao.TreinoDAO;
import com.masidev.fitre.data.entidade.Aluno;
import com.masidev.fitre.data.entidade.Musculo;
import com.masidev.fitre.data.entidade.Treino;
import com.masidev.fitre.data.entidade.TreinoProgressivo;
import com.masidev.fitre.listener.OnItemLongClickListenerDialogAlterarRepeticaoCargaTreino;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jmasiero on 13/06/16.
 */
public class FragmentDialogListaExercicio extends DialogFragment {
    private ListView listaExercicio;
    private ArrayList<Musculo> lstExercicios;
    private Integer tipoTreino;
    private Aluno aluno;
    private Spinner spnVariacao;
    private ArrayAdapter<String> adapterSpnVariacao;
    private List<String> lstVariacoes;
    private View viewBase;
    private Context contextBase;

    public Integer getTipoTreino() {
        return tipoTreino;
    }

    public void setTipoTreino(Integer tipoTreino) {
        this.tipoTreino = tipoTreino;
    }

    public static FragmentDialogListaExercicio newInstance() {
        FragmentDialogListaExercicio fragment = new FragmentDialogListaExercicio();
        return fragment;
    }

    public FragmentDialogListaExercicio() {
    }

    @Override
    public void onStart() {
        super.onStart();

        // safety check
        if (getDialog() == null) {
            return;
        }

        if(lstExercicios.size() >= 8) {

            int y;
            int dialogHeight;
            DisplayMetrics metrics = new DisplayMetrics();
            getDialog().getWindow().getWindowManager().getDefaultDisplay().getMetrics(metrics);

            y = metrics.heightPixels;
            dialogHeight = (int) (y * 0.70);

            int x = metrics.widthPixels;
            int dialogWidth = (int) (x * 0.90);

            getDialog().getWindow().setLayout(dialogWidth, dialogHeight);
        }

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        viewBase = inflater.inflate(R.layout.fragment_lista_exercicios_em_treinamento, null);
        builder.setView(viewBase);

        if(getAluno().getTreino().getProgressivo()){
            preencheSpinner(viewBase);
            iniciaListenersTreinamentoProgressivo(viewBase);
        }

        iniciaListeners(viewBase);

        listarExercicios(viewBase);

        return builder.create();
    }

    private void iniciaListenersTreinamentoProgressivo(View viewBase) {
        spnVariacao.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                restauraTreinoOriginal();
                atualizaTreinamentoSeguindoTreinoProgressivo(position);
                atualizaListagem();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void preencheSpinner(View view) {
        spnVariacao = (Spinner) view.findViewById(R.id.spnVariacao);
        spnVariacao.setVisibility(View.VISIBLE);

        lstVariacoes = new ArrayList<String>();
        lstVariacoes.add("Sem Variação");
        lstVariacoes.add("20%");
        lstVariacoes.add("10%");
        lstVariacoes.add("-10%");
        lstVariacoes.add("-20%");

        ArrayAdapter<String> adapterSpnVariacao = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, lstVariacoes);

        // Drop down layout style - list view with radio button
        adapterSpnVariacao.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spnVariacao.setAdapter(adapterSpnVariacao);
        spnVariacao.setSelection(getAluno().getVariacaoSelecionada());

    }

    private void iniciaListeners(View view) {
        Button btnAnotacoes = (Button) view.findViewById(R.id.btnAnotacoes);
        btnAnotacoes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostraDialogComAnotacoes();
            }
        });
    }

    private void atualizaListagem(){
        listarExercicios(viewBase);
    }

    private void atualizaTreinamentoSeguindoTreinoProgressivo(int variacaoSelecionada) {
        TreinoProgressivo tp = getAluno().getTreinoProgressivo();
        int variacao = 0;
        switch (variacaoSelecionada){
            case 0:
                variacao = 0;
                break;
            case 1:
                variacao = 20;
                break;
            case 2:
                variacao = 10;
                break;
            case 3:
                variacao = -10;
                break;
            case 4:
                variacao = -20;
                break;
        }
        getAluno().setVariacaoSelecionada(variacaoSelecionada);
        for (ArrayList<Musculo> lst : getAluno().getTreino().getLstTodosMusculos()) {
            for ( Musculo m : lst) {
                m.setCarga(retornaCargaAtualizada(m.getCarga(), tp.getLstSemanas().get(0).getPorcentagemCargas(), variacao));
                m.setRepeticoes(tp.getLstSemanas().get(0).getRepeticoes());
            }
        }
    }

    private String retornaCargaAtualizada(String carga, String porcentagemCargas, int variacao) {
        String[] arrPorcCarga = porcentagemCargas.split("%");
        String arrRetorno = "";
        for (int i = 0; i <= arrPorcCarga.length -1; i++){
            if(i > 0){
                arrRetorno += "-";
            }
            if((i <= arrPorcCarga.length - 1)){
                try{
                    arrRetorno += String.valueOf(Integer.valueOf(carga)*(Integer.valueOf(arrPorcCarga[i])+variacao)/100);
                }catch (Exception e){
                    arrRetorno += "Erro!";
                }
            }else{
                arrRetorno += carga  ;
            }
        }

        return arrRetorno;
    }

    private void restauraTreinoOriginal() {
        Treino tClone = new Treino();

        ArrayList<ArrayList<Musculo>> lstTodosMusculos = tClone.getLstTodosMusculos();

        for(int i = 0; i < lstTodosMusculos.size(); i++){
            if(getAluno().getTreino().getLstMusculos(i).size() > 0){
                int indiceMusculo = -1;
                for ( Musculo m : getAluno().getTreinoClone().getLstMusculos(i)) {
                    indiceMusculo++;
                    Musculo mClone = new Musculo(m.getId(),
                            m.getOrdem(),
                            m.getExercicio(),
                            m.getSeries(),
                            m.getTmpExecIni(),
                            m.getTmpExecFim(),
                            m.getIntervalos(),
                            m.getRepeticoes(),
                            m.getCarga(),
                            m.getTipoMusculo(),
                            m.getIdTreino(),
                            m.getIdTreinoBase());

                    mClone.jaFez(getAluno().getTreino().getLstMusculos(i).get(indiceMusculo).jaFez());
                    lstTodosMusculos.get(i).add(mClone);
                }
            }
        }

        getAluno().getTreino().setLstTodosMusculos(tClone.getLstTodosMusculos());


    }

    private void mostraDialogComAnotacoes() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        String Anotacao = TreinoDAO.getInstance(contextBase).buscaAnotacaoTreinoAluno(getAluno());
        builder.setTitle("Anotações!")
                .setMessage(Anotacao)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        builder.show();
    }

    private void listarExercicios(View view) {
        lstExercicios = new ArrayList<>();
        if(getTipoTreino() == Constantes.TREINO_SIMPLES) {
            for (ArrayList<Musculo> lstM : aluno.getTreino().getLstTodosMusculos()) {
                if (lstM.size() > 0) {
                    for (Musculo m : lstM) {
                        lstExercicios.add(m);
                    }
                }
            }
        }else{
            int maiorValor = 0;
            ArrayList<ArrayList<Musculo>> listaSimples = new ArrayList<>();
            for (ArrayList<Musculo> lstM : aluno.getTreino().getLstTodosMusculos()) {
                if (lstM.size() > 0) {
                    listaSimples.add(lstM);
                    if(maiorValor < lstM.size()){
                        maiorValor = lstM.size();
                    }
                }
            }

            int volta = 0;
            int voltaAnterior = 0;

            for(int i = 0; i < listaSimples.size(); i++){
                volta++;
                int i1 = -1;
                int i2 = -1;

                for(int j = 0; j < maiorValor; j++){
                    if(volta != voltaAnterior){
                        i1 = i;
                        i2 = ++i;
                        voltaAnterior = volta;
                    }

                    if((listaSimples.size() -1) >= i1 && (listaSimples.get(i1).size() -1) >= j){
                        lstExercicios.add(listaSimples.get(i1).get(j));
                    }

                    if((listaSimples.size() -1) >= i2 && (listaSimples.get(i2).size() -1) >= j){
                        lstExercicios.add(listaSimples.get(i2).get(j));
                    }
                }
            }
        }
        preencheLista(view);
    }

    private void preencheLista(final View v){
        listaExercicio = (ListView) v.findViewById(R.id.listaExercicios);
        AdapterExercicioEmTreinamento adapter = new AdapterExercicioEmTreinamento(
                    v.getContext(),
                    R.layout.adapter_exercicio_row_item,
                    lstExercicios);

        listaExercicio.setAdapter(adapter);

        if(!getAluno().getTreino().getProgressivo()) {
            listaExercicio.setOnItemLongClickListener(new OnItemLongClickListenerDialogAlterarRepeticaoCargaTreino(getFragmentManager(), lstExercicios));

        }

        listaExercicio.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Musculo musculo = lstExercicios.get(position);
                LinearLayout ll = (LinearLayout) view.findViewById(R.id.fundo_item_listagem);

                if (musculo.jaFez()) {
                    musculo.jaFez(false);
                    ll.setBackground(view.getContext().getResources().getDrawable(R.drawable.item_listagem));
                } else {
                    musculo.jaFez(true);
                    ll.setBackground(view.getContext().getResources().getDrawable(R.drawable.item_listagem_selecionado));
                }
            }
        });

    }

    public void setAluno(Aluno aluno) {
        this.aluno = aluno;
    }

    public Aluno getAluno(){
        return aluno;
    }
}

