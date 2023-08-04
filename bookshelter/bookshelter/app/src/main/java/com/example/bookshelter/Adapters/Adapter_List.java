package com.example.bookshelter.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bookshelter.Models.BookItem;
import com.example.bookshelter.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;

//==================================================================================================
//  Autor: Nicolás González Bórnez
//  Fecha: 27/11/2022
/*  Clase adapter que gestiona los datos que se mostrarán en el ListView de libros etiquetados*/
//==================================================================================================

public class Adapter_List extends ArrayAdapter<BookItem>
{
    //Atributos de la clase
    protected Context context;  //Contexto de la activity
    protected ArrayList<BookItem> libro;    //ArrayList con la información de los libros

    //Constructor de la clase que recibe el contexto y el ArrayList<BookItem> con la información de los libros
    public Adapter_List(@NonNull Activity context, ArrayList<BookItem> libro)
    {
        super(context, R.layout.book_item, libro);
        this.context = context;
        this.libro = libro;
    }

    //Método que inicializa los datos contenidos en el GridView
    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        //Inflamos el layout
        LayoutInflater inflater = LayoutInflater.from(context);
        View item =inflater.inflate(R.layout.book_mark_item, null);

        //Inicializamos el imageView de la portada
        ImageView portada =(ImageView) item.findViewById(R.id.linearLayoutPortada);
        portada.setImageBitmap(libro.get(position).getPortada());

        //Inicializamos el textView del título
        TextView titulo = (TextView) item.findViewById(R.id.linearLayoutTitulo);
        titulo.setText(libro.get(position).getTitulo());
        titulo.setSelected(true);

        //Inicializamos el TextView del autor
        TextView autor = (TextView) item.findViewById(R.id.linearLayoutAutor);
        autor.setText(libro.get(position).getAutor());
        autor.setSelected(true);

        //Inicializamos el TextView de la/las categorías
        TextView categoria = (TextView) item.findViewById(R.id.linearLayoutGenero);
        categoria.setText(libro.get(position).getCategoria());
        if(categoria.equals("")){categoria.setText("Categoría no definida");}
        categoria.setSelected(true);

        return item;
    }
}
