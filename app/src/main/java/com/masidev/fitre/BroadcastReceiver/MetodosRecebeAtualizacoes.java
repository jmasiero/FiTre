package com.masidev.fitre.BroadcastReceiver;

import android.content.Context;
import android.content.SharedPreferences;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.masidev.fitre.constante.Constantes;
import com.masidev.fitre.data.dao.AcademiaDAO;
import com.masidev.fitre.data.dao.AlunoDAO;
import com.masidev.fitre.data.dao.FrequenciaDAO;
import com.masidev.fitre.data.dao.MusculoTreinadoSemanaDAO;
import com.masidev.fitre.data.dao.PersonalDAO;
import com.masidev.fitre.data.dao.ProgramaTreinoDAO;
import com.masidev.fitre.data.dao.TreinoBaseDAO;
import com.masidev.fitre.data.dao.TreinoDAO;
import com.masidev.fitre.data.dao.TreinoProgressivoDAO;
import com.masidev.fitre.data.entidade.Academia;
import com.masidev.fitre.data.entidade.Aluno;
import com.masidev.fitre.data.entidade.Frequencia;
import com.masidev.fitre.data.entidade.Musculo;
import com.masidev.fitre.data.entidade.MusculoTreinadoSemana;
import com.masidev.fitre.data.entidade.Personal;
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
public class MetodosRecebeAtualizacoes {
    private static Context contextBase;

    public static void
    enviaChamadaParaServidor(Context context, String acao){
        contextBase = context;
        enviaDados(acao);
    }

    public static void enviaDados(final String acao){
        JSONObject massaDados =  new JSONObject();

        try {
            massaDados.put("CREDENCIAIS", criarMassaDeCredenciais(acao));

            massaDados.put("ACAO", acao);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                Constantes.ACADEMIA_CONTROLLER,
                massaDados,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String acaoRetorno = response.getString("acao");
                            Integer ultIdAlt = response.getInt("ultIdAlt");

                            if(response.length() > 2){
                                try {
                                    switch (acaoRetorno){
                                        case Constantes.INSERT:
                                            retornoInsert(response);
                                            break;

                                        case Constantes.DELETE:
                                            retornoDelete(response);
                                            break;

                                        case Constantes.UPDATE:
                                            retornoUpdate(response);
                                            break;
                                    }

                                    AcademiaDAO.getInstance(contextBase).atualizarUltimoIdAlteradoAcad(ultIdAlt, acao);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

                            if(acao.equals(Constantes.UPDATE)) {
                                new MetodosEnviaAtualizacoes().enviaChamadaParaServidor(contextBase);
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

    public static JSONObject criarMassaDeCredenciais(String acao){
        SharedPreferences sp1 = contextBase.getSharedPreferences(Constantes.SHARED_PREFERENCES_LOGIN, Context.MODE_PRIVATE);

        Personal p = PersonalDAO.getInstance(contextBase).retornaPersonal(sp1.getString(Constantes.CPF_TREINADOR_LOGADO, ""));

        Academia a = AcademiaDAO.getInstance(contextBase).retornaAcademiaPorCnpj(sp1.getString(Constantes.CNPJ_ACADEMIA_LOGADA, ""));

        JSONObject retorno = new JSONObject();

        try {
            retorno.put("cnpjAcademia", a.getCnpj());
            retorno.put("cpfPersonal", p.getCPF());
            retorno.put("senha", p.getSenha());

            switch (acao){
                case Constantes.INSERT:
                    retorno.put("ultIdAlteracao", a.getUltimoIdInsert());
                    break;
                case Constantes.DELETE:
                    retorno.put("ultIdAlteracao", a.getUltimoIdDelete());
                    break;
                case Constantes.UPDATE:
                    retorno.put("ultIdAlteracao", a.getUltimoIdUpdate());
                    break;
            }

        }catch (Exception e){
            e.printStackTrace();
        }

        return  retorno;
    }

    public static void retornoInsert(JSONObject response) throws Exception{

        JSONArray arrAluno = response.getJSONArray("arrAluno");
        JSONArray arrPersonal = response.getJSONArray("arrPersonal");
        JSONArray arrMusculoTreinadoSemana = response.getJSONArray("arrMusculoTreinadoSemana");
        JSONArray arrTreino = response.getJSONArray("arrTreino");
        JSONArray arrTreinoBase = response.getJSONArray("arrTreinoBase");
        JSONArray arrTreinoProgressivo = response.getJSONArray("arrTreinoProgressivo");
        JSONArray arrProgramaTreino = response.getJSONArray("arrProgramaTreino");
        JSONArray arrFrequencia = response.getJSONArray("arrFrequencia");

        if (arrAluno.length() > 0){
            inserirAlunos(arrAluno);
        }

        if(arrPersonal.length() > 0){
            inserirPersonais(arrPersonal);
        }

        if(arrMusculoTreinadoSemana.length() > 0){
            inserirOuAlterarMusculoTreinadoSemana(arrMusculoTreinadoSemana);
        }

        if (arrTreino.length() > 0){
            inserirTreinos(arrTreino);
        }

        if(arrTreinoBase.length() > 0){
            inserirTreinosBase(arrTreinoBase);
        }

        if(arrTreinoProgressivo.length() > 0) {
            inserirTreinosProgressivos(arrTreinoProgressivo);
        }

        if(arrProgramaTreino.length() > 0) {
            inserirProgramasTreino(arrProgramaTreino);
        }

        if(arrFrequencia.length() > 0) {
            inserirFrequencias(arrFrequencia);
        }

    }

    public static void retornoDelete(JSONObject response) throws Exception{

        JSONArray arrAluno = response.getJSONArray("arrAluno");
        //JSONArray arrPersonal = response.getJSONArray("arrPersonal");
        //JSONArray arrMusculoTreinadoSemana = response.getJSONArray("arrMusculoTreinadoSemana");
        JSONArray arrTreino = response.getJSONArray("arrTreino");
        JSONArray arrTreinoBase = response.getJSONArray("arrTreinoBase");
        JSONArray arrTreinoProgressivo = response.getJSONArray("arrTreinoProgressivo");
        JSONArray arrProgramaTreino = response.getJSONArray("arrProgramaTreino");


        if (arrAluno.length() > 0){
            excluirAlunos(arrAluno);
        }

//            if(arrPersonal.length() > 0){
//                inserirPersonais(arrPersonal);
//            }

        if (arrTreino.length() > 0){
            excluirTreinos(arrTreino);
        }

        if(arrTreinoBase.length() > 0){
            excluirTreinosBase(arrTreinoBase);
        }

        if(arrTreinoProgressivo.length() > 0) {
            excluirTreinosProgressivos(arrTreinoProgressivo);
        }

        if(arrProgramaTreino.length() > 0) {
            excluirProgramaTreino(arrProgramaTreino);
        }
    }

    public static void retornoUpdate(JSONObject response) throws Exception{
        JSONArray arrAluno = response.getJSONArray("arrAluno");
        JSONArray arrPersonal = response.getJSONArray("arrPersonal");
        JSONArray arrMusculoTreinadoSemana = response.getJSONArray("arrMusculoTreinadoSemana");
        JSONArray arrTreino = response.getJSONArray("arrTreino");
        JSONArray arrTreinoBase = response.getJSONArray("arrTreinoBase");
        JSONArray arrTreinoProgressivo = response.getJSONArray("arrTreinoProgressivo");
        JSONArray arrProgramaTreino = response.getJSONArray("arrProgramaTreino");

        if(arrAluno.length() > 0){
            alterarAlunos(arrAluno);
        }

        if(arrPersonal.length() > 0){
            //alterarPersonais(arrPersonal);
        }

        if(arrMusculoTreinadoSemana.length() > 0){
            inserirOuAlterarMusculoTreinadoSemana(arrMusculoTreinadoSemana);
        }

        if(arrTreino.length() > 0){
            alterarTreinos(arrTreino);
        }

        if(arrTreinoBase.length() > 0){
            alterarTreinosBase(arrTreinoBase);
        }

        if(arrTreinoProgressivo.length() > 0){
            alterarTreinosProgressivos(arrTreinoProgressivo);
        }

        if(arrProgramaTreino.length() > 0){
            alterarProgramaTreino(arrProgramaTreino);
        }

    }

    private static void alterarTreinosProgressivos(JSONArray arrTreinoProgressivo) throws Exception {
        for(int i = 0; i < arrTreinoProgressivo.length(); i++) {
            JSONObject jsTP = arrTreinoProgressivo.getJSONObject(i);
            JSONArray jsArrTP = new JSONArray();
            jsArrTP.put(jsTP);

            excluirTreinosProgressivos(jsArrTP);
            inserirTreinosProgressivos(jsArrTP);
        }
    }

    private static void alterarProgramaTreino(JSONArray arrProgramaTreino) throws Exception {
//        JSONObject obj = arrProgramaTreino.getJSONObject(i);
//        ProgramaTreinoDAO.getInstance(contextBase).salvarJson(obj);

        for(int i = 0; i < arrProgramaTreino.length(); i++) {
            JSONObject jsPT = arrProgramaTreino.getJSONObject(i);
            JSONArray jsArrPT = new JSONArray();
            jsArrPT.put(jsPT);

            excluirProgramaTreino(jsArrPT);
            inserirProgramasTreino(jsArrPT);
        }
    }

    private static void excluirTreinosProgressivos(JSONArray jsArrTP) throws Exception {
        for(int i = 0; i < jsArrTP.length(); i++) {
            JSONObject jsTreinoProgressivo = jsArrTP.getJSONObject(i);
            JSONObject jsTreino = jsTreinoProgressivo.getJSONObject("dadosTreino");

            TreinoProgressivo treinoProgressivo = new TreinoProgressivo();
            treinoProgressivo.setMes(Integer.parseInt(jsTreinoProgressivo.getString("mes")));

            Treino treino = new Treino();
            treino.setTipoTreino(jsTreino.getString("tipoTreino"));
            treino.setDataInicio(jsTreino.getString("dataInicio"));
            treino.setAluno(new Aluno(jsTreino.getString("cpfAluno")));
            treino.setPersonal(new Personal(jsTreino.getString("cpfPersonal")));
            treino.setPersonalCriador(new Personal(jsTreino.getString("cpfPersonal")));

            treino = TreinoDAO.getInstance(contextBase).retornaTreinoComDadosServidor(treino);
            if(treino != null){
                TreinoProgressivoDAO.getInstance(contextBase).excluirTodosDoTreino(treino, false);
            }

        }
    }

    private static void excluirProgramaTreino(JSONArray arrProgramaTreino) throws Exception {
        for(int i = 0; i < arrProgramaTreino.length(); i++) {
            JSONObject jsDadosProgramaTreino = arrProgramaTreino.getJSONObject(i);

            ProgramaTreinoDAO.getInstance(contextBase).excluirProgramaTreinoComDadosServidor(jsDadosProgramaTreino.getString("cpfAluno"),
                    jsDadosProgramaTreino.getInt("ordem"),
                    jsDadosProgramaTreino.getString("nomePrograma"),
                    false
            );
        }
    }

    private static void alterarTreinosBase(JSONArray arrTreinoBase) throws Exception{
        for(int i = 0; i < arrTreinoBase.length(); i++) {
            JSONObject jsTreinoBase = arrTreinoBase.getJSONObject(i);
            JSONArray jsArrTreinoBase = new JSONArray();
            jsArrTreinoBase.put(jsTreinoBase);

            excluirTreinosBase(jsArrTreinoBase);
            inserirTreinosBase(jsArrTreinoBase);
        }
    }

    private static void alterarTreinos(JSONArray arrTreino) throws Exception{
        for(int i = 0; i < arrTreino.length(); i++) {
            JSONObject jsTreino = arrTreino.getJSONObject(i);
            JSONArray jsArrTreino = new JSONArray();
            jsArrTreino.put(jsTreino);

            excluirTreinos(jsArrTreino);
            inserirTreinos(jsArrTreino);
        }
    }

    private void alterarPersonais(JSONArray arrPersonal) throws Exception{
//        for(int i = 0; i < arrPersonal.length(); i++){
//            JSONObject obj = arrPersonal.getJSONObject(i);
//            Personal p = new Personal(obj.getString("cpf"),
//                                    obj.getString("nome"),
//                                    obj.getString("cref"),
//                                    obj.getString("senha"));
//
//            PersonalDAO.getInstance(contextBase).salvar(p, false);
//        }
    }

    private static void alterarAlunos(JSONArray arrAluno) throws Exception{
        for(int i = 0; i < arrAluno.length(); i++) {
            JSONObject jsAluno = arrAluno.getJSONObject(i);

            Aluno a = new Aluno(jsAluno.getString("cpf"),
                    jsAluno.getString("nome"),
                    jsAluno.getString("dataNascimento"),
                    jsAluno.getString("observacao"),
                    jsAluno.getString("sexo"));

            AlunoDAO.getInstance(contextBase).alterar(a, false);
        }
    }

    private static void excluirTreinos(JSONArray arrTreino) throws Exception {
        for(int i = 0; i < arrTreino.length(); i++) {
            JSONObject jsTreino = arrTreino.getJSONObject(i);

            Treino treino = new Treino();
            treino.setTipoTreino(jsTreino.getString("tipoTreino"));
            treino.setDataInicio(jsTreino.getString("dataInicio"));
            treino.setAluno(new Aluno(jsTreino.getString("cpfAluno")));
            treino.setPersonalCriador(new Personal(jsTreino.getString("cpfPersonal")));

            TreinoDAO.getInstance(contextBase).excluirTreinoComDadosServidor(treino);
        }
    }

    private static void excluirTreinosBase(JSONArray arrTreinoBase) throws Exception{
        for(int i = 0; i < arrTreinoBase.length(); i++) {
            JSONObject jsTreinoBase = arrTreinoBase.getJSONObject(i);

            TreinoBase treinoBase = new TreinoBase();
            treinoBase.setTipoTreino(jsTreinoBase.getString("tipoTreino"));
            treinoBase.setPersonal(new Personal(jsTreinoBase.getString("cpfPersonal")));

            TreinoBase tbRetorno = TreinoBaseDAO.getInstance(contextBase).retornaTreinoBasePorNomeEPersonal(treinoBase);

            if(tbRetorno != null){
                TreinoBaseDAO.getInstance(contextBase).excluir(tbRetorno, false);
            }
        }
    }

    private static void excluirAlunos(JSONArray arrAluno) throws Exception {
        for(int i = 0; i < arrAluno.length(); i++){
            String cpf = (String) arrAluno.get(i);
            Aluno a = new Aluno(cpf);

            AlunoDAO.getInstance(contextBase).excluir(a, false);
        }
    }

    private static void inserirOuAlterarMusculoTreinadoSemana(JSONArray arrMusculoTreinadoSemana) throws Exception {
        for(int i = 0; i < arrMusculoTreinadoSemana.length(); i++){
            JSONObject obj = arrMusculoTreinadoSemana.getJSONObject(i);
            MusculoTreinadoSemana mts = new MusculoTreinadoSemana();
            mts.setAluno(new Aluno(obj.getString("cpfAluno")));
            mts.setTipoMusculo(obj.getInt("tipoMusculo"));

            mts.setTreinou(obj.getInt("treinou"));

            MusculoTreinadoSemanaDAO.getInstance(contextBase).salvaOuAlteraMusculoTreinadoSemanaPorCpfAluno(mts);
        }
    }

    public static void inserirAlunos(JSONArray arrAluno) throws Exception {
        for(int i = 0; i < arrAluno.length(); i++){
            JSONObject obj = arrAluno.getJSONObject(i);
            Aluno a = new Aluno(obj.getString("cpf"),
                    obj.getString("nome"),
                    obj.getString("dataNascimento"),
                    obj.getString("observacao"),
                    obj.getString("sexo"));

            AlunoDAO.getInstance(contextBase).salvar(a, false);
        }
    }

    public static void inserirFrequencias(JSONArray arrFrequencia) throws Exception {
        for(int i = 0; i < arrFrequencia.length(); i++){
            JSONObject obj = arrFrequencia.getJSONObject(i);
            Frequencia f = new Frequencia();
            f.setAluno(new Aluno(obj.getString("cpfAluno")));
            f.setData(obj.getString("data"));

            ArrayList<String> lstMusculosTreinados = new ArrayList<>();
            if(!obj.getString("lstMusculosTreinados").equals("")){
                String[] arrMusculosTreinados = obj.getString("lstMusculosTreinados").split("-");
                for(String m : arrMusculosTreinados){
                    lstMusculosTreinados.add(m);
                }
            }
            f.setLstMusculosTreinados(lstMusculosTreinados);

            ArrayList<String> lstOrdemProgramaTreinado = new ArrayList<>();
            if(!obj.getString("lstOrdemProgramaTreinado").equals("")){
                String[] arrOrdemProgramaTreinado = obj.getString("lstOrdemProgramaTreinado").split("-");
                for(String m : arrOrdemProgramaTreinado){
                    lstOrdemProgramaTreinado.add(m);
                }
            }
            f.setLstOrdemProgramaTreino(lstOrdemProgramaTreinado);

            FrequenciaDAO.getInstance(contextBase).salvar(f, false);
        }
    }

    private static void inserirPersonais(JSONArray arrPersonal) throws Exception{
        for(int i = 0; i < arrPersonal.length(); i++){
            JSONObject obj = arrPersonal.getJSONObject(i);
            Personal p = new Personal(obj.getString("cpf"),
                    obj.getString("nome"),
                    obj.getString("cref"),
                    obj.getString("senha"));

            PersonalDAO.getInstance(contextBase).salvar(p, false);
        }
    }

    private static void inserirTreinos(JSONArray arrTreino) throws Exception{
        for(int i = 0; i < arrTreino.length(); i++){
            JSONObject obj = arrTreino.getJSONObject(i);
            TreinoDAO.getInstance(contextBase).salvarJson(obj);
        }
    }

    private static void inserirTreinosProgressivos(JSONArray arrTreinoProgressivo) throws Exception{
        for(int i = 0; i < arrTreinoProgressivo.length(); i++){
            JSONObject obj = arrTreinoProgressivo.getJSONObject(i);
            TreinoProgressivoDAO.getInstance(contextBase).salvarJson(obj, null);
        }
    }

    private static void inserirProgramasTreino(JSONArray arrProgramaTreino) throws Exception{
        for(int i = 0; i < arrProgramaTreino.length(); i++){
            JSONObject obj = arrProgramaTreino.getJSONObject(i);
            ProgramaTreinoDAO.getInstance(contextBase).salvarJson(obj);
        }
    }

    private static void inserirTreinosBase(JSONArray arrTreinoBase) throws Exception {
        for(int i = 0; i < arrTreinoBase.length(); i++) {
            JSONObject obj = arrTreinoBase.getJSONObject(i);
            //verifica se o treino base ja existe no banco
            if (TreinoBaseDAO.getInstance(contextBase).retornaTreinoBasePorNome(obj.getString("tipoTreino")) == null) {
                TreinoBase tb = new TreinoBase();
                tb.setTipoTreino(obj.getString("tipoTreino"));
                tb.setPersonal(new Personal(obj.getString("cpfPersonal")));

                JSONArray arrMusculos = obj.getJSONArray("musculos");

                //faço isso para ele retornar a lista de cada musculo com seu indice, basta preencher
                tb.setLstTodosMusculos(new ArrayList<ArrayList<Musculo>>());
                ArrayList<ArrayList<Musculo>> lstMusculos = tb.getLstTodosMusculos();

                for (int j = 0; j < arrMusculos.length(); j++) {
                    JSONObject objMusculo = arrMusculos.getJSONObject(j);
                    Musculo m = new Musculo();
                    m.setOrdem(objMusculo.getInt("ordem"));
                    m.setExercicio(objMusculo.getString("exercicio"));
                    m.setSeries(objMusculo.getInt("series"));
                    m.setTmpExecIni(objMusculo.getInt("tmpExecIni"));
                    m.setTmpExecFim(objMusculo.getInt("tmpExecFim"));
                    m.setIntervalos(objMusculo.getInt("intervalos"));
                    m.setRepeticoes(objMusculo.getString("repeticoes"));
                    m.setCarga(objMusculo.getString("cargas"));
                    m.setTipoMusculo(objMusculo.getInt("tipoMusculo"));

                    switch (m.getTipoMusculo()) {
                        case Constantes.BICEPS:
                            lstMusculos.get(Constantes.BICEPS).add(m);
                            break;

                        case Constantes.TRICEPS:
                            lstMusculos.get(Constantes.TRICEPS).add(m);
                            break;

                        case Constantes.PEITO:
                            lstMusculos.get(Constantes.PEITO).add(m);
                            break;

                        case Constantes.OMBRO:
                            lstMusculos.get(Constantes.OMBRO).add(m);
                            break;

                        case Constantes.COSTAS:
                            lstMusculos.get(Constantes.COSTAS).add(m);
                            break;

                        case Constantes.PERNA:
                            lstMusculos.get(Constantes.PERNA).add(m);
                            break;

                        case Constantes.GLUTEOS:
                            lstMusculos.get(Constantes.GLUTEOS).add(m);
                            break;

                        case Constantes.ANTEBRACO:
                            lstMusculos.get(Constantes.ANTEBRACO).add(m);
                            break;

                        case Constantes.ABDOMINAIS:
                            lstMusculos.get(Constantes.ABDOMINAIS).add(m);
                            break;
                    }
                }

                TreinoBaseDAO.getInstance(contextBase).salvar(tb, false);
            }
        }
    }
}
