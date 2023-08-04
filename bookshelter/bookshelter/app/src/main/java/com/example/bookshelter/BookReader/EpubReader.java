package com.example.bookshelter.BookReader;

import android.content.Context;
import android.net.Uri;
import android.widget.Toast;

import com.folioreader.FolioReader;

import java.io.File;

import androidx.annotation.NonNull;

//==================================================================================================
//  Autor: Nicolás González Bórnez
//  Fecha: 19/10/2022
/*  Descripción: Clase encargada de llamar a la clase FolioReader y generar la activity para
*   la lectura de los archivos epub, además de berificar si la ruta recibida existe*/
//==================================================================================================

public class EpubReader
{
    //Atributos de la clase
    Context context;

    //Constructor de la clase que recibe el contexto de la clase que lo llamó
    public EpubReader(Context context){
        this.context = context;}

    //Método encargado de llamar a la clase FolioReader y pasarle la ruta del archivo
    public void leerDocumento(@NonNull Uri uri)
    {
        //Verificamos si la ruta recibida existe
        if((new File(uri.getPath())).exists()){

            //Llamamos a la clase que lee el documento y le pasamos la ruta/uri del mismo
            FolioReader fr = FolioReader.get();
            fr.openBook(uri.getPath());
        }
        else{
            //En caso de que no exista, se le indica al usuario
            Toast.makeText(context, "El libro ha sido eliminado o se ha cambiado de directorio", Toast.LENGTH_LONG).show();
        }
    }
}
