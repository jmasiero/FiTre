package com.masidev.fitre.fragment;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.masidev.fitre.R;
import com.masidev.fitre.data.dao.AlunoDAO;
import com.masidev.fitre.mascara.Mask;

/**
 * Created by jmasiero on 02/05/16.
 */
public class FragmentDialogImportarAluno extends DialogFragment{



    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //return super.onCreateDialog(savedInstanceState);

        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_importar_aluno, null);
        builder.setView(view)
                .setMessage("Importar Aluno")
                .setPositiveButton("Importar", new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int id) {
                        importarAluno(dialog);
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

    private void importarAluno(DialogInterface dialog){
        EditText edtCpf = (EditText) ((AlertDialog) dialog).findViewById(R.id.edtCpf);
        String cpf = Mask.unmask(edtCpf.getText().toString());

        new Importacao(cpf, getActivity().getApplicationContext()).run();
    }

    public class Importacao extends Thread{
        private String cpf;
        private Context context;

        public Importacao(String cpf, Context context){
            this.cpf = cpf;
            this.context = context;
        }

        @Override
        public void run() {
            AlunoDAO.getInstance(context).importarAlunoPorCpf(cpf);
        }
    }
}
