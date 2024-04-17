package com.masidev.fitre.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.masidev.fitre.R;
import com.masidev.fitre.data.dao.AlunoDAO;
import com.masidev.fitre.data.entidade.Aluno;
import com.masidev.fitre.data.entidade.Treino;

import java.util.List;

/**
 * Created by jmasiero on 27/10/15.
 */
public class AdapterTreino extends ArrayAdapter<Treino> {

    Context contextBase;
    int layoutResourceId;
    List<Treino> lsTreino;
    Treino treino;

    public FragmentManager getFragmentManager() {
        return fragmentManager;
    }

    public void setFragmentManager(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }

    FragmentManager fragmentManager;

    public AdapterTreino(Context context, int resource, List<Treino> lsTreino) {
        super(context, resource, lsTreino);

        contextBase = context;
        layoutResourceId = resource;
        this.lsTreino = lsTreino;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            LayoutInflater inflater = ((Activity) contextBase).getLayoutInflater();
            convertView = inflater.inflate(layoutResourceId, parent, false);
        }

        treino = lsTreino.get(position);

        Aluno aluno = AlunoDAO.getInstance(contextBase).getAluno(treino.getAluno().getCPF());

        TextView textViewItem = (TextView) convertView.findViewById(R.id.textViewItem);

        String[] arrNome = aluno.getNome().split(" ");
        if(arrNome.length > 1) {
            textViewItem.setText(arrNome[0] + " " + arrNome[arrNome.length - 1] + " -> " + treino.getTipoTreino());
        }else{
            textViewItem.setText(arrNome[0] + " -> " + treino.getTipoTreino());
        }
        textViewItem.setTag(treino.getId());

        TextView validade = (TextView) convertView.findViewById(R.id.validadeTreino);
        String[] arrData = treino.getDataFim().split("-");
        validade.setText("V: "+arrData[1]+"/"+arrData[0]);

        return convertView;
    }
}
