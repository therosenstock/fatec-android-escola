package com.example.exemplocrud;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.exemplocrud.controller.ProfessorController;
import com.example.exemplocrud.model.Professor;
import com.example.exemplocrud.persistence.ProfessorDao;

import java.sql.SQLException;
import java.util.List;

public class ProfessorFragment extends Fragment {

   View view;
   private EditText etCodigoProf, etNomeProf, etTitulacaoProf;
   private Button btnInserirProf, btnModificarProf, btnExcluirProf, btnListarProf, btnBuscarProf;
   private TextView tvListarProf;
   private ProfessorController pCont;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_professor, container, false);

        etCodigoProf = view.findViewById(R.id.etCodigoProf);
        etNomeProf = view.findViewById(R.id.etNomeProf);
        etTitulacaoProf = view.findViewById(R.id.etTitulacaoProf);

        btnInserirProf = view.findViewById(R.id.btnInserirProf);
        btnModificarProf = view.findViewById(R.id.btnModificarProf);
        btnExcluirProf = view.findViewById(R.id.btnExcluirProf);
        btnListarProf = view.findViewById(R.id.btnListarProf);
        btnBuscarProf = view.findViewById(R.id.btnBuscarProf);

        tvListarProf = view.findViewById(R.id.tvListarProf);
        tvListarProf.setMovementMethod(new ScrollingMovementMethod());

        pCont = new ProfessorController((new ProfessorDao(view.getContext())));
        btnInserirProf.setOnClickListener(op -> acaoInserir());
        btnModificarProf.setOnClickListener(op -> acaoModificar());
        btnExcluirProf.setOnClickListener(op -> acaoExcluir());
        btnBuscarProf.setOnClickListener(op -> acaoBuscar());
        btnListarProf.setOnClickListener(op -> acaoListar());

        return view;
    }

    private void acaoListar() {
        try{
            List<Professor> professores = pCont.listar();
            StringBuffer buffer = new StringBuffer();
            for(Professor p : professores){
                buffer.append(p.toString() + "\n");
                tvListarProf.setText(buffer.toString());
            }

        }catch(SQLException e){
            Toast.makeText(view.getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void acaoBuscar() {
        Professor p = montaProfessor();
        try {
            p = pCont.buscar(p);
            if(p.getNome() != null){
                preencherCampos(p);
            } else{
                Toast.makeText(view.getContext(), "Professor não encontrado!", Toast.LENGTH_LONG).show();
                limparCampos();
            }

        } catch (SQLException e) {
            Toast.makeText(view.getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void acaoExcluir() {
        Professor p = montaProfessor();
        try {
            pCont.deletar(p);
            Toast.makeText(view.getContext(), "Professor deletado com sucesso!", Toast.LENGTH_LONG).show();
        } catch (SQLException e) {
            Toast.makeText(view.getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
        limparCampos();
    }

    private void acaoModificar() {
        Professor p = montaProfessor();
        try {
            pCont.modificar(p);
            Toast.makeText(view.getContext(), "Professor modificado com sucesso!", Toast.LENGTH_LONG).show();
        } catch (SQLException e) {
            Toast.makeText(view.getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
        limparCampos();
    }

    private void acaoInserir() {
        Professor p = montaProfessor();
        try {
            pCont.inserir(p);
            Toast.makeText(view.getContext(), "Professor inserido com sucesso!", Toast.LENGTH_LONG).show();
        } catch (SQLException e) {
            Toast.makeText(view.getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
        limparCampos();
    }

    private Professor montaProfessor(){
        Professor p = new Professor();
        p.setCodigo(Integer.parseInt(etCodigoProf.getText().toString()));
        p.setNome(etNomeProf.getText().toString());
        p.setTitulacao(etTitulacaoProf.getText().toString());
        return p;
    }

    private void preencherCampos(Professor p){
        etCodigoProf.setText(String.valueOf(p.getCodigo()));
        etNomeProf.setText(p.getNome());
        etTitulacaoProf.setText(p.getTitulacao());
    }

    private void limparCampos(){
        etCodigoProf.setText("");
        etNomeProf.setText("");
        etTitulacaoProf.setText("");
    }
}