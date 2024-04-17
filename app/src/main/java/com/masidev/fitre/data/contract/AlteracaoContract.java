package com.masidev.fitre.data.contract;

/**
 * Created by jmasiero on 05/04/16.
 */
public final class AlteracaoContract {
    public static final String TABLE_NAME = "Alteracao";

    public static final class columns {
        public static final String ID = "Id";
        public static final String ID_TABELA = "IdTabela";
        public static final String ACAO = "Acao";
        public static final String CHAVE = "Chave";
        public static final String ATIVO = "Ativo";
        public static final String CPF_PERSONAL = "CpfPersonal";
        public static final String DATA = "Data";
        public static final String CNPJ_ACADEMIA = "CnpjAcademia";
    }
}
