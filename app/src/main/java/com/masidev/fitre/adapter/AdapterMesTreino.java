package com.masidev.fitre.adapter;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.masidev.fitre.R;
import com.masidev.fitre.constante.Constantes;
import com.masidev.fitre.data.entidade.SemanaTreinoProgressivo;
import com.masidev.fitre.data.entidade.TreinoProgressivo;
import com.masidev.fitre.mascara.MaskTreinoPiramide;
import com.masidev.fitre.mascara.MaskTreinoProgressivoPiramide;

import java.util.ArrayList;

/**
 * Created by jmasiero on 27/10/15.
 */
public class AdapterMesTreino extends RecyclerView.Adapter<AdapterMesTreino.ViewHolder>{

    private final int rowLayout;
    private final Context mContext;
    Context contextBase;
    int layoutResourceId;
    ArrayList<TreinoProgressivo> lstTreinoProgressivo;
    Boolean visualizar;
    Boolean editar;

    public Boolean isVisualizar() {
        if(visualizar == null){
            visualizar = false;
        }
        return visualizar;
    }

    public void setVisualizar(boolean visualizar) {
        this.visualizar = visualizar;
    }

    public Boolean isEditar() {
        if(editar == null){
            editar = false;
        }
        return editar;
    }

    public void setEditar(boolean editar) {
        this.editar = editar;
    }

    public FragmentManager getFragmentManager() {
        return fragmentManager;
    }

    public void setFragmentManager(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }

    FragmentManager fragmentManager;

    public AdapterMesTreino( int rowLayout, Context context, ArrayList<TreinoProgressivo> lstTreinoProgressivo) {
        this.rowLayout = rowLayout;
        this.mContext = context;
        this.lstTreinoProgressivo = lstTreinoProgressivo;
        this.contextBase = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        return new ViewHolder(v, isVisualizar());
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if(isVisualizar()){
            populaCamposVisualizar(holder, position);
        }else{
            populaCampos(holder, position);
            holder.iniciaMascara(holder, lstTreinoProgressivo, position);

            if(lstTreinoProgressivo.get(position).getLstSemanas().size() == 5){
                holder.lytSemana5.setVisibility(View.VISIBLE);
            }
        }
    }

    private void populaCamposVisualizar(ViewHolder holder, int position) {
        TreinoProgressivo tp = lstTreinoProgressivo.get(position);
        holder.txtMes.setText(Constantes.ARRAY_MESES[lstTreinoProgressivo.get(position).getMes() - 1]);

        holder.txtRepeticoesSem1.setText(tp.getLstSemanas().get(0).getRepeticoes());
        holder.txtRepeticoesSem2.setText(tp.getLstSemanas().get(1).getRepeticoes());
        holder.txtRepeticoesSem3.setText(tp.getLstSemanas().get(2).getRepeticoes());
        holder.txtRepeticoesSem4.setText(tp.getLstSemanas().get(3).getRepeticoes());

        holder.txtCargasSem1.setText(tp.getLstSemanas().get(0).getPorcentagemCargas());
        holder.txtCargasSem2.setText(tp.getLstSemanas().get(1).getPorcentagemCargas());
        holder.txtCargasSem3.setText(tp.getLstSemanas().get(2).getPorcentagemCargas());
        holder.txtCargasSem4.setText(tp.getLstSemanas().get(3).getPorcentagemCargas());

        if(tp.getLstSemanas().size() == 5){
            holder.lytSemana5.setVisibility(View.VISIBLE);
            holder.txtRepeticoesSem5.setText(tp.getLstSemanas().get(4).getRepeticoes());
            holder.txtCargasSem5.setText(tp.getLstSemanas().get(4).getPorcentagemCargas());
        }
    }

    private void populaCampos(ViewHolder holder, int position) {
        holder.txtMes.setText(Constantes.ARRAY_MESES[lstTreinoProgressivo.get(position).getMes() - 1]);

        populaSpinners(holder.spnCarga1Sem1, position, 0, 1);
        populaSpinners(holder.spnCarga2Sem1, position, 0, 2);
        populaSpinners(holder.spnCarga3Sem1, position, 0, 3);

        populaSpinners(holder.spnCarga1Sem2, position, 1, 1);
        populaSpinners(holder.spnCarga2Sem2, position, 1, 2);
        populaSpinners(holder.spnCarga3Sem2, position, 1, 3);

        populaSpinners(holder.spnCarga1Sem3, position, 2, 1);
        populaSpinners(holder.spnCarga2Sem3, position, 2, 2);
        populaSpinners(holder.spnCarga3Sem3, position, 2, 3);

        populaSpinners(holder.spnCarga1Sem4, position, 3, 1);
        populaSpinners(holder.spnCarga2Sem4, position, 3, 2);
        populaSpinners(holder.spnCarga3Sem4, position, 3, 3);


        holder.edtRepeticoesSem1.setText(lstTreinoProgressivo.get(position).getLstSemanas().get(0).getRepeticoes());
        holder.edtRepeticoesSem2.setText(lstTreinoProgressivo.get(position).getLstSemanas().get(1).getRepeticoes());
        holder.edtRepeticoesSem3.setText(lstTreinoProgressivo.get(position).getLstSemanas().get(2).getRepeticoes());
        holder.edtRepeticoesSem4.setText(lstTreinoProgressivo.get(position).getLstSemanas().get(3).getRepeticoes());

        if(lstTreinoProgressivo.get(position).getLstSemanas().size() == 5){
            populaSpinners(holder.spnCarga1Sem5, position, 4, 1);
            populaSpinners(holder.spnCarga2Sem5, position, 4, 2);
            populaSpinners(holder.spnCarga3Sem5, position, 4, 3);

            holder.edtRepeticoesSem5.setText(lstTreinoProgressivo.get(position).getLstSemanas().get(4).getRepeticoes());
            completaSpinners(holder, position, 4);
        }

        if(isVisualizar() || isEditar()){
            completaSpinners(holder, position, 0);
            completaSpinners(holder, position, 1);
            completaSpinners(holder, position, 2);
            completaSpinners(holder, position, 3);
        }

    }

    private void completaSpinners(ViewHolder holder, int posMes, int posSemana){
        String porcCargas = lstTreinoProgressivo.get(posMes).getLstSemanas().get(posSemana).getPorcentagemCargas();
        if(!porcCargas.isEmpty()) {
            String[] arrCargas = porcCargas.split("%");

            if (arrCargas.length == 1) {
                int posicao = retornaPosicaoCarga(arrCargas[0]+"%");
                switch (posSemana){
                    case 0: holder.spnCarga1Sem1.setSelection(posicao);
                        break;
                    case 1: holder.spnCarga1Sem2.setSelection(posicao);
                        break;
                    case 2: holder.spnCarga1Sem3.setSelection(posicao);
                        break;
                    case 3: holder.spnCarga1Sem4.setSelection(posicao);
                        break;
                    case 4: holder.spnCarga1Sem5.setSelection(posicao);
                        break;
                }
            } else {
                int posicao;
                switch (posSemana){
                    case 0:
                        posicao = retornaPosicaoCarga(arrCargas[0]+"%");
                        holder.spnCarga1Sem1.setSelection(posicao);
                        posicao = retornaPosicaoCarga(arrCargas[1]+"%");
                        holder.spnCarga2Sem1.setSelection(posicao);
                        posicao = retornaPosicaoCarga(arrCargas[2]+"%");
                        holder.spnCarga3Sem1.setSelection(posicao);
                        break;
                    case 1:
                        posicao = retornaPosicaoCarga(arrCargas[0] + "%");
                        holder.spnCarga1Sem2.setSelection(posicao);
                        posicao = retornaPosicaoCarga(arrCargas[1]+"%");
                        holder.spnCarga2Sem2.setSelection(posicao);
                        posicao = retornaPosicaoCarga(arrCargas[2]+"%");
                        holder.spnCarga3Sem2.setSelection(posicao);
                        break;
                    case 2:
                        posicao = retornaPosicaoCarga(arrCargas[0]+"%");
                        holder.spnCarga1Sem3.setSelection(posicao);
                        posicao = retornaPosicaoCarga(arrCargas[1]+"%");
                        holder.spnCarga2Sem3.setSelection(posicao);
                        posicao = retornaPosicaoCarga(arrCargas[2]+"%");
                        holder.spnCarga3Sem3.setSelection(posicao);
                        break;
                    case 3:
                        posicao = retornaPosicaoCarga(arrCargas[0]+"%");
                        holder.spnCarga1Sem4.setSelection(posicao);
                        posicao = retornaPosicaoCarga(arrCargas[1]+"%");
                        holder.spnCarga2Sem4.setSelection(posicao);
                        posicao = retornaPosicaoCarga(arrCargas[2]+"%");
                        holder.spnCarga3Sem4.setSelection(posicao);
                        break;
                    case 4:
                        posicao = retornaPosicaoCarga(arrCargas[0]+"%");
                        holder.spnCarga1Sem5.setSelection(posicao);
                        posicao = retornaPosicaoCarga(arrCargas[1]+"%");
                        holder.spnCarga2Sem5.setSelection(posicao);
                        posicao = retornaPosicaoCarga(arrCargas[2]+"%");
                        holder.spnCarga3Sem5.setSelection(posicao);
                        break;
                }




            }
        }
    }

    private void populaSpinners(Spinner spn, final int posMes, final int posSemana, final int tipoSpinner){
        ArrayAdapter<String> adapterSpnCarga = new ArrayAdapter<>(contextBase, android.R.layout.simple_spinner_dropdown_item, Constantes.ARRAY_PORCENTAGEM__TREINO_PROGRESSIVO);
        spn.setAdapter(adapterSpnCarga);
        spn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String spn = Constantes.ARRAY_PORCENTAGEM__TREINO_PROGRESSIVO[position];
                if (spn.equals("-")) {
                    spn = "";
                }

                switch (tipoSpinner) {
                    case 1:
                        lstTreinoProgressivo.get(posMes).getLstSemanas().get(posSemana).setPorcentagemCarga1(spn);
                        break;
                    case 2:
                        lstTreinoProgressivo.get(posMes).getLstSemanas().get(posSemana).setPorcentagemCarga2(spn);
                        break;
                    case 3:
                        lstTreinoProgressivo.get(posMes).getLstSemanas().get(posSemana).setPorcentagemCarga3(spn);
                        break;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        switch (tipoSpinner) {
            case 1:
                spn.setSelection(retornaPosicaoCarga(lstTreinoProgressivo.get(posMes).getLstSemanas().get(posSemana).getPorcentagemCarga1()));
                break;
            case 2:
                spn.setSelection(retornaPosicaoCarga(lstTreinoProgressivo.get(posMes).getLstSemanas().get(posSemana).getPorcentagemCarga2()));
                break;
            case 3:
                spn.setSelection(retornaPosicaoCarga(lstTreinoProgressivo.get(posMes).getLstSemanas().get(posSemana).getPorcentagemCarga3()));
                break;
        }

    }

    private int retornaPosicaoCarga(String carga){
        int posicao = 0;
        if(!carga.isEmpty()) {
            for (int i = 0; i <= Constantes.ARRAY_PORCENTAGEM__TREINO_PROGRESSIVO.length - 1; i++) {
                if (carga.equals(Constantes.ARRAY_PORCENTAGEM__TREINO_PROGRESSIVO[i])) {
                    posicao = i;
                    break;
                }
            }
        }

        return posicao;
    }

    @Override
    public int getItemCount() {
        return lstTreinoProgressivo.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView txtMes;
        //Visualizar
        public TextView txtRepeticoesSem1;
        public TextView txtRepeticoesSem2;
        public TextView txtRepeticoesSem3;
        public TextView txtRepeticoesSem4;
        public TextView txtRepeticoesSem5;

        public TextView txtCargasSem1;
        public TextView txtCargasSem2;
        public TextView txtCargasSem3;
        public TextView txtCargasSem4;
        public TextView txtCargasSem5;

        //Editar e Criar
        public EditText edtRepeticoesSem1;
        public EditText edtRepeticoesSem2;
        public EditText edtRepeticoesSem3;
        public EditText edtRepeticoesSem4;
        public EditText edtRepeticoesSem5;

        private Spinner spnCarga1Sem1;
        private Spinner spnCarga2Sem1;
        private Spinner spnCarga3Sem1;

        private Spinner spnCarga1Sem2;
        private Spinner spnCarga2Sem2;
        private Spinner spnCarga3Sem2;

        private Spinner spnCarga1Sem3;
        private Spinner spnCarga2Sem3;
        private Spinner spnCarga3Sem3;

        private Spinner spnCarga1Sem4;
        private Spinner spnCarga2Sem4;
        private Spinner spnCarga3Sem4;

        private Spinner spnCarga1Sem5;
        private Spinner spnCarga2Sem5;
        private Spinner spnCarga3Sem5;

        private LinearLayout lytSemana5;

        public ViewHolder(View itemView, Boolean visualizar) {
            super(itemView);

            txtMes = (TextView) itemView.findViewById(R.id.txtMes);
            if(visualizar){
                txtRepeticoesSem1 = (TextView) itemView.findViewById(R.id.txtRepeticoesSem1);
                txtRepeticoesSem2 = (TextView) itemView.findViewById(R.id.txtRepeticoesSem2);
                txtRepeticoesSem3 = (TextView) itemView.findViewById(R.id.txtRepeticoesSem3);
                txtRepeticoesSem4 = (TextView) itemView.findViewById(R.id.txtRepeticoesSem4);
                txtRepeticoesSem5 = (TextView) itemView.findViewById(R.id.txtRepeticoesSem5);

                txtCargasSem1 = (TextView) itemView.findViewById(R.id.txtCargasSem1);
                txtCargasSem2 = (TextView) itemView.findViewById(R.id.txtCargasSem2);
                txtCargasSem3 = (TextView) itemView.findViewById(R.id.txtCargasSem3);
                txtCargasSem4 = (TextView) itemView.findViewById(R.id.txtCargasSem4);
                txtCargasSem5 = (TextView) itemView.findViewById(R.id.txtCargasSem5);
            }else {
                edtRepeticoesSem1 = (EditText) itemView.findViewById(R.id.edtRepeticoesSem1);
                edtRepeticoesSem2 = (EditText) itemView.findViewById(R.id.edtRepeticoesSem2);
                edtRepeticoesSem3 = (EditText) itemView.findViewById(R.id.edtRepeticoesSem3);
                edtRepeticoesSem4 = (EditText) itemView.findViewById(R.id.edtRepeticoesSem4);
                edtRepeticoesSem5 = (EditText) itemView.findViewById(R.id.edtRepeticoesSem5);

                spnCarga1Sem1 = (Spinner) itemView.findViewById(R.id.spnCarga1Sem1);
                spnCarga2Sem1 = (Spinner) itemView.findViewById(R.id.spnCarga2Sem1);
                spnCarga3Sem1 = (Spinner) itemView.findViewById(R.id.spnCarga3Sem1);

                spnCarga1Sem2 = (Spinner) itemView.findViewById(R.id.spnCarga1Sem2);
                spnCarga2Sem2 = (Spinner) itemView.findViewById(R.id.spnCarga2Sem2);
                spnCarga3Sem2 = (Spinner) itemView.findViewById(R.id.spnCarga3Sem2);

                spnCarga1Sem3 = (Spinner) itemView.findViewById(R.id.spnCarga1Sem3);
                spnCarga2Sem3 = (Spinner) itemView.findViewById(R.id.spnCarga2Sem3);
                spnCarga3Sem3 = (Spinner) itemView.findViewById(R.id.spnCarga3Sem3);

                spnCarga1Sem4 = (Spinner) itemView.findViewById(R.id.spnCarga1Sem4);
                spnCarga2Sem4 = (Spinner) itemView.findViewById(R.id.spnCarga2Sem4);
                spnCarga3Sem4 = (Spinner) itemView.findViewById(R.id.spnCarga3Sem4);

                spnCarga1Sem5 = (Spinner) itemView.findViewById(R.id.spnCarga1Sem5);
                spnCarga2Sem5 = (Spinner) itemView.findViewById(R.id.spnCarga2Sem5);
                spnCarga3Sem5 = (Spinner) itemView.findViewById(R.id.spnCarga3Sem5);
            }

            lytSemana5 = (LinearLayout) itemView.findViewById(R.id.semana5);
        }

        public void iniciaMascara(ViewHolder holder, ArrayList<TreinoProgressivo> lstTreinoProgressivo, int position){
            SemanaTreinoProgressivo smtp1 = lstTreinoProgressivo.get(position).getLstSemanas().get(0);
            SemanaTreinoProgressivo smtp2 = lstTreinoProgressivo.get(position).getLstSemanas().get(1);
            SemanaTreinoProgressivo smtp3 = lstTreinoProgressivo.get(position).getLstSemanas().get(2);
            SemanaTreinoProgressivo smtp4 = lstTreinoProgressivo.get(position).getLstSemanas().get(3);

            holder.edtRepeticoesSem1.addTextChangedListener(MaskTreinoProgressivoPiramide.insert("###########", edtRepeticoesSem1, smtp1));
            holder.edtRepeticoesSem2.addTextChangedListener(MaskTreinoProgressivoPiramide.insert("###########", edtRepeticoesSem2, smtp2));
            holder.edtRepeticoesSem3.addTextChangedListener(MaskTreinoProgressivoPiramide.insert("###########", edtRepeticoesSem3, smtp3));
            holder.edtRepeticoesSem4.addTextChangedListener(MaskTreinoProgressivoPiramide.insert("###########", edtRepeticoesSem4, smtp4));

            if(lstTreinoProgressivo.get(position).getLstSemanas().size() == 5) {
                SemanaTreinoProgressivo smtp5 = lstTreinoProgressivo.get(position).getLstSemanas().get(4);
                holder.edtRepeticoesSem5.addTextChangedListener(MaskTreinoProgressivoPiramide.insert("###########", edtRepeticoesSem5, smtp5));
            }
        }

    }
}
