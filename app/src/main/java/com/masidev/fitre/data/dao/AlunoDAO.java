package com.masidev.fitre.data.dao;

import android.content.ContentValues;
import android.content.Context;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.Gravity;
import android.widget.Toast;


import com.android.volley.Request;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import com.masidev.fitre.R;
import com.masidev.fitre.constante.Constantes;
import com.masidev.fitre.data.contract.AlunoContract;

import com.masidev.fitre.data.contract.TreinoContract;
import com.masidev.fitre.data.contract.TreinoProgressivoContract;
import com.masidev.fitre.data.db.DBHelper;

import com.masidev.fitre.data.entidade.Alteracao;
import com.masidev.fitre.data.entidade.Aluno;

import com.masidev.fitre.data.entidade.MusculoTreinadoSemana;
import com.masidev.fitre.data.entidade.Personal;
import com.masidev.fitre.data.entidade.Treino;
import com.masidev.fitre.volley.VolleySingleton;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by jmasiero on 22/09/15.
 */
public class AlunoDAO {
    private static AlunoDAO instance;
    private static Context contextBase;
    private SQLiteDatabase db;

    public static AlunoDAO getInstance(Context context){
        if(instance == null){
            instance = new AlunoDAO(context.getApplicationContext());
        }

        contextBase = context;

        return instance;
    }

    private AlunoDAO(Context context){
        DBHelper dbHelper = DBHelper.getInstance(context);
        db = dbHelper.getWritableDatabase();
    }

    public long salvar(Aluno a, Boolean registra) throws Exception{
        long retorno = 0;

        if(!a.getNome().isEmpty() ||
                !a.getCPF().isEmpty() ||
                !a.getDtNascimento().isEmpty() ||
                !a.getObservacao().isEmpty() ||
                !a.getSexo().isEmpty())
        {
            try {
                ContentValues values = new ContentValues();
                values.put(AlunoContract.columns.CPF, a.getCPF());
                values.put(AlunoContract.columns.NOME, a.getNome());
                values.put(AlunoContract.columns.DTNASIMENTO, a.getDtNascimento());
                values.put(AlunoContract.columns.OBSERVACAO, a.getObservacao());
                values.put(AlunoContract.columns.SEXO, a.getSexo());

                retorno = db.insert(AlunoContract.TABLE_NAME, null, values);

                if(registra) {
                    registraAlteracoes(Constantes.INSERT, a.getCPF());
                }
            }catch (Exception e){
                e.printStackTrace();
                throw new Exception("id repetido");
            }
        }
        return retorno;
    }

    public long alterar(Aluno a, Boolean registra) throws Exception{
        long retorno = 0;

        if(!a.getNome().isEmpty() ||
                !a.getCPF().isEmpty() ||
                !a.getDtNascimento().isEmpty() ||
                !a.getObservacao().isEmpty() ||
                !a.getSexo().isEmpty())
        {
            try {
                ContentValues values = new ContentValues();
                values.put(AlunoContract.columns.NOME, a.getNome());
                values.put(AlunoContract.columns.DTNASIMENTO, a.getDtNascimento());
                values.put(AlunoContract.columns.OBSERVACAO, a.getObservacao());
                values.put(AlunoContract.columns.SEXO, a.getSexo());

                String whereClause = AlunoContract.columns.CPF+" = ?";
                String [] whereArgs = {a.getCPF()};

                retorno = db.update(AlunoContract.TABLE_NAME, values, whereClause, whereArgs);

                if(registra) {
                    registraAlteracoes(Constantes.UPDATE, a.getCPF());
                }
            }catch (Exception e){
                e.printStackTrace();
                throw new Exception("id repetido");
            }
        }
        return retorno;
    }

    public boolean excluir(Aluno a, Boolean registra) throws Exception{
        boolean retorno = false;

        if(!a.getCPF().isEmpty())
        {
            ArrayList<Integer> arrIdsTreinos = TreinoDAO.getInstance(contextBase).retornaIdsTreinosDoAluno(a);

            if(arrIdsTreinos.size() > 0){
                for (int id : arrIdsTreinos) {
                    Treino t = TreinoDAO.getInstance(contextBase).retornaTreinoPorId(id);
                    if(t != null) {
                        TreinoDAO.getInstance(contextBase).excluir(t, false, false);
                    }
                }
            }

            //apaga musculosTreinadosSemana do aluno
            MusculoTreinadoSemanaDAO.getInstance(contextBase).excluirMTSDoAluno(a);
            FrequenciaDAO.getInstance(contextBase).excluirHistoricoDeFrequencia(a);
            String whereClause = AlunoContract.columns.CPF+"= ?";
            String [] whereArgs = {a.getCPF()};

            try {
                db.delete(AlunoContract.TABLE_NAME, whereClause, whereArgs);
                removeDasAlteracoes(a.getCPF());
                if(registra) {
                    registraAlteracoes(Constantes.DELETE, a.getCPF());
                }
                retorno = true;
            }catch (Exception e){
                e.printStackTrace();
                throw new Exception("Ocorreu um erro na exclusão");
            }
        }
        return retorno;
    }

    public ArrayList<Aluno> listar(boolean spinner) {

        String[] columns = {
                AlunoContract.columns.CPF,
                AlunoContract.columns.NOME,
                AlunoContract.columns.OBSERVACAO,
                AlunoContract.columns.DTNASIMENTO,
                AlunoContract.columns.SEXO
        };

        ArrayList<Aluno> alunos = new ArrayList<>();

        if(spinner){
            alunos.add(new Aluno("", "Selecione...", "", "", ""));
        }

        try (Cursor c = db.query(AlunoContract.TABLE_NAME, columns, null, null, null, null, AlunoContract.columns.NOME)) {
            if (c.moveToFirst()) {
                do {
                    Aluno a = AlunoDAO.fromCursor(c);
                    alunos.add(a);
                } while (c.moveToNext());
            }

            return alunos;
        }

    }

    public ArrayList<Aluno> listarTodosFiltrada(String filtro) {

        String[] columns = {
                AlunoContract.columns.CPF,
                AlunoContract.columns.NOME,
                AlunoContract.columns.OBSERVACAO,
                AlunoContract.columns.DTNASIMENTO,
                AlunoContract.columns.SEXO
        };

        ArrayList<Aluno> alunos = new ArrayList<>();

        String[] whereArgs = {"%" + filtro + "%"};
        String whereClause = AlunoContract.columns.NOME + " like ? ";

        try (Cursor c = db.query(AlunoContract.TABLE_NAME, columns, whereClause, whereArgs, null, null, AlunoContract.columns.NOME)) {
            if (c.moveToFirst()) {
                do {
                    Aluno a = AlunoDAO.fromCursor(c);
                    alunos.add(a);
                } while (c.moveToNext());
            }

            return alunos;
        }

    }

    public ArrayList<Aluno> listarAlunosSemTreinoDeHoje(String dataAtual, String filtro) {
        ArrayList<Aluno> lstAlunos = listarTodosFiltrada(filtro);
        ArrayList<Aluno> lstAlunosComTreinoHoje = retornaAlunosComTreinoDeHoje(dataAtual, filtro);

        //melhorar desempenho depois

        for (int i = 0; i < lstAlunosComTreinoHoje.size(); i++) {
            for (int j = 0; j < lstAlunos.size(); j++) {
                if(lstAlunos.get(j).getCPF().equals(lstAlunosComTreinoHoje.get(i).getCPF())){
                    lstAlunos.remove(j);
                    break;
                }
            }
        }

        return  lstAlunos;
    }

    public ArrayList<Aluno> retornaAlunosComTreinoDeHoje(String dataAtual, String filtro){
        String[] whereArgs = {};
        String whereClause = "";
        String filtroNome = "";

        if(!filtro.equals("")){
            filtro = " AND a.Nome like ?" ;
            whereArgs = new String[]{dataAtual, String.valueOf(Constantes.BANCO_TRUE), "%" + filtro + "%"};
        }else{
            whereArgs = new String[]{dataAtual, String.valueOf(Constantes.BANCO_TRUE)};
        }

        whereClause = " WHERE t." + TreinoContract.columns.DATA_INICIO + " = ? AND t.Atual = ? " + filtro;

        //whereArgs = new String[]{"2016-06-3", String.valueOf(Constantes.BANCO_TRUE)};

        ArrayList<Aluno> alunos = new ArrayList<>();

        // query manual
        String query = "SELECT a.Cpf," +
                " a.Nome," +
                " a.Observacao," +
                " a.DtNascimento," +
                " a.Sexo, " +
                " t." + TreinoContract.columns.DATA_INICIO +
                " FROM Aluno a " +
                " LEFT JOIN Treino t ON t.CpfAluno = a.Cpf " +
                whereClause +
                " GROUP BY a.Cpf " +
                " ORDER BY a.Nome";

        //fim query manual
        try (Cursor c = db.rawQuery(query, whereArgs)) {
            if (c.moveToFirst()) {
                do {
                    Aluno a = AlunoDAO.fromCursor(c);
                    alunos.add(a);
                } while (c.moveToNext());
            }

            return alunos;
        }
    }

    public ArrayList<Aluno> listaFiltrada(String Nome, boolean semTreino, boolean somenteTreinoProgressivo, boolean comTreinoProgressivo) {

        String[] whereArgs = {};
        String whereClause = "";
        if(semTreino){
            whereClause = " WHERE a.Nome like ? ";
            whereArgs = new String[]{"%" + Nome + "%"};
        }else if(somenteTreinoProgressivo){
            String subSelect = " AND t." + TreinoContract.columns.ID + " NOT IN ( SELECT " + TreinoProgressivoContract.columns.ID_TREINO + " FROM " + TreinoProgressivoContract.TABLE_NAME + " WHERE t." + TreinoContract.columns.ID + " ) ";
            whereClause = " WHERE a.Nome like ? AND t.Atual = ? AND t.Progressivo = ? " + subSelect;
            whereArgs = new String[]{"%" + Nome + "%", String.valueOf(Constantes.BANCO_TRUE), String.valueOf(Constantes.BANCO_TRUE)};
        }else if(comTreinoProgressivo){
            whereClause = " WHERE a.Nome like ? AND t.Atual = ? ";
            whereArgs = new String[]{"%" + Nome + "%", String.valueOf(Constantes.BANCO_TRUE)};
        }else{
            whereClause = " WHERE a.Nome like ? AND t.Atual = ? AND t.Progressivo = ? ";
            whereArgs = new String[]{"%" + Nome + "%", String.valueOf(Constantes.BANCO_TRUE), String.valueOf(Constantes.BANCO_FALSE)};
        }

        ArrayList<Aluno> alunos = new ArrayList<>();
        // query manual
        String query = "SELECT a.Cpf," +
                            " a.Nome," +
                            " a.Observacao," +
                            " a.DtNascimento," +
                            " a.Sexo " +
                        "FROM Aluno a " +
                        " LEFT JOIN Treino t ON t.CpfAluno = a.Cpf " +
                         whereClause +
                        " GROUP BY a.Cpf " +
                        " ORDER BY a.Nome";

        //fim query manual
        try (Cursor c = db.rawQuery(query, whereArgs)) {
            if (c.moveToFirst()) {
                do {
                    Aluno a = AlunoDAO.fromCursor(c);
                    alunos.add(a);
                } while (c.moveToNext());
            }

            return alunos;
        }

    }

    public ArrayList<Aluno> listaFiltradaAlunosSemProgramaTreino(String Nome) {

        String[] whereArgs = {};
        String whereClause = "";

        whereClause = " WHERE a.Nome like ? AND t.Atual = ? AND a.Cpf AND a.Cpf NOT IN ( SELECT CpfAluno FROM ProgramaTreino GROUP BY CpfAluno )";
        whereArgs = new String[]{"%" + Nome + "%", String.valueOf(Constantes.BANCO_TRUE)};

        ArrayList<Aluno> alunos = new ArrayList<>();
        // query manual
        String query = "SELECT a.Cpf," +
                " a.Nome," +
                " a.Observacao," +
                " a.DtNascimento," +
                " a.Sexo " +
                "FROM Aluno a " +
                " LEFT JOIN Treino t ON t.CpfAluno = a.Cpf " +
                " LEFT JOIN ProgramaTreino pt ON pt.CpfAluno = a.Cpf " +
                whereClause +
                " GROUP BY a.Cpf " +
                " ORDER BY a.Nome";

        //fim query manual
        try (Cursor c = db.rawQuery(query, whereArgs)) {
            if (c.moveToFirst()) {
                do {
                    Aluno a = AlunoDAO.fromCursor(c);
                    alunos.add(a);
                } while (c.moveToNext());
            }

            return alunos;
        }

    }

    public ArrayList<String> listaDeStrings() {

        String[] columns = {
                AlunoContract.columns.CPF,
                AlunoContract.columns.NOME,
                AlunoContract.columns.OBSERVACAO,
                AlunoContract.columns.DTNASIMENTO,
                AlunoContract.columns.SEXO
        };

        ArrayList<String> alunos = new ArrayList<String>();

        try (Cursor c = db.query(AlunoContract.TABLE_NAME, columns, null, null, null, null, AlunoContract.columns.NOME)) {
            if (c.moveToFirst()) {
                do {
                    Aluno a = AlunoDAO.fromCursor(c);
                    alunos.add(a.getNome());
                } while (c.moveToNext());
            }

            return alunos;
        }

    }

    public Aluno getAluno(String cpf){

        String[] columns = {
                AlunoContract.columns.CPF,
                AlunoContract.columns.NOME,
                AlunoContract.columns.OBSERVACAO,
                AlunoContract.columns.DTNASIMENTO,
                AlunoContract.columns.SEXO
        };

        String whereClause = AlunoContract.columns.CPF+" = ? ";
        String [] whereArgs = {cpf};

        Aluno a = null;

        try (Cursor c = db.query(AlunoContract.TABLE_NAME, columns, whereClause, whereArgs, null, null, AlunoContract.columns.NOME)) {
            if (c.moveToFirst()) {
                a = AlunoDAO.fromCursor(c);
            }

            return a;
        }
    }

    private void removeDasAlteracoes(String chave) throws Exception {

        Alteracao a = new Alteracao();
        a.setChave(chave);

        AlteracaoDAO.getInstance(contextBase).excluir(a);
    }

    private void registraAlteracoes(String acao, String chave) throws Exception {

        Alteracao a = new Alteracao();
        a.setAcao(acao);
        a.setAtivo(1);
        a.setChave(chave);

        AlteracaoDAO.getInstance(contextBase).salvar(a, AlunoContract.TABLE_NAME);
    }

    public void importarAlunoPorCpf(String cpf){
        JSONObject massaDados = new JSONObject();
        try {
            massaDados.put("cpf", cpf);
            massaDados.put("CREDENCIAIS", criarMassaDeCredenciais());
        }catch (Exception e){
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                Constantes.ALUNO_CONTROLLER,
                massaDados,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Long idTreino;
                        try {

                            if(response.get("aluno").equals("0")){
                                toastCentralizado("Aluno não encontrado!!");
                            }else if(response.get("aluno").equals("1")){
                                toastCentralizado("Aluno já cadastrado na academia!!!");
                            }else{
                                    JSONObject retorno = response.getJSONObject("aluno");
                                    if(retorno.length() > 0) {
                                        Aluno a = new Aluno(retorno.getString("cpf"),
                                                retorno.getString("nome"),
                                                retorno.getString("dataNascimento"),
                                                retorno.getString("observacao"),
                                                retorno.getString("sexo"));

                                        salvar(a, false);

                                        JSONObject objTreino = retorno.getJSONObject("treino");

                                        idTreino = TreinoDAO.getInstance(contextBase).salvarJson(objTreino);

                                        JSONArray arrTreinoProgressivo = retorno.getJSONArray("arrTreinoProgressivo");

                                        if(arrTreinoProgressivo.length() > 0) {
                                            for (int i = 0; i < arrTreinoProgressivo.length(); i++) {
                                                JSONObject tp = arrTreinoProgressivo.getJSONObject(i);

                                                TreinoProgressivoDAO.getInstance(contextBase).salvarJson(tp, new Treino(Integer.valueOf(idTreino.toString())));
                                            }
                                        }

                                        JSONArray arrMusculoTreinadoSemana = retorno.getJSONArray("arrMusculoTreinadoSemana");

                                        if(arrMusculoTreinadoSemana.length() > 0){
                                            for (int i = 0; i < arrMusculoTreinadoSemana.length(); i++) {
                                                JSONObject obj = arrMusculoTreinadoSemana.getJSONObject(i);
                                                MusculoTreinadoSemana mts = new MusculoTreinadoSemana();
                                                mts.setAluno(a);
                                                mts.setTipoMusculo(obj.getInt("tipoMusculo"));

                                                mts.setTreinou(obj.getInt("treinou"));

                                                MusculoTreinadoSemanaDAO.getInstance(contextBase).salvaOuAlteraMusculoTreinadoSemanaPorCpfAluno(mts);
                                            }
                                        }

                                        JSONArray arrProgramaTreino = retorno.getJSONArray("arrProgramaTreino");

                                        if(arrProgramaTreino.length() > 0){
                                            for (int i = 0; i < arrProgramaTreino.length(); i++) {
                                                ProgramaTreinoDAO.getInstance(contextBase).salvarJson(arrProgramaTreino.getJSONObject(i));
                                            }
                                        }

                                        toastCentralizado(contextBase.getString(R.string.aluno_importado_com_sucesso));

                                        Integer ultIdAlt = response.getInt("ultIdAlt");
                                        AcademiaDAO.getInstance(contextBase).atualizarUltimoIdAlteradoAcad(ultIdAlt, Constantes.INSERT);
                                    }

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (Exception e) {
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

    private static Aluno fromCursor(Cursor c) {
        String cpf = c.getString(c.getColumnIndex(AlunoContract.columns.CPF));
        String nome = c.getString(c.getColumnIndex(AlunoContract.columns.NOME));
        String observacao = c.getString(c.getColumnIndex(AlunoContract.columns.OBSERVACAO));
        String dtNascimento = c.getString(c.getColumnIndex(AlunoContract.columns.DTNASIMENTO));
        String sexo = c.getString(c.getColumnIndex(AlunoContract.columns.SEXO));
        return new Aluno(cpf, nome, dtNascimento, observacao, sexo);
    }

    public void toastCentralizado(String msg){
        Toast toast = Toast.makeText(contextBase, msg, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL, 0, 0);
        toast.show();
    }
}
