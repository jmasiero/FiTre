package com.masidev.fitre.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.masidev.fitre.R;
import com.masidev.fitre.activity.Main;
import com.masidev.fitre.adapter.AdapterProgramaTreino;
import com.masidev.fitre.constante.Constantes;
import com.masidev.fitre.data.dao.ProgramaTreinoDAO;
import com.masidev.fitre.data.entidade.Aluno;
import com.masidev.fitre.data.entidade.ProgramaTreino;

import java.util.ArrayList;

/**
 * Created by jmasiero on 16/10/15.
 */
public class FragmentNovoProgramaTreinamento extends Fragment {
    private Aluno aluno;
    private String nomePrograma;
    private ArrayList<ProgramaTreino> lstProgramaTreino;
    private ArrayList<ProgramaTreino> lstProgramaTreinoExcluidos;
    private TextView txtNomeAluno;
    private EditText edtNomePrograma;
    private Button btnSalvar;
    private Button btnSair;
    private Boolean visualizar;
    private Boolean editar;

    public ArrayList<ProgramaTreino> getLstProgramaTreinoExcluidos() {
        if(lstProgramaTreinoExcluidos == null){
            lstProgramaTreinoExcluidos = new ArrayList<>();
        }
        return lstProgramaTreinoExcluidos;
    }

    public void setLstProgramaTreinoExcluidos(ArrayList<ProgramaTreino> lstProgramaTreinoExcluidos) {
        this.lstProgramaTreinoExcluidos = lstProgramaTreinoExcluidos;
    }

    public ArrayList<ProgramaTreino> getLstProgramaTreino() {
        if(lstProgramaTreino == null){
            lstProgramaTreino = new ArrayList<>();
        }
        return lstProgramaTreino;
    }

    public void setLstProgramaTreino(ArrayList<ProgramaTreino> lstProgramaTreino) {
        this.lstProgramaTreino = lstProgramaTreino;
    }


    public Aluno getAluno(){
        return this.aluno;
    }
    public void setAluno(Aluno aluno) {
        this.aluno = aluno;
    }

    public static FragmentNovoProgramaTreinamento newInstance() {
        Bundle args = new Bundle();

        FragmentNovoProgramaTreinamento fragment = new FragmentNovoProgramaTreinamento();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_novo_programa_de_treinamento, container, false);
        edtNomePrograma = (EditText) view.findViewById(R.id.edtNomePrograma);

        if(getVisualizar() || getEditar()){
            preencheListaDeProgramas();
            populaCampos(view);
        }

        if (getAluno() != null ) {
            txtNomeAluno = (TextView) view.findViewById(R.id.txtNomeAluno);
            txtNomeAluno.setText(getAluno().getNome());
        }

        if(getLstProgramaTreino().size() == 0){
            ProgramaTreino pt = new ProgramaTreino();
            pt.setSimples(true);
            getLstProgramaTreino().add(pt);
        }

        RecyclerView mRecyclerView = (RecyclerView) view.findViewById(R.id.listCardView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        AdapterProgramaTreino mAdapter;
        mAdapter = new AdapterProgramaTreino(R.layout.adapter_card_view_programa_de_treinamento, getContext(), getLstProgramaTreino(), aluno, getVisualizar(), getLstProgramaTreinoExcluidos());
        mAdapter.setFragmentManager(getFragmentManager());
        mRecyclerView.setAdapter(mAdapter);

        criaListeners(view);
        return view;
    }

    private void populaCampos(View view) {
        if(getVisualizar()){
            edtNomePrograma.setVisibility(View.GONE);
            TextView txtNomePrograma = (TextView) view.findViewById(R.id.txtNomePrograma);
            txtNomePrograma.setVisibility(View.VISIBLE);
            txtNomePrograma.setText(getLstProgramaTreino().get(0).getNomePrograma());
        }else if(getEditar()){
            edtNomePrograma.setVisibility(View.GONE);
            edtNomePrograma.setText(getLstProgramaTreino().get(0).getNomePrograma());
            TextView txtNomePrograma = (TextView) view.findViewById(R.id.txtNomePrograma);
            txtNomePrograma.setVisibility(View.VISIBLE);
            txtNomePrograma.setText(getLstProgramaTreino().get(0).getNomePrograma());
        }else{
            edtNomePrograma.setText(getLstProgramaTreino().get(0).getNomePrograma());
        }
    }

    private void preencheListaDeProgramas() {
        setLstProgramaTreino(ProgramaTreinoDAO.getInstance(getContext()).retornarProgramasDoAluno(getAluno()));
    }

    private void criaListeners(View view) {
        btnSalvar = (Button) view.findViewById(R.id.btnSalvar);
        btnSair = (Button) view.findViewById(R.id.btnSair);

        btnSair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();

                fragmentManager.beginTransaction()
                        .replace(R.id.container, Main.PlaceholderFragment.newInstance(Constantes.FRAGMENT_PROGRAMA_DE_TREINO), String.valueOf(Constantes.FRAGMENT_PROGRAMA_DE_TREINO))
                        .addToBackStack(String.valueOf(Constantes.FRAGMENT_PROGRAMA_DE_TREINO))
                        .commit();
            }
        });

        if(getVisualizar()){
            btnSalvar.setVisibility(View.GONE);
        }else{
            btnSalvar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (verificaCampos(v)) {
                        try {
                            boolean novo = true;
                            for (ProgramaTreino pt : getLstProgramaTreinoExcluidos()){
                                ProgramaTreinoDAO.getInstance(getContext()).excluir(pt, true);
                                novo = false;
                            }

                            for (ProgramaTreino pt : aluno.getLstProgramaTreino()) {
                                pt.setNomePrograma(edtNomePrograma.getText().toString());
                                pt.setAluno(new Aluno(aluno.getCPF()));
                                if(pt.getId() != null){
                                    ProgramaTreinoDAO.getInstance(getContext()).alterar(pt, true);
                                    novo = false;
                                }else{
                                    ProgramaTreinoDAO.getInstance(getContext()).salvar(pt, true);
                                }
                            }

                            if(novo){
                                mostraToast(getContext().getString(R.string.programa_de_treinamento_salvo));
                            }else{
                                mostraToast(getContext().getString(R.string.programa_de_treinamento_atualizado));
                            }

                            FragmentManager fragmentManager = getFragmentManager();

                            fragmentManager.beginTransaction()
                                    .replace(R.id.container, Main.PlaceholderFragment.newInstance(Constantes.FRAGMENT_PROGRAMA_DE_TREINO), String.valueOf(Constantes.FRAGMENT_PROGRAMA_DE_TREINO))
                                    .addToBackStack(String.valueOf(Constantes.FRAGMENT_PROGRAMA_DE_TREINO))
                                    .commit();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }

    }

    private boolean verificaCampos(View v) {
        boolean retorno = true;
        String mensagem = "";

        if(edtNomePrograma.getText().toString().trim().isEmpty()){
            retorno = false;
            mensagem = getContext().getString(R.string.Erro_nome_do_programa_vazio);
        }

        if(retorno) {
            for (ProgramaTreino pt : aluno.getLstProgramaTreino()) {
                if (pt.getLstMusculos().size() == 0) {
                    retorno = false;
                    mensagem = getContext().getString(R.string.Erro_programa) + pt.getOrdem() + getContext().getString(R.string.nao_possui_exercicio);
                }
            }
        }

        if(!retorno){
            mostraToast(mensagem);
        }

        return retorno;
    }

    private void mostraToast(String mensagem){
        Toast t = Toast.makeText(getContext(), mensagem, Toast.LENGTH_LONG);
        t.setGravity(Gravity.CENTER, Gravity.CENTER_HORIZONTAL,Gravity.CENTER_VERTICAL);
        t.show();
    }

    public Boolean getVisualizar() {
        if(visualizar == null){
            visualizar = false;
        }
        return visualizar;
    }

    public void setVisualizar(Boolean visualizar) {
        this.visualizar = visualizar;
    }

    public Boolean getEditar() {
        if(editar == null){
            editar = false;
        }
        return editar;
    }

    public void setEditar(Boolean editar) {
        this.editar = editar;
    }
}
