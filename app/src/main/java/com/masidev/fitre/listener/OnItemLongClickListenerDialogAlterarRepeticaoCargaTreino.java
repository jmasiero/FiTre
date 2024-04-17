package com.masidev.fitre.listener;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.masidev.fitre.R;
import com.masidev.fitre.constante.Constantes;
import com.masidev.fitre.data.entidade.Musculo;
import com.masidev.fitre.fragment.FragmentDialogAlteraRepeticoesCargasMusculoEmTreinamento;

import java.util.ArrayList;

/**
 * Created by jmasiero on 29/02/16.
 */
public class OnItemLongClickListenerDialogAlterarRepeticaoCargaTreino implements AdapterView.OnItemLongClickListener{
    private FragmentManager fragmentManager;
    private Musculo musculo;
    private ArrayList<Musculo> lstMusculos;
    private TextView txtCargaAnterior;
    private TextView txtRepeticaoAnterior;

    public OnItemLongClickListenerDialogAlterarRepeticaoCargaTreino(FragmentManager fragmentManager, ArrayList<Musculo> lstM){
        this.fragmentManager = fragmentManager;
        this.lstMusculos = lstM;
    }

    private void criaListenerPopupAlterarTreino(){

        FragmentDialogAlteraRepeticoesCargasMusculoEmTreinamento fragmentAlteraTreino = new FragmentDialogAlteraRepeticoesCargasMusculoEmTreinamento();
        Bundle b = new Bundle();
        b.putSerializable(Constantes.BUNDLE_MUSCULO, musculo);
        fragmentAlteraTreino.setTxtRepeticaoAnterior(txtRepeticaoAnterior);
        fragmentAlteraTreino.setTxtCargaAnterior(txtCargaAnterior);
        fragmentAlteraTreino.setArguments(b);

        fragmentAlteraTreino.show(fragmentManager, Constantes.ALTERAR_MUSCULO_TREINO);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        musculo = lstMusculos.get(position);
        txtCargaAnterior = (TextView) view.findViewById(R.id.cargas);
        txtRepeticaoAnterior = (TextView) view.findViewById(R.id.repeticoes);
        criaListenerPopupAlterarTreino();
        return false;
    }
}
