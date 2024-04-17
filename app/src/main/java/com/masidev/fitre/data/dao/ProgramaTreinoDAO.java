package com.masidev.fitre.data.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.masidev.fitre.constante.Constantes;
import com.masidev.fitre.data.contract.AlunoContract;
import com.masidev.fitre.data.contract.ProgramaTreinoContract;
import com.masidev.fitre.data.db.DBHelper;
import com.masidev.fitre.data.entidade.Alteracao;
import com.masidev.fitre.data.entidade.Aluno;
import com.masidev.fitre.data.entidade.Musculo;
import com.masidev.fitre.data.entidade.ProgramaMusculo;
import com.masidev.fitre.data.entidade.ProgramaTreinadoSemana;
import com.masidev.fitre.data.entidade.ProgramaTreino;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jmasiero on 15/04/16.
 */
public class ProgramaTreinoDAO {
    private static ProgramaTreinoDAO instance;
    private SQLiteDatabase db;
    private Context contextBase;

    public static ProgramaTreinoDAO getInstance(Context context){

        if (instance == null) {
            instance = new ProgramaTreinoDAO(context);
        }

        return instance;
    }

    private ProgramaTreinoDAO(Context context){

        DBHelper dbHelper = DBHelper.getInstance(context);
        db = dbHelper.getWritableDatabase();
        this.contextBase = context;
    }

    public long salvar(ProgramaTreino pt, Boolean registra) throws Exception{
        long retorno = 0;
        Integer treinou = 0;
        if(!pt.getNomePrograma().isEmpty() &&
                (pt.getLstMusculos().size() > 0))
        {
            try {
                ContentValues values = new ContentValues();
                values.put(ProgramaTreinoContract.columns.CPF_ALUNO, pt.getAluno().getCPF());
                values.put(ProgramaTreinoContract.columns.ORDEM, pt.getOrdem());
                values.put(ProgramaTreinoContract.columns.NOME_PROGRAMA, pt.getNomePrograma());
                values.put(ProgramaTreinoContract.columns.MISTO, pt.getMisto());
                values.put(ProgramaTreinoContract.columns.SIMPLES, pt.getSimples());

                if(pt.getTreinou()){
                    treinou = 1;
                }
                values.put(ProgramaTreinoContract.columns.TREINOU, treinou);

                retorno = db.insert(ProgramaTreinoContract.TABLE_NAME, null, values);

                if(registra) {
                    registraAlteracoes(Constantes.INSERT, String.valueOf(retorno));
                }

                if(pt.getLstMusculos().size() > 0) {
                    for (Musculo m : pt.getLstMusculos()) {
                        ProgramaMusculo pm = new ProgramaMusculo((int) retorno, m.getId(), m.getTipoMusculo(), m.getOrdem());
                        ProgramaMusculoDAO.getInstance(contextBase).salvar(pm);
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
                throw new Exception("id repetido");
            }
        }
        return retorno;
    }

    public boolean alterar(ProgramaTreino pt, Boolean registra) throws Exception{
        boolean retorno = false;
        Integer treinou = 0;

        if(!pt.getNomePrograma().isEmpty() &&
                (pt.getLstMusculos().size() > 0) &&
                pt.getId() != null)
        {
            try {
                ContentValues values = new ContentValues();
                values.put(ProgramaTreinoContract.columns.ORDEM, pt.getOrdem());
                values.put(ProgramaTreinoContract.columns.NOME_PROGRAMA, pt.getNomePrograma());
                values.put(ProgramaTreinoContract.columns.MISTO, pt.getMisto());
                values.put(ProgramaTreinoContract.columns.SIMPLES, pt.getSimples());
                if(pt.getTreinou()){
                    treinou = 1;
                }
                values.put(ProgramaTreinoContract.columns.TREINOU, treinou);

                String whereClause = ProgramaTreinoContract.columns.ID+" = ? ";
                String [] whereArgs = {String.valueOf(pt.getId())};

                db.update(ProgramaTreinoContract.TABLE_NAME, values, whereClause, whereArgs);

                if(registra) {
                    registraAlteracoes(Constantes.UPDATE, String.valueOf(pt.getId()));
                }

                if(pt.getLstMusculos().size() > 0) {
                    ProgramaMusculoDAO.getInstance(contextBase).excluirTodosDoPrograma(pt);
                    for (Musculo m : pt.getLstMusculos()) {
                        ProgramaMusculo pm = new ProgramaMusculo(pt.getId(), m.getId(), m.getTipoMusculo(), m.getOrdem());
                        ProgramaMusculoDAO.getInstance(contextBase).salvar(pm);
                    }
                }
                retorno = true;
            }catch (Exception e){
                e.printStackTrace();
                throw new Exception("problema alteração");
            }
        }
        return retorno;
    }

    public List<ProgramaTreino> listarPorAluno(String filtro) {
        List<ProgramaTreino> lstProgramaTreino = new ArrayList<>();

        String whereClause = "";
        String[] whereArgs = {};

        whereClause = " WHERE A."+AlunoContract.columns.NOME+" like ? ";
        whereArgs = new String[]{"%" + filtro + "%"};

        String query = "SELECT "+
                " PT."+ProgramaTreinoContract.columns.ID+
                " ,PT."+ProgramaTreinoContract.columns.CPF_ALUNO+
                " ,PT."+ProgramaTreinoContract.columns.NOME_PROGRAMA+
                " ,PT."+ProgramaTreinoContract.columns.ORDEM+
                " ,PT."+ProgramaTreinoContract.columns.MISTO+
                " ,PT."+ProgramaTreinoContract.columns.SIMPLES+
                " ,PT."+ProgramaTreinoContract.columns.TREINOU+
                " FROM " + ProgramaTreinoContract.TABLE_NAME + " PT "+
                " INNER JOIN "+AlunoContract.TABLE_NAME+" A ON PT."+ProgramaTreinoContract.columns.CPF_ALUNO+" = A."+AlunoContract.columns.CPF+" " +
                whereClause +
                " GROUP BY A.Cpf " +
                " ORDER BY A."+AlunoContract.columns.NOME;

        try (Cursor c = db.rawQuery(query, whereArgs)) {
            if (c.moveToFirst()) {
                do {
                    ProgramaTreino pt = fromCursor(c);
                    lstProgramaTreino.add(pt);
                } while (c.moveToNext());
            }

        }

        return lstProgramaTreino;
    }

    public ArrayList<ProgramaTreino> retornarProgramasDoAluno(Aluno aluno) {
        String[] columns = {
                ProgramaTreinoContract.columns.ID,
                ProgramaTreinoContract.columns.CPF_ALUNO,
                ProgramaTreinoContract.columns.NOME_PROGRAMA,
                ProgramaTreinoContract.columns.ORDEM,
                ProgramaTreinoContract.columns.MISTO,
                ProgramaTreinoContract.columns.SIMPLES,
                ProgramaTreinoContract.columns.TREINOU
        };

        String whereClause = ProgramaTreinoContract.columns.CPF_ALUNO + " = ? ";
        String[] whereArgs = {aluno.getCPF()};

        ArrayList<ProgramaTreino> lstProgramasTreino = new ArrayList<>();

        try (Cursor c = db.query(ProgramaTreinoContract.TABLE_NAME, columns, whereClause, whereArgs, null, null, ProgramaTreinoContract.columns.ORDEM)) {
            if (c.moveToFirst()) {
                do {
                    ProgramaTreino pt = fromCursor(c);
                    pt.setLstMusculos(ProgramaMusculoDAO.getInstance(contextBase).retornaMusculos(pt));
                    lstProgramasTreino.add(pt);
                } while (c.moveToNext());
            }

        }

        return lstProgramasTreino;
    }

    public ProgramaTreino retornarProgramaTreinoPorId(int id) throws  Exception{
        String[] columns = {
                ProgramaTreinoContract.columns.ID,
                ProgramaTreinoContract.columns.CPF_ALUNO,
                ProgramaTreinoContract.columns.NOME_PROGRAMA,
                ProgramaTreinoContract.columns.ORDEM,
                ProgramaTreinoContract.columns.MISTO,
                ProgramaTreinoContract.columns.SIMPLES,
                ProgramaTreinoContract.columns.TREINOU
        };

        String whereClause = ProgramaTreinoContract.columns.ID + " = ? ";
        String[] whereArgs = {String.valueOf(id)};

        ProgramaTreino pt = null;

        try (Cursor c = db.query(ProgramaTreinoContract.TABLE_NAME, columns, whereClause, whereArgs, null, null, ProgramaTreinoContract.columns.ORDEM)) {
            if (c.moveToFirst()) {
                do {
                    pt = fromCursor(c);
                    pt.setLstMusculos(ProgramaMusculoDAO.getInstance(contextBase).retornaMusculos(pt));
                } while (c.moveToNext());
            }
        }

        return pt;
    }

    public boolean excluir(ProgramaTreino pt, Boolean registrar) throws Exception{
        boolean retorno = false;
        try {

            String whereClause = ProgramaTreinoContract.columns.ID + " = ? ";
            String [] whereArgs = {String.valueOf(pt.getId())};

            db.delete(ProgramaTreinoContract.TABLE_NAME, whereClause, whereArgs);
            if(registrar) {
                registraAlteracoes(Constantes.DELETE, pt.getId() + "/" +pt.getAluno().getCPF() + "/" + String.valueOf(pt.getOrdem()) + "/" + String.valueOf(pt.getNomePrograma()));
            }

            retorno = true;
        }catch (Exception e){
            e.printStackTrace();
            throw new Exception("Ocorreu um erro na exclusão");
        }

        return retorno;
    }

    public boolean excluirTodosDoAluno(Aluno aluno, Boolean registra) throws Exception{
        boolean retorno = false;
        try {
            ArrayList<ProgramaTreino> lstPt = retornarProgramasDoAluno(aluno);

            for(ProgramaTreino pt : lstPt ){
                if(registra) {
                    // IdProgramaTreino . "/" . cpfAluno . "/" . ordem . "/" . nomePrograma;
                    registraAlteracoes(Constantes.DELETE, pt.getId() + "/" + pt.getAluno().getCPF() + "/" + String.valueOf(pt.getOrdem()) + "/" + String.valueOf(pt.getNomePrograma()));
                }
                String whereClause = ProgramaTreinoContract.columns.ID + " = ? ";
                String [] whereArgs = {String.valueOf(pt.getId())};

                db.delete(ProgramaTreinoContract.TABLE_NAME, whereClause, whereArgs);
            }

            retorno = true;
        }catch (Exception e){
            e.printStackTrace();
            throw new Exception("Ocorreu um erro na exclusão");
        }

        return retorno;
    }

    public boolean excluirProgramaTreinoComDadosServidor(String cpfAluno, int ordem, String nomePrograma, Boolean registra) throws Exception{
        boolean retorno = false;
        try {
            StringBuilder sb = new StringBuilder();
            sb.append(ProgramaTreinoContract.columns.CPF_ALUNO + " = ? AND ");
            sb.append(ProgramaTreinoContract.columns.ORDEM + " = ? AND ");
            sb.append(ProgramaTreinoContract.columns.NOME_PROGRAMA + " = ? ");

            String whereClause = sb.toString();
            String [] whereArgs = {cpfAluno, String.valueOf(ordem), nomePrograma};

            if(registra){
                registraAlteracoes(Constantes.DELETE, cpfAluno + "/" + ordem + "/" + nomePrograma);
            }

            db.delete(ProgramaTreinoContract.TABLE_NAME, whereClause, whereArgs);
            retorno = true;
        }catch (Exception e){
            e.printStackTrace();
            throw new Exception("Ocorreu um erro na exclusão");
        }

        return retorno;
    }

    public void salvarJson(JSONObject obj) throws Exception{
        ProgramaTreino pt = new ProgramaTreino();
        pt.setAluno(new Aluno(obj.getString("cpfAluno")));
        pt.setOrdem(obj.getInt("ordem"));
        pt.setNomePrograma(obj.getString("nomePrograma"));

        boolean misto = false;
        if(obj.getInt("misto") == 1){
            misto = true;
        }
        pt.setMisto(misto);

        boolean simples = false;
        if(obj.getInt("simples") == 1){
            simples = true;
        }
        pt.setSimples(simples);

        boolean treinou = false;
        if(obj.getInt("treinou") == 1){
            treinou = true;
        }
        pt.setTreinou(treinou);

        JSONArray jsArrProgramaMusculo = obj.getJSONArray("arrProgramaMusculo");
        ArrayList<Musculo> lstMusculos = new ArrayList<>();

        if(jsArrProgramaMusculo.length() > 0){
            for (int i = 0; i < jsArrProgramaMusculo.length(); i++){
                JSONObject jsProgramaMusculo = jsArrProgramaMusculo.getJSONObject(i);

                lstMusculos.add(MusculoDAO.getInstance(contextBase).retornaMusculoDoProgramaTreino(jsProgramaMusculo.getInt("tipoMusculo"), jsProgramaMusculo.getInt("ordemMusculo"), obj.getString("cpfAluno")));
            }
        }

        pt.setLstMusculos(lstMusculos);

        salvar(pt, false);
    }

    public ProgramaTreino retornaProgramaTreinoComDadosProgramaTreinadoSemana(ProgramaTreinadoSemana pts) {
        String[] columns = {
                ProgramaTreinoContract.columns.ID,
                ProgramaTreinoContract.columns.CPF_ALUNO,
                ProgramaTreinoContract.columns.NOME_PROGRAMA,
                ProgramaTreinoContract.columns.ORDEM,
                ProgramaTreinoContract.columns.MISTO,
                ProgramaTreinoContract.columns.SIMPLES,
                ProgramaTreinoContract.columns.TREINOU
        };

        StringBuilder sb = new StringBuilder();
        sb.append(ProgramaTreinoContract.columns.CPF_ALUNO + " = ? AND ");
        sb.append(ProgramaTreinoContract.columns.ORDEM + " = ? ");

        String whereClause = sb.toString();
        String[] whereArgs = {pts.getAluno().getCPF(), pts.getOrdemPrograma().toString()};

        ProgramaTreino pt = null;

        try (Cursor c = db.query(ProgramaTreinoContract.TABLE_NAME, columns, whereClause, whereArgs, null, null, ProgramaTreinoContract.columns.ORDEM)) {
            if (c.moveToFirst()) {
                pt = fromCursor(c);
            }

        }

        return pt;
    }

    public void programaTreinado(ProgramaTreino pt) throws Exception{
        ContentValues values = new ContentValues();
        values.put(ProgramaTreinoContract.columns.TREINOU, Constantes.BANCO_TRUE);

        String whereClause = ProgramaTreinoContract.columns.ID + " = ? ";
        String [] whereArgs = {String.valueOf(pt.getId())};

        db.update(ProgramaTreinoContract.TABLE_NAME, values, whereClause, whereArgs);

        //if(registra) {
            registraAlteracoes(Constantes.UPDATE, String.valueOf(pt.getId()));
        //}
    }

    public void limpaHistoricoSemana() {
        ContentValues values = new ContentValues();
        values.put(ProgramaTreinoContract.columns.TREINOU, Constantes.BANCO_FALSE);

        db.update(ProgramaTreinoContract.TABLE_NAME, values, null, null);
    }

    private void registraAlteracoes(String acao, String chave) throws Exception {

        Alteracao a = new Alteracao();
        a.setAcao(acao);
        a.setAtivo(1);
        a.setChave(chave);

        AlteracaoDAO.getInstance(contextBase).salvar(a, ProgramaTreinoContract.TABLE_NAME);
    }

    private static ProgramaTreino fromCursor(Cursor c) {
        Integer id = c.getInt(c.getColumnIndex(ProgramaTreinoContract.columns.ID));
        String cpf = c.getString(c.getColumnIndex(ProgramaTreinoContract.columns.CPF_ALUNO));
        Integer ordem = c.getInt(c.getColumnIndex(ProgramaTreinoContract.columns.ORDEM));
        String nomePrograma = c.getString(c.getColumnIndex(ProgramaTreinoContract.columns.NOME_PROGRAMA));
        Integer misto = c.getInt(c.getColumnIndex(ProgramaTreinoContract.columns.MISTO));
        Boolean bMisto = false;
        if(misto == 1){
            bMisto = true;
        }

        Integer simples = c.getInt(c.getColumnIndex(ProgramaTreinoContract.columns.SIMPLES));
        Boolean bSimples = false;
        if(simples == 1){
            bSimples = true;
        }

        Integer treinou = c.getInt(c.getColumnIndex(ProgramaTreinoContract.columns.TREINOU));
        Boolean bTreinou = false;
        if(treinou == 1){
            bTreinou = true;
        }

        return new ProgramaTreino(id, new Aluno(cpf), ordem, nomePrograma, bMisto, bSimples, bTreinou);
    }
}
