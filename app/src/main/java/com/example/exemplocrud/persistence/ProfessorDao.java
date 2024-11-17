package com.example.exemplocrud.persistence;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.exemplocrud.model.Professor;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ProfessorDao implements IProfessorDao, ICRUDDao<Professor>{
    private final Context context;
    private GenericDao gDao;
    private SQLiteDatabase db;

    public ProfessorDao(Context context){
        this.context = context;
    }

    @Override
    public ProfessorDao open() throws SQLException {
        gDao = new GenericDao(context);
        db = gDao.getWritableDatabase();
        return this;
    }

    @Override
    public void close() {
        gDao.close();
    }

    @Override
    public void insert(Professor professor) throws SQLException {
        ContentValues contentValues = getContentValues(professor);
    }

    public static ContentValues getContentValues(Professor professor){
        ContentValues contentValues = new ContentValues();
        contentValues.put("codigo", professor.getCodigo());
        contentValues.put("nome", professor.getNome());
        contentValues.put("titulacao", professor.getTitulacao());

        return contentValues;
    }

    @Override
    public int update(Professor professor) throws SQLException {
        ContentValues contentValues = getContentValues(professor);
        int ret = db.update("professor", contentValues,"codigo = " + professor.getCodigo(), null);
        return ret;
    }

    @Override
    public void delete(Professor professor) throws SQLException {
        db.delete("professor","codigo = " + professor.getCodigo(), null);
    }
    @SuppressLint("Range")
    @Override
    public Professor findOne(Professor professor) throws SQLException {
        String sql = "SELECT codigo, nome, titulacao FROM professor WHERE codigo = " + professor.getCodigo();
        Cursor cursor = db.rawQuery(sql, null);
        if(cursor != null){
            cursor.moveToFirst();
        }
        if(!cursor.isAfterLast()){
            professor.setCodigo(cursor.getInt(cursor.getColumnIndex("codigo")));
            professor.setNome(cursor.getString(cursor.getColumnIndex("nome")));
            professor.setTitulacao(cursor.getString(cursor.getColumnIndex("titulacao")));
        }
        cursor.close();
        return professor;
    }

    @SuppressLint("Range")
    @Override
    public List<Professor> findAll() throws SQLException {
        List<Professor> professores = new ArrayList<>();
        String sql = "SELECT codigo, nome, titulacao FROM professor";
        Cursor cursor = db.rawQuery(sql, null);
        if(cursor != null){
            cursor.moveToFirst();
        }
        while(!cursor.isAfterLast()){
            Professor professor = new Professor();
            professor.setCodigo(cursor.getInt(cursor.getColumnIndex("codigo")));
            professor.setNome(cursor.getString(cursor.getColumnIndex("nome")));
            professor.setTitulacao(cursor.getString(cursor.getColumnIndex("titulacao")));
            professores.add(professor);
            cursor.moveToNext();
        }
        cursor.close();
        return professores;
    }
}
