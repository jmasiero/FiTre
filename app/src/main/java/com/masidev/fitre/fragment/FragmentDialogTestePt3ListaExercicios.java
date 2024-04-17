package com.masidev.fitre.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.masidev.fitre.R;
import com.masidev.fitre.adapter.AdapterAluno;
import com.masidev.fitre.adapter.AdapterAlunoEmTeste;
import com.masidev.fitre.adapter.AdapterMusculoTeste;
import com.masidev.fitre.constante.Constantes;
import com.masidev.fitre.data.dao.AlunoDAO;
import com.masidev.fitre.data.dao.TreinoDAO;
import com.masidev.fitre.data.entidade.Aluno;
import com.masidev.fitre.data.entidade.Musculo;
import com.masidev.fitre.data.entidade.Personal;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by jmasiero on 10/05/16.
 */
public class FragmentDialogTestePt3ListaExercicios extends DialogFragment {
    private Aluno alunoSelecionado;
    private ListView listaExercicios;
    private Button btnFechar;
    private Button btnFinalizar;

    public Aluno getAlunoSelecionado() {
        return alunoSelecionado;
    }

    public void setAlunoSelecionado(Aluno alunoSelecionado) {
        this.alunoSelecionado = alunoSelecionado;
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

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_dialog_teste_lista_exercicios, null);
        builder.setView(view);
        iniciaListeners(view);
        preencheLista(view);
        return builder.create();
    }

    private void iniciaListeners(View view) {
        btnFechar = (Button) view.findViewById(R.id.btnFechar);
        btnFechar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        btnFinalizar = (Button) view.findViewById(R.id.btnFinalizar);
        btnFinalizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                salvarTreino();
            }
        });

    }

    private void salvarTreino(){
        new AlertDialog.Builder(getContext())
                .setTitle("Mensagem")
                .setMessage("Deseja realmente finalizar o teste?")
                .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-d");
                        String dataIni = sdf.format(System.currentTimeMillis());
                        alunoSelecionado.getTreino().setDataInicio(dataIni);

                        alunoSelecionado.getTreino().setAluno(new Aluno(alunoSelecionado.getCPF()));

                        SharedPreferences sp2 = getContext().getSharedPreferences(Constantes.SHARED_PREFERENCES_LOGIN, Context.MODE_PRIVATE);
                        Personal personal = new Personal(sp2.getString(Constantes.CPF_TREINADOR_LOGADO, "0"));
                        alunoSelecionado.getTreino().setPersonal(personal);



                        try {
                            TreinoDAO.getInstance(getContext()).salvar(alunoSelecionado.getTreino(), true, false);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        RecyclerView recyclerView = (RecyclerView) getFragmentManager()
                                .findFragmentByTag(Constantes.TAG_FRAGMENT_ALUNOS_EM_TREINAMENTO)
                                .getView()
                                .findViewById(R.id.listTeste);

                        AdapterAlunoEmTeste adapter = (AdapterAlunoEmTeste) recyclerView.getAdapter();
                        ArrayList<Aluno> lstAlunos = adapter.retornaListaAlunos();
                        lstAlunos.remove(alunoSelecionado);
                        if(lstAlunos.size() == 0){
                            recyclerView.setVisibility(View.GONE);
                        }

                        adapter.notifyDataSetChanged();
                        dismiss();
                    }
                })
                .setNegativeButton("Não", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .show();
    }

    private void preencheLista(View view){
        listaExercicios = (ListView) view.findViewById(R.id.listaDeExercicios);
        AdapterMusculoTeste adapter = new AdapterMusculoTeste(getContext(),
                                                                R.layout.adapter_musculo_teste_row_item,
                                                                retornaListaComMusculos(),
                                                                getFragmentManager(),
                                                                alunoSelecionado
                                                            );
        listaExercicios.setAdapter(adapter);
    }

    private ArrayList<Musculo> retornaListaComMusculos(){
        ArrayList<Musculo> listaRetorno = new ArrayList<>();
        for(int i = 0; i < alunoSelecionado.getTreino().getLstTodosMusculos().size(); i++){
            for(int j = 0; j < alunoSelecionado.getTreino().getLstMusculos(i).size(); j++){
                listaRetorno.add(alunoSelecionado.getTreino().getLstMusculos(i).get(j));
            }
        }

        return listaRetorno;
    }

}
