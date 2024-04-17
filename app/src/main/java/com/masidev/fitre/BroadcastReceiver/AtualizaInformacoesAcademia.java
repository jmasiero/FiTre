package com.masidev.fitre.BroadcastReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.masidev.fitre.activity.Login;
import com.masidev.fitre.constante.Constantes;
import com.masidev.fitre.data.dao.AcademiaDAO;
import com.masidev.fitre.data.entidade.Academia;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by jmasiero on 05/04/16.
 */
public class AtualizaInformacoesAcademia extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        AcademiaDAO.getInstance(context).buscaDadosAcademiaServidor(context);
        deslogaUsuarioInadimplente(context);
    }

    private void deslogaUsuarioInadimplente(Context context) {
        SharedPreferences sp = context.getSharedPreferences(Constantes.SHARED_PREFERENCES_LOGIN, Context.MODE_PRIVATE);
        String cnpj = sp.getString(Constantes.CNPJ_ACADEMIA_LOGADA, "");
        Integer jaAvisou = sp.getInt(Constantes.AVISOU_SOBRE_PAGAMENTO_DIRECIONOU_PARA_LOGIN, 0);
        Academia academia = AcademiaDAO.getInstance(context).retornaAcademiaPorCnpj(cnpj);

        if(!academia.getPagou() && jaAvisou == 0){
            deslogaUsuario(sp, context);
        }

        if(pagamentoVencido(academia.getDtUltimoPagamento()) && jaAvisou == 0){
            academia.setPagou(false);
            AcademiaDAO.getInstance(context).atualizarAcademia(academia);
            deslogaUsuario(sp, context);
        }
    }

    private void deslogaUsuario(SharedPreferences sp, Context context) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(Constantes.AVISOU_SOBRE_PAGAMENTO_DIRECIONOU_PARA_LOGIN, Constantes.BANCO_TRUE);
        editor.commit();

        Intent i = new Intent(context, Login.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
        Toast.makeText(context, "Problema com a mensalidade do sistema, entre em contato com o suporte!", Toast.LENGTH_SHORT).show();
    }

    private boolean pagamentoVencido(String dtUltimoPagamento) {
        boolean retorno = false;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-d");
        String dataAtual = sdf.format(System.currentTimeMillis());

        Calendar data1 = Calendar.getInstance();
        Calendar data2 = Calendar.getInstance();

        try {
            data1.setTime(sdf.parse(dataAtual));
            data2.setTime(sdf.parse(dtUltimoPagamento));
        } catch (java.text.ParseException e ) {}
        int dias = data2.get(Calendar.DAY_OF_YEAR) - data1.get(Calendar.DAY_OF_YEAR);

        if(dias > 40){
            retorno = true;
        }

        return retorno;
    }

}
