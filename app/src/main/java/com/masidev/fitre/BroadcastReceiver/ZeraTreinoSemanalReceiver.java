package com.masidev.fitre.BroadcastReceiver;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.masidev.fitre.constante.Constantes;
import com.masidev.fitre.data.dao.MusculoTreinadoSemanaDAO;
import com.masidev.fitre.data.dao.ProgramaTreinoDAO;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by jmasiero on 22/02/16.
 */
public class ZeraTreinoSemanalReceiver extends BroadcastReceiver {
    private NotificationManager mManager;

    public ZeraTreinoSemanalReceiver(){

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            verificaSeZerouOsTreinos(context);
        } catch (Exception e) {
            e.printStackTrace();
        }

//        mManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
//        Intent intent1 = new Intent(context, Login.class);
//
//        Notification notification = new Notification(R.drawable.ic_drawer,"This is a test message!", System.currentTimeMillis());
//        intent1.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP| Intent.FLAG_ACTIVITY_CLEAR_TOP);
//
//        PendingIntent pendingNotificationIntent = PendingIntent.getActivity( context,0, intent1,PendingIntent.FLAG_UPDATE_CURRENT);
//        notification.flags |= Notification.FLAG_AUTO_CANCEL;
//        notification.setLatestEventInfo(context, "AlarmManagerDemo", "This is a test message!", pendingNotificationIntent);
//        mManager.notify(0, notification);
    }

    private boolean verificaSeZerouOsTreinos(Context context) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        //Date hoje = sdf.parse("22/02/2016");
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(new Date(System.currentTimeMillis()));
        int diaDaSemana = gc.get(GregorianCalendar.DAY_OF_WEEK);
        boolean zerado = false;
        SharedPreferences sp1 = context.getSharedPreferences(Constantes.SHARED_PREFERENCES_STATUS_TREINO_SEMANAL, Context.MODE_PRIVATE);

        zerado = sp1.getBoolean(Constantes.SHARED_PREFERENCES_TREINO_SEMANAL_ZERADO, false);

        if(diaDaSemana == 2 && !zerado){
            zerado = zerarTreinoSemanal(context);
        }else if(diaDaSemana != 2 ){
            SharedPreferences.Editor editor = sp1.edit();
            zerado = false;
            editor.putBoolean(Constantes.SHARED_PREFERENCES_TREINO_SEMANAL_ZERADO, false);
            editor.commit();
        }

        return zerado;
    }

    private boolean zerarTreinoSemanal(Context context) throws Exception {
        ProgramaTreinoDAO.getInstance(context).limpaHistoricoSemana();
        Boolean zerado = MusculoTreinadoSemanaDAO.getInstance(context).desmarcarTodosMusculos();

        SharedPreferences sp1 = context.getSharedPreferences(Constantes.SHARED_PREFERENCES_STATUS_TREINO_SEMANAL, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp1.edit();
        editor.putBoolean(Constantes.SHARED_PREFERENCES_TREINO_SEMANAL_ZERADO, zerado);
        editor.commit();

        return zerado;
    }
}
