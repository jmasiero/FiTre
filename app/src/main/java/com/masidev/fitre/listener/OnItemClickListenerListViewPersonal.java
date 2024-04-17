package com.masidev.fitre.listener;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.masidev.fitre.R;

/**
 * Created by jmasiero on 30/09/15.
 */
public class OnItemClickListenerListViewPersonal implements AdapterView.OnItemClickListener {

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Context context = view.getContext();

        TextView textViewItem = ((TextView) view.findViewById(R.id.textViewItem));

        //String info = textViewItem.getTag().toString();

        //Toast.makeText(context, info, Toast.LENGTH_LONG).show();
    }
}
