package com.example.bookshelter.InfoLibros;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.bookshelter.DBLibros.DbLibros;
import com.example.bookshelter.Pantalla_Principal.Pantalla_Principal;
import com.example.bookshelter.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

//==================================================================================================
//  Autor: Nicolás González Bórnez
//  Fecha: 15/11/2022
/*  Clase encargada de habilitar la edición de los datos del libro seleccionado y de
*   sobrescribirlos en la base de datps*/
//==================================================================================================

public class Edit_libros extends AppCompatActivity {

    //Atributos de la clase
    int id, marca;
    Bitmap portadaBitmap;
    Bundle info;
    Boolean editado = false;

    //Componentes del layout
    ImageView portada;
    EditText titulo, autor, fecha, idioma, editorial, categoria;
    RadioGroup group;
    RadioButton rb_noEMarca, rb_leyendo, rb_leido, rb_favorito, rb_descartado;
    FloatingActionButton btnFlotanteSave;

    //Método "onCreate" que crea e inicializa el layout "activity_edit_libros"
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_libros);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Variable "Bundle" que guarda los datos recibidos a través de la llamada intent
        info =getIntent().getExtras();

        //Componentes del layout
        portada = (ImageView) findViewById(R.id.edit_portada);
        //-------------------------------------------------------------
        titulo = (EditText) findViewById(R.id.edit_titulo);
        autor = (EditText) findViewById(R.id.edit_autor);
        fecha = (EditText) findViewById(R.id.edit_fecha);
        idioma = (EditText) findViewById(R.id.edit_idioma);
        editorial = (EditText) findViewById(R.id.edit_editorial);
        categoria = (EditText) findViewById(R.id.edit_categoría);
        //-------------------------------------------------------------
        group = (RadioGroup) findViewById(R.id.edit_radiogroup);
        rb_noEMarca = (RadioButton) findViewById(R.id.radioButton_sinMarcar);
        rb_leyendo = (RadioButton) findViewById(R.id.radioButton_leyendo);
        rb_leido = (RadioButton) findViewById(R.id.radioButton_leido);
        rb_favorito = (RadioButton) findViewById(R.id.radioButton_favorito);
        rb_descartado = (RadioButton) findViewById(R.id.radioButton_descartado);
        //-------------------------------------------------------------
        btnFlotanteSave = (FloatingActionButton) findViewById(R.id.btnFlotanteSave);

        //Llamada al método que inicializa los componentes del layout con la información recibida
        infoLibros();

        //Método que cambia el valor de "marca" según la etiqueta que el usuario seleccione
        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {

                String marcaId = ((RadioButton) findViewById(group.getCheckedRadioButtonId())).getText().toString();
                switch (marcaId)
                {
                    case "Leyendo":
                        marca = 1;
                        break;
                    case "Leido":
                        marca = 2;
                        break;
                    case "Favorito":
                        marca = 3;
                        break;
                    case "Descartado":
                        marca = 4;
                        break;
                    default:
                        marca = 0;
                        break;
                }
            }
        });

        /*Método que avisa al usuairo de querer guardar los cambios del libro y así procede si la
        *   respuesta es afirmativa*/
        //El método se inicia al pulsar el botón flotante "btnFlotanteSave"
        btnFlotanteSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Mensaje de alerta para informar al usuario de que se sobrescribirán los datos del libro
                AlertDialog.Builder builder = new AlertDialog.Builder(Edit_libros.this);
                builder.setMessage("Se sobrescribirán los datos del registro \n ¿Desea continuar?")

                        //En caso de querer proceder con la edición de datos
                        .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                //El campo "titulo" y "autor" son obligatorios
                                if(titulo.getText().length() > 0 || autor.getText().length() > 0) {

                                    //Llamada a la base de datos donde le pasamos el contexto de la activity
                                    DbLibros db = new DbLibros(Edit_libros.this);

                                    //Método que modifica en la base de datos los datos del libro seleccionado
                                    editado = db.editarLibro(id, titulo.getText().toString(), autor.getText().toString(), fecha.getText().toString(),
                                            idioma.getText().toString(), editorial.getText().toString(), categoria.getText().toString(), marca);

                                    //Si la edición ha sido exitosa se muestra un mensaje
                                    if (editado) {
                                        Toast.makeText(Edit_libros.this, "Registro editado exitosamente", Toast.LENGTH_LONG).show();
                                    }

                                    //En caso contrario se muestra un mensaje de error
                                    else {
                                        Toast.makeText(Edit_libros.this, "Ha ocurrido un problema durante el proceso", Toast.LENGTH_LONG).show();
                                    }
                                }
                                else{Toast.makeText(Edit_libros.this, "Recuerde rellenar los espacios obligatorios", Toast.LENGTH_LONG).show();}

                                //Devolvemos al usuario a la pantalla principal una vez se hayan realizado la modificación
                                Intent intent = new Intent(Edit_libros.this, Pantalla_Principal.class);
                                startActivity(intent);
                                finish();
                            }
                        })

                        //En caso de no querer proceder, no se hace nada
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        }).show();
            }
        });
    }

    //Método que se encarga de mostrar la información del libro
    @SuppressLint("ResourceType")
    private void infoLibros()
    {
        //Objeto de la base de datos al que el pasamos el context de la activity
        DbLibros db = new DbLibros(Edit_libros.this);

        //Inicializamos cada elemento del layout con la información del libro recibida
        id = info.getInt("id");
        titulo.setText(info.getString("titulo"));
        autor.setText(info.getString("autor"));
        //-------------------------------------------------------------
        if(info.getString("fecha").length() > 0){fecha.setText(info.getString("fecha"));}
        //-------------------------------------------------------------
        idioma.setText(info.getString("idioma"));
        //-------------------------------------------------------------
        if(info.getString("editorial").length() > 0){editorial.setText(info.getString("editorial"));}
        //-------------------------------------------------------------
        if(info.getString("categoria").length() > 0){categoria.setText(info.getString("categoria"));}
        //-------------------------------------------------------------
        marca = (info.getInt("marca"));
        //-------------------------------------------------------------
        portadaBitmap = db.getPortada(id);
        portada.setImageBitmap(portadaBitmap);
    }
}