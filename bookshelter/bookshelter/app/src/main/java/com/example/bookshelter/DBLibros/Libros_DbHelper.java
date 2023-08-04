package com.example.bookshelter.DBLibros;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

//==================================================================================================
//  Autor: Nicolás González Bórnez
//  Fecha: 04/11/2022
/*  Clase que crea la base de datos y su tabla por primera vez*/
//==================================================================================================

public class Libros_DbHelper extends SQLiteOpenHelper
{
    private static final int DATABASE_VERSION = 1;              //Versión de la base de datos
    private static final String DATABASE_NAME = "books.db";     //Nombre de la base de datos
    public static final String TABLE_LIBROS = "t_libros";       //Nombre de la tabla

    //Constructor de la clase
    public Libros_DbHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //Método "onCreate" que crea la tabla de la base de datos y sus respectivas columnas
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase)
    {
        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_LIBROS + "(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "titulo TEXT NOT NULL," +
                "autor TEXT NOT NULL," +
                "fecha DATE," +
                "idioma TEXT NOT NULL," +
                "editorial TEXT," +
                "categoria TEXT," +
                "portada TEXT," +
                "ruta TEXT NOT NULL," +
                "marca INTEGER)");
    }

    //Método "onUpgrade" que vuelve a crear el contenido de la base de datos
    //No se activa a menos que cambiemos de versión
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1)
    {
        sqLiteDatabase.execSQL("DROP TABLE " + TABLE_LIBROS);
        onCreate(sqLiteDatabase);
    }
}
