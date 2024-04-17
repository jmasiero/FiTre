package com.masidev.fitre.listener;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.masidev.fitre.R;
import com.masidev.fitre.adapter.AdapterAluno;
import com.masidev.fitre.constante.Constantes;
import com.masidev.fitre.data.dao.AlunoDAO;
import com.masidev.fitre.data.dao.FrequenciaDAO;
import com.masidev.fitre.data.entidade.Aluno;
import com.masidev.fitre.data.entidade.Frequencia;
import com.masidev.fitre.fragment.FragmentNovoAluno;

import java.util.ArrayList;

/**
 * Created by jmasiero on 30/09/15.
 */
public class OnItemClickListenerListViewAluno implements AdapterView.OnItemClickListener {
    Context context;
    AdapterAluno adapter;
    FragmentManager fragmentManager;
    TextView txtQtdAlunos;
    //TextView txtQtdAlunos = (TextView) view.findViewById(R.id.txtQtdAlunos);

    public OnItemClickListenerListViewAluno(AdapterAluno a, FragmentManager fragmentManager, TextView txtQtdAlunos){
        adapter = a;
        this.fragmentManager = fragmentManager;
        this.txtQtdAlunos = txtQtdAlunos;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
        context = view.getContext();

        TextView textViewItem = ((TextView) view.findViewById(R.id.textViewItem));

        final String Cpf = textViewItem.getTag().toString();

        final PopupMenu popupMenu = new PopupMenu(context, view);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.item_frequencia:
                        mostraFrequencia(Cpf);

                        return true;
                    case R.id.item_editar:
                        Bundle args = new Bundle();
                        args.putSerializable("aluno", adapter.getItem(position));
                        FragmentNovoAluno fragNovoAluno = new FragmentNovoAluno();
                        fragNovoAluno.setArguments(args);

                        fragmentManager.beginTransaction()
                                .replace(R.id.container, fragNovoAluno, String.valueOf(Constantes.FRAGMENT_ALTERAR_ALUNO))
                                .addToBackStack(String.valueOf(Constantes.FRAGMENT_ALTERAR_ALUNO))
                                .commit();

                        return true;
                    case R.id.item_excluir:
                        try {
                            exlcuirAluno(Cpf, position);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        return true;
                    default:
                        return false;
                }
            }
        });

        popupMenu.inflate(R.menu.listar_aluno_popup_menu);
        popupMenu.show();

    }

    private void mostraFrequencia(String cpf){
        Aluno a = new Aluno(cpf);
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
        ArrayList<Frequencia> arrFrequencia = FrequenciaDAO.getInstance(context).listar(a);
        StringBuilder sb = new StringBuilder();
        if(arrFrequencia.size() == 0){
            sb.append("O aluno ainda não treinou!");
        }
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

    private void exlcuirAluno(final String Cpf, final int position) throws Exception {
        boolean retorno = false;

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(context.getString(R.string.Deseja_excluir_o_aluno));

        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Aluno a = new Aluno();
                a.setCPF(Cpf);
                try {
                    if (AlunoDAO.getInstance(context).excluir(a, true)) {
                        adapter.remove((Aluno) adapter.getItem(position));
                        txtQtdAlunos.setText(context.getString(R.string.qtd) + adapter.getLsAluno().size());
                        mostraToast(context.getString(R.string.Aluno_excluido_com_sucesso));
                    } else {
                        mostraToast(context.getString(R.string.Ocorreu_um_erro_ao_excluir_o_Aluno));
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

    private void mostraToast(String mensagem){
        Toast t = Toast.makeText(context, mensagem, Toast.LENGTH_LONG);
        t.setGravity(Gravity.CENTER, Gravity.CENTER_HORIZONTAL, Gravity.CENTER_VERTICAL);
        t.show();
    }
}
