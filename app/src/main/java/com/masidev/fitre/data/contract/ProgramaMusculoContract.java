package com.masidev.fitre.data.contract;

/**
 * Created by jmasiero on 05/04/16.
 */
public final class ProgramaMusculoContract {
    public static final String TABLE_NAME = "ProgramaMusculo";

    public static final class columns {
        public static final String ID = "Id";
        public static final String ID_PROGRAMA = "IdPrograma";
        public static final String ID_MUSCULO = "IdMusculo";
        public static final String TIPO_MUSCULO = "TipoMusculo";
        public static final String ORDEM_MUSCULO = "OrdemMusculo";
    }
}
