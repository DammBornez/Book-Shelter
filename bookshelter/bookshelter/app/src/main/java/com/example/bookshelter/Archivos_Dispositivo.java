package com.example.bookshelter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bookshelter.Adapters.Adapter_Archivos;
import com.example.bookshelter.InfoLibros.Edit_libros;

import java.io.File;
import java.util.Arrays;
import java.util.List;

//==================================================================================================
//  Autor: Nicolás González Bórnez
//  Fecha: 01/09/2022
/*  Activity que se encarga de mostrar la lista de carpetas del dispositivo para que el usuario
*   pueda seleccionar la ruta donde se encuentran los archivos de lectura*/
//==================================================================================================

public class Archivos_Dispositivo extends AppCompatActivity {

    //Variables de la clase
    RecyclerView recyclerView;
    TextView noHayArchivos;
    String ruta;

    private Adapter_Archivos adapter;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_storage);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        try {
            //Asignamos cada variable con su elemento correspondiente
            recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
            noHayArchivos = (TextView) findViewById(R.id.text_noHayArchivos);

            noHayArchivos.setVisibility(View.INVISIBLE);    //Hacemos que el texto indicatio no se muestre en un inicio

            //Recivimos y guardamos la ruta enviada desde la activity "PopUp_RutaArchivos"
            ruta = getIntent().getStringExtra("ruta");

            File rutaArchivos = new File(ruta);         //Variable "File" que contiene la ruta donde se almacenan los archivos del dispositivo
            List<File> archivos = Arrays.asList(rutaArchivos.listFiles()); //Guardamos dichos archivos en una cadena de tipo "File"

            //En caso de que no haya archivos, mostramos el texto correspondiente
            if (archivos == null || archivos.size() == 0) {
                noHayArchivos.setVisibility(View.VISIBLE);
            }

            /*Establecemos un linearLayoutManager para el recyclerView y pasamos los datos de la clase y de la lista
             * de archivos a la lista del ArrayAdapter*/
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(new Adapter_Archivos(getApplicationContext(), archivos));
        }
        catch(Exception error){
            Toast.makeText(Archivos_Dispositivo.this, "No tiene permisos para acceder a esta carpeta", Toast.LENGTH_LONG).show();}
    }
}