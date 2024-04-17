package com.masidev.fitre.fragment;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.masidev.fitre.R;
import com.masidev.fitre.constante.Constantes;
import com.masidev.fitre.data.dao.PersonalDAO;
import com.masidev.fitre.data.entidade.Academia;
import com.masidev.fitre.data.entidade.Personal;
import com.masidev.fitre.mascara.Mask;

/**
 * Created by jmasiero on 02/05/16.
 */
public class FragmentDialogTrocarPersonal extends DialogFragment{
    public TextView getHeaderView() {
        return headerView;
    }

    public void setHeaderView(TextView headerView) {
        this.headerView = headerView;
    }

    private TextView headerView;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //return super.onCreateDialog(savedInstanceState);

        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_trocar_personal, null);
        builder.setView(view)
                .setMessage("Trocar Personal")
                .setPositiveButton("Confirmar", new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int id) {
                        trocarPersonal(dialog);
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });

        iniciaMascara(view);
        // Create the AlertDialog object and return it
        return builder.create();
    }

    private void iniciaMascara(View view) {
        EditText edtCpf = (EditText) view.findViewById(R.id.edtCpf);
        edtCpf.addTextChangedListener(Mask.insert("###.###.###-##", edtCpf));
    }

    private void trocarPersonal(DialogInterface dialog){
        EditText edtCpf = (EditText) ((AlertDialog) dialog).findViewById(R.id.edtCpf);
        EditText edtSenha = (EditText) ((AlertDialog) dialog).findViewById(R.id.edtSenha);
        String cpf = Mask.unmask(edtCpf.getText().toString());
        String senha = edtSenha.getText().toString();

        Personal p = new Personal();
        p.setCPF(cpf);
        p.setSenha(senha);

        SharedPreferences sp1 = getActivity().getSharedPreferences(Constantes.SHARED_PREFERENCES_LOGIN, Context.MODE_PRIVATE);
        Academia ac = new Academia(sp1.getString(Constantes.CNPJ_ACADEMIA_LOGADA, ""));

        p.setAcademia(ac);

        Personal personalValidado = PersonalDAO.getInstance(getContext()).validar(p);
        if(personalValidado != null){
            SharedPreferences sp2 = getContext().getSharedPreferences(Constantes.SHARED_PREFERENCES_LOGIN, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sp2.edit();
            editor.putString(Constantes.NOME_TREINADOR_LOGADO, personalValidado.getNome());
            editor.putString(Constantes.CPF_TREINADOR_LOGADO, personalValidado.getCPF());

            getHeaderView().setText(personalValidado.getNome());

            editor.commit();
        }else{
            Toast t = Toast.makeText(getContext(), "ERRO! CPF ou Senha incorretos!", Toast.LENGTH_LONG);
            t.setGravity(Gravity.CENTER, 0, 0);
            t.show();
        }
    }

}
