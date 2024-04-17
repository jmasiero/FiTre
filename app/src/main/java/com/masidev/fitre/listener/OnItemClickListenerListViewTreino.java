package com.masidev.fitre.listener;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.AdapterView;

import com.masidev.fitre.R;
import com.masidev.fitre.adapter.AdapterTreino;
import com.masidev.fitre.constante.Constantes;
import com.masidev.fitre.data.entidade.Treino;
import com.masidev.fitre.fragment.FragmentNovoTreinoPt2;

/**
 * Created by jmasiero on 12/02/16.
 */
public class OnItemClickListenerListViewTreino implements AdapterView.OnItemClickListener{
    Context context;
    AdapterTreino adapter;
    FragmentManager fragmentManager;
    int position;

    public OnItemClickListenerListViewTreino(AdapterTreino a, FragmentManager fragmentManager){
        adapter = a;
        this.fragmentManager = fragmentManager;
        //this.position = position;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Treino treino = adapter.getItem(position);
        treino.setVisualizar(true);

        FragmentNovoTreinoPt2 fragNovoTreino = new FragmentNovoTreinoPt2();
        fragNovoTreino.setTreino(treino);

        fragmentManager.beginTransaction()
                .replace(R.id.container, fragNovoTreino, String.valueOf(Constantes.FRAGMENT_VISUALIZAR_TREINO))
                .addToBackStack(String.valueOf(Constantes.FRAGMENT_VISUALIZAR_TREINO))
                .commit();
    }
}
