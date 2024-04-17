package com.masidev.fitre.BroadcastReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by jmasiero on 05/04/16.
 */
public class EnviaAtualizacoesParaServidor extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        new MetodosEnviaAtualizacoes().enviaChamadaParaServidor(context);
    }

}
