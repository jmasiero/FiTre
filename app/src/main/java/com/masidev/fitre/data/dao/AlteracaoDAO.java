package com.masidev.fitre.data.dao;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.masidev.fitre.constante.Constantes;
import com.masidev.fitre.data.contract.AlteracaoContract;
import com.masidev.fitre.data.db.DBHelper;
import com.masidev.fitre.data.entidade.Academia;
import com.masidev.fitre.data.entidade.Alteracao;
import com.masidev.fitre.data.entidade.Personal;
import com.masidev.fitre.data.entidade.Tabela;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by jmasiero on 05/04/16.
 */
public class AlteracaoDAO {

    private static AlteracaoDAO instance;
    private SQLiteDatabase db;
    private Context contextBase;

    public static AlteracaoDAO getInstance(Context context){
        if(instance == null){
            instance = new AlteracaoDAO(context.getApplicationContext());
        }

        return instance;
    }

    private AlteracaoDAO(Context context){
        DBHelper dbHelper = DBHelper.getInstance(context);
        db = dbHelper.getWritableDatabase();
        this.contextBase = context;
    }

    public long salvar(Alteracao a, String tabela) throws Exception{
        long retorno = 0;

        if(a.getAcao() == Constantes.DELETE){
            verificaInsertAtivoEApaga(a, tabela);
            verificaUpdateAtivoEApaga(a, tabela);
        }else if(a.getAcao() == Constantes.UPDATE){
            //verificaInsertAtivoEApaga(a, tabela);
            verificaUpdateAtivoEDesativa(a, tabela);
        }

        a.setTabela(TabelaDAO.getInstance(contextBase).retornaTabela(tabela));

        //Seta data de criação do registro
        Date data = new Date(System.currentTimeMillis());
        SimpleDateFormat formatarData = new SimpleDateFormat("yyyy-MM-dd");
        a.setData(formatarData.format(data));

        //recupera e seta o personal logado
        Personal p = new Personal();
        SharedPreferences sp = contextBase.getSharedPreferences(Constantes.SHARED_PREFERENCES_LOGIN, Context.MODE_PRIVATE);
        p.setCPF(sp.getString(Constantes.CPF_TREINADOR_LOGADO, ""));

        a.setPersonal(p);

        //recupera e seta o academia logada
        String cnpj = sp.getString(Constantes.CNPJ_ACADEMIA_LOGADA, "");
        Academia ac = new Academia(cnpj);

        a.setAcademia(ac);

        try {
            ContentValues values = new ContentValues();
            values.put(AlteracaoContract.columns.ACAO, a.getAcao());
            values.put(AlteracaoContract.columns.ATIVO, a.getAtivo());
            values.put(AlteracaoContract.columns.CHAVE, a.getChave());
            values.put(AlteracaoContract.columns.CPF_PERSONAL, a.getPersonal().getCPF());
            values.put(AlteracaoContract.columns.DATA, String.valueOf(a.getData()));
            values.put(AlteracaoContract.columns.ID_TABELA, a.getTabela().getId());
            values.put(AlteracaoContract.columns.CNPJ_ACADEMIA, a.getAcademia().getCnpj());

            retorno = db.insert(AlteracaoContract.TABLE_NAME, null, values);
        }catch (Exception e){
            e.printStackTrace();
            throw new Exception("id repetido");
        }

        return retorno;
    }

    private void verificaInsertAtivoEApaga(Alteracao a, String tabela) {
        Alteracao paramAlt = new Alteracao();
        String[] arrChave = a.getChave().split("/");

        paramAlt.setChave(arrChave[0]);
        paramAlt.setAtivo(1);

        Alteracao altExcluir = retornaAlteracaoPorChaveETabelaEAcao(paramAlt, tabela, Constantes.INSERT);

        if(altExcluir != null){
            try {
                excluirPorId(altExcluir);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public boolean excluir(Alteracao a) throws Exception {
        boolean retorno = false;

        String whereClause = AlteracaoContract.columns.CHAVE + " = ? ";
        String [] whereArgs = {a.getChave()};

        try{
            db.delete(AlteracaoContract.TABLE_NAME, whereClause, whereArgs);

            retorno = true;
        }catch (Exception e){
            e.printStackTrace();
            throw new Exception("Ocorreu um erro na exclusão");
        }

        return retorno;
    }

    public boolean excluirPorId(Alteracao a) throws Exception {
        boolean retorno = false;

        String whereClause = AlteracaoContract.columns.ID + " = ? ";
        String [] whereArgs = {String.valueOf(a.getId())};

        try{
            db.delete(AlteracaoContract.TABLE_NAME, whereClause, whereArgs);

            retorno = true;
        }catch (Exception e){
            e.printStackTrace();
            throw new Exception("Ocorreu um erro na exclusão");
        }

        return retorno;
    }

    public Integer desativarPorId(Alteracao a) throws Exception {
        int retorno = 0;

        try {
            ContentValues values = new ContentValues();
            values.put(AlteracaoContract.columns.ATIVO, Constantes.BANCO_FALSE);

            String whereClause = AlteracaoContract.columns.ID + " = ? ";
            String [] whereArgs = {String.valueOf(a.getId())};

            retorno = db.update(AlteracaoContract.TABLE_NAME, values, whereClause, whereArgs);

        }catch (Exception e){
            e.printStackTrace();
            throw new Exception("Ocorreu um problema na inativação da alteração");
        }

        return retorno;
    }

    public ArrayList<Alteracao> listar() {

        String[] columns = {
                AlteracaoContract.columns.ID,
                AlteracaoContract.columns.ID_TABELA,
                AlteracaoContract.columns.ACAO,
                AlteracaoContract.columns.ATIVO,
                AlteracaoContract.columns.CHAVE,
                AlteracaoContract.columns.CPF_PERSONAL,
                AlteracaoContract.columns.DATA,
                AlteracaoContract.columns.CNPJ_ACADEMIA
        };

        ArrayList<Alteracao> alteracoes = new ArrayList<>();

        try (Cursor c = db.query(AlteracaoContract.TABLE_NAME, columns, null, null, null, null, AlteracaoContract.columns.ID)) {
            if (c.moveToFirst()) {
                do {
                    Alteracao a = fromCursor(c);
                    Tabela tb = TabelaDAO.getInstance(contextBase).retornaTabelaPorId(a.getTabela().getId());
                    a.setTabela(tb);
                    alteracoes.add(a);
                } while (c.moveToNext());
            }

            return alteracoes;
        }

    }

    public ArrayList<Alteracao> listarPorAcao(String acao) {

        String[] columns = {
                AlteracaoContract.columns.ID,
                AlteracaoContract.columns.ID_TABELA,
                AlteracaoContract.columns.ACAO,
                AlteracaoContract.columns.ATIVO,
                AlteracaoContract.columns.CHAVE,
                AlteracaoContract.columns.CPF_PERSONAL,
                AlteracaoContract.columns.DATA,
                AlteracaoContract.columns.CNPJ_ACADEMIA
        };

        ArrayList<Alteracao> alteracoes = new ArrayList<>();

        String whereClause = AlteracaoContract.columns.ATIVO + " = ? AND " +
                             AlteracaoContract.columns.ACAO + " = ? ";
        String [] whereArgs = {String.valueOf(1), acao};

        try (Cursor c = db.query(AlteracaoContract.TABLE_NAME, columns, whereClause, whereArgs, null, null, AlteracaoContract.columns.ID)) {
            if (c.moveToFirst()) {
                do {
                    Alteracao a = fromCursor(c);
                    Tabela tb = TabelaDAO.getInstance(contextBase).retornaTabelaPorId(a.getTabela().getId());
                    a.setTabela(tb);
                    alteracoes.add(a);
                } while (c.moveToNext());
            }

            return alteracoes;
        }

    }

    public Alteracao retornaAlteracaoPorChaveETabela(String chave, String tabela) {

        String[] columns = {
                AlteracaoContract.columns.ID,
                AlteracaoContract.columns.ID_TABELA,
                AlteracaoContract.columns.ACAO,
                AlteracaoContract.columns.ATIVO,
                AlteracaoContract.columns.CHAVE,
                AlteracaoContract.columns.CPF_PERSONAL,
                AlteracaoContract.columns.DATA,
                AlteracaoContract.columns.CNPJ_ACADEMIA
        };

        Tabela tb = TabelaDAO.getInstance(contextBase).retornaTabela(tabela);

        Alteracao alteracao = null;

        if(tb != null) {
            String whereClause = AlteracaoContract.columns.ATIVO + " = ? AND " +
                    AlteracaoContract.columns.CHAVE + " = ? AND " +
                    AlteracaoContract.columns.ID_TABELA + " = ? ";
            String[] whereArgs = {String.valueOf(1), chave, String.valueOf(tb.getId())};

            try (Cursor c = db.query(AlteracaoContract.TABLE_NAME, columns, whereClause, whereArgs, null, null, AlteracaoContract.columns.ID)) {
                if (c.moveToFirst()) {
                    alteracao = fromCursor(c);
                    alteracao.setTabela(tb);
                }

            }
        }

        return alteracao;
    }

    public void verificaUpdateAtivoEApaga(Alteracao a, String tabela){
        Alteracao paramAlt = new Alteracao();
        String[] arrChave = a.getChave().split("/");

        paramAlt.setChave(arrChave[0]);
        paramAlt.setAtivo(1);

        Alteracao altExcluir = retornaAlteracaoPorChaveETabelaEAcao(paramAlt, tabela, Constantes.UPDATE);

        if(altExcluir != null){
            try {
                excluirPorId(altExcluir);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void verificaUpdateAtivoEDesativa(Alteracao a, String tabela){
        Alteracao altDesativar = retornaAlteracaoPorChaveETabelaEAcao(a, tabela, Constantes.UPDATE);

        if(altDesativar != null){
            try {
                desativarPorId(altDesativar);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public Alteracao retornaAlteracaoPorChaveETabelaEAcao(Alteracao a, String tabela, String acao) {

        String[] columns = {
                AlteracaoContract.columns.ID,
                AlteracaoContract.columns.ID_TABELA,
                AlteracaoContract.columns.ACAO,
                AlteracaoContract.columns.ATIVO,
                AlteracaoContract.columns.CHAVE,
                AlteracaoContract.columns.CPF_PERSONAL,
                AlteracaoContract.columns.DATA,
                AlteracaoContract.columns.CNPJ_ACADEMIA
        };

        Tabela tb = TabelaDAO.getInstance(contextBase).retornaTabela(tabela);

        Alteracao alteracao = null;

        if(tb != null) {
            String whereClause = AlteracaoContract.columns.ATIVO + " = ? AND " +
                                AlteracaoContract.columns.CHAVE + " = ? AND " +
                                AlteracaoContract.columns.ACAO + " = ? AND " +
                                AlteracaoContract.columns.ID_TABELA + " = ? ";

            String[] whereArgs = {String.valueOf(1), a.getChave(), acao, String.valueOf(tb.getId())};

            try (Cursor c = db.query(AlteracaoContract.TABLE_NAME, columns, whereClause, whereArgs, null, null, AlteracaoContract.columns.ID)) {
                if (c.moveToFirst()) {
                    alteracao = fromCursor(c);
                    alteracao.setTabela(tb);
                }
            }
        }

        return alteracao;
    }

    public boolean inativar(String acao) throws Exception{
        boolean retorno = false;

        if(!acao.isEmpty())
        {
            try {
                ContentValues values = new ContentValues();
                values.put(AlteracaoContract.columns.ATIVO, Constantes.BANCO_FALSE);

                String whereClause = AlteracaoContract.columns.ACAO + " = ?";
                String [] whereArgs = {acao};

                db.update(AlteracaoContract.TABLE_NAME, values, whereClause, whereArgs);
                retorno = true;

            }catch (Exception e){
                e.printStackTrace();
                throw new Exception("id repetido");
            }
        }
        return retorno;
    }

    private static Alteracao fromCursor(Cursor c) {
        Integer id = c.getInt(c.getColumnIndex(AlteracaoContract.columns.ID));
        Integer idTabela = c.getInt(c.getColumnIndex(AlteracaoContract.columns.ID_TABELA));
        Tabela tb = new Tabela();
        tb.setId(idTabela);

        String acao = c.getString(c.getColumnIndex(AlteracaoContract.columns.ACAO));
        Integer ativo = c.getInt(c.getColumnIndex(AlteracaoContract.columns.ATIVO));
        String chave = c.getString(c.getColumnIndex(AlteracaoContract.columns.CHAVE));
        String cpfPersonal = c.getString(c.getColumnIndex(AlteracaoContract.columns.CPF_PERSONAL));
        Personal p = new Personal(cpfPersonal);

        String data = c.getString(c.getColumnIndex(AlteracaoContract.columns.DATA));
        Academia ac = new Academia(c.getString(c.getColumnIndex(AlteracaoContract.columns.CNPJ_ACADEMIA)));

        return new Alteracao(id, tb, acao, chave, ativo, p, data, ac);
    }

}
