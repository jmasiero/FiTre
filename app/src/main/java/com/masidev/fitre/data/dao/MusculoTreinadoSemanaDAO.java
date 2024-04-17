package com.masidev.fitre.data.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.masidev.fitre.constante.Constantes;
import com.masidev.fitre.data.contract.MusculoTreinadoSemanaContract;
import com.masidev.fitre.data.db.DBHelper;
import com.masidev.fitre.data.entidade.Alteracao;
import com.masidev.fitre.data.entidade.Aluno;
import com.masidev.fitre.data.entidade.MusculoTreinadoSemana;

import java.util.ArrayList;

/**
 * Created by jmasiero on 15/02/16.
 */
public class MusculoTreinadoSemanaDAO {
    private static MusculoTreinadoSemanaDAO instance;
    private static Context contextBase;
    private SQLiteDatabase db;

    public static MusculoTreinadoSemanaDAO getInstance(Context context){
        if(instance == null){
            instance = new MusculoTreinadoSemanaDAO(context.getApplicationContext());
        }

        contextBase = context;

        return instance;
    }

    private MusculoTreinadoSemanaDAO(Context context){
        DBHelper dbHelper = DBHelper.getInstance(context);
        db = dbHelper.getWritableDatabase();
    }

    //Crud
    public long salvar(MusculoTreinadoSemana m, Boolean registrar) throws Exception{
        long retorno = 0;

        if(m.getAluno() != null
                && m.getTipoMusculo() != null
                && m.getTreinou() != null)
        {
            try {
                ContentValues values = new ContentValues();
                values.put(MusculoTreinadoSemanaContract.columns.CPF_ALUNO, m.getAluno().getCPF());
                values.put(MusculoTreinadoSemanaContract.columns.TIPO_MUSCULO, m.getTipoMusculo());
                values.put(MusculoTreinadoSemanaContract.columns.TREINOU, m.getTreinou());

                retorno = db.insert(MusculoTreinadoSemanaContract.TABLE_NAME, null, values);

                if(registrar) {
                    registraAlteracoes(Constantes.INSERT, String.valueOf(retorno));
                }

            }catch (Exception e){
                e.printStackTrace();
                throw new Exception("ocorreu um erro ao salvar");
            }
        }
        return retorno;
    }

    public long alterar(MusculoTreinadoSemana m) throws Exception{
        long retorno = 0;

        if(m.getAluno() != null
                && m.getTipoMusculo() != null
                && m.getId() != null
                && m.getTreinou() != null)
        {
            try {
                ContentValues values = new ContentValues();
                values.put(MusculoTreinadoSemanaContract.columns.CPF_ALUNO, m.getAluno().getCPF());
                values.put(MusculoTreinadoSemanaContract.columns.TIPO_MUSCULO, m.getTipoMusculo());
                values.put(MusculoTreinadoSemanaContract.columns.TREINOU, m.getTreinou());

                String whereClause = MusculoTreinadoSemanaContract.columns.ID+" = ?";
                String [] whereArgs = {m.getId().toString()};

                retorno = db.update(MusculoTreinadoSemanaContract.TABLE_NAME, values, whereClause, whereArgs);

                registraAlteracoes(Constantes.UPDATE, String.valueOf(m.getId()));
            }catch (Exception e){
                e.printStackTrace();
                throw new Exception("ocorreu um erro ao alterar");
            }
        }
        return retorno;
    }

    public long salvaOuAlteraMusculoTreinadoSemanaPorCpfAluno(MusculoTreinadoSemana mts) throws Exception{
        long retorno = 0;
        MusculoTreinadoSemana mtsBancoInterno = retornaMusculoTreinadoSemanaPorCpfETipoMusculo(mts);

        if(mts.getTreinou() == null){
            mts.setTreinou(0);
        }

        if(mtsBancoInterno == null) {
            salvar(mts, false);
        }else{
            try {
                ContentValues values = new ContentValues();
                values.put(MusculoTreinadoSemanaContract.columns.CPF_ALUNO, mtsBancoInterno.getAluno().getCPF());
                values.put(MusculoTreinadoSemanaContract.columns.TIPO_MUSCULO, mtsBancoInterno.getTipoMusculo());
                values.put(MusculoTreinadoSemanaContract.columns.TREINOU, mts.getTreinou());

                String whereClause = MusculoTreinadoSemanaContract.columns.ID+" = ?";
                String [] whereArgs = {mtsBancoInterno.getId().toString()};

                retorno = db.update(MusculoTreinadoSemanaContract.TABLE_NAME, values, whereClause, whereArgs);
            }catch (Exception e){
                e.printStackTrace();
                throw new Exception("ocorreu um erro ao alterar");
            }
        }

        return retorno;
    }

    public boolean desmarcarTodosMusculos() throws Exception{
        boolean retorno = false;

        try {
            ContentValues values = new ContentValues();
            values.put(MusculoTreinadoSemanaContract.columns.TREINOU, false);

            db.update(MusculoTreinadoSemanaContract.TABLE_NAME, values, null, null);

            retorno = true;
        }catch (Exception e){
            e.printStackTrace();
            throw new Exception("Ocorreu um erro na alteração");
        }
        return retorno;
    }

    public boolean excluir(MusculoTreinadoSemana m) throws Exception{
        boolean retorno = false;

        if(m.getId() != null)
        {
            String whereClause = MusculoTreinadoSemanaContract.columns.ID+" = ?";
            String [] whereArgs = {m.getId().toString()};

            try {

                db.delete(MusculoTreinadoSemanaContract.TABLE_NAME, whereClause, whereArgs);

                registraAlteracoes(Constantes.DELETE, String.valueOf(m.getId()));
                retorno = true;
            }catch (Exception e){
                e.printStackTrace();
                throw new Exception("Ocorreu um erro na exclusão");
            }
        }
        return retorno;
    }

    public boolean excluirHistorico(Aluno a) throws Exception{
        boolean retorno = false;

        if(a.getCPF() != null)
        {
            String whereClause = MusculoTreinadoSemanaContract.columns.CPF_ALUNO + " = ?";
            String [] whereArgs = {a.getCPF().toString()};

            try {

                db.delete(MusculoTreinadoSemanaContract.TABLE_NAME, whereClause, whereArgs);

                retorno = true;
            }catch (Exception e){
                e.printStackTrace();
                throw new Exception("Ocorreu um erro na exclusão");
            }
        }
        return retorno;
    }

    public ArrayList<MusculoTreinadoSemana> listar(Aluno a) {

        String[] columns = {
                MusculoTreinadoSemanaContract.columns.CPF_ALUNO,
                MusculoTreinadoSemanaContract.columns.TIPO_MUSCULO,
                MusculoTreinadoSemanaContract.columns.TREINOU,
                MusculoTreinadoSemanaContract.columns.ID
        };

        ArrayList<MusculoTreinadoSemana> musculoTreinadoSemanas = new ArrayList<>();

        String whereClause = MusculoTreinadoSemanaContract.columns.CPF_ALUNO+" = ?";
        String [] whereArgs = {a.getCPF()};

        try (Cursor c = db.query(MusculoTreinadoSemanaContract.TABLE_NAME, columns, whereClause, whereArgs, null, null, MusculoTreinadoSemanaContract.columns.TIPO_MUSCULO)) {
            if (c.moveToFirst()) {
                do {
                    MusculoTreinadoSemana m = fromCursor(c);
                    musculoTreinadoSemanas.add(m);
                } while (c.moveToNext());
            }

            return musculoTreinadoSemanas;
        }

    }

    public MusculoTreinadoSemana retornaMusculoTreinadoSemanaPorId(Integer id) {

        String[] columns = {
                MusculoTreinadoSemanaContract.columns.CPF_ALUNO,
                MusculoTreinadoSemanaContract.columns.TIPO_MUSCULO,
                MusculoTreinadoSemanaContract.columns.TREINOU,
                MusculoTreinadoSemanaContract.columns.ID
        };

        MusculoTreinadoSemana musculoTreinadoSemana = null;

        String whereClause = MusculoTreinadoSemanaContract.columns.ID + " = ? ";
        String [] whereArgs = {String.valueOf(id)};

        try (Cursor c = db.query(MusculoTreinadoSemanaContract.TABLE_NAME, columns, whereClause, whereArgs, null, null, MusculoTreinadoSemanaContract.columns.ID)) {
            if (c.moveToFirst()) {
                musculoTreinadoSemana = fromCursor(c);
            }

        }

        return musculoTreinadoSemana;
    }

    public MusculoTreinadoSemana retornaMusculoTreinadoSemanaPorCpfETipoMusculo(MusculoTreinadoSemana mts) {

        String[] columns = {
                MusculoTreinadoSemanaContract.columns.CPF_ALUNO,
                MusculoTreinadoSemanaContract.columns.TIPO_MUSCULO,
                MusculoTreinadoSemanaContract.columns.TREINOU,
                MusculoTreinadoSemanaContract.columns.ID
        };

        MusculoTreinadoSemana musculoTreinadoSemana = null;

        StringBuilder sb = new StringBuilder();

        sb.append(MusculoTreinadoSemanaContract.columns.CPF_ALUNO + " = ? AND ");
        sb.append(MusculoTreinadoSemanaContract.columns.TIPO_MUSCULO + " = ? ");

        String whereClause = sb.toString();
        String [] whereArgs = {mts.getAluno().getCPF(), String.valueOf(mts.getTipoMusculo())};

        try (Cursor c = db.query(MusculoTreinadoSemanaContract.TABLE_NAME, columns, whereClause, whereArgs, null, null, MusculoTreinadoSemanaContract.columns.ID)) {
            if (c.moveToFirst()) {
                musculoTreinadoSemana = fromCursor(c);
            }

        }

        return musculoTreinadoSemana;
    }

    private void registraAlteracoes(String acao, String chave) throws Exception {

        Alteracao a = new Alteracao();
        a.setAcao(acao);
        a.setAtivo(1);
        a.setChave(chave);

        AlteracaoDAO.getInstance(contextBase).salvar(a, MusculoTreinadoSemanaContract.TABLE_NAME);
    }

    public void excluirMTSDoAluno(Aluno a) throws Exception {
        try {
            ContentValues values = new ContentValues();

            String whereClause = MusculoTreinadoSemanaContract.columns.CPF_ALUNO + " = ? ";
            String [] whereArgs = {String.valueOf(a.getCPF())};

            db.delete(MusculoTreinadoSemanaContract.TABLE_NAME, whereClause, whereArgs);

        }catch (Exception e){
            e.printStackTrace();
            throw new Exception("Ocorreu um erro na exclusão");
        }

    }

    private static MusculoTreinadoSemana fromCursor(Cursor c) {
        Integer id = c.getInt(c.getColumnIndex(MusculoTreinadoSemanaContract.columns.ID));
        Integer tipoMusculo = c.getInt(c.getColumnIndex(MusculoTreinadoSemanaContract.columns.TIPO_MUSCULO));
        Integer treinou = c.getInt(c.getColumnIndex(MusculoTreinadoSemanaContract.columns.TREINOU));
        String cpf = c.getString(c.getColumnIndex(MusculoTreinadoSemanaContract.columns.CPF_ALUNO));

        if(cpf.length() == 10){
            cpf = "0" + cpf;
        }

        if(cpf.length() == 9){
            cpf = "00" + cpf;
        }

        Aluno a = new Aluno(cpf);
        return new MusculoTreinadoSemana(id, tipoMusculo, treinou, a);
    }
}
