package com.masidev.fitre.activity;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.masidev.fitre.R;
import com.masidev.fitre.adapter.AdapterListaProgramaTreino;
import com.masidev.fitre.adapter.AdapterTreino;
import com.masidev.fitre.adapter.AdapterAluno;
import com.masidev.fitre.adapter.AdapterPersonal;
import com.masidev.fitre.adapter.AdapterTreinoBase;
import com.masidev.fitre.constante.Constantes;
import com.masidev.fitre.data.dao.AlunoDAO;
import com.masidev.fitre.data.dao.PersonalDAO;
import com.masidev.fitre.data.dao.ProgramaTreinoDAO;
import com.masidev.fitre.data.dao.TreinoBaseDAO;
import com.masidev.fitre.data.dao.TreinoDAO;
import com.masidev.fitre.data.entidade.Aluno;
import com.masidev.fitre.data.entidade.Personal;
import com.masidev.fitre.data.entidade.ProgramaTreino;
import com.masidev.fitre.data.entidade.Treino;
import com.masidev.fitre.data.entidade.TreinoBase;
import com.masidev.fitre.fragment.FragmentDialogSelecionaAlunoProgramaTreinamento;
import com.masidev.fitre.fragment.FragmentDialogSelecionaAlunoTreinoProgressivo;
import com.masidev.fitre.fragment.FragmentNovoAluno;
import com.masidev.fitre.fragment.FragmentNovoPersonal;
import com.masidev.fitre.fragment.FragmentNovoTreinoPt1;
import com.masidev.fitre.fragment.FragmentNovoTreinoPt2;
import com.masidev.fitre.fragment.FragmentAlunosEmTreinamento;
import com.masidev.fitre.listener.OnItemClickListenerListViewAluno;
import com.masidev.fitre.listener.OnItemClickListenerListViewPersonal;
import com.masidev.fitre.listener.OnItemClickListenerListViewProgramaTreino;
import com.masidev.fitre.listener.OnItemClickListenerListViewTreino;
import com.masidev.fitre.listener.OnItemClickListenerListViewTreinoBase;
import com.masidev.fitre.listener.OnItemClickListenerListViewTreinoProgressivo;
import com.masidev.fitre.listener.OnItemLongClickListenerListViewProgramaTreino;
import com.masidev.fitre.listener.OnItemLongClickListenerListViewTreino;
import com.masidev.fitre.listener.OnItemLongClickListenerListViewTreinoBase;
import com.masidev.fitre.listener.OnItemLongClickListenerListViewTreinoProgressivo;

import java.util.ArrayList;
import java.util.List;

public class Main extends AppCompatActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    //Caminho do fragment
    private static int res;
    private int idHome;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();

        //fragmentManager.getBackStackEntryAt()
        if(position == Constantes.FRAGMENT_EM_TREINAMENTO && fragmentManager.getBackStackEntryCount() < 1){
            fragmentManager.beginTransaction()
                    .replace(R.id.container, PlaceholderFragment.newInstance(position), Constantes.TAG_HOME)
                    .addToBackStack(Constantes.TAG_HOME)
                    .commit();
            setaTituloActionBar(position);
        }else if(position == Constantes.FRAGMENT_EM_TREINAMENTO){
            //while(fragmentManager.getBackStackEntryCount() != 1) {
                fragmentManager.popBackStackImmediate(Constantes.TAG_HOME, 0);
            //}
            setaTituloActionBar(position);
        }else{
            fragmentManager.popBackStack(Constantes.TAG_FRAGMENT_MENU_EXERCICIO, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            fragmentManager.beginTransaction()
                    .replace(R.id.container, PlaceholderFragment.newInstance(position), String.valueOf(position))
                    .addToBackStack(String.valueOf(position))
                    .commit();
        }
    }

    public void onSectionAttached(int number) {
        setaTituloActionBar(number);
    }

    public void setaTituloActionBar(int posicao){
        switch (posicao) {
            case Constantes.FRAGMENT_EM_TREINAMENTO:
                mTitle = getString(R.string.title_treino);
                res = R.layout.fragment_main;
                break;
            case Constantes.FRAGMENT_TREINOS:
                mTitle = getString(R.string.title_treinos);
                res = R.layout.fragment_lista_treino;
                break;
            case Constantes.FRAGMENT_ALUNOS:
                mTitle = getString(R.string.title_alunos);
                res = R.layout.fragment_lista_aluno;
                break;
            case Constantes.FRAGMENT_PERSONAIS:
                mTitle = getString(R.string.title_personais);
                res = R.layout.fragment_lista_personal;
                break;
            case Constantes.FRAGMENT_TRANSACAO_NOVO_TREINO_PT1_PT2:
                mTitle = getString(R.string.title_novo_treino);
                res = R.layout.fragment_novo_treino_pt2;
                break;
            case Constantes.FRAGMENT_TREINO_BASE:
                mTitle = getString(R.string.title_treino_base);
                res = R.layout.fragment_lista_treino_base;
                break;
            case Constantes.FRAGMENT_TREINO_PROGRESSIVO:
                mTitle = getString(R.string.title_treino_progressivo);
                res = R.layout.fragment_lista_treino_progressivo;
                break;
            case Constantes.FRAGMENT_PROGRAMA_DE_TREINO:
                mTitle = getString(R.string.title_programa_de_treinamento);
                res = R.layout.fragment_lista_programa_treinamento;
                break;

        }
        restoreActionBar();
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }

    @Override
    public void onBackPressed() {
        //getFragmentManager().popBackStack();
        //if(res == R.layout.fragment_lista_treino){
            moveTaskToBack(false);
        //}else{
            //super.onBackPressed();
        //}
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        private LayoutInflater inflaterBase;
        private ViewGroup containerBase;
        private  EditText edtNome;
        private ArrayList<Aluno> lstAlunos;
        private EditText edtTreinoBase;
        private List<TreinoBase> lstTreinoBase;
        private EditText edtNomePersonal;
        private List<Personal> lstPersonais;
        private EditText edtBuscaTreino;
        private EditText edtBuscaProgramaTreino;
        private List<Treino> lstTreinos;
        private List<ProgramaTreino> lstProgramaTreino;

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber, Bundle args) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setRetainInstance(true);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                                 Bundle savedInstanceState) {
            inflaterBase = inflater;
            containerBase = container;
            View rootView = inflaterBase.inflate(res, containerBase, false);
            criaListeners(rootView, containerBase.getContext());
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((Main) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }

        private void criaListeners(final View view, final Context context) {

            switch (res) {
                case R.layout.fragment_main:

                    final FragmentManager fragmentManager = getFragmentManager();
                    FragmentAlunosEmTreinamento fragmentTreinoAlunos = new FragmentAlunosEmTreinamento();

                    fragmentManager.beginTransaction()
                            .replace(R.id.container, fragmentTreinoAlunos, Constantes.TAG_FRAGMENT_ALUNOS_EM_TREINAMENTO)
                            .commit();

                    break;

                case R.layout.fragment_lista_treino:
                    try {
                        Button btnNovoTreino = (Button) view.findViewById(R.id.btnNovoTreino);
                        edtBuscaTreino = (EditText) view.findViewById(R.id.edtBuscaTreino);

                        btnNovoTreino.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                FragmentNovoTreinoPt1 fragmentNovoTreinoPt1 = new FragmentNovoTreinoPt1();

                                getFragmentManager().beginTransaction()
                                        .replace(R.id.container, fragmentNovoTreinoPt1, String.valueOf(Constantes.FRAGMENT_NOVO_TREINO))
                                        .addToBackStack(String.valueOf(Constantes.FRAGMENT_NOVO_TREINO))
                                        .commit();
                            }
                        });

                        lstTreinos = TreinoDAO.getInstance(context).listarSemMusculos();
                        preecheListaTreinos(view);

                        edtBuscaTreino.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                            }

                            @Override
                            public void onTextChanged(CharSequence s, int start, int before, int count) {
                                String filtro = edtBuscaTreino.getText().toString().trim();
                                if (filtro != "") {
                                    lstTreinos = TreinoDAO.getInstance(getContext()).listarSemMusculosFiltrada(filtro);
                                    preecheListaTreinos(view);
                                }
                            }

                            @Override
                            public void afterTextChanged(Editable s) {

                            }
                        });

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;

                case R.layout.fragment_lista_personal:
                            try {

                                Button btnNovoPersonal = (Button) view.findViewById(R.id.btnNovoPersonal);
                                edtNomePersonal = (EditText) view.findViewById(R.id.edtNome);
                                lstPersonais =  PersonalDAO.getInstance(getContext()).listar(false);
                                preecheListaPersonais(view);

                                edtNomePersonal.addTextChangedListener(new TextWatcher() {
                                    @Override
                                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                    }

                                    @Override
                                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                                        String filtro = edtNomePersonal.getText().toString().trim();
                                        if (filtro != "") {
                                            lstPersonais = PersonalDAO.getInstance(view.getContext()).listaFiltrada(filtro);
                                            preecheListaPersonais(view);
                                        }
                                    }

                                    @Override
                                    public void afterTextChanged(Editable s) {

                                    }
                                });

                                btnNovoPersonal.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        FragmentNovoPersonal fragNovoPersonal = new FragmentNovoPersonal();

                                        getFragmentManager().beginTransaction()
                                                .replace(R.id.container, fragNovoPersonal, String.valueOf(Constantes.FRAGMENT_NOVO_PERSONAL) )
                                                .addToBackStack(String.valueOf(Constantes.FRAGMENT_NOVO_PERSONAL))
                                                .commit();
                                    }
                                });

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                    break;
                case R.layout.fragment_lista_aluno:
                        try {
                            Button btnNovoAluno = (Button) view.findViewById(R.id.btnNovoAluno);
                            btnNovoAluno.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    FragmentNovoAluno fragNovoAluno = new FragmentNovoAluno();

                                    getFragmentManager().beginTransaction()
                                            .replace(R.id.container, fragNovoAluno, String.valueOf(Constantes.FRAGMENT_NOVO_ALUNO) )
                                            .addToBackStack(String.valueOf(Constantes.FRAGMENT_NOVO_ALUNO))
                                            .commit();
                                }
                            });

                            TextView txtQtdAlunos = (TextView) view.findViewById(R.id.txtQtdAlunos);
                            edtNome = (EditText) view.findViewById(R.id.edtNome);
                            lstAlunos = AlunoDAO.getInstance(context).listar(false);
                            txtQtdAlunos.setText(context.getResources().getString(R.string.qtd) + lstAlunos.size());
                            preencheListaAlunos(view);

                            edtNome.addTextChangedListener(new TextWatcher() {
                                @Override
                                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                }

                                @Override
                                public void onTextChanged(CharSequence s, int start, int before, int count) {
                                    String filtro = edtNome.getText().toString().trim();
                                    if (filtro != "") {
                                        lstAlunos = AlunoDAO.getInstance(view.getContext()).listaFiltrada(filtro, true, false, false);
                                        preencheListaAlunos(view);
                                    }
                                }

                                @Override
                                public void afterTextChanged(Editable s) {

                                }
                            });

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    break;
                case R.layout.fragment_lista_treino_base:
                    //Setar TreinoBase pq está estourando a tela
                    Button btnNovoTreinoBase = (Button) view.findViewById(R.id.btnNovoTreinoBase);
                    edtTreinoBase = (EditText) view.findViewById(R.id.edtBuscaTreino);

                    lstTreinoBase = TreinoBaseDAO.getInstance(getContext()).listarSemMusculos(false, "");
                    preencheListaTreinoBase(view);


                    edtTreinoBase.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            String filtro = edtTreinoBase.getText().toString().trim();
                            if (filtro != "") {
                                lstTreinoBase = TreinoBaseDAO.getInstance(view.getContext()).listarSemMusculosFiltrada(filtro);
                                preencheListaTreinoBase(view);
                            }
                        }

                        @Override
                        public void afterTextChanged(Editable s) {

                        }
                    });

                    btnNovoTreinoBase.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            FragmentNovoTreinoPt2 fragNovoTreino = new FragmentNovoTreinoPt2();

                            fragNovoTreino.setBase(true);
                            fragNovoTreino.setTreinoBase(new TreinoBase());

                            getFragmentManager().beginTransaction()
                                    .replace(R.id.container, fragNovoTreino, String.valueOf(Constantes.FRAGMENT_NOVO_TREINO) )
                                    .addToBackStack(String.valueOf(Constantes.FRAGMENT_NOVO_TREINO))
                                    .commit();
                        }
                    });

                    break;

                case R.layout.fragment_lista_treino_progressivo:

                    try {
                        Button btnNovoTreino = (Button) view.findViewById(R.id.btnNovoTreino);
                        edtBuscaTreino = (EditText) view.findViewById(R.id.edtBuscaTreino);

                        btnNovoTreino.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                FragmentDialogSelecionaAlunoTreinoProgressivo dialog = new FragmentDialogSelecionaAlunoTreinoProgressivo();

                                dialog.show(getFragmentManager(), "busca_aluno_treino");
                            }
                        });

                        lstTreinos = TreinoDAO.getInstance(context).listarTreinosProgressivosSemMusculos();
                        preecheListaTreinosProgressivos(view);

                        edtBuscaTreino.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                            }

                            @Override
                            public void onTextChanged(CharSequence s, int start, int before, int count) {
                                String filtro = edtBuscaTreino.getText().toString().trim();
                                if (filtro != "") {
                                    lstTreinos = TreinoDAO.getInstance(getContext()).listarTreinosProgressivosSemMusculosFiltrada(filtro);
                                    preecheListaTreinosProgressivos(view);
                                }
                            }

                            @Override
                            public void afterTextChanged(Editable s) {

                            }
                        });




                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;

                case R.layout.fragment_lista_programa_treinamento:

                    try {
                        Button btnNovoPrograma = (Button) view.findViewById(R.id.btnNovoPrograma);
                        edtBuscaProgramaTreino = (EditText) view.findViewById(R.id.edtBuscaTreino);

                        btnNovoPrograma.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                FragmentDialogSelecionaAlunoProgramaTreinamento dialog = new FragmentDialogSelecionaAlunoProgramaTreinamento();

                                dialog.show(getFragmentManager(), "busca_aluno_treino");
                            }
                        });

                        lstProgramaTreino = ProgramaTreinoDAO.getInstance(context).listarPorAluno("");
                        preecheListaProgramaDeTreinamento(view);

                        edtBuscaTreino.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                            }

                            @Override
                            public void onTextChanged(CharSequence s, int start, int before, int count) {
                                String filtro = edtBuscaTreino.getText().toString().trim();
                                if (filtro != "") {
                                    lstProgramaTreino = ProgramaTreinoDAO.getInstance(context).listarPorAluno(filtro);
                                    preecheListaProgramaDeTreinamento(view);
                                }
                            }

                            @Override
                            public void afterTextChanged(Editable s) {

                            }
                        });

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;

            }

        }

        private void preencheListaAlunos(View view){
            ListView listaAlunos = (ListView) view.findViewById(R.id.listaAlunos);
            TextView txtQtdAlunos = (TextView) view.findViewById(R.id.txtQtdAlunos);

            AdapterAluno adapter = new AdapterAluno(
                    view.getContext(),
                    R.layout.adapter_aluno_row_item,
                    lstAlunos);
            listaAlunos.setAdapter(adapter);
            listaAlunos.setOnItemClickListener(new OnItemClickListenerListViewAluno(adapter, getFragmentManager(), txtQtdAlunos));
        }

        private void preencheListaTreinoBase(View view){
            ListView listaTreinoBase = (ListView) view.findViewById(R.id.listaTreinoBase);

            AdapterTreinoBase adapter = new AdapterTreinoBase(
                    view.getContext(),
                    R.layout.adapter_treino_row_item,
                    lstTreinoBase);

            adapter.setFragmentManager(getFragmentManager());
            listaTreinoBase.setAdapter(adapter);
            listaTreinoBase.setOnItemClickListener(new OnItemClickListenerListViewTreinoBase(adapter, getFragmentManager()));
            listaTreinoBase.setOnItemLongClickListener(new OnItemLongClickListenerListViewTreinoBase(adapter));
        }

        private void preecheListaPersonais(View view){
            ListView listaTreinadores = (ListView) view.findViewById(R.id.listaTreinadores);

            AdapterPersonal adapter = new AdapterPersonal(
                    view.getContext(),
                    R.layout.adapter_personal_row_item,
                    lstPersonais);

            listaTreinadores.setAdapter(adapter);
            listaTreinadores.setOnItemClickListener(new OnItemClickListenerListViewPersonal());
        }

        private void preecheListaTreinos(View view){
            ListView listaTreinos = (ListView) view.findViewById(R.id.listaTreino);

            AdapterTreino adapter = new AdapterTreino(
                    view.getContext(),
                    R.layout.adapter_treino_row_item,
                    lstTreinos);

            adapter.setFragmentManager(getFragmentManager());
            listaTreinos.setAdapter(adapter);
            listaTreinos.setOnItemClickListener(new OnItemClickListenerListViewTreino(adapter, getFragmentManager()));
            listaTreinos.setOnItemLongClickListener(new OnItemLongClickListenerListViewTreino(adapter));
        }

        private void preecheListaTreinosProgressivos(View view){
            ListView listaTreinos = (ListView) view.findViewById(R.id.listaTreino);

            AdapterTreino adapter = new AdapterTreino(
                    view.getContext(),
                    R.layout.adapter_treino_row_item,
                    lstTreinos);

            adapter.setFragmentManager(getFragmentManager());
            listaTreinos.setAdapter(adapter);
            listaTreinos.setOnItemClickListener(new OnItemClickListenerListViewTreinoProgressivo(adapter, getFragmentManager()));
            listaTreinos.setOnItemLongClickListener(new OnItemLongClickListenerListViewTreinoProgressivo(adapter));
        }

        private void preecheListaProgramaDeTreinamento(View view){
            ListView listaTreinos = (ListView) view.findViewById(R.id.listaTreino);

            AdapterListaProgramaTreino adapter = new AdapterListaProgramaTreino(
                    view.getContext(),
                    R.layout.adapter_programa_treino_row_item,
                    lstProgramaTreino);

            adapter.setFragmentManager(getFragmentManager());
            listaTreinos.setAdapter(adapter);
            listaTreinos.setOnItemClickListener(new OnItemClickListenerListViewProgramaTreino(adapter, getFragmentManager()));
            listaTreinos.setOnItemLongClickListener(new OnItemLongClickListenerListViewProgramaTreino(adapter));
        }

    }

}
