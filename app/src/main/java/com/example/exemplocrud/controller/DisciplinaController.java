package com.example.exemplocrud.controller;

import com.example.exemplocrud.model.Disciplina;
import com.example.exemplocrud.model.Disciplina;
import com.example.exemplocrud.persistence.DisciplinaDao;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

public class DisciplinaController implements IController<Disciplina> {
    private final DisciplinaDao ddao;

    public DisciplinaController(DisciplinaDao ddao) {
        this.ddao = ddao;
    }

    @Override
    public void inserir(Disciplina Disciplina) throws SQLException {
        if(ddao.open() == null){
            ddao.open();
        }
        ddao.insert(Disciplina);
        ddao.close();
    }

    @Override
    public void modificar(Disciplina Disciplina) throws SQLException {
        if(ddao.open() == null){
            ddao.open();
        }
        ddao.update(Disciplina);
        ddao.close();
    }

    @Override
    public void deletar(Disciplina Disciplina) throws SQLException {
        if(ddao.open() == null){
            ddao.open();
        }
        ddao.delete(Disciplina);
        ddao.close();
    }

    @Override
    public Disciplina buscar(Disciplina Disciplina) throws SQLException {
        if(ddao.open() == null){
            ddao.open();
        }
        return ddao.findOne(Disciplina);
    }

    @Override
    public List<Disciplina> listar() throws SQLException {
        if(ddao.open() == null){
            ddao.open();
        }
        return ddao.findAll();
    }
}
