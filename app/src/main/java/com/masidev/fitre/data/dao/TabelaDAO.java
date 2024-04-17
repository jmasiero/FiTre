package com.masidev.fitre.data.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.masidev.fitre.data.contract.AcademiaContract;
import com.masidev.fitre.data.contract.AlteracaoContract;
import com.masidev.fitre.data.contract.AlunoContract;
import com.masidev.fitre.data.contract.FrequenciaContract;
import com.masidev.fitre.data.contract.MusculoContract;
import com.masidev.fitre.data.contract.MusculoTreinadoSemanaContract;
import com.masidev.fitre.data.contract.PersonalAcademiaContract;
import com.masidev.fitre.data.contract.PersonalContract;
import com.masidev.fitre.data.contract.ProgramaMusculoContract;
import com.masidev.fitre.data.contract.ProgramaTreinoContract;
import com.masidev.fitre.data.contract.SemanaTreinoProgressivoContract;
import com.masidev.fitre.data.contract.TabelaContract;
import com.masidev.fitre.data.contract.TreinoBaseContract;
import com.masidev.fitre.data.contract.TreinoContract;
import com.masidev.fitre.data.contract.TreinoProgressivoContract;
import com.masidev.fitre.data.db.DBHelper;
import com.masidev.fitre.data.entidade.Tabela;

import java.util.ArrayList;

/**
 * Created by jmasiero on 05/04/16.
 */
public class TabelaDAO {
    private static TabelaDAO instance;
    private SQLiteDatabase db;

    public static TabelaDAO getInstance(Context context){
        if(instance == null){
            instance = new TabelaDAO(context.getApplicationContext());
        }
        return instance;
    }

    private TabelaDAO(Context context){
        DBHelper dbHelper = DBHelper.getInstance(context);
        db = dbHelper.getWritableDatabase();
    }

    public long salvarTabelas() throws Exception{
        long retorno = 0;

        if(listar().size() < 1) {

            ArrayList<Tabela> lstTabelas = new ArrayList<>();

            Tabela musculoTreinadoSemana = new Tabela();
            musculoTreinadoSemana.setNome(MusculoTreinadoSemanaContract.TABLE_NAME);
            musculoTreinadoSemana.setTipoChave("Integer");
            lstTabelas.add(musculoTreinadoSemana);

            Tabela aluno = new Tabela();
            aluno.setNome(AlunoContract.TABLE_NAME);
            aluno.setTipoChave("String");
            lstTabelas.add(aluno);

            Tabela treino = new Tabela();
            treino.setNome(TreinoContract.TABLE_NAME);
            treino.setTipoChave("Integer");
            lstTabelas.add(treino);

            Tabela musculo = new Tabela();
            musculo.setNome(MusculoContract.TABLE_NAME);
            musculo.setTipoChave("Integer");
            lstTabelas.add(musculo);

            Tabela treinoBase = new Tabela();
            treinoBase.setNome(TreinoBaseContract.TABLE_NAME);
            treinoBase.setTipoChave("Integer");
            lstTabelas.add(treinoBase);

            Tabela personal = new Tabela();
            personal.setNome(PersonalContract.TABLE_NAME);
            personal.setTipoChave("String");
            lstTabelas.add(personal);

            Tabela alteracao = new Tabela();
            alteracao.setNome(AlteracaoContract.TABLE_NAME);
            alteracao.setTipoChave("Integer");
            lstTabelas.add(alteracao);

            Tabela tabela = new Tabela();
            tabela.setNome(TabelaContract.TABLE_NAME);
            tabela.setTipoChave("Integer");
            lstTabelas.add(tabela);

            Tabela personalAcademia = new Tabela();
            personalAcademia.setNome(PersonalAcademiaContract.TABLE_NAME);
            personalAcademia.setTipoChave("Integer");
            lstTabelas.add(personalAcademia);

            Tabela academia = new Tabela();
            academia.setNome(AcademiaContract.TABLE_NAME);
            academia.setTipoChave("Integer");
            lstTabelas.add(academia);

            Tabela treinoProgressivo = new Tabela();
            treinoProgressivo.setNome(TreinoProgressivoContract.TABLE_NAME);
            treinoProgressivo.setTipoChave("Integer");
            lstTabelas.add(treinoProgressivo);

            Tabela semanaTreinoProgressivo = new Tabela();
            semanaTreinoProgressivo.setNome(SemanaTreinoProgressivoContract.TABLE_NAME);
            semanaTreinoProgressivo.setTipoChave("Integer");
            lstTabelas.add(semanaTreinoProgressivo);

            Tabela programaTreino = new Tabela();
            programaTreino.setNome(ProgramaTreinoContract.TABLE_NAME);
            programaTreino.setTipoChave("Integer");
            lstTabelas.add(programaTreino);

            Tabela programaMusculo = new Tabela();
            programaMusculo.setNome(ProgramaMusculoContract.TABLE_NAME);
            programaMusculo.setTipoChave("Integer");
            lstTabelas.add(programaMusculo);

            Tabela frequencia = new Tabela();
            frequencia.setNome(FrequenciaContract.TABLE_NAME);
            frequencia.setTipoChave("Integer");
            lstTabelas.add(frequencia);

            try {
                for (Tabela tb : lstTabelas) {
                    ContentValues values = new ContentValues();

                    values.put(TabelaContract.columns.NOME, tb.getNome());
                    values.put(TabelaContract.columns.TIPO_CHAVE, tb.getTipoChave());

                    db.insert(TabelaContract.TABLE_NAME, null, values);
                }
                retorno = 1;
            } catch (Exception e) {
                retorno = 0;
                e.printStackTrace();
                throw new Exception("id repetido");
            }
        }
        return retorno;
    }

    public ArrayList<Tabela> listar() {

        String[] columns = {
                TabelaContract.columns.ID,
                TabelaContract.columns.NOME,
                TabelaContract.columns.TIPO_CHAVE
        };

        ArrayList<Tabela> tabelas = new ArrayList<>();

        try (Cursor c = db.query(TabelaContract.TABLE_NAME, columns, null, null, null, null, TabelaContract.columns.ID)) {
            if (c.moveToFirst()) {
                do {
                    Tabela tb = fromCursor(c);
                    tabelas.add(tb);
                } while (c.moveToNext());
            }

            return tabelas;
        }

    }

    public Tabela retornaTabela(String nomeTabela) {
        String[] columns = {
                TabelaContract.columns.NOME,
                TabelaContract.columns.ID,
                TabelaContract.columns.TIPO_CHAVE
        };

        String whereClause = TabelaContract.columns.NOME + " = ? ";
        String[] whereArgs = {nomeTabela};

        Tabela tb = null;

        try (Cursor c = db.query(TabelaContract.TABLE_NAME, columns, whereClause, whereArgs, null, null, TabelaContract.columns.NOME)) {
            if (c.moveToFirst()) {
                    tb = fromCursor(c);
            }

        }
        return tb;
    }

    public Tabela retornaTabelaPorId(Integer id) {
        String[] columns = {
                TabelaContract.columns.NOME,
                TabelaContract.columns.ID,
                TabelaContract.columns.TIPO_CHAVE
        };

        String whereClause = TabelaContract.columns.ID + " = ? ";
        String[] whereArgs = {String.valueOf(id)};

        Tabela tb = null;

        try (Cursor c = db.query(TabelaContract.TABLE_NAME, columns, whereClause, whereArgs, null, null, TabelaContract.columns.NOME)) {
            if (c.moveToFirst()) {
                tb = fromCursor(c);
            }

        }
        return tb;
    }

    public void atualizaNovaTabela(String tableName, String chave) {
        if(retornaTabela(tableName) == null){
            Tabela tb = new Tabela();
            tb.setNome(tableName);
            tb.setTipoChave(chave);

            try {
                ContentValues values = new ContentValues();

                values.put(TabelaContract.columns.NOME, tb.getNome());
                values.put(TabelaContract.columns.TIPO_CHAVE, tb.getTipoChave());

                db.insert(TabelaContract.TABLE_NAME, null, values);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public boolean excluirTudo() throws Exception{
        boolean retorno = false;

        try {

            db.delete(TabelaContract.TABLE_NAME, null, null);

            retorno = true;
        }catch (Exception e){
            e.printStackTrace();
            throw new Exception("Ocorreu um erro na exclusão");
        }
        return retorno;
    }

    private static Tabela fromCursor(Cursor c) {
        Integer id = c.getInt(c.getColumnIndex(TabelaContract.columns.ID));
        String nome = c.getString(c.getColumnIndex(TabelaContract.columns.NOME));
        String tipoChave = c.getString(c.getColumnIndex(TabelaContract.columns.TIPO_CHAVE));
        return new Tabela(id, nome, tipoChave);
    }
}
