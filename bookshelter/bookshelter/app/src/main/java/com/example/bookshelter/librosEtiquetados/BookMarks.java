package com.example.bookshelter.librosEtiquetados;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.bookshelter.Adapters.Adapter_List;
import com.example.bookshelter.BookReader.EpubReader;
import com.example.bookshelter.DBLibros.DbLibros;
import com.example.bookshelter.Models.BookItem;
import com.example.bookshelter.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

//==================================================================================================
//  Autor: Nicolás González Bórnez
//  Fecha: 25/11/2022
/*  Clase que permite visualizar y cambiar una lista con los diferentes libros almacenados
*   en la app clasificados por categoría*/
//==================================================================================================

public class BookMarks extends AppCompatActivity {

    //Atributos de la clase
    Adapter_List adapterLista;
    ArrayList<BookItem> listaLibros = new ArrayList<>();
    EpubReader epubReader;
    int marca = 1;

    //Componentes del layout
    FloatingActionButton btnFlotanteFavoritos, btnFlotanteLeyendo, btnFlotanteLeido, btnFlotanteAbandonados;
    ListView listaEtiquetados;
    TextView textCategoria;

    //Métoco "onCreate" que crea al layout "activity_book_marks"
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_marks);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Inicializamos los componentes del layout
        btnFlotanteFavoritos = (FloatingActionButton) findViewById(R.id.btnFlotanteFavoritos);
        btnFlotanteLeyendo = (FloatingActionButton) findViewById(R.id.btnFlotanteLeyendo);
        btnFlotanteLeido = (FloatingActionButton) findViewById(R.id.btnFlotanteLeido);
        btnFlotanteAbandonados = (FloatingActionButton) findViewById(R.id.btnFlotanteAbandonados);
        //----------------------------------------------------------------
        listaEtiquetados = (ListView) findViewById(R.id.listViewEtiquetados);
        listaEtiquetados.setSelected(true);
        //----------------------------------------------------------------
        textCategoria = (TextView) findViewById(R.id.textView_Categoria);

        /*Llamamos al método que retorna una lista con la información de los libros
        cuya marca corresponda con la enviada*/
        getLibrosEtiquetados(marca);

        /*Método que cambia el valor de la marca y el texto de la categoría
        * al pulsar el botón "btnFlotanteFavoritos"*/
        btnFlotanteFavoritos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                marca = 1;
                getLibrosEtiquetados(marca);
                textCategoria.setText("FAVORITOS");
            }
        });

        /*Método que cambia el valor de la marca y el texto de la categoría
         * al pulsar el botón "btnFlotanteLeyendo"*/
        btnFlotanteLeyendo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                marca = 2;
                getLibrosEtiquetados(marca);
                textCategoria.setText("LEYENDO");
            }
        });

        /*Método que cambia el valor de la marca y el texto de la categoría
         * al pulsar el botón "btnFlotanteLeido"*/
        btnFlotanteLeido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                marca = 3;
                getLibrosEtiquetados(marca);
                textCategoria.setText("LEIDOS");
            }
        });

        /*Método que cambia el valor de la marca y el texto de la categoría
         * al pulsar el botón "btnFlotanteAbandonados"*/
        btnFlotanteAbandonados.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                marca = 4;
                getLibrosEtiquetados(marca);
                textCategoria.setText("ABANDONADOS");
            }
        });

        //Método que permite visualizar el contenido del libro sobre el que se ha pulsado en la lista
        listaEtiquetados.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                /*Llamamos al método "mostrarLibros" la clase "DbLibros" que devuelve
                * la información de los libros de la base de datos*/
                DbLibros db = new DbLibros(BookMarks.this);
                listaLibros = db.mostrarLibros();

                //Llamamos al método "getRutaLibro" que deuelve la ruta del libro especificado
                int id = listaLibros.get(i).getId();
                String ruta = db.getRutaLibro(id);

                //Mostramos el contenido del libro
                Uri uriLibro = Uri.parse(ruta);
                epubReader = new EpubReader(BookMarks.this);
                epubReader.leerDocumento(uriLibro);
            }
        });
    }

    /*Método que retorna una lista con la información de los libros
        cuya marca corresponda con la enviada*/
    private void getLibrosEtiquetados(int marca)
    {
        //Objeto de la clase "DbLibros"
        DbLibros db = new DbLibros(BookMarks.this);

        //Asignamos un adapter a la lista
        listaEtiquetados.setAdapter(null);

        /*Llamamos al método "retornarLibrosEtiquetados" que devuelve una lista con los libros
        * cuya marca corresponda a la que se le ha enviado*/
        listaLibros = db.retornarLibrosEtiquetados(marca);
        adapterLista = new Adapter_List(BookMarks.this, listaLibros);
        listaEtiquetados.setAdapter(adapterLista);
    }
}