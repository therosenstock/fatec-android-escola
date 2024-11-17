package com.example.exemplocrud.controller;

import com.example.exemplocrud.model.Professor;
import com.example.exemplocrud.persistence.ProfessorDao;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

public class ProfessorController implements IController<Professor> {
    private final ProfessorDao pdao;

    public ProfessorController(ProfessorDao pdao) {
        this.pdao = pdao;
    }

    @Override
    public void inserir(Professor professor) throws SQLException {
        if(pdao.open() == null){
            pdao.open();
        }
        pdao.insert(professor);
        pdao.close();
    }

    @Override
    public void modificar(Professor professor) throws SQLException {
        if(pdao.open() == null){
            pdao.open();
        }
        pdao.update(professor);
        pdao.close();
    }

    @Override
    public void deletar(Professor professor) throws SQLException {
        if(pdao.open() == null){
            pdao.open();
        }
        pdao.delete(professor);
        pdao.close();
    }

    @Override
    public Professor buscar(Professor professor) throws SQLException {
        if(pdao.open() == null){
            pdao.open();
        }
        return pdao.findOne(professor);
    }

    @Override
    public List<Professor> listar() throws SQLException {
        if(pdao.open() == null){
            pdao.open();
        }
        return pdao.findAll();
    }
}
