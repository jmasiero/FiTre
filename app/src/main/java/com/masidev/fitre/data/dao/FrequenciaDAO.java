package com.masidev.fitre.data.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.masidev.fitre.constante.Constantes;
import com.masidev.fitre.data.contract.FrequenciaContract;
import com.masidev.fitre.data.contract.MusculoTreinadoSemanaContract;
import com.masidev.fitre.data.db.DBHelper;
import com.masidev.fitre.data.entidade.Alteracao;
import com.masidev.fitre.data.entidade.Aluno;
import com.masidev.fitre.data.entidade.Frequencia;
import com.masidev.fitre.data.entidade.MusculoTreinadoSemana;

import java.util.ArrayList;

/**
 * Created by jmasiero on 15/02/16.
 */
public class FrequenciaDAO {
    private static FrequenciaDAO instance;
    private static Context contextBase;
    private SQLiteDatabase db;

    public static FrequenciaDAO getInstance(Context context){
        if(instance == null){
            instance = new FrequenciaDAO(context.getApplicationContext());
        }

        contextBase = context;

        return instance;
    }

    private FrequenciaDAO(Context context){
        DBHelper dbHelper = DBHelper.getInstance(context);
        db = dbHelper.getWritableDatabase();
    }

    //Crud
    public long salvar(Frequencia f, Boolean registrar) throws Exception{
        long retorno = 0;

        if(f.getAluno() != null
                && !f.getData().equals("")
        )
        {
            try {
                ContentValues values = new ContentValues();
                values.put(FrequenciaContract.columns.DATA, f.getData());
                values.put(FrequenciaContract.columns.CPF_ALUNO, f.getAluno().getCPF());
                values.put(FrequenciaContract.columns.MUSCULOS_TREINADOS, f.retornaListaEmStringMusculosTreinados());
                values.put(FrequenciaContract.columns.ORDEM_PROGRAMA_TREINO, f.retornaListaEmStringOrdemProgramaTreino());

                retorno = db.insert(FrequenciaContract.TABLE_NAME, null, values);

                if(registrar) {
                    registraAlteracoes(Constantes.INSERT, String.valueOf(retorno));
                }

            }catch (Exception e){
                e.printStackTrace();
                throw new Exception("ocorreu um erro ao salvar");
            }
        }
        return retorno;
    }

    public ArrayList<Frequencia> listar(Aluno a) {

        String[] columns = {
                FrequenciaContract.columns.ID,
                FrequenciaContract.columns.CPF_ALUNO,
                FrequenciaContract.columns.DATA,
                FrequenciaContract.columns.MUSCULOS_TREINADOS,
                FrequenciaContract.columns.ORDEM_PROGRAMA_TREINO
        };

        ArrayList<Frequencia> arrFrequencia = new ArrayList<>();

        String whereClause = FrequenciaContract.columns.CPF_ALUNO+" = ?";
        String [] whereArgs = {a.getCPF()};

        try (Cursor c = db.query(FrequenciaContract.TABLE_NAME, columns, whereClause, whereArgs, null, null, FrequenciaContract.columns.ID + " DESC")) {
            if (c.moveToFirst()) {
                do {
                    Frequencia f = fromCursor(c);
                    arrFrequencia.add(f);
                } while (c.moveToNext());
            }

            return arrFrequencia;
        }

    }

    public ArrayList<Frequencia> listarUltimasDez(Aluno a) {

        String[] columns = {
                FrequenciaContract.columns.ID,
                FrequenciaContract.columns.CPF_ALUNO,
                FrequenciaContract.columns.DATA,
                FrequenciaContract.columns.MUSCULOS_TREINADOS,
                FrequenciaContract.columns.ORDEM_PROGRAMA_TREINO
        };

        ArrayList<Frequencia> arrFrequencia = new ArrayList<>();

        String whereClause = FrequenciaContract.columns.CPF_ALUNO+" = ?";
        String [] whereArgs = {a.getCPF()};

        try (Cursor c = db.query(FrequenciaContract.TABLE_NAME, columns, whereClause, whereArgs, null, null, FrequenciaContract.columns.ID+" DESC")) {
            if (c.moveToFirst()) {
                int curSize=c.getCount();  // return no of rows
                if(curSize>10) {
                    for(int i=0;i<10;i++){
                        Frequencia f = fromCursor(c);
                        arrFrequencia.add(f);
                        c.moveToNext();
                    }
                } else {
                    do {
                        Frequencia f = fromCursor(c);
                        arrFrequencia.add(f);
                    } while (c.moveToNext());
                }
            }

            return arrFrequencia;
        }

    }

    public Frequencia buscarFrequencia(Integer id) {

        String[] columns = {
                FrequenciaContract.columns.ID,
                FrequenciaContract.columns.CPF_ALUNO,
                FrequenciaContract.columns.DATA,
                FrequenciaContract.columns.MUSCULOS_TREINADOS,
                FrequenciaContract.columns.ORDEM_PROGRAMA_TREINO
        };

        Frequencia f = null;

        String whereClause = FrequenciaContract.columns.ID + " = ?";
        String [] whereArgs = {id.toString()};

        try (Cursor c = db.query(FrequenciaContract.TABLE_NAME, columns, whereClause, whereArgs, null, null, FrequenciaContract.columns.DATA)) {
            if (c.moveToFirst()) {
                f = fromCursor(c);
            }

        }

        return f;
    }

    public boolean excluirHistoricoDeFrequencia(Aluno a) throws Exception{
        boolean retorno = false;

        if(a.getCPF() != null)
        {
            String whereClause = FrequenciaContract.columns.CPF_ALUNO + " = ?";
            String [] whereArgs = {a.getCPF().toString()};

            try {
                excluirAlteracoesHistoricoFrequencia(a);
                db.delete(FrequenciaContract.TABLE_NAME, whereClause, whereArgs);

                retorno = true;
            }catch (Exception e){
                e.printStackTrace();
                throw new Exception("Ocorreu um erro na exclusão");
            }
        }
        return retorno;
    }

    private void excluirAlteracoesHistoricoFrequencia(Aluno a) {
        ArrayList<Frequencia> arrFrequencia = listar(a);
        try {
            for(Frequencia f : arrFrequencia){
                Alteracao alt = AlteracaoDAO.getInstance(contextBase).retornaAlteracaoPorChaveETabela(f.getId().toString(), FrequenciaContract.TABLE_NAME);

                AlteracaoDAO.getInstance(contextBase).excluirPorId(alt);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private static Frequencia fromCursor(Cursor c) {
        ArrayList<String> arrOrdemProgramaSelecionado = new ArrayList<>();
        ArrayList<String> arrMusculosTreinados = new ArrayList<>();

        Integer id = c.getInt(c.getColumnIndex(FrequenciaContract.columns.ID));
        String cpfAluno = c.getString(c.getColumnIndex(FrequenciaContract.columns.CPF_ALUNO));
        String data = c.getString(c.getColumnIndex(FrequenciaContract.columns.DATA));
        String musculosTreinados = c.getString(c.getColumnIndex(FrequenciaContract.columns.MUSCULOS_TREINADOS));
        String ordemProgramasTreinados = c.getString(c.getColumnIndex(FrequenciaContract.columns.ORDEM_PROGRAMA_TREINO));

        if(!musculosTreinados.equals("")){
            arrMusculosTreinados = retornaArrayList(musculosTreinados.split("-"));
        }

        if(!ordemProgramasTreinados.equals("")){
            arrOrdemProgramaSelecionado = retornaArrayList(ordemProgramasTreinados.split("-"));
        }

        Aluno a = new Aluno(cpfAluno);
        return new Frequencia(id, data, arrMusculosTreinados, arrOrdemProgramaSelecionado, a);
    }

    private static ArrayList<String> retornaArrayList(String[] arr){
        ArrayList<String> arrLst = new ArrayList<>();
        for(int i = 0; i < arr.length; i++){
            arrLst.add(arr[i]);
        }

        return arrLst;
    }

    private void registraAlteracoes(String acao, String chave) throws Exception {

        Alteracao a = new Alteracao();
        a.setAcao(acao);
        a.setAtivo(1);
        a.setChave(chave);

        AlteracaoDAO.getInstance(contextBase).salvar(a, FrequenciaContract.TABLE_NAME);
    }
}
