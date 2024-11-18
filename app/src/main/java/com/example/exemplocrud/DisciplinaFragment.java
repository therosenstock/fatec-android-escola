package com.example.exemplocrud;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.exemplocrud.controller.DisciplinaController;
import com.example.exemplocrud.controller.ProfessorController;
import com.example.exemplocrud.model.Disciplina;
import com.example.exemplocrud.model.Professor;
import com.example.exemplocrud.persistence.DisciplinaDao;
import com.example.exemplocrud.persistence.ProfessorDao;

import java.sql.SQLException;
import java.util.List;


public class DisciplinaFragment extends Fragment {

    View view;
    EditText etCodigoDisc, etNomeDisc;
    Spinner spProfDisc;
    Button btnInserirDisc, btnModificarDisc, btnExcluirDisc, btnBuscarDisc, btnListarDisc;
    TextView tvListarDisc;
    private DisciplinaController dCont;
    private ProfessorController pCont;
    private List<Professor> professores;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view =  inflater.inflate(R.layout.fragment_disciplina, container, false);
        
        etCodigoDisc = view.findViewById(R.id.etCodigoDisc);
        etNomeDisc = view.findViewById(R.id.etNomeDisc);
        spProfDisc = view.findViewById(R.id.spProfDisc);

        btnInserirDisc = view.findViewById(R.id.btnInserirDisc);
        btnModificarDisc = view.findViewById(R.id.btnModificarDisc);
        btnExcluirDisc = view.findViewById(R.id.btnExcluirDisc);
        btnBuscarDisc = view.findViewById(R.id.btnBuscarDisc);
        btnListarDisc = view.findViewById(R.id.btnListarDisc);

        tvListarDisc = view.findViewById(R.id.tvListarDisc);
        tvListarDisc.setMovementMethod(new ScrollingMovementMethod());
        
        dCont = new DisciplinaController((new DisciplinaDao(view.getContext())));
        pCont = new ProfessorController((new ProfessorDao(view.getContext())));

        btnInserirDisc.setOnClickListener(op -> acaoInserir());
        btnModificarDisc.setOnClickListener(op -> acaoModificar());
        btnExcluirDisc.setOnClickListener(op -> acaoExcluir());
        btnBuscarDisc.setOnClickListener(op -> acaoBuscar());
        btnListarDisc.setOnClickListener(op -> acaoListar());
        
        preencheSpinner();
        
        return view;
    }

    private void acaoInserir() {
        int posicao = spProfDisc.getSelectedItemPosition();
        if(posicao > 0){
            Disciplina disciplina  = montaDisciplina();
            try {
                dCont.inserir(disciplina);
                Toast.makeText(view.getContext(), "Disciplina inserida com sucesso!", Toast.LENGTH_LONG).show();
            } catch (SQLException e) {
                Toast.makeText(view.getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            }
            limpaCampos();
        }else {
            Toast.makeText(view.getContext(), "Selecione um professor!", Toast.LENGTH_LONG).show();
        }
    }

    private void acaoModificar() {
        int posicao = spProfDisc.getSelectedItemPosition();
        if(posicao > 0){
            Disciplina disciplina  = montaDisciplina();
            try {
                dCont.modificar(disciplina);
                Toast.makeText(view.getContext(), "Disciplina modificada com sucesso!", Toast.LENGTH_LONG).show();
            } catch (SQLException e) {
                Toast.makeText(view.getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            }
            limpaCampos();
        }else {
            Toast.makeText(view.getContext(), "Selecione um professor!", Toast.LENGTH_LONG).show();
        }
        limpaCampos();
    }

    private void acaoExcluir() {
        Disciplina disciplina  = montaDisciplina();
        try {
            dCont.deletar(disciplina);
            Toast.makeText(view.getContext(), "Disciplina excluída com sucesso!", Toast.LENGTH_LONG).show();
        } catch (SQLException e) {
            Toast.makeText(view.getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
        limpaCampos();
    }

    private void acaoBuscar() {
        Disciplina disciplina  = montaDisciplina();
        try {
            professores = pCont.listar();
            disciplina = dCont.buscar(disciplina);
            if(disciplina.getNome() != null){
                preencheCampos(disciplina);
            } else{
                Toast.makeText(view.getContext(), "Disciplina não encontrada!", Toast.LENGTH_LONG).show();
                limpaCampos();
            }
        } catch (SQLException e) {
            Toast.makeText(view.getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void acaoListar() {
        try{
            List<Disciplina> disciplinas = dCont.listar();
            StringBuffer buffer = new StringBuffer();
            for(Disciplina d : disciplinas){
                buffer.append(d.toString() + "\n");
                tvListarDisc.setText(buffer.toString());
            }
        }catch(SQLException e){
            Toast.makeText(view.getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private Disciplina montaDisciplina(){
        Disciplina d = new Disciplina();
        d.setCodigo(Integer.parseInt(etCodigoDisc.getText().toString()));
        d.setNome(etNomeDisc.getText().toString());
        d.setProfessor((Professor) spProfDisc.getSelectedItem());
        return d;
    }

    private void limpaCampos(){
        etCodigoDisc.setText("");
        etNomeDisc.setText("");
        spProfDisc.setSelection(0);
    }

    private void preencheCampos(Disciplina d){
        etCodigoDisc.setText(String.valueOf(d.getCodigo()));
        etNomeDisc.setText(d.getNome());
        int cont = 1;
        for(Professor p: professores){
            if(p.getCodigo() == d.getProfessor().getCodigo()){
                spProfDisc.setSelection(cont);
            } else{
                cont++;
            }
            if(cont > professores.size()){
                spProfDisc.setSelection(0);
            }
        }

    }

    private void preencheSpinner() {
        Professor p0 = new Professor();
        p0.setCodigo(0);
        p0.setNome("Selecione um professor...");
        p0.setTitulacao("");

        try {
            professores = pCont.listar();
            professores.add(0, p0);

            ArrayAdapter ad = new ArrayAdapter(view.getContext(),
                    android.R.layout.simple_spinner_item,
                    professores);
            ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spProfDisc.setAdapter(ad);
        } catch (SQLException e) {
            Toast.makeText(view.getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}