package com.masidev.fitre.data.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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
import com.masidev.fitre.data.contract.TreinoProgressivoContract;
import com.masidev.fitre.data.contract.TreinoBaseContract;
import com.masidev.fitre.data.contract.TreinoContract;

/**
 * Created by jmasiero on 22/09/15.
 */
public class DBHelper extends SQLiteOpenHelper{

    public static final String DB_NAME = "FitreDb";
    public static final int DB_VERSION = 5;

    //Creates Banco
    //Create TB Personal
    private static final String SQL_DROP_PERSONAL = "DROP TABLE IF EXISTS " + PersonalContract.TABLE_NAME;
    private static final String SQL_CREATE_PERSONAL = String.format("CREATE TABLE %s ( %s TEXT PRIMARY KEY NOT NULL, " +
                                                                                      " %s TEXT NOT NULL," +
                                                                                      " %s TEXT NOT NULL," +
                                                                                      " %s TEXT NOT NULL)",
                                                                                        PersonalContract.TABLE_NAME,
                                                                                        PersonalContract.columns.CPF,
                                                                                        PersonalContract.columns.NOME,
                                                                                        PersonalContract.columns.CREF,
                                                                                        PersonalContract.columns.SENHA);

    //Create TB Aluno
    private static final String SQL_DROP_ALUNO = "DROP TABLE IF EXISTS " + AlunoContract.TABLE_NAME;
    private static final String SQL_CREATE_ALUNO = String.format("CREATE TABLE %s ( %s TEXT PRIMARY KEY NOT NULL, " +
                                                                                            " %s TEXT NOT NULL," +
                                                                                            " %s TEXT NOT NULL," +
                                                                                            " %s TEXT,"+
                                                                                            " %s TEXT NOT NULL)",
                                                                                    AlunoContract.TABLE_NAME,
                                                                                    AlunoContract.columns.CPF,
                                                                                    AlunoContract.columns.NOME,
                                                                                    AlunoContract.columns.DTNASIMENTO,
                                                                                    AlunoContract.columns.OBSERVACAO,
                                                                                    AlunoContract.columns.SEXO);

    //Create TB Treino
    private static final String SQL_DROP_TREINO = "DROP TABLE IF EXISTS " + TreinoContract.TABLE_NAME;
    private static final String SQL_CREATE_TREINO = String.format("CREATE TABLE %s ( %s INTEGER PRIMARY KEY NOT NULL, " +
                                                                                    " %s TEXT NOT NULL," +
                                                                                    " %s REAL NOT NULL," +
                                                                                    " %s REAL NOT NULL," +
                                                                                    " %s TEXT NOT NULL," +
                                                                                    " %s TEXT NOT NULL," +
                                                                                    " %s TEXT NOT NULL," +
                                                                                    " %s TEXT ," +
                                                                                    " %s TEXT NOT NULL," +
                                                                                    " %s TEXT NOT NULL," +
                                                                                    " %s TEXT NOT NULL," +
                                                                                    " %s INTEGER NOT NULL," +
                                                                                    " %s INTEGER NOT NULL," +
                                                                                    " FOREIGN KEY (%s) REFERENCES %s(%s) ," +
                                                                                    " FOREIGN KEY (%s) REFERENCES %s(%s) )",
                                                                    TreinoContract.TABLE_NAME,
                                                                    TreinoContract.columns.ID,
                                                                    TreinoContract.columns.TIPO_TREINO,
                                                                    TreinoContract.columns.PESO_INICIO,
                                                                    TreinoContract.columns.PESO_IDEAL,
                                                                    TreinoContract.columns.DATA_INICIO,
                                                                    TreinoContract.columns.DATA_FIM,
                                                                    TreinoContract.columns.OBJETIVO,
                                                                    TreinoContract.columns.ANOTACOES,
                                                                    TreinoContract.columns.CPF_ALUNO,
                                                                    TreinoContract.columns.CPF_PERSONAL,
                                                                    TreinoContract.columns.CPF_PERSONAL_CRIADOR,
                                                                    TreinoContract.columns.ATUAL,
                                                                    TreinoContract.columns.PROGRESSIVO,

                                                                    TreinoContract.columns.CPF_ALUNO,
                                                                    AlunoContract.TABLE_NAME,
                                                                    AlunoContract.columns.CPF,

                                                                    TreinoContract.columns.CPF_PERSONAL,
                                                                    PersonalContract.TABLE_NAME,
                                                                    PersonalContract.columns.CPF);


    //Create TB TreinoBase
    private static final String SQL_DROP_TREINO_BASE = "DROP TABLE IF EXISTS " + TreinoBaseContract.TABLE_NAME;
    private static final String SQL_CREATE_TREINO_BASE = String.format("CREATE TABLE %s ( %s INTEGER PRIMARY KEY NOT NULL, " +
                                                                            " %s TEXT NOT NULL, "+
                                                                            " %s TEXT NOT NULL, " +
                                                                            " FOREIGN KEY (%s) REFERENCES %s(%s) )",
                                                                        TreinoBaseContract.TABLE_NAME,
                                                                        TreinoBaseContract.columns.ID,
                                                                        TreinoBaseContract.columns.TIPO_TREINO,
                                                                        TreinoBaseContract.columns.CPF_PERSONAL,
                                                                        TreinoBaseContract.columns.CPF_PERSONAL,
                                                                        PersonalContract.TABLE_NAME,
                                                                        PersonalContract.columns.CPF);

    //Create TB MusculoTreinadoSemana
    private static final String SQL_DROP_MUSCULO_TREINADO_SEMANA = "DROP TABLE IF EXISTS " + MusculoTreinadoSemanaContract.TABLE_NAME;
    private static final String SQL_CREATE_MUSCULO_TREINADO_SEMANA = String.format("CREATE TABLE %s ( %s INTEGER PRIMARY KEY NOT NULL, " +
                                                                                                    " %s INTEGER NOT NULL," +
                                                                                                    " %s INTEGER NOT NULL," +
                                                                                                    " %s INTEGER NOT NULL," +
                                                                                                    " FOREIGN KEY (%s) REFERENCES %s(%s) ON DELETE CASCADE )",
                                                                                            MusculoTreinadoSemanaContract.TABLE_NAME,
                                                                                            MusculoTreinadoSemanaContract.columns.ID,
                                                                                            MusculoTreinadoSemanaContract.columns.TIPO_MUSCULO,
                                                                                            MusculoTreinadoSemanaContract.columns.TREINOU,
                                                                                            MusculoTreinadoSemanaContract.columns.CPF_ALUNO,
                                                                                            MusculoTreinadoSemanaContract.columns.CPF_ALUNO,
                                                                                            AlunoContract.TABLE_NAME,
                                                                                            AlunoContract.columns.CPF);

    //Create TB Musculo
    private static final String SQL_DROP_MUSCULO = "DROP TABLE IF EXISTS " + MusculoContract.TABLE_NAME;
    private static final String SQL_CREATE_MUSCULO = String.format("CREATE TABLE %s " +
                                                                    "( %s INTEGER PRIMARY KEY NOT NULL, " +
                                                                    " %s INTEGER NOT NULL," +
                                                                    " %s TEXT NOT NULL," +
                                                                    " %s INTEGER NOT NULL," +
                                                                    " %s INTEGER NOT NULL," +
                                                                    " %s INTEGER NOT NULL," +
                                                                    " %s INTEGER NOT NULL," +
                                                                    " %s TEXT NOT NULL," +
                                                                    " %s TEXT NOT NULL," +
                                                                    " %s INTEGER NOT NULL," +
                                                                    " %s INTEGER ," +
                                                                    " %s INTEGER ," +
                                                                    " FOREIGN KEY (%s) REFERENCES %s(%s) ON DELETE CASCADE , " +
                                                                    " FOREIGN KEY (%s) REFERENCES %s(%s) ON DELETE CASCADE )",
                                                            MusculoContract.TABLE_NAME,
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

                                                            MusculoContract.columns.ID_TREINO,
                                                            TreinoContract.TABLE_NAME,
                                                            TreinoContract.columns.ID,

                                                            MusculoContract.columns.ID_TREINO_BASE,
                                                            TreinoBaseContract.TABLE_NAME,
                                                            TreinoBaseContract.columns.ID);


    //Create TB Tabela
    private static final String SQL_DROP_TABELA = "DROP TABLE IF EXISTS " + TabelaContract.TABLE_NAME;
    private static final String SQL_CREATE_TABELA = String.format("CREATE TABLE %s ( %s INTEGER PRIMARY KEY NOT NULL, " +
                                                                                    " %s TEXT NOT NULL, "+
                                                                                    " %s TEXT NOT NULL)",
                                                                            TabelaContract.TABLE_NAME,
                                                                            TabelaContract.columns.ID,
                                                                            TabelaContract.columns.NOME,
                                                                            TabelaContract.columns.TIPO_CHAVE);

    //Create TB Alteracao
    private static final String SQL_DROP_ALTERACAO = "DROP TABLE IF EXISTS " + AlteracaoContract.TABLE_NAME;
    private static final String SQL_CREATE_ALTERACAO = String.format("CREATE TABLE %s ( %s INTEGER PRIMARY KEY NOT NULL, " +
                                                                                        " %s INTEGER NOT NULL," +
                                                                                        " %s TEXT NOT NULL, "+
                                                                                        " %s TEXT NOT NULL, "+
                                                                                        " %s INTEGER NOT NULL, "+
                                                                                        " %s TEXT , "+
                                                                                        " %s TEXT NOT NULL, "+
                                                                                        " %s TEXT,  "+
                                                                                        " FOREIGN KEY (%s) REFERENCES %s(%s) ON DELETE CASCADE , "+
                                                                                        " FOREIGN KEY (%s) REFERENCES %s(%s) , "+
                                                                                        " FOREIGN KEY (%s) REFERENCES %s(%s) ON DELETE CASCADE ) " ,
                                                                                AlteracaoContract.TABLE_NAME,
                                                                                AlteracaoContract.columns.ID,
                                                                                AlteracaoContract.columns.ID_TABELA,
                                                                                AlteracaoContract.columns.ACAO,
                                                                                AlteracaoContract.columns.CHAVE,
                                                                                AlteracaoContract.columns.ATIVO,
                                                                                AlteracaoContract.columns.CPF_PERSONAL,
                                                                                AlteracaoContract.columns.DATA,
                                                                                AlteracaoContract.columns.CNPJ_ACADEMIA,

                                                                                AlteracaoContract.columns.ID_TABELA,
                                                                                TabelaContract.TABLE_NAME,
                                                                                TabelaContract.columns.ID,

                                                                                AlteracaoContract.columns.CNPJ_ACADEMIA,
                                                                                AcademiaContract.TABLE_NAME,
                                                                                AcademiaContract.columns.CNPJ,

                                                                                AlteracaoContract.columns.CPF_PERSONAL,
                                                                                PersonalContract.TABLE_NAME,
                                                                                PersonalContract.columns.CPF);

    //Create TB Academia
    private static final String SQL_DROP_ACADEMIA = "DROP TABLE IF EXISTS " + AcademiaContract.TABLE_NAME;
    private static final String SQL_CREATE_ACADEMIA = String.format("CREATE TABLE %s ( %s TEXT PRIMARY KEY NOT NULL, " +
                                                                                        " %s TEXT NOT NULL, "+
                                                                                        " %s INTEGER, "+
                                                                                        " %s INTEGER, "+
                                                                                        " %s INTEGER, "+
                                                                                        " %s TEXT NOT NULL, "+
                                                                                        " %s INTEGER, "+
                                                                                        " %s TEXT )",
                                                                                AcademiaContract.TABLE_NAME,
                                                                                AcademiaContract.columns.CNPJ,
                                                                                AcademiaContract.columns.NOME_FANTASIA,
                                                                                AcademiaContract.columns.ULTIMO_ID_INSERT,
                                                                                AcademiaContract.columns.ULTIMO_ID_DELETE,
                                                                                AcademiaContract.columns.ULTIMO_ID_UPDATE,
                                                                                AcademiaContract.columns.TELEFONE,
                                                                                AcademiaContract.columns.PAGOU,
                                                                                AcademiaContract.columns.DT_ULTIMO_PAGAMENTO);

    //Create TB PersonalAcademia
    private static final String SQL_DROP_PERSONAL_ACADEMIA = "DROP TABLE IF EXISTS " + PersonalAcademiaContract.TABLE_NAME;
    private static final String SQL_CREATE_PERSONAL_ACADEMIA = String.format("CREATE TABLE %s ( %s INTEGER PRIMARY KEY NOT NULL, " +
                                                                                                " %s TEXT NOT NULL, "+
                                                                                                " %s TEXT NOT NULL , "+
                                                                                                " FOREIGN KEY (%s) REFERENCES %s(%s) ON DELETE CASCADE , "+
                                                                                                " FOREIGN KEY (%s) REFERENCES %s(%s) ON DELETE CASCADE ) ",
                                                                                        PersonalAcademiaContract.TABLE_NAME,
                                                                                        PersonalAcademiaContract.columns.ID,
                                                                                        PersonalAcademiaContract.columns.CPF_PERSONAL,
                                                                                        PersonalAcademiaContract.columns.CNPJ_ACADEMIA,

                                                                                        PersonalAcademiaContract.columns.CPF_PERSONAL,
                                                                                        PersonalContract.TABLE_NAME,
                                                                                        PersonalContract.columns.CPF,

                                                                                        PersonalAcademiaContract.columns.CNPJ_ACADEMIA,
                                                                                        AcademiaContract.TABLE_NAME,
                                                                                        AcademiaContract.columns.CNPJ);

    //Create TB TreinamentoProgressivo
    private static final String SQL_DROP_TREINO_PROGRESSIVO = "DROP TABLE IF EXISTS " + TreinoProgressivoContract.TABLE_NAME;
    private static final String SQL_CREATE_TREINO_PROGRESSIVO = String.format("CREATE TABLE %s ( %s INTEGER PRIMARY KEY NOT NULL, " +
                                                                                    " %s INTEGER NOT NULL, "+
                                                                                    " %s INTEGER NOT NULL , "+
                                                                                    " FOREIGN KEY (%s) REFERENCES %s(%s)  ON DELETE CASCADE ) ",
                                                                            TreinoProgressivoContract.TABLE_NAME,
                                                                            TreinoProgressivoContract.columns.ID,
                                                                            TreinoProgressivoContract.columns.ID_TREINO,
                                                                            TreinoProgressivoContract.columns.MES,

                                                                            TreinoProgressivoContract.columns.ID_TREINO,
                                                                            TreinoContract.TABLE_NAME,
                                                                            TreinoContract.columns.ID);

    //Create TB SemanaTreinoProgressivo
    private static final String SQL_DROP_SEMANA_TREINO_PROGRESSIVO = "DROP TABLE IF EXISTS " + SemanaTreinoProgressivoContract.TABLE_NAME;
    private static final String SQL_CREATE_SEMANA_TREINO_PROGRESSIVO = String.format("CREATE TABLE %s ( %s INTEGER PRIMARY KEY NOT NULL, " +
                                                                                    " %s INTEGER NOT NULL, "+
                                                                                    " %s TEXT NOT NULL , "+
                                                                                    " %s TEXT NOT NULL , "+
                                                                                    " %s INTEGER NOT NULL , "+
                                                                                    " FOREIGN KEY (%s) REFERENCES %s(%s)  ON DELETE CASCADE ) ",
                                                                            SemanaTreinoProgressivoContract.TABLE_NAME,
                                                                            SemanaTreinoProgressivoContract.columns.ID,
                                                                            SemanaTreinoProgressivoContract.columns.ID_TREINO_PROGRESSIVO,
                                                                            SemanaTreinoProgressivoContract.columns.REPETICOES,
                                                                            SemanaTreinoProgressivoContract.columns.PORCENTAGEM_CARGAS,
                                                                            SemanaTreinoProgressivoContract.columns.SEMANA,

                                                                            SemanaTreinoProgressivoContract.columns.ID_TREINO_PROGRESSIVO,
                                                                            TreinoProgressivoContract.TABLE_NAME,
                                                                            TreinoProgressivoContract.columns.ID);


    //Create TB ProgramaTreino
    private static final String SQL_DROP_PROGRAMA_TREINO = "DROP TABLE IF EXISTS " + ProgramaTreinoContract.TABLE_NAME;
    private static final String SQL_CREATE_PROGRAMA_TREINO = String.format("CREATE TABLE %s ( %s INTEGER PRIMARY KEY NOT NULL, " +
                                                                                            " %s TEXT NOT NULL, "+
                                                                                            " %s INTEGER NOT NULL , "+
                                                                                            " %s TEXT NOT NULL , "+
                                                                                            " %s INTEGER NOT NULL , "+
                                                                                            " %s INTEGER NOT NULL , "+
                                                                                            " %s INTEGER NOT NULL , "+
                                                                                            " FOREIGN KEY (%s) REFERENCES %s(%s)  ON DELETE CASCADE ) ",
                                                                                    ProgramaTreinoContract.TABLE_NAME,
                                                                                    ProgramaTreinoContract.columns.ID,
                                                                                    ProgramaTreinoContract.columns.CPF_ALUNO,
                                                                                    ProgramaTreinoContract.columns.ORDEM,
                                                                                    ProgramaTreinoContract.columns.NOME_PROGRAMA,
                                                                                    ProgramaTreinoContract.columns.MISTO,
                                                                                    ProgramaTreinoContract.columns.SIMPLES,
                                                                                    ProgramaTreinoContract.columns.TREINOU,

                                                                                    ProgramaTreinoContract.columns.CPF_ALUNO,
                                                                                    AlunoContract.TABLE_NAME,
                                                                                    AlunoContract.columns.CPF);


    //Create TB ProgramaMusculo
    private static final String SQL_DROP_PROGRAMA_MUSCULO = "DROP TABLE IF EXISTS " + ProgramaMusculoContract.TABLE_NAME;
    private static final String SQL_CREATE_PROGRAMA_MUSCULO = String.format("CREATE TABLE %s ( %s INTEGER PRIMARY KEY NOT NULL, " +
                                                                                                " %s INTEGER NOT NULL, "+
                                                                                                " %s INTEGER NOT NULL , "+
                                                                                                " %s INTEGER NOT NULL , "+
                                                                                                " %s INTEGER NOT NULL , "+
                                                                                                " FOREIGN KEY (%s) REFERENCES %s(%s) ON DELETE CASCADE , "+
                                                                                                " FOREIGN KEY (%s) REFERENCES %s(%s)  ON DELETE CASCADE ) ",
                                                                                        ProgramaMusculoContract.TABLE_NAME,
                                                                                        ProgramaMusculoContract.columns.ID,
                                                                                        ProgramaMusculoContract.columns.ID_PROGRAMA,
                                                                                        ProgramaMusculoContract.columns.ID_MUSCULO,
                                                                                        ProgramaMusculoContract.columns.TIPO_MUSCULO,
                                                                                        ProgramaMusculoContract.columns.ORDEM_MUSCULO,

                                                                                        ProgramaMusculoContract.columns.ID_PROGRAMA,
                                                                                        ProgramaTreinoContract.TABLE_NAME,
                                                                                        ProgramaTreinoContract.columns.ID,

                                                                                        ProgramaMusculoContract.columns.ID_MUSCULO,
                                                                                        MusculoContract.TABLE_NAME,
                                                                                        MusculoContract.columns.ID);

    //Create TB Frequencia
    private static final String SQL_DROP_FREQUENCIA = "DROP TABLE IF EXISTS " + FrequenciaContract.TABLE_NAME;
    private static final String SQL_CREATE_FREQUENCIA = String.format("CREATE TABLE %s ( %s INTEGER PRIMARY KEY NOT NULL, " +
                                                                                        " %s TEXT NOT NULL, "+
                                                                                        " %s TEXT NOT NULL , "+
                                                                                        " %s TEXT, "+
                                                                                        " %s TEXT, "+
                                                                                        " FOREIGN KEY (%s) REFERENCES %s(%s) ON DELETE CASCADE ) ",
                                                                                        FrequenciaContract.TABLE_NAME,
                                                                                        FrequenciaContract.columns.ID,
                                                                                        FrequenciaContract.columns.CPF_ALUNO,
                                                                                        FrequenciaContract.columns.DATA,
                                                                                        FrequenciaContract.columns.MUSCULOS_TREINADOS,
                                                                                        FrequenciaContract.columns.ORDEM_PROGRAMA_TREINO,

                                                                                        FrequenciaContract.columns.CPF_ALUNO,
                                                                                        AlunoContract.TABLE_NAME,
                                                                                        AlunoContract.columns.CPF);



    private static DBHelper instance;

    public static DBHelper getInstance(Context context){
        if (instance == null){
            instance = new DBHelper(context);
        }
        return instance;
    }

    private  DBHelper(Context context){
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            //DROPS
            db.execSQL(SQL_DROP_MUSCULO_TREINADO_SEMANA);
            db.execSQL(SQL_DROP_ALTERACAO);
            db.execSQL(SQL_DROP_TABELA);
            db.execSQL(SQL_DROP_PERSONAL_ACADEMIA);
            db.execSQL(SQL_DROP_ACADEMIA);
            db.execSQL(SQL_DROP_SEMANA_TREINO_PROGRESSIVO);
            db.execSQL(SQL_DROP_TREINO_PROGRESSIVO);
            db.execSQL(SQL_DROP_MUSCULO);
            db.execSQL(SQL_DROP_TREINO_BASE);
            db.execSQL(SQL_DROP_TREINO);
            db.execSQL(SQL_DROP_PERSONAL);
            db.execSQL(SQL_DROP_PROGRAMA_MUSCULO);
            db.execSQL(SQL_DROP_PROGRAMA_TREINO);
            db.execSQL(SQL_DROP_ALUNO);
            db.execSQL(SQL_DROP_FREQUENCIA);

            //CREATES

            db.execSQL(SQL_CREATE_ALUNO);
            db.execSQL(SQL_CREATE_MUSCULO_TREINADO_SEMANA);
            db.execSQL(SQL_CREATE_ACADEMIA);
            db.execSQL(SQL_CREATE_TABELA);
            db.execSQL(SQL_CREATE_PERSONAL);
            db.execSQL(SQL_CREATE_PERSONAL_ACADEMIA);
            db.execSQL(SQL_CREATE_TREINO_BASE);
            db.execSQL(SQL_CREATE_ALTERACAO);
            db.execSQL(SQL_CREATE_TREINO);
            db.execSQL(SQL_CREATE_TREINO_PROGRESSIVO);
            db.execSQL(SQL_CREATE_SEMANA_TREINO_PROGRESSIVO);
            db.execSQL(SQL_CREATE_MUSCULO);
            db.execSQL(SQL_CREATE_PROGRAMA_TREINO);
            db.execSQL(SQL_CREATE_PROGRAMA_MUSCULO);
            db.execSQL(SQL_CREATE_FREQUENCIA);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onCreate(db);
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        db.setForeignKeyConstraintsEnabled(true);
    }
}
