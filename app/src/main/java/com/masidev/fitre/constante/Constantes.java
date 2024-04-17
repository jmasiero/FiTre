package com.masidev.fitre.constante;

/**
 * Created by jmasiero on 06/10/15.
 */
public final class Constantes {
    //Versão
    public static final String VERSAO = "1.1.19";

    //Telas
    public static final int FRAGMENT_EM_TREINAMENTO = 0;
    public static final int FRAGMENT_TREINOS = 1;
    public static final int FRAGMENT_ALUNOS = 2;
    public static final int FRAGMENT_PERSONAIS = 3;
    public static final int FRAGMENT_TREINO_BASE = 4;
    public static final int FRAGMENT_TREINO_PROGRESSIVO = 5;
    public static final int FRAGMENT_PROGRAMA_DE_TREINO = 6;
    public static final int FRAGMENT_NOVO_TREINO = 11;
    public static final int FRAGMENT_NOVO_ALUNO = 13;
    public static final int FRAGMENT_NOVO_PERSONAL = 14;
    public static final int FRAGMENT_TRANSACAO_NOVO_TREINO_PT1_PT2 = 17;

    // Transações extras
    public static final int FRAGMENT_NOVO_MUSCULO = 8;
    public static final int FRAGMENT_VISUALIZAR_TREINO = 9;
    public static final int FRAGMENT_ALTERAR_ALUNO = 10;
    public static final int FRAGMENT_NOVO_TREINO_PROGRESSIVO = 11;
    public static final int FRAGMENT_VISUALIZAR_TREINO_PROGRESSIVO = 12;

    //Transações Dialogs
    public static final int FRAGMENT_DIALOG_SELECIONA_ALUNO_PARA_TESTE_PT1 = 20;
    public static final int FRAGMENT_DIALOG_SELECIONA_ALUNO_PARA_TESTE_PT2 = 21;
    public static final int FRAGMENT_DIALOG_SELECIONA_ALUNO_PARA_TESTE_PT3 = 22;
    public static final int FRAGMENT_DIALOG_SELECIONA_EXERCICIO_PARA_TESTE_PT4 = 23;

    //TAGs para transações entre fragmentos
    public static final String TAG_FRAGMENT_ALUNOS_EM_TREINAMENTO = "AlunosEmTreinamento";
    public static final String TAG_FRAGMENT_BUSCA_ALUNO_INICIA_TREINO = "BuscaAlunoIniciaTreino";
    public static final String TAG_FRAGMENT_MENU_EXERCICIO = "MenuExercicio";
    public static final String TAG_POPUP_ALTERAR_MUSCULO = "PoPupAlterarMusculo";
    public static final String TAG_HOME = "Home";

    //TAGs Dialogs Fragment
    public static final String IMPORTAR_ALUNO = "ImportarAluno";
    public static final String CONFIGURACOES = "Configuracoes";
    public static final String ALTERAR_MUSCULO_TREINO = "AlterarMusculoTreino";
    public static final String TROCAR_PERSONAL = "TrocarPersonal";

    //Arrays Constantes de treino
    public static final String[] ARRAY_SERIES = new String[]{"Series",
                                                                "1",
                                                                "2",
                                                                "3",
                                                                "4",
                                                                "5",
                                                                "6"};
    //Arrays Constantes Dias da Semana
    public static final int[] ARRAY_DIAS_DA_SEMANA = new int[]{1,2,3,4,5,6,7};


    public static final int ITEM_SERIE_VAZIA = 0;

    public static final String[] ARRAY_INTERVALOS = new String[]{"Intervalos",
                                                                    "30s",
                                                                    "1min",
                                                                    "1:30min",
                                                                    "2min",
                                                                    "2:30min",
                                                                    "3min"};
    public static final int ITEM_INTERVALO_VAZIO = 0;

    public static final String[] ARRAY_VEL_EXERCICIOS = new String[]{"Vel Exercícios",
                                                                        "1/1",
                                                                        "1/2",
                                                                        "2/2"};

    public static final int ITEM_VEL_EXERCICIO_VAZIO = 0;

    public static final String[] ARRAY_TIPO_MUSCULOS = new String[]{"BICEPS",
                                                                    "TRICEPS",
                                                                    "PEITO",
                                                                    "OMBRO",
                                                                    "COSTAS",
                                                                    "PERNA",
                                                                    "GLUTEOS",
                                                                    "ANTEBRACO",
                                                                    "ABDOMINAIS"};
    public static final int BICEPS = 0;
    public static final int TRICEPS = 1;
    public static final int PEITO = 2;
    public static final int OMBRO = 3;
    public static final int COSTAS = 4;
    public static final int PERNA = 5;
    public static final int GLUTEOS = 6;
    public static final int ANTEBRACO = 7;
    public static final int ABDOMINAIS = 8;

    public static final String[] ARRAY_SEXO = new String[]{"Selecione...", "Masculino", "Feminino"};
    public static final String SEXO_MASCULINO = "M";
    public static final String SEXO_FEMININO = "F";

    public static final int BANCO_TRUE = 1;
    public static final int BANCO_FALSE = 0;

    public static final int TREINO_SIMPLES = 0;
    public static final int TREINO_MISTO = 1;

    //Login SharedPreferences
    public static final String NOME_ACADEMIA_LOGADA = "nome_academia_logada";
    public static final String CNPJ_ACADEMIA_LOGADA = "cnpj_academia_logada";
    public static final String AVISOU_SOBRE_PAGAMENTO_DIRECIONOU_PARA_LOGIN = "aviso_pagamento";

    public static final String NOME_TREINADOR_LOGADO = "nome_treinador_logado";
    public static final String CPF_TREINADOR_LOGADO = "cpf_treinador_logado";
    public static final String SHARED_PREFERENCES_LOGIN = "LOGIN";

    public static final String SHARED_PREFERENCES_CONFIGURACOES = "CONFIG";
    public static final String SHARED_PREFERENCES_VISUALIZACAO_TREINAMENTO = "VISUALIZACAO_TREINAMENTO";

    public static final String VISUALIZACAO_TREINAMENTO_JANELA = "JANELA";
    public static final String VISUALIZACAO_TREINAMENTO_LISTA = "LISTA";

    //STATUS TREINO SEMANAL
    public static final String SHARED_PREFERENCES_STATUS_TREINO_SEMANAL = "STATUS_TREINO_SEMANAL";
    public static final String SHARED_PREFERENCES_PRIMEIRO_DIA_DA_SEMANA_ACESSADO = "PRIMEIRO_DIA_SEMANA";
    public static final String SHARED_PREFERENCES_TREINO_SEMANAL_ZERADO = "STATUS_TREINO_SEMANAL_APAGADO";
    //STATUS DO CRUD
    public static final String STATUS_SALVAR = "SALVAR";
    public static final String STATUS_ALTERAR = "ALTERAR";

    //Key
    public static final String BUNDLE_MUSCULO = "BUNDLE_MUSCULO";
    public static final String BUNDLE_ALUNO = "BUNDLE_ALUNO";
    public static final String BUNDLE_PROGRAMA_TREINO = "BUNDLE_PROGRAMA_TREINO";

    //Campo selecionado do musculo a ser alterado
    public static final String CARGAS = "CARGAS";
    public static final String REPETICOES = "REPETIÇÕES";

    //AÇÕES
    public static final String CAMPO_CLICADO = "CAMPO_CLICADO";

    //Ações tabela alteração
    public static final String INSERT = "I";
    public static final String UPDATE = "U";
    public static final String DELETE = "D";

    //Constantes para as funções de alteração de carga e repetição do exercicio em tempo de treinamento
    public static final Boolean INCREMENTA = Boolean.TRUE;
    public static final Boolean DECREMENTA = Boolean.FALSE;
    public static final Integer CAMPO_REPETICAO = 1;
    public static final Integer CAMPO_CARGAS = 2;

    //Endereços de Requisições
    //importa aluno
    public static final String ALUNO_CONTROLLER = "http://fitre.com.br/public/aluno";
    //tras informações das academias
    public static final String ACADEMIA_CONTROLLER = "http://fitre.com.br/public/academia";
    //faz upload dos dados
    public static final String NUVEM_CONTROLLER = "http://fitre.com.br/public/nuvem";
    //tras informações dos personais
    public static final String PERSONAL_CONTROLLER = "http://fitre.com.br/public/personal";

    //Endereços de Requisições
//    //importa aluno
//    public static final String ALUNO_CONTROLLER = "http://192.168.0.104/fitre/public/aluno";
//    //tras informações das academias
//    public static final String ACADEMIA_CONTROLLER = "http://192.168.0.104/fitre/public/academia";
//    //faz upload dos dados
//    public static final String NUVEM_CONTROLLER = "http://192.168.0.104/fitre/public/nuvem";
//    //tras informações dos personais
//    public static final String PERSONAL_CONTROLLER = "http://192.168.0.104/fitre/public/personal";

    //Meses
    public static final String[] ARRAY_MESES = new String[]{"Janeiro",
                                                            "Fevereiro",
                                                            "Março",
                                                            "Abril",
                                                            "Maio",
                                                            "Junho",
                                                            "Julho",
                                                            "Agosto",
                                                            "Setembro",
                                                            "Outubro",
                                                            "Novembro",
                                                            "Dezembro"};

    //Duração
    public static final String[] ARRAY_DURACAO_MESES = new String[]{"1 mês",
                                                                    "2 meses",
                                                                    "3 meses",
                                                                    "4 meses",
                                                                    "5 meses",
                                                                    "6 meses"};

    //Duração com titulo
    public static final String[] ARRAY_DURACAO_MESES_COM_TITULO = new String[]{    "Duração",
                                                                        "1 mês",
                                                                        "2 meses",
                                                                        "3 meses",
                                                                        "4 meses",
                                                                        "5 meses",
                                                                        "6 meses"};

    //RMS1
    public static final String[] ARRAY_RMS1 = new String[]{ "RMS1",
                                                            "40%","50%",
                                                            "55%","60%",
                                                            "65%","70%",
                                                            "75%","80%",
                                                            "85%","90%",
                                                            "95%","100%"};

    //RMS2
    public static final String[] ARRAY_RMS2 = new String[]{ "RMS2",
                                                            "40%","50%",
                                                            "55%","60%",
                                                            "65%","70%",
                                                            "75%","80%",
                                                            "85%","90%",
                                                            "95%","100%"};

    //RMS3
    public static final String[] ARRAY_RMS3 = new String[]{ "RMS3",
                                                            "40%","50%",
                                                            "55%","60%",
                                                            "65%","70%",
                                                            "75%","80%",
                                                            "85%","90%",
                                                            "95%","100%"};
    //Tentativas para o teste RMS
    public static final String[] ARRAY_TENTATIVAS = new String[]{ "Tentativas","1","2","3"};

    //Porcentagem Cargas Treino Progressivo
    public static final String[] ARRAY_PORCENTAGEM__TREINO_PROGRESSIVO = new String[]{
                                                                                    "-","40%",
                                                                                    "45%","50%",
                                                                                    "55%","60%",
                                                                                    "65%","70%",
                                                                                    "75%","80%",
                                                                                    "85%","90%",
                                                                                    "95%","100%"};

//    public static final class columns {
//        public static final String CPF = "Cpf";
//        public static final String NOME = "Nome";
//        public static final String DTNASIMENTO = "DtNascimento";
//        public static final String OBSERVACAO = "Observacao";
//        //public static final String FOTO = "Foto";
//    }
}
