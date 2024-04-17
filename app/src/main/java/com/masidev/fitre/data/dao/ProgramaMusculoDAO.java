package com.masidev.fitre.data.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.masidev.fitre.data.contract.ProgramaMusculoContract;
import com.masidev.fitre.data.contract.ProgramaTreinoContract;
import com.masidev.fitre.data.db.DBHelper;
import com.masidev.fitre.data.entidade.Musculo;
import com.masidev.fitre.data.entidade.ProgramaMusculo;
import com.masidev.fitre.data.entidade.ProgramaTreino;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by jmasiero on 15/04/16.
 */
public class ProgramaMusculoDAO {
    private static ProgramaMusculoDAO instance;
    private SQLiteDatabase db;
    private Context contextBase;

    public static ProgramaMusculoDAO getInstance(Context context){

        if (instance == null) {
            instance = new ProgramaMusculoDAO(context);
        }

        return instance;
    }

    private ProgramaMusculoDAO(Context context){

        DBHelper dbHelper = DBHelper.getInstance(context);
        db = dbHelper.getWritableDatabase();
        this.contextBase = context;
    }

    public long salvar(ProgramaMusculo pm) throws Exception{
        long retorno = 0;

        if(pm.getIdMusculo() != null &&
                pm.getIdPrograma() != null &&
                pm.getTipoMusculo() != null &&
                pm.getOrdemMusculo() != null)
        {
            try {
                ContentValues values = new ContentValues();
                values.put(ProgramaMusculoContract.columns.ID_PROGRAMA, pm.getIdPrograma());
                values.put(ProgramaMusculoContract.columns.ID_MUSCULO, pm.getIdMusculo());
                values.put(ProgramaMusculoContract.columns.TIPO_MUSCULO, pm.getTipoMusculo());
                values.put(ProgramaMusculoContract.columns.ORDEM_MUSCULO, pm.getOrdemMusculo());

                retorno = db.insert(ProgramaMusculoContract.TABLE_NAME, null, values);

            }catch (Exception e){
                e.printStackTrace();
                throw new Exception("id repetido");
            }
        }
        return retorno;
    }

    public ArrayList<Musculo> retornaMusculos(ProgramaTreino pt) {
        String[] columns = {
                ProgramaMusculoContract.columns.ID,
                ProgramaMusculoContract.columns.ID_MUSCULO,
                ProgramaMusculoContract.columns.ID_PROGRAMA,
                ProgramaMusculoContract.columns.ORDEM_MUSCULO,
                ProgramaMusculoContract.columns.TIPO_MUSCULO
        };

        String whereClause = ProgramaMusculoContract.columns.ID_PROGRAMA + " = ? ";
        String[] whereArgs = {String.valueOf(pt.getId())};

        ArrayList<Musculo> lstMusculos = new ArrayList<>();

        try (Cursor c = db.query(ProgramaMusculoContract.TABLE_NAME, columns, whereClause, whereArgs, null, null, ProgramaMusculoContract.columns.ID)) {
            if (c.moveToFirst()) {
                do {
                    ProgramaMusculo pm = fromCursor(c);

                    lstMusculos.add(MusculoDAO.getInstance(contextBase).retornaMusculoPorId(pm.getIdMusculo()));
                } while (c.moveToNext());
            }

        }

        return lstMusculos;
    }

    public JSONArray retornaJsonExercicios(ProgramaTreino pt) {
        String[] columns = {
                ProgramaMusculoContract.columns.ID,
                ProgramaMusculoContract.columns.ID_MUSCULO,
                ProgramaMusculoContract.columns.ID_PROGRAMA,
                ProgramaMusculoContract.columns.ORDEM_MUSCULO,
                ProgramaMusculoContract.columns.TIPO_MUSCULO
        };

        String whereClause = ProgramaMusculoContract.columns.ID_PROGRAMA + " = ? ";
        String[] whereArgs = {String.valueOf(pt.getId())};

        JSONArray jsonArrayExercicios = new JSONArray();

        try (Cursor c = db.query(ProgramaMusculoContract.TABLE_NAME, columns, whereClause, whereArgs, null, null, ProgramaMusculoContract.columns.ID)) {
            if (c.moveToFirst()) {
                do {
                    try {

                    ProgramaMusculo pm = fromCursor(c);

                    JSONObject jsExercicio = new JSONObject();
                    jsExercicio.put("ordemMusculo", pm.getOrdemMusculo());
                    jsExercicio.put("tipoMusculo", pm.getTipoMusculo());

                    jsonArrayExercicios.put(jsExercicio);

                    }catch (Exception e){
                        e.printStackTrace();
                    }
                } while (c.moveToNext());
            }

        }

        return jsonArrayExercicios;
    }

    public boolean excluirTodosDoPrograma(ProgramaTreino pt) throws Exception{
        boolean retorno = false;
        try {
            String whereClause = ProgramaMusculoContract.columns.ID_PROGRAMA + " = ? ";
            String [] whereArgs = {String.valueOf(pt.getId())};

            db.delete(ProgramaMusculoContract.TABLE_NAME, whereClause, whereArgs);
            retorno = true;
        }catch (Exception e){
            e.printStackTrace();
            throw new Exception("Ocorreu um erro na exclusão");
        }

        return retorno;
    }

    public void excluirLigacaoMusculo(Musculo m)throws Exception{
        boolean retorno = false;

        try {
            ContentValues values = new ContentValues();

            String whereClause = ProgramaTreinoContract.columns.ID + " = ? ";
            String [] whereArgs = {String.valueOf(m.getId())};

            db.delete(ProgramaMusculoContract.TABLE_NAME, whereClause, whereArgs);

            retorno = true;

        }catch (Exception e){
            e.printStackTrace();
            throw new Exception("Ocorreu um erro na exclusão");
        }
    }

    private static ProgramaMusculo fromCursor(Cursor c) {
        Integer id = c.getInt(c.getColumnIndex(ProgramaMusculoContract.columns.ID));
        Integer idPrograma = c.getInt(c.getColumnIndex(ProgramaMusculoContract.columns.ID_PROGRAMA));
        Integer idMusculo = c.getInt(c.getColumnIndex(ProgramaMusculoContract.columns.ID_MUSCULO));
        Integer tipoMusculo = c.getInt(c.getColumnIndex(ProgramaMusculoContract.columns.TIPO_MUSCULO));
        Integer ordemMusculo = c.getInt(c.getColumnIndex(ProgramaMusculoContract.columns.ORDEM_MUSCULO));

        return new ProgramaMusculo(id, idPrograma, idMusculo, tipoMusculo, ordemMusculo);
    }

}
