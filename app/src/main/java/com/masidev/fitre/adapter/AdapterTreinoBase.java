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
import com.masidev.fitre.data.entidade.TreinoBase;

import java.util.List;

/**
 * Created by jmasiero on 07/03/16.
 */
public class AdapterTreinoBase extends ArrayAdapter<TreinoBase> {

    Context contextBase;
    int layoutResourceId;
    List<TreinoBase> lsTreinoBase;
    TreinoBase treinoBase;
    FragmentManager fragmentManager;

    public FragmentManager getFragmentManager() {
        return fragmentManager;
    }

    public void setFragmentManager(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }

    public AdapterTreinoBase(Context context, int resource, List<TreinoBase> lsTreinoBase) {
        super(context, resource, lsTreinoBase);

        contextBase = context;
        layoutResourceId = resource;
        this.lsTreinoBase = lsTreinoBase;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            LayoutInflater inflater = ((Activity) contextBase).getLayoutInflater();
            convertView = inflater.inflate(layoutResourceId, parent, false);
        }

        treinoBase = lsTreinoBase.get(position);

        TextView textViewItem = (TextView) convertView.findViewById(R.id.textViewItem);
        textViewItem.setText(treinoBase.getTipoTreino());
        textViewItem.setTag(treinoBase.getId());

        return convertView;
    }
}
