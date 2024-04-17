package com.masidev.fitre.data.contract;

/**
 * Created by jmasiero on 30/09/15.
 */
public final class TreinoContract {
    public static final String TABLE_NAME = "Treino";

    public static final class columns {
        public static final String ID = "Id";
        public static final String TIPO_TREINO = "TipoTreino";
        public static final String PESO_INICIO = "PesoInicio";
        public static final String PESO_IDEAL = "PesoIdeal";
        public static final String DATA_INICIO = "DataInicio";
        public static final String DATA_FIM = "DataFim";
        public static final String OBJETIVO = "Objetivo";
        public static final String ANOTACOES = "Anotacoes";
        public static final String CPF_ALUNO = "CpfAluno";
        public static final String CPF_PERSONAL = "CpfPersonal";
        public static final String CPF_PERSONAL_CRIADOR = "CpfPersonalCriador";
        public static final String ATUAL = "Atual";
        public static final String PROGRESSIVO = "Progressivo";

    }
}
