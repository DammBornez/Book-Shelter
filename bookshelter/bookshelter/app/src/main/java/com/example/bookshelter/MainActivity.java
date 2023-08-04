package com.example.bookshelter;

import android.content.Intent;
import android.os.Bundle;

import com.example.bookshelter.Pantalla_Principal.Pantalla_Principal;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Handler;
import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

//==================================================================================================
//  Autor: Nicolás González Bórnez
//  Fecha: 29/09/2022
/*  Descripción: Ventana de iniciuo del programa que da paso a el resto de funcionalidades de
    la aplicación*/
//==================================================================================================

public class MainActivity extends AppCompatActivity {

    //Variable que verifica si ya hemos entrado en la aplicación
    Boolean acceso = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //En caso de que sea la primera vez
        //Se mostrará la ventana que nos pedirá la ruta de lectura de los archivos
        if(!acceso)
        {
            //El uso de la clase "Handler" permite crear un efecto retardante en la apariciàon de las ventanas
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(MainActivity.this, Pantalla_Principal.class));
                }
                //En este caso, el retardo es de 1 segundo
            }, 1000);
        }
    }
}