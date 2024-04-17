package com.masidev.fitre.data.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.masidev.fitre.constante.Constantes;
import com.masidev.fitre.data.contract.AlunoContract;
import com.masidev.fitre.data.contract.MusculoContract;
import com.masidev.fitre.data.contract.TreinoBaseContract;
import com.masidev.fitre.data.contract.TreinoContract;
import com.masidev.fitre.data.db.DBHelper;
import com.masidev.fitre.data.entidade.Alteracao;
import com.masidev.fitre.data.entidade.Aluno;
import com.masidev.fitre.data.entidade.Musculo;
import com.masidev.fitre.data.entidade.Treino;
import com.masidev.fitre.data.entidade.TreinoBase;

import java.util.ArrayList;

/**
 * Created by jmasiero on 23/10/15.
 */
public class MusculoDAO {
    private static MusculoDAO instance;
    private SQLiteDatabase db;
    private Context contextBase;

    public static MusculoDAO getInstance(Context context){
        if(instance == null){
            instance = new MusculoDAO(context.getApplicationContext());
        }
        return instance;
    }

    private MusculoDAO(Context context){
        DBHelper dbHelper = DBHelper.getInstance(context);
        db = dbHelper.getWritableDatabase();
        contextBase = context;
    }

    public String salvar(Musculo m) throws Exception{
        String retorno;;

        try {
            ContentValues values = new ContentValues();
            values.put(MusculoContract.columns.CARGAS, m.getCarga());
            values.put(MusculoContract.columns.EXERCICIO, m.getExercicio());
            values.put(MusculoContract.columns.ID_TREINO, m.getIdTreino());
            values.put(MusculoContract.columns.ID_TREINO_BASE, m.getIdTreinoBase());
            values.put(MusculoContract.columns.INTERVALOS, m.getIntervalos());
            values.put(MusculoContract.columns.ORDEM, m.getOrdem());
            values.put(MusculoContract.columns.REPETICOES, m.getRepeticoes());
            values.put(MusculoContract.columns.SERIES, m.getSeries());
            values.put(MusculoContract.columns.TIPO_MUSCULO, m.getTipoMusculo());
            values.put(MusculoContract.columns.TMP_EXEC_FIM, m.getTmpExecFim());
            values.put(MusculoContract.columns.TMP_EXEC_INI, m.getTmpExecIni());
            db.insert(MusculoContract.TABLE_NAME, null, values);

            retorno = "1";
        }catch (Exception e){
            retorno = "0";
            e.printStackTrace();
            throw new Exception("id repetido");
        }

        return retorno;
    }

    public long alterar(Musculo m) throws Exception{
        long retorno = 0;

            try {
                ContentValues values = new ContentValues();
                values.put(MusculoContract.columns.ID, m.getId());
                values.put(MusculoContract.columns.CARGAS, m.getCarga());
                values.put(MusculoContract.columns.EXERCICIO, m.getExercicio());
                if(m.getIdTreino() != null){
                    values.put(MusculoContract.columns.ID_TREINO, m.getIdTreino());
                }

                if(m.getIdTreinoBase() != null){
                    values.put(MusculoContract.columns.ID_TREINO_BASE, m.getIdTreinoBase());
                }

                values.put(MusculoContract.columns.INTERVALOS, m.getIntervalos());
                values.put(MusculoContract.columns.ORDEM, m.getOrdem());
                values.put(MusculoContract.columns.REPETICOES, m.getRepeticoes());
                values.put(MusculoContract.columns.SERIES, m.getSeries());
                values.put(MusculoContract.columns.TIPO_MUSCULO, m.getTipoMusculo());
                values.put(MusculoContract.columns.TMP_EXEC_FIM, m.getTmpExecFim());
                values.put(MusculoContract.columns.TMP_EXEC_INI, m.getTmpExecIni());

                String whereClause = MusculoContract.columns.ID + " = ?";
                String [] whereArgs = {String.valueOf(m.getId())};

                retorno =  db.update(MusculoContract.TABLE_NAME, values, whereClause, whereArgs);

            }catch (Exception e){
                e.printStackTrace();
                throw new Exception("ocorreu um erro na alteração");
            }
        return retorno;
    }

    public ArrayList<Musculo> retornaMusculos(Treino treino, int tipoMusculo){
        String[] columns = {
                MusculoContract.columns.ID,
                MusculoContract.columns.ORDEM,
                MusculoContract.columns.EXERCICIO,
                MusculoContract.columns.SERIES,
                MusculoContract.columns.TMP_EXEC_INI,
                MusculoContract.columns.TMP_EXEC_FIM,
                MusculoContract.columns.INTERVALOS,
                MusculoContract.columns.REPETICOES,
                MusculoContract.columns.CARGAS,
                MusculoContract.columns.TIPO_MUSCULO,
                MusculoContract.columns.ID_TREINO
        };

        ArrayList<Musculo> lstMusculos = new ArrayList<>();
        StringBuilder builder = new StringBuilder();
        builder.append(MusculoContract.columns.ID_TREINO + " = ? AND ");
        builder.append(MusculoContract.columns.TIPO_MUSCULO + " = ? ");

        String whereClause = builder.toString();

        String[] whereArgs = {String.valueOf(treino.getId()), String.valueOf(tipoMusculo)};

        try (Cursor c = db.query(MusculoContract.TABLE_NAME, columns, whereClause, whereArgs, null, null, MusculoContract.columns.ORDEM)) {
            if(c.moveToFirst()){
                do{
                    Musculo m = fromCursor(c);
                    lstMusculos.add(m);
                }while (c.moveToNext());
            }

            return lstMusculos;
        }
    }

    public ArrayList<Musculo> retornaMusculosTreinoBase(TreinoBase treinoBase, int tipoMusculo){
        String[] columns = {
                MusculoContract.columns.ID,
                MusculoContract.columns.ORDEM,
                MusculoContract.columns.EXERCICIO,
                MusculoContract.columns.SERIES,
                MusculoContract.columns.TMP_EXEC_INI,
                MusculoContract.columns.TMP_EXEC_FIM,
                MusculoContract.columns.INTERVALOS,
                MusculoContract.columns.REPETICOES,
                MusculoContract.columns.CARGAS,
                MusculoContract.columns.TIPO_MUSCULO,
                MusculoContract.columns.ID_TREINO,
                MusculoContract.columns.ID_TREINO_BASE,
        };

        ArrayList<Musculo> lstMusculos = new ArrayList<>();
        StringBuilder builder = new StringBuilder();
        builder.append(MusculoContract.columns.ID_TREINO_BASE + " = ? AND ");
        builder.append(MusculoContract.columns.TIPO_MUSCULO + " = ? ");

        String whereClause = builder.toString();

        String[] whereArgs = {String.valueOf(treinoBase.getId()), String.valueOf(tipoMusculo)};

        try (Cursor c = db.query(MusculoContract.TABLE_NAME, columns, whereClause, whereArgs, null, null, MusculoContract.columns.ORDEM)) {
            if(c.moveToFirst()){
                do{
                    Musculo m = fromCursor(c);
                    lstMusculos.add(m);
                }while (c.moveToNext());
            }

        }
        return lstMusculos;
    }

    public ArrayList<Integer> retornaTiposDeMusculosCadastradosPorAluno(Aluno aluno){
        ArrayList<Integer> arrTipoMusculo =  new ArrayList<>();
        String[] whereArgs = {aluno.getCPF()};

        try( Cursor c = db.rawQuery("SELECT distinct M.TipoMusculo FROM MUSCULO M " +
                        " INNER JOIN TREINO T ON M.IdTreino = T.Id " +
                        " WHERE T.CpfAluno = ? AND T.Atual = 1 " +
                        " ORDER BY M.TipoMusculo ",
                whereArgs)){
            if(c.moveToFirst()){
                do{
                    arrTipoMusculo.add(c.getInt(c.getColumnIndex("TipoMusculo")));
                }while(c.moveToNext());
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return arrTipoMusculo;
    }

    public ArrayList<Integer> retornaTiposDeMusculosCadastradosPorTreinoBase(TreinoBase treinoBase){
        ArrayList<Integer> arrTipoMusculo =  new ArrayList<>();
        String[] whereArgs = {String.valueOf(treinoBase.getId())};

        try( Cursor c = db.rawQuery("SELECT distinct M.TipoMusculo FROM " + MusculoContract.TABLE_NAME + " M " +
                        " INNER JOIN " + TreinoBaseContract.TABLE_NAME + " TB ON M." + MusculoContract.columns.ID_TREINO_BASE + " = TB." + TreinoBaseContract.columns.ID + " " +
                        " WHERE TB." + TreinoBaseContract.columns.ID + " = ? " +
                        " ORDER BY M.TipoMusculo ",
                whereArgs)){
            if(c.moveToFirst()){
                do{
                    arrTipoMusculo.add(c.getInt(c.getColumnIndex(MusculoContract.columns.TIPO_MUSCULO)));
                }while(c.moveToNext());
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return arrTipoMusculo;
    }

    public ArrayList<Integer> retornaIdsMusculosTreino(Treino t){
        ArrayList<Integer> arrIdsMusculos = new ArrayList<>();

        String[] columns = {
                MusculoContract.columns.ID,
                MusculoContract.columns.ID_TREINO
        };

        String whereClause = MusculoContract.columns.ID_TREINO + " = ?";
        String [] whereArgs = {String.valueOf(t.getId())};

        try (Cursor c = db.query(MusculoContract.TABLE_NAME, columns, whereClause, whereArgs, null, null, MusculoContract.columns.ID)) {
            if (c.moveToFirst()) {
                do{
                    arrIdsMusculos.add(c.getInt(c.getColumnIndex(MusculoContract.columns.ID)));
                }while(c.moveToNext());
            }
        }

        return arrIdsMusculos;
    }

    public ArrayList<Integer> retornaIdsMusculosTreinoBase(TreinoBase tb){
        ArrayList<Integer> arrIdsMusculos = new ArrayList<>();

        String[] columns = {
                MusculoContract.columns.ID,
                MusculoContract.columns.ID_TREINO_BASE
        };

        String whereClause = MusculoContract.columns.ID_TREINO_BASE + " = ?";
        String [] whereArgs = {String.valueOf(tb.getId())};

        try (Cursor c = db.query(MusculoContract.TABLE_NAME, columns, whereClause, whereArgs, null, null, MusculoContract.columns.ID)) {
            if (c.moveToFirst()) {
                arrIdsMusculos.add(c.getInt(c.getColumnIndex(MusculoContract.columns.ID)));
            }
        }

        return arrIdsMusculos;
    }

    public boolean excluir(Musculo m) throws Exception {
        boolean retorno = false;

        try {
            ContentValues values = new ContentValues();

            String whereClause = MusculoContract.columns.ID + " = ? ";
            String [] whereArgs = {String.valueOf(m.getId())};

            ProgramaMusculoDAO.getInstance(contextBase).excluirLigacaoMusculo(m);

            db.delete(MusculoContract.TABLE_NAME, whereClause, whereArgs);

            retorno = true;

        }catch (Exception e){
            e.printStackTrace();
            throw new Exception("Ocorreu um erro na exclusão");
        }

        return retorno;
    }

    public boolean excluirTodosDoTreino(Treino t) throws Exception {
        boolean retorno = false;

        try {
            String whereClause = MusculoContract.columns.ID_TREINO + " = ? ";
            String [] whereArgs = {String.valueOf(t.getId())};

            db.delete(MusculoContract.TABLE_NAME, whereClause, whereArgs);
            retorno = true;

        }catch (Exception e){
            e.printStackTrace();
            throw new Exception("Ocorreu um erro na exclusão");
        }

        return retorno;
    }

    public boolean excluirTodosDoTreinoBase(TreinoBase tb) throws Exception {
        boolean retorno = false;

        try {
            ContentValues values = new ContentValues();

            String whereClause = MusculoContract.columns.ID_TREINO_BASE + " = ? ";
            String [] whereArgs = {String.valueOf(tb.getId())};

            db.delete(MusculoContract.TABLE_NAME, whereClause, whereArgs);
            retorno = true;

        }catch (Exception e){
            e.printStackTrace();
            throw new Exception("Ocorreu um erro na exclusão");
        }

        return retorno;
    }



    public long alterarCargasMusculo(Musculo m) throws Exception{
        long retorno = 0;

        try {
            ContentValues values = new ContentValues();
            values.put(MusculoContract.columns.CARGAS, m.getCarga());

            String whereClause = MusculoContract.columns.ID + " = ?";
            String [] whereArgs = {String.valueOf(m.getId())};

            retorno = db.update(MusculoContract.TABLE_NAME, values, whereClause, whereArgs);

            registraAlteracoesMusculoTreino(Constantes.UPDATE, m);

        }catch (Exception e){
            e.printStackTrace();
            throw new Exception("ocorreu um erro");
        }
        return retorno;
    }

    public long alterarRepeticoesMusculo(Musculo m)throws Exception{
        long retorno = 0;

        try {
            ContentValues values = new ContentValues();
            values.put(MusculoContract.columns.REPETICOES, m.getRepeticoes());

            String whereClause = MusculoContract.columns.ID + " = ?";
            String [] whereArgs = {String.valueOf(m.getId())};

            retorno = db.update(MusculoContract.TABLE_NAME, values, whereClause, whereArgs);

            registraAlteracoesMusculoTreino(Constantes.UPDATE, m);

        }catch (Exception e){
            e.printStackTrace();
            throw new Exception("ocorreu um erro");
        }

        return retorno;
    }

    public long alterarRepeticoesECargasMusculo(Musculo m)throws Exception{
        long retorno = 0;

        try {
            ContentValues values = new ContentValues();
            values.put(MusculoContract.columns.REPETICOES, m.getRepeticoes());
            values.put(MusculoContract.columns.CARGAS, m.getCarga());

            String whereClause = MusculoContract.columns.ID + " = ?";
            String [] whereArgs = {String.valueOf(m.getId())};

            retorno = db.update(MusculoContract.TABLE_NAME, values, whereClause, whereArgs);

            registraAlteracoesMusculoTreino(Constantes.UPDATE, m);

        }catch (Exception e){
            e.printStackTrace();
            throw new Exception("ocorreu um erro");
        }

        return retorno;
    }

    private void registraAlteracoesMusculoTreino(String acao, Musculo m) throws Exception {
        //verifica se o treino que esse musculo pertence ja vai ser alterado no servidor
        //se o treino não estiver registrado para ser alterado (apagado e recriado)
        //ele entra no registro agora

        if(AlteracaoDAO.getInstance(contextBase).retornaAlteracaoPorChaveETabela(String.valueOf(m.getIdTreino()), TreinoContract.TABLE_NAME) == null){
            Alteracao a = new Alteracao();
            a.setAcao(acao);
            a.setAtivo(1);
            a.setChave(String.valueOf(m.getIdTreino()));
            AlteracaoDAO.getInstance(contextBase).salvar(a, TreinoContract.TABLE_NAME);
        }
    }


    public Musculo retornaMusculoPorId(Integer idMusculo) {
        String[] columns = {
                MusculoContract.columns.ID,
                MusculoContract.columns.ORDEM,
                MusculoContract.columns.EXERCICIO,
                MusculoContract.columns.SERIES,
                MusculoContract.columns.TMP_EXEC_INI,
                MusculoContract.columns.TMP_EXEC_FIM,
                MusculoContract.columns.INTERVALOS,
                MusculoContract.columns.REPETICOES,
                MusculoContract.columns.CARGAS,
                MusculoContract.columns.TIPO_MUSCULO,
                MusculoContract.columns.ID_TREINO,
                MusculoContract.columns.ID_TREINO_BASE,
        };

        String whereClause = MusculoContract.columns.ID + " = ?";
        String [] whereArgs = {String.valueOf(idMusculo)};

        Musculo m = null;

        try (Cursor c = db.query(MusculoContract.TABLE_NAME, columns, whereClause, whereArgs, null, null, MusculoContract.columns.ID)) {
            if (c.moveToFirst()) {
                m = fromCursor(c);
            }
        }

        return m;
    }

    public Musculo retornaMusculoDoProgramaTreino(int tipoMusculo, int ordemMusculo, String cpfAluno) {
        String[] whereArgs = {};
        String whereClause = "";

        whereClause = " WHERE a." + AlunoContract.columns.CPF + " = ? AND t.Atual = ? AND m.Ordem = ? AND m.TipoMusculo = ?";
        whereArgs = new String[]{cpfAluno, String.valueOf(Constantes.BANCO_TRUE), String.valueOf(ordemMusculo), String.valueOf(tipoMusculo)};

        // query manual
        String query = "SELECT m.Id," +
                        " m.Ordem," +
                        " m.Exercicio," +
                        " m.Series," +
                        " m.TmpExecIni," +
                        " m.TmpExecFim," +
                        " m.Intervalos," +
                        " m.Repeticoes," +
                        " m.Cargas," +
                        " m.TipoMusculo," +
                        " m.IdTreino," +
                        " m.IdTreinoBase " +
                        " FROM Musculo m " +
                        " LEFT JOIN Treino t ON m.IdTreino = t.Id " +
                        " LEFT JOIN Aluno a ON a.Cpf = t.CpfAluno " +
                        whereClause +
                        " ORDER BY m.Id";

        Musculo m = null;

        //fim query manual
        try (Cursor c = db.rawQuery(query, whereArgs)) {
            if (c.moveToFirst()) {
                m = fromCursor(c);
            }
        }

        return m;
    }

    private static Musculo fromCursor(Cursor c) {
        //OBS verificar defeito idTreinoBase estourando ao retornar valor nulo
        int id = c.getInt(c.getColumnIndex(MusculoContract.columns.ID));
        int ordem = c.getInt(c.getColumnIndex(MusculoContract.columns.ORDEM));
        String exercicio = c.getString(c.getColumnIndex(MusculoContract.columns.EXERCICIO));
        int series = c.getInt(c.getColumnIndex(MusculoContract.columns.SERIES));
        int tmpExecIni = c.getInt(c.getColumnIndex(MusculoContract.columns.TMP_EXEC_INI));
        int tmpExecFim = c.getInt(c.getColumnIndex(MusculoContract.columns.TMP_EXEC_FIM));
        int intervalos = c.getInt(c.getColumnIndex(MusculoContract.columns.INTERVALOS));
        String repeticoes = c.getString(c.getColumnIndex(MusculoContract.columns.REPETICOES));
        String carga = c.getString(c.getColumnIndex(MusculoContract.columns.CARGAS));
        int tipoMusculo = c.getInt(c.getColumnIndex(MusculoContract.columns.TIPO_MUSCULO));

        Integer idTreino = null;
        if(c.getColumnIndex(MusculoContract.columns.ID_TREINO) > 0) {
            idTreino = c.getInt(c.getColumnIndex(MusculoContract.columns.ID_TREINO));

            if (idTreino == 0) {
                idTreino = null;
            }
        }

        Integer idTreinoBase = null;
        if(c.getColumnIndex(MusculoContract.columns.ID_TREINO_BASE) > 0) {
            idTreinoBase = c.getInt(c.getColumnIndex(MusculoContract.columns.ID_TREINO_BASE));
            if (idTreinoBase == 0) {
                idTreinoBase = null;
            }
        }

        return new Musculo(id, ordem, exercicio, series, tmpExecIni, tmpExecFim, intervalos, repeticoes, carga, tipoMusculo, idTreino, idTreinoBase);
    }

}
