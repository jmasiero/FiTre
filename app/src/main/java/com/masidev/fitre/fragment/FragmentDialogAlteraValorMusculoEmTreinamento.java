package com.masidev.fitre.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.masidev.fitre.R;
import com.masidev.fitre.constante.Constantes;
import com.masidev.fitre.data.dao.MusculoDAO;
import com.masidev.fitre.data.entidade.Musculo;

public class FragmentDialogAlteraValorMusculoEmTreinamento extends DialogFragment {
    private String campoClicado;
    private TextView txtValorCampo;
    private TextView txtNomeCampo;
    private String valorCampoAntigo;

    public Button getCampoAnterior() {
        return campoAnterior;
    }

    private Button campoAnterior;

    public void setCampoAnterior(Button campoAnterior) {
        this.campoAnterior = campoAnterior;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_popup_altera_valor_musculo_em_treinamento, null);
        builder.setView(view);

        iniciaListeners(view);
        preencheCampo(view);

        return builder.create();
    }

    private void iniciaListeners(View v) {
        Button btnOk = (Button) v.findViewById(R.id.btnOk);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!valorCampoAntigo.equals(txtValorCampo.getText().toString())){
                    salvarDados();
                }
                getDialog().dismiss();
            }
        });

        Button btnMenos = (Button) v.findViewById(R.id.btnMenos);
        btnMenos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alteraValorCampo(false);
            }
        });

        Button btnMais = (Button) v.findViewById(R.id.btnMais);
        btnMais.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alteraValorCampo(true);
            }
        });
    }

    private void preencheCampo(View v){
        txtNomeCampo = (TextView) v.findViewById(R.id.txtCampoNome);
        txtValorCampo = (TextView) v.findViewById(R.id.txtCampoValor);

        Musculo m = (Musculo) getArguments().getSerializable(Constantes.BUNDLE_MUSCULO);
        campoClicado = getArguments().getString(Constantes.CAMPO_CLICADO);

        txtNomeCampo.setText(campoClicado);
        if(campoClicado.equals(Constantes.CARGAS)){
            txtValorCampo.setText(m.getCarga());
        }else {
            txtValorCampo.setText(m.getRepeticoes());
        }

        valorCampoAntigo = txtValorCampo.getText().toString();
    }

    private void alteraValorCampo(boolean incrementa){
        String valor = txtValorCampo.getText().toString();
        String[] arrValores;

        int barra = valor.indexOf("-");
        if(barra >= 0){
            arrValores = valor.split("-");
        }else{
            arrValores = new String[0];
        }

        if(incrementa){
            if(arrValores.length > 0){
                for (int i = 0; i < arrValores.length; i++){
                    arrValores[i] = String.valueOf(Integer.parseInt(arrValores[i]) + 1);
                }
            }else{
                valor = String.valueOf(Integer.parseInt(valor) + 1);
            }
        }else{
            if(arrValores.length > 0){
                for (int i = 0; i < arrValores.length; i++){
                    if(Integer.parseInt(arrValores[i]) > 0) {
                        arrValores[i] = String.valueOf(Integer.parseInt(arrValores[i]) - 1);
                    }
                }
            }else{
                if(Integer.parseInt(valor) > 0) {
                    valor = String.valueOf(Integer.parseInt(valor) - 1);
                }
            }
        }

        if(arrValores.length > 0){
            valor = "";
            for (int i = 0; i < arrValores.length; i++){
                valor += arrValores[i];
                if(i != (arrValores.length - 1)){
                    valor += "-";
                }
            }

        }

        txtValorCampo.setText(valor);
    }

    private void salvarDados(){
        Musculo m = (Musculo) getArguments().getSerializable(Constantes.BUNDLE_MUSCULO);
        getCampoAnterior().setText(txtValorCampo.getText().toString());

        if(campoClicado == Constantes.CARGAS){
            m.setCarga(txtValorCampo.getText().toString());
            try {
                MusculoDAO.getInstance(getContext()).alterarCargasMusculo(m);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else{
            m.setRepeticoes(txtValorCampo.getText().toString());
            try {
                MusculoDAO.getInstance(getContext()).alterarRepeticoesMusculo(m);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
