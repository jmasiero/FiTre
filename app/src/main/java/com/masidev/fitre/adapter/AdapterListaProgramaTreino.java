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
import com.masidev.fitre.data.entidade.ProgramaTreino;

import java.util.List;

/**
 * Created by jmasiero on 27/10/15.
 */
public class AdapterListaProgramaTreino extends ArrayAdapter<ProgramaTreino> {

    Context contextBase;
    int layoutResourceId;
    List<ProgramaTreino> lsProgramaTreino;
    ProgramaTreino programaTreino;

    public FragmentManager getFragmentManager() {
        return fragmentManager;
    }

    public void setFragmentManager(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }

    FragmentManager fragmentManager;

    public AdapterListaProgramaTreino(Context context, int resource, List<ProgramaTreino> lsProgramaTreino) {
        super(context, resource, lsProgramaTreino);

        contextBase = context;
        layoutResourceId = resource;
        this.lsProgramaTreino = lsProgramaTreino;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            LayoutInflater inflater = ((Activity) contextBase).getLayoutInflater();
            convertView = inflater.inflate(layoutResourceId, parent, false);
        }

        programaTreino = lsProgramaTreino.get(position);

        Aluno aluno = AlunoDAO.getInstance(contextBase).getAluno(programaTreino.getAluno().getCPF());

        String[] arrNome = aluno.getNome().split(" ");

        TextView textViewItem = (TextView) convertView.findViewById(R.id.textViewItem);
        textViewItem.setText(arrNome[0] + " " + arrNome[arrNome.length -1] + " -> " + programaTreino.getNomePrograma());
        textViewItem.setTag(aluno.getCPF());

        return convertView;
    }
}
