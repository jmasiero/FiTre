package com.masidev.fitre.adapter;

import android.app.Activity;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.masidev.fitre.R;
import com.masidev.fitre.constante.Constantes;
import com.masidev.fitre.data.entidade.Aluno;
import com.masidev.fitre.data.entidade.Musculo;
import com.masidev.fitre.fragment.FragmentDialogTesteTentativas;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jmasiero on 30/09/15.
 */
public class AdapterMusculoTeste extends ArrayAdapter<Musculo> {
    private FragmentManager fm;
    private Context mContext;
    private int layoutResourceId;
    private List<Musculo> lsMusculos;
    private Integer tentativas;
    private ArrayList<String> arrRMS;
    private String repeticoesTesteRMS;
    private Musculo musculoSelecionado;


    public AdapterMusculoTeste(Context context, int resource, List<Musculo> lsMusculos, FragmentManager fm, Aluno aluno) {
        super(context, resource, lsMusculos);

        this.layoutResourceId = resource;
        this.mContext = context;
        this.lsMusculos = lsMusculos;
        this.fm = fm;
        this.tentativas = aluno.getTreino().getTentativas();
        this.arrRMS = aluno.getTreino().getArrRMS();
        this.repeticoesTesteRMS = aluno.getTreino().getRepeticoesTesteRMS();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
        convertView = inflater.inflate(layoutResourceId, parent, false);


        Musculo musculo = lsMusculos.get(position);

        TextView txtExercicio = (TextView) convertView.findViewById(R.id.txtExercicio);
        txtExercicio.setText(musculo.getExercicio());

        iniciaListener(convertView, musculo);
        organizaTela(convertView, musculo);
        return convertView;
    }

    private void organizaTela(View convertView, Musculo musculo) {
        ImageView imgChk = (ImageView) convertView.findViewById(R.id.imgChk);
        if (musculo.getCalculoValido()){
            imgChk.setVisibility(View.VISIBLE);
        }
    }

    private void iniciaListener(View convertView,final Musculo musculo) {

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageView imgChk = (ImageView) v.findViewById(R.id.imgChk);

                FragmentDialogTesteTentativas dialog = new FragmentDialogTesteTentativas();
                dialog.setImgChk(imgChk);
                dialog.setMusculo(musculo);
                dialog.setArrRMS(arrRMS);
                dialog.setTentativas(tentativas);
                dialog.setRepeticoesTesteRMS(repeticoesTesteRMS);
                dialog.show(fm, String.valueOf(Constantes.FRAGMENT_DIALOG_SELECIONA_EXERCICIO_PARA_TESTE_PT4));
            }
        });
    }
}

