package com.masidev.fitre.fragment;

import android.app.AlertDialog;
import android.app.Dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.masidev.fitre.R;
import com.masidev.fitre.activity.Main;
import com.masidev.fitre.adapter.AdapterAluno;
import com.masidev.fitre.constante.Constantes;
import com.masidev.fitre.data.dao.AlunoDAO;
import com.masidev.fitre.data.dao.FrequenciaDAO;
import com.masidev.fitre.data.dao.MusculoDAO;
import com.masidev.fitre.data.dao.MusculoTreinadoSemanaDAO;
import com.masidev.fitre.data.dao.ProgramaTreinoDAO;
import com.masidev.fitre.data.dao.TreinoDAO;
import com.masidev.fitre.data.dao.TreinoProgressivoDAO;
import com.masidev.fitre.data.entidade.Aluno;
import com.masidev.fitre.data.entidade.Frequencia;
import com.masidev.fitre.data.entidade.Musculo;
import com.masidev.fitre.data.entidade.MusculoTreinadoSemana;
import com.masidev.fitre.data.entidade.ProgramaTreinadoSemana;
import com.masidev.fitre.data.entidade.ProgramaTreino;
import com.masidev.fitre.data.entidade.Treino;
import com.masidev.fitre.data.entidade.TreinoProgressivo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by jmasiero on 03/11/15.
 */
public class FragmentDialogBuscaAlunoIniciaTreino extends DialogFragment {
    private EditText edtNome;
    private ListView listaTreinoAluno;
    private ArrayList<Aluno> lstAlunos;
    private ArrayList<Aluno> lstAlunas;
    private ArrayList<Aluno> lstAlunosBusca;
    private ArrayList<MusculoTreinadoSemana> lstMts;
    private ArrayList<ProgramaTreinadoSemana> lstPts;
    private Integer indiceProgramaSelecionado;

    private Aluno alunoSelecionado;
    private Context contextBase;

    public ArrayList<Aluno> getLstAlunos() {
        return lstAlunos;
    }

    public void setLstAlunosEAlunas(ArrayList<Aluno> lstAlunos, ArrayList<Aluno> lstAlunas) {
        this.lstAlunos = lstAlunos;
        this.lstAlunas = lstAlunas;
    }

    public static FragmentDialogBuscaAlunoIniciaTreino newInstance() {
        FragmentDialogBuscaAlunoIniciaTreino fragment = new FragmentDialogBuscaAlunoIniciaTreino();
        return fragment;
    }

    public FragmentDialogBuscaAlunoIniciaTreino() {
    }

    @Override
    public void onStart() {
        super.onStart();

        // safety check
        if (getDialog() == null) {
            return;
        }

        DisplayMetrics metrics = new DisplayMetrics();
        getDialog().getWindow().getWindowManager().getDefaultDisplay().getMetrics(metrics);

        int y = metrics.heightPixels;

        int x = metrics.widthPixels;

        int dialogHeight = (int) (y*0.65);
        int dialogWidth = (int) (x*0.75);

        getDialog().getWindow().setLayout(dialogWidth, dialogHeight);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_dialog_lista_filtra_aluno, null);
        builder.setView(view);

        listaAlunos(view);
        iniciaListeners(view);

        return builder.create();
    }

    private void listaAlunos(View view) {
        lstAlunosBusca = AlunoDAO.getInstance(view.getContext()).listaFiltrada("", false, false, true);
        preencheLista(view);
    }

    private void iniciaListeners(final View view) {
        edtNome = (EditText) view.findViewById(R.id.edtNome);
        edtNome.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String filtro = edtNome.getText().toString().trim();
                if (filtro != "") {
                    lstAlunosBusca = AlunoDAO.getInstance(view.getContext()).listaFiltrada(filtro, false, false, true);
                    preencheLista(view);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        listaTreinoAluno.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                //Separa o aluno selecionado
                alunoSelecionado = lstAlunosBusca.get(position);
                if (!alunoRepetido(view) && treinoProgressivoValido(view)) {
                    alunoSelecionado.setLstProgramaTreino(ProgramaTreinoDAO.getInstance(getContext()).retornarProgramasDoAluno(alunoSelecionado));

                    if (!alunoSelecionado.getTreinoProgramado()) {
                        //Aqui o treinador verificar se o aluno quer treino misto ou simples
                        CharSequence sequenciaTreino[] = new CharSequence[]{"Simples", "Misto"};
                        contextBase = view.getContext();
                        AlertDialog.Builder builder = new AlertDialog.Builder(contextBase);
                        builder.setTitle("Sequência de Treino")
                                .setItems(sequenciaTreino, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        try {
                                            selecionaTreinoDaSemana(which);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });

                        builder.show();
                    } else {
                        try {
                            selecionaProgramaDeTreinamento();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });

    }

    private void selecionaProgramaDeTreinamento() throws Exception {
        String treinou = "";

        CharSequence[] csProgramas = new String[alunoSelecionado.getLstProgramaTreino().size()];

        ProgramaTreino pt;
        for (int i = 0; i < alunoSelecionado.getLstProgramaTreino().size(); i++) {
            pt = alunoSelecionado.getLstProgramaTreino().get(i);
            treinou = "";

            if(pt.getTreinou()){
                treinou = "[TREINADO] - ";
            }

            csProgramas[i] = treinou + pt.getNomePrograma() + " - " + pt.getOrdem();
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(R.string.Treinos_da_semana);

        builder.setSingleChoiceItems(csProgramas, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                indiceProgramaSelecionado = which;
                alunoSelecionado.setProgramaSelecionado(alunoSelecionado.getLstProgramaTreino().get(which));
            }
        });

        builder.setPositiveButton(R.string.Confirmar, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (alunoSelecionado.getProgramaSelecionado() != null) {
                    if (alunoSelecionado.getProgramaSelecionado().getSimples()) {
                        alunoSelecionado.setTipoTreino(Constantes.TREINO_SIMPLES);
                    } else {
                        alunoSelecionado.setTipoTreino(Constantes.TREINO_MISTO);
                    }
                    final ArrayList<Integer> arrTipoMusculo = new ArrayList<>();
                    Treino treino = new Treino();
                    Musculo musculoComIdTreino = null;
                    for(Musculo m : alunoSelecionado.getProgramaSelecionado().getLstMusculos()){
                        treino.getLstTodosMusculos().get(m.getTipoMusculo()).add(m);
                        if(musculoComIdTreino == null){
                            musculoComIdTreino = m;
                        }
                    }

                    for (int i = 0; i < treino.getLstTodosMusculos().size(); i++){
                        if(treino.getLstTodosMusculos().get(i).size() > 0){
                            arrTipoMusculo.add(i);
                        }
                    }

                    alunoSelecionado.setArrTipoMusculos(arrTipoMusculo);
                    treino.setId(musculoComIdTreino.getIdTreino());

                    alunoSelecionado.setTreino(treino);

                    Treino t = TreinoDAO.getInstance(contextBase).retornaTreinoPorIdAtivo(treino.getId());
                    if(t != null && t.getProgressivo()){
                        treino.setProgressivo(true);
                        preencheTreinoProgressivo();
                    }

                    if (alunoSelecionado.getSexo().equals("M")) {
                        lstAlunos.add(alunoSelecionado);
                    } else {
                        lstAlunas.add(alunoSelecionado);
                    }

//                    try {
//                        if (arrTipoMusculo.size() > 0) {
//                            atualizaMusculoDaSemanaTreinado(arrTipoMusculo, lstMts);
//                        }
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//

                    atualizaListaTela();

                    try {
                        registraFrequenciaProgramaTreino(alunoSelecionado.getProgramaSelecionado());
                        ProgramaTreinoDAO.getInstance(contextBase).programaTreinado(alunoSelecionado.getProgramaSelecionado());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        Toast.makeText(contextBase, R.string.selecione_um_treino, Toast.LENGTH_LONG).show();
                        //selecionaProgramaDeTreinamento();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        builder.setNegativeButton(R.string.Cancelar, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        builder.show();
    }

    private boolean treinoProgressivoValido(final View view) {
        boolean retorno = true;
        String data = retornaSemanaEMes();
        Treino treino = TreinoDAO.getInstance(view.getContext()).retornaTreinoAtualDoAluno(alunoSelecionado);

        if(TreinoProgressivoDAO.getInstance(view.getContext()).retornaTreinoProgressivoDaSemana(treino, data) == null){
            String[] arrData = data.split("-");
            //arrData[0] = "1";
            arrData[1] = String.valueOf(Integer.valueOf(arrData[1]) + 1);

            data = arrData[0] + "-" + arrData[1];
        }

        if(treino.getProgressivo() && TreinoProgressivoDAO.getInstance(view.getContext()).retornaTreinoProgressivoDaSemana(treino, data) == null){
            retorno = false;
            AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
//            builder.setTitle("O treinamento progressivo expirou, deseja repetir o último?")
//                    .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            //replicarUltimoTreinoProgressivo
//                            retorno[0] = true;
//                        }
//                    })
//                    .setNegativeButton("Não", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//
//                        }
//                    });
            builder.setTitle("O treinamento progressivo expirou, deseja criar um novo?")
                    .setPositiveButton(R.string.Sim, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            FragmentManager fragmentManager = getFragmentManager();

                            fragmentManager.beginTransaction()
                                    .replace(R.id.container, Main.PlaceholderFragment.newInstance(Constantes.FRAGMENT_TREINOS), String.valueOf(Constantes.FRAGMENT_TREINOS))
                                    .addToBackStack(String.valueOf(Constantes.FRAGMENT_TREINOS))
                                    .commit();

                            dismiss();
                        }
                    })
                    .setNegativeButton(R.string.Nao, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });

            builder.show();
        }

        return retorno;
    }

    private void atualizaListaTela(){
        //Da um refresh no recyclerviewer dos alunos em treinamento
        RecyclerView recyclerView = (RecyclerView) getFragmentManager()
                .findFragmentByTag(Constantes.TAG_FRAGMENT_ALUNOS_EM_TREINAMENTO)
                .getView()
                .findViewById(R.id.listCardTreino1);

        recyclerView.getAdapter().notifyDataSetChanged();

        getDialog().dismiss();

    }

    private void preencheLista(View view){
        listaTreinoAluno = (ListView) view.findViewById(R.id.listaDeAluno);
        AdapterAluno adapter = new AdapterAluno(
                view.getContext(),
                R.layout.adapter_aluno_row_item,
                lstAlunosBusca);
        listaTreinoAluno.setAdapter(adapter);

    }

    private void selecionaTreinoDaSemana(final int tipoTreino) throws Exception {
        CharSequence csMusculos[] = Constantes.ARRAY_TIPO_MUSCULOS;
        final ArrayList<Integer> arrTipoMusculoRetorno = MusculoDAO.getInstance(contextBase).retornaTiposDeMusculosCadastradosPorAluno(alunoSelecionado);
        lstMts = MusculoTreinadoSemanaDAO.getInstance(contextBase).listar(alunoSelecionado);
        CharSequence[] csTipoMusculoRetorno = new String[arrTipoMusculoRetorno.size()];

        if(lstMts.size() == 0){
            gravaTipoMusculosNaTabelaMusculoSemana(arrTipoMusculoRetorno);
            lstMts = MusculoTreinadoSemanaDAO.getInstance(contextBase).listar(alunoSelecionado);
        }

        boolean bl[] = new boolean[arrTipoMusculoRetorno.size()];

        for (int i = 0; i < arrTipoMusculoRetorno.size(); i++){
            csTipoMusculoRetorno[i] = csMusculos[arrTipoMusculoRetorno.get(i)];
            //Controla o check dos tipos de musculo
            for (MusculoTreinadoSemana mj : lstMts) {
                if(mj.getTipoMusculo() == arrTipoMusculoRetorno.get(i)){
                    if(mj.getTreinou() == 1){
                        bl[i] = true;
                    }else{
                        bl[i] = false;
                    }
                }
            }
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(contextBase);
        builder.setTitle(R.string.Treinos_da_semana);

        final ArrayList<Integer> arrTipoMusculo = new ArrayList<>();

        builder.setMultiChoiceItems(csTipoMusculoRetorno, bl, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                if(isChecked){
                    arrTipoMusculo.add(arrTipoMusculoRetorno.get(which));
                }

                if(!isChecked){
                    arrTipoMusculo.remove(arrTipoMusculoRetorno.get(which));
                }
            }
        });

        builder.setPositiveButton(R.string.Confirmar, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(arrTipoMusculo.size() > 0) {
                    alunoSelecionado.setArrTipoMusculos(arrTipoMusculo);
                    alunoSelecionado.setTreino(TreinoDAO.getInstance(contextBase).buscarTreinoComMusculosFiltrados(alunoSelecionado, arrTipoMusculo, false, false));
                    alunoSelecionado.setTipoTreino(tipoTreino);

                    if(alunoSelecionado.getTreino().getProgressivo()){
                        preencheTreinoProgressivo();
                    }

                    if (alunoSelecionado.getSexo().equals("M")) {
                        lstAlunos.add(alunoSelecionado);
                    } else {
                        lstAlunas.add(alunoSelecionado);
                    }

                    try {
                        if (arrTipoMusculo.size() > 0) {
                            atualizaMusculoDaSemanaTreinado(arrTipoMusculo, lstMts);

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    atualizaListaTela();
                }else {
                    try {
                        Toast.makeText(getContext(), R.string.selecione_algum_musculo, Toast.LENGTH_LONG).show();
                        selecionaTreinoDaSemana(tipoTreino);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        builder.setNegativeButton(R.string.Cancelar, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();
    }

    private void preencheTreinoProgressivo() {
        String data = retornaSemanaEMes();

        if(TreinoProgressivoDAO.getInstance(contextBase).retornaTreinoProgressivoDaSemana(alunoSelecionado.getTreino(), data) == null){
            String[] arrData = data.split("-");
            //arrData[0] = "1";
            arrData[1] = String.valueOf(Integer.valueOf(arrData[1]) + 1);

            data = arrData[0] + "-" + arrData[1];
        }

        TreinoProgressivo tp = TreinoProgressivoDAO.getInstance(contextBase).retornaTreinoProgressivoDaSemana(alunoSelecionado.getTreino(), data);

        clonaTreinoOriginal();
        alunoSelecionado.setTreinoProgressivo(tp);
        atualizaTreinamentoSeguindoTreinoProgressivo(tp);
    }

    private void clonaTreinoOriginal() {
        Treino tClone = new Treino();

        ArrayList<ArrayList<Musculo>> lstTodosMusculos = tClone.getLstTodosMusculos();

        for(int i = 0; i < lstTodosMusculos.size(); i++){
            if(alunoSelecionado.getTreino().getLstMusculos(i).size() > 0){
                for ( Musculo m : alunoSelecionado.getTreino().getLstMusculos(i)) {
                    Musculo mClone = new Musculo(m.getId(),
                                                m.getOrdem(),
                                                m.getExercicio(),
                                                m.getSeries(),
                                                m.getTmpExecIni(),
                                                m.getTmpExecFim(),
                                                m.getIntervalos(),
                                                m.getRepeticoes(),
                                                m.getCarga(),
                                                m.getTipoMusculo(),
                                                m.getIdTreino(),
                                                m.getIdTreinoBase());
                    lstTodosMusculos.get(i).add(mClone);
                }
            }
        }

        alunoSelecionado.setTreinoClone(tClone);


    }

    private String retornaSemanaEMes(){
        Calendar ca = new GregorianCalendar();
        Calendar ca2 = new GregorianCalendar();
        Calendar ca3 = new GregorianCalendar();
        String semana = "";
        String mes = "";
        Boolean voltouMes = false;

        int semanaDoMes = ca.get(Calendar.WEEK_OF_MONTH);

        int ultimoDiaDoMes = ca2.getActualMaximum(Calendar.DAY_OF_MONTH);
        ca2.set(Calendar.DAY_OF_MONTH, ultimoDiaDoMes);
        int ultimaSemanaDoMes = ca2.get(Calendar.WEEK_OF_MONTH);

        String[] arrConfigSemanas = obterQuantidadeDeSemanasMes(ca3);

        if(semanaDoMes == 1){
            //volta para ultima semana do mes anterior
            if(Integer.parseInt(arrConfigSemanas[1]) == 0){
                ca.set(Calendar.MONTH,(ca.get(Calendar.MONTH) - 1));
                ca.set(Calendar.DAY_OF_MONTH, ca.getActualMaximum(Calendar.DAY_OF_MONTH));
                voltouMes = true;
            }
        }

        if(semanaDoMes == ultimaSemanaDoMes){
            //pula para primeira semana do mes posterior
            if(Integer.parseInt(arrConfigSemanas[2]) == 0){
                ca.set(Calendar.MONTH,(ca.get(Calendar.MONTH) + 1));
                ca.set(Calendar.DAY_OF_MONTH, 1);
            }
        }

//        mes = String.valueOf(ca.get(Calendar.MONTH) + 1);
        mes = String.valueOf(ca.get(Calendar.MONTH));
        semana = String.valueOf(ca.get(Calendar.WEEK_OF_MONTH));
        if(arrConfigSemanas[1].equals("0") && !voltouMes){
            semana = String.valueOf(ca.get(Calendar.WEEK_OF_MONTH) - 1);
        }
        String data = semana + "-" + mes;

        return data;
    }


    private String[] obterQuantidadeDeSemanasMes(Calendar ca) {
        // 0 - numero Semanas uteis
        // 1 - primeira semana valida
        // 2 - ultima semana valida

        String[] arrConfigSemanas = {"0", "1", "1"};

        int primeiroDiaDoMes = 1;
        int ultimoDiaDoMes = ca.getActualMaximum(Calendar.DAY_OF_MONTH);

        ca.set(Calendar.DAY_OF_MONTH, ultimoDiaDoMes);
        int semanasMes = ca.get(Calendar.WEEK_OF_MONTH);

        if(ca.get(Calendar.DAY_OF_WEEK) < 4){
            semanasMes--;
            arrConfigSemanas[2] = "0";
        }

        ca.set(Calendar.DAY_OF_MONTH, primeiroDiaDoMes);
        if(ca.get(Calendar.DAY_OF_WEEK) > 4){
            semanasMes--;
            arrConfigSemanas[1] = "0";
        }

        arrConfigSemanas[0] = String.valueOf(semanasMes);

        return arrConfigSemanas;
    }

    private void atualizaTreinamentoSeguindoTreinoProgressivo(TreinoProgressivo tp) {
        for (ArrayList<Musculo> lst : alunoSelecionado.getTreino().getLstTodosMusculos()) {
            for ( Musculo m : lst) {
                m.setCarga(retornaCargaAtualizada(m.getCarga(), tp.getLstSemanas().get(0).getPorcentagemCargas()));
                m.setRepeticoes(tp.getLstSemanas().get(0).getRepeticoes());
            }
        }
    }

    private String retornaCargaAtualizada(String carga, String porcentagemCargas) {
        String[] arrPorcCarga = porcentagemCargas.split("%");
        String arrRetorno = "";
        for (int i = 0; i <= arrPorcCarga.length -1; i++){
            if(i > 0){
                arrRetorno += "-";
            }
            if((i <= arrPorcCarga.length - 1)){
                try{
                    arrRetorno += String.valueOf(Integer.valueOf(carga)*Integer.valueOf(arrPorcCarga[i])/100);
                }catch (Exception e){
                    arrRetorno += "Erro!";
                }
            }else{
                arrRetorno += carga  ;
            }
        }

        return arrRetorno;
    }

    private boolean alunoRepetido(View view){
        boolean retorno = false;

        for ( Aluno a : lstAlunos) {
            if(a.getCPF().equals(alunoSelecionado.getCPF())){
                retorno = true;
                break;
            }
        }

        if(!retorno){
            for ( Aluno a : lstAlunas) {
                if(a.getCPF().equals(alunoSelecionado.getCPF())){
                    retorno = true;
                    break;
                }
            }
        }

        if(retorno){
            Toast.makeText(view.getContext(), "Aluno Repetido", Toast.LENGTH_LONG).show();
        }

        return retorno;
    }

    private void gravaTipoMusculosNaTabelaMusculoSemana(ArrayList<Integer> arrTipoMusculo) throws Exception {

        for(int i = 0; i < arrTipoMusculo.size(); i++){
            MusculoTreinadoSemana m = new MusculoTreinadoSemana();
            m.setAluno(alunoSelecionado);
            m.setTipoMusculo(arrTipoMusculo.get(i));
            m.setTreinou(0);
            MusculoTreinadoSemanaDAO.getInstance(contextBase).salvar(m, true);
        }
    }

    private void atualizaMusculoDaSemanaTreinado(ArrayList<Integer> arrMusculosSelecionados, ArrayList<MusculoTreinadoSemana> arrMusculosTreinadosBanco) throws Exception {
        int e = arrMusculosSelecionados.get(0);
        MusculoTreinadoSemana a = arrMusculosTreinadosBanco.get(0);

        registraFrequenciaMusculosTreinados(arrMusculosSelecionados);

        for (MusculoTreinadoSemana m : arrMusculosTreinadosBanco) {
            for ( int musculoSelecionado : arrMusculosSelecionados) {
                if(musculoSelecionado == m.getTipoMusculo()){
                    m.setTreinou(Constantes.BANCO_TRUE);
                    MusculoTreinadoSemanaDAO.getInstance(contextBase).alterar(m);
                }
            }
        }
    }

    private void registraFrequenciaMusculosTreinados(ArrayList<Integer> arrMusculosSelecionados) {
        ArrayList<String> lstTiposMusculos = new ArrayList<>();
        for (int m : arrMusculosSelecionados){
            lstTiposMusculos.add(String.valueOf(m));
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-d H:m");
        String dataAtual = sdf.format(System.currentTimeMillis());

        Frequencia f = new Frequencia();
        f.setData(dataAtual);
        f.setAluno(alunoSelecionado);
        f.setLstMusculosTreinados(lstTiposMusculos);

        try {
            FrequenciaDAO.getInstance(getContext()).salvar(f, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void registraFrequenciaProgramaTreino(ProgramaTreino pt) {
        ArrayList<String> lstOrdemProgramaTreino = new ArrayList<>();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-d H:m");
        String dataAtual = sdf.format(System.currentTimeMillis());
        lstOrdemProgramaTreino.add(pt.getNomePrograma() + " " + pt.getOrdem().toString());

        Frequencia f = new Frequencia();
        f.setData(dataAtual);
        f.setAluno(alunoSelecionado);
        f.setLstOrdemProgramaTreino(lstOrdemProgramaTreino);

        try {
            FrequenciaDAO.getInstance(getContext()).salvar(f, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
