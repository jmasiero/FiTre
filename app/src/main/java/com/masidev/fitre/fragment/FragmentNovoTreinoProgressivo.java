package com.masidev.fitre.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;

import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.masidev.fitre.R;
import com.masidev.fitre.activity.Main;
import com.masidev.fitre.adapter.AdapterMesTreino;
import com.masidev.fitre.constante.Constantes;
import com.masidev.fitre.data.dao.AlunoDAO;
import com.masidev.fitre.data.dao.TreinoDAO;
import com.masidev.fitre.data.dao.TreinoProgressivoDAO;
import com.masidev.fitre.data.entidade.Aluno;
import com.masidev.fitre.data.entidade.SemanaTreinoProgressivo;
import com.masidev.fitre.data.entidade.Treino;
import com.masidev.fitre.data.entidade.TreinoProgressivo;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by jmasiero on 16/10/15.
 */
public class FragmentNovoTreinoProgressivo extends Fragment {
    private Treino treino;
    private Aluno aluno;
    private ArrayList<TreinoProgressivo> lstTP;
    private Boolean visualizar;
    private Boolean editar;
    private TextView txtNomeTreinoAluno;

    public Boolean getEditar() {
        if(editar == null){
            editar = false;
        }
        return editar;
    }

    public void setEditar(Boolean editar) {
        this.editar = editar;
    }

    public Boolean getVisualizar() {
        if(visualizar == null){
            visualizar = false;
        }
        return visualizar;
    }

    public void setVisualizar(Boolean visualizar) {
        this.visualizar = visualizar;
    }


    public Treino getTreino() {
        return treino;
    }

    public void setTreino(Treino treino) {
        this.treino = treino;
    }

    public Aluno getAluno(){
        return this.aluno;
    }
    public void setAluno(Aluno aluno) {
        this.aluno = aluno;
    }

    public static FragmentNovoTreinoProgressivo newInstance() {

        Bundle args = new Bundle();

        FragmentNovoTreinoProgressivo fragment = new FragmentNovoTreinoProgressivo();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_lista_novo_treino_progressivo, container, false);

        criaListeners(view);
        if(getVisualizar() || getEditar()){
            populaCampos(view);
            setAluno(AlunoDAO.getInstance(getContext()).getAluno(getTreino().getAluno().getCPF()));
            txtNomeTreinoAluno = (TextView) view.findViewById(R.id.txtNomeTreinoAluno);
            txtNomeTreinoAluno.setText(getAluno().getNome() + " - " + getTreino().getTipoTreino());
        }

        if(getVisualizar()){
            Button btnSalvar = (Button) view.findViewById(R.id.btnSalvarTreino);
            btnSalvar.setVisibility(View.GONE);
        }

        if (getAluno() != null && !getVisualizar() && !getEditar()) {
            ArrayList<Integer> arrMusculos = new ArrayList<>();

            setTreino(TreinoDAO.getInstance(view.getContext()).buscarTreinoComMusculosFiltrados(getAluno(), arrMusculos, true, true));


            txtNomeTreinoAluno = (TextView) view.findViewById(R.id.txtNomeTreinoAluno);
            txtNomeTreinoAluno.setText(getAluno().getNome() + " - " + getTreino().getTipoTreino());

            //cria treinos progressivos
            String[] arrDataInicio = getTreino().getDataInicio().split("-");
            Integer anoInicio = Integer.parseInt(arrDataInicio[0]);
            Integer mesInicio = Integer.parseInt(arrDataInicio[1]);
            Integer mesInicio2 = Integer.parseInt(arrDataInicio[1]);
            Integer diaInicio = Integer.parseInt(arrDataInicio[2]);

            String[] arrDataFim = getTreino().getDataFim().split("-");
            Integer anoFim = Integer.parseInt(arrDataFim[0]);
            Integer mesFim = Integer.parseInt(arrDataFim[1]);
            Integer mesFim2 = 0;
            Integer diaFim = Integer.parseInt(arrDataFim[2]);

            lstTP = new ArrayList<>();

            if(mesInicio > mesFim){
                mesFim2 = 12;
                for (int i = mesInicio; i <= mesFim2; i++) {
                    TreinoProgressivo tp = new TreinoProgressivo(getTreino().getId(), i);
                    Calendar ca = new GregorianCalendar();

                    ca.set(Calendar.DAY_OF_MONTH, 1);
                    ca.set(Calendar.MONTH, i - 1);
                    ca.set(Calendar.YEAR, anoInicio);

                    int semanas = obterQuantidadeDeSemanasMes(ca);
                    for (int j = 1; j <= semanas; j++) {
                        tp.getLstSemanas().add(new SemanaTreinoProgressivo("", "", j));
                    }

                    lstTP.add(tp);
                }

                mesInicio2 = 1;
                for (int i = mesInicio2; i <= mesFim; i++) {
                    TreinoProgressivo tp = new TreinoProgressivo(getTreino().getId(), i);
                    Calendar ca = new GregorianCalendar();

                    ca.set(Calendar.DAY_OF_MONTH, 1);
                    ca.set(Calendar.MONTH, i - 1);
                    ca.set(Calendar.YEAR, anoFim);

                    int semanas = obterQuantidadeDeSemanasMes(ca);
                    for (int j = 1; j <= semanas; j++) {
                        tp.getLstSemanas().add(new SemanaTreinoProgressivo("", "", j));
                    }

                    lstTP.add(tp);
                }
            }else{
                for (int i = mesInicio; i <= mesFim; i++) {
                    TreinoProgressivo tp = new TreinoProgressivo(getTreino().getId(), i);
                    Calendar ca = new GregorianCalendar();

                    ca.set(Calendar.DAY_OF_MONTH, 1);
                    ca.set(Calendar.MONTH, i - 1);
                    ca.set(Calendar.YEAR, anoInicio);

                    int semanas = obterQuantidadeDeSemanasMes(ca);
                    for (int j = 1; j <= semanas; j++) {
                        tp.getLstSemanas().add(new SemanaTreinoProgressivo("", "", j));
                    }

                    lstTP.add(tp);
                }
            }

        }

        RecyclerView mRecyclerView = (RecyclerView) view.findViewById(R.id.listCardView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        AdapterMesTreino mAdapter;
        if(getVisualizar()){
            mAdapter = new AdapterMesTreino(R.layout.adapter_card_view_treino_progressivo_visualizar, getContext(), lstTP);
        }else{
            mAdapter = new AdapterMesTreino(R.layout.adapter_card_view_treino_progressivo, getContext(), lstTP);
        }
        mAdapter.setVisualizar(getVisualizar());
        mAdapter.setEditar(getEditar());
        mRecyclerView.setAdapter(mAdapter);

        return view;
    }

    private void populaCampos(View view) {
        lstTP = TreinoProgressivoDAO.getInstance(getContext()).retornarTreinoProgressivo(getTreino());
    }

    private void criaListeners(View view) {
        final Button btnSalvar = (Button) view.findViewById(R.id.btnSalvarTreino);

        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if(validaCampos()) {
                        if(getEditar()){
                            for (TreinoProgressivo tp : lstTP) {
                                TreinoProgressivoDAO.getInstance(getContext()).alterar(tp, true);
                            }

                            FragmentManager fragmentManager = getFragmentManager();

                            fragmentManager.beginTransaction()
                                    .replace(R.id.container, Main.PlaceholderFragment.newInstance(Constantes.FRAGMENT_TREINO_PROGRESSIVO), String.valueOf(Constantes.FRAGMENT_TREINO_PROGRESSIVO))
                                    .addToBackStack(String.valueOf(Constantes.FRAGMENT_TREINO_PROGRESSIVO))
                                    .commit();
                        }else{
                            for (TreinoProgressivo tp : lstTP) {
                                    TreinoProgressivoDAO.getInstance(getContext()).salvar(tp, true);
                            }

                            FragmentManager fragmentManager = getFragmentManager();

                            fragmentManager.beginTransaction()
                                    .replace(R.id.container, Main.PlaceholderFragment.newInstance(Constantes.FRAGMENT_TREINO_PROGRESSIVO), String.valueOf(Constantes.FRAGMENT_TREINO_PROGRESSIVO))
                                    .addToBackStack(String.valueOf(Constantes.FRAGMENT_TREINO_PROGRESSIVO))
                                    .commit();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private boolean validaCampos() {
        Boolean retorno = true;
        Integer mes = 0;
        Integer spnVazios = 0;
        String erro = "Erro! Campos Vazios! no Mês: ";
        for (TreinoProgressivo tp : lstTP){
            for (SemanaTreinoProgressivo smtp : tp.getLstSemanas()) {
                spnVazios = 0;
                if( smtp.getPorcentagemCarga1().isEmpty() &&
                        smtp.getPorcentagemCarga2().isEmpty() &&
                        smtp.getPorcentagemCarga3().isEmpty()){
                    retorno = false;
                    break;
                }

                if( smtp.getPorcentagemCarga1().isEmpty() ){
                    spnVazios++;
                }

                if( smtp.getPorcentagemCarga2().isEmpty() ){
                    spnVazios++;
                }

                if( smtp.getPorcentagemCarga3().isEmpty() ){
                    spnVazios++;
                }

                if(spnVazios == 1){
                    erro = "Erro! Para criar o treino é necessário 1 ou 3 campos de carga preenchido. Mês: ";
                    retorno = false;
                    break;
                }

                if(smtp.getRepeticoes().trim().isEmpty()){
                    retorno = false;
                    break;
                }
            }

            if(!retorno){
                mes = tp.getMes();
                break;
            }
        }

        if(!retorno) {
            Toast toast = Toast.makeText(getContext(), erro + Constantes.ARRAY_MESES[mes-1] , Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL, 0, 0);
            toast.show();
        }

        return retorno;
    }

    private int obterQuantidadeDeSemanasMes(Calendar ca) {
        int primeiroDiaDoMes = 1;
        int ultimoDiaDoMes = ca.getActualMaximum(Calendar.DAY_OF_MONTH);

        ca.set(Calendar.DAY_OF_MONTH, ultimoDiaDoMes);
        int semanasMes = ca.get(Calendar.WEEK_OF_MONTH);

        if(ca.get(Calendar.DAY_OF_WEEK) < 4){
            semanasMes--;
        }

        ca.set(Calendar.DAY_OF_MONTH, primeiroDiaDoMes);
        if(ca.get(Calendar.DAY_OF_WEEK) > 4){
            semanasMes--;
        }

        return semanasMes;
    }

}
