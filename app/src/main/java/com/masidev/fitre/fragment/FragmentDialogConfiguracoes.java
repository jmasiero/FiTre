package com.masidev.fitre.fragment;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioButton;

import com.masidev.fitre.R;
import com.masidev.fitre.constante.Constantes;

import static com.masidev.fitre.constante.Constantes.*;

/**
 * Created by jmasiero on 02/05/16.
 */
public class FragmentDialogConfiguracoes extends DialogFragment{



    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //return super.onCreateDialog(savedInstanceState);

        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_configuracoes, null);

        RadioButton rdLista = (RadioButton) view.findViewById(R.id.rdLista);
        RadioButton rdJanela = (RadioButton) view.findViewById(R.id.rdJanela);

        SharedPreferences sp1 = getContext().getSharedPreferences(Constantes.SHARED_PREFERENCES_CONFIGURACOES, Context.MODE_PRIVATE);

        String modo = sp1.getString(Constantes.SHARED_PREFERENCES_VISUALIZACAO_TREINAMENTO, Constantes.VISUALIZACAO_TREINAMENTO_JANELA);

        if(modo.equals(Constantes.VISUALIZACAO_TREINAMENTO_JANELA)){
            rdJanela.setChecked(true);
        }else{
            rdLista.setChecked(true);
        }

        builder.setView(view)
                .setMessage(R.string.configuracoes)
                .setPositiveButton("Confirmar", new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int id) {
                        atualizaConfiguracoes(dialog);
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });

        // Create the AlertDialog object and return it
        return builder.create();
    }

    private void atualizaConfiguracoes(DialogInterface dialog) {
        RadioButton rdLista = (RadioButton) ((AlertDialog)dialog).findViewById(R.id.rdLista);
        RadioButton rdJanela = (RadioButton) ((AlertDialog)dialog).findViewById(R.id.rdJanela);

        String modo = Constantes.VISUALIZACAO_TREINAMENTO_JANELA;

        if(rdLista.isChecked()){
            modo = Constantes.VISUALIZACAO_TREINAMENTO_LISTA;
        }

        SharedPreferences sp = getContext().getSharedPreferences(Constantes.SHARED_PREFERENCES_CONFIGURACOES, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sp.edit();

        editor.putString(SHARED_PREFERENCES_VISUALIZACAO_TREINAMENTO, modo);

        editor.commit();
    }

}
