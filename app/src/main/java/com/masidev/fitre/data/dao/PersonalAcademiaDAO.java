package com.masidev.fitre.data.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.masidev.fitre.constante.Constantes;
import com.masidev.fitre.data.contract.PersonalAcademiaContract;
import com.masidev.fitre.data.db.DBHelper;
import com.masidev.fitre.data.entidade.Alteracao;
import com.masidev.fitre.data.entidade.PersonalAcademia;

/**
 * Created by jmasiero on 15/04/16.
 */
public class PersonalAcademiaDAO {
    private static PersonalAcademiaDAO instance;
    private SQLiteDatabase db;
    private Context contextBase;

    public static PersonalAcademiaDAO getInstance(Context context){
        if(instance == null){
            instance = new PersonalAcademiaDAO(context.getApplicationContext());
        }

        return instance;
    }

    private PersonalAcademiaDAO(Context context){
        DBHelper dbHelper = DBHelper.getInstance(context);
        db = dbHelper.getWritableDatabase();
        this.contextBase = context;
    }

    public long salvar(PersonalAcademia pa) throws Exception{
        long retorno = 0;
        try {
            ContentValues values = new ContentValues();
            values.put(PersonalAcademiaContract.columns.CNPJ_ACADEMIA, pa.getCnpjAcademia());
            values.put(PersonalAcademiaContract.columns.CPF_PERSONAL, pa.getCpfPersonal());

            retorno = db.insert(PersonalAcademiaContract.TABLE_NAME, null, values);

            registraAlteracoes(Constantes.INSERT, String.valueOf(retorno));
        }catch (Exception e){
            e.printStackTrace();
            throw new Exception("id repetido");
        }

        return retorno;
    }

    public long salvarSemRegistrarAlteracoes(PersonalAcademia pa) throws Exception{
        long retorno = 0;
        try {
            ContentValues values = new ContentValues();
            values.put(PersonalAcademiaContract.columns.CNPJ_ACADEMIA, pa.getCnpjAcademia());
            values.put(PersonalAcademiaContract.columns.CPF_PERSONAL, pa.getCpfPersonal());

            retorno = db.insert(PersonalAcademiaContract.TABLE_NAME, null, values);

        }catch (Exception e){
            e.printStackTrace();
            throw new Exception("id repetido");
        }

        return retorno;
    }

    public PersonalAcademia retornaPersonalAcademia(String cpf) {

        String[] columns = {
                PersonalAcademiaContract.columns.CNPJ_ACADEMIA,
                PersonalAcademiaContract.columns.CPF_PERSONAL,
                PersonalAcademiaContract.columns.ID
        };

        String whereClause = PersonalAcademiaContract.columns.CPF_PERSONAL + " = ? ";
        String[] whereArgs = {cpf};

        PersonalAcademia pa = null;

        try (Cursor c = db.query(PersonalAcademiaContract.TABLE_NAME, columns, whereClause, whereArgs, null, null, PersonalAcademiaContract.columns.CPF_PERSONAL)) {
            if (c.moveToFirst()) {
                pa = fromCursor(c);
            }

        }

        return pa;
    }

    private static PersonalAcademia fromCursor(Cursor c) {
        String cpf = c.getString(c.getColumnIndex(PersonalAcademiaContract.columns.CPF_PERSONAL));
        String cnpj = c.getString(c.getColumnIndex(PersonalAcademiaContract.columns.CNPJ_ACADEMIA));
        Integer id = c.getInt(c.getColumnIndex(PersonalAcademiaContract.columns.ID));
        return new PersonalAcademia(id, cnpj, cpf);
    }

    private void registraAlteracoes(String acao, String chave) throws Exception {
        Alteracao a = new Alteracao();
        a.setAcao(acao);
        a.setAtivo(1);
        a.setChave(chave);

        AlteracaoDAO.getInstance(contextBase).salvar(a, PersonalAcademiaContract.TABLE_NAME);
    }
}
