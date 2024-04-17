package com.masidev.fitre.componetes;
import android.view.View;
import android.widget.TextView;

import com.masidev.fitre.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by jmasiero on 09/11/15.
 * pagina http://www.guj.com.br/java/271494-resolvido-timer-regressivo-como-implementar
 */
public class Cronometro {
    private Timer cronometro;
    private DateFormat formato = new SimpleDateFormat("HH:mm:ss");
    private Calendar calendario = Calendar.getInstance();
    private final byte contagem;
    public static final byte PROGRESSIVA = 1;
    public static final byte REGRESSIVA = -1;

    public Cronometro(int ano, int mes, int dia, int horas, int minutos, int segundos, byte tipoContagem) {
        this.cronometro = new Timer();
        calendario.set(ano, mes, dia, horas, minutos, segundos);
        this.contagem = tipoContagem;
    }

    public void cronometro(final View view) {
        TimerTask tarefa = new TimerTask() {

            @Override
            public void run() {
                TextView tempo = (TextView) view.findViewById(R.id.txtTempoDecorrido);
                tempo.setText(getTime().toString());
            }
        };
        cronometro.scheduleAtFixedRate(tarefa, 0, 1000);
        this.cronometro = null;
    }

    public String getTime() {
        calendario.add(Calendar.SECOND, contagem);
        return formato.format(calendario.getTime());
    }

}
