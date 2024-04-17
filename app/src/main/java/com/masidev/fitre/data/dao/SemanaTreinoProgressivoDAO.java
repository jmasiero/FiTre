package com.masidev.fitre.data.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.masidev.fitre.data.contract.SemanaTreinoProgressivoContract;
import com.masidev.fitre.data.db.DBHelper;
import com.masidev.fitre.data.entidade.SemanaTreinoProgressivo;
import com.masidev.fitre.data.entidade.TreinoProgressivo;

import java.util.ArrayList;

/**
 * Created by jmasiero on 15/04/16.
 */
public class SemanaTreinoProgressivoDAO {
    private static SemanaTreinoProgressivoDAO instance;
    private SQLiteDatabase db;
    private Context contextBase;

    public static SemanaTreinoProgressivoDAO getInstance(Context context){

        if (instance == null) {
            instance = new SemanaTreinoProgressivoDAO(context);
        }

        return instance;
    }

    private SemanaTreinoProgressivoDAO(Context context){
        DBHelper dbHelper = DBHelper.getInstance(context);
        db = dbHelper.getWritableDatabase();
        this.contextBase = context;
    }

    public void salvar(SemanaTreinoProgressivo smtp) throws Exception{
        try {
            ContentValues values = new ContentValues();
            values.put(SemanaTreinoProgressivoContract.columns.ID_TREINO_PROGRESSIVO, smtp.getIdTreinoProgressivo());
            values.put(SemanaTreinoProgressivoContract.columns.PORCENTAGEM_CARGAS, smtp.getPorcentagemCargas());
            values.put(SemanaTreinoProgressivoContract.columns.REPETICOES, smtp.getRepeticoes());
            values.put(SemanaTreinoProgressivoContract.columns.SEMANA, smtp.getSemana());

            db.insert(SemanaTreinoProgressivoContract.TABLE_NAME, null, values);

        }catch (Exception e){
            e.printStackTrace();
            throw new Exception("id repetido");
        }
    }

    public void alterar(SemanaTreinoProgressivo smtp) throws Exception{
        try {

            ContentValues values = new ContentValues();
            values.put(SemanaTreinoProgressivoContract.columns.PORCENTAGEM_CARGAS, smtp.getPorcentagemCargas());
            values.put(SemanaTreinoProgressivoContract.columns.REPETICOES, smtp.getRepeticoes());

            String whereClause = SemanaTreinoProgressivoContract.columns.ID + " = ? ";
            String[] whereArgs = {String.valueOf(smtp.getId())};

            db.update(SemanaTreinoProgressivoContract.TABLE_NAME, values, whereClause, whereArgs);
        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }

    public ArrayList<SemanaTreinoProgressivo> retornarSemanas(TreinoProgressivo tp) {
        String[] columns = {
                SemanaTreinoProgressivoContract.columns.ID,
                SemanaTreinoProgressivoContract.columns.ID_TREINO_PROGRESSIVO,
                SemanaTreinoProgressivoContract.columns.PORCENTAGEM_CARGAS,
                SemanaTreinoProgressivoContract.columns.REPETICOES,
                SemanaTreinoProgressivoContract.columns.SEMANA
        };

        String whereClause = SemanaTreinoProgressivoContract.columns.ID_TREINO_PROGRESSIVO + " = ? ";
        String [] whereArgs = {String.valueOf(tp.getId())};

        ArrayList<SemanaTreinoProgressivo> lstSemanas = new ArrayList<>();

        try (Cursor c = db.query(SemanaTreinoProgressivoContract.TABLE_NAME, columns, whereClause, whereArgs, null, null, SemanaTreinoProgressivoContract.columns.SEMANA)) {
            if (c.moveToFirst()) {
                do {
                    lstSemanas.add(fromCursor(c));
                }while (c.moveToNext());
            }
        }

        return lstSemanas;
    }

    public ArrayList<SemanaTreinoProgressivo> retornarSemanaAtual(TreinoProgressivo treinoProgressivo, String data) {
        String[] columns = {
                SemanaTreinoProgressivoContract.columns.ID,
                SemanaTreinoProgressivoContract.columns.ID_TREINO_PROGRESSIVO,
                SemanaTreinoProgressivoContract.columns.PORCENTAGEM_CARGAS,
                SemanaTreinoProgressivoContract.columns.REPETICOES,
                SemanaTreinoProgressivoContract.columns.SEMANA
        };
        String[] arrData = data.split("-");

        StringBuilder sb = new StringBuilder();
        sb.append(SemanaTreinoProgressivoContract.columns.ID_TREINO_PROGRESSIVO + " = ? AND ");
        sb.append(SemanaTreinoProgressivoContract.columns.SEMANA + " = ? ");
        String whereClause = sb.toString();

        String [] whereArgs = {String.valueOf(treinoProgressivo.getId()), arrData[0]};

        ArrayList<SemanaTreinoProgressivo> lstSemanas = new ArrayList<>();

        try (Cursor c = db.query(SemanaTreinoProgressivoContract.TABLE_NAME, columns, whereClause, whereArgs, null, null, SemanaTreinoProgressivoContract.columns.SEMANA)) {
            if (c.moveToFirst()) {
                lstSemanas.add(fromCursor(c));
            }
        }

        return lstSemanas;
    }

    private static SemanaTreinoProgressivo fromCursor(Cursor c) {
        Integer id = c.getInt(c.getColumnIndex(SemanaTreinoProgressivoContract.columns.ID));
        String porcentagemCargas = c.getString(c.getColumnIndex(SemanaTreinoProgressivoContract.columns.PORCENTAGEM_CARGAS));
        String repeticoes = c.getString(c.getColumnIndex(SemanaTreinoProgressivoContract.columns.REPETICOES));
        Integer semana = c.getInt(c.getColumnIndex(SemanaTreinoProgressivoContract.columns.SEMANA));

        return new SemanaTreinoProgressivo(id, repeticoes, porcentagemCargas, semana);
    }


}
