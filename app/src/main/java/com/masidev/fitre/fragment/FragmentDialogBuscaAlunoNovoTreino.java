package com.masidev.fitre.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.masidev.fitre.R;
import com.masidev.fitre.adapter.AdapterAluno;
import com.masidev.fitre.data.dao.AlunoDAO;
import com.masidev.fitre.data.entidade.Aluno;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by jmasiero on 03/11/15.
 */
public class FragmentDialogBuscaAlunoNovoTreino extends DialogFragment {
    private EditText edtNome;
    private ListView listaDeAlunos;
    private ArrayList<Aluno> lstAlunos;
    private ArrayList<Aluno> lstAlunosBusca;
    private String dataAtual;

    public TextView getTxtNomeAlunoSelecionado() {
        return txtNomeAlunoSelecionado;
    }

    public void setTxtNomeAlunoSelecionado(TextView txtNomeAlunoSelecionado) {
        this.txtNomeAlunoSelecionado = txtNomeAlunoSelecionado;
    }

    private TextView txtNomeAlunoSelecionado;

    public Aluno getAlunoSelecionado() {
        return alunoSelecionado;
    }

    public void setAlunoSelecionado(Aluno alunoSelecionado) {
        this.alunoSelecionado = alunoSelecionado;
    }

    private Aluno alunoSelecionado;


    public ArrayList<Aluno> getLstAlunos() {
        return lstAlunos;
    }


    public static FragmentDialogBuscaAlunoNovoTreino newInstance() {
        FragmentDialogBuscaAlunoNovoTreino fragment = new FragmentDialogBuscaAlunoNovoTreino();
        return fragment;
    }

    public FragmentDialogBuscaAlunoNovoTreino() {

    }

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

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_dialog_lista_filtra_aluno, null);
        builder.setView(view);

        listaAlunos(view);
        iniciaListeners(view);

        return builder.create();
    }

    private void listaAlunos(View view) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-d");
        dataAtual = sdf.format(System.currentTimeMillis());

        lstAlunosBusca = AlunoDAO.getInstance(getContext()).listarAlunosSemTreinoDeHoje(dataAtual, "");
        preencheLista(view);
    }

    private void iniciaListeners(final View view) {
        edtNome = (EditText) view.findViewById(R.id.edtNome);
        edtNome.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String filtro = edtNome.getText().toString().trim();
                if (filtro != "") {
                    lstAlunosBusca = AlunoDAO.getInstance(getContext()).listarAlunosSemTreinoDeHoje(dataAtual, filtro);
                    preencheLista(view);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        listaDeAlunos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                Aluno a = (Aluno) listaDeAlunos.getAdapter().getItem(position);
                getAlunoSelecionado().setCPF(a.getCPF());
                getAlunoSelecionado().setNome(a.getNome());
                getAlunoSelecionado().setSexo(a.getSexo());
                getAlunoSelecionado().setDtNascimento(a.getDtNascimento());

                getTxtNomeAlunoSelecionado().setText(a.getNome());

                dismiss();
            }
        });

    }

    private void preencheLista(View view){
        listaDeAlunos = (ListView) view.findViewById(R.id.listaDeAluno);
        AdapterAluno adapter = new AdapterAluno(
                view.getContext(),
                R.layout.adapter_aluno_row_item,
                lstAlunosBusca);
        listaDeAlunos.setAdapter(adapter);

    }

}
