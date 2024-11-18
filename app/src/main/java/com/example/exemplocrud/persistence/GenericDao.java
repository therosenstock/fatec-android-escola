package com.example.exemplocrud.persistence;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class GenericDao extends SQLiteOpenHelper {
    private static final String DATABASE = "ACADEMICO.DB";
    private static final int DATABASE_VER = 1;
    private static final String CREATE_TABLE_PROF =
            "CREATE TABLE professor(" +
                    "codigo int not null PRIMARY KEY," +
                    "nome VARCHAR(100) NOT NULL," +
                    "titulacao VARCHAR(50) NOT NULL);";
    private static final String CREATE_TABLE_DISC =
            "CREATE TABLE disciplina(" +
                    "codigo int not null PRIMARY KEY," +
                    "nome VARCHAR(100) NOT NULL," +
                    "codigo_professor INT NOT NULL," +
                    "FOREIGN KEY (codigo_professor) REFERENCES professor(codigo));";

    public GenericDao(Context context){
        super(context, DATABASE, null, DATABASE_VER);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_PROF);
        db.execSQL(CREATE_TABLE_DISC);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(newVersion > oldVersion){
            db.execSQL("DROP TABLE IF EXISTS disciplina");
            db.execSQL("DROP TABLE IF EXISTS professor");
            onCreate(db);
        }
    }
}
