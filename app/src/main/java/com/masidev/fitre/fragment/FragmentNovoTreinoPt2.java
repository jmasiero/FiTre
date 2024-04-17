package com.masidev.fitre.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.masidev.fitre.R;
import com.masidev.fitre.activity.Main;
import com.masidev.fitre.constante.Constantes;
import com.masidev.fitre.data.dao.MusculoDAO;
import com.masidev.fitre.data.dao.MusculoTreinadoSemanaDAO;
import com.masidev.fitre.data.dao.TreinoBaseDAO;
import com.masidev.fitre.data.dao.TreinoDAO;
import com.masidev.fitre.data.entidade.Personal;
import com.masidev.fitre.data.entidade.Treino;
import com.masidev.fitre.data.entidade.TreinoBase;

import java.util.ArrayList;

/**
 * Created by jmasiero on 15/10/15.
 */
public class FragmentNovoTreinoPt2 extends Fragment {

    private Treino treino;

    private Boolean base;

    private TreinoBase treinoBase;

    private Boolean copiarTreinoBase;

    public boolean getAlterarTreino(){
        boolean retorno = false;
        if(getTreino() != null && getTreino().getId() != null){
            retorno = true;
        }

        return retorno;
    }

    public boolean getAlterarTreinoBase(){
        boolean retorno = false;
        if(getTreinoBase() != null && getTreinoBase().getId() != null){
            retorno = true;
        }

        return retorno;
    }

    public Boolean getCopiarTreinoBase() {
        if(copiarTreinoBase == null){
            copiarTreinoBase = false;
        }

        return copiarTreinoBase;
    }

    public void setCopiarTreinoBase(Boolean copiarTreinoBase) {
        this.copiarTreinoBase = copiarTreinoBase;
    }

    public Boolean getBase() {
        if(base == null){
            base = false;
        }

        return base;
    }

    private EditText edtNomeTreino;

    public void setBase(Boolean base) {
        this.base = base;
    }

    public TreinoBase getTreinoBase() {
        return treinoBase;
    }

    public void setTreinoBase(TreinoBase treinoBase) {
        this.treinoBase = treinoBase;
    }

    public void setTreino(Treino t){
        treino = t;
    }

    public Treino getTreino(){
        return this.treino;
    }

    public static FragmentNovoTreinoPt2 newInstance() {

        FragmentNovoTreinoPt2 fragment = new FragmentNovoTreinoPt2();

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_novo_treino_pt2, container, false);
        copiaListaMusculoDoTreinoBaseParaTreino();
        iniciaListeners(view);
        return view;
    }

    private void iniciaListeners(View v){

        Button btnBiceps = (Button) v.findViewById(R.id.btnBiceps);
        btnBiceps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                colocaNomeDoTreinoNoTreinoBase();
                novoMusculo(Constantes.BICEPS);
            }
        });

        Button btnTriceps = (Button) v.findViewById(R.id.btnTriceps);
        btnTriceps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                colocaNomeDoTreinoNoTreinoBase();
                novoMusculo(Constantes.TRICEPS);
            }
        });

        Button btnPeito = (Button) v.findViewById(R.id.btnPeito);
        btnPeito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                colocaNomeDoTreinoNoTreinoBase();
                novoMusculo(Constantes.PEITO);
            }
        });

        Button btnOmbro = (Button) v.findViewById(R.id.btnOmbro);
        btnOmbro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                colocaNomeDoTreinoNoTreinoBase();
                novoMusculo(Constantes.OMBRO);
            }
        });

        Button btnCostas = (Button) v.findViewById(R.id.btnCostas);
        btnCostas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                colocaNomeDoTreinoNoTreinoBase();
                novoMusculo(Constantes.COSTAS);
            }
        });


        Button btnPerna = (Button) v.findViewById(R.id.btnPerna);
        btnPerna.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                colocaNomeDoTreinoNoTreinoBase();
                novoMusculo(Constantes.PERNA);
            }
        });


        Button btnGluteos = (Button) v.findViewById(R.id.btnGluteos);
        btnGluteos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                colocaNomeDoTreinoNoTreinoBase();
                novoMusculo(Constantes.GLUTEOS);
            }
        });


        Button btnAntebraco = (Button) v.findViewById(R.id.btnAntebraco);
        btnAntebraco.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                colocaNomeDoTreinoNoTreinoBase();
                novoMusculo(Constantes.ANTEBRACO);
            }
        });


        Button btnAbdominais = (Button) v.findViewById(R.id.btnAbdominais);
        btnAbdominais.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                colocaNomeDoTreinoNoTreinoBase();
                novoMusculo(Constantes.ABDOMINAIS);
            }
        });


        Button btnVoltar = (Button) v.findViewById(R.id.btnVoltar);
        if(getTreino() != null) {
            btnVoltar.setVisibility(View.VISIBLE);
            btnVoltar.setText("Sair");
            btnVoltar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getFragmentManager().beginTransaction()
                            .replace(R.id.container, Main.PlaceholderFragment.newInstance(Constantes.FRAGMENT_TREINOS), String.valueOf(Constantes.FRAGMENT_TREINOS))
                            .addToBackStack(String.valueOf(Constantes.FRAGMENT_TREINOS))
                            .commit();
                }
            });
        }else if(getTreinoBase() != null) {
            btnVoltar.setVisibility(View.VISIBLE);
            btnVoltar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getFragmentManager().beginTransaction()
                            .replace(R.id.container, Main.PlaceholderFragment.newInstance(Constantes.FRAGMENT_TREINO_BASE), String.valueOf(Constantes.FRAGMENT_TREINO_BASE))
                            .addToBackStack(String.valueOf(Constantes.FRAGMENT_TREINO_BASE))
                            .commit();
                }
            });
        }else{
            btnVoltar.setVisibility(View.GONE);
        }

        if((getTreinoBase() != null && getTreinoBase().getVisualizar()) || (getTreino() != null && getTreino().getVisualizar())) {
            ArrayList<Integer> musculosCadastrados = new ArrayList<>();
            if (getTreino() != null && getTreino().getVisualizar()) {
                btnBiceps.setVisibility(View.GONE);
                btnTriceps.setVisibility(View.GONE);
                btnPeito.setVisibility(View.GONE);
                btnOmbro.setVisibility(View.GONE);
                btnCostas.setVisibility(View.GONE);
                btnPerna.setVisibility(View.GONE);
                btnGluteos.setVisibility(View.GONE);
                btnAntebraco.setVisibility(View.GONE);
                btnAbdominais.setVisibility(View.GONE);
                //filtra os musculos registrados para a tela de visualização
                musculosCadastrados = retornaMusculosCadastradosNoTreino(v);
            }

            if (getTreinoBase() != null && getTreinoBase().getVisualizar()) {
                btnBiceps.setVisibility(View.GONE);
                btnTriceps.setVisibility(View.GONE);
                btnPeito.setVisibility(View.GONE);
                btnOmbro.setVisibility(View.GONE);
                btnCostas.setVisibility(View.GONE);
                btnPerna.setVisibility(View.GONE);
                btnGluteos.setVisibility(View.GONE);
                btnAntebraco.setVisibility(View.GONE);
                btnAbdominais.setVisibility(View.GONE);
                //filtra os musculos registrados para a tela de visualização
                musculosCadastrados = retornaMusculosCadastradosNoTreinoBase(v);

                edtNomeTreino = (EditText) v.findViewById(R.id.edtNomeTreinoBase);

                if(getTreinoBase().getTipoTreino() != null){
                    edtNomeTreino.setText(getTreinoBase().getTipoTreino());
                }

                TextView txtNomeTreinoBase = (TextView) v.findViewById(R.id.txtNomeTreinoBase);
                txtNomeTreinoBase.setVisibility(View.VISIBLE);
                txtNomeTreinoBase.setText(getTreinoBase().getTipoTreino());
            }

            for (int i : musculosCadastrados) {
                if (i == 0) {
                    btnBiceps.setVisibility(View.VISIBLE);
                }

                if (i == 1) {
                    btnTriceps.setVisibility(View.VISIBLE);
                }

                if (i == 2) {
                    btnPeito.setVisibility(View.VISIBLE);
                }

                if (i == 3) {
                    btnOmbro.setVisibility(View.VISIBLE);
                }

                if (i == 4) {
                    btnCostas.setVisibility(View.VISIBLE);
                }

                if (i == 5) {
                    btnPerna.setVisibility(View.VISIBLE);
                }

                if (i == 6) {
                    btnGluteos.setVisibility(View.VISIBLE);
                }

                if (i == 7) {
                    btnAntebraco.setVisibility(View.VISIBLE);
                }

                if (i == 8) {
                    btnAbdominais.setVisibility(View.VISIBLE);
                }
            }

        }else if(getBase()){
            edtNomeTreino = (EditText) v.findViewById(R.id.edtNomeTreinoBase);
            edtNomeTreino.setVisibility(View.VISIBLE);
            edtNomeTreino.setText(getTreinoBase().getTipoTreino());

            if(getTreinoBase().getAlterar() && getTreinoBase().getTipoTreino() != null && !getTreinoBase().getTipoTreino().isEmpty()){
                //se for alterar o treino base, ele irá entrar aqui, o nome tipoTreino será bloqueado pq é unico

                edtNomeTreino.setText(getTreinoBase().getTipoTreino());

                TextView txtNomeTreinoBase = (TextView) v.findViewById(R.id.txtNomeTreinoBase);
                edtNomeTreino.setVisibility(View.GONE);
                txtNomeTreinoBase.setVisibility(View.VISIBLE);
                txtNomeTreinoBase.setText(getTreinoBase().getTipoTreino());
            }
        }

        Button btnFinalizar = (Button) v.findViewById(R.id.btnFinalizar);

        if((getTreino() != null && getTreino().getVisualizar()) || (getTreinoBase() != null && getTreinoBase().getVisualizar())){
            btnFinalizar.setVisibility(View.INVISIBLE);
        }else{
            btnFinalizar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        salvarTreino(v);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    public void novoMusculo(int tipoMusculo){

        FragmentManager fragmentManager = getFragmentManager();
        FragmentNovoMusculo fragNovoMusculo = new FragmentNovoMusculo();

        if(!getBase()){
            fragNovoMusculo.setTreino(getTreino());
        }else{
            fragNovoMusculo.setTreinoBase(getTreinoBase());
        }
        fragNovoMusculo.setTipoMusculo(tipoMusculo);
        fragNovoMusculo.setBase(getBase());

        fragmentManager.beginTransaction()
                .replace(R.id.container, fragNovoMusculo, String.valueOf(Constantes.FRAGMENT_NOVO_MUSCULO))
                .addToBackStack(String.valueOf(Constantes.FRAGMENT_NOVO_MUSCULO))
                .commit();

    }

    public void salvarTreino(View v) throws Exception {
        if(possuiMusculos() || getAlterarTreino() || getAlterarTreinoBase()) {
            if(!getBase()) {
                //novo
                if(getTreino().getId() == null) {
                    if (TreinoDAO.getInstance(v.getContext()).salvar(getTreino(), true, false) >= 0) {
                        MusculoTreinadoSemanaDAO.getInstance(v.getContext()).excluirHistorico(getTreino().getAluno());
                        FragmentManager fragmentManager = getFragmentManager();

                        fragmentManager.beginTransaction()
                                .replace(R.id.container, Main.PlaceholderFragment.newInstance(Constantes.FRAGMENT_TREINOS), String.valueOf(Constantes.FRAGMENT_TREINOS))
                                .addToBackStack(String.valueOf(Constantes.FRAGMENT_TREINOS))
                                .commit();
                    }
                }else{//alterar
                    if (TreinoDAO.getInstance(v.getContext()).alterar(getTreino()) >= 0) {
                        //MusculoTreinadoSemanaDAO.getInstance(v.getContext()).excluirHistorico(getTreino().getAluno());
                        FragmentManager fragmentManager = getFragmentManager();

                        fragmentManager.beginTransaction()
                                .replace(R.id.container, Main.PlaceholderFragment.newInstance(Constantes.FRAGMENT_TREINOS), String.valueOf(Constantes.FRAGMENT_TREINOS))
                                .addToBackStack(String.valueOf(Constantes.FRAGMENT_TREINOS))
                                .commit();
                    }
                }
            }else{
                //salvar
                    EditText edtNomeTreinoBase = (EditText) getView().findViewById(R.id.edtNomeTreinoBase);
                    String nomeTreinoBase = edtNomeTreinoBase.getText().toString();
                    getTreinoBase().setTipoTreino(nomeTreinoBase);
                    SharedPreferences sp1 = this.getActivity().getSharedPreferences(Constantes.SHARED_PREFERENCES_LOGIN, Context.MODE_PRIVATE);
                    Personal p = new Personal(sp1.getString(Constantes.CPF_TREINADOR_LOGADO, "vazio"));

                    getTreinoBase().setPersonal(p);

                if(getTreinoBase().getId() == null) {
                    if (!verificaTreinoBaseComNomeRepetido() && verificaCampoVazio() && TreinoBaseDAO.getInstance(v.getContext()).salvar(getTreinoBase(), true) >= 0) {
                        FragmentManager fragmentManager = getFragmentManager();

                        fragmentManager.beginTransaction()
                                .replace(R.id.container, Main.PlaceholderFragment.newInstance(Constantes.FRAGMENT_TREINO_BASE), String.valueOf(Constantes.FRAGMENT_TREINO_BASE))
                                .addToBackStack(String.valueOf(Constantes.FRAGMENT_TREINO_BASE))
                                .commit();
                    }
                }else{//alterar
                    if (TreinoBaseDAO.getInstance(v.getContext()).alterar(getTreinoBase()) >= 0) {
                        FragmentManager fragmentManager = getFragmentManager();

                        fragmentManager.beginTransaction()
                                .replace(R.id.container, Main.PlaceholderFragment.newInstance(Constantes.FRAGMENT_TREINO_BASE), String.valueOf(Constantes.FRAGMENT_TREINO_BASE))
                                .addToBackStack(String.valueOf(Constantes.FRAGMENT_TREINO_BASE))
                                .commit();
                    }
                }
            }
        }else{
            toastCentralizado(getContext().getString(R.string.nao_ha_musculos_cadastrados));
        }
    }

    private boolean verificaTreinoBaseComNomeRepetido() {
        boolean retorno = false;

        TreinoBase tbRetorno = TreinoBaseDAO.getInstance(getContext()).retornaTreinoBasePorNome(getTreinoBase().getTipoTreino());

        if(tbRetorno != null){
            toastCentralizado(getContext().getString(R.string.erro_existe_um_treino_base_com_o_mesmo_nome));
            retorno = true;
        }

        return retorno;
    }

    public boolean possuiMusculos(){
        boolean retorno = false;

        if(!getBase()) {
            for (Integer i = 0; i < getTreino().getLstTodosMusculos().size(); i++) {
                if (getTreino().getLstMusculos(i) != null && getTreino().getLstMusculos(i).size() > 0) {
                    retorno = true;
                    break;
                }
            }
        }else{
            for (Integer i = 0; i < getTreinoBase().getLstTodosMusculos().size(); i++) {
                if (getTreinoBase().getLstMusculos(i) != null && getTreinoBase().getLstMusculos(i).size() > 0) {
                    retorno = true;
                    break;
                }
            }
        }

        return retorno;
    }

    public Boolean verificaCampoVazio(){
        Boolean retorno = false;
        EditText edtTipoTreino = (EditText) getView().findViewById(R.id.edtNomeTreinoBase);
        if(!edtTipoTreino.getText().toString().trim().isEmpty()){
            retorno = true;
        }

        if(!retorno){
            toastCentralizado(getContext().getString(R.string.erro_nome_do_treino_nao_informado));
        }

        return retorno;
    }

    public ArrayList<Integer> retornaMusculosCadastradosNoTreino(View v){
        final ArrayList<Integer> arrTipoMusculoRetorno;
        arrTipoMusculoRetorno = MusculoDAO.getInstance(v.getContext()).retornaTiposDeMusculosCadastradosPorAluno(getTreino().getAluno());

        return arrTipoMusculoRetorno;
    }

    public ArrayList<Integer> retornaMusculosCadastradosNoTreinoBase(View v){
        final ArrayList<Integer> arrTipoMusculoRetorno;

        arrTipoMusculoRetorno = MusculoDAO.getInstance(v.getContext()).retornaTiposDeMusculosCadastradosPorTreinoBase(getTreinoBase());
        return arrTipoMusculoRetorno;
    }

    private void colocaNomeDoTreinoNoTreinoBase(){
        if(getBase()){
            getTreinoBase().setTipoTreino(edtNomeTreino.getText().toString());
        }
    }

    private void copiaListaMusculoDoTreinoBaseParaTreino(){
        if(getCopiarTreinoBase()) {

            ArrayList<Integer> arrMusculos = new ArrayList<>();
            arrMusculos.add(0);
            arrMusculos.add(1);
            arrMusculos.add(2);
            arrMusculos.add(3);
            arrMusculos.add(4);
            arrMusculos.add(5);
            arrMusculos.add(6);
            arrMusculos.add(7);
            arrMusculos.add(8);

            setTreinoBase(TreinoBaseDAO.getInstance(getContext()).buscarTreinoBaseComMusculosFiltrados(getTreinoBase(), arrMusculos));

            getTreino().setLstTodosMusculos(getTreinoBase().getLstTodosMusculos());
        }
    }

    public void toastCentralizado(String msg){
        Toast toast = Toast.makeText(getContext(), msg , Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL, 0, 0);
        toast.show();
    }
}
