package com.masidev.fitre.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.masidev.fitre.R;
import com.masidev.fitre.adapter.AdapterMusculo;
import com.masidev.fitre.constante.Constantes;
import com.masidev.fitre.data.dao.TreinoBaseDAO;
import com.masidev.fitre.data.dao.TreinoDAO;
import com.masidev.fitre.data.entidade.Musculo;
import com.masidev.fitre.data.entidade.Treino;
import com.masidev.fitre.data.entidade.TreinoBase;

import java.util.ArrayList;

/**
 * Created by jmasiero on 16/10/15.
 */
public class FragmentNovoMusculo extends Fragment {
    private int tipoMusculo;
    private Treino treino;
    private TreinoBase treinoBase;
    private Boolean base;

    public Treino getTreino() {
        return treino;
    }

    public void setTreino(Treino treino) {
        this.treino = treino;
    }

    public void setTipoMusculo(int tipoMusculo){
        this.tipoMusculo = tipoMusculo;
    }

    public TreinoBase getTreinoBase() {
        return treinoBase;
    }

    public void setTreinoBase(TreinoBase treinoBase) {
        this.treinoBase = treinoBase;
    }

    public Boolean getBase() {
        return base;
    }

    public void setBase(Boolean base) {
        this.base = base;
    }

    public boolean getAlterarTreino(){
        boolean retorno = false;
        if(getTreino().getId() != null){
            retorno = true;
        }

        return retorno;
    }

    public boolean getAlterarTreinoBase(){
        boolean retorno = false;
        if(getTreinoBase().getId() != null){
            retorno = true;
        }

        return retorno;
    }

    public static FragmentNovoMusculo newInstance() {

        Bundle args = new Bundle();

        FragmentNovoMusculo fragment = new FragmentNovoMusculo();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_lista_novo_treino, container, false);

        TextView txtMusculo = (TextView) view.findViewById(R.id.txtNomeMusculo);
        txtMusculo.setText(Constantes.ARRAY_TIPO_MUSCULOS[tipoMusculo]);

        Button btnSalvarTreino = (Button) view.findViewById(R.id.btnSalvarTreino);
        if(!base) {
            if (getTreino().getVisualizar()) {
                btnSalvarTreino.setVisibility(View.INVISIBLE);
            } else {
                btnSalvarTreino.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (verificaMusculosPreenchidos(true)) {
                            salvarTreinoMusculo();
                        }
                    }
                });
            }
        }else{
            if (getTreinoBase().getVisualizar()) {
                btnSalvarTreino.setVisibility(View.INVISIBLE);
            } else {
                btnSalvarTreino.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (verificaMusculosPreenchidos(true)) {
                            salvarTreinoMusculo();
                        }
                    }
                });
            }
        }

        Button btnVoltarTreino = (Button) view.findViewById(R.id.btnVoltarTreino);

        btnVoltarTreino.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //mandar mensagem falando que existe um exercicio com campo vazio e o mesmo será excluido ao clicar no botão voltar
                if(!verificaMusculosPreenchidos(false)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                    builder.setMessage("Existe um exercício com campo vazio e o mesmo será excluido")
                            .setPositiveButton("Continuar",  new DialogInterface.OnClickListener(){
                                @Override
                                public void onClick(DialogInterface dialog, int id) {
                                    excluiMusculosIncompletos();
                                    salvarTreinoMusculo();
                                }
                            })
                            .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,int id) {
                                    dialog.cancel();
                                }
                            })
                            .show();
                }else{
                    excluiMusculosIncompletos();
                    salvarTreinoMusculo();
                }
            }
        });

        if(!base) {
            if (getTreino().getId() != null) {
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

                Boolean visualizar = false;

                if (getTreino().getVisualizar()) {
                    visualizar = true;
                }

                if(!possuiMusculos() && getAlterarTreino() && !getTreino().getVisualizar()){
                    setTreino(TreinoDAO.getInstance(view.getContext()).buscarTreinoComMusculosFiltradosParaAlterar(getTreino(), arrMusculos));
                    getTreino().setVisualizar(visualizar);
                }else if(!possuiMusculos()){
                    setTreino(TreinoDAO.getInstance(view.getContext()).buscarTreinoComMusculosFiltrados(getTreino().getAluno(), arrMusculos, false, false));
                    getTreino().setVisualizar(visualizar);
                }
            }
        }else{
            if (getTreinoBase().getId() != null) {
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

                Boolean visualizar = false;

                if (getTreinoBase().getVisualizar()) {
                    visualizar = true;
                }

                if(!possuiMusculos() && getAlterarTreinoBase()) {
                    setTreinoBase(TreinoBaseDAO.getInstance(view.getContext()).buscarTreinoBaseComMusculosFiltradosParaAlterar(getTreinoBase(), arrMusculos));
                    getTreinoBase().setVisualizar(visualizar);
                }else if(!possuiMusculos()) {
                    setTreinoBase(TreinoBaseDAO.getInstance(view.getContext()).buscarTreinoBaseComMusculosFiltrados(getTreinoBase(), arrMusculos));
                    getTreinoBase().setVisualizar(visualizar);
                }
            }
        }

        ArrayList<Musculo> lstMusculos;
        ArrayList<Musculo> lstMusculosRemovidos = new ArrayList<>();
        if(!base) {
            lstMusculos = getTreino().getLstMusculos(tipoMusculo);
            if(getTreino().getMusculosRemovidos() == null) {
                getTreino().setMusculosRemovidos(lstMusculosRemovidos);
            }
        }else{
            lstMusculos = getTreinoBase().getLstMusculos(tipoMusculo);
            if(getTreinoBase().getMusculosRemovidos() == null) {
                getTreinoBase().setMusculosRemovidos(lstMusculosRemovidos);
            }
        }

        if(lstMusculos.size() == 0){
            lstMusculos.add(new Musculo(null, 1, "", 0, 0, 0, 0, "", "", tipoMusculo, null, null));
        }

//        if(!base){
//            getTreino().addLstMusculos(lstMusculos, tipoMusculo);
//        }else{
//            getTreinoBase().addLstMusculos(lstMusculos, tipoMusculo);
//        }

        RecyclerView mRecyclerView = (RecyclerView) view.findViewById(R.id.listCardView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        AdapterMusculo mAdapter;

        if(!base) {
            if(getTreino().getVisualizar()){
                mAdapter = new AdapterMusculo(R.layout.adapter_card_view_treino_visualizar, view.getContext(), lstMusculos, getTreino().getMusculosRemovidos(), tipoMusculo, getTreino().getVisualizar());
            }else{
                mAdapter = new AdapterMusculo(R.layout.adapter_card_view_treino, view.getContext(), lstMusculos, getTreino().getMusculosRemovidos(), tipoMusculo, getTreino().getVisualizar());
            }
        }else{
            if(getTreinoBase().getVisualizar()) {
                mAdapter = new AdapterMusculo(R.layout.adapter_card_view_treino_visualizar, view.getContext(), lstMusculos, getTreinoBase().getMusculosRemovidos(), tipoMusculo, getTreinoBase().getVisualizar());
            }else{
                mAdapter = new AdapterMusculo(R.layout.adapter_card_view_treino, view.getContext(), lstMusculos, getTreinoBase().getMusculosRemovidos(), tipoMusculo, getTreinoBase().getVisualizar());
            }

        }

        mRecyclerView.setAdapter(mAdapter);
        return view;
    }

    public void salvarTreinoMusculo(){
        //Esconde teclado
//        ((InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE))
//                .hideSoftInputFromWindow(
//                        ((Button) getView().findViewById(R.id.btnExcluiExercicio)).getWindowToken(), 0
//                );

        FragmentManager fragmentManager = getFragmentManager();
        FragmentNovoTreinoPt2 fragNovoTreino = new FragmentNovoTreinoPt2();

        if(!base){
            fragNovoTreino.setTreino(getTreino());
        }else{
            fragNovoTreino.setTreinoBase(getTreinoBase());
            fragNovoTreino.setBase(base);
        }

        fragmentManager.beginTransaction()
                .replace(R.id.container, fragNovoTreino, String.valueOf(Constantes.FRAGMENT_NOVO_TREINO))
                .addToBackStack(String.valueOf(Constantes.FRAGMENT_NOVO_TREINO))
                .commit();
    }

    public boolean verificaMusculosPreenchidos(Boolean exibeToast){
        ArrayList<Musculo> arrMusculos;
        String campo = "";

        if(!base) {
            arrMusculos = getTreino().getLstMusculos(tipoMusculo);
        }else{
            arrMusculos = getTreinoBase().getLstMusculos(tipoMusculo);
        }

        boolean retorno = true;
        for (Musculo m : arrMusculos) {
            if(m.getCarga() == null || m.getCarga().isEmpty()){
                retorno = false;
                campo = "Carga";
                break;
            }else if(m.getCarga().toString().replace("-","").equals("#") || m.getCarga().toString().replace("-","").equals("*")){
                retorno = false;
                campo = "Carga";
                break;
            }

            if(m.getExercicio() == null || m.getExercicio().isEmpty()){
                retorno = false;
                campo = "Exercicio/Máquina";
                break;
            }

            if(m.getRepeticoes() == null || m.getRepeticoes().isEmpty()){
                retorno = false;
                campo = "Repetições";
                break;
            }else if(m.getRepeticoes().toString().replace("-","").equals("#") || m.getRepeticoes().toString().replace("-","").equals("*")){
                retorno = false;
                campo = "Repetições";
                break;
            }

            if(m.getIntervalos() == Constantes.ITEM_INTERVALO_VAZIO){
                retorno = false;
                campo = "Intervalos";
                break;
            }

            if(m.getSeries() == Constantes.ITEM_SERIE_VAZIA){
                retorno = false;
                campo = "Series";
                break;
            }

            if(m.getTmpExecIni() == Constantes.ITEM_VEL_EXERCICIO_VAZIO || m.getTmpExecFim() == Constantes.ITEM_VEL_EXERCICIO_VAZIO){
                retorno = false;
                campo = "Vel Exercicios";
                break;
            }

        }

        if(!retorno && exibeToast){
            Toast.makeText(getContext(), "ERRO, campo " + campo + " vazio", Toast.LENGTH_LONG).show();
        }

        return retorno;
    }

    public void excluiMusculosIncompletos(){
        ArrayList<Musculo> arrMusculos;
        ArrayList<Musculo> arrRemocao = new ArrayList<>();

        if(!base){
            arrMusculos = getTreino().getLstMusculos(tipoMusculo);
        }else{
            arrMusculos = getTreinoBase().getLstMusculos(tipoMusculo);
        }


        for (Musculo m : arrMusculos) {
            boolean retorno = true;

            if (m.getCarga() == null || m.getCarga().isEmpty()) {
                retorno = false;
            }

            if (retorno & m.getExercicio() == null || m.getExercicio().isEmpty()) {
                retorno = false;
            }

            if (retorno & m.getRepeticoes() == null || m.getRepeticoes().isEmpty()) {
                retorno = false;
            }

            if (retorno & m.getIntervalos() == Constantes.ITEM_INTERVALO_VAZIO) {
                retorno = false;
            }

            if (retorno & m.getSeries() == Constantes.ITEM_SERIE_VAZIA) {
                retorno = false;
            }

            if (retorno & m.getTmpExecIni() == Constantes.ITEM_VEL_EXERCICIO_VAZIO || m.getTmpExecFim() == Constantes.ITEM_VEL_EXERCICIO_VAZIO) {
                retorno = false;
            }

            if(!retorno){
                arrRemocao.add(m);
            }
        }

        if (arrRemocao.size() > 0){
            for (Musculo m : arrRemocao) {
                arrMusculos.remove(m);
            }
        }

    }

    public boolean possuiMusculos(){
        boolean retorno = false;

        if(!getBase()) {
            for (Integer i = 0; i < getTreino().getLstTodosMusculos().size(); i++) {
                if (getTreino().getLstMusculos(i) != null && getTreino().getLstMusculos(i).size() > 0) {
                    retorno = true;
                    break;
                }
            }
        }else{
            for (Integer i = 0; i < getTreinoBase().getLstTodosMusculos().size(); i++) {
                if (getTreinoBase().getLstMusculos(i) != null && getTreinoBase().getLstMusculos(i).size() > 0) {
                    retorno = true;
                    break;
                }
            }
        }

        return retorno;
    }

}
