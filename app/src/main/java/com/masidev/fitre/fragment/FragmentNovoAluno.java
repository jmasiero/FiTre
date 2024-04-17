package com.masidev.fitre.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.masidev.fitre.R;
import com.masidev.fitre.activity.Main;
import com.masidev.fitre.componetes.CPF;
import com.masidev.fitre.constante.Constantes;
import com.masidev.fitre.data.dao.AlunoDAO;
import com.masidev.fitre.data.entidade.Aluno;
import com.masidev.fitre.mascara.Mask;

/**
 * Created by jmasiero on 08/03/16.
 */
public class FragmentNovoAluno extends Fragment {
    private View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_novo_aluno, container, false);
        this.view = view;
        iniciaListeners();
        populaCampos();
        return view;
    }

    private void iniciaListeners(){
        Button btnVoltar = (Button) view.findViewById(R.id.btnVoltar);
        btnVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction()
                        .replace(R.id.container, Main.PlaceholderFragment.newInstance(Constantes.FRAGMENT_ALUNOS), String.valueOf(Constantes.FRAGMENT_ALUNOS))
                        .addToBackStack(String.valueOf(Constantes.FRAGMENT_ALUNOS))
                        .commit();
            }
        });

        Button btnSalvarAluno = (Button) view.findViewById(R.id.btnSalvar);
        EditText edtCPFAluno = (EditText) view.findViewById(R.id.edtCpf);
        edtCPFAluno.addTextChangedListener(Mask.insert("###.###.###-##", edtCPFAluno));
        Spinner spnSexo = (Spinner) view.findViewById(R.id.spnSexo);
        ArrayAdapter<String> adapterSexo = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, Constantes.ARRAY_SEXO);
        spnSexo.setAdapter(adapterSexo);

        btnSalvarAluno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText edtNome = (EditText) view.findViewById(R.id.edtNome);
                EditText edtCPF = (EditText) view.findViewById(R.id.edtCpf);
                EditText edtObservacao = (EditText) view.findViewById(R.id.edtObservacao);
                TextView hiddenStatus = (TextView) view.findViewById(R.id.hidden_status);
                DatePicker dtpkDtNascimento = (DatePicker) view.findViewById(R.id.dtpkDtNascimento);

                String dtNascimento = String.format("%s-%s-%s",
                        dtpkDtNascimento.getYear(),
                        dtpkDtNascimento.getMonth(),
                        dtpkDtNascimento.getDayOfMonth());

                Spinner spnSexo = (Spinner) view.findViewById(R.id.spnSexo);
                long sexo = spnSexo.getSelectedItemId();
                String strSexo = "";

                if (sexo == 1) {
                    strSexo = Constantes.SEXO_MASCULINO;
                } else if (sexo == 2) {
                    strSexo = Constantes.SEXO_FEMININO;
                }
                //validar data de nascimento
                Aluno a = new Aluno(
                        Mask.unmask(edtCPF.getText().toString()),
                        edtNome.getText().toString(),
                        dtNascimento,
                        edtObservacao.getText().toString(),
                        strSexo
                );
                if (!a.getCPF().isEmpty() &
                        a.getCPF().length() == 11 &
                        !a.getNome().isEmpty() &
                        !a.getSexo().isEmpty() &
                        !a.getDtNascimento().isEmpty()) {
                    if (CPF.validaCPF(a.getCPF())) {
                        try {
                            if (Constantes.STATUS_SALVAR.equals(hiddenStatus.getText().toString())) {
                                if (!verificaCpfRepetido(a) && AlunoDAO.getInstance(getContext()).salvar(a, true) >= 0) {
                                    FragmentManager fragmentManager = getFragmentManager();

                                    fragmentManager.beginTransaction()
                                            .replace(R.id.container, Main.PlaceholderFragment.newInstance(Constantes.FRAGMENT_ALUNOS), String.valueOf(Constantes.FRAGMENT_ALUNOS))
                                            .addToBackStack(String.valueOf(Constantes.FRAGMENT_ALUNOS))
                                            .commit();
                                }
                            } else if (Constantes.STATUS_ALTERAR.equals(hiddenStatus.getText().toString())) {
                                if (AlunoDAO.getInstance(getContext()).alterar(a, true) >= 0) {
                                    FragmentManager fragmentManager = getFragmentManager();

                                    fragmentManager.beginTransaction()
                                            .replace(R.id.container, Main.PlaceholderFragment.newInstance(Constantes.FRAGMENT_ALUNOS), String.valueOf(Constantes.FRAGMENT_ALUNOS))
                                            .addToBackStack(String.valueOf(Constantes.FRAGMENT_ALUNOS))
                                            .commit();
                                }
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        mostraToast(getResources().getString(R.string.CPF_invalido));
                    }
                } else {
                    if (!a.getCPF().isEmpty() & a.getCPF().length() < 11) {
                        mostraToast(getResources().getString(R.string.CPF_invalido));
                    } else {
                        mostraToast(getResources().getString(R.string.Erro_Campo_Vazio));
                    }
                }
            }
        });
    }

    private boolean verificaCpfRepetido(Aluno aluno) {
        boolean retorno = false;

        Aluno aRetono = AlunoDAO.getInstance(getContext()).getAluno(aluno.getCPF());
        if(aRetono != null){
            mostraToast(getContext().getString(R.string.erro_o_cpf_ja_foi_cadastrado_no_sistema));
            retorno = true;
        }

        return retorno;
    }

    private void populaCampos(){
        TextView hiddenStatus = (TextView) view.findViewById(R.id.hidden_status);

        if(getArguments() != null && getArguments().getSerializable("aluno") != null) {
            // mTitle = getString(R.string.title_novo_aluno);
            //res = R.layout.fragment_novo_aluno;

            Aluno a = (Aluno) getArguments().getSerializable("aluno");

            String[] dataNascimento = a.getDtNascimento().split("-");
            DatePicker dtpkDtNascimento = (DatePicker) view.findViewById(R.id.dtpkDtNascimento);
            dtpkDtNascimento.init(Integer.parseInt(dataNascimento[0]), Integer.parseInt(dataNascimento[1]), Integer.parseInt(dataNascimento[2]), null);

            TextView hiddenSalvar = (TextView) view.findViewById(R.id.hidden_status);
            hiddenSalvar.setText(Constantes.STATUS_ALTERAR);

            Button btnAluno = (Button) view.findViewById(R.id.btnSalvar);
            btnAluno.setText("Alterar");

            EditText edtNome = (EditText) view.findViewById(R.id.edtNome);
            edtNome.setText(a.getNome());

            EditText edtCPFAluno = (EditText) view.findViewById(R.id.edtCpf);
            edtCPFAluno.setText(a.getCPF());
            edtCPFAluno.setEnabled(false);

            EditText edtObservacao = (EditText) view.findViewById(R.id.edtObservacao);
            edtObservacao.setText(a.getObservacao());

            Spinner spnSexo = (Spinner) view.findViewById(R.id.spnSexo);
            ArrayAdapter<String> adapterSexo = new ArrayAdapter<String>(view.getContext(),
                    android.R.layout.simple_spinner_dropdown_item,
                    Constantes.ARRAY_SEXO);
            spnSexo.setAdapter(adapterSexo);
            if(a.getSexo().equals(Constantes.SEXO_MASCULINO)){
                spnSexo.setSelection(1);
            }else{
                spnSexo.setSelection(2);
            }

        }

        if (Constantes.STATUS_ALTERAR.equals(hiddenStatus.getText().toString())){
            TextView txtNome = (TextView) view.findViewById(R.id.txtNome);
            txtNome.setVisibility(View.VISIBLE);

            TextView txtCpf = (TextView) view.findViewById(R.id.txtCpf);
            txtCpf.setVisibility(View.VISIBLE);

            TextView txtObservacao = (TextView) view.findViewById(R.id.txtObservacao);
            txtObservacao.setVisibility(View.VISIBLE);
        }

    }

    private void mostraToast(String mensagem){
        Toast t = Toast.makeText(getContext(), mensagem, Toast.LENGTH_LONG);
        t.setGravity(Gravity.CENTER, Gravity.CENTER_HORIZONTAL,Gravity.CENTER_VERTICAL);
        t.show();
    }
}
