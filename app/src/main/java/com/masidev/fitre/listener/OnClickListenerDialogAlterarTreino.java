package com.masidev.fitre.listener;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.Button;

import com.masidev.fitre.constante.Constantes;
import com.masidev.fitre.data.entidade.Musculo;
import com.masidev.fitre.fragment.FragmentDialogAlteraValorMusculoEmTreinamento;

/**
 * Created by jmasiero on 29/02/16.
 */
public class OnClickListenerDialogAlterarTreino implements View.OnClickListener {
    private FragmentManager fragmentManager;
    private Musculo musculo;
    private String campoCliclado;
    private Button btnCampoAnterior;

    public OnClickListenerDialogAlterarTreino(FragmentManager fragmentManager, Musculo m, String campoCliclado, Button btn){
        this.fragmentManager = fragmentManager;
        this.musculo = m;
        this.campoCliclado = campoCliclado;
        this.btnCampoAnterior = btn;
    }

    @Override
    public void onClick(View v) {
        criaListenerPopupAlterarTreino();
    }

    private void criaListenerPopupAlterarTreino(){

        FragmentDialogAlteraValorMusculoEmTreinamento fragmentAlteraTreino = new FragmentDialogAlteraValorMusculoEmTreinamento();
        Bundle b = new Bundle();
        b.putString(Constantes.CAMPO_CLICADO, campoCliclado);
        b.putSerializable(Constantes.BUNDLE_MUSCULO, musculo);
        fragmentAlteraTreino.setCampoAnterior(btnCampoAnterior);
        fragmentAlteraTreino.setArguments(b);

        fragmentAlteraTreino.show(fragmentManager, Constantes.ALTERAR_MUSCULO_TREINO);
    }

}
