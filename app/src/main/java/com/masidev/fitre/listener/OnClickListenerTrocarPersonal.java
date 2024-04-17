package com.masidev.fitre.listener;

import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.TextView;

import com.masidev.fitre.constante.Constantes;
import com.masidev.fitre.fragment.FragmentDialogTrocarPersonal;

/**
 * Created by jmasiero on 23/05/16.
 */
public class OnClickListenerTrocarPersonal implements View.OnClickListener{
    FragmentManager fm;
    TextView headerView;

    public OnClickListenerTrocarPersonal(FragmentManager fm, TextView headerView) {
        this.fm = fm;
        this.headerView = headerView;
    }

    @Override
    public void onClick(View v) {
        FragmentDialogTrocarPersonal fragTrocarPersonal = new FragmentDialogTrocarPersonal();
        fragTrocarPersonal.setHeaderView(headerView);
        fragTrocarPersonal.show(fm, Constantes.TROCAR_PERSONAL);
    }

}
