package com.masidev.fitre.adapter;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.masidev.fitre.R;
import com.masidev.fitre.constante.Constantes;
import com.masidev.fitre.data.entidade.Aluno;
import com.masidev.fitre.fragment.FragmentDialogTestePt3ListaExercicios;

import java.util.ArrayList;

/**
 * Created by jmasiero on 03/11/15.
 */
public class AdapterAlunoEmTeste extends RecyclerView.Adapter<AdapterAlunoEmTeste.ViewHolder> {
    private int rowLayout;
    private Context contextBase;
    private ViewHolder holderBase;
    private ArrayList<Aluno> alunos;
    private FragmentManager fragmentManager;

    public AdapterAlunoEmTeste(int rowLayout, Context context, ArrayList<Aluno> alunos){
        this.rowLayout = rowLayout;
        contextBase = context;
        this.alunos = alunos;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(rowLayout, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holderBase = holder;
        final Aluno aluno = alunos.get(position);
        holder.lblNomeAluno.setText(aluno.getNome());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentDialogTestePt3ListaExercicios dialog = new FragmentDialogTestePt3ListaExercicios();
                dialog.setAlunoSelecionado(aluno);
                dialog.show(getFragmentManager(), String.valueOf(Constantes.FRAGMENT_DIALOG_SELECIONA_ALUNO_PARA_TESTE_PT3));
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                final PopupMenu popupMenu = new PopupMenu(contextBase, v);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.item_cancelar_teste:
                                cancelarTeste(aluno);
                                return true;
                            default:
                                return false;
                        }
                    }
                });

                popupMenu.inflate(R.menu.aluno_em_teste_popup_menu);
                popupMenu.show();
                return false;
            }
        });
    }

    private void cancelarTeste(Aluno aluno) {
        alunos.remove(aluno);
        this.notifyDataSetChanged();

        RecyclerView recyclerView = (RecyclerView) getFragmentManager()
                .findFragmentByTag(Constantes.TAG_FRAGMENT_ALUNOS_EM_TREINAMENTO)
                .getView()
                .findViewById(R.id.listTeste);

        if(alunos.size() == 0){
            recyclerView.setVisibility(View.GONE);
        }
    }

    public ArrayList<Aluno> retornaListaAlunos(){
        return alunos;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return alunos.size();
    }

    public void setFragmentManager(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }

    public FragmentManager getFragmentManager() {
        return fragmentManager;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        TextView lblTempoDecorrido;
        TextView lblNomeAluno;
        ImageView imgObservacao;
        TextView lblValidadeTreino;

        public ViewHolder(View itemView) {
            super(itemView);

            lblValidadeTreino = (TextView) itemView.findViewById(R.id.validadeTreino);
            lblTempoDecorrido = (TextView) itemView.findViewById(R.id.txtTempoDecorrido);
            lblNomeAluno = (TextView) itemView.findViewById(R.id.lblNomeAluno);
            imgObservacao = (ImageView) itemView.findViewById(R.id.imgObservacao);
        }
    }
}
