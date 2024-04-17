package com.masidev.fitre.data.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.masidev.fitre.R;
import com.masidev.fitre.constante.Constantes;
import com.masidev.fitre.data.contract.TreinoBaseContract;
import com.masidev.fitre.data.contract.TreinoContract;
import com.masidev.fitre.data.db.DBHelper;
import com.masidev.fitre.data.entidade.Alteracao;
import com.masidev.fitre.data.entidade.Musculo;
import com.masidev.fitre.data.entidade.Personal;
import com.masidev.fitre.data.entidade.TreinoBase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jmasiero on 07/03/16.
 */
public class TreinoBaseDAO {

    private static TreinoBaseDAO instance;
    private SQLiteDatabase db;
    private static Context contextBase;

    public static TreinoBaseDAO getInstance(Context context){
        if(instance == null){
            instance = new TreinoBaseDAO(context.getApplicationContext());
        }

        contextBase = context;

        return instance;
    }

    private TreinoBaseDAO(Context context){
        DBHelper dbHelper = DBHelper.getInstance(context);
        db = dbHelper.getWritableDatabase();
    }

    public TreinoBase buscarTreinoBaseComMusculosFiltrados(TreinoBase treinoBase, ArrayList<Integer> arrTipoMusculos) {
        //tipo sequencia 0 - simples / 1 - misto

        String[] columns = {
                TreinoBaseContract.columns.ID,
                TreinoBaseContract.columns.TIPO_TREINO,
                TreinoBaseContract.columns.CPF_PERSONAL
        };

        TreinoBase treinoB = null;

        StringBuilder builder = new StringBuilder();
        builder.append(TreinoBaseContract.columns.ID + " = ? ");

        String whereClause = builder.toString();
        String [] whereArgs = {String.valueOf(treinoBase.getId())};

        try (Cursor c = db.query(TreinoBaseContract.TABLE_NAME, columns, whereClause, whereArgs, null, null, TreinoBaseContract.columns.TIPO_TREINO)) {
            if (c.moveToFirst()) {
                treinoB = fromCursor(c);
            }

            if(treinoB != null){
                for (int i = 0; i < arrTipoMusculos.size(); i++){
                    treinoB.addLstMusculos(MusculoDAO.getInstance(contextBase).retornaMusculosTreinoBase(treinoB, arrTipoMusculos.get(i)), arrTipoMusculos.get(i));
                }
            }
            return treinoB;
        }

    }

    public TreinoBase buscarTreinoBaseComMusculosFiltradosParaAlterar(TreinoBase treinoBase, ArrayList<Integer> arrTipoMusculos) {

        if(treinoBase != null){
            for (int i = 0; i < arrTipoMusculos.size(); i++){
                treinoBase.addLstMusculos(MusculoDAO.getInstance(contextBase).retornaMusculosTreinoBase(treinoBase, arrTipoMusculos.get(i)), arrTipoMusculos.get(i));
            }
        }

        return treinoBase;
    }

    public long salvar(TreinoBase tb, Boolean registra) throws Exception{
        long retorno = 0;

        try {
            ContentValues values = new ContentValues();
            values.put(TreinoBaseContract.columns.CPF_PERSONAL, tb.getPersonal().getCPF());
            values.put(TreinoBaseContract.columns.TIPO_TREINO, tb.getTipoTreino());

            retorno = db.insert(TreinoBaseContract.TABLE_NAME, null, values);

            if(registra) {
                registraAlteracoes(Constantes.INSERT, String.valueOf(retorno));
            }

            tb.setId((int)retorno);

            for (ArrayList<Musculo> lstMustulo : tb.getLstTodosMusculos()) {
                if(lstMustulo != null) {
                    for (Musculo m : lstMustulo) {
                        m.setIdTreino(null);
                        m.setIdTreinoBase(tb.getId());
                        MusculoDAO.getInstance(contextBase).salvar(m);
                    }
                }
            }

        }catch (Exception e){
            e.printStackTrace();
            throw new Exception("id repetido");
        }

        return retorno;
    }

    public long alterar(TreinoBase tb) throws Exception{
        long retorno = 0;

        try {
            ContentValues values = new ContentValues();
            values.put(TreinoBaseContract.columns.TIPO_TREINO, tb.getTipoTreino());

            String whereClause = TreinoBaseContract.columns.ID + " = ? ";
            String [] whereArgs = {String.valueOf(tb.getId())};

            retorno = db.update(TreinoBaseContract.TABLE_NAME, values, whereClause, whereArgs);

            registraAlteracoes(Constantes.UPDATE, String.valueOf(tb.getId()));

            for (Musculo m : tb.getMusculosRemovidos()){
                if(m.getId() != null) {
                    MusculoDAO.getInstance(contextBase).excluir(m);
                }
            }

            for (ArrayList<Musculo> lstMustulo : tb.getLstTodosMusculos()) {
                if(lstMustulo != null) {
                    for (Musculo m : lstMustulo) {
                        if(m.getId() == null) {
                            m.setIdTreinoBase(tb.getId());
                            m.setIdTreino(null);
                            MusculoDAO.getInstance(contextBase).salvar(m);
                        }else{
                            MusculoDAO.getInstance(contextBase).alterar(m);
                        }
                    }
                }
            }

        }catch (Exception e){
            e.printStackTrace();
            throw new Exception("id repetido");
        }

        return retorno;
    }

    public List<TreinoBase> listarSemMusculos(Boolean spinner, String textoSpn) {

        String[] columns = {
                TreinoBaseContract.columns.ID,
                TreinoBaseContract.columns.TIPO_TREINO,
                TreinoBaseContract.columns.CPF_PERSONAL
        };

        List<TreinoBase> lstTreinoBase = new ArrayList<>();

        if(spinner){
            if(textoSpn.equals("")){
                lstTreinoBase.add(new TreinoBase(null, contextBase.getString(R.string.Nenhum), null));
            }else{
                lstTreinoBase.add(new TreinoBase(null, textoSpn, null));
            }
        }

        try (Cursor c = db.query(TreinoBaseContract.TABLE_NAME, columns, null, null, null, null, TreinoBaseContract.columns.TIPO_TREINO)) {
            if (c.moveToFirst()) {
                do {
                    TreinoBase tb = fromCursor(c);
                    lstTreinoBase.add(tb);
                } while (c.moveToNext());
            }

            return lstTreinoBase;
        }

    }

    public List<TreinoBase> listarSemMusculosFiltrada(String filtro) {

        String[] columns = {
                TreinoBaseContract.columns.ID,
                TreinoBaseContract.columns.TIPO_TREINO,
                TreinoBaseContract.columns.CPF_PERSONAL
        };

        String whereClause = TreinoBaseContract.columns.TIPO_TREINO + " like ? ";
        String[] whereArgs = {"%" + filtro + "%"};

        List<TreinoBase> lstTreinoBase = new ArrayList<>();

        try (Cursor c = db.query(TreinoBaseContract.TABLE_NAME, columns, whereClause, whereArgs, null, null, TreinoBaseContract.columns.TIPO_TREINO)) {
            if (c.moveToFirst()) {
                do {
                    TreinoBase tb = fromCursor(c);
                    lstTreinoBase.add(tb);
                } while (c.moveToNext());
            }

            return lstTreinoBase;
        }

    }

    public TreinoBase retornaTreinoBasePorNome(String tipoTreino) {
        String[] columns = {
                TreinoBaseContract.columns.ID,
                TreinoBaseContract.columns.TIPO_TREINO,
                TreinoContract.columns.CPF_PERSONAL
        };

        TreinoBase treinoBase = null;

        String whereClause = " UPPER("+TreinoBaseContract.columns.TIPO_TREINO + ") = UPPER(?) ";
        String [] whereArgs = {tipoTreino};

        try (Cursor c = db.query(TreinoBaseContract.TABLE_NAME, columns, whereClause, whereArgs, null, null, TreinoBaseContract.columns.TIPO_TREINO)) {
            if (c.moveToFirst()) {
                treinoBase = fromCursor(c);
            }
        }

        return treinoBase;
    }

    public TreinoBase retornaTreinoBasePorNomeEPersonal(TreinoBase tb) {
        String[] columns = {
                TreinoBaseContract.columns.ID,
                TreinoBaseContract.columns.TIPO_TREINO,
                TreinoContract.columns.CPF_PERSONAL
        };

        TreinoBase treinoBase = null;

        StringBuilder sb = new StringBuilder();
        sb.append(TreinoBaseContract.columns.TIPO_TREINO + " = ? AND ");
        sb.append(TreinoBaseContract.columns.CPF_PERSONAL + " = ? ");

        String whereClause = sb.toString();
        String [] whereArgs = {tb.getTipoTreino(), tb.getPersonal().getCPF()};

        try (Cursor c = db.query(TreinoBaseContract.TABLE_NAME, columns, whereClause, whereArgs, null, null, TreinoBaseContract.columns.TIPO_TREINO)) {
            if (c.moveToFirst()) {
                treinoBase = fromCursor(c);
            }
        }

        return treinoBase;
    }

    public TreinoBase retornaTreinoBaseComMusculos(Integer idTreinoBase) {
        //tipo sequencia 0 - simples / 1 - misto

        String[] columns = {
                TreinoBaseContract.columns.ID,
                TreinoBaseContract.columns.TIPO_TREINO,
                TreinoContract.columns.CPF_PERSONAL
        };

        ArrayList<Integer> arrMusculos = new ArrayList<>();
        arrMusculos.add(0);
        arrMusculos.add(1);
        arrMusculos.add(2);
        arrMusculos.add(3);
        arrMusculos.add(4);
        arrMusculos.add(5);
        arrMusculos.add(6);
        arrMusculos.add(7);
        arrMusculos.add(8);

        TreinoBase treinoBase = null;

        StringBuilder builder = new StringBuilder();
        builder.append(TreinoBaseContract.columns.ID + " = ? ");

        String whereClause = builder.toString();
        String [] whereArgs = {String.valueOf(idTreinoBase)};

        try (Cursor c = db.query(TreinoBaseContract.TABLE_NAME, columns, whereClause, whereArgs, null, null, TreinoBaseContract.columns.TIPO_TREINO)) {
            if (c.moveToFirst()) {
                treinoBase = fromCursor(c);
            }

            if(treinoBase != null){
                for (int i = 0; i < arrMusculos.size(); i++){
                    treinoBase.addLstMusculos(MusculoDAO.getInstance(contextBase).retornaMusculosTreinoBase(treinoBase, arrMusculos.get(i)), arrMusculos.get(i));
                }
            }
        }

        return treinoBase;
    }

    public TreinoBase retornaTreinoBase(Integer idTreinoBase) {
        //tipo sequencia 0 - simples / 1 - misto

        String[] columns = {
                TreinoBaseContract.columns.ID,
                TreinoBaseContract.columns.TIPO_TREINO,
                TreinoContract.columns.CPF_PERSONAL
        };

        TreinoBase treinoBase = null;

        StringBuilder builder = new StringBuilder();
        builder.append(TreinoBaseContract.columns.ID + " = ? ");

        String whereClause = builder.toString();
        String [] whereArgs = {String.valueOf(idTreinoBase)};

        try (Cursor c = db.query(TreinoBaseContract.TABLE_NAME, columns, whereClause, whereArgs, null, null, TreinoBaseContract.columns.TIPO_TREINO)) {
            if (c.moveToFirst()) {
                treinoBase = fromCursor(c);
            }
        }

        return treinoBase;
    }

    public boolean excluir(TreinoBase tb, Boolean registra) throws Exception {
        boolean retorno = false;

        ArrayList<Integer> arrIdsMusculos = MusculoDAO.getInstance(contextBase).retornaIdsMusculosTreinoBase(tb);

        if(arrIdsMusculos.size() > 0){
            MusculoDAO.getInstance(contextBase).excluirTodosDoTreinoBase(tb);
        }

        //Implementar treino da semana

        try {
            ContentValues values = new ContentValues();

            String whereClause = TreinoBaseContract.columns.ID + " = ? ";
            String [] whereArgs = {String.valueOf(tb.getId())};

            db.delete(TreinoBaseContract.TABLE_NAME, whereClause, whereArgs);

            if (registra) {
                registraAlteracoes(Constantes.DELETE, tb.getId() + "/" +tb.getTipoTreino() + "/" + tb.getPersonal().getCPF());
            }

            retorno = true;

        }catch (Exception e){
            e.printStackTrace();
            throw new Exception("Ocorreu um erro na exclusão");
        }

        return retorno;
    }

    private void registraAlteracoes(String acao, String chave) throws Exception {

        Alteracao a = new Alteracao();
        a.setAcao(acao);
        a.setAtivo(1);
        a.setChave(chave);

        AlteracaoDAO.getInstance(contextBase).salvar(a, TreinoBaseContract.TABLE_NAME);
    }

//    public ArrayList<Integer> retornaTiposDeMusculosCadastradosPorTreinoBase(TreinoBase tb){
//        ArrayList<Integer> arrTipoMusculo =  new ArrayList<>();
//        //new String[] {String.valueOf(tb.getId())}
//        try( Cursor c = db.rawQuery("SELECT distinct M.TipoMusculo FROM "+ TreinoBaseContract.TABLE_NAME +" TB " +
//                        " INNER JOIN "+ MusculoContract.TABLE_NAME +" M ON M."+MusculoContract.columns.ID_TREINO_BASE+" = TB.Id " +
//                        " WHERE TB.Id = 6 " +
//                        " ORDER BY M.TipoMusculo ",
//                null)){
//            if(c.moveToFirst()){
//                do{
//                    arrTipoMusculo.add(c.getInt(c.getColumnIndex("TipoMusculo")));
//                }while(c.moveToNext());
//            }
//        }
//
//        return arrTipoMusculo;
//    }

    private static TreinoBase fromCursor(Cursor c) {
        Integer id = c.getInt(c.getColumnIndex(TreinoBaseContract.columns.ID));
        String tipoTreino = c.getString(c.getColumnIndex(TreinoBaseContract.columns.TIPO_TREINO));
        String cpfPersonal = c.getString(c.getColumnIndex(TreinoBaseContract.columns.CPF_PERSONAL));

        return new TreinoBase(id,tipoTreino, new Personal(cpfPersonal));
    }
}
