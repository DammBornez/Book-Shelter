package com.example.bookshelter.InfoLibros;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;

import com.example.bookshelter.DBLibros.DbLibros;
import com.example.bookshelter.Pantalla_Principal.Pantalla_Principal;
import com.example.bookshelter.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

//==================================================================================================
//  Autor: Nicolás González Bórnez
//  Fecha: 07/11/2022
/*  Clase encargada de mostrar por pantalla la información referente al libro seleccionado
*   por el usuario y dar acceso a las opción de "eliminar libro" y "editar libro"*/
//==================================================================================================

public class Detalles_libros extends AppCompatActivity {

    //Atributos de la clase
    int id, marca;
    Bitmap portadaBitmap;
    Bundle info;
    ImageView portada;
    TextView titulo, autor, fecha, idioma, editorial, categoria, etiqueta;
    FloatingActionButton btnFlotanteEdit, btnFlotanteDelete;

    //Método "onCreate" que crea e inicializa el layout "activity_detalles_libros"
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles_libros);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Variable "Bundle" que guarda los datos recibidos a través de la llamada intent
        info =getIntent().getExtras();

        //Componentes del layout
        portada = (ImageView) findViewById(R.id.detalles_portada);
        //-------------------------------------------------------------
        titulo = (TextView) findViewById(R.id.detalles_titulo);
        autor = (TextView) findViewById(R.id.detalles_autor);
        fecha = (TextView) findViewById(R.id.detalles_fecha);
        idioma = (TextView) findViewById(R.id.detalles_idioma);
        editorial = (TextView) findViewById(R.id.detalles_editorial);
        categoria = (TextView) findViewById(R.id.detalles_categoría);
        etiqueta = (TextView) findViewById(R.id.detalles_marca);
        //-------------------------------------------------------------
        btnFlotanteEdit = (FloatingActionButton) findViewById(R.id.btnFlotanteEdit);
        btnFlotanteDelete = (FloatingActionButton) findViewById(R.id.btnFlotanteDelete);

        //Llamamos al método que se encarga de mostrar la información del libro
        detallesLibro();

        //Botón flotante que abre la ventana de edición al pulsarlo
        btnFlotanteEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Intent a la activity "Edit_libros"
                Intent intent = new Intent(Detalles_libros.this, Edit_libros.class);

                //Pasamos los datos del libro a la clase
                intent.putExtra("id", info.getInt("id"));
                intent.putExtra("titulo",info.getString("titulo"));
                intent.putExtra("autor",info.getString("autor"));
                intent.putExtra("fecha",info.getString("fecha"));
                intent.putExtra("idioma",info.getString("idioma"));
                intent.putExtra("editorial",info.getString("editorial"));
                intent.putExtra("categoria",info.getString("categoria"));
                intent.putExtra("marca",info.getString("marca"));

                //Abrimos la activity y cerramos esta
                startActivity(intent);
                finish();
            }
        });

        //Botón flotante que muestra la alerta de "eliminación de documento" al pulsarlo
        btnFlotanteDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Mensaje de alerta para informar al usuario de la eliminación
                AlertDialog.Builder builder = new AlertDialog.Builder(Detalles_libros.this);
                builder.setMessage("¿Desea eliminar este registro?")

                        //Si el usuario quiere eliminar el registro se procee con la operación
                        .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                //Llamada al método encargado de eliminar los datos del libro de la base de datos
                                DbLibros db = new DbLibros(Detalles_libros.this);
                                Boolean eliminar = db.eliminarLibro(id);

                                //Si el libro se ha eliminado correctamente
                                if(eliminar)
                                {
                                    //Devolvemos al usuaio a la pantalla principal
                                    Intent intent = new Intent(Detalles_libros.this, Pantalla_Principal.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }
                        })

                        //en caso de que el usuario no quiera, no se hace nada
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        }).show();  //Mostramos el mensaje de alerta
            }
        });
    }

    //Método que se encarga de mostrar la información del libro
    private void detallesLibro()
    {
        //Objeto de la base de datos al que el pasamos el context de la activity
        DbLibros db = new DbLibros(Detalles_libros.this);

        //Inicializamos cada elemento del layout con la información del libro recibida
        id = info.getInt("id");
        titulo.setText(info.getString("titulo"));
        autor.setText(autor.getText() + info.getString("autor"));
        //-------------------------------------------------------------
        if(info.getString("fecha").length() > 0){fecha.setText("Fecha: " + info.getString("fecha"));}
        //-------------------------------------------------------------
        idioma.setText(idioma.getText() + info.getString("idioma"));
        //-------------------------------------------------------------
        if(info.getString("editorial").length() > 0){editorial.setText("Editorial: " + info.getString("editorial"));}
        //-------------------------------------------------------------
        if(info.getString("categoria").length() > 0){categoria.setText("Categoría: " + info.getString("categoria"));}
        //-------------------------------------------------------------
        marca = (info.getInt("marca"));
        //-------------------------------------------------------------
        portadaBitmap = db.getPortada(id);
        portada.setImageBitmap(portadaBitmap);

        //En función de la marca mostramos el mensaje de la categoría con un texto y color diferentes
        switch (marca)
        {
            case 1:
                etiqueta.setText("Leyendo");
                etiqueta.setTextColor(Color.BLUE);
                break;
            case 2:
                etiqueta.setText("Leido");
                etiqueta.setTextColor(Color.rgb(255, 149, 0));
                break;
            case 3:
                etiqueta.setText("Favorito");
                etiqueta.setTextColor(Color.MAGENTA);
                break;
            case 4:
                etiqueta.setText("Abandonado");
                etiqueta.setTextColor(Color.RED);
                break;
            default:
                break;
        }
    }
}