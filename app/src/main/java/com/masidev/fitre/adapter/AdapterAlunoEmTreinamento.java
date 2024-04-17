package com.masidev.fitre.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.masidev.fitre.R;
import com.masidev.fitre.constante.Constantes;
import com.masidev.fitre.data.dao.FrequenciaDAO;
import com.masidev.fitre.data.entidade.Aluno;
import com.masidev.fitre.data.entidade.Frequencia;
import com.masidev.fitre.fragment.FragmentDialogListaExercicio;
import com.masidev.fitre.fragment.FragmentTreinamentoMenuExercicio;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by jmasiero on 03/11/15.
 */
public class AdapterAlunoEmTreinamento extends RecyclerView.Adapter<AdapterAlunoEmTreinamento.ViewHolder> {
    private int rowLayout;
    private Context contextBase;
    private ViewHolder holderBase;
    private ArrayList<Aluno> alunos;
    private FragmentManager fragmentManager;

    public AdapterAlunoEmTreinamento(int rowLayout, Context context, ArrayList<Aluno> alunos){
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
        String dataAtual;
        String[] arrDataAtual;
        String[] arrDataFim;
        final Aluno aluno = alunos.get(position);
        holder.lblNomeAluno.setText(aluno.getNome());

        if(aluno.getProgramaSelecionado() == null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-d");
            dataAtual = sdf.format(System.currentTimeMillis());
            arrDataAtual = dataAtual.split("-");
            arrDataFim = aluno.getTreino().getDataFim().split("-");
            //txtDataInicio.setText(dtInicio[2]+ "/" + dtInicio[1] + "/" + dtInicio[0]);

            if (arrDataAtual[1].equals(arrDataFim[1]) && arrDataAtual[0].equals(arrDataFim[0])) {
                holder.lblValidadeTreino.setText("V: " + arrDataAtual[1] + "/" + arrDataAtual[0]);
            }
        }

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                final PopupMenu popupMenu = new PopupMenu(contextBase, v);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.item_observacao:
                                mostraDialogComObservação(aluno);
                                return true;
                            case R.id.lista_exercicio:
                                mostraListaExercicio(aluno);
                                return true;
                            case R.id.item_exercicio:
                                mostraExercicio(aluno);
                                return true;
                            case R.id.item_frequencia:
                                mostraFrequencia(aluno);
                                return true;
                            case R.id.item_finalizar:
                                alunos.remove(position);
                                atualizarPagina();
                                return true;
                            default:
                                return false;
                        }
                    }
                });

                if (!aluno.getObservacao().trim().equals("")) {
                    popupMenu.inflate(R.menu.aluno_em_treinamento_popup_menu_com_observacao);
                } else {
                    popupMenu.inflate(R.menu.aluno_em_treinamento_popup_menu);
                }

                popupMenu.show();

                return false;
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isJanela()) {
                    mostraExercicio(aluno);
                } else {
                    mostraListaExercicio(aluno);
                }
            }
        });

        if(!aluno.getObservacao().trim().equals("")){
            holder.imgObservacao.setVisibility(View.VISIBLE);
        }

    }

    private void mostraDialogComObservação(Aluno aluno) {
        AlertDialog.Builder builder = new AlertDialog.Builder(contextBase);
        builder.setTitle("Observação!")
                .setMessage(aluno.getObservacao())
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        builder.show();
    }

    private boolean isJanela(){
        Boolean janela = true;

        SharedPreferences sp1 = contextBase.getSharedPreferences(Constantes.SHARED_PREFERENCES_CONFIGURACOES, Context.MODE_PRIVATE);

        String modo = sp1.getString(Constantes.SHARED_PREFERENCES_VISUALIZACAO_TREINAMENTO, Constantes.VISUALIZACAO_TREINAMENTO_JANELA);

        if(modo.equals(Constantes.VISUALIZACAO_TREINAMENTO_LISTA)){
            janela = false;
        }
        return janela;
    }

    private void mostraListaExercicio(Aluno aluno){
        FragmentDialogListaExercicio dialog = new FragmentDialogListaExercicio();
        dialog.setAluno(aluno);

        if(Constantes.TREINO_SIMPLES == aluno.getTipoTreino()){
            dialog.setTipoTreino(Constantes.TREINO_SIMPLES);
        }else{
            dialog.setTipoTreino(Constantes.TREINO_MISTO);
        }

        dialog.show(getFragmentManager(), "dialog_lista_exercicio");
    }

    private void mostraExercicio(Aluno aluno){
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTreinamentoMenuExercicio fragmentTreinamentoMenuExercicio = new FragmentTreinamentoMenuExercicio();
        fragmentTreinamentoMenuExercicio.setAluno(aluno);

        if(Constantes.TREINO_SIMPLES == aluno.getTipoTreino()){
            fragmentTreinamentoMenuExercicio.setTipoTreino(Constantes.TREINO_SIMPLES);
        }else{
            fragmentTreinamentoMenuExercicio.setTipoTreino(Constantes.TREINO_MISTO);
        }

        fragmentTreinamentoMenuExercicio.setLstAlunos(alunos);
        fragmentTreinamentoMenuExercicio.setAdapterAlunoEmTreinamento(this);
        //Apaga fragment anterior da tela para não haver sobreposição
        getFragmentManager().popBackStack(Constantes.TAG_FRAGMENT_MENU_EXERCICIO, FragmentManager.POP_BACK_STACK_INCLUSIVE);

        fragmentManager.beginTransaction()
                .add(R.id.container, fragmentTreinamentoMenuExercicio)
                .addToBackStack(Constantes.TAG_FRAGMENT_MENU_EXERCICIO)
                .commit();
    }

    private void mostraFrequencia(Aluno aluno){
        AlertDialog.Builder builder = new AlertDialog.Builder(contextBase);
        //ArrayList<Frequencia> arrFrequencia = FrequenciaDAO.getInstance(contextBase).listar(aluno);
        ArrayList<Frequencia> arrFrequencia = FrequenciaDAO.getInstance(contextBase).listarUltimasDez(aluno);
        StringBuilder sb = new StringBuilder();

        for (Frequencia f : arrFrequencia){
            String[] arrDataEHora = f.getData().split(" ");
            String[] arrData = arrDataEHora[0].split("-");

            if (arrDataEHora.length > 1 ){
                sb.append(arrData[2]+"/"+arrData[1]+"/"+arrData[0] + " " + arrDataEHora[1]);
            }else{
                sb.append(arrData[2]+"/"+arrData[1]+"/"+arrData[0] + " ");
            }

            if(f.getLstOrdemProgramaTreino().size() > 0){
                for(String s : f.getLstOrdemProgramaTreino()){
                    sb.append(" - " + s);
                }
            }

            if(f.getLstMusculosTreinados().size() > 0){
                for(String s : f.getLstMusculosTreinados()){
                    sb.append(" - " + Constantes.ARRAY_TIPO_MUSCULOS[Integer.parseInt(s)]);
                }
            }

            sb.append("\n");
        }

        builder.setTitle("Frequência!")
                .setMessage(sb.toString())
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        builder.show();
    }

    private void atualizarPagina(){
        //Apaga fragment anterior da tela para não haver sobreposição
        getFragmentManager().popBackStack(Constantes.TAG_FRAGMENT_MENU_EXERCICIO, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        this.notifyDataSetChanged();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return alunos.size();
    }

    public void setFragmentManager(android.support.v4.app.FragmentManager fragmentManager) {
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
