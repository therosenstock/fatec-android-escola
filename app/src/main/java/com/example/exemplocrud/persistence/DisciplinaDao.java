package com.example.exemplocrud.persistence;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.exemplocrud.model.Disciplina;
import com.example.exemplocrud.model.Professor;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DisciplinaDao implements IDisciplinaDao, ICRUDDao<Disciplina> {

    private final Context context;
    private GenericDao gDao;
    private SQLiteDatabase db;

    public DisciplinaDao(Context context){
        this.context = context;
    }

    @Override
    public DisciplinaDao open() throws SQLException {
        gDao = new GenericDao(context);
        db = gDao.getWritableDatabase();
        return this;
    }

    @Override
    public void close() {
        gDao.close();
    }

    @Override
    public void insert(Disciplina disciplina) throws SQLException {
        ContentValues contentValues = getContentValues(disciplina);
        db.insert("disciplina", null, contentValues);
    }

    public static ContentValues getContentValues(Disciplina disciplina){
        ContentValues contentValues = new ContentValues();
        contentValues.put("codigo", disciplina.getCodigo());
        contentValues.put("nome", disciplina.getNome());
        contentValues.put("codigo_professor", disciplina.getProfessor().getCodigo());

        return contentValues;
    }

    @Override
    public int update(Disciplina disciplina) throws SQLException {
        ContentValues contentValues = getContentValues(disciplina);
        int ret = db.update("disciplina", contentValues,"codigo = " + disciplina.getCodigo(), null);
        return ret;
    }

    @Override
    public void delete(Disciplina disciplina) throws SQLException {
        db.delete("disciplina","codigo = " + disciplina.getCodigo(), null);
    }

    @SuppressLint("Range")
    @Override
    public Disciplina findOne(Disciplina disciplina) throws SQLException {
        String sql = "SELECT p.codigo as cod_prof, p.nome as nome_prof, p.titulacao as tit_prof, " +
                "d.codigo as codigo, d.nome as nome FROM professor p, disciplina d " +
                "WHERE p.codigo = d.codigo_professor " +
                "AND d.codigo =  " + disciplina.getCodigo();

        Cursor cursor = db.rawQuery(sql, null);
        if(cursor != null){
            cursor.moveToFirst();
        }
        if(!cursor.isAfterLast()){
            Professor professor = new Professor();
            professor.setCodigo(cursor.getInt(cursor.getColumnIndex("cod_prof")));
            professor.setNome(cursor.getString(cursor.getColumnIndex("nome_prof")));
            professor.setTitulacao(cursor.getString(cursor.getColumnIndex("tit_prof")));

            disciplina.setCodigo(cursor.getInt(cursor.getColumnIndex("codigo")));
            disciplina.setNome(cursor.getString(cursor.getColumnIndex("nome")));
            disciplina.setProfessor(professor);
        }
        cursor.close();
        return disciplina;

    }

    @SuppressLint("Range")
    @Override
    public List<Disciplina> findAll() throws SQLException {
        List<Disciplina> disciplinas = new ArrayList<>();
        String sql = "SELECT p.codigo as cod_prof, p.nome as nome_prof, p.titulacao as tit_prof, " +
                "d.codigo as codigo, d.nome as nome FROM professor p, disciplina d " +
                "WHERE p.codigo = d.codigo_professor" ;
        Cursor cursor = db.rawQuery(sql, null);
        if(cursor != null){
            cursor.moveToFirst();
        }
        while(!cursor.isAfterLast()){
            Disciplina disciplina = new Disciplina();
            Professor professor = new Professor();
            professor.setCodigo(cursor.getInt(cursor.getColumnIndex("cod_prof")));
            professor.setNome(cursor.getString(cursor.getColumnIndex("nome_prof")));
            professor.setTitulacao(cursor.getString(cursor.getColumnIndex("tit_prof")));

            disciplina.setCodigo(cursor.getInt(cursor.getColumnIndex("codigo")));
            disciplina.setNome(cursor.getString(cursor.getColumnIndex("nome")));
            disciplina.setProfessor(professor);

            disciplinas.add(disciplina);
            cursor.moveToNext();
        }
        cursor.close();
        return disciplinas;
    }
}

