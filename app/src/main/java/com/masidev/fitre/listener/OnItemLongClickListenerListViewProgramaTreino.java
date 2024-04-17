package com.masidev.fitre.listener;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.masidev.fitre.R;
import com.masidev.fitre.activity.Main;
import com.masidev.fitre.adapter.AdapterListaProgramaTreino;
import com.masidev.fitre.constante.Constantes;
import com.masidev.fitre.data.dao.AlunoDAO;
import com.masidev.fitre.data.dao.ProgramaTreinoDAO;
import com.masidev.fitre.data.entidade.Aluno;
import com.masidev.fitre.data.entidade.ProgramaTreino;
import com.masidev.fitre.fragment.FragmentNovoProgramaTreinamento;

/**
 * Created by jmasiero on 12/02/16.
 */
public class OnItemLongClickListenerListViewProgramaTreino implements AdapterView.OnItemLongClickListener {
    private Context context;

    private AdapterListaProgramaTreino adapter;
    private int position;

    public OnItemLongClickListenerListViewProgramaTreino(AdapterListaProgramaTreino t){
        adapter = t;
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

        context = view.getContext();
        this.position = position;

        final PopupMenu popupMenu = new PopupMenu(context, view);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.item_editar:
                        editarProgramaTreino();
                        return true;
                    case R.id.item_excluir:
                        try {
                            exlcuirProgramaTreino();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        return true;
                    default:
                        return false;
                }
            }
        });

        popupMenu.inflate(R.menu.listar_programa_treino_popup_menu);
        popupMenu.show();

        return true;
    }

    private void editarProgramaTreino(){
        ProgramaTreino pt = adapter.getItem(position);

        Aluno a = AlunoDAO.getInstance(context).getAluno(pt.getAluno().getCPF());

        FragmentNovoProgramaTreinamento fragmentNovoProgramaTreinamento = new FragmentNovoProgramaTreinamento();
        fragmentNovoProgramaTreinamento.setAluno(a);
        fragmentNovoProgramaTreinamento.setEditar(true);


        adapter.getFragmentManager().beginTransaction()
                .replace(R.id.container, fragmentNovoProgramaTreinamento, String.valueOf(Constantes.FRAGMENT_PROGRAMA_DE_TREINO))
                .addToBackStack(String.valueOf(Constantes.FRAGMENT_PROGRAMA_DE_TREINO))
                .commit();
    }

    private void exlcuirProgramaTreino() throws Exception {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(context.getString(R.string.deseja_excluir_o_programa_de_treinamento));

        builder.setPositiveButton(context.getString(R.string.Sim), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ProgramaTreino pt = adapter.getItem(position);

                try {
                    if (ProgramaTreinoDAO.getInstance(context).excluirTodosDoAluno(pt.getAluno(), true)) {
                        adapter.remove((ProgramaTreino) adapter.getItem(position));
                        mostraToast("Programa de Treinamento excluído com sucesso!");
                        adapter.getFragmentManager().beginTransaction()
                                .replace(R.id.container, Main.PlaceholderFragment.newInstance(Constantes.FRAGMENT_PROGRAMA_DE_TREINO))
                                .addToBackStack(String.valueOf(Constantes.FRAGMENT_PROGRAMA_DE_TREINO))
                                .commit();
                    } else {
                        mostraToast("Ocorreu um erro ao excluir o Programa de Treinamento!!!");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        builder.setNegativeButton(context.getString(R.string.Nao), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        builder.show();
    }

    private void mostraToast(String mensagem){
        Toast t = Toast.makeText(context, mensagem, Toast.LENGTH_LONG);
        t.setGravity(Gravity.CENTER, Gravity.CENTER_HORIZONTAL, Gravity.CENTER_VERTICAL);
        t.show();
    }

}
