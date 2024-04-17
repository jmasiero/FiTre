package com.masidev.fitre.BroadcastReceiver;

import android.content.Context;
import android.content.SharedPreferences;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.masidev.fitre.constante.Constantes;
import com.masidev.fitre.data.contract.AlunoContract;
import com.masidev.fitre.data.contract.FrequenciaContract;
import com.masidev.fitre.data.contract.MusculoTreinadoSemanaContract;
import com.masidev.fitre.data.contract.PersonalContract;
import com.masidev.fitre.data.contract.ProgramaTreinoContract;
import com.masidev.fitre.data.contract.TreinoBaseContract;
import com.masidev.fitre.data.contract.TreinoContract;
import com.masidev.fitre.data.contract.TreinoProgressivoContract;
import com.masidev.fitre.data.dao.AcademiaDAO;
import com.masidev.fitre.data.dao.AlteracaoDAO;
import com.masidev.fitre.data.dao.AlunoDAO;
import com.masidev.fitre.data.dao.FrequenciaDAO;
import com.masidev.fitre.data.dao.MusculoTreinadoSemanaDAO;
import com.masidev.fitre.data.dao.PersonalAcademiaDAO;
import com.masidev.fitre.data.dao.PersonalDAO;
import com.masidev.fitre.data.dao.ProgramaMusculoDAO;
import com.masidev.fitre.data.dao.ProgramaTreinoDAO;
import com.masidev.fitre.data.dao.TreinoBaseDAO;
import com.masidev.fitre.data.dao.TreinoDAO;
import com.masidev.fitre.data.dao.TreinoProgressivoDAO;
import com.masidev.fitre.data.entidade.Alteracao;
import com.masidev.fitre.data.entidade.Aluno;
import com.masidev.fitre.data.entidade.Frequencia;
import com.masidev.fitre.data.entidade.Musculo;
import com.masidev.fitre.data.entidade.MusculoTreinadoSemana;
import com.masidev.fitre.data.entidade.Personal;
import com.masidev.fitre.data.entidade.PersonalAcademia;
import com.masidev.fitre.data.entidade.ProgramaTreino;
import com.masidev.fitre.data.entidade.SemanaTreinoProgressivo;
import com.masidev.fitre.data.entidade.Treino;
import com.masidev.fitre.data.entidade.TreinoBase;
import com.masidev.fitre.data.entidade.TreinoProgressivo;
import com.masidev.fitre.volley.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by jmasiero on 02/05/16.
 */
public class MetodosEnviaAtualizacoes {
    private static Context contextBase;

    public static void enviaChamadaParaServidor(Context context) {
        contextBase = context;

        ArrayList<Alteracao> lstInsercoes = AlteracaoDAO.getInstance(context).listarPorAcao(Constantes.INSERT);
        ArrayList<Alteracao> lstAtualizacoes = AlteracaoDAO.getInstance(context).listarPorAcao(Constantes.UPDATE);
        ArrayList<Alteracao> lstDeletes = AlteracaoDAO.getInstance(context).listarPorAcao(Constantes.DELETE);

        JSONObject massaDadosInsert = new JSONObject();
        JSONObject massaDadosUpdate = new JSONObject();
        JSONObject massaDadosDelete = new JSONObject();

        if(existeAlteracao(lstInsercoes, lstAtualizacoes, lstDeletes)) {

            try {
                massaDadosInsert.put("INSERT", criarMassaDeDados(lstInsercoes));
                massaDadosUpdate.put("UPDATE", criarMassaDeDados(lstAtualizacoes));
                massaDadosDelete.put("DELETE", criarMassaDeDadosDelete(lstDeletes));

            } catch (Exception e) {
                e.printStackTrace();
            }

            if(lstInsercoes.size() > 0){
                enviaDados(massaDadosInsert, Constantes.INSERT);
            }

            if(lstAtualizacoes.size() > 0) {
                enviaDados(massaDadosUpdate, Constantes.UPDATE);
            }

            if(lstDeletes.size() > 0) {
                enviaDados(massaDadosDelete, Constantes.DELETE);
            }
        }
    }

//    public static boolean isOnline(Context context) {
//        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
//        NetworkInfo netInfo = cm.getActiveNetworkInfo();
//        if (netInfo != null && netInfo.isConnected())
//            return true;
//        else
//            return false;
//    }

    public static boolean existeAlteracao(ArrayList<Alteracao> arrInsert, ArrayList<Alteracao> arrUpdate, ArrayList<Alteracao> arrDelete){
        Boolean retorno = false;

        if(arrInsert.size() > 0){
            retorno = true;
        }

        if(!retorno && arrUpdate.size() > 0){
            retorno = true;
        }

        if(!retorno && arrDelete.size() > 0){
            retorno = true;
        }

        return retorno;
    }

    public static void enviaDados(JSONObject massaDados, final String acao){
        try {
            massaDados.put("CREDENCIAIS", criarMassaDeCredenciais());

            massaDados.put("ACAO", acao);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                Constantes.NUVEM_CONTROLLER,
                massaDados,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Boolean retorno = response.getBoolean("resposta");
                            Integer ultIdAlt = response.getInt("ultIdAlt");
                            if(retorno){
                                try {
                                    AlteracaoDAO.getInstance(contextBase).inativar(acao);
                                    AcademiaDAO.getInstance(contextBase).atualizarUltimoIdAlteradoAcad(ultIdAlt, acao);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(contextBase, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }
        );

        VolleySingleton.getInstance(contextBase).addToRequestQueue(request);
    }

//    private void recebeAtualizacoes() {
//        new MetodosRecebeAtualizacoes().enviaChamadaParaServidor(contextBase, Constantes.INSERT);
//        new MetodosRecebeAtualizacoes().enviaChamadaParaServidor(contextBase, Constantes.DELETE);
//        new MetodosRecebeAtualizacoes().enviaChamadaParaServidor(contextBase, Constantes.UPDATE);
//    }

    public static JSONObject criarMassaDeCredenciais(){
        //recupera e seta o personal logado
        SharedPreferences sp1 = contextBase.getSharedPreferences(Constantes.SHARED_PREFERENCES_LOGIN, Context.MODE_PRIVATE);

        Personal p = PersonalDAO.getInstance(contextBase).retornaPersonal(sp1.getString(Constantes.CPF_TREINADOR_LOGADO, ""));
        String cnpjAcademia = sp1.getString(Constantes.CNPJ_ACADEMIA_LOGADA, "");

        JSONObject retorno = new JSONObject();

        try {
            retorno.put("cnpjAcademia", cnpjAcademia);
            retorno.put("cpfPersonal", p.getCPF());
            retorno.put("senha", p.getSenha());
        }catch (Exception e){
            e.printStackTrace();
        }

        return  retorno;
    }

    public static JSONObject criarMassaDeDadosDelete(ArrayList<Alteracao> lst){
        JSONArray jsonArrAluno = new JSONArray();//Buscar no servidor pelo cpf
        JSONArray jsonArrTreinoBase = new JSONArray();
        JSONArray jsonArrTreino = new JSONArray();
        JSONArray jsonArrTreinoProgressivo = new JSONArray();
        JSONArray jsonArrProgramaTreino = new JSONArray();

        if(lst.size() > 0){
            for ( Alteracao a : lst ){
                switch (a.getTabela().getNome()){
                    case AlunoContract.TABLE_NAME:
                        jsonArrAluno.put(retornaJasonAlunoExcluido(a.getChave()));
                        break;

                    case TreinoContract.TABLE_NAME:
                        jsonArrTreino.put(retornaJsonTreino(a.getChave()));
                        break;

                    case TreinoBaseContract.TABLE_NAME:
                        jsonArrTreinoBase.put(retornaJsonTreinoBase(a.getChave()));
                        break;

                    case TreinoProgressivoContract.TABLE_NAME:
                        jsonArrTreinoProgressivo.put(retornaJsonTreinoProgressivoApagado(a.getChave()));
                        break;

                    case ProgramaTreinoContract.TABLE_NAME:
                        jsonArrProgramaTreino.put(retornaJsonProgramaTreinoExcluido(a.getChave()));
                        break;
                }
            }
        }

        JSONObject retorno = new JSONObject();

        try {
            if(jsonArrAluno.length() > 0){
                retorno.put("arrAluno", jsonArrAluno);
            }

            if(jsonArrTreino.length() > 0){
                retorno.put("arrTreino", jsonArrTreino);
            }

            if(jsonArrTreinoBase.length() > 0){
                retorno.put("arrTreinoBase", jsonArrTreinoBase);
            }

            if(jsonArrTreinoProgressivo.length() > 0){
                retorno.put("arrTreinoProgressivo", jsonArrTreinoProgressivo);
            }

            if(jsonArrProgramaTreino.length() > 0){
                retorno.put("arrProgramaTreino", jsonArrProgramaTreino);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return retorno;

    }

    private static JSONObject retornaJsonProgramaTreinoExcluido(String chave) {
        //0 - id , 1 - cpfAluno, 2 - ordem, 3 - nomePrograma
        JSONObject jsPt = new JSONObject();

        String[] arrTP = chave.split("/");

        try{
            jsPt.put("cpfAluno", arrTP[1]);
            jsPt.put("ordem", arrTP[2]);
            jsPt.put("nomePrograma", arrTP[3]);
        }catch (Exception e){
            e.printStackTrace();
        }

        return jsPt;
    }

    private static JSONObject retornaJsonTreinoProgressivoApagado(String chave) {
        JSONObject jsTp = new JSONObject();
        JSONObject jsTreino = new JSONObject();
        //0 - mes , 1 - cpfAluno, 2 - cpfPersonal, 3 - tipoTreino, 4 - dataInicio
        String[] arrTP = chave.split("/");

        try{
            jsTp.put("mes", arrTP[0]);
            jsTreino.put("cpfAluno", arrTP[1]);
            jsTreino.put("cpfPersonal", arrTP[2]);
            jsTreino.put("tipoTreino", arrTP[3]);
            jsTreino.put("dataInicio", arrTP[4]);

            jsTp.put("dadosTreino", jsTreino);
        }catch (Exception e){
            e.printStackTrace();
        }

        return jsTp;
    }

    private static JSONObject retornaJasonAlunoExcluido(String cpf){
        JSONObject js = new JSONObject();
        try{
            js.put("cpf", cpf);
        }catch (Exception e){
            e.printStackTrace();
        }

        return js;
    }

    private JSONObject retornaJasonPersonalExcluido(String cpf){
        Personal p = new Personal(cpf);

        JSONObject js = new JSONObject();
        try{
            js.put("cpf", cpf);
        }catch (Exception e){
            e.printStackTrace();
        }

        return js;
    }

    private static JSONObject retornaJsonTreino(String chave) {
        // 0 - t.getId() + "/" 1 - t.getTipoTreino() "/" 2 - t.getDataInicio() "/" 3 - t.getAluno().getCPF() "/" 4 - t.getAtual() "/" 5 - t.getPersonalCriador().getCPF());
        String[] arrChave = chave.split("/");
        int atual = 0;
        Treino treino = new Treino();
        treino.setTipoTreino(arrChave[1]);
        treino.setDataInicio(arrChave[2]);
        treino.setAluno(new Aluno(arrChave[3]));
        treino.setPersonalCriador(new Personal(arrChave[5]));
        if(arrChave[4].equals("true")){
            atual = 1;
        }else{
            atual = 0;
        }

        JSONObject js = new JSONObject();

        try {
            js.put("tipoTreino", treino.getTipoTreino());
            js.put("dataInicio", treino.getDataInicio());
            js.put("cpfAluno", treino.getAluno().getCPF());
            js.put("cpfPersonal", treino.getPersonalCriador().getCPF());
            js.put("atual", atual);

        }catch (Exception e){
            e.printStackTrace();
        }

        return js;
    }

    private static JSONObject retornaJsonTreinoBase(String chave) {
        // 0 - tb.getId() + "/" + 1 - tb.getTipoTreino() "/" 2 - tb.getPersonal().getCPF());
        String[] arrChave = chave.split("/");

        TreinoBase treinoBase = new TreinoBase();

        treinoBase.setTipoTreino(arrChave[1]);
        treinoBase.setPersonal(new Personal(arrChave[2]));

        JSONObject js = new JSONObject();
        JSONArray jsonArrayMusculos = new JSONArray();

        try {
            js.put("tipoTreino", treinoBase.getTipoTreino());
            js.put("cpfPersonal", treinoBase.getPersonal().getCPF());

        }catch (Exception e){
            e.printStackTrace();
        }

        return js;
    }

    public static JSONObject criarMassaDeDados(ArrayList<Alteracao> lst){
        JSONArray jsonArrAluno = new JSONArray();//Buscar no servidor pelo cpf
        JSONArray jsonArrPersonal = new JSONArray();//Buscar no servidor pelo cpf
        JSONArray jsonArrMusculoTreinadoSemana = new JSONArray();//buscar no servidor por cpfAluno junto com tipoMucsulo
        JSONArray jsonArrTreinoBase = new JSONArray();
        JSONArray jsonArrTreino = new JSONArray();
        JSONArray jsonArrTreinoProgressivo = new JSONArray();
        JSONArray jsonArrProgramaTreino = new JSONArray();
        JSONArray jsonArrFrequencia = new JSONArray();

        JSONObject retorno;

        if(lst.size() > 0){
            for ( Alteracao a : lst ){
                retorno = null;
                switch (a.getTabela().getNome()){
                    case AlunoContract.TABLE_NAME:
                        jsonArrAluno.put(retornaJsonAluno(a.getChave()));
                        break;
                    case PersonalContract.TABLE_NAME:
                        jsonArrPersonal.put(retornaJsonPersonal(a.getChave()));
                        break;
                    case MusculoTreinadoSemanaContract.TABLE_NAME:
                        jsonArrMusculoTreinadoSemana.put(retornaJsonMusculoTreinadoSemana(Integer.parseInt(a.getChave())));
                        break;
                    case TreinoContract.TABLE_NAME:
                        jsonArrTreino.put(retornaJsonTreinoComMusculos(Integer.parseInt(a.getChave()), a.getAcao()));
                        break;
                    case TreinoBaseContract.TABLE_NAME:
                        jsonArrTreinoBase.put(retornaJsonTreinoBaseComMusculos(Integer.parseInt(a.getChave())));
                        break;
                    case TreinoProgressivoContract.TABLE_NAME:
                        retorno = retornaJsonTreinoProgressivo(Integer.parseInt(a.getChave()));
                        if(retorno.length() > 0) {
                            jsonArrTreinoProgressivo.put(retorno);
                        }
                        break;
                    case ProgramaTreinoContract.TABLE_NAME:
                        retorno = retornaJsonProgramaTreino(Integer.parseInt(a.getChave()));
                        if(retorno.length() > 0) {
                            jsonArrProgramaTreino.put(retorno);
                        }
                        break;
                    case FrequenciaContract.TABLE_NAME:
                        retorno = retornaJsonFrequencia(a.getChave());
                        if(retorno != null){
                            jsonArrFrequencia.put(retorno);
                        }
                        break;

                }
            }
        }

        retorno = new JSONObject();

        try {
            if(jsonArrPersonal.length() > 0){
                retorno.put("arrPersonal", jsonArrPersonal);
            }

            if(jsonArrAluno.length() > 0){
                retorno.put("arrAluno", jsonArrAluno);
            }

            if(jsonArrMusculoTreinadoSemana.length() > 0){
                retorno.put("arrMusculoTreinadoSemana", jsonArrMusculoTreinadoSemana);
            }


            if(jsonArrTreino.length() > 0){
                retorno.put("arrTreino", jsonArrTreino);
            }

            if(jsonArrTreinoBase.length() > 0){
                retorno.put("arrTreinoBase", jsonArrTreinoBase);
            }

            if(jsonArrTreinoProgressivo.length() > 0){
                retorno.put("arrTreinoProgressivo", jsonArrTreinoProgressivo);
            }

            if(jsonArrProgramaTreino.length() > 0){
                retorno.put("arrProgramaTreino", jsonArrProgramaTreino);
            }

            if(jsonArrFrequencia.length() > 0){
                retorno.put("arrFrequencia", jsonArrFrequencia);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return retorno;
    }

    private static JSONObject retornaJsonProgramaTreino(int id) {
        ProgramaTreino pt = null;
        try {
            pt = ProgramaTreinoDAO.getInstance(contextBase).retornarProgramaTreinoPorId(id);
        } catch (Exception e) {
            e.printStackTrace();
        }

        JSONObject jsPt = new JSONObject();
        if(pt != null) {
            try {
                int misto = 0;
                if(pt.getMisto()){
                    misto = 1;
                }

                int simples = 0;
                if(pt.getSimples()){
                    simples = 1;
                }

                int treinou = 0;
                if(pt.getTreinou()){
                    treinou = 1;
                }

                jsPt.put("misto", misto);
                jsPt.put("simples", simples);
                jsPt.put("treinou", treinou);
                jsPt.put("nomePrograma", pt.getNomePrograma());
                jsPt.put("ordem", pt.getOrdem());
                jsPt.put("cpfAluno", pt.getAluno().getCPF());

                JSONArray jsonArrayExercicios = new JSONArray();
                jsonArrayExercicios.put(ProgramaMusculoDAO.getInstance(contextBase).retornaJsonExercicios(pt));

                jsPt.put("arrExercicios", jsonArrayExercicios);

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        return jsPt;
    }

    private static JSONObject retornaJsonAluno(String cpf) {
        Aluno a = AlunoDAO.getInstance(contextBase).getAluno(cpf);

        JSONObject js = new JSONObject();
        try {
            js.put("cpf", a.getCPF());
            js.put("nome", a.getNome());
            js.put("dtNascimento", a.getDtNascimento());
            js.put("observacao", a.getObservacao());
            //js.put("foto", a.getFoto());
            js.put("sexo", a.getSexo());

        }catch (Exception e){
            e.printStackTrace();
        }

        return js;
    }

    private static JSONObject retornaJsonFrequencia(String id) {
        Frequencia f = FrequenciaDAO.getInstance(contextBase).buscarFrequencia(Integer.parseInt(id));
        JSONObject js = null;
        if(f != null){
            js = new JSONObject();
            try {
                js.put("id", f.getId());
                js.put("data", f.getData());
                js.put("lstMusculosTreinados", f.retornaListaEmStringMusculosTreinados());
                js.put("lstOrdemProgramaTreinado", f.retornaListaEmStringOrdemProgramaTreino());
                js.put("cpfAluno", f.getAluno().getCPF());

            }catch (Exception e){
                e.printStackTrace();
            }
        }

        return js;
    }

    private static JSONObject retornaJsonPersonal(String cpf) {
        Personal p = PersonalDAO.getInstance(contextBase).retornaPersonal(cpf);

        PersonalAcademia pa = PersonalAcademiaDAO.getInstance(contextBase).retornaPersonalAcademia(cpf);

        JSONObject js = new JSONObject();
        try {
            js.put("cpf", p.getCPF());
            js.put("nome", p.getNome());
            js.put("cref", p.getCREF());
            js.put("senha", p.getSenha());
            js.put("cnpjAcademia", pa.getCnpjAcademia());

        }catch (Exception e){
            e.printStackTrace();
        }

        return js;
    }

    private static JSONObject retornaJsonMusculoTreinadoSemana(Integer id) {
        MusculoTreinadoSemana mts = MusculoTreinadoSemanaDAO.getInstance(contextBase).retornaMusculoTreinadoSemanaPorId(id);

        JSONObject js = new JSONObject();
        try {
            js.put("id", mts.getId());
            js.put("tipoMusculo", mts.getTipoMusculo());
            js.put("treinou", mts.getTreinou());
            js.put("cpfAluno", mts.getAluno().getCPF());

        }catch (Exception e){
            e.printStackTrace();
        }

        return js;
    }

    private static JSONObject retornaJsonTreinoComMusculos(Integer id, String acao) {
        Treino treino = TreinoDAO.getInstance(contextBase).retornaTreinoComMusculos(id);
        JSONObject js = new JSONObject();

        if(treino != null) {
            int atual = 0;
            int progressivo = 0;
            JSONArray jsonArrayMusculos = new JSONArray();
            JSONArray jsonArrayTreinoProgressivo = new JSONArray();
            JSONArray jsonArrayProgramaTreino = new JSONArray();

            try {
                js.put("id", treino.getId());
                js.put("tipoTreino", treino.getTipoTreino());
                js.put("pesoInicio", treino.getPesoInicio());
                js.put("pesoIdeal", treino.getPesoIdeal());
                js.put("dataInicio", treino.getDataInicio());
                js.put("dataFim", treino.getDataFim());
                js.put("objetivo", treino.getObjetivo());
                js.put("anotacoes", treino.getAnotacoes());
                js.put("cpfAluno", treino.getAluno().getCPF());
                js.put("cpfPersonal", treino.getPersonalCriador().getCPF());
                if (treino.getAtual()) {
                    atual = 1;
                }
                js.put("atual", atual);

                if (treino.getProgressivo()) {
                    progressivo = 1;
                }
                js.put("progressivo", progressivo);

                for (ArrayList<Musculo> lstTipoMusculo : treino.getLstTodosMusculos()) {
                    for (Musculo m : lstTipoMusculo) {
                        JSONObject jsMusculo = new JSONObject();

                        jsMusculo.put("id", m.getId());
                        jsMusculo.put("ordem", m.getOrdem());
                        jsMusculo.put("exercicio", m.getExercicio());
                        jsMusculo.put("series", m.getSeries());
                        jsMusculo.put("tmpExecIni", m.getTmpExecIni());
                        jsMusculo.put("tmpExecFim", m.getTmpExecFim());
                        jsMusculo.put("intervalos", m.getIntervalos());
                        jsMusculo.put("repeticoes", m.getRepeticoes());
                        jsMusculo.put("cargas", m.getCarga());
                        jsMusculo.put("tipoMusculo", m.getTipoMusculo());
                        jsMusculo.put("idTreino", m.getIdTreino());
                        jsMusculo.put("idTreinoBase", m.getIdTreinoBase());

                        jsonArrayMusculos.put(jsMusculo);
                    }
                }

                if (treino.getProgressivo() && acao.equals(Constantes.UPDATE)) {
                    ArrayList<TreinoProgressivo> lstTp = TreinoProgressivoDAO.getInstance(contextBase).retornarTreinoProgressivo(treino);
                    for (TreinoProgressivo tp : lstTp) {
                        JSONArray jsonArraySemanaTreinoProgressivo = new JSONArray();
                        JSONObject jsTreinoProgressivo = new JSONObject();

                        jsTreinoProgressivo.put("mes", tp.getMes());

                        for (SemanaTreinoProgressivo semana : tp.getLstSemanas()) {
                            JSONObject jsSemana = new JSONObject();
                            jsSemana.put("repeticoes", semana.getRepeticoes());
                            jsSemana.put("semana", semana.getSemana());
                            jsSemana.put("porcCargas", semana.getPorcentagemCargas());

                            jsonArraySemanaTreinoProgressivo.put(jsSemana);
                        }

                        jsTreinoProgressivo.put("lstSemanas", jsonArraySemanaTreinoProgressivo);
                        jsonArrayTreinoProgressivo.put(jsTreinoProgressivo);
                    }

                    js.put("lstTreinoProgressivo", jsonArrayTreinoProgressivo);
                }

                if (acao.equals(Constantes.UPDATE)) {
                    ArrayList<ProgramaTreino> lstPT = ProgramaTreinoDAO.getInstance(contextBase).retornarProgramasDoAluno(treino.getAluno());

                    if (lstPT.size() > 0) {
                        for (ProgramaTreino pt : lstPT) {
                            JSONObject jsPt = new JSONObject();
                            if (pt != null) {
                                try {
                                    int misto = 0;
                                    if (pt.getMisto()) {
                                        misto = 1;
                                    }

                                    int simples = 0;
                                    if (pt.getSimples()) {
                                        simples = 1;
                                    }

                                    int treinou = 0;
                                    if (pt.getTreinou()) {
                                        treinou = 1;
                                    }

                                    jsPt.put("misto", misto);
                                    jsPt.put("simples", simples);
                                    jsPt.put("treinou", treinou);
                                    jsPt.put("nomePrograma", pt.getNomePrograma());
                                    jsPt.put("ordem", pt.getOrdem());
                                    jsPt.put("cpfAluno", pt.getAluno().getCPF());

                                    JSONArray jsonArrayExercicios = new JSONArray();
                                    jsonArrayExercicios.put(ProgramaMusculoDAO.getInstance(contextBase).retornaJsonExercicios(pt));

                                    jsPt.put("arrExercicios", jsonArrayExercicios);
                                    jsonArrayProgramaTreino.put(jsPt);
                                    //"lstProgramaTreino"
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            }
                        }
                    }

                    js.put("lstProgramaTreino", jsonArrayProgramaTreino);
                }

                js.put("musculos", jsonArrayMusculos);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return js;
    }

    private static JSONObject retornaJsonTreinoBaseComMusculos(Integer id) {
        TreinoBase treinoBase = TreinoBaseDAO.getInstance(contextBase).retornaTreinoBaseComMusculos(id);

        JSONObject js = new JSONObject();
        JSONArray jsonArrayMusculos = new JSONArray();

        try {
            js.put("id", treinoBase.getId());
            js.put("tipoTreino", treinoBase.getTipoTreino());
            js.put("cpfPersonal", treinoBase.getPersonal().getCPF());

            for (ArrayList<Musculo> lstTipoMusculo : treinoBase.getLstTodosMusculos()) {
                for (Musculo m : lstTipoMusculo) {
                    JSONObject jsMusculo = new JSONObject();

                    jsMusculo.put("id", m.getId());
                    jsMusculo.put("ordem", m.getOrdem());
                    jsMusculo.put("exercicio", m.getExercicio());
                    jsMusculo.put("series", m.getSeries());
                    jsMusculo.put("tmpExecIni", m.getTmpExecIni());
                    jsMusculo.put("tmpExecFim", m.getTmpExecFim());
                    jsMusculo.put("intervalos", m.getIntervalos());
                    jsMusculo.put("repeticoes", m.getRepeticoes());
                    jsMusculo.put("cargas", m.getCarga());
                    jsMusculo.put("tipoMusculo", m.getTipoMusculo());
                    jsMusculo.put("idTreino", m.getIdTreino());
                    jsMusculo.put("idTreinoBase", m.getIdTreinoBase());

                    jsonArrayMusculos.put(jsMusculo);
                }
            }

            js.put("musculos", jsonArrayMusculos);

        }catch (Exception e){
            e.printStackTrace();
        }

        return js;
    }

    private static JSONObject retornaJsonTreinoProgressivo(Integer id) {

        TreinoProgressivo tp = TreinoProgressivoDAO.getInstance(contextBase).retornarTreinoProgressivoPorId(id);
        JSONObject jsTp = new JSONObject();
        if(tp != null) {
            Treino treino = TreinoDAO.getInstance(contextBase).retornaTreinoPorIdAtivo(tp.getIdTreino());

            JSONArray jsonArraySemanas = new JSONArray();
            JSONObject jsTreino = new JSONObject();

            try {
                jsTp.put("mes", tp.getMes());
                jsTreino.put("cpfAluno", treino.getAluno().getCPF());
                jsTreino.put("cpfPersonal", treino.getPersonal().getCPF());
                jsTreino.put("tipoTreino", treino.getTipoTreino());
                jsTreino.put("dataInicio", treino.getDataInicio());

                for (SemanaTreinoProgressivo semana : tp.getLstSemanas()) {
                    JSONObject jsSemana = new JSONObject();

                    jsSemana.put("id", semana.getId());
                    jsSemana.put("semana", semana.getSemana());
                    jsSemana.put("porcCargas", semana.getPorcentagemCargas());
                    jsSemana.put("repeticoes", semana.getRepeticoes());

                    jsonArraySemanas.put(jsSemana);
                }
                jsTp.put("dadosTreino", jsTreino);
                jsTp.put("lstSemanas", jsonArraySemanas);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return jsTp;
    }
}
