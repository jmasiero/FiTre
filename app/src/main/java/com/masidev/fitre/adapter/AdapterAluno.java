package com.masidev.fitre.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.masidev.fitre.R;
import com.masidev.fitre.data.entidade.Aluno;

import java.util.List;

/**
 * Created by jmasiero on 30/09/15.
 */
public class AdapterAluno extends ArrayAdapter<Aluno> {

    Context mContext;
    int layoutResourceId;
    List<Aluno> lsAluno;

    public AdapterAluno(Context context, int resource, List<Aluno> lsAluno) {
        super(context, resource, lsAluno);

        this.layoutResourceId = resource;
        this.mContext = context;
        this.lsAluno = lsAluno;

    }

    public List<Aluno> getLsAluno(){
        return lsAluno;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            convertView = inflater.inflate(layoutResourceId, parent, false);
        }

        Aluno aluno = lsAluno.get(position);

        TextView textViewItem = (TextView) convertView.findViewById(R.id.textViewItem);
        textViewItem.setText(aluno.getNome());
        textViewItem.setTag(aluno.getCPF());

        return convertView;
    }
}

