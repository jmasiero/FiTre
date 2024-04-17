package com.masidev.fitre.data.dao;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.masidev.fitre.constante.Constantes;
import com.masidev.fitre.data.contract.AcademiaContract;
import com.masidev.fitre.data.contract.AlunoContract;
import com.masidev.fitre.data.db.DBHelper;
import com.masidev.fitre.data.entidade.Academia;
import com.masidev.fitre.volley.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jmasiero on 15/04/16.
 */
public class AcademiaDAO {
    private static AcademiaDAO instance;
    private SQLiteDatabase db;
    private Context contextBase;

    public static AcademiaDAO getInstance(Context context){

        if (instance == null) {
            instance = new AcademiaDAO(context);
        }

        return instance;
    }

    private AcademiaDAO(Context context){

        DBHelper dbHelper = DBHelper.getInstance(context);
        db = dbHelper.getWritableDatabase();
        this.contextBase = context;
    }

    public void atualizarAcademia(Academia ac) {
        Academia acInterna = retornaAcademiaPorCnpj(ac.getCnpj());
        if(acInterna.getPagou() != ac.getPagou() ||
                !acInterna.getDtUltimoPagamento().equals(ac.getDtUltimoPagamento()) ||
                !acInterna.getTelefone().equals(ac.getTelefone())){

            try {
                ContentValues values = new ContentValues();
                values.put(AcademiaContract.columns.NOME_FANTASIA, ac.getNomeFantasia());
                values.put(AcademiaContract.columns.DT_ULTIMO_PAGAMENTO, ac.getDtUltimoPagamento());
                values.put(AcademiaContract.columns.PAGOU, ac.getPagou());
                values.put(AcademiaContract.columns.TELEFONE, ac.getTelefone());

                String whereClause = AcademiaContract.columns.CNPJ + " = ? ";
                String [] whereArgs = {ac.getCnpj()};

                db.update(AcademiaContract.TABLE_NAME, values, whereClause, whereArgs);

            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public void atualizarListaDeAcademias(){

        try {
        }catch (Exception e){
            e.printStackTrace();
        }

//        Request.Method.GET,
//                Constantes.ACADEMIA_CONTROLLER,
//                null,

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                Constantes.ACADEMIA_CONTROLLER,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray retorno = response.getJSONArray("arrAcademias");
                            if(retorno.length() > 0) {
                                ArrayList<Academia> arrAcademias = new ArrayList<>();
                                for (int i = 0; i < retorno.length(); i++) {
                                    JSONObject obj = retorno.getJSONObject(i);
                                    String data = retiraZeroEsquedaData(obj.getString("dtUltimoPagamento"));
                                    Boolean pagou = false;
                                    if(obj.getInt("pagou") == 1){
                                        pagou = true;
                                    }
                                    Academia ac = new Academia(obj.getString("cnpj"), obj.getString("nomeFantasia"), obj.getString("telefone"), pagou, data);
                                    arrAcademias.add(ac);
                                }

                                salvarAcademiasEmMassa(arrAcademias);
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

    public void buscaDadosAcademiaServidor(final Context context){

        JSONObject massaDados =  new JSONObject();
        String cnpj = "";
        try {
            SharedPreferences sp = context.getSharedPreferences(Constantes.SHARED_PREFERENCES_LOGIN, Context.MODE_PRIVATE);
            cnpj = sp.getString(Constantes.CNPJ_ACADEMIA_LOGADA, "");

            massaDados.put("id", cnpj);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if(!cnpj.equals("")) {

            JsonObjectRequest request = new JsonObjectRequest(
                    Request.Method.GET,
                    Constantes.ACADEMIA_CONTROLLER + "/"+cnpj,
                    null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                JSONObject obj = response.getJSONObject("ACADEMIA");
                                String data = AcademiaDAO.getInstance(context).retiraZeroEsquedaData(obj.getString("dtUltimoPagamento"));
                                Boolean pagou = false;
                                if (obj.getInt("pagou") == 1) {
                                    pagou = true;
                                }

                                Academia ac = new Academia(obj.getString("cnpj"), obj.getString("nomeFantasia"), obj.getString("telefone"), pagou, data);
                                AcademiaDAO.getInstance(context).atualizarAcademia(ac);

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

            VolleySingleton.getInstance(context).addToRequestQueue(request);
        }
    }

    public String retiraZeroEsquedaData(String data) {
        String[] dtInicio = data.split("-");
        int indiceDia = dtInicio[1].indexOf("0");

        if(indiceDia == 0) {
            dtInicio[1] = dtInicio[1].replace("0", "");
        }

        int indiceMes = dtInicio[2].indexOf("0");

        if(indiceMes == 0) {
            dtInicio[2] = dtInicio[2].replace("0", "");
        }

        String retorno = dtInicio[0] + "-" + dtInicio[1] + "-" + dtInicio[2];
        return retorno;
    }

    public List<Academia> listar(boolean spinner) {

        String[] columns = {
                AcademiaContract.columns.CNPJ,
                AcademiaContract.columns.NOME_FANTASIA,
                AcademiaContract.columns.TELEFONE,
                AcademiaContract.columns.ULTIMO_ID_INSERT,
                AcademiaContract.columns.ULTIMO_ID_DELETE,
                AcademiaContract.columns.ULTIMO_ID_UPDATE,
                AcademiaContract.columns.PAGOU,
                AcademiaContract.columns.DT_ULTIMO_PAGAMENTO

        };

        List<Academia> academias = new ArrayList<>();

        if(spinner){
            academias.add(new Academia("", "Selecione...", "", false, ""));
        }

        try (Cursor c = db.query(AcademiaContract.TABLE_NAME, columns, null, null, null, null, AcademiaContract.columns.NOME_FANTASIA)) {
            if (c.moveToFirst()) {
                do {
                    Academia a = fromCursor(c);
                    academias.add(a);
                } while (c.moveToNext());
            }

            return academias;
        }

    }

    public Academia retornaAcademiaPorCnpj(String cnpj) {

        String[] columns = {
                AcademiaContract.columns.CNPJ,
                AcademiaContract.columns.NOME_FANTASIA,
                AcademiaContract.columns.TELEFONE,
                AcademiaContract.columns.ULTIMO_ID_INSERT,
                AcademiaContract.columns.ULTIMO_ID_DELETE,
                AcademiaContract.columns.ULTIMO_ID_UPDATE,
                AcademiaContract.columns.PAGOU,
                AcademiaContract.columns.DT_ULTIMO_PAGAMENTO
        };

        Academia academia = null;

        String whereClause = AcademiaContract.columns.CNPJ + " = ? ";
        String[] whereArgs = {cnpj};

        try (Cursor c = db.query(AcademiaContract.TABLE_NAME, columns, whereClause, whereArgs, null, null, AcademiaContract.columns.NOME_FANTASIA)) {
            if (c.moveToFirst()) {
                academia = fromCursor(c);
            }

            return academia;
        }

    }

    public long salvarAcademiasEmMassa(ArrayList<Academia> arrAcademias) throws Exception{
        long retorno = 0;

        for (Academia ac : arrAcademias) {
            if(!verificaSeJaExisteNoBanco(ac)) {
                try {
                    ContentValues values = new ContentValues();
                    values.put(AcademiaContract.columns.CNPJ, ac.getCnpj());
                    values.put(AcademiaContract.columns.NOME_FANTASIA, ac.getNomeFantasia());
                    values.put(AcademiaContract.columns.TELEFONE, ac.getTelefone());
                    values.put(AcademiaContract.columns.ULTIMO_ID_INSERT, ac.getUltimoIdInsert());
                    values.put(AcademiaContract.columns.ULTIMO_ID_DELETE, ac.getUltimoIdDelete());
                    values.put(AcademiaContract.columns.ULTIMO_ID_UPDATE, ac.getUltimoIdUpdate());
                    values.put(AcademiaContract.columns.PAGOU, ac.getPagou());
                    values.put(AcademiaContract.columns.DT_ULTIMO_PAGAMENTO, ac.getDtUltimoPagamento());

                    retorno = db.insert(AcademiaContract.TABLE_NAME, null, values);
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new Exception("id repetido");
                }
            }
        }

        return retorno;
    }

    public Boolean verificaSeJaExisteNoBanco(Academia ac){
        Boolean retorno = false;

        String[] columns = {
                AcademiaContract.columns.CNPJ,
                AcademiaContract.columns.NOME_FANTASIA,
                AcademiaContract.columns.TELEFONE
        };

        String whereClause = AcademiaContract.columns.CNPJ + " = ? ";
        String[] whereArgs = {ac.getCnpj()};

        Academia academia = null;

        try (Cursor c = db.query(AcademiaContract.TABLE_NAME, columns, whereClause, whereArgs, null, null, AcademiaContract.columns.NOME_FANTASIA)) {
            if (c.moveToFirst()) {
                retorno = true;
            }
        }

        return retorno;
    }

    public void atualizarUltimoIdAlteradoAcad(Integer ultIdAlt, String acao) throws Exception{
        SharedPreferences sp = contextBase.getSharedPreferences(Constantes.SHARED_PREFERENCES_LOGIN, Context.MODE_PRIVATE);
        String cnpj = sp.getString(Constantes.CNPJ_ACADEMIA_LOGADA, "");

        if(!cnpj.isEmpty()) {
            try {
                ContentValues values = new ContentValues();

                switch (acao){
                    case Constantes.INSERT:
                            values.put(AcademiaContract.columns.ULTIMO_ID_INSERT, ultIdAlt);
                        break;
                    case Constantes.DELETE:
                            values.put(AcademiaContract.columns.ULTIMO_ID_DELETE, ultIdAlt);
                        break;
                    case Constantes.UPDATE:
                            values.put(AcademiaContract.columns.ULTIMO_ID_UPDATE, ultIdAlt);
                        break;
                }

                String whereClause = AcademiaContract.columns.CNPJ + " = ? ";
                String[] whereArgs = {cnpj};

                db.update(AcademiaContract.TABLE_NAME, values, whereClause, whereArgs);
            } catch (Exception e) {
                e.printStackTrace();
                throw new Exception("id repetido");
            }
        }
    }

    private static Academia fromCursor(Cursor c) {
        String cnpj = c.getString(c.getColumnIndex(AcademiaContract.columns.CNPJ));
        String nomeFantasia = c.getString(c.getColumnIndex(AcademiaContract.columns.NOME_FANTASIA));
        String telefone = c.getString(c.getColumnIndex(AcademiaContract.columns.TELEFONE));
        Integer ultimoIdInsert = c.getInt(c.getColumnIndex(AcademiaContract.columns.ULTIMO_ID_INSERT));
        Integer ultimoIdDelete = c.getInt(c.getColumnIndex(AcademiaContract.columns.ULTIMO_ID_DELETE));
        Integer ultimoIdUpdate = c.getInt(c.getColumnIndex(AcademiaContract.columns.ULTIMO_ID_UPDATE));

        Integer intPagou = c.getInt(c.getColumnIndex(AcademiaContract.columns.PAGOU));
        Boolean pagou = false;
        if (intPagou == 1){
            pagou = true;
        }

        String dtUltimoPagamento = c.getString(c.getColumnIndex(AcademiaContract.columns.DT_ULTIMO_PAGAMENTO));

        return new Academia(cnpj, nomeFantasia, telefone, ultimoIdInsert, ultimoIdDelete, ultimoIdUpdate, pagou, dtUltimoPagamento);
    }


}
