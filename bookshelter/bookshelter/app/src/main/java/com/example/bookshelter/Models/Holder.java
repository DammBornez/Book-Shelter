package com.example.bookshelter.Models;

//==================================================================================================
//  Autor: Nicolás González Bórnez
//  Fecha: 01/09/2022
/*  Clase "Holder" que hace uso del patrón "ViewHolder" con el que conseguimos mayor eficiencia
 *   a la hora de mostar los datos de la lista.
 *   Las listas de tipo "RecyclerView" poseen un adaptador que viene por defecto con este patrón*/
//==================================================================================================

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bookshelter.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public  class Holder extends RecyclerView.ViewHolder
{
    //Atributos de la clase
    public ImageView icon;
    public TextView archivo;

    public Holder(@NonNull View itemView) {
        super(itemView);

        //Relacionamos cada atributo con su correspondiente elemento
        icon = itemView.findViewById(R.id.image_icon);
        archivo = itemView.findViewById(R.id.text_storage);
    }
}
