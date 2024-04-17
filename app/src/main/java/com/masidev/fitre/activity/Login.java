package com.masidev.fitre.activity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.masidev.fitre.BroadcastReceiver.AtualizaInformacoesAcademia;
import com.masidev.fitre.BroadcastReceiver.EnviaAtualizacoesParaServidor;
import com.masidev.fitre.BroadcastReceiver.RecebeAtualizacoesDoServidor;
import com.masidev.fitre.BroadcastReceiver.ZeraTreinoSemanalReceiver;
import com.masidev.fitre.R;
import com.masidev.fitre.constante.Constantes;
import com.masidev.fitre.data.contract.FrequenciaContract;
import com.masidev.fitre.data.dao.AcademiaDAO;
import com.masidev.fitre.data.dao.PersonalDAO;
import com.masidev.fitre.data.dao.TabelaDAO;
import com.masidev.fitre.data.entidade.Academia;
import com.masidev.fitre.data.entidade.Personal;
import com.masidev.fitre.mascara.Mask;

import java.util.List;

public class Login extends AppCompatActivity {
    private Button btnAtualizarAcademias;
    private Spinner spnAcademia;
    private ArrayAdapter<Academia> adapterSpnAcademia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        criaListeners();
        preencheSpinner();
        preencheVersao();
        preencheTabelaComNomesDeTabelas();
        iniciaMascara();
    }

    private void preencheVersao() {
        TextView txtVersao = (TextView) findViewById(R.id.txtVersaoSistema);
        txtVersao.setText("Versão "+Constantes.VERSAO);
    }

    public void criaListeners(){
        btnAtualizarAcademias = (Button) findViewById(R.id.btnAtualizarAcademia);
        btnAtualizarAcademias.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                atualizaComboAcademia();
            }
        });
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        moveTaskToBack(false);
    }

    public void preencheSpinner(){
        int posicao = 0;
        spnAcademia = (Spinner) findViewById(R.id.spnAcademia);
        SharedPreferences sp = getSharedPreferences(Constantes.SHARED_PREFERENCES_LOGIN, Context.MODE_PRIVATE);

        String cnpj = sp.getString(Constantes.CNPJ_ACADEMIA_LOGADA, "");

        List<Academia> lstAcademia = AcademiaDAO.getInstance(getApplicationContext()).listar(true);

        if(!cnpj.isEmpty()){
            for ( int i = 0; i < lstAcademia.size() ; i++) {
                if(lstAcademia.get(i).getCnpj().equals(cnpj)){
                    posicao = i;
                    break;
                }
            }
        }

        adapterSpnAcademia = new ArrayAdapter<>(getBaseContext(), android.R.layout.simple_spinner_dropdown_item, lstAcademia);
        spnAcademia.setAdapter(adapterSpnAcademia);
        spnAcademia.setSelection(posicao);

    }

    public void preencheTabelaComNomesDeTabelas(){
        try {
            TabelaDAO.getInstance(getApplicationContext()).salvarTabelas();
            TabelaDAO.getInstance(getApplicationContext()).atualizaNovaTabela(FrequenciaContract.TABLE_NAME, "Integer");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void validaLogin(View v){

        EditText edtCpf = (EditText) this.findViewById(R.id.edtCPF);
        EditText edtSenha = (EditText) this.findViewById(R.id.edtSenha);

        Academia acSelecionada = (Academia) spnAcademia.getSelectedItem();


        Personal p = new Personal();
        p.setCPF(Mask.unmask(edtCpf.getText().toString()));
        p.setSenha(edtSenha.getText().toString());
        ativaAlarmDoBroadcast();
        //p.setCPF("73049042168");
        //p.setSenha("asdf1234");
        p.setAcademia(acSelecionada);

        if(spnAcademia.getSelectedItemPosition() > 0) {
            if (AcademiaDAO.getInstance(getApplicationContext()).retornaAcademiaPorCnpj(((Academia) spnAcademia.getSelectedItem()).getCnpj()).getPagou()) {
                Personal retorno = PersonalDAO.getInstance(getApplicationContext()).validar(p);

                if (retorno != null) {
                    Intent i = new Intent(this, Main.class);

                    SharedPreferences sp = getSharedPreferences(Constantes.SHARED_PREFERENCES_LOGIN, MODE_PRIVATE);
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString(Constantes.NOME_ACADEMIA_LOGADA, ((Academia) spnAcademia.getSelectedItem()).getNomeFantasia());
                    editor.putString(Constantes.CNPJ_ACADEMIA_LOGADA, ((Academia) spnAcademia.getSelectedItem()).getCnpj());

                    editor.putString(Constantes.NOME_TREINADOR_LOGADO, retorno.getNome());
                    editor.putString(Constantes.CPF_TREINADOR_LOGADO, retorno.getCPF());
                    editor.putInt(Constantes.AVISOU_SOBRE_PAGAMENTO_DIRECIONOU_PARA_LOGIN, Constantes.BANCO_FALSE);

                    editor.commit();

                    ativaAlarmDoBroadcastAtualizaAcademia();
                    ativaAlarmDoBroadcastRecebeDadosServidor();

                    startActivity(i);
                } else {
                    Toast.makeText(getApplicationContext(), "CPF ou Senha incorreto!!!", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getApplicationContext(), "Problema com a mensalidade do sistema, entre em contato com o suporte!", Toast.LENGTH_SHORT).show();
                AcademiaDAO.getInstance(getApplicationContext()).buscaDadosAcademiaServidor(getApplicationContext());
            }
        }else{
            Toast.makeText(getApplicationContext(), "ERRO! Selecione uma academia", Toast.LENGTH_SHORT).show();
        }
    }

    public void atualizaComboAcademia(){
        AcademiaDAO.getInstance(getApplicationContext()).atualizarListaDeAcademias();
        preencheSpinner();
    }

    protected void iniciaMascara(){
        EditText edtCPF = (EditText) this.findViewById(R.id.edtCPF);
        edtCPF.addTextChangedListener(Mask.insert("###.###.###-##", edtCPF));
    }

    private void ativaAlarmDoBroadcast(){
        long seisHoras = 6 * 60 * 60 * 1000;
        Intent intent = new Intent(this, ZeraTreinoSemanalReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                this.getApplicationContext(), 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
                System.currentTimeMillis(),
                seisHoras,
                pendingIntent);
    }

    private void ativaAlarmDoBroadcastAtualizaAcademia(){
        long tempo = 60000 * 5;
        Intent intent = new Intent(this, AtualizaInformacoesAcademia.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                this.getApplicationContext(), 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
                System.currentTimeMillis(),
                tempo,
                pendingIntent);

    }

    private void ativaAlarmDoBroadcastRecebeDadosServidor(){
        long tempo = 25000;
        Intent intent = new Intent(this, RecebeAtualizacoesDoServidor.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                this.getApplicationContext(), 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
                System.currentTimeMillis(),
                tempo,
                pendingIntent);

    }

}
