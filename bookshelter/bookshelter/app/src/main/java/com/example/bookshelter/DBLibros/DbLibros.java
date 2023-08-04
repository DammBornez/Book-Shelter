package com.example.bookshelter.DBLibros;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.Toast;

import com.example.bookshelter.Models.BookItem;
import com.example.bookshelter.R;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import androidx.annotation.Nullable;

//==================================================================================================
//  Autor: Nicolás González Bórnez
//  Fecha: 04/11/2022
/*  Clase encargada de la gestión de los diferentes scripts de la base de datos*/
//==================================================================================================

public class DbLibros extends Libros_DbHelper {

    Context context;    //Contexto de la clase

    //Constructor que recibe el contexto
    public DbLibros(@Nullable Context context) {
        super(context);
        this.context = context;
    }

    //Método que recibe toda la información correspondiente a un libro y la agrega a la base de datos
    public long agregarLibro(String titulo, String autor, String fecha,
                             String idioma, String editorial, String categoria, Bitmap portada,
                             String ruta, int marca)
    {
        long id = 0;    //Variable que verifica si los datos se han guardado

        //Bloque de instrucciones que puede dar a error
        try {
            //Validamos que la ruta no se encuentre ya en la base de datos
            //En caso de que no
            if(!libroRepetido(ruta)) {
                Libros_DbHelper dbHelper = new Libros_DbHelper(context);
                SQLiteDatabase db = dbHelper.getWritableDatabase();

                //En caso de no encontrar una portada, le damos una por ddfecto
                if (portada == null) {
                    portada = BitmapFactory.decodeResource(context.getResources(), R.drawable.libro_sin_portada);
                }

                //Convertimos el bitmap de la portada en un array de bytes
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                portada.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] portada_bytes = stream.toByteArray();

                //Guardamos todos los datos del libro en un "ContentValues"
                ContentValues values = new ContentValues();
                values.put("titulo", titulo);
                values.put("autor", autor);
                values.put("fecha", fecha);
                values.put("idioma", idioma);
                values.put("editorial", editorial);
                values.put("categoria", categoria);
                values.put("portada", portada_bytes);
                values.put("ruta", ruta);
                values.put("marca", marca);

                //Insertamos los datos en la tabla
                //Si se ha relizado correctamente, i > 0
                id = db.insert(TABLE_LIBROS, null, values);
            }

            //Si la ruta se encuentra ya guardada en la base de datos, se le informa al usuario
            else{ Toast.makeText(context, "El libro seleccionado ya ha sido agregado previamente", Toast.LENGTH_LONG).show();}
        }

        //En caso de error
        catch(Exception error)
        {
            error.toString();
        }

        //Retornamos el valor del script
        return id;
    }

    //Método que comprueba si el libro seleccionado ya se ha agregado previamente
    private Boolean libroRepetido(String ruta)
    {
        //Variable que confirma si la ruta es repetida o no
        Boolean repetido = false;

        Libros_DbHelper dbHelper = new Libros_DbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        try{
            //Cursor que se mueve a lo largo del contenido que devuelve el script
            Cursor cursor;

            //Recorremos la columna "ruta" d ela base de datos
            cursor = db.rawQuery("SELECT ruta FROM " + TABLE_LIBROS, null);

            //Si el cursor se mueve, por lo que tiene contenido
            if(cursor.moveToFirst())
            {
                do
                {
                    //Comprobamos si el contenido del cursor es igual a la ruta del libro
                    if(cursor.getString(0).equals(ruta))
                    {
                        //Si es así, cambiamos el valor de "repetido" y salimos del bucle
                        repetido = true;
                        break;
                    }
                    else{ repetido = false;}
                }while(cursor.moveToNext()); //Repetimos mientras el cursor se mueva
            }
        }
        catch(Exception error)
        {
            repetido = true;
        }
        finally {

            //Cerramos la conexión
            db.close();
        }

        return repetido;
    }

    //Método encargado de editar la información del libro seleccionado
    public Boolean editarLibro(int id, String titulo, String autor, String fecha,
                             String idioma, String editorial, String categoria, int marca)
    {
        Libros_DbHelper dbHelper = new Libros_DbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        Boolean editado = false;

        try {

            //Guardamos todos los datos del libro en un "ContentValues"
            ContentValues values = new ContentValues();
            values.put("titulo", titulo);
            values.put("autor", autor);
            values.put("fecha", fecha);
            values.put("idioma", idioma);
            values.put("editorial", editorial);
            values.put("categoria", categoria);
            values.put("marca", marca);

            //Modificamos la tabla con los datos enviados por la activity
            db.execSQL("UPDATE " + TABLE_LIBROS + " SET titulo = '" + titulo +
                    "', autor = '" + autor +
                    "', fecha = '" + fecha +
                    "', idioma = '" + idioma +
                    "', editorial = '" + editorial +
                    "', categoria = '" + categoria +
                    "', marca = '" + marca +
                    "' WHERE id = '" + id + "' ");

            //Validamos que la modificación ha sido exitosa
            editado = true;
        }
        catch(Exception error)
        {
            error.toString();
            editado = false;
        }
        finally {

            //Cerramos la conexión
            db.close();
        }

        return editado;
    }

    //Método encargado de eliminar el libro seleccionado de la base de datos
    public boolean eliminarLibro(int id)
    {
        //Variable que confirma el borrado del libro
        Boolean eliminado = false;

        Libros_DbHelper dbHelper = new Libros_DbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        try
        {
            //Eliminamos el libro de la base de datos
            db.execSQL("DELETE FROM " + TABLE_LIBROS + " WHERE id = " + id);
            eliminado = true;
        }
        catch(Exception error)
        {
            eliminado = false;
        }
        finally{ db.close();}

        return eliminado;
    }

    //Método que devuelve la portada del libro especificado
    public Bitmap getPortada(int id)
    {
        Libros_DbHelper dbHelper = new Libros_DbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        byte[] portadaByte;
        Bitmap portadaBitmap = null;
        Cursor cursor;

        try {
            //Seleccionamos la portada según el id recibido
            cursor = db.rawQuery("SELECT portada FROM " + TABLE_LIBROS + " WHERE id = " + id, null);

            //Si el cursor tiene contenido
            if (cursor.moveToFirst()) {
                do {
                    //Pasamos la portada a un BitMap
                    portadaByte = cursor.getBlob(0);
                    portadaBitmap = BitmapFactory.decodeByteArray(portadaByte, 0, portadaByte.length);

                } while (cursor.moveToNext()); //recorremos mientras el cursor tenga contenido
            }

            cursor.close();
        }
        catch(Exception error)
        {
            error.toString();
        }
        finally {

            //Cerramos la conexión
            db.close();
        }

        //Devolvemos la portada
        return portadaBitmap;
    }

    //Método que devuelve un arrayList con los datos de los libros almacenados en la base de datos
    public ArrayList<BookItem> mostrarLibros()
    {
        Libros_DbHelper dbHelper = new Libros_DbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        //ArrayList con los datos de los libros
        ArrayList<BookItem> listaLibros = new ArrayList<>();
        BookItem libro = null;  //Objeto que guarda los datos
        Cursor cursor = null;   //Cursor que recorre los datos del script

        try {
            //Recogemos todos lo datos de la base de datos
            cursor = db.rawQuery("SELECT * FROM " + TABLE_LIBROS, null);

            //Si el cursor se desplaza, esto indica que tiene contenido
            if (cursor.moveToFirst()) {
                do {
                    //Guardamos los datos del cursor en el objeto de "BookItem"
                    libro = new BookItem();

                    libro.setId(cursor.getInt(0));
                    libro.setTitulo(cursor.getString(1));
                    libro.setAutor(cursor.getString(2));
                    libro.setFecha(cursor.getString(3));
                    libro.setIdioma(cursor.getString(4));
                    libro.setEditorial(cursor.getString(5));
                    libro.setCategoria(cursor.getString(6));

                    //Convertimos la portada a un BitMap
                    Bitmap cover = BitmapFactory.decodeByteArray(cursor.getBlob(7), 0, cursor.getBlob(7).length);
                    libro.setPortada(cover);

                    libro.setMarca(cursor.getInt(9));

                    //Añadimos el libro a la lista
                    listaLibros.add(libro);
                } while (cursor.moveToNext());   //Realizamos mientras el cursor se mueva
            }

            cursor.close();
        }
        catch(Exception error)
        {
            error.toString();
        }
        finally {

            //Cerramos la conexión
            db.close();
        }


        cursor.close();
        return listaLibros;
    }


    //Método que devuelve la ruta del libro seleccionado en el gridView
    public String getRutaLibro(int idLibro)
    {
        String rutaLibro = "";

        Libros_DbHelper dbHelper = new Libros_DbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        Cursor cursor;

        try {
            //Seleccionamos la ruta del libro especificado
            cursor = db.rawQuery("SELECT ruta FROM " + TABLE_LIBROS + " WHERE id = " + idLibro, null);

            if (cursor.moveToFirst()) {
                rutaLibro = cursor.getString(0);
            }

            cursor.close();
        }
        catch(Exception error)
        {
            error.toString();
        }
        finally {

            //Cerramos la conexión
            db.close();
        }

        return rutaLibro;
    }

    //Método que devuelve una lisla de libros en función de su etiqueta
    public ArrayList<BookItem> retornarLibrosEtiquetados(int marca)
    {
        ArrayList<BookItem> listaLibros = new ArrayList<>();
        BookItem libro = null;

        Libros_DbHelper dbHelper = new Libros_DbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        Cursor cursor;

        try {
            //Seleccionamos el titulo, el autor, la portada y la categoria de los libros con la marca especificada
            cursor = db.rawQuery("SELECT titulo, autor, portada, categoria FROM " + TABLE_LIBROS + " WHERE marca = " + marca, null);

            //Si el cursor se desplaza, esto indica que tiene contenido
            if (cursor.moveToFirst()) {
                do {
                    //Guardamos los datos en el objeto de "BookItem"
                    libro = new BookItem();

                    libro.setTitulo(cursor.getString(0));
                    libro.setAutor(cursor.getString(1));

                    //Convertimos la imagen a BitMap
                    Bitmap cover = BitmapFactory.decodeByteArray(cursor.getBlob(2), 0, cursor.getBlob(2).length);
                    libro.setPortada(cover);

                    libro.setCategoria(cursor.getString(3));

                    //Añadimos el libro a la lista
                    listaLibros.add(libro);

                } while (cursor.moveToNext());//Realizamos mientras el cursor se mueva
            }

            cursor.close();
        }
        catch(Exception error)
        {
            error.toString();
        }
        finally {

            //Cerramos la conexión
            db.close();
        }

        //Retornamos la lista
        return listaLibros;
    }

    //Devuelve un arraylist con todas las categorías de la base de datos
    public ArrayList<String> getListaCategorias()
    {
        String categoria;
        ArrayList<String> listaCategorias = new ArrayList<>(); //Lista que guarda las categorías que haya en la base de datos

        Libros_DbHelper dbHelper = new Libros_DbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        Cursor cursor;

        try {
            //Almacenamos las categorias de la base de datos
            cursor = db.rawQuery("SELECT categoria FROM " + TABLE_LIBROS, null);

            //Si el cursor se desplaza, esto indica que tiene contenido
            if (cursor.moveToFirst()) {
                do {
                    //Almacenamos la categoria en la variable correspondiente
                    categoria = cursor.getString(0);

                    //Validamos si la categoría tiene contenido
                    if (!categoria.isEmpty()) {
                        listaCategorias.add(categoria);
                    }

                } while (cursor.moveToNext()); //Realizamos mientras el cursor se mueva
            }

        }
        catch(Exception error)
        {
            error.toString();
        }
        finally {

            //Cerramos la conexión
            db.close();
        }

        //retornamos la lista con las categorías
        return listaCategorias;
    }
}
