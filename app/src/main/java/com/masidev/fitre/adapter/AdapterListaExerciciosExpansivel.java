package com.masidev.fitre.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.masidev.fitre.constante.Constantes;
import com.masidev.fitre.data.entidade.Aluno;
import com.masidev.fitre.data.entidade.Musculo;
import com.masidev.fitre.data.entidade.ProgramaTreino;

import java.util.ArrayList;

/**
 * Created by jmasiero on 15/06/16.
 */
public class AdapterListaExerciciosExpansivel extends BaseExpandableListAdapter {
    Context context;

    private Aluno aluno;
    private ProgramaTreino programaTreino;
    public ProgramaTreino getProgramaTreino() {
        return programaTreino;
    }
    private Boolean visualizar;

    public Boolean getVisualizar() {
        return visualizar;
    }

    public void setVisualizar(Boolean visualizar) {
        this.visualizar = visualizar;
    }


    public void setProgramaTreino(ProgramaTreino programaTreino) {
        this.programaTreino = programaTreino;
    }


    public Aluno getAluno() {
        return aluno;
    }

    public void setAluno(Aluno aluno) {
        this.aluno = aluno;
    }

    //String[] listaPai = { "Categoria 1", "Categoria 2", "Categoria 3" };
//    String[][] listafilho = { { "Subcategoria 1", "Subcategoria 1.2" },
//                             { "Subcategoria 2" }, { "Subcategoria 3" } };
    ArrayList<String> listaPai;
    ArrayList<ArrayList<Musculo>> listafilho;


    public AdapterListaExerciciosExpansivel(Context context, Aluno aluno, ProgramaTreino programaTreino, Boolean visualizar) {
        this.context = context;
        this.aluno = aluno;
        this.programaTreino = programaTreino;
        this.visualizar = visualizar;
        organizaLista(aluno);
    }

    private void organizaLista(Aluno aluno) {
        listaPai = new ArrayList<>();
        listafilho = new ArrayList<>();
        if(getVisualizar()){
            ArrayList<Musculo> lstMusculos = getProgramaTreino().getLstMusculos();

            listafilho.add(new ArrayList<Musculo>());
            listafilho.add(new ArrayList<Musculo>());
            listafilho.add(new ArrayList<Musculo>());
            listafilho.add(new ArrayList<Musculo>());
            listafilho.add(new ArrayList<Musculo>());
            listafilho.add(new ArrayList<Musculo>());
            listafilho.add(new ArrayList<Musculo>());
            listafilho.add(new ArrayList<Musculo>());
            listafilho.add(new ArrayList<Musculo>());

            for (Musculo m : lstMusculos){
                if(!listaPai.contains(Constantes.ARRAY_TIPO_MUSCULOS[m.getTipoMusculo()])){
                    listaPai.add(Constantes.ARRAY_TIPO_MUSCULOS[m.getTipoMusculo()]);
                    //listafilho.add(m.getTipoMusculo(), new ArrayList<Musculo>());
                }
            }

            for (Musculo m : lstMusculos){
                listafilho.get(m.getTipoMusculo()).add(m);
            }

            for (int i = listafilho.size() - 1; i >= 0; i--){
                if(listafilho.get(i).size() == 0){
                   listafilho.remove(i);
                }
            }
        }else {
            ArrayList<ArrayList<Musculo>> lstMusculos = aluno.getTreino().getLstTodosMusculos();
            for (int i = 0; i <= lstMusculos.size() - 1; i++) {
                if (lstMusculos.get(i).size() > 0) {
                    listaPai.add(Constantes.ARRAY_TIPO_MUSCULOS[i]);
                    listafilho.add(lstMusculos.get(i));
                }
            }
        }
    }

    @Override
    public int getGroupCount() {
        return listaPai.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return listafilho.get(groupPosition).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return listaPai.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return listafilho.get(groupPosition).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

// Criamos um TextView que conterá as informações da listaPai que
// criamos
            TextView textViewCategorias = new TextView(context);
            textViewCategorias.setText(listaPai.get(groupPosition));
// Definimos um alinhamento
            textViewCategorias.setPadding(30, 5, 0, 5);
// Definimos o tamanho do texto
            textViewCategorias.setTextSize(20);
// Definimos que o texto estará em negrito
        textViewCategorias.setTypeface(null, Typeface.BOLD);

            return textViewCategorias;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if(getVisualizar()) {
            TextView txtExercicio = new TextView(context);
            txtExercicio.setText(listafilho.get(groupPosition).get(childPosition).getExercicio());
            txtExercicio.setPadding(10, 5, 0, 5);

            return txtExercicio;
        }else {
            final CheckBox chkExercicio = new CheckBox(context);
            chkExercicio.setText(listafilho.get(groupPosition).get(childPosition).getExercicio());

            chkExercicio.setChecked(jaSelecionada(listafilho.get(groupPosition).get(childPosition)));

            chkExercicio.setPadding(10, 5, 0, 5);

            chkExercicio.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        getProgramaTreino().getLstMusculos().add(listafilho.get(groupPosition).get(childPosition));
                    } else {
                        ArrayList<Musculo> lstMusculos = getProgramaTreino().getLstMusculos();

                        for (int i = 0; i < lstMusculos.size(); i++) {
                            if (lstMusculos.get(i).getId() == listafilho.get(groupPosition).get(childPosition).getId()) {
                                lstMusculos.remove(i);
                                break;
                            }
                        }

                    }
                }
            });

            return chkExercicio;
        }
    }

    private boolean jaSelecionada(Musculo musculo) {
        boolean retorno = false;
        for(Musculo m : getProgramaTreino().getLstMusculos()){
            if(m.getId() == musculo.getId()){
                retorno = true;
            }
        }

        return retorno;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}
