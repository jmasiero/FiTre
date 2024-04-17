package com.masidev.fitre.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.masidev.fitre.R;
import com.masidev.fitre.adapter.AdapterAluno;
import com.masidev.fitre.constante.Constantes;
import com.masidev.fitre.data.dao.AlunoDAO;
import com.masidev.fitre.data.entidade.Aluno;
import com.masidev.fitre.data.entidade.Musculo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by jmasiero on 10/05/16.
 */
public class FragmentDialogTesteTentativas extends DialogFragment {
    private Musculo musculo;
    private ArrayList<String> arrRMS;
    private Integer tentativas;
    private String repeticoesTesteRMS;
    private ArrayList<ArrayList<String>> arrTentativas;

    private ImageView imgChk;

    public String getRepeticoesTesteRMS() {
        return repeticoesTesteRMS;
    }

    public void setRepeticoesTesteRMS(String repeticoesTesteRMS) {
        this.repeticoesTesteRMS = repeticoesTesteRMS;
    }

    public ImageView getImgChk() {
        return imgChk;
    }

    public void setImgChk(ImageView imgChk) {
        this.imgChk = imgChk;
    }

    EditText edtCarga1;
    EditText edtEdtMovimentos1;
    EditText edtCarga2;
    EditText edtEdtMovimentos2;
    EditText edtCarga3;
    EditText edtEdtMovimentos3;
    TextView edtResultadoCargas;

    Button btnOk;


    public Integer getTentativas() {
        return tentativas;
    }

    public void setTentativas(Integer tentativas) {
        this.tentativas = tentativas;
    }

    public ArrayList<String> getArrRMS() {
        return arrRMS;
    }

    public void setArrRMS(ArrayList<String> arrRMS) {
        this.arrRMS = arrRMS;
    }


    public Musculo getMusculo() {
        return musculo;
    }

    public void setMusculo(Musculo musculo) {
        this.musculo = musculo;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_dialog_teste_tentativas, null);
        ativaCamposTentativas(view);
        iniciaListeners(view);
        preencheCampos();
        builder.setView(view);

        return builder.create();
    }

    private void preencheCampos() {
        edtResultadoCargas.setText(musculo.getCarga());
        arrTentativas = musculo.getArrTentativas(tentativas);

        edtCarga1.setText(arrTentativas.get(0).get(0));
        edtEdtMovimentos1.setText(arrTentativas.get(0).get(1));

        switch (tentativas){
            case 2:
                edtCarga2.setText(arrTentativas.get(1).get(0));
                edtEdtMovimentos2.setText(arrTentativas.get(1).get(1));
                break;
            case 3:
                edtCarga2.setText(arrTentativas.get(1).get(0));
                edtEdtMovimentos2.setText(arrTentativas.get(1).get(1));
                edtCarga3.setText(arrTentativas.get(2).get(0));
                edtEdtMovimentos3.setText(arrTentativas.get(2).get(1));
                break;
        }

    }

    private void ativaCamposTentativas(View v) {
        LinearLayout tentativa2 = (LinearLayout) v.findViewById(R.id.tentativa2);
        LinearLayout tentativa3 = (LinearLayout) v.findViewById(R.id.tentativa3);

        if(tentativas == 2){
            tentativa2.setVisibility(View.VISIBLE);
        }else if(tentativas == 3){
            tentativa2.setVisibility(View.VISIBLE);
            tentativa3.setVisibility(View.VISIBLE);
        }
    }

    private void iniciaListeners(View view) {
        edtCarga1 = (EditText) view.findViewById(R.id.edtCarga1);
        edtEdtMovimentos1 = (EditText) view.findViewById(R.id.edtMovimentos1);
        edtCarga2 = (EditText) view.findViewById(R.id.edtCarga2);
        edtEdtMovimentos2 = (EditText) view.findViewById(R.id.edtMovimentos2);
        edtCarga3 = (EditText) view.findViewById(R.id.edtCarga3);
        edtEdtMovimentos3 = (EditText) view.findViewById(R.id.edtMovimentos3);

        edtResultadoCargas = (TextView) view.findViewById(R.id.edtResultadoCargas);

        edtCarga1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                arrTentativas.get(0).set(0, s.toString());
                calculoRms();
            }
        });

        edtEdtMovimentos1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                arrTentativas.get(0).set(1, s.toString());
                calculoRms();
            }
        });

        edtCarga2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                arrTentativas.get(1).set(0, s.toString());
                calculoRms();
            }
        });

        edtEdtMovimentos2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                arrTentativas.get(1).set(1, s.toString());
                calculoRms();
            }
        });

        edtCarga3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                arrTentativas.get(2).set(0, s.toString());
                calculoRms();
            }
        });

        edtEdtMovimentos3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                arrTentativas.get(2).set(1, s.toString());
                calculoRms();
            }
        });

        btnOk = (Button) view.findViewById(R.id.btnOk);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    public void calculoRms(){
        Integer carga1 = 0;
        Integer carga2 = 0;
        Integer carga3 = 0;
        Integer movimentos1 = 0;
        Integer movimentos2 = 0;
        Integer movimentos3 = 0;

        Integer retorno1 = 0;
        Integer retorno2 = 0;
        Integer retorno3 = 0;

        Integer cargaMaximaFinal = 0;

        switch (tentativas){
            case 1:
                if(!edtCarga1.getText().toString().equals("") && !edtEdtMovimentos1.getText().toString().equals("")){
                    carga1 = Integer.valueOf(edtCarga1.getText().toString());
                    movimentos1 = Integer.valueOf(edtEdtMovimentos1.getText().toString());
                    retorno1 = retornaCargaMaxima(carga1, movimentos1);
                    cargaMaximaFinal = retorno1;
                    musculo.setCalculoValido(true);
                    getImgChk().setVisibility(View.VISIBLE);
                }else{
                    getImgChk().setVisibility(View.INVISIBLE);
                }
                break;

            case 2:
                if( !edtCarga1.getText().toString().equals("") && !edtEdtMovimentos1.getText().toString().equals("") &&
                    !edtCarga2.getText().toString().equals("") && !edtEdtMovimentos2.getText().toString().equals("") ){

                    carga1 = Integer.valueOf(edtCarga1.getText().toString());
                    carga2 = Integer.valueOf(edtCarga2.getText().toString());
                    movimentos1 = Integer.valueOf(edtEdtMovimentos1.getText().toString());
                    movimentos2 = Integer.valueOf(edtEdtMovimentos2.getText().toString());
                    retorno1 = retornaCargaMaxima(carga1, movimentos1);
                    retorno2 = retornaCargaMaxima(carga2, movimentos2);

                    cargaMaximaFinal = (retorno1 + retorno2)/2;

                    musculo.setCalculoValido(true);
                    getImgChk().setVisibility(View.VISIBLE);
                }else{
                    getImgChk().setVisibility(View.INVISIBLE);
                }

                break;

            case 3:
                if( !edtCarga1.getText().toString().equals("") && !edtEdtMovimentos1.getText().toString().equals("") &&
                    !edtCarga2.getText().toString().equals("") && !edtEdtMovimentos2.getText().toString().equals("") &&
                    !edtCarga3.getText().toString().equals("") && !edtEdtMovimentos3.getText().toString().equals("") ){

                    carga1 = Integer.valueOf(edtCarga1.getText().toString());
                    carga2 = Integer.valueOf(edtCarga2.getText().toString());
                    carga3 = Integer.valueOf(edtCarga3.getText().toString());
                    movimentos1 = Integer.valueOf(edtEdtMovimentos1.getText().toString());
                    movimentos2 = Integer.valueOf(edtEdtMovimentos2.getText().toString());
                    movimentos3 = Integer.valueOf(edtEdtMovimentos3.getText().toString());

                    retorno1 = retornaCargaMaxima(carga1, movimentos1);
                    retorno2 = retornaCargaMaxima(carga2, movimentos2);
                    retorno3 = retornaCargaMaxima(carga3, movimentos3);

                    cargaMaximaFinal = (retorno1 + retorno2 + retorno3)/3;

                    musculo.setCalculoValido(true);
                    getImgChk().setVisibility(View.VISIBLE);
                }else{
                    getImgChk().setVisibility(View.INVISIBLE);
                }

                break;
        }

        if(musculo.getCalculoValido()){
            String carga = "";
            for (int i = 0; i < arrRMS.size(); i++){
                if(i == 0){
                    carga += Math.round(cargaMaximaFinal * (Double.valueOf(arrRMS.get(i).split("%")[0])/100));
                }else{
                    carga += "-" + Math.round((cargaMaximaFinal * (Double.valueOf(arrRMS.get(i).split("%")[0])/100)));
                }

            }

            edtResultadoCargas.setText(carga);
            musculo.setCarga(carga);
            musculo.setRepeticoes(repeticoesTesteRMS);
        }

    }

    private Integer retornaCargaMaxima(Integer carga, Integer movimentos) {
        Integer porcentagemTabela = retornaPorcentagemTabela(movimentos);

        return (carga * 100)/porcentagemTabela;
    }

    private Integer retornaPorcentagemTabela(Integer movimentos) {
        Integer retorno = 1;

        switch (movimentos){
            case 1://100%
                retorno = 100;
                break;
            case 2://95%
            case 3:
                retorno = 95;
                break;
            case 4://90%
            case 5:
                retorno = 90;
                break;
            case 6://85%
            case 7:
                retorno = 85;
                break;
            case 8://80%
            case 9:
                retorno = 80;
                break;
            case 10://75%
            case 11:
                retorno = 75;
                break;
            case 12://70%
            case 13:
            case 14:
                retorno = 70;
                break;
            case 15://65%
            case 16:
                retorno = 65;
                break;
            case 17://60%
            case 18:
            case 19:
            case 20:
                retorno = 60;
                break;
            case 21://55%
            case 22:
            case 23:
            case 24:
            case 25:
            case 26:
                retorno = 55;
                break;
            case 27://50%
            case 28:
            case 29:
            case 30:
            case 31:
            case 32:
            case 33:
            case 34:
            case 35:
            case 36:
            case 37:
            case 38:
            case 39:
            case 40:
            case 41:
                retorno = 50;
                break;
            default://40%
                retorno = 40;
                break;
        }

        return retorno;
    }
}
