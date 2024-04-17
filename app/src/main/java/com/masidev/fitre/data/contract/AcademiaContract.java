package com.masidev.fitre.data.contract;

/**
 * Created by jmasiero on 05/04/16.
 */
public final class AcademiaContract {
    public static final String TABLE_NAME = "Academia";

    public static final class columns {
        public static final String CNPJ = "Cnpj";
        public static final String NOME_FANTASIA = "NomeFantasia";
        public static final String TELEFONE = "Telefone";
        public static final String ULTIMO_ID_INSERT = "UltimoIdInsert";
        public static final String ULTIMO_ID_DELETE = "UltimoIdDelete";
        public static final String ULTIMO_ID_UPDATE = "UltimoIdUpdate";
        public static final String PAGOU = "Pagou";
        public static final String DT_ULTIMO_PAGAMENTO = "DtUltimoPagamento";
    }
}
