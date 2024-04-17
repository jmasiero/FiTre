package com.masidev.fitre.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.masidev.fitre.R;
import com.masidev.fitre.constante.Constantes;
import com.masidev.fitre.data.entidade.Aluno;
import com.masidev.fitre.data.entidade.ProgramaTreino;
import com.masidev.fitre.fragment.FragmentDialogListaExerciciosExpansivel;

import java.util.ArrayList;

/**
 * Created by jmasiero on 27/10/15.
 */
public class AdapterProgramaTreino extends RecyclerView.Adapter<AdapterProgramaTreino.ViewHolder>{

    private final int rowLayout;
    private final Context mContext;
    private Aluno aluno;
    Context contextBase;
    int layoutResourceId;
    ArrayList<ProgramaTreino> lstProgramaTreino;
    ArrayList<ProgramaTreino> lstProgramaTreinoExcluidos;
    Boolean visualizar;

    public Boolean getVisualizar() {
        return visualizar;
    }

    public void setVisualizar(Boolean visualizar) {
        this.visualizar = visualizar;
    }

    public FragmentManager getFragmentManager() {
        return fragmentManager;
    }

    public void setFragmentManager(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }

    FragmentManager fragmentManager;

    public AdapterProgramaTreino(int rowLayout, Context context, ArrayList<ProgramaTreino> lstProgramaTreino, Aluno aluno, Boolean visualizar, ArrayList<ProgramaTreino> lstProgramaTreinoExcluidos) {
        this.rowLayout = rowLayout;
        this.mContext = context;
        this.lstProgramaTreino = lstProgramaTreino;
        this.contextBase = context;
        this.aluno = aluno;
        this.visualizar = visualizar;
        this.lstProgramaTreinoExcluidos = lstProgramaTreinoExcluidos;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        populaCampos(holder, position);
        iniciaListeners(holder, position);
    }

    private void iniciaListeners(ViewHolder holder, final int position) {
        if(getVisualizar()){
            holder.adicionarPrograma.setVisibility(View.GONE);
            holder.excluirPrograma.setVisibility(View.GONE);
            holder.addExercicios.setText("Exercícios");
            holder.addExercicios.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentDialogListaExerciciosExpansivel fragmentDialog = new FragmentDialogListaExerciciosExpansivel();
                    Bundle b = new Bundle();
                    aluno.setLstProgramaTreino(lstProgramaTreino);
                    b.putSerializable(Constantes.BUNDLE_ALUNO, aluno);
                    b.putSerializable(Constantes.BUNDLE_PROGRAMA_TREINO, lstProgramaTreino.get(position));
                    fragmentDialog.setArguments(b);
                    fragmentDialog.setVisualizar(true);
                    fragmentDialog.show(getFragmentManager(), "lista_exercicios_programa");
                }
            });
        }else {
            holder.addExercicios.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentDialogListaExerciciosExpansivel fragmentDialog = new FragmentDialogListaExerciciosExpansivel();
                    Bundle b = new Bundle();
                    aluno.setLstProgramaTreino(lstProgramaTreino);
                    b.putSerializable(Constantes.BUNDLE_ALUNO, aluno);
                    b.putSerializable(Constantes.BUNDLE_PROGRAMA_TREINO, lstProgramaTreino.get(position));
                    fragmentDialog.setArguments(b);
                    fragmentDialog.show(getFragmentManager(), "lista_exercicios_programa");
                }
            });

            holder.adicionarPrograma.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    lstProgramaTreino.add(new ProgramaTreino());
                    atualizaTela();
                }
            });

            if ((lstProgramaTreino.size() - 1) == position) {
                holder.adicionarPrograma.setVisibility(View.VISIBLE);
            } else {
                holder.adicionarPrograma.setVisibility(View.GONE);
            }

            holder.excluirPrograma.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(lstProgramaTreino.get(position).getId() != null) {
                        lstProgramaTreinoExcluidos.add(lstProgramaTreino.get(position));
                    }
                    lstProgramaTreino.remove(position);
                    mostraToast("Programa excluído!");
                    if (lstProgramaTreino.size() == 0) {
                        ProgramaTreino pt = new ProgramaTreino();
                        pt.setSimples(true);
                        lstProgramaTreino.add(pt);
                    }
                    atualizaTela();
                }
            });
        }
    }

    private void atualizaTela(){
        this.notifyDataSetChanged();
    }

    private void populaCampos(ViewHolder holder, int position) {
        holder.ordemPrograma.setText("P" + String.valueOf(position+1));
        lstProgramaTreino.get(position).setOrdem(position+1);
    }

    @Override
    public int getItemCount() {
        return lstProgramaTreino.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView ordemPrograma;
        public Button addExercicios;
        public Button excluirPrograma;
        public Button adicionarPrograma;


        public ViewHolder(View itemView) {
            super(itemView);

            ordemPrograma = (TextView) itemView.findViewById(R.id.txtOrdemPrograma);
            addExercicios = (Button) itemView.findViewById(R.id.btnAddExercicios);
            excluirPrograma = (Button) itemView.findViewById(R.id.btnExcluir);
            adicionarPrograma = (Button) itemView.findViewById(R.id.btnAdicionarPrograma);
        }

    }

    private void mostraToast(String mensagem){
        Toast t = Toast.makeText(contextBase, mensagem, Toast.LENGTH_LONG);
        t.setGravity(Gravity.CENTER, Gravity.CENTER_HORIZONTAL,Gravity.CENTER_VERTICAL);
        t.show();
    }
}
