package com.masidev.fitre.listener;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.masidev.fitre.R;
import com.masidev.fitre.activity.Main;
import com.masidev.fitre.adapter.AdapterTreinoBase;
import com.masidev.fitre.constante.Constantes;
import com.masidev.fitre.data.dao.TreinoBaseDAO;
import com.masidev.fitre.data.entidade.TreinoBase;
import com.masidev.fitre.fragment.FragmentNovoTreinoPt2;

/**
 * Created by jmasiero on 07/03/16.
 */
public class OnItemLongClickListenerListViewTreinoBase implements AdapterView.OnItemLongClickListener {
    private Context context;

    private AdapterTreinoBase adapter;
    private int position;

    public OnItemLongClickListenerListViewTreinoBase(AdapterTreinoBase a){
        adapter = a;
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

        popupMenu.inflate(R.menu.listar_treino_base_popup_menu);
        popupMenu.show();


        return true;
    }

    private void editarTreino(){
        TreinoBase tb = adapter.getItem(position);
        tb.setAlterar(true);

        FragmentNovoTreinoPt2 fragmentNovoTreinoPt2 = new FragmentNovoTreinoPt2();

        Bundle args = new Bundle();

        fragmentNovoTreinoPt2.setArguments(args);
        fragmentNovoTreinoPt2.setTreinoBase(tb);
        fragmentNovoTreinoPt2.setBase(true);

        adapter.getFragmentManager().beginTransaction()
                .replace(R.id.container, fragmentNovoTreinoPt2, String.valueOf(Constantes.FRAGMENT_TRANSACAO_NOVO_TREINO_PT1_PT2))
                .addToBackStack(String.valueOf(Constantes.FRAGMENT_TRANSACAO_NOVO_TREINO_PT1_PT2))
                .commit();

    }

    private void exlcuirTreino() throws Exception {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Deseja Excluir o Treinamento Base?");

        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                TreinoBase tb = adapter.getItem(position);


                try {
                    if (TreinoBaseDAO.getInstance(context).excluir(tb, true)) {
                        adapter.remove((TreinoBase) adapter.getItem(position));
                        Toast.makeText(context, "Treinamento Base excluído com sucesso!", Toast.LENGTH_SHORT).show();

                        adapter.getFragmentManager().beginTransaction()
                                .replace(R.id.container, Main.PlaceholderFragment.newInstance(Constantes.FRAGMENT_TREINO_BASE))
                                .addToBackStack(String.valueOf(Constantes.FRAGMENT_TREINO_BASE))
                                .commit();
                    } else {
                        Toast.makeText(context, "Ocorreu um erro ao excluir o Treinamento!!!", Toast.LENGTH_SHORT).show();
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