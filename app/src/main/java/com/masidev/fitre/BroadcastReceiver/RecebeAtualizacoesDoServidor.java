package com.masidev.fitre.BroadcastReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.masidev.fitre.constante.Constantes;

/**
 * Created by jmasiero on 05/04/16.
 */
public class RecebeAtualizacoesDoServidor extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, Intent intent) {
       new Thread(new Runnable() {
            public void run() {
                new MetodosRecebeAtualizacoes().enviaChamadaParaServidor(context, Constantes.INSERT);
                new MetodosRecebeAtualizacoes().enviaChamadaParaServidor(context, Constantes.DELETE);
                new MetodosRecebeAtualizacoes().enviaChamadaParaServidor(context, Constantes.UPDATE);
            }
        }).start();
    }
}
