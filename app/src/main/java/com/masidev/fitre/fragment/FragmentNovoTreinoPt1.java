package com.masidev.fitre.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.masidev.fitre.R;
import com.masidev.fitre.activity.Main;
import com.masidev.fitre.constante.Constantes;
import com.masidev.fitre.data.dao.AlunoDAO;
import com.masidev.fitre.data.dao.TreinoBaseDAO;
import com.masidev.fitre.data.entidade.Aluno;
import com.masidev.fitre.data.entidade.Personal;
import com.masidev.fitre.data.entidade.Treino;
import com.masidev.fitre.data.entidade.TreinoBase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by jmasiero on 08/03/16.
 */
public class FragmentNovoTreinoPt1 extends Fragment {
    private View view;
    private TreinoBase treinoBase;
    private boolean copiarTreinoBase;
    private String dataAtual;
    private ArrayAdapter<String> adapterSpnDuracao;
    private ArrayAdapter<TreinoBase> adapterSpnTreinoBase;
    private Spinner spnTreinoBase;
    private Spinner spnDuracao;
    private TextView txtDataInicio;
    private Treino treino;
    private CheckBox chkTreinoProgressivo;
    private Boolean alterar;
    private Button btnProcurarAluno;
    private Aluno alunoSelecionado;

    public Aluno getAlunoSelecionado() {
        if(alunoSelecionado == null){
            alunoSelecionado = new Aluno();
        }
        return alunoSelecionado;
    }

    public void setAlunoSelecionado(Aluno alunoSelecionado) {
        this.alunoSelecionado = alunoSelecionado;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_novo_treino_pt1, container, false);

        this.view = view;
        iniciaListeners();
        //parte acionada pra edição do treino
        retornaTreino();
        return view;
    }

    public Treino getTreino(){
        if(this.treino == null){
            this.treino = new Treino();
        }

        return treino;
    }

    private void iniciaListeners(){
        Button btnVoltar = (Button) view.findViewById(R.id.btnVoltar);
        btnVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction()
                        .replace(R.id.container, Main.PlaceholderFragment.newInstance(Constantes.FRAGMENT_TREINOS), String.valueOf(Constantes.FRAGMENT_TREINOS))
                        .addToBackStack(String.valueOf(Constantes.FRAGMENT_TREINOS))
                        .commit();
            }
        });

        chkTreinoProgressivo = (CheckBox) view.findViewById(R.id.chkTreinoProgressivo);
        chkTreinoProgressivo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBox chk = (CheckBox) v.findViewById(R.id.chkTreinoProgressivo);

                if(chk.isChecked()){
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setTitle("Informação!")
                            .setMessage("Treino Progressivo selecionado, coloque todas das cargas do treinamento em 100% e depois configure o treino no menu Treino Progressivo.")
                            .setPositiveButton("Entedi", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });

                    builder.show();
                }

            }
        });

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-d");
        dataAtual = sdf.format(System.currentTimeMillis());


        List<Aluno> alunos = null;
        if(isAlterar()){
            btnProcurarAluno = (Button) view.findViewById(R.id.btnProcurarAluno);
            btnProcurarAluno.setEnabled(false);
        }else{
            btnProcurarAluno = (Button) view.findViewById(R.id.btnProcurarAluno);
            btnProcurarAluno.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TextView txtNomeAluno = (TextView) getView().findViewById(R.id.txtNomeAluno);

                    FragmentDialogBuscaAlunoNovoTreino dialog = new FragmentDialogBuscaAlunoNovoTreino();
                    dialog.setAlunoSelecionado(getAlunoSelecionado());
                    dialog.setTxtNomeAlunoSelecionado(txtNomeAluno);
                    dialog.show(getFragmentManager(), "busca_aluno");
                }
            });
        }

        spnDuracao = (Spinner) view.findViewById(R.id.spnDuracao);
        String[] arrDuracao = Constantes.ARRAY_DURACAO_MESES;
        adapterSpnDuracao = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, arrDuracao);
        spnDuracao.setAdapter(adapterSpnDuracao);

        spnTreinoBase = (Spinner) view.findViewById(R.id.spnTreinoBase);
        List<TreinoBase> lstTreinoBase = TreinoBaseDAO.getInstance(getContext()).listarSemMusculos(true, "");
        adapterSpnTreinoBase = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, lstTreinoBase);
        spnTreinoBase.setAdapter(adapterSpnTreinoBase);

        SharedPreferences sp1 = getContext().getSharedPreferences(Constantes.SHARED_PREFERENCES_LOGIN, Context.MODE_PRIVATE);
        TextView txtNomePersonal = (TextView) view.findViewById(R.id.txtNomePersonal);
        txtNomePersonal.setText(sp1.getString(Constantes.NOME_TREINADOR_LOGADO, "Vazio"));

        Button btnProximaTela = (Button) view.findViewById(R.id.btnProximaTela);

        txtDataInicio = (TextView) view.findViewById(R.id.txtDataInicio);
        String[] dtInicio = dataAtual.split("-");
        txtDataInicio.setText(dtInicio[2]+ "/" + dtInicio[1] + "/" + dtInicio[0]);
        btnProximaTela.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (verificaCamposNovoTreino(view)) {
                    Treino treino = getTreino();
                    EditText edtTreino = (EditText) view.findViewById(R.id.edtTipoTreino);
                    treino.setTipoTreino(edtTreino.getText().toString());

                    treino.setPesoInicio(Double.parseDouble(((EditText) view.findViewById(R.id.edtPesoInicio)).getText().toString()));

                    treino.setPesoIdeal(Double.parseDouble(((EditText) view.findViewById(R.id.edtPesoIdeal)).getText().toString()));

                    String dataIni = dataAtual;

                    treino.setDataInicio(dataIni);

                    String dataFim = "";
                    TextView txtDataFim = (TextView) view.findViewById(R.id.txtDataFim);
                    if(txtDataFim.getText().toString().isEmpty()){
                        int posicaoDuracao = spnDuracao.getSelectedItemPosition();
                        String[] arrDataAtual = dataAtual.split("-");

                        dataFim = retornaDataFinal(arrDataAtual, posicaoDuracao);

                        treino.setDataFim(dataFim);
                    }else{
                        String[] data = txtDataFim.getText().toString().split("/");
                        treino.setDataFim(data[2] + "-" + data[1] + "-" + data[0]);
                    }

                    treino.setObjetivo(((EditText) view.findViewById(R.id.edtObjetivo)).getText().toString());
                    treino.setAnotacoes(((EditText) view.findViewById(R.id.edtAnotacoes)).getText().toString());

                    treino.setAluno(getAlunoSelecionado());

                    SharedPreferences sp2 = getContext().getSharedPreferences(Constantes.SHARED_PREFERENCES_LOGIN, Context.MODE_PRIVATE);

                    Personal personal = new Personal(sp2.getString(Constantes.CPF_TREINADOR_LOGADO, "0"));
                    treino.setPersonal(personal);
                    treino.setVisualizar(false);
                    FragmentManager fragmentManager = getFragmentManager();
                    FragmentNovoTreinoPt2 fragNovoTreino = new FragmentNovoTreinoPt2();
                    fragNovoTreino.setTreino(treino);

                    if (copiarTreinoBase) {
                        fragNovoTreino.setTreinoBase(treinoBase);
                        fragNovoTreino.setCopiarTreinoBase(true);
                    }

                    if(chkTreinoProgressivo.isChecked()){
                        getTreino().setProgressivo(true);
                    }

                    fragmentManager.beginTransaction()
                            .replace(R.id.container, fragNovoTreino, String.valueOf(Constantes.FRAGMENT_NOVO_TREINO))
                            .addToBackStack(String.valueOf(Constantes.FRAGMENT_NOVO_TREINO))
                            .commit();
                } else {                    
                    toastCentralizado(getContext().getString(R.string.erro_algum_campo_vazio));
                }

            }
        });

    }

    private String retornaDataFinal(String[] arrDataAtual, int posicaoDuracao) {
        int mes = Integer.parseInt(arrDataAtual[1]);
        int ano = Integer.parseInt(arrDataAtual[0]);

        for (int i = 1; i <= posicaoDuracao; i++){
            if(mes != 12){
                mes++;
            }else{
                ano++;
                mes = 1;
            }
        }

        Calendar ca = new GregorianCalendar();

        ca.set(Calendar.DAY_OF_MONTH, 1);
        ca.set(Calendar.MONTH, mes-1);
        ca.set(Calendar.YEAR, ano);
        int ultimoDiaDoMes = ca.getActualMaximum(Calendar.DAY_OF_MONTH);

        String dataFinal = String.valueOf(ano) + "-" + String.valueOf(mes) + "-" + String.valueOf(ultimoDiaDoMes);

        return dataFinal;
    }

    public boolean verificaCamposNovoTreino(View view){
        if(((EditText) view.findViewById(R.id.edtTipoTreino)).getText().toString().isEmpty()){
            return false;
        }

        if(((EditText) view.findViewById(R.id.edtPesoInicio)).getText().toString().isEmpty()){
            return false;
        }

        if(((EditText) view.findViewById(R.id.edtPesoIdeal)).getText().toString().isEmpty()){
            return false;
        }

        if(((EditText) view.findViewById(R.id.edtObjetivo)).getText().toString().isEmpty()){
            return false;
        }

        if(getAlunoSelecionado().getCPF() == null){
            return false;
        }

        if(((Spinner) view.findViewById(R.id.spnTreinoBase)).getSelectedItemId() == 0){
            copiarTreinoBase = false;
        }else{
            treinoBase = (TreinoBase) ((Spinner) view.findViewById(R.id.spnTreinoBase)).getSelectedItem();
            copiarTreinoBase = true;
        }

        return true;
    }

    public void retornaTreino(){
        if(getArguments() != null && getArguments().getSerializable("treino") != null) {
            treino = (Treino) getArguments().getSerializable("treino");
            setAlunoSelecionado(AlunoDAO.getInstance(getContext()).getAluno(treino.getAluno().getCPF()));
            populaCampos(treino);
            setAlterar(true);
        }

        limitaDatePicker();

    }

    private void limitaDatePicker() {

    }

    public void populaCampos(Treino t){
        EditText edtTipoTreino = (EditText) view.findViewById(R.id.edtTipoTreino);
        edtTipoTreino.setText(t.getTipoTreino());
        edtTipoTreino.setEnabled(false);

        EditText edtPesoInicio = (EditText) view.findViewById(R.id.edtPesoInicio);
        edtPesoInicio.setText(String.valueOf(t.getPesoInicio()));

        EditText edtPesoIdeal = (EditText) view.findViewById(R.id.edtPesoIdeal);
        edtPesoIdeal.setText(String.valueOf(t.getPesoIdeal()));

        EditText edtObjetivo = (EditText) view.findViewById(R.id.edtObjetivo);
        edtObjetivo.setText(t.getObjetivo());

        EditText edtAnotacoes = (EditText) view.findViewById(R.id.edtAnotacoes);
        edtAnotacoes.setText(t.getAnotacoes());

        TextView txtNomeAluno = (TextView) view.findViewById(R.id.txtNomeAluno);
        txtNomeAluno.setText(getAlunoSelecionado().getNome());

        int posicaoTreinoBase = 0;

        spnTreinoBase.setSelection(posicaoTreinoBase);
        spnTreinoBase.setEnabled(false);

        dataAtual = t.getDataInicio();
        String[] dtInicio = dataAtual.split("-");
        txtDataInicio.setText(dtInicio[2] + "/" + dtInicio[1] + "/" + dtInicio[0]);

        TextView lblDataFim = (TextView) view.findViewById(R.id.lblDataFim);
        lblDataFim.setText("Data Fim");

        TextView txtDataFim = (TextView) view.findViewById(R.id.txtDataFim);
        String[] data = t.getDataFim().split("-");
        txtDataFim.setText(data[2] + "/" + data[1] + "/" + data[0]);
        txtDataFim.setVisibility(View.VISIBLE);

        spnDuracao.setVisibility(View.GONE);

        if(t.getProgressivo()){
            chkTreinoProgressivo = (CheckBox) view.findViewById(R.id.chkTreinoProgressivo);
            chkTreinoProgressivo.setChecked(true);
            chkTreinoProgressivo.setEnabled(false);
        }

    }

    public Boolean isAlterar() {
        if(alterar == null){
            alterar = false;
        }
        return alterar;
    }

    public void setAlterar(Boolean alterar) {
        this.alterar = alterar;
    }

    public void toastCentralizado(String msg){
        Toast toast = Toast.makeText(getContext(), msg , Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL, 0, 0);
        toast.show();
    }
}
