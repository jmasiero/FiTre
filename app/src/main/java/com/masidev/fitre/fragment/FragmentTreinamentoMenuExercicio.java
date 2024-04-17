package com.masidev.fitre.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.masidev.fitre.R;
import com.masidev.fitre.adapter.AdapterAlunoEmTreinamento;
import com.masidev.fitre.constante.Constantes;
import com.masidev.fitre.data.entidade.Aluno;
import com.masidev.fitre.data.entidade.Musculo;
import com.masidev.fitre.data.entidade.Treino;
import com.masidev.fitre.listener.OnClickListenerDialogAlterarTreino;

import java.util.ArrayList;

/**
 * Created by jmasiero on 11/11/15.
 */
public class FragmentTreinamentoMenuExercicio extends Fragment {
    private Treino treino;
    private Aluno aluno;
    private Integer tipoTreino;
    private View viewBase;
    private ArrayList<Aluno> lstAlunos;
    private AdapterAlunoEmTreinamento adapterAlunoEmTreinamento;

    public void setTreino(Treino t){
        treino = t;
    }

    public Treino getTreino(){
        return this.treino;
    }

    public static FragmentTreinamentoMenuExercicio newInstance() {

        FragmentTreinamentoMenuExercicio fragment = new FragmentTreinamentoMenuExercicio();

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        int layout = 0;

        if(getTipoTreino() == Constantes.TREINO_SIMPLES){
            layout = R.layout.fragment_treinamento_menu_exercicio_simples;
        }else{
            layout = R.layout.fragment_treinamento_menu_exercicio_misto;
        }

        View view = inflater.inflate(layout, container, false);

        setViewBase(view);
        iniciaListeners(view);
        ArrayList<Musculo> lstMusculos;
        lstMusculos = retornaExercicio(false, false);
        preencheCampos(view, lstMusculos);
        return view;
    }

    private void iniciaListeners(View v){
        Button btnFechar = (Button) v.findViewById(R.id.btnFechar);
        btnFechar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack(Constantes.TAG_FRAGMENT_MENU_EXERCICIO, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }
        });

        Button btnProximo = (Button) v.findViewById(R.id.btnProximo);
        btnProximo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<Musculo> lstMusculos;
                lstMusculos = proximoExercicio();
                getAluno().setContadorTipoMusculo(0);
                preencheCampos(getViewBase(), lstMusculos);
            }
        });

        Button btnAnotacoes = (Button) v.findViewById(R.id.btnAnotacoes);
        btnAnotacoes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostraDialogComAnotacoes();
            }
        });

        Button btnPular = (Button) v.findViewById(R.id.btnPular);
        btnPular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<Musculo> lstMusculos;
                lstMusculos = pularExercicio();
                preencheCampos(getViewBase(), lstMusculos);
            }
        });
    }

    private void mostraDialogComAnotacoes() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Anotações!")
                .setMessage(treino.getAnotacoes())
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        builder.show();
    }

    private void preencheCampos(View v, ArrayList<Musculo> lstMusculos){

        if(lstMusculos.size() > 0){
            String[] arrNome = aluno.getNome().split(" ");

            if(Constantes.TREINO_SIMPLES == getTipoTreino()) {
                TextView lblNomeAluno = (TextView) v.findViewById(R.id.lblNomeAluno);
                TextView txtAparelho = (TextView) v.findViewById(R.id.txtAparelho);
                TextView txtSeries = (TextView) v.findViewById(R.id.txtSeries1);
                Button btnRepeticoes = (Button) v.findViewById(R.id.btnRepeticoes1);
                Button btnCargas = (Button) v.findViewById(R.id.btnCargas1);
                RelativeLayout layout = (RelativeLayout) v.findViewById(R.id.layout_exercicios);

                if(lstMusculos.get(0).jaFez()){
                    layout.setBackground(v.getContext().getResources().getDrawable(R.color.azul_claro));
                    lstMusculos.get(0).jaFez(true);
                }else{
                    layout.setBackground(v.getContext().getResources().getDrawable(R.color.amarelo));
                    lstMusculos.get(0).jaFez(true);
                }

                if(!getTreino().getProgressivo()) {
                    btnCargas.setOnClickListener(new OnClickListenerDialogAlterarTreino(getFragmentManager(), lstMusculos.get(0), Constantes.CARGAS, btnCargas));
                    btnRepeticoes.setOnClickListener(new OnClickListenerDialogAlterarTreino(getFragmentManager(), lstMusculos.get(0), Constantes.REPETICOES, btnRepeticoes));
                }

                if(arrNome.length > 1){
                    lblNomeAluno.setText(arrNome[0] + " " + arrNome[arrNome.length -1]);
                }else{
                    lblNomeAluno.setText(arrNome[0]);
                }


                txtAparelho.setText(lstMusculos.get(0).getExercicio());
                txtSeries.setText(String.valueOf(lstMusculos.get(0).getSeries()));
                btnRepeticoes.setText(lstMusculos.get(0).getRepeticoes());
                btnCargas.setText(lstMusculos.get(0).getCarga());
            }else{
                TextView lblNomeAluno = (TextView) v.findViewById(R.id.lblNomeAluno);
                TextView txtAparelho1 = (TextView) v.findViewById(R.id.txtAparelho1);
                TextView txtSeries1 = (TextView) v.findViewById(R.id.txtSeries1);
                Button btnRepeticoes1 = (Button) v.findViewById(R.id.btnRepeticoes1);
                Button btnCargas1 = (Button) v.findViewById(R.id.btnCargas1);
                RelativeLayout layout = (RelativeLayout) v.findViewById(R.id.layout_exercicios);

                if(lstMusculos.get(0).jaFez() || lstMusculos.get(1).jaFez()){
                    layout.setBackground(v.getContext().getResources().getDrawable(R.color.azul_claro));
                    lstMusculos.get(0).jaFez(true);
                    lstMusculos.get(1).jaFez(true);
                }else{
                    layout.setBackground(v.getContext().getResources().getDrawable(R.color.amarelo));
                    lstMusculos.get(0).jaFez(true);
                    lstMusculos.get(1).jaFez(true);
                }

                if(!getTreino().getProgressivo()) {
                    btnCargas1.setOnClickListener(new OnClickListenerDialogAlterarTreino(getFragmentManager(), lstMusculos.get(0), Constantes.CARGAS, btnCargas1));
                    btnRepeticoes1.setOnClickListener(new OnClickListenerDialogAlterarTreino(getFragmentManager(), lstMusculos.get(0), Constantes.REPETICOES, btnRepeticoes1));
                }

                if(arrNome.length > 1){
                    lblNomeAluno.setText(arrNome[0] + " " + arrNome[arrNome.length -1]);
                }else{
                    lblNomeAluno.setText(arrNome[0]);
                }
                txtAparelho1.setText(lstMusculos.get(0).getExercicio());
                txtSeries1.setText(String.valueOf(lstMusculos.get(0).getSeries()));
                btnRepeticoes1.setText(lstMusculos.get(0).getRepeticoes());
                btnCargas1.setText(lstMusculos.get(0).getCarga());

                TextView txtAparelho2 = (TextView) v.findViewById(R.id.txtAparelho2);
                TextView txtSeries2 = (TextView) v.findViewById(R.id.txtSeries2);
                Button btnRepeticoes2 = (Button) v.findViewById(R.id.btnRepeticoes2);
                Button btnCargas2 = (Button) v.findViewById(R.id.btnCargas2);

                if(!getTreino().getProgressivo()) {
                    btnCargas2.setOnClickListener(new OnClickListenerDialogAlterarTreino(getFragmentManager(), lstMusculos.get(1), Constantes.CARGAS, btnCargas2));
                    btnRepeticoes2.setOnClickListener(new OnClickListenerDialogAlterarTreino(getFragmentManager(), lstMusculos.get(1), Constantes.REPETICOES, btnRepeticoes2));
                }

                txtAparelho2.setText(lstMusculos.get(1).getExercicio());
                txtSeries2.setText(String.valueOf(lstMusculos.get(1).getSeries()));
                btnRepeticoes2.setText(lstMusculos.get(1).getRepeticoes());
                btnCargas2.setText(lstMusculos.get(1).getCarga());

            }
        }else{
            getFragmentManager().popBackStack(Constantes.TAG_FRAGMENT_MENU_EXERCICIO, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            getLstAlunos().remove(getAluno());
            getAdapterAlunoEmTreinamento().notifyDataSetChanged();
            Toast.makeText(viewBase.getContext(), "O treino acabou!!!!!", Toast.LENGTH_LONG).show();
        }
    }

    private ArrayList<Musculo> proximoExercicio() {
        getTreino().setOrdem(getTreino().getOrdem() + 1);
        return retornaExercicio(true, false);
    }

    private ArrayList<Musculo> pularExercicio() {
        getTreino().setOrdem(getTreino().getOrdem() + 1);
        return retornaExercicio(false, true);
    }

    private ArrayList<Musculo> retornaExercicio(boolean proximo, boolean pula){
        Musculo m1 = null;
        Musculo m2 = null;
        ArrayList<Musculo> lstMusculos = new ArrayList<>();
        if(proximo){
            getAluno().setMusculoEmTreinamento(getAluno().getProximoMusculoTreinamento());
            getAluno().setContadorTipoMusculo(0);
            getTreino().setOrdem(0);
        }

        if(Constantes.TREINO_SIMPLES == getTipoTreino()){
            int tipoMusculoTreinando = getAluno().getMusculoEmTreinamento();
            int proximoInidice = -1;
            if(pula){
                getAluno().incrementaContadorTipoMusculo();
            }
            //Apaga musculo que vai ser listado e seta o proximo musculo se houver
            proximoInidice = removeTipoMusculoTreinandoDaLista(tipoMusculoTreinando);

            if(proximoInidice >= 0){
                getAluno().setMusculoEmTreinamento(proximoInidice);
            }
            setTreino(getAluno().getTreino());
            Treino treino = getTreino();

            m1 = treino.getMusculoParaTreinamento(proximoInidice, proximo);
            lstMusculos.add(m1);
        }else{
            int tipoMusculoTreinando = getAluno().getMusculoEmTreinamento();
            int proximoInidice = -1;
            if(pula){
                getAluno().incrementaContadorTipoMusculo();
                getAluno().incrementaContadorTipoMusculo();
            }
//            getAluno().incrementaContadorTipoMusculo();
            //Apaga musculo que vai ser listado e seta o proximo musculo se houver
            proximoInidice = removeTipoMusculoTreinandoDaLista(tipoMusculoTreinando);

            if(proximoInidice >= 0){
                getAluno().setMusculoEmTreinamento(proximoInidice);
            }
            setTreino(getAluno().getTreino());
            Treino treino = getTreino();

            m1 = treino.getMusculoParaTreinamento(proximoInidice, proximo);

            int segundoTipoDeMusculo = getAluno().getProximoMusculoTreinamento();



            //Apaga musculo que vai ser listado e seta o proximo musculo se houver
            //proximoInidice = removeTipoMusculoTreinandoDaLista(tipoMusculoTreinando);

//            if(proximoInidice >= 0){
//                getAluno().setMusculoEmTreinamento(proximoInidice);
//            }
//            setTreino(getAluno().getTreino());
//            Treino treino = getTreino();

            if(segundoTipoDeMusculo == proximoInidice){
                //proximo = true;
                treino.incrementaOrdem();
            }

            m2 = treino.getMusculoParaTreinamento(segundoTipoDeMusculo, proximo);
            lstMusculos.add(m1);
            lstMusculos.add(m2);
        }


        return lstMusculos;
    }


    //Remove e passa o tipo musculo para proximo
    private int removeTipoMusculoTreinandoDaLista(int tipoMusculoTreinando){
        ArrayList<Integer> lstTipoMusculoTreinando = getAluno().getArrTipoMusculos();
        int indexExclusao = -1;
        int proximoIndice = -1;
        boolean achouIndiceExclusao = false;

        //passa para proximo musculo
        ArrayList<Musculo> lstProxMusculo = getAluno().getTreino().getLstMusculos(tipoMusculoTreinando);
        if((lstTipoMusculoTreinando.size() > 1) && (lstProxMusculo.size() <= getAluno().getContadorTipoMusculo())){
            //Apaga lista de musculos excluidos
//            getTreino().apagaMusculosExcluidos(tipoMusculoTreinando);

            //zera contador
            getAluno().setContadorTipoMusculo(0);
            tipoMusculoTreinando = getAluno().getProximoMusculoTreinamento();

            getAluno().setMusculoEmTreinamento(tipoMusculoTreinando);
        }

        for (int tipoMusculo : lstTipoMusculoTreinando ) {
            if(achouIndiceExclusao){
                proximoIndice = tipoMusculo;
                break;
            }

            if(tipoMusculo == tipoMusculoTreinando){
                indexExclusao = tipoMusculo;
                achouIndiceExclusao = true;
            }
        }

        if(indexExclusao >= 0){
            ArrayList<Musculo> lstMusculo = getAluno().getTreino().getLstMusculos(indexExclusao);
            if(lstMusculo.size() == 0){
                lstTipoMusculoTreinando.remove(indexExclusao);
            }else{
                proximoIndice = indexExclusao;
            }
        }

        return proximoIndice;
    }

    public Integer getTipoTreino() {
        return tipoTreino;
    }

    public void setTipoTreino(Integer tipoTreino) {
        this.tipoTreino = tipoTreino;
    }

    public Aluno getAluno() {
        return aluno;
    }

    public void setAluno(Aluno aluno) {
        this.aluno = aluno;
    }

    public View getViewBase() {
        return viewBase;
    }

    public void setViewBase(View viewBase) {
        this.viewBase = viewBase;
    }

    public ArrayList<Aluno> getLstAlunos() {
        return lstAlunos;
    }

    public void setLstAlunos(ArrayList<Aluno> lstAlunos) {
        this.lstAlunos = lstAlunos;
    }

    public AdapterAlunoEmTreinamento getAdapterAlunoEmTreinamento() {
        return adapterAlunoEmTreinamento;
    }

    public void setAdapterAlunoEmTreinamento(AdapterAlunoEmTreinamento adapterAlunoEmTreinamento) {
        this.adapterAlunoEmTreinamento = adapterAlunoEmTreinamento;
    }

}
