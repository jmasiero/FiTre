package com.masidev.fitre.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.masidev.fitre.R;
import com.masidev.fitre.activity.Main;
import com.masidev.fitre.componetes.CPF;
import com.masidev.fitre.constante.Constantes;
import com.masidev.fitre.data.dao.PersonalDAO;
import com.masidev.fitre.data.entidade.Personal;
import com.masidev.fitre.mascara.Mask;

/**
 * Created by jmasiero on 08/03/16.
 */
public class FragmentNovoPersonal extends Fragment {
    private View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_novo_personal, container, false);
        this.view = view;
        iniciaListeners();
        return view;
    }

    private void iniciaListeners(){
        Button btnVoltar = (Button) view.findViewById(R.id.btnVoltar);
        btnVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction()
                        .replace(R.id.container, Main.PlaceholderFragment.newInstance(Constantes.FRAGMENT_PERSONAIS), String.valueOf(Constantes.FRAGMENT_PERSONAIS))
                        .addToBackStack(String.valueOf(Constantes.FRAGMENT_PERSONAIS))
                        .commit();
            }
        });

        Button btnPersonal = (Button) view.findViewById(R.id.btnSalvar);
        EditText edtCPFPersonal = (EditText) view.findViewById(R.id.edtCpf);
        edtCPFPersonal.addTextChangedListener(Mask.insert("###.###.###-##", edtCPFPersonal));
        btnPersonal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText edtNome = (EditText) view.findViewById(R.id.edtNome);
                EditText edtCPF = (EditText) view.findViewById(R.id.edtCpf);
                EditText edtCREF = (EditText) view.findViewById(R.id.edtCref);
                EditText edtSenha = (EditText) view.findViewById(R.id.edtSenha);
                EditText edtConfirmarSenha = (EditText) view.findViewById(R.id.edtConfirmarSenha);

                Personal p = new Personal(Mask.unmask(edtCPF.getText().toString()),
                        edtNome.getText().toString(),
                        edtCREF.getText().toString());
                p.setSenha(edtSenha.getText().toString());
                if(edtConfirmarSenha.getText().toString().equals(edtSenha.getText().toString())) {
                    if (!p.getCPF().isEmpty() &
                            p.getCPF().length() == 11 &
                            !p.getNome().isEmpty() &
                            !p.getCREF().isEmpty() &
                            !p.getSenha().isEmpty()) {
                        if (CPF.validaCPF(p.getCPF())) {
                            try {
                                if (PersonalDAO.getInstance(view.getContext()).salvar(p, true) >= 0) {
                                    FragmentManager fragmentManager = getFragmentManager();

                                    fragmentManager.beginTransaction()
                                            .replace(R.id.container, Main.PlaceholderFragment.newInstance(Constantes.FRAGMENT_PERSONAIS), String.valueOf(Constantes.FRAGMENT_PERSONAIS))
                                                    //.addToBackStack(null)
                                            .commit();
                                }
                            } catch (Exception e) {
                                mostraToast(getResources().getString(R.string.cpf_repetido));
                                e.printStackTrace();
                            }
                        } else {
                            mostraToast(getResources().getString(R.string.CPF_invalido));
                        }
                    } else {
                        if (!p.getCPF().isEmpty() & p.getCPF().length() < 11) {
                            mostraToast(getResources().getString(R.string.CPF_invalido));
                        } else {
                            mostraToast(getResources().getString(R.string.Erro_Campo_Vazio));
                        }
                    }
                }else{
                    mostraToast(getResources().getString(R.string.erro_senha_confirmacao_senha));
                }
            }
        });
    }

    private void mostraToast(String mensagem){
        Toast t = Toast.makeText(getContext(), mensagem, Toast.LENGTH_LONG);
        t.setGravity(Gravity.CENTER, Gravity.CENTER_HORIZONTAL,Gravity.CENTER_VERTICAL);
        t.show();
    }

}
