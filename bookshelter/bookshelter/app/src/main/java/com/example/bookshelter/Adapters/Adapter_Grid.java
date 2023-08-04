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
import java.util.List;
import java.util.stream.Collectors;

import androidx.annotation.NonNull;

//==================================================================================================
//  Autor: Nicolás González Bórnez
//  Fecha: 31/10/2022
/*  Clase adapter que gestiona los datos que se mostrarán en el GridView de libros*/
//==================================================================================================

public class Adapter_Grid extends ArrayAdapter<BookItem>
{
    //Atributos de la clase
    protected Context context;  //Contexto de la activity
    protected ArrayList<BookItem> libro;            //ArrayList con la información de los libros
    protected ArrayList<BookItem> libro_buscador;   //ArrayList utilizaod para la filtración de libros

    //Constructor de la clase que recibe el contexto y el ArrayList<BookItem> con la información de los libros
    public Adapter_Grid(@NonNull Activity context, ArrayList<BookItem> libro)
    {
        super(context, R.layout.book_item, libro);
        this.context = context;
        this.libro = libro;

        //Inicializamos el arrayList con la información de los libros
        libro_buscador = new ArrayList<>();
        libro_buscador.addAll(libro);
    }

    //Método que inicializa los datos contenidos en el GridView
    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        //Inflamos el layout
        LayoutInflater inflater = LayoutInflater.from(context);
        View item =inflater.inflate(R.layout.book_item, null);

        //Inicializamos el imageView de la portada
        ImageView portada =(ImageView) item.findViewById(R.id.PortadaLibro);
        portada.setImageBitmap(libro.get(position).getPortada());

        //Inicializamos el textView del título
        TextView titulo = (TextView) item.findViewById(R.id.TituloLibro);
        titulo.setText(libro.get(position).getTitulo());
        titulo.setSelected(true);

        //Inicializamos el TextView del autor
        TextView autor = (TextView) item.findViewById(R.id.AutorLibro);
        autor.setText(libro.get(position).getAutor());
        autor.setSelected(true);

        return item;
    }

    //Método que crea una nueva lista con los libros cuyo título coincide con el texto recivido
    public void filtracion(String filtro, boolean filtroCategoria)
    {
        //Variables del método
        List<BookItem> coleccion;
        int longitud = filtro.length();

        //Comprobamos que haya contenido en el buscador
        //En caso de que no lo haya, retornamos la tabla con todos los libros recibidos
        if(longitud == 0)
        {
            libro.clear();
            libro.addAll(libro_buscador);
        }

        //Si se ha escrito algo
        else
        {
            libro.clear();
            libro.addAll(libro_buscador);

            //En caso de que se hayan filtrado por categorías
            if(filtroCategoria){
                //Filtramos los libros según la categoría
                coleccion = libro.stream().filter(i -> i.getCategoria().toLowerCase().contains(filtro.toLowerCase()))
                        .collect(Collectors.toList());
            }
            //En caso de que se haya filtrado por el título del libro
            else{
                //Filtramos los libros según el título
                coleccion = libro.stream().filter(i -> i.getTitulo().toLowerCase().contains(filtro.toLowerCase()))
                        .collect(Collectors.toList());
            }

            //Vaciamos la lista y la volvemos a inicializar con los libros ya filtrados
            libro.clear();
            libro.addAll(coleccion);
        }

        //Método que notifica que se han realizado cambios en la lista
        notifyDataSetChanged();
    }
}
