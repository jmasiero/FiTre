package com.masidev.fitre.data.dao;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.masidev.fitre.constante.Constantes;
import com.masidev.fitre.data.contract.PersonalAcademiaContract;
import com.masidev.fitre.data.contract.PersonalContract;
import com.masidev.fitre.data.db.DBHelper;
import com.masidev.fitre.data.entidade.Academia;
import com.masidev.fitre.data.entidade.Alteracao;
import com.masidev.fitre.data.entidade.Personal;
import com.masidev.fitre.data.entidade.PersonalAcademia;
import com.masidev.fitre.volley.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jmasiero on 22/09/15.
 */
public class PersonalDAO {
    private static PersonalDAO instance;
    private SQLiteDatabase db;
    private Context contextBase;

    public static PersonalDAO getInstance(Context context){
        if(instance == null){
            instance = new PersonalDAO(context.getApplicationContext());
        }
        return instance;
    }

    private PersonalDAO(Context context){
        DBHelper dbHelper = DBHelper.getInstance(context);
        db = dbHelper.getWritableDatabase();
        contextBase = context;
    }

    public long salvar(Personal p, Boolean registra) throws Exception{
        long retorno = 0;

        if(!p.getNome().isEmpty() || !p.getCPF().isEmpty() || !p.getCREF().isEmpty() || !p.getSenha().isEmpty()){
            try {
                ContentValues values = new ContentValues();
                values.put(PersonalContract.columns.CPF, p.getCPF());
                values.put(PersonalContract.columns.NOME, p.getNome());
                values.put(PersonalContract.columns.CREF, p.getCREF());
                values.put(PersonalContract.columns.SENHA, p.getSenha());

                retorno = db.insert(PersonalContract.TABLE_NAME, null, values);

                if(registra) {
                    registraAlteracoes(Constantes.INSERT, p.getCPF());
                }

                SharedPreferences sp = contextBase.getSharedPreferences(Constantes.SHARED_PREFERENCES_LOGIN, Context.MODE_PRIVATE);
                p.setAcademia(new Academia(sp.getString(Constantes.CNPJ_ACADEMIA_LOGADA, "")));

                PersonalAcademia pa = new PersonalAcademia(p.getAcademia().getCnpj(), p.getCPF());
                PersonalAcademiaDAO.getInstance(contextBase).salvar(pa);
            }catch (Exception e){
                e.printStackTrace();
                throw new Exception("cpf repetido");
            }
        }
        return retorno;
    }

    public long salvarSemRegistrarAlteracao(Personal p) throws Exception{
        long retorno = 0;

        if(!p.getNome().isEmpty() || !p.getCPF().isEmpty() || !p.getCREF().isEmpty() || !p.getSenha().isEmpty()){
            try {
                ContentValues values = new ContentValues();
                values.put(PersonalContract.columns.CPF, p.getCPF());
                values.put(PersonalContract.columns.NOME, p.getNome());
                values.put(PersonalContract.columns.CREF, p.getCREF());
                values.put(PersonalContract.columns.SENHA, p.getSenha());

                retorno = db.insert(PersonalContract.TABLE_NAME, null, values);

                PersonalAcademia pa = new PersonalAcademia(p.getAcademia().getCnpj(), p.getCPF());
                PersonalAcademiaDAO.getInstance(contextBase).salvarSemRegistrarAlteracoes(pa);
            }catch (Exception e){
                e.printStackTrace();
                throw new Exception("id repetido");
            }
        }
        return retorno;
    }

    private void registraAlteracoes(String acao, String chave) throws Exception {
        Alteracao a = new Alteracao();
        a.setAcao(acao);
        a.setAtivo(1);
        a.setChave(chave);

        AlteracaoDAO.getInstance(contextBase).salvar(a, PersonalContract.TABLE_NAME);
    }

    public List<Personal> listar(boolean spinner) {

        String[] columns = {
                PersonalContract.columns.CPF,
                PersonalContract.columns.NOME,
                PersonalContract.columns.CREF
        };

        List<Personal> treinadores = new ArrayList<>();

        if(spinner){
            treinadores.add(new Personal("", "Selecione...", ""));
        }

        try (Cursor c = db.query(PersonalContract.TABLE_NAME, columns, null, null, null, null, PersonalContract.columns.NOME)) {
            if (c.moveToFirst()) {
                do {
                    Personal p = PersonalDAO.fromCursor(c);
                    treinadores.add(p);
                } while (c.moveToNext());
            }

            return treinadores;
        }

    }

    public List<Personal> listarComSenha() {

        String[] columns = {
                PersonalContract.columns.CPF,
                PersonalContract.columns.NOME,
                PersonalContract.columns.CREF,
                PersonalContract.columns.SENHA
        };

        List<Personal> treinadores = new ArrayList<>();

        try (Cursor c = db.query(PersonalContract.TABLE_NAME, columns, null, null, null, null, PersonalContract.columns.NOME)) {
            if (c.moveToFirst()) {
                do {
                    Personal p = PersonalDAO.fromCursorComSenha(c);
                    treinadores.add(p);
                } while (c.moveToNext());
            }

            return treinadores;
        }

    }

    public List<Personal> listaFiltrada(String filtro) {

        String[] columns = {
                PersonalContract.columns.CPF,
                PersonalContract.columns.NOME,
                PersonalContract.columns.CREF
        };

        String whereClause = PersonalContract.columns.NOME + " like ? ";
        String[] whereArgs = {"%" + filtro + "%"};

        List<Personal> treinadores = new ArrayList<>();

        try (Cursor c = db.query(PersonalContract.TABLE_NAME, columns, whereClause, whereArgs, null, null, PersonalContract.columns.NOME)) {
            if (c.moveToFirst()) {
                do {
                    Personal p = PersonalDAO.fromCursor(c);
                    treinadores.add(p);
                } while (c.moveToNext());
            }

            return treinadores;
        }

    }

    public Personal retornaPersonal(String cpf) {

        String[] columns = {
                PersonalContract.columns.CPF,
                PersonalContract.columns.NOME,
                PersonalContract.columns.CREF,
                PersonalContract.columns.SENHA
        };

        String whereClause = PersonalContract.columns.CPF + " = ? ";
        String[] whereArgs = {cpf};

        Personal personal = null;

        try (Cursor c = db.query(PersonalContract.TABLE_NAME, columns, whereClause, whereArgs, null, null, PersonalContract.columns.CPF)) {
            if (c.moveToFirst()) {
                personal = fromCursorComSenha(c);
            }

        }

        return personal;
    }

//    public Personal validar(Personal p) {
//
//        String[] columns = {
//                PersonalContract.columns.CPF,
//                PersonalContract.columns.NOME,
//                PersonalContract.columns.CREF,
//                PersonalContract.columns.SENHA
//        };
//
//        String whereClause = PersonalContract.columns.CPF+" = ? AND " + PersonalContract.columns.SENHA+" = ? ";
//        String [] whereArgs = {p.getCPF(), p.getSenha()};
//
//        Personal retorno = null;
//
//        try (Cursor c = db.query(PersonalContract.TABLE_NAME, columns, whereClause, whereArgs, null, null, PersonalContract.columns.NOME)) {
//            if (c.moveToFirst()) {
//                retorno = PersonalDAO.fromCursor(c);
//            }
//        }
//
//        return retorno;
//    }

    //fluxo de validação
    public Personal validar(Personal p){
        Personal retorno = validaNoBancoInterno(p);

        if(retorno == null){
            //valida no servidor e insere no banco do tablet
            verificaPersonalNoServidor(p);

            //verifica novamente no tablet
            retorno = validaNoBancoInterno(p);
        }

        return retorno;
    }

    public Personal validaNoBancoInterno(Personal p) {
         String whereClause = " WHERE p.Cpf = ? AND p.Senha = ? AND pa.CnpjAcademia = ? ";
        String[] whereArgs = {p.getCPF(), p.getSenha(), p.getAcademia().getCnpj()};

        // query manual
        String query = "SELECT p.Cpf," +
                            " p.Nome," +
                            " p.Cref," +
                            " p.Senha " +
                        " FROM " + PersonalContract.TABLE_NAME + " p " +
                        " JOIN " + PersonalAcademiaContract.TABLE_NAME + " pa ON pa.CpfPersonal = p.Cpf " +
                        whereClause +
                        " GROUP BY p.Cpf " +
                        " ORDER BY p.Nome ";

        Personal personal = null;

        //fim query manual
        try (Cursor c = db.rawQuery(query, whereArgs)) {
            if (c.moveToFirst()) {
                personal = fromCursor(c);
            }

            return personal;
        }

    }

    public void verificaPersonalNoServidor(Personal personal){
        JSONObject massaDados = new JSONObject();

        try {
            massaDados.put("PERSONAL_SENHA", personal.getSenha());
            massaDados.put("PERSONAL_CPF", personal.getCPF());
            massaDados.put("ACADEMIA_CNPJ", personal.getAcademia().getCnpj());
        }catch (Exception e){
            e.printStackTrace();
        }

        final JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                Constantes.PERSONAL_CONTROLLER,
                massaDados,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject jsonPersonal = response.getJSONObject("personal");
                            JSONObject jsonAcademia = response.getJSONObject("academia");

                            if(jsonPersonal.length() > 0) {
                                Personal p = new Personal(jsonPersonal.getString("cpf"), jsonPersonal.getString("nome"), jsonPersonal.getString("cref"), jsonPersonal.getString("senha"));
                                p.setAcademia(new Academia(jsonAcademia.getString("cnpj")));
                                if(!p.getCPF().isEmpty() && !p.getCPF().equals("null")) {
                                    Toast.makeText(contextBase, "Personal encontrado no servidor, tente novamente", Toast.LENGTH_LONG).show();
                                    salvarSemRegistrarAlteracao(p);
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





    private static Personal fromCursor(Cursor c) {
        String cpf = c.getString(c.getColumnIndex(PersonalContract.columns.CPF));
        String nome = c.getString(c.getColumnIndex(PersonalContract.columns.NOME));
        String cref = c.getString(c.getColumnIndex(PersonalContract.columns.CREF));
        return new Personal(cpf, nome, cref);
    }

    private static Personal fromCursorComSenha(Cursor c) {
        String cpf = c.getString(c.getColumnIndex(PersonalContract.columns.CPF));
        String nome = c.getString(c.getColumnIndex(PersonalContract.columns.NOME));
        String cref = c.getString(c.getColumnIndex(PersonalContract.columns.CREF));
        String senha = c.getString(c.getColumnIndex(PersonalContract.columns.SENHA));

        Personal p = new Personal();
        p.setCPF(cpf);
        p.setCREF(cref);
        p.setNome(nome);
        p.setSenha(senha);

        return p;
    }

}
