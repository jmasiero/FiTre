package com.masidev.fitre.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.masidev.fitre.R;
import com.masidev.fitre.adapter.AdapterAluno;
import com.masidev.fitre.constante.Constantes;
import com.masidev.fitre.data.dao.AlunoDAO;
import com.masidev.fitre.data.entidade.Aluno;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by jmasiero on 10/05/16.
 */
public class FragmentDialogSelecionaAlunoParaTestePt1 extends DialogFragment {
    private ArrayList<Aluno> lstAlunos;
    private ListView lstVwAluno;
    private Aluno alunoSelecionado;
    private String dataAtual;
    private EditText edtNome;

    @Override
    public void onStart() {
        super.onStart();

        // safety check
        if (getDialog() == null) {
            return;
        }

        DisplayMetrics metrics = new DisplayMetrics();
        getDialog().getWindow().getWindowManager().getDefaultDisplay().getMetrics(metrics);

        int y = metrics.heightPixels;

        int x = metrics.widthPixels;

        int dialogHeight = (int) (y*0.65);
        int dialogWidth = (int) (x*0.75);

        getDialog().getWindow().setLayout(dialogWidth, dialogHeight);

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_dialog_lista_filtra_aluno, null);
        builder.setView(view);

        organizaTela(view);
        iniciaListeners(view);
        return builder.create();
    }

    private void iniciaListeners(final View view) {
        lstVwAluno.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                alunoSelecionado = lstAlunos.get(position);
                getDialog().dismiss();

                FragmentDialogSelecionaAlunoParaTestePt2 fragment = new FragmentDialogSelecionaAlunoParaTestePt2();
                fragment.setAlunoSelecionado(alunoSelecionado);
                fragment.show(getFragmentManager(), String.valueOf(Constantes.FRAGMENT_DIALOG_SELECIONA_ALUNO_PARA_TESTE_PT2));
            }
        });

        edtNome = (EditText) view.findViewById(R.id.edtNome);
        edtNome.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String filtro = edtNome.getText().toString().trim();
                if (filtro != "") {
                    lstAlunos = AlunoDAO.getInstance(getContext()).listarAlunosSemTreinoDeHoje(dataAtual, filtro);
                    preencheLista(view);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    private void organizaTela(View view) {
        listarAlunos(view);
    }

    private void listarAlunos(View view) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-d");
        dataAtual = sdf.format(System.currentTimeMillis());

        lstAlunos = AlunoDAO.getInstance(getContext()).listarAlunosSemTreinoDeHoje(dataAtual, "");
        preencheLista(view);
    }

    private void preencheLista(View view){
        lstVwAluno = (ListView) view.findViewById(R.id.listaDeAluno);
        AdapterAluno adapter = new AdapterAluno(
                view.getContext(),
                R.layout.adapter_aluno_row_item,
                lstAlunos);
        lstVwAluno.setAdapter(adapter);
    }

}
