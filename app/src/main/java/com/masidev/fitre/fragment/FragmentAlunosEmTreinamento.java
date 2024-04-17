package com.masidev.fitre.fragment;

import android.graphics.Point;
import android.os.Bundle;


import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;

import com.masidev.fitre.R;
import com.masidev.fitre.adapter.AdapterAlunoEmTeste;
import com.masidev.fitre.adapter.AdapterAlunoEmTreinamento;
import com.masidev.fitre.data.entidade.Aluno;

import java.util.ArrayList;


public class FragmentAlunosEmTreinamento extends Fragment {
    private ArrayList<Aluno> lstAlunos;
    private ArrayList<Aluno> lstAlunas;
    private ArrayList<Aluno> lstAlunosEmTeste;

    private RecyclerView recyclerView1;
    private RecyclerView recyclerView2;
    private RecyclerView recyclerViewTeste;


    public static FragmentAlunosEmTreinamento newInstance() {
        FragmentAlunosEmTreinamento fragment = new FragmentAlunosEmTreinamento();
        return fragment;
    }

    public FragmentAlunosEmTreinamento() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        iniciaListeners(view);

        if(lstAlunos == null){
            lstAlunos = new ArrayList<>();
        }
        if(lstAlunas == null){
            lstAlunas = new ArrayList<>();
        }

        AdapterAlunoEmTreinamento adapterHomens = new AdapterAlunoEmTreinamento(R.layout.adapter_card_view_aluno_treinando, view.getContext(), lstAlunos);
        AdapterAlunoEmTreinamento adapterMulheres = new AdapterAlunoEmTreinamento(R.layout.adapter_card_view_aluno_treinando, view.getContext(), lstAlunas);

        recyclerView1 = (RecyclerView) view.findViewById(R.id.listCardTreino1);
        recyclerView2 = (RecyclerView) view.findViewById(R.id.listCardTreino2);

        recyclerView1.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView1.setItemAnimator(new DefaultItemAnimator());
        configuraTamanhoRecyclerView(recyclerView1);

        recyclerView2.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView2.setItemAnimator(new DefaultItemAnimator());
        configuraTamanhoRecyclerView(recyclerView2);

        adapterHomens.setFragmentManager(getFragmentManager());
        adapterMulheres.setFragmentManager(getFragmentManager());

        recyclerView1.setAdapter(adapterHomens);
        recyclerView2.setAdapter(adapterMulheres);

        //inicia campo teste rms
        recyclerViewTeste = (RecyclerView) view.findViewById(R.id.listTeste);
        recyclerViewTeste.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerViewTeste.setItemAnimator(new DefaultItemAnimator());

        if(lstAlunosEmTeste == null){
            lstAlunosEmTeste = new ArrayList<>();
        }else if(lstAlunosEmTeste.size() > 0){
            recyclerViewTeste.setVisibility(View.VISIBLE);
        }

        AdapterAlunoEmTeste adapterAlunosTeste = new AdapterAlunoEmTeste(R.layout.adapter_card_view_aluno_treinando, view.getContext(), lstAlunosEmTeste);
        adapterAlunosTeste.setFragmentManager(getFragmentManager());
        recyclerViewTeste.setAdapter(adapterAlunosTeste);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewTeste.setLayoutManager(layoutManager);

        return view;
    }

    private void configuraTamanhoRecyclerView(RecyclerView rv){
        WindowManager wm = (WindowManager) getContext().getSystemService(getContext().WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();

        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        rv.getLayoutParams().width = width/2;
    }

    private void iniciaListeners(View view) {
        ImageButton btnNovoAluno = (ImageButton) view.findViewById(R.id.floatNovoAluno);

        btnNovoAluno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iniciaTreinamento();
            }
        });

    }

    private void iniciaTreinamento(){

        FragmentDialogBuscaAlunoIniciaTreino dialog = new FragmentDialogBuscaAlunoIniciaTreino();
        dialog.setLstAlunosEAlunas(lstAlunos, lstAlunas);
        dialog.show(getFragmentManager(), "busca_aluno_treino");
    }

    @Override
    public void onDetach() {
        //getFragmentManager().popBackStack(Constantes.TAG_FRAGMENT_BUSCA_ALUNO_INICIA_TREINO, FragmentManager.POP_BACK_STACK_INCLUSIVE);

        super.onDetach();
    }
}
