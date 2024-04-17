package com.masidev.fitre.listener;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.AdapterView;

import com.masidev.fitre.R;
import com.masidev.fitre.adapter.AdapterListaProgramaTreino;
import com.masidev.fitre.constante.Constantes;
import com.masidev.fitre.data.dao.AlunoDAO;
import com.masidev.fitre.data.entidade.Aluno;
import com.masidev.fitre.data.entidade.ProgramaTreino;
import com.masidev.fitre.fragment.FragmentNovoProgramaTreinamento;

/**
 * Created by jmasiero on 12/02/16.
 */
public class OnItemClickListenerListViewProgramaTreino implements AdapterView.OnItemClickListener{
    Context context;
    AdapterListaProgramaTreino adapter;
    FragmentManager fragmentManager;
    int position;

    public OnItemClickListenerListViewProgramaTreino(AdapterListaProgramaTreino a, FragmentManager fragmentManager){
        adapter = a;
        this.fragmentManager = fragmentManager;
        //this.position = position;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ProgramaTreino pt = adapter.getItem(position);
        Aluno a = AlunoDAO.getInstance(context).getAluno(pt.getAluno().getCPF());

        FragmentNovoProgramaTreinamento fragment = new FragmentNovoProgramaTreinamento();
        fragment.setAluno(a);

        fragment.setVisualizar(true);

        fragmentManager.beginTransaction()
                .replace(R.id.container, fragment, String.valueOf(Constantes.FRAGMENT_PROGRAMA_DE_TREINO))
                .addToBackStack(String.valueOf(Constantes.FRAGMENT_PROGRAMA_DE_TREINO))
                .commit();
    }
}
