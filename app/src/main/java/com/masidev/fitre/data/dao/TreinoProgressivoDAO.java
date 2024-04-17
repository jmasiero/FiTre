package com.masidev.fitre.data.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.masidev.fitre.constante.Constantes;
import com.masidev.fitre.data.contract.TreinoProgressivoContract;
import com.masidev.fitre.data.db.DBHelper;
import com.masidev.fitre.data.entidade.Alteracao;
import com.masidev.fitre.data.entidade.Aluno;
import com.masidev.fitre.data.entidade.Personal;
import com.masidev.fitre.data.entidade.SemanaTreinoProgressivo;
import com.masidev.fitre.data.entidade.Treino;
import com.masidev.fitre.data.entidade.TreinoProgressivo;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by jmasiero on 15/04/16.
 */
public class TreinoProgressivoDAO {
    private static TreinoProgressivoDAO instance;
    private SQLiteDatabase db;
    private Context contextBase;

    public static TreinoProgressivoDAO getInstance(Context context){

        if (instance == null) {
            instance = new TreinoProgressivoDAO(context);
        }

        return instance;
    }

    private TreinoProgressivoDAO(Context context){

        DBHelper dbHelper = DBHelper.getInstance(context);
        db = dbHelper.getWritableDatabase();
        this.contextBase = context;
    }

    public long salvar(TreinoProgressivo tp, Boolean registra) throws Exception{
        long retorno = 0;

        try {
            ContentValues values = new ContentValues();
            values.put(TreinoProgressivoContract.columns.ID_TREINO, tp.getIdTreino());
            values.put(TreinoProgressivoContract.columns.MES, tp.getMes());

            retorno = db.insert(TreinoProgressivoContract.TABLE_NAME, null, values);

            if(registra) {
                registraAlteracoes(Constantes.INSERT, String.valueOf(retorno));
            }

            tp.setId((int) retorno);

            for (SemanaTreinoProgressivo smtp : tp.getLstSemanas()) {
                if(smtp != null) {
                    smtp.setIdTreinoProgressivo(tp.getId());
                    smtp.setPorcentagemCargas(smtp.getPorcentagemCarga1()+smtp.getPorcentagemCarga2()+smtp.getPorcentagemCarga3());
                    SemanaTreinoProgressivoDAO.getInstance(contextBase).salvar(smtp);
                }
            }

        }catch (Exception e){
            e.printStackTrace();
            throw new Exception("id repetido");
        }

        return retorno;
    }
        //finalizar
    public void alterar(TreinoProgressivo tp, boolean registra) throws Exception{
        try {
            ContentValues values = new ContentValues();
            values.put(TreinoProgressivoContract.columns.ID_TREINO, tp.getIdTreino());
            values.put(TreinoProgressivoContract.columns.MES, tp.getMes());

            String whereClause = TreinoProgressivoContract.columns.ID + " = ? ";
            String [] whereArgs = {String.valueOf(tp.getId())};

            db.update(TreinoProgressivoContract.TABLE_NAME, values, whereClause, whereArgs);

            if(registra) {
                registraAlteracoes(Constantes.UPDATE, String.valueOf(tp.getId()));
            }

            for (SemanaTreinoProgressivo smtp : tp.getLstSemanas()) {
                if(smtp != null) {
                    smtp.setPorcentagemCargas(smtp.getPorcentagemCarga1()+smtp.getPorcentagemCarga2()+smtp.getPorcentagemCarga3());
                    SemanaTreinoProgressivoDAO.getInstance(contextBase).alterar(smtp);
                }
            }

        }catch (Exception e){
            e.printStackTrace();
            throw new Exception("Problema na alteração!");
        }
    }

    private void registraAlteracoes(String acao, String chave) throws Exception {

        Alteracao a = new Alteracao();
        a.setAcao(acao);
        a.setAtivo(1);
        a.setChave(chave);

        AlteracaoDAO.getInstance(contextBase).salvar(a, TreinoProgressivoContract.TABLE_NAME);
    }

    public boolean excluirTodosDoTreino(Treino t, Boolean registrar) throws Exception {
        boolean retorno = false;
        try {
            String whereClause = TreinoProgressivoContract.columns.ID_TREINO + " = ? ";
            String [] whereArgs = {String.valueOf(t.getId())};

            if(registrar) {
                String chave = "";
                ArrayList<TreinoProgressivo> lstTp = retornarTreinoProgressivo(t);
                for (TreinoProgressivo tp : lstTp) {
                    //0 - mes , 1 - cpfAluno, 2 - cpfPersonal, 3 - tipoTreino, 4 - dataInicio
                    chave = tp.getMes() + "/" + t.getAluno().getCPF() + "/" + t.getPersonal().getCPF() + "/" + t.getTipoTreino() + "/" + t.getDataInicio();
                    registraAlteracoes(Constantes.DELETE, String.valueOf(chave));
                }
            }

            db.delete(TreinoProgressivoContract.TABLE_NAME, whereClause, whereArgs);
            retorno = true;
        }catch (Exception e){
            e.printStackTrace();
            throw new Exception("Ocorreu um erro na exclusão");
        }

        return retorno;
    }

    public ArrayList<TreinoProgressivo> retornarTreinoProgressivo(Treino treino) {
        String[] columns = {
                TreinoProgressivoContract.columns.ID,
                TreinoProgressivoContract.columns.ID_TREINO,
                TreinoProgressivoContract.columns.MES
        };

        String whereClause = TreinoProgressivoContract.columns.ID_TREINO + " = ? ";
        String [] whereArgs = {String.valueOf(treino.getId())};

        ArrayList<TreinoProgressivo> lstTreinoProgressivo = new ArrayList<>();

        try (Cursor c = db.query(TreinoProgressivoContract.TABLE_NAME, columns, whereClause, whereArgs, null, null, TreinoProgressivoContract.columns.MES)) {
            if (c.moveToFirst()) {
                do {
                    lstTreinoProgressivo.add(fromCursor(c));
                }while (c.moveToNext());
            }
        }

        //preenche os meses com suas respectivas semanas
        for (TreinoProgressivo tp : lstTreinoProgressivo) {
            tp.setLstSemanas(SemanaTreinoProgressivoDAO.getInstance(contextBase).retornarSemanas(tp));
        }

        return lstTreinoProgressivo;
    }

    public TreinoProgressivo retornarTreinoProgressivoPorId(Integer id) {
        String[] columns = {
                TreinoProgressivoContract.columns.ID,
                TreinoProgressivoContract.columns.ID_TREINO,
                TreinoProgressivoContract.columns.MES
        };

        String whereClause = TreinoProgressivoContract.columns.ID + " = ? ";
        String [] whereArgs = {String.valueOf(id)};

        TreinoProgressivo treinoProgressivo = null;

        try (Cursor c = db.query(TreinoProgressivoContract.TABLE_NAME, columns, whereClause, whereArgs, null, null, TreinoProgressivoContract.columns.MES)) {
            if (c.moveToFirst()) {
                treinoProgressivo = fromCursor(c);
            }
        }
        if(treinoProgressivo != null) {
            //preenche o mes com suas respectivas semanas
            treinoProgressivo.setLstSemanas(SemanaTreinoProgressivoDAO.getInstance(contextBase).retornarSemanas(treinoProgressivo));
        }

        return treinoProgressivo;
    }

    public TreinoProgressivo retornaTreinoProgressivoDaSemana(Treino treino, String data) {
        String[] columns = {
                TreinoProgressivoContract.columns.ID,
                TreinoProgressivoContract.columns.ID_TREINO,
                TreinoProgressivoContract.columns.MES
        };
        //data[0] - semana data[1] - mes
        String[] arrData = data.split("-");

        StringBuilder sb = new StringBuilder();
        sb.append(TreinoProgressivoContract.columns.ID_TREINO + " = ? AND ");
        sb.append(TreinoProgressivoContract.columns.MES + " = ? ");
        String whereClause = sb.toString();

        String [] whereArgs = {String.valueOf(treino.getId()), arrData[1]};

        TreinoProgressivo treinoProgressivo = null;

        try (Cursor c = db.query(TreinoProgressivoContract.TABLE_NAME, columns, whereClause, whereArgs, null, null, TreinoProgressivoContract.columns.MES)) {
            if (c.moveToFirst()) {
                treinoProgressivo = fromCursor(c);
            }
        }

        //preenche o com a semana
        if (treinoProgressivo != null) {
            treinoProgressivo.setLstSemanas(SemanaTreinoProgressivoDAO.getInstance(contextBase).retornarSemanaAtual(treinoProgressivo, data));
        }

        return treinoProgressivo;
    }

    public void salvarJson(JSONObject obj, Treino treino) throws Exception{
        TreinoProgressivo tp = new TreinoProgressivo();
        tp.setMes(Integer.parseInt(obj.getString("mes")));

        Treino treinoInterno = null;

        if(treino == null || treino.getId() == null || treino.getId() <= 0){
            JSONObject objTreino = obj.getJSONObject("dadosTreino");
            Treino treinoServidor = new Treino();
            treinoServidor.setAluno(new Aluno(objTreino.getString("cpfAluno")));
            treinoServidor.setPersonalCriador(new Personal(objTreino.getString("cpfPersonal")));
            treinoServidor.setTipoTreino(objTreino.getString("tipoTreino"));
            treinoServidor.setDataInicio(objTreino.getString("dataInicio"));
            treinoInterno = TreinoDAO.getInstance(contextBase).retornaTreinoComDadosServidor(treinoServidor);
        }else{
            treinoInterno = treino;
        }

        if(treinoInterno != null) {

            tp.setIdTreino(treinoInterno.getId());

            JSONArray arrSemanas = obj.getJSONArray("lstSemanas");
            ArrayList<SemanaTreinoProgressivo> lstSemanas = new ArrayList<>();
            for (int j = 0; j < arrSemanas.length(); j++) {
                JSONObject objSemana = arrSemanas.getJSONObject(j);
                SemanaTreinoProgressivo semana = new SemanaTreinoProgressivo();
                String[] arrCargas = objSemana.getString("porcentagemCargas").split("%");
                if (arrCargas.length > 1) {
                    semana.setPorcentagemCarga1(arrCargas[0] + "%");
                    semana.setPorcentagemCarga2(arrCargas[1] + "%");
                    semana.setPorcentagemCarga3(arrCargas[2] + "%");
                } else {
                    semana.setPorcentagemCarga1(arrCargas[0] + "%");
                }
                semana.setRepeticoes(objSemana.getString("repeticoes"));
                semana.setSemana(objSemana.getInt("semana"));

                lstSemanas.add(semana);
            }

            tp.setLstSemanas(lstSemanas);

            TreinoProgressivoDAO.getInstance(contextBase).salvar(tp, false);
        }
    }

    private static TreinoProgressivo fromCursor(Cursor c){
        Integer id = c.getInt(c.getColumnIndex(TreinoProgressivoContract.columns.ID));
        Integer idTreino = c.getInt(c.getColumnIndex(TreinoProgressivoContract.columns.ID_TREINO));
        Integer mes = c.getInt(c.getColumnIndex(TreinoProgressivoContract.columns.MES));

        return new TreinoProgressivo(id, idTreino, mes);
    }


}
