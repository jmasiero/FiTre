package com.masidev.fitre.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.masidev.fitre.R;
import com.masidev.fitre.data.entidade.Personal;

import java.util.List;

/**
 * Created by jmasiero on 30/09/15.
 */
public class AdapterPersonal extends ArrayAdapter<Personal> {

    Context mContext;
    int layoutResourceId;
    List<Personal> lsPersonal;

    public AdapterPersonal(Context context, int resource, List<Personal> lsPersonal) {
        super(context, resource, lsPersonal);

        this.layoutResourceId = resource;
        this.mContext = context;
        this.lsPersonal = lsPersonal;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            convertView = inflater.inflate(layoutResourceId, parent, false);
        }

        Personal personal = lsPersonal.get(position);

        TextView textViewItem = (TextView) convertView.findViewById(R.id.textViewItem);
        textViewItem.setText(personal.getNome());
        textViewItem.setTag(personal.getCPF());

        return convertView;
    }
}
