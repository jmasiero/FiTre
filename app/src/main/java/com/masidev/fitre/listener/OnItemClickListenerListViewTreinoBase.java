package com.masidev.fitre.listener;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.AdapterView;

import com.masidev.fitre.R;
import com.masidev.fitre.adapter.AdapterTreinoBase;
import com.masidev.fitre.constante.Constantes;
import com.masidev.fitre.data.entidade.TreinoBase;
import com.masidev.fitre.fragment.FragmentNovoTreinoPt2;

/**
 * Created by jmasiero on 07/03/16.
 */
public class OnItemClickListenerListViewTreinoBase implements AdapterView.OnItemClickListener {
    Context context;
    AdapterTreinoBase adapter;
    FragmentManager fragmentManager;
    int position;

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        TreinoBase treinoBase = adapter.getItem(position);
        treinoBase.setVisualizar(true);

        FragmentNovoTreinoPt2 fragNovoTreino = new FragmentNovoTreinoPt2();
        fragNovoTreino.setTreinoBase(treinoBase);
        fragNovoTreino.setBase(true);

        fragmentManager.beginTransaction()
                .replace(R.id.container, fragNovoTreino, String.valueOf(Constantes.FRAGMENT_VISUALIZAR_TREINO))
                .addToBackStack(String.valueOf(Constantes.FRAGMENT_VISUALIZAR_TREINO))
                .commit();
    }

    public OnItemClickListenerListViewTreinoBase(AdapterTreinoBase a, FragmentManager fragmentManager){
        adapter = a;
        this.fragmentManager = fragmentManager;
        //this.position = position;
    }
}
