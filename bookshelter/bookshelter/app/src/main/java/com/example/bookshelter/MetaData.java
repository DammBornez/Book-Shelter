package com.example.bookshelter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.bookshelter.DBLibros.DbLibros;
import com.example.bookshelter.Models.BookItem;
import com.example.bookshelter.Pantalla_Principal.Pantalla_Principal;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import androidx.appcompat.content.res.AppCompatResources;
import nl.siegmann.epublib.domain.Author;
import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.domain.Date;
import nl.siegmann.epublib.domain.Identifier;
import nl.siegmann.epublib.domain.Metadata;
import nl.siegmann.epublib.epub.EpubReader;

//==================================================================================================
//  Autor: Nicolás González Bórnez
//  Fecha: 24/10/2022
/*  Descripción: Clase que extrae y devuelve los metadatos del archivo .opf contenido dentro
*    del documento .epub y su portada*/
//==================================================================================================


public class MetaData
{
    //Atributos de la clase
    protected Context context;

    String autor = "", titulo = "", descripcion = "", idioma = "", editorial = "", categoria = "";
    String fecha = "";
    Bitmap portada;
    Uri uri;

    //Constructores de la clase
    public MetaData(Uri uri) throws IOException {
        this.uri = uri;
    }

    public MetaData(){}

    //Método que hace uso de la librería "EpubLib" para extraer el contenido del archivo de datos .opf
    public void readOpf() throws IOException {
        EpubReader lector = new EpubReader();
        Book libro = lector.readEpub(new FileInputStream(uri.getPath()));
        Metadata metadata = libro.getMetadata();

        try {
            List<String> titulo = metadata.getTitles();
            List<Author> autor = metadata.getAuthors();
            List<Date> fecha = metadata.getDates();
            String idioma = metadata.getLanguage();
            List<String> editorial = metadata.getPublishers();
            List<String> categoria = metadata.getSubjects();

            this.autor = autor.get(0).toString();
            this.titulo = titulo.get(0).toString();
            this.fecha = fecha.get(0).toString();
            this.idioma = idioma;
            this.editorial = editorial.get(0).toString();
            this.categoria = categoria.get(0).toString();
        }
        catch(Exception error){}
    }

    //Método que descomprime el documento .epub y extrae la portada del libro
    public void extraerPortada(Uri uri)
    {
        Bitmap cover = null;
        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();

        try
        {
            File zipFile = new File(uri.getPath());
            ZipInputStream zipInput = new ZipInputStream(new FileInputStream(zipFile));

            ZipEntry zEntry;
            while((zEntry = zipInput.getNextEntry()) != null)
            {
                if(zEntry.getName().endsWith(".jpg") || zEntry.getName().endsWith(".png")
                        || zEntry.getName().endsWith(".jpeg"))
                {
                    cover = BitmapFactory.decodeStream(zipInput, null, bitmapOptions);
                }
            }

            this.portada = cover;

            zipInput.close();
        }

        catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Método que inserta los datos del libro seleccionado en la base de datos
    public void insertarRegistro(Context context)
    {
        DbLibros db = new DbLibros(context);
        long id = db.agregarLibro(titulo, autor, fecha, idioma, editorial, categoria, portada, uri.getPath(), 0);

        if(id > 0){
            Toast.makeText(context, "EL LIBRO SE HA GUARDADO CORRECTAMENTE", Toast.LENGTH_LONG);
        }
        else{Toast.makeText(context, "HA OCURRIDO UN PROBLEMA AL GUARDAR EL LIBRO", Toast.LENGTH_LONG);}
    }
}
