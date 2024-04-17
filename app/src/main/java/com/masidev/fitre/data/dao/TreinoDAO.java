package com.masidev.fitre.data.dao;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.masidev.fitre.constante.Constantes;
import com.masidev.fitre.data.contract.AlunoContract;
import com.masidev.fitre.data.contract.TreinoContract;
import com.masidev.fitre.data.contract.TreinoProgressivoContract;
import com.masidev.fitre.data.db.DBHelper;
import com.masidev.fitre.data.entidade.Alteracao;
import com.masidev.fitre.data.entidade.Aluno;
import com.masidev.fitre.data.entidade.Musculo;
import com.masidev.fitre.data.entidade.Personal;
import com.masidev.fitre.data.entidade.ProgramaTreino;
import com.masidev.fitre.data.entidade.Treino;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jmasiero on 23/10/15.
 */
public class TreinoDAO {
    private static TreinoDAO instance;
    private SQLiteDatabase db;
    private static Context contextBase;

    public static TreinoDAO getInstance(Context context){
        if(instance == null){
            instance = new TreinoDAO(context.getApplicationContext());
        }

        contextBase = context;

        return instance;
    }

    private TreinoDAO(Context context){
        DBHelper dbHelper = DBHelper.getInstance(context);
        db = dbHelper.getWritableDatabase();
    }

    public long salvar(Treino t, Boolean registra, Boolean doServidor) throws Exception{
        long retorno = 0;
        int progressivo = Constantes.BANCO_FALSE;

        try {

            ProgramaTreinoDAO.getInstance(contextBase).excluirTodosDoAluno(t.getAluno(), false);

            ContentValues values = new ContentValues();
            values.put(TreinoContract.columns.CPF_ALUNO, t.getAluno().getCPF());
            values.put(TreinoContract.columns.CPF_PERSONAL, t.getPersonal().getCPF());

            if(doServidor){
                values.put(TreinoContract.columns.CPF_PERSONAL_CRIADOR, t.getPersonalCriador().getCPF());
            }else{
                values.put(TreinoContract.columns.CPF_PERSONAL_CRIADOR, t.getPersonal().getCPF());
            }

            values.put(TreinoContract.columns.DATA_FIM, t.getDataFim());

            values.put(TreinoContract.columns.DATA_INICIO, t.getDataInicio());

            values.put(TreinoContract.columns.OBJETIVO, t.getObjetivo());
            values.put(TreinoContract.columns.ANOTACOES, t.getAnotacoes());
            values.put(TreinoContract.columns.PESO_IDEAL, t.getPesoIdeal());
            values.put(TreinoContract.columns.PESO_INICIO, t.getPesoInicio());
            values.put(TreinoContract.columns.TIPO_TREINO, t.getTipoTreino());

            if(t.getProgressivo()){
                progressivo = Constantes.BANCO_TRUE;
            }

            values.put(TreinoContract.columns.PROGRESSIVO, progressivo );

            values.put(TreinoContract.columns.ATUAL, Constantes.BANCO_TRUE);

            inativarTreinoAntigo(t);

            retorno = db.insert(TreinoContract.TABLE_NAME, null, values);

            if(registra) {
                registraAlteracoes(Constantes.INSERT, String.valueOf(retorno));
            }

            t.setId((int) retorno);
            if(retorno > 0) {

                for (ArrayList<Musculo> lstMustulo : t.getLstTodosMusculos()) {
                    if (lstMustulo != null) {
                        for (Musculo m : lstMustulo) {
                            m.setIdTreino(t.getId());
                            m.setIdTreinoBase(null);
                            MusculoDAO.getInstance(contextBase).salvar(m);
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

    public int alterar(Treino t) throws Exception {
        int retorno = 0;
//verificar se muda caso não colocar o personal criador aqui
        try {
            ContentValues values = new ContentValues();
            values.put(TreinoContract.columns.CPF_ALUNO, t.getAluno().getCPF());
            values.put(TreinoContract.columns.CPF_PERSONAL, t.getPersonal().getCPF());

            values.put(TreinoContract.columns.DATA_FIM, t.getDataFim());

            values.put(TreinoContract.columns.DATA_INICIO, t.getDataInicio());

            values.put(TreinoContract.columns.OBJETIVO, t.getObjetivo());
            values.put(TreinoContract.columns.ANOTACOES, t.getAnotacoes());
            values.put(TreinoContract.columns.PESO_IDEAL, t.getPesoIdeal());
            values.put(TreinoContract.columns.PESO_INICIO, t.getPesoInicio());
            values.put(TreinoContract.columns.TIPO_TREINO, t.getTipoTreino());
            values.put(TreinoContract.columns.PROGRESSIVO, t.getProgressivo());

            values.put(TreinoContract.columns.ATUAL, 1);

            String whereClause = TreinoContract.columns.ID + " = ? ";
            String [] whereArgs = {String.valueOf(t.getId())};

            retorno = db.update(TreinoContract.TABLE_NAME, values, whereClause, whereArgs);

            registraAlteracoes(Constantes.UPDATE, String.valueOf(t.getId()));
            if(t.getMusculosRemovidos() != null) {
                for (Musculo m : t.getMusculosRemovidos()) {
                    if (m.getId() != null) {
                        MusculoDAO.getInstance(contextBase).excluir(m);
                    }
                }
            }

            for (ArrayList<Musculo> lstMustulo : t.getLstTodosMusculos()) {
                if(lstMustulo != null) {
                    for (Musculo m : lstMustulo) {
                        if(m.getId() == null) {
                            m.setIdTreino(t.getId());
                            m.setIdTreinoBase(null);
                            MusculoDAO.getInstance(contextBase).salvar(m);
                        }else{
                            MusculoDAO.getInstance(contextBase).alterar(m);
                        }
                    }
                }
            }

        }catch (Exception e){
            e.printStackTrace();
            throw new Exception("Ocorreu um erro na alteração");
        }

        return retorno;
    }

    public int inativarTreinoAntigo(Treino t) throws Exception {
        int retorno = 0;

        try {
            ContentValues values = new ContentValues();

            values.put(TreinoContract.columns.ATUAL, 0);
            String whereClause = TreinoContract.columns.CPF_ALUNO + " = ? ";
            String [] whereArgs = {String.valueOf(t.getAluno().getCPF())};

            retorno = db.update(TreinoContract.TABLE_NAME, values, whereClause, whereArgs);

        }catch (Exception e){
            e.printStackTrace();
            throw new Exception("Ocorreu um erro na alteração");
        }

        return retorno;
    }

    public int alterarStatusTreinoMaisRecente(Treino t) throws Exception {
        int retorno = 0;

        try {
            ContentValues values = new ContentValues();
            values.put(TreinoContract.columns.ATUAL, 1);
            String whereClause = TreinoContract.columns.ID + " = ? ";
            String [] whereArgs = {String.valueOf(t.getId())};

            retorno = db.update(TreinoContract.TABLE_NAME, values, whereClause, whereArgs);

            registraAlteracoes(Constantes.UPDATE, String.valueOf(t.getId()));

        }catch (Exception e){
            e.printStackTrace();
            throw new Exception("Ocorreu um erro na alteração");
        }

        return retorno;
    }

    //Ativa o treino anterior do aluno caso tenha um, se baseia pela data de criação
    public void ativarTreinoAnterior(Aluno a) throws Exception {
        String[] columns = {
                TreinoContract.columns.ID,
                TreinoContract.columns.TIPO_TREINO,
                TreinoContract.columns.OBJETIVO,
                TreinoContract.columns.ANOTACOES,
                TreinoContract.columns.CPF_ALUNO,
                TreinoContract.columns.CPF_PERSONAL,
                TreinoContract.columns.CPF_PERSONAL_CRIADOR,
                TreinoContract.columns.DATA_FIM,
                TreinoContract.columns.DATA_INICIO,
                TreinoContract.columns.PESO_IDEAL,
                TreinoContract.columns.PESO_INICIO,
                TreinoContract.columns.ATUAL,
                TreinoContract.columns.PROGRESSIVO
        };

        String whereClause = TreinoContract.columns.CPF_ALUNO + " = ? ";
        String [] whereArgs = {a.getCPF()};

        List<Treino> treinos = new ArrayList<>();

        try (Cursor c = db.query(TreinoContract.TABLE_NAME, columns, whereClause, whereArgs, null, null, "date(" + TreinoContract.columns.DATA_INICIO + ")")) {
            if (c.moveToFirst()) {
                do {
                    Treino t = fromCursor(c);
                    treinos.add(t);
                } while (c.moveToNext());
            }
            if(treinos.size() > 0){
                alterarStatusTreinoMaisRecente(treinos.get(treinos.size() - 1));
            }
        }
    }

    public List<Treino> listarSemMusculos() {

        String[] columns = {
                TreinoContract.columns.ID,
                TreinoContract.columns.TIPO_TREINO,
                TreinoContract.columns.OBJETIVO,
                TreinoContract.columns.ANOTACOES,
                TreinoContract.columns.CPF_ALUNO,
                TreinoContract.columns.CPF_PERSONAL,
                TreinoContract.columns.CPF_PERSONAL_CRIADOR,
                TreinoContract.columns.DATA_FIM,
                TreinoContract.columns.DATA_INICIO,
                TreinoContract.columns.PESO_IDEAL,
                TreinoContract.columns.PESO_INICIO,
                TreinoContract.columns.ATUAL,
                TreinoContract.columns.PROGRESSIVO
        };

        StringBuilder sb = new StringBuilder();
        sb.append(TreinoContract.columns.ATUAL + " = ? ");

        String whereClause = sb.toString();
        String [] whereArgs = {String.valueOf(Constantes.BANCO_TRUE)};

        List<Treino> treinos = new ArrayList<>();

        try (Cursor c = db.query(TreinoContract.TABLE_NAME, columns, whereClause, whereArgs, null, null, TreinoContract.columns.TIPO_TREINO)) {
            if (c.moveToFirst()) {
                do {
                    Treino t = fromCursor(c);
                    treinos.add(t);
                } while (c.moveToNext());
            }

            return treinos;
        }

    }

    public List<Treino> listarTreinosProgressivosSemMusculos() {
        String[] whereArgs = {};
        String whereClause = "";

        String subSelect = " AND t." + TreinoContract.columns.ID + " IN ( SELECT " + TreinoProgressivoContract.columns.ID_TREINO + " FROM " + TreinoProgressivoContract.TABLE_NAME + " WHERE t." + TreinoContract.columns.ID + " ) ";
        whereClause = " WHERE t." + TreinoContract.columns.ATUAL + " = ? AND t."+ TreinoContract.columns.PROGRESSIVO + " = ? " + subSelect;
        whereArgs = new String[]{String.valueOf(Constantes.BANCO_TRUE), String.valueOf(Constantes.BANCO_TRUE)};

        // query manual
        String query = " SELECT " +
                        "t." + TreinoContract.columns.ID + ", "+
                        "t." + TreinoContract.columns.TIPO_TREINO + ", "+
                        "t." + TreinoContract.columns.OBJETIVO + ", "+
                        "t." + TreinoContract.columns.ANOTACOES + ", "+
                        "t." + TreinoContract.columns.CPF_ALUNO + ", "+
                        "t." + TreinoContract.columns.CPF_PERSONAL + ", "+
                        "t." + TreinoContract.columns.CPF_PERSONAL_CRIADOR + ", "+
                        "t." + TreinoContract.columns.DATA_FIM + ", "+
                        "t." + TreinoContract.columns.DATA_INICIO + ", "+
                        "t." + TreinoContract.columns.PESO_IDEAL + ", "+
                        "t." + TreinoContract.columns.PESO_INICIO + ", "+
                        "t." + TreinoContract.columns.ATUAL + ", "+
                        "t." + TreinoContract.columns.PROGRESSIVO +
                        " FROM " + AlunoContract.TABLE_NAME + " a " +
                        " LEFT JOIN " + TreinoContract.TABLE_NAME + " t ON t." + TreinoContract.columns.CPF_ALUNO + " = a." + AlunoContract.columns.CPF + " " +
                        whereClause +
                        " GROUP BY a.Cpf " +
                        " ORDER BY a.Nome";

        List<Treino> treinos = new ArrayList<>();

        try (Cursor c = db.rawQuery(query, whereArgs)) {
            if (c.moveToFirst()) {
                do {
                    Treino t = fromCursor(c);
                    treinos.add(t);
                } while (c.moveToNext());
            }

            return treinos;
        }

    }

    public List<Treino> listarSemMusculosFiltrada(String filtro) {

            String[] whereArgs = {};
            String whereClause = "";

            whereClause = " WHERE A."+AlunoContract.columns.NOME+" like ? AND T.Atual = ? ";
            whereArgs = new String[]{"%" + filtro + "%", String.valueOf(Constantes.BANCO_TRUE)};

            List<Treino> treinos = new ArrayList<>();
            // query manual
            String query = "SELECT "+
                            " T."+TreinoContract.columns.ID+
                            " ,T."+TreinoContract.columns.TIPO_TREINO+
                            " ,T."+TreinoContract.columns.OBJETIVO+
                            " ,T."+TreinoContract.columns.ANOTACOES+
                            " ,T."+TreinoContract.columns.CPF_ALUNO+
                            " ,T."+TreinoContract.columns.CPF_PERSONAL+
                            " ,T."+TreinoContract.columns.CPF_PERSONAL_CRIADOR+
                            " ,T."+TreinoContract.columns.DATA_FIM+
                            " ,T."+TreinoContract.columns.DATA_INICIO+
                            " ,T."+TreinoContract.columns.PESO_IDEAL+
                            " ,T."+TreinoContract.columns.PESO_INICIO+
                            " ,T."+TreinoContract.columns.ATUAL+
                            " ,T."+TreinoContract.columns.PROGRESSIVO+
                            " FROM " + TreinoContract.TABLE_NAME + " T "+
                            " INNER JOIN "+AlunoContract.TABLE_NAME+" A ON T.CpfAluno = A.Cpf " +
                            whereClause +
                            " GROUP BY A.Cpf " +
                            " ORDER BY A.Nome";

            //fim query manual
            try (Cursor c = db.rawQuery(query, whereArgs)) {
                if (c.moveToFirst()) {
                    do {
                        Treino t = fromCursor(c);
                        treinos.add(t);
                    } while (c.moveToNext());
                }

                return treinos;
            }

    }

    public List<Treino> listarTreinosProgressivosSemMusculosFiltrada(String filtro) {

        String[] whereArgs = {};
        String whereClause = "";

        whereClause = " WHERE A."+AlunoContract.columns.NOME+" like ? AND T.Atual = ? AND T.Progressivo = ? ";
        whereArgs = new String[]{"%" + filtro + "%", String.valueOf(Constantes.BANCO_TRUE), String.valueOf(Constantes.BANCO_TRUE)};

        List<Treino> treinos = new ArrayList<>();
        // query manual
        String query = "SELECT "+
                " T."+TreinoContract.columns.ID+
                " ,T."+TreinoContract.columns.TIPO_TREINO+
                " ,T."+TreinoContract.columns.OBJETIVO+
                " ,T."+TreinoContract.columns.ANOTACOES+
                " ,T."+TreinoContract.columns.CPF_ALUNO+
                " ,T."+TreinoContract.columns.CPF_PERSONAL+
                " ,T."+TreinoContract.columns.CPF_PERSONAL_CRIADOR+
                " ,T."+TreinoContract.columns.DATA_FIM+
                " ,T."+TreinoContract.columns.DATA_INICIO+
                " ,T."+TreinoContract.columns.PESO_IDEAL+
                " ,T."+TreinoContract.columns.PESO_INICIO+
                " ,T."+TreinoContract.columns.ATUAL+
                " ,T."+TreinoContract.columns.PROGRESSIVO+
                " FROM " + TreinoContract.TABLE_NAME + " T "+
                " INNER JOIN "+AlunoContract.TABLE_NAME+" A ON T.CpfAluno = A.Cpf " +
                whereClause +
                " GROUP BY A.Cpf " +
                " ORDER BY A.Nome";

        //fim query manual
        try (Cursor c = db.rawQuery(query, whereArgs)) {
            if (c.moveToFirst()) {
                do {
                    Treino t = fromCursor(c);
                    treinos.add(t);
                } while (c.moveToNext());
            }

            return treinos;
        }

    }

    public Treino buscarTreinoComMusculosFiltrados(Aluno aluno, ArrayList<Integer> arrTipoMusculos, boolean somenteTreinoProgressivo, boolean comTreinoProgressivo) {
        //tipo sequencia 0 - simples / 1 - misto

        Integer progressivo = Constantes.BANCO_FALSE;
        if(somenteTreinoProgressivo){
            progressivo = Constantes.BANCO_TRUE;
        }


        String[] columns = {
                TreinoContract.columns.ID,
                TreinoContract.columns.TIPO_TREINO,
                TreinoContract.columns.OBJETIVO,
                TreinoContract.columns.ANOTACOES,
                TreinoContract.columns.CPF_ALUNO,
                TreinoContract.columns.CPF_PERSONAL,
                TreinoContract.columns.CPF_PERSONAL_CRIADOR,
                TreinoContract.columns.DATA_FIM,
                TreinoContract.columns.DATA_INICIO,
                TreinoContract.columns.PESO_IDEAL,
                TreinoContract.columns.PESO_INICIO,
                TreinoContract.columns.ATUAL,
                TreinoContract.columns.PROGRESSIVO
        };

        Treino treino = null;

        StringBuilder builder = new StringBuilder();
        builder.append(TreinoContract.columns.CPF_ALUNO + " = ? AND ");
        builder.append(TreinoContract.columns.ATUAL + " = ? ");

        String [] whereArgs;

        if(comTreinoProgressivo) {
            builder.append(" AND " + TreinoContract.columns.PROGRESSIVO + " = ? ");
            whereArgs = new String[]{String.valueOf(aluno.getCPF()), String.valueOf(Constantes.BANCO_TRUE), String.valueOf(progressivo)};
        }else{
            whereArgs = new String[]{String.valueOf(aluno.getCPF()), String.valueOf(Constantes.BANCO_TRUE)};
        }
        String whereClause = builder.toString();

        try (Cursor c = db.query(TreinoContract.TABLE_NAME, columns, whereClause, whereArgs, null, null, TreinoContract.columns.TIPO_TREINO)) {
            if (c.moveToFirst()) {
                    treino = fromCursor(c);
            }

            if(treino != null){
                for (int i = 0; i < arrTipoMusculos.size(); i++){
                    treino.addLstMusculos(MusculoDAO.getInstance(contextBase).retornaMusculos(treino, arrTipoMusculos.get(i)), arrTipoMusculos.get(i));
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return treino;
    }

    public Treino retornaTreinoComMusculos(Integer idTreino) {
        //tipo sequencia 0 - simples / 1 - misto

        String[] columns = {
                TreinoContract.columns.ID,
                TreinoContract.columns.TIPO_TREINO,
                TreinoContract.columns.OBJETIVO,
                TreinoContract.columns.ANOTACOES,
                TreinoContract.columns.CPF_ALUNO,
                TreinoContract.columns.CPF_PERSONAL,
                TreinoContract.columns.CPF_PERSONAL_CRIADOR,
                TreinoContract.columns.DATA_FIM,
                TreinoContract.columns.DATA_INICIO,
                TreinoContract.columns.PESO_IDEAL,
                TreinoContract.columns.PESO_INICIO,
                TreinoContract.columns.ATUAL,
                TreinoContract.columns.PROGRESSIVO
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

        Treino treino = null;

        StringBuilder builder = new StringBuilder();
        builder.append(TreinoContract.columns.ID + " = ? ");

        String whereClause = builder.toString();
        String [] whereArgs = {String.valueOf(idTreino)};

        try (Cursor c = db.query(TreinoContract.TABLE_NAME, columns, whereClause, whereArgs, null, null, TreinoContract.columns.TIPO_TREINO)) {
            if (c.moveToFirst()) {
                treino = fromCursor(c);
            }

            if(treino != null){
                for (int i = 0; i < arrMusculos.size(); i++){
                    treino.addLstMusculos(MusculoDAO.getInstance(contextBase).retornaMusculos(treino, arrMusculos.get(i)), arrMusculos.get(i));
                }
            }
        }

        return treino;
    }

    public Treino retornaTreinoPorIdAtivo(Integer idTreino) {
        //tipo sequencia 0 - simples / 1 - misto

        String[] columns = {
                TreinoContract.columns.ID,
                TreinoContract.columns.TIPO_TREINO,
                TreinoContract.columns.OBJETIVO,
                TreinoContract.columns.ANOTACOES,
                TreinoContract.columns.CPF_ALUNO,
                TreinoContract.columns.CPF_PERSONAL,
                TreinoContract.columns.CPF_PERSONAL_CRIADOR,
                TreinoContract.columns.DATA_FIM,
                TreinoContract.columns.DATA_INICIO,
                TreinoContract.columns.PESO_IDEAL,
                TreinoContract.columns.PESO_INICIO,
                TreinoContract.columns.ATUAL,
                TreinoContract.columns.PROGRESSIVO
        };

        Treino treino = null;

        StringBuilder builder = new StringBuilder();
        builder.append(TreinoContract.columns.ID + " = ? AND ");
        builder.append(TreinoContract.columns.ATUAL + " = ? ");

        String whereClause = builder.toString();
        String [] whereArgs = {String.valueOf(idTreino), String.valueOf(Constantes.BANCO_TRUE)};

        try (Cursor c = db.query(TreinoContract.TABLE_NAME, columns, whereClause, whereArgs, null, null, TreinoContract.columns.TIPO_TREINO)) {
            if (c.moveToFirst()) {
                treino = fromCursor(c);
            }
        }

        return treino;
    }

    public Treino retornaTreinoPorId(Integer idTreino) {
        //tipo sequencia 0 - simples / 1 - misto

        String[] columns = {
                TreinoContract.columns.ID,
                TreinoContract.columns.TIPO_TREINO,
                TreinoContract.columns.OBJETIVO,
                TreinoContract.columns.ANOTACOES,
                TreinoContract.columns.CPF_ALUNO,
                TreinoContract.columns.CPF_PERSONAL,
                TreinoContract.columns.CPF_PERSONAL_CRIADOR,
                TreinoContract.columns.DATA_FIM,
                TreinoContract.columns.DATA_INICIO,
                TreinoContract.columns.PESO_IDEAL,
                TreinoContract.columns.PESO_INICIO,
                TreinoContract.columns.ATUAL,
                TreinoContract.columns.PROGRESSIVO
        };

        Treino treino = null;

        StringBuilder builder = new StringBuilder();
        builder.append(TreinoContract.columns.ID + " = ? ");

        String whereClause = builder.toString();
        String [] whereArgs = {String.valueOf(idTreino)};

        try (Cursor c = db.query(TreinoContract.TABLE_NAME, columns, whereClause, whereArgs, null, null, TreinoContract.columns.TIPO_TREINO)) {
            if (c.moveToFirst()) {
                treino = fromCursor(c);
            }
        }

        return treino;
    }

    public Treino buscarTreinoComMusculosFiltradosParaAlterar(Treino treino, ArrayList<Integer> arrTipoMusculos) {

            if(treino != null){
                for (int i = 0; i < arrTipoMusculos.size(); i++){
                    treino.addLstMusculos(MusculoDAO.getInstance(contextBase).retornaMusculos(treino, arrTipoMusculos.get(i)), arrTipoMusculos.get(i));
                }
            }

        return treino;
    }

    public ArrayList<Integer> retornaIdsTreinosDoAluno(Aluno a){
        ArrayList<Integer> arrIdTreinoAluno = new ArrayList<>();

        String[] columns = {
                TreinoContract.columns.ID,
                TreinoContract.columns.CPF_ALUNO
        };

        String whereClause = TreinoContract.columns.CPF_ALUNO + " = ? ";
        String [] whereArgs = {String.valueOf(a.getCPF())};

        try (Cursor c = db.query(TreinoContract.TABLE_NAME, columns, whereClause, whereArgs, null, null, TreinoContract.columns.ID)) {
            if (c.moveToFirst()) {
                do{
                    arrIdTreinoAluno.add(c.getInt(c.getColumnIndex(TreinoContract.columns.ID)));
                }while (c.moveToNext());
            }
        }

        return arrIdTreinoAluno;
    }

    public Treino retornaTreinoAtualDoAluno(Aluno a){
        String[] columns = {
                TreinoContract.columns.ID,
                TreinoContract.columns.TIPO_TREINO,
                TreinoContract.columns.OBJETIVO,
                TreinoContract.columns.ANOTACOES,
                TreinoContract.columns.CPF_ALUNO,
                TreinoContract.columns.CPF_PERSONAL,
                TreinoContract.columns.CPF_PERSONAL_CRIADOR,
                TreinoContract.columns.DATA_FIM,
                TreinoContract.columns.DATA_INICIO,
                TreinoContract.columns.PESO_IDEAL,
                TreinoContract.columns.PESO_INICIO,
                TreinoContract.columns.ATUAL,
                TreinoContract.columns.PROGRESSIVO
        };

        StringBuilder sb = new StringBuilder();
        sb.append(TreinoContract.columns.CPF_ALUNO + " = ? AND ");
        sb.append(TreinoContract.columns.ATUAL + " = ? ");
        String whereClause = sb.toString();
        String [] whereArgs = {String.valueOf(a.getCPF()), String.valueOf(Constantes.BANCO_TRUE)};

        Treino treino = null;

        try (Cursor c = db.query(TreinoContract.TABLE_NAME, columns, whereClause, whereArgs, null, null, TreinoContract.columns.ID)) {
            if (c.moveToFirst()) {
                treino = fromCursor(c);
            }
        }

        return treino;
    }

    public boolean excluir(Treino t, Boolean registra, Boolean ativaAnterior) throws Exception {
        boolean retorno = false;

        ProgramaTreinoDAO.getInstance(contextBase).excluirTodosDoAluno(t.getAluno(), false);

        ArrayList<Integer> arrIdsMusculos = MusculoDAO.getInstance(contextBase).retornaIdsMusculosTreino(t);

        if(arrIdsMusculos.size() > 0){
            MusculoDAO.getInstance(contextBase).excluirTodosDoTreino(t);
        }

        if(t.getProgressivo()){
            TreinoProgressivoDAO.getInstance(contextBase).excluirTodosDoTreino(t, false);
        }

        try {
            ContentValues values = new ContentValues();

            String whereClause = TreinoContract.columns.ID + " = ? ";
            String [] whereArgs = {String.valueOf(t.getId())};

            db.delete(TreinoContract.TABLE_NAME, whereClause, whereArgs);
            //manda esses valores como chave para o servidor conseguir achar o id especifico
            if(registra) {
                registraAlteracoes(Constantes.DELETE, t.getId() + "/" + t.getTipoTreino() + "/" + t.getDataInicio() + "/" + t.getAluno().getCPF() + "/" + t.getAtual() + "/" + t.getPersonalCriador().getCPF());
            }

            retorno = true;

        }catch (Exception e){
            e.printStackTrace();
            throw new Exception("Ocorreu um erro na exclusão");
        }

        if(ativaAnterior){
            ativarTreinoAnterior(t.getAluno());
        }

        return retorno;
    }

    public boolean excluirTreinoComDadosServidor(Treino t) throws Exception {
        boolean retorno = false;

        Treino treino = retornaTreinoComDadosServidor(t);

        if(treino != null) {
            retorno = excluir(treino, false, true);
        }

        return retorno;
    }

    public Treino retornaTreinoComDadosServidor(Treino t) throws Exception{
        String[] columns = {
                TreinoContract.columns.ID,
                TreinoContract.columns.TIPO_TREINO,
                TreinoContract.columns.OBJETIVO,
                TreinoContract.columns.ANOTACOES,
                TreinoContract.columns.CPF_ALUNO,
                TreinoContract.columns.CPF_PERSONAL,
                TreinoContract.columns.CPF_PERSONAL_CRIADOR,
                TreinoContract.columns.DATA_FIM,
                TreinoContract.columns.DATA_INICIO,
                TreinoContract.columns.PESO_IDEAL,
                TreinoContract.columns.PESO_INICIO,
                TreinoContract.columns.ATUAL,
                TreinoContract.columns.PROGRESSIVO
        };

        Treino treino = null;

        StringBuilder builder = new StringBuilder();
        builder.append(TreinoContract.columns.TIPO_TREINO + " = ? AND ");
        builder.append(TreinoContract.columns.DATA_INICIO + " = ? AND ");
        builder.append(TreinoContract.columns.CPF_ALUNO + " = ? AND ");
        builder.append(TreinoContract.columns.CPF_PERSONAL_CRIADOR + " = ? ");

        t.setDataInicio(retiraZeroEsquedaData(t.getDataInicio()));

        String whereClause = builder.toString();
        String [] whereArgs = {t.getTipoTreino(), t.getDataInicio(), t.getAluno().getCPF(), t.getPersonalCriador().getCPF()};

        try (Cursor c = db.query(TreinoContract.TABLE_NAME, columns, whereClause, whereArgs, null, null, TreinoContract.columns.TIPO_TREINO)) {
            if (c.moveToFirst()) {
                treino = fromCursor(c);
            }
        }

        return treino;
    }

    private String retiraZeroEsquedaData(String dataInicio) {
        String[] dtInicio = dataInicio.split("-");
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

    private void registraAlteracoes(String acao, String chave) throws Exception {

        Alteracao a = new Alteracao();
        a.setAcao(acao);
        a.setAtivo(1);
        a.setChave(chave);

        AlteracaoDAO.getInstance(contextBase).salvar(a, TreinoContract.TABLE_NAME);
    }

    //atualizar dpois aqui treino progressivo em json
    public long salvarJson(JSONObject obj) throws Exception{
        Treino t = new Treino();
        t.setTipoTreino(obj.getString("tipoTreino"));
        t.setPesoInicio(obj.getDouble("pesoInicio"));
        t.setPesoIdeal(obj.getDouble("pesoIdeal"));
        t.setDataInicio(retiraZeroEsquedaData(obj.getString("dataInicio")));
        t.setDataFim(retiraZeroEsquedaData(obj.getString("dataFim")));
        t.setObjetivo(obj.getString("objetivo"));
        t.setAnotacoes(obj.getString("anotacoes"));
        t.setAluno(new Aluno(obj.getString("cpfAluno")));

        SharedPreferences sp = contextBase.getSharedPreferences(Constantes.SHARED_PREFERENCES_LOGIN, Context.MODE_PRIVATE);
        t.setPersonal(new Personal(sp.getString(Constantes.CPF_TREINADOR_LOGADO, "")));
        t.setPersonalCriador(new Personal(obj.getString("cpfPersonal")));

        Boolean atual = false;
        if(obj.getInt("atual") == 1){
            atual = true;
        }

        t.setAtual(atual);

        Boolean progressivo = false;
        if(obj.getInt("progressivo") == 1){
            progressivo = true;
        }

        t.setProgressivo(progressivo);

        JSONArray arrMusculos = obj.getJSONArray("musculos");
        //faço isso para ele retornar a lista de cada musculo com seu indice, basta preencher
        t.setLstTodosMusculos(new ArrayList<ArrayList<Musculo>>());
        ArrayList<ArrayList<Musculo>> lstMusculos = t.getLstTodosMusculos();

        for (int j = 0; j < arrMusculos.length(); j++){
            JSONObject objMusculo = arrMusculos.getJSONObject(j);
            Musculo m = new Musculo();
            m.setOrdem(objMusculo.getInt("ordem"));
            m.setExercicio(objMusculo.getString("exercicio"));
            m.setSeries(objMusculo.getInt("series"));
            m.setTmpExecIni(objMusculo.getInt("tmpExecIni"));
            m.setTmpExecFim(objMusculo.getInt("tmpExecFim"));
            m.setIntervalos(objMusculo.getInt("intervalos"));
            m.setRepeticoes(objMusculo.getString("repeticoes"));
            m.setCarga(objMusculo.getString("cargas"));
            m.setTipoMusculo(objMusculo.getInt("tipoMusculo"));

            switch (m.getTipoMusculo()){
                case Constantes.BICEPS:
                    lstMusculos.get(Constantes.BICEPS).add(m);
                    break;

                case Constantes.TRICEPS:
                    lstMusculos.get(Constantes.TRICEPS).add(m);
                    break;

                case Constantes.PEITO:
                    lstMusculos.get(Constantes.PEITO).add(m);
                    break;

                case Constantes.OMBRO:
                    lstMusculos.get(Constantes.OMBRO).add(m);
                    break;

                case Constantes.COSTAS:
                    lstMusculos.get(Constantes.COSTAS).add(m);
                    break;

                case Constantes.PERNA:
                    lstMusculos.get(Constantes.PERNA).add(m);
                    break;

                case Constantes.GLUTEOS:
                    lstMusculos.get(Constantes.GLUTEOS).add(m);
                    break;

                case Constantes.ANTEBRACO:
                    lstMusculos.get(Constantes.ANTEBRACO).add(m);

                    break;

                case Constantes.ABDOMINAIS:
                    lstMusculos.get(Constantes.ABDOMINAIS).add(m);
                    break;
            }
        }

        return TreinoDAO.getInstance(contextBase).salvar(t, false, true);
    }

    public String buscaAnotacaoTreinoAluno(Aluno aluno) {
        String[] columns = {
                TreinoContract.columns.ID,
                TreinoContract.columns.TIPO_TREINO,
                TreinoContract.columns.OBJETIVO,
                TreinoContract.columns.ANOTACOES,
                TreinoContract.columns.CPF_ALUNO,
                TreinoContract.columns.CPF_PERSONAL,
                TreinoContract.columns.CPF_PERSONAL_CRIADOR,
                TreinoContract.columns.DATA_FIM,
                TreinoContract.columns.DATA_INICIO,
                TreinoContract.columns.PESO_IDEAL,
                TreinoContract.columns.PESO_INICIO,
                TreinoContract.columns.ATUAL,
                TreinoContract.columns.PROGRESSIVO
        };

        StringBuilder sb = new StringBuilder();
        sb.append(TreinoContract.columns.CPF_ALUNO + " = ? AND ");
        sb.append(TreinoContract.columns.ATUAL + " = ? ");
        String whereClause = sb.toString();
        String [] whereArgs = {String.valueOf(aluno.getCPF()), String.valueOf(Constantes.BANCO_TRUE)};

        Treino treino = null;

        try (Cursor c = db.query(TreinoContract.TABLE_NAME, columns, whereClause, whereArgs, null, null, TreinoContract.columns.ID)) {
            if (c.moveToFirst()) {
                treino = fromCursor(c);
            }
        }

        return treino.getAnotacoes();
    }


    public Treino retornaTreinoDoPrograma(ProgramaTreino programaSelecionado) {
        return null;
    }

    private static Treino fromCursor(Cursor c) {
        int id = c.getInt(c.getColumnIndex(TreinoContract.columns.ID));
        String tipoTreino = c.getString(c.getColumnIndex(TreinoContract.columns.TIPO_TREINO));
        String objetivo = c.getString(c.getColumnIndex(TreinoContract.columns.OBJETIVO));
        String anotacoes = c.getString(c.getColumnIndex(TreinoContract.columns.ANOTACOES));
        String cpfAluno = c.getString(c.getColumnIndex(TreinoContract.columns.CPF_ALUNO));
        String cpfPersonal = c.getString(c.getColumnIndex(TreinoContract.columns.CPF_PERSONAL));
        String cpfPersonalCriador = c.getString(c.getColumnIndex(TreinoContract.columns.CPF_PERSONAL_CRIADOR));

        String dtFim =  c.getString(c.getColumnIndex(TreinoContract.columns.DATA_FIM));
        String dtInicio =  c.getString(c.getColumnIndex(TreinoContract.columns.DATA_INICIO));

        Double pesoIdeal = c.getDouble(c.getColumnIndex(TreinoContract.columns.PESO_IDEAL));
        Double pesoInicio = c.getDouble(c.getColumnIndex(TreinoContract.columns.PESO_INICIO));

        int atual =  c.getInt(c.getColumnIndex(TreinoContract.columns.ATUAL));
        Boolean atualBoolean;
        if(atual == Constantes.BANCO_TRUE){
            atualBoolean = true;
        }else{
            atualBoolean = false;
        }

        int progressivo =  c.getInt(c.getColumnIndex(TreinoContract.columns.PROGRESSIVO));
        Boolean progressivoBoolean;
        if(progressivo == Constantes.BANCO_TRUE){
            progressivoBoolean = true;
        }else{
            progressivoBoolean = false;
        }

        return new Treino(id,tipoTreino, pesoInicio, pesoIdeal, dtInicio, dtFim, objetivo, anotacoes, new Aluno(cpfAluno), new Personal(cpfPersonal), atualBoolean, progressivoBoolean, new Personal(cpfPersonalCriador));
    }


}
