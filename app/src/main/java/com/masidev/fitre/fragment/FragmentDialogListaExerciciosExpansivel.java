package com.masidev.fitre.fragment;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ExpandableListView;
import android.widget.RadioButton;

import com.masidev.fitre.R;
import com.masidev.fitre.adapter.AdapterListaExerciciosExpansivel;
import com.masidev.fitre.constante.Constantes;
import com.masidev.fitre.data.dao.TreinoDAO;
import com.masidev.fitre.data.entidade.Aluno;
import com.masidev.fitre.data.entidade.ProgramaTreino;

import java.util.ArrayList;

/**
 * Created by jmasiero on 02/05/16.
 */
public class FragmentDialogListaExerciciosExpansivel extends DialogFragment{
    private ExpandableListView lstExerciciosExpansivel;
    private Aluno aluno;
    private ProgramaTreino programaTreino;
    private Context contextBase;
    private Boolean visualizar;

    public Boolean getVisualizar() {
        if (visualizar == null){
            visualizar = false;
        }
        return visualizar;
    }

    public void setVisualizar(Boolean visualizar) {
        this.visualizar = visualizar;
    }


    public ProgramaTreino getProgramaTreino() {
        return programaTreino;
    }

    public void setProgramaTreino(ProgramaTreino programaTreino) {
        this.programaTreino = programaTreino;
    }

    public Aluno getAluno() {
        return aluno;
    }

    public void setAluno(Aluno aluno) {
        this.aluno = aluno;
    }

    public static FragmentDialogListaExerciciosExpansivel newInstance() {
        FragmentDialogListaExerciciosExpansivel fragment = new FragmentDialogListaExerciciosExpansivel();
        return fragment;
    }

    public  FragmentDialogListaExerciciosExpansivel() {

    }

    @Override
    public void onStart() {
        super.onStart();

        // safety check
        if (getDialog() == null) {
            return;
        }

        int y;
        int dialogHeight;
        DisplayMetrics metrics = new DisplayMetrics();
        getDialog().getWindow().getWindowManager().getDefaultDisplay().getMetrics(metrics);

        y = metrics.heightPixels;
        dialogHeight = (int) (y * 0.45);

        int x = metrics.widthPixels;
        int dialogWidth = (int) (x * 0.70);

        getDialog().getWindow().setLayout(dialogWidth, dialogHeight);

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_dialog_lista_exercicios, null);
        builder.setView(view);

        preencheAlunoComTreinoCompleto();
        iniciaListeners(view);
        preencheLista(view);
        populaCampos(view);
        return builder.create();
    }

    private void populaCampos(View view) {
        RadioButton rdSimples = (RadioButton) view.findViewById(R.id.rdSimples);
        RadioButton rdMisto = (RadioButton) view.findViewById(R.id.rdMisto);

        if(getProgramaTreino().getMisto()){
            rdSimples.setChecked(false);
            rdMisto.setChecked(true);
        }else{
            rdSimples.setChecked(true);
            rdMisto.setChecked(false);
        }
    }

    private void iniciaListeners(View view) {
        Button btnOk = (Button) view.findViewById(R.id.btnOk);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getProgramaTreino().getSimples() == false && getProgramaTreino().getMisto() == false){
                    getProgramaTreino().setSimples(true);
                    getProgramaTreino().setMisto(false);
                }
                getDialog().dismiss();
            }
        });

        RadioButton rdSimples = (RadioButton) view.findViewById(R.id.rdSimples);
        RadioButton rdMisto = (RadioButton) view.findViewById(R.id.rdMisto);

        if(getVisualizar()){
            rdSimples.setEnabled(false);
            rdMisto.setEnabled(false);
        }else{
            rdSimples.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked){
                        getProgramaTreino().setSimples(true);
                        getProgramaTreino().setMisto(false);
                    }
                }
            });

            rdMisto.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked){
                        getProgramaTreino().setSimples(false);
                        getProgramaTreino().setMisto(true);
                    }
                }
            });
        }
    }

    private void preencheAlunoComTreinoCompleto() {
        setAluno((Aluno) getArguments().getSerializable(Constantes.BUNDLE_ALUNO));
        //private ArrayList<ArrayList<Musculo>> lstTodosMusculos;

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

        getAluno().setTreino(TreinoDAO.getInstance(getContext()).buscarTreinoComMusculosFiltrados(getAluno(), arrMusculos, false, false));
    }

    private void preencheLista(View v){
        programaTreino = (ProgramaTreino) getArguments().getSerializable(Constantes.BUNDLE_PROGRAMA_TREINO);

        lstExerciciosExpansivel = (ExpandableListView) v.findViewById(R.id.lstExerciciosExpansivel);

        lstExerciciosExpansivel.setAdapter(new AdapterListaExerciciosExpansivel(getContext(), getAluno(), programaTreino, getVisualizar()));

    }

}
