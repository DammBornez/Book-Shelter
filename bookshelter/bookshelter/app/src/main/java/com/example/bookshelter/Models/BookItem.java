package com.example.bookshelter.Models;

import android.graphics.Bitmap;

import androidx.appcompat.app.AppCompatActivity;

//==================================================================================================
//  Autor: Nicolás González Bórnez
//  Fecha: 31/10/2022
/*  Clase recibe y devuelve los tipos de datos que se mostrarán el el GridVieww de Libros*/
//==================================================================================================

public class BookItem extends AppCompatActivity {

    //Atributos de la clase
    private int id;
    private String titulo;
    private String autor;
    private String fecha;
    private String idioma;
    private String editorial;
    private String categoria;
    private Bitmap portada;
    private int marca;

    //id
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    //Título
    public String getTitulo() {
        return titulo;
    }
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    //Autor
    public String getAutor() {
        return autor;
    }
    public void setAutor(String autor) {
        this.autor = autor;
    }

    //Fecha
    public String getFecha() {
        return fecha;
    }
    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    //Idioma
    public String getIdioma() {
        return idioma;
    }
    public void setIdioma(String idioma) {
        this.idioma = idioma;
    }

    //Editorial
    public String getEditorial() {
        return editorial;
    }
    public void setEditorial(String editorial) {
        this.editorial = editorial;
    }

    //Categorñia
    public String getCategoria() {
        return categoria;
    }
    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    //Portada
    public Bitmap getPortada() {
        return portada;
    }
    public void setPortada(Bitmap portada) {
        this.portada = portada;
    }

    //Marca
    public int getMarca() {
        return marca;
    }
    public void setMarca(int marca) {
        this.marca = marca;
    }
}