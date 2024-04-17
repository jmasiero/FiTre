package com.masidev.fitre.listener;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.masidev.fitre.R;
import com.masidev.fitre.activity.Main;
import com.masidev.fitre.adapter.AdapterTreino;
import com.masidev.fitre.constante.Constantes;
import com.masidev.fitre.data.dao.TreinoProgressivoDAO;
import com.masidev.fitre.data.entidade.Treino;
import com.masidev.fitre.fragment.FragmentNovoTreinoProgressivo;

/**
 * Created by jmasiero on 12/02/16.
 */
public class OnItemLongClickListenerListViewTreinoProgressivo implements AdapterView.OnItemLongClickListener {
    private Context context;

    private AdapterTreino adapter;
    private int position;

    public OnItemLongClickListenerListViewTreinoProgressivo(AdapterTreino t){
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
                        editarTreino();
                        return true;
                    case R.id.item_excluir:
                        try {
                            exlcuirTreino();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        return true;
                    default:
                        return false;
                }
            }
        });

        popupMenu.inflate(R.menu.listar_treino_progressivo_popup_menu);
        popupMenu.show();

        return true;
    }

    private void editarTreino(){
        Treino t = adapter.getItem(position);

        FragmentNovoTreinoProgressivo fragmentNovoTreinoProgressivo = new FragmentNovoTreinoProgressivo();
        fragmentNovoTreinoProgressivo.setTreino(t);
        fragmentNovoTreinoProgressivo.setEditar(true);

        adapter.getFragmentManager().beginTransaction()
                .replace(R.id.container, fragmentNovoTreinoProgressivo, String.valueOf(Constantes.FRAGMENT_TREINO_PROGRESSIVO))
                .addToBackStack(String.valueOf(Constantes.FRAGMENT_TREINO_PROGRESSIVO))
                .commit();
    }

    private void exlcuirTreino() throws Exception {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Deseja excluir o Treinamento Progressivo?");

        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Treino t = adapter.getItem(position);

                try {
                    if (TreinoProgressivoDAO.getInstance(context).excluirTodosDoTreino(t, true)) {
                        adapter.remove((Treino) adapter.getItem(position));
                        Toast.makeText(context, "Treinamento Progressivo excluído com sucesso!", Toast.LENGTH_SHORT).show();

                        adapter.getFragmentManager().beginTransaction()
                                .replace(R.id.container, Main.PlaceholderFragment.newInstance(Constantes.FRAGMENT_TREINO_PROGRESSIVO))
                                .addToBackStack(String.valueOf(Constantes.FRAGMENT_TREINO_PROGRESSIVO))
                                .commit();
                    } else {
                        Toast.makeText(context, "Ocorreu um erro ao excluir o Treinamento Progressivo!!!", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        builder.show();
    }

}
