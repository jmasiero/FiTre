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

public class FragmentDialogAlteraRepeticoesCargasMusculoEmTreinamento extends DialogFragment {
    private String campoClicado;
    private TextView txtRepeticoes;
    private TextView txtCargas;
    private String valorRepeticoesAntigo;
    private String valorCargasAntigo;
    private TextView txtCargaAnterior;
    private TextView txtRepeticaoAnterior;


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_popup_altera_repeticoes_cargas_musculo_em_treinamento, null);
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
                if(!valorCargasAntigo.equals(txtCargas.getText().toString()) || !valorRepeticoesAntigo.equals(txtRepeticoes.getText().toString())){
                    salvarDados();
                }
                getDialog().dismiss();
            }
        });

        Button btnMenosRepeticoes = (Button) v.findViewById(R.id.btnMenosRepeticoes);
        btnMenosRepeticoes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alteraValorCampo(Constantes.CAMPO_REPETICAO, Constantes.DECREMENTA);
            }
        });

        Button btnMaisRepeticoes = (Button) v.findViewById(R.id.btnMaisRepeticoes);
        btnMaisRepeticoes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alteraValorCampo(Constantes.CAMPO_REPETICAO, Constantes.INCREMENTA);
            }
        });

        Button btnMenosCargas = (Button) v.findViewById(R.id.btnMenosCargas);
        btnMenosCargas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alteraValorCampo(Constantes.CAMPO_CARGAS, Constantes.DECREMENTA);
            }
        });

        Button btnMaisCargas = (Button) v.findViewById(R.id.btnMaisCargas);
        btnMaisCargas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alteraValorCampo(Constantes.CAMPO_CARGAS, Constantes.INCREMENTA);
            }
        });
    }

    private void preencheCampo(View v){
        txtRepeticoes = (TextView) v.findViewById(R.id.txtRepeticoes);
        txtCargas = (TextView) v.findViewById(R.id.txtCargas);

        Musculo m = (Musculo) getArguments().getSerializable(Constantes.BUNDLE_MUSCULO);

        txtRepeticoes.setText(m.getRepeticoes());
        txtCargas.setText(m.getCarga());

        valorCargasAntigo = txtCargas.getText().toString();
        valorRepeticoesAntigo = txtRepeticoes.getText().toString();
    }

    private void alteraValorCampo(int tipoCampo, boolean incrementa){
        String valor = "";
        String[] arrValores;

        if(tipoCampo == Constantes.CAMPO_CARGAS){
            valor = txtCargas.getText().toString();
        }else{
            valor = txtRepeticoes.getText().toString();
        }

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

        if(tipoCampo == Constantes.CAMPO_CARGAS){
            txtCargas.setText(valor);
        }else{
            txtRepeticoes.setText(valor);
        }
    }

    private void salvarDados(){
        Musculo m = (Musculo) getArguments().getSerializable(Constantes.BUNDLE_MUSCULO);

        getTxtCargaAnterior().setText("C: "+txtCargas.getText().toString());
        getTxtRepeticaoAnterior().setText("R: "+txtRepeticoes.getText().toString());

        m.setRepeticoes(txtRepeticoes.getText().toString());
        m.setCarga(txtCargas.getText().toString());
        try {
            MusculoDAO.getInstance(getContext()).alterarRepeticoesECargasMusculo(m);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public TextView getTxtRepeticaoAnterior() {
        return txtRepeticaoAnterior;
    }

    public void setTxtRepeticaoAnterior(TextView txtRepeticaoAnterior) {
        this.txtRepeticaoAnterior = txtRepeticaoAnterior;
    }

    public TextView getTxtCargaAnterior() {
        return txtCargaAnterior;
    }

    public void setTxtCargaAnterior(TextView txtCargaAnterior) {
        this.txtCargaAnterior = txtCargaAnterior;
    }
}
