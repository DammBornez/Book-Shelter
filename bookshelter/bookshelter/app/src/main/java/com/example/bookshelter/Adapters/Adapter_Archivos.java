package com.example.bookshelter.Adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.bookshelter.Archivos_Dispositivo;
import com.example.bookshelter.BookReader.EpubReader;
import com.example.bookshelter.MetaData;
import com.example.bookshelter.Models.Holder;
import com.example.bookshelter.R;

import java.io.File;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

//==================================================================================================
//  Autor: Nicolás González Bórnez
//  Fecha: 01/09/2022
/*  Activity que se encarga de gestionar la apariencia y los datos que se van a mostrar en el
*   listado de los archivos del dispositivo*/
//==================================================================================================

//El adaptador utiliza una lista tipo "RecyclerView" para mostrar los archivos
public class Adapter_Archivos extends  RecyclerView.Adapter<Holder>
{
    //Atributos de la clase
    protected Context context;
    protected List<File> archivos_count;

    //Constructor de la clase
    public Adapter_Archivos(Context context, List<File> archivos)
    {
        //super(context, R.layout.activity_archivos_dispositivo, archivos);
        this.context = context;
        this.archivos_count = archivos;
    }

    //--------------------------------------------------------------------
    //MÉTODOS DEL ADAPTADOR
    //--------------------------------------------------------------------

    //Método que le pasa al "RecyclerView" los elementos que este va a tener
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        //Almacenamos los elementos de la vista ("storage_item") en una variable tipo "View"
        View view = LayoutInflater.from(context).inflate(R.layout.storage_item, parent, false);

        //Devolvemos un objeto de la clase "Holder" con la vista de la lista
        return new Holder(view);
    }

    //Método encargado de mostrar los datos en la lista
    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position)
    {
        //Almacenamos el nombre del archivo seleccionado y lo mostramos en la lista
        File archivo_seleccionado = archivos_count.get(position);
        holder.archivo.setText(archivo_seleccionado.getName());
        holder.archivo.setSelected(true);


        //Se mostrará un icono diferente si se muestra una carpeta o un archivo
        if(archivo_seleccionado.isDirectory())
        {
            holder.icon.setImageResource(R.drawable.icon_folder); //Icono de la carpeta
        }
        else{ holder.icon.setImageResource(R.drawable.icon_file_drive);}    //Icono del archivo

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /*En caso de que el elemento de la lista sea una carpeta/directorio
                * abrimos este y mostramos su contenido*/
                if(archivo_seleccionado.isDirectory())
                {
                    Intent intent = new Intent(context, Archivos_Dispositivo.class);

                    //Guardamos la ruta de la carpeta seleccionada
                    String ruta = archivo_seleccionado.getAbsolutePath();
                    intent.putExtra("ruta", ruta);

                    //Volvemos a llamar al activity, pero con el contenido de la nueva carpeta
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }

                //En caso de que el elemento sea un archivo, tratará de abrirlo
                else
                {
                    //Bloque de instrucciones que puede dar a error
                    try
                    {
                        //Guardamos la ruta del archivo
                        Uri uri = Uri.parse(archivo_seleccionado.getAbsolutePath());

                        //Validamos que el documento seleccionado sea un documento epub
                        //en caso de serlo...
                        if(uri.getPath().endsWith(".epub"))
                        {
                            //Recogemos la información del documento
                            MetaData meta = new MetaData(uri);
                            meta.readOpf(); //Leemos el contenido de los metadatos
                            meta.extraerPortada(uri);   //Obtenemos la portada del libro
                            meta.insertarRegistro(context); //Guardamos la información en la base de datos

                            //Abrimos el documento
                            EpubReader epubReader = new EpubReader(context);
                            epubReader.leerDocumento(uri);
                        }

                        //En caso de no serlo, mostramos un mensaje indicando al usuario esto mismo
                        else{
                            Toast.makeText(context.getApplicationContext(),"Solo se permiten archivos .Epub", Toast.LENGTH_SHORT).show();
                        }
                    }

                    //En caso de error
                    catch(Exception error)
                    {
                        Toast.makeText(context.getApplicationContext(),"NO SE HA PODIDO ABRIR EL ARCHIVO SELECCIONADO", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    //Método que devuelve la cantidad de elementos que va a tener la lista
    @Override
    public int getItemCount()
    {
        /*En este caso, devuelve la cantidad de archivos que hay en el dispositivo
        * y que ya guardamos anteriormente en la variable "archivos"*/
        return archivos_count.size();
    }
}
