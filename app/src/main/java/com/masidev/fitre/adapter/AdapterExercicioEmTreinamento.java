package com.masidev.fitre.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.masidev.fitre.R;
import com.masidev.fitre.data.entidade.Musculo;

import java.util.List;

/**
 * Created by jmasiero on 27/10/15.
 */
public class AdapterExercicioEmTreinamento extends ArrayAdapter<Musculo> {

    Context contextBase;
    int layoutResourceId;
    List<Musculo> lsMusculos;
    LinearLayout ll;

    public FragmentManager getFragmentManager() {
        return fragmentManager;
    }

    public void setFragmentManager(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }

    FragmentManager fragmentManager;

    public AdapterExercicioEmTreinamento(Context context, int resource, List<Musculo> lstMusculos) {
        super(context, resource, lstMusculos);

        contextBase = context;
        layoutResourceId = resource;
        this.lsMusculos = lstMusculos;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            LayoutInflater inflater = ((Activity) contextBase).getLayoutInflater();
            convertView = inflater.inflate(layoutResourceId, parent, false);
        }

        final Musculo musculo = lsMusculos.get(position);

        TextView nomeExercicio = (TextView) convertView.findViewById(R.id.nomeExercicio);
        nomeExercicio.setText(musculo.getExercicio());

        TextView series = (TextView) convertView.findViewById(R.id.series);
        series.setText("S: "+musculo.getSeries());

        TextView repeticoes = (TextView) convertView.findViewById(R.id.repeticoes);
        repeticoes.setText("R: " + musculo.getRepeticoes());

        TextView cargas = (TextView) convertView.findViewById(R.id.cargas);
        cargas.setText("C: " + musculo.getCarga());

        LinearLayout ll = (LinearLayout) convertView.findViewById(R.id.fundo_item_listagem);
        if(musculo.jaFez()){
            ll.setBackground(convertView.getContext().getResources().getDrawable(R.drawable.item_listagem_selecionado));
        }else{
            ll.setBackground(convertView.getContext().getResources().getDrawable(R.drawable.item_listagem));
        }
        return convertView;
    }

//    public void itemSelecionado(boolean selecionado) {
//        if(selecionado){
//            ll.setBackground(contextBase.getResources().getDrawable(R.drawable.item_listagem));
//        }else{
//            ll.setBackground(contextBase.getResources().getDrawable(R.drawable.item_listagem));
//        }
//    }
}
