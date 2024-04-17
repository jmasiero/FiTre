package com.masidev.fitre.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.masidev.fitre.R;

/**
 * Created by jmasiero on 04/03/16.
 */
public class FragmentTreinoBase extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lista_treino_base, container, false);

        //iniciaListeners(view);
        return view;
    }
}
