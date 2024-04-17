package com.masidev.fitre.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.masidev.fitre.R;
import com.masidev.fitre.constante.Constantes;
import com.masidev.fitre.data.entidade.Musculo;

import com.masidev.fitre.mascara.MaskTreinoPiramide;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jmasiero on 13/10/15.
 */
public class AdapterMusculo extends RecyclerView.Adapter<AdapterMusculo.ViewHolder> {
    private int tipoMusculo;
    private List<Musculo> musculos;
    private int rowLayout;
    private Context mContext;
    private Boolean visualizar;
    //private ViewHolder holderBase;
    private List<Musculo> musculosRemovidos;

    public AdapterMusculo( int rowLayout, Context context, ArrayList<Musculo> lstMusculos, ArrayList<Musculo> musculosRemovidos, int tipoMusculo, Boolean visualizar) {
        musculos = lstMusculos;
        this.musculosRemovidos = musculosRemovidos;
        this.tipoMusculo = tipoMusculo;
        this.rowLayout = rowLayout;
        this.mContext = context;
        this.visualizar = visualizar;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(rowLayout, viewGroup, false);

        return new ViewHolder(v, this.visualizar);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        //holderBase = new ViewHolder(v);

        ArrayAdapter<String> adapterSeries = new ArrayAdapter<String>(
                mContext,R.layout.support_simple_spinner_dropdown_item, Constantes.ARRAY_SERIES
        );
        holder.spnSeries.setAdapter(adapterSeries);

        ArrayAdapter<String> adapterIntervalos = new ArrayAdapter<String>(
                mContext,R.layout.support_simple_spinner_dropdown_item, Constantes.ARRAY_INTERVALOS
        );
        holder.spnIntervalos.setAdapter(adapterIntervalos);

        ArrayAdapter<String> adapterVelExecucao = new ArrayAdapter<String>(
                mContext,R.layout.support_simple_spinner_dropdown_item, Constantes.ARRAY_VEL_EXERCICIOS
        );
        holder.spnVelExecucoes.setAdapter(adapterVelExecucao);

        //aqui
        iniciaListeners(holder);
        populaCampos(holder);

        if(musculos.size() == holder.getAdapterPosition() + 1){
            holder.btnNovoExercicio.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    adicionaMusculo(v, position, holder);
                    //Esconde teclado
                    ((InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE))
                            .hideSoftInputFromWindow(
                                    ((Button) v.findViewById(R.id.btnNovoExercicio)).getWindowToken(), 0
                            );
                }
            });

            if(this.visualizar){
                holder.btnNovoExercicio.setVisibility(View.INVISIBLE);
            }else{
                holder.btnNovoExercicio.setVisibility(View.VISIBLE);
            }

            if(this.visualizar){
                holder.btnExcluiExercicio.setVisibility(View.INVISIBLE);
            }

        }else{
            if(this.visualizar){
                holder.btnNovoExercicio.setVisibility(View.INVISIBLE);
                holder.btnExcluiExercicio.setVisibility(View.INVISIBLE);
            }else{
                holder.btnNovoExercicio.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        removeMusculo(v, position);
                    }
                });
            }
            holder.btnNovoExercicio.setVisibility(View.INVISIBLE);

        }

        holder.btnExcluiExercicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeMusculo(v, position);

                //Esconde teclado
                ((InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE))
                        .hideSoftInputFromWindow(
                                ((Button) v.findViewById(R.id.btnExcluiExercicio)).getWindowToken(), 0
                        );
            }
        });
    }

    private void populaCampos(ViewHolder holder){

        if(holder.getAdapterPosition() >= 0) {
            holder.lblOrdem.setText("Ordem " + (holder.getAdapterPosition() + 1));
            musculos.get(holder.getAdapterPosition()).setOrdem((holder.getAdapterPosition() + 1));
            holder.edtExercicio.setText(musculos.get(holder.getAdapterPosition()).getExercicio());

            holder.edtRepeticoes.setText(musculos.get(holder.getAdapterPosition()).getRepeticoes());

            holder.spnSeries.setSelection(musculos.get(holder.getAdapterPosition()).getSeries());

            holder.spnIntervalos.setSelection(musculos.get(holder.getAdapterPosition()).getIntervalos());

            holder.spnVelExecucoes.setSelection(retornaVelExercicio(musculos.get(holder.getAdapterPosition()).getTmpExecIni(),
                            musculos.get(holder.getAdapterPosition()).getTmpExecFim())
            );

            holder.edtCargas.setText(musculos.get(holder.getAdapterPosition()).getCarga());

            if(this.visualizar){
                holder.edtExercicio.setEnabled(false);
                holder.edtExercicio.setTextColor(Color.BLACK);
                holder.edtRepeticoes.setEnabled(false);
                holder.edtRepeticoes.setTextColor(Color.BLACK);
                holder.lblSeries.setText(String.valueOf(musculos.get(holder.getAdapterPosition()).getSeries()));
                holder.lblIntervalos.setText(String.valueOf(musculos.get(holder.getAdapterPosition()).getIntervalos()));
                holder.lblExecucoes.setText(String.valueOf(musculos.get(holder.getAdapterPosition()).getTmpExecIni()) + "/" + String.valueOf(musculos.get(holder.getAdapterPosition()).getTmpExecFim()));
                holder.edtCargas.setEnabled(false);
                holder.edtCargas.setTextColor(Color.BLACK);
            }

        }
    }

    private Musculo baseiaNovoCampoNoUltimo(ViewHolder holder){
        Musculo musculoAnterior = musculos.get(holder.getAdapterPosition());
        Musculo novoMusculo = new Musculo();

        novoMusculo.setRepeticoes(musculoAnterior.getRepeticoes());
        novoMusculo.setSeries(musculoAnterior.getSeries());
        novoMusculo.setIntervalos(musculoAnterior.getIntervalos());
        novoMusculo.setTmpExecIni(musculoAnterior.getTmpExecIni());
        novoMusculo.setTmpExecFim(musculoAnterior.getTmpExecFim());
        novoMusculo.setCarga(musculoAnterior.getCarga());
        novoMusculo.setTipoMusculo(musculoAnterior.getTipoMusculo());
        novoMusculo.setOrdem(musculoAnterior.getOrdem() + 1);
        return novoMusculo;
    }

    private int retornaVelExercicio(int velIni, int velFim){
        String velExec = velIni + "/" + velFim;

        String[] arrVelocidades = Constantes.ARRAY_VEL_EXERCICIOS;

        int idVelExercicio = 0;

        for (int i = 0; i < arrVelocidades.length; i++){
            if(arrVelocidades[i].equals(velExec)){
                idVelExercicio = i;
                break;
            }
        }

        return idVelExercicio;
    }

    public void removeMusculo(View v, int position) {
        musculosRemovidos.add(musculos.get(position));
        musculos.remove(position);

        this.notifyDataSetChanged();
    }

    public void confirmaExercicio(View v, int position){
//        if(verificaCampos()){
//            //gravaMusculoNaLista(v, position);
//            this.notifyItemChanged(position);
//        }else{
//            Toast.makeText(v.getContext(),"Erro, Campo vazio", Toast.LENGTH_LONG).show();
//        }
    }

    public void adicionaMusculo(View v, int position, ViewHolder holder){
        if(verificaCampos(holder)){
            //gravaMusculoNaLista(v, position);

            Musculo mu = baseiaNovoCampoNoUltimo(holder);
            musculos.add(mu);
            //this.notifyItemChanged(holderBase.getAdapterPosition());
            this.notifyDataSetChanged();
        }else{
            Toast.makeText(v.getContext(),"Erro, Campo vazio", Toast.LENGTH_LONG).show();
        }
    }

    private boolean verificaCampos(ViewHolder holder){

        if (holder.spnVelExecucoes.getSelectedItemId() == Constantes.ITEM_VEL_EXERCICIO_VAZIO){
            return false;
        }

        if (holder.spnSeries.getSelectedItemId() == Constantes.ITEM_SERIE_VAZIA){
            return false;
        }

        if (holder.spnIntervalos.getSelectedItemId() == Constantes.ITEM_INTERVALO_VAZIO){
            return false;
        }

        if (holder.edtExercicio.getText().toString().isEmpty()){
            return false;
        }

        if (holder.edtCargas.getText().toString().isEmpty()){
            return false;
        }else{
            holder.edtCargas.getText().toString().split("-");
        }

        if (holder.edtRepeticoes.getText().toString().isEmpty()){
            return false;
        }

        return true;
    }

    private void gravaMusculoNaLista(View v, int position, ViewHolder holder){

        String[] execucoes = holder.spnVelExecucoes.getSelectedItem().toString().split("/");
        String exercicio = holder.edtExercicio.getText().toString();
        String repeticoes = holder.edtRepeticoes.getText().toString();
        String cargas = holder.edtCargas.getText().toString();


        musculos.set(position, new Musculo(null,
                position + 1,
                exercicio,
                (int) holder.spnSeries.getSelectedItemId(),
                Integer.parseInt(execucoes[0]),
                Integer.parseInt(execucoes[1]),
                (int) holder.spnIntervalos.getSelectedItemId(),
                repeticoes,
                cargas,
                tipoMusculo,
                null,
                null));
    }

    @Override
    public int getItemCount() {
        return musculos.size();
    }

    private void iniciaListeners(final ViewHolder holder){
        holder.edtExercicio.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                musculos.get(holder.getAdapterPosition()).setExercicio(s.toString());
            }
        });

        holder.edtRepeticoes.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                musculos.get(holder.getAdapterPosition()).setRepeticoes(s.toString());
            }
        });

        holder.edtCargas.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                musculos.get(holder.getAdapterPosition()).setCarga(s.toString());
            }
        });

        holder.spnIntervalos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                musculos.get(holder.getAdapterPosition()).setIntervalos((int) holder.spnIntervalos.getSelectedItemId());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        holder.spnSeries.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                musculos.get(holder.getAdapterPosition()).setSeries((int) holder.spnSeries.getSelectedItemId());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        holder.spnVelExecucoes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(holder.spnVelExecucoes.getSelectedItemPosition() > 0) {
                    String[] execucoes = holder.spnVelExecucoes.getSelectedItem().toString().split("/");
                    musculos.get(holder.getAdapterPosition()).setTmpExecIni(Integer.parseInt(execucoes[0]));
                    musculos.get(holder.getAdapterPosition()).setTmpExecFim(Integer.parseInt(execucoes[1]));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView lblOrdem;
        public EditText edtExercicio;
        public EditText edtRepeticoes;
        public Spinner spnSeries;
        public Spinner spnIntervalos;
        public Spinner spnVelExecucoes;
        public EditText edtCargas;
        public EditText edtDias;
        public Button btnNovoExercicio;
        public Button btnExcluiExercicio;
        //parte de visualização
        public TextView lblSeries;
        public TextView lblIntervalos;
        public TextView lblExecucoes;

        public ViewHolder(View itemView, Boolean visualizar) {
            super(itemView);

            lblOrdem = (TextView) itemView.findViewById(R.id.lblOrdem);
            edtExercicio = (EditText) itemView.findViewById(R.id.edtExercicio);
            edtRepeticoes = (EditText) itemView.findViewById(R.id.edtRepeticoes);
            spnSeries = (Spinner) itemView.findViewById(R.id.spnSeries);
            spnIntervalos = (Spinner) itemView.findViewById(R.id.spnIntervalos);
            spnVelExecucoes = (Spinner) itemView.findViewById(R.id.spnVelExecucoes);
            edtCargas = (EditText) itemView.findViewById(R.id.edtCargas);
            btnNovoExercicio = (Button) itemView.findViewById(R.id.btnNovoExercicio);
            btnExcluiExercicio = (Button) itemView.findViewById(R.id.btnExcluiExercicio);

            if(visualizar){
                lblSeries = (TextView) itemView.findViewById(R.id.lblSeries);
                lblIntervalos = (TextView) itemView.findViewById(R.id.lblIntervalos);
                lblExecucoes = (TextView) itemView.findViewById(R.id.lblExecucoes);
            }

            iniciaMascara(itemView);
        }

        private void iniciaMascara(View v){
            edtCargas.addTextChangedListener(MaskTreinoPiramide.insert("###########", edtCargas));
            edtRepeticoes.addTextChangedListener(MaskTreinoPiramide.insert("###########", edtRepeticoes));
        }
    }
}
