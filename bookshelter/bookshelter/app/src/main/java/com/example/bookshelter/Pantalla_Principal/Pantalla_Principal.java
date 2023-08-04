package com.example.bookshelter.Pantalla_Principal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.bookshelter.Adapters.Adapter_Grid;
import com.example.bookshelter.Archivos_Dispositivo;
import com.example.bookshelter.librosEtiquetados.BookMarks;
import com.example.bookshelter.DBLibros.DbLibros;
import com.example.bookshelter.DBLibros.Libros_DbHelper;
import com.example.bookshelter.InfoLibros.Detalles_libros;
import com.example.bookshelter.BookReader.EpubReader;
import com.example.bookshelter.Models.BookItem;
import com.example.bookshelter.R;

import java.util.ArrayList;


//==================================================================================================
//  Autor: Nicolás González Bórnez
//  Fecha: 15/10/2022
/*  Descripción: Clase donde se mostrarán los libros seleccionados en una lista tipo gridView y
*   desde donde se podrá acceder al perfil del ususario y buscar más libros*/
//==================================================================================================

public class Pantalla_Principal extends AppCompatActivity implements SearchView.OnQueryTextListener {

    GridView gridview;
    SearchView buscador;
    ArrayList<BookItem> listaLibros = new ArrayList<>();
    EpubReader epubReader;
    Adapter_Grid adapter;
    ImageButton imgbtnFiltro;

    PopupMenu popup;
    ArrayList<String> listaCategorias;
    String categoria = "";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_principal);

        gridview = (GridView) findViewById(R.id.GridViewLibros);

        buscador = (SearchView) findViewById(R.id.SearchView_buscador);

        imgbtnFiltro = (ImageButton) findViewById(R.id.imageButton_filtro);

        //Llamada al método que crea la base de datos de libros
        createDataBase();

        //Llamada al método que muestra los libros guardados en la base de datos en el gridview
        mostrarLibrosGrid();

        //Método que crea e inicializa el popupMenu de los filtros
        crearPopupMenu();

        //Método que abre la ventana del lectura del libro seleccionado
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                int id = listaLibros.get(i).getId();

                DbLibros db = new DbLibros(Pantalla_Principal.this);
                String ruta = db.getRutaLibro(id);

                Uri uriLibro = Uri.parse(ruta);
                epubReader = new EpubReader(Pantalla_Principal.this);
                epubReader.leerDocumento(uriLibro);
            }
        });

        gridview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(Pantalla_Principal.this, Detalles_libros.class);

                intent.putExtra("id", listaLibros.get(i).getId());
                intent.putExtra("titulo", listaLibros.get(i).getTitulo());
                intent.putExtra("autor", listaLibros.get(i).getAutor());
                intent.putExtra("fecha", listaLibros.get(i).getFecha());
                intent.putExtra("idioma", listaLibros.get(i).getIdioma());
                intent.putExtra("editorial", listaLibros.get(i).getEditorial());
                intent.putExtra("categoria", listaLibros.get(i).getCategoria());
                intent.putExtra("marca", listaLibros.get(i).getMarca());

                startActivity(intent);

                return true;
            }
        });

        buscador.setOnQueryTextListener(this);

        imgbtnFiltro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popup.show();
            }
        });

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {

                int id;

                if(!menuItem.getTitle().equals("Todos"))
                {
                    adapter.filtracion(menuItem.getTitle().toString(), true);
                }
                else{
                    adapter.filtracion("", true);}

                return false;
            }
        });
    }

    private void crearPopupMenu()
    {
        popup = new PopupMenu(Pantalla_Principal.this, imgbtnFiltro);
        int i = 1;

        DbLibros db = new DbLibros(Pantalla_Principal.this);
        listaCategorias = db.getListaCategorias();

        popup.getMenu().add(Menu.NONE, 0, 0, "Todos");

        for(String categoria: listaCategorias)
        {
            if(categoria.length() > 0)
            {
                popup.getMenu().add(Menu.NONE, i, i, categoria);
            }
        }
    }

    private void agregarLibro()
    {
        //En caso de que el usuario haya aceptado los permisos
        if(verificarPermisos())
        {
            //Llamamos a la activity que muestra los archivos del dispositivo
            Intent intent = new Intent(Pantalla_Principal.this, Archivos_Dispositivo.class);

            //Guardamos la ruta en una variable y se la pasamos a la nueva activity
            String ruta = Environment.getExternalStorageDirectory().getPath();
            intent.putExtra("ruta", ruta);

            //Iniciamos la activity
            startActivity(intent);
        }

        //En caso de que no se hayan aceptado los permisos
        else
        {
            permisos();
        }
    }
    //--------------------------------------------------------------------
    //PERMISOS DE LA APLICACIÓN
    //--------------------------------------------------------------------

    //Método que valida si el usuario ha aceptado o no la petición del permiso y actua en consecuencia
    private void permisos()
    {
        /*La función "shouldShowRequestPermissionRationale" se encarga de devolver un valor booleano "true" en caso de que la app
         * haya lanzado la petición del permiso y el usuario la haya denagado*/
        if(!ActivityCompat.shouldShowRequestPermissionRationale(Pantalla_Principal.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) ||
                !ActivityCompat.shouldShowRequestPermissionRationale(Pantalla_Principal.this, Manifest.permission.READ_EXTERNAL_STORAGE))
        {
            //En caso de que la haya aceptado, pedimos el permiso
            ActivityCompat.requestPermissions(Pantalla_Principal.this, new String[]
                    {
                            Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE
                    }, 123);
        }
    }

    //Método que valida si el usuario ya otorgó los permisos necesarios
    private boolean verificarPermisos()
    {
        //Validamos si los permisos de "MANAGE_EXTERNAL_STORAGE" han sido concedidos
        if(Environment.isExternalStorageManager())
        {
            return true;
        }

        //En caso de que no sea así, los pedimos
        else
        {
            //Intent con la ventana que habilita el MANAGE_EXTERNAL_STORAGE
            Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
            Uri uri = Uri.fromParts("package", getPackageName(), null);
            intent.setData(uri);
            startActivity(intent);
            return false;
        }
    }

    //--------------------------------------------------------------------
    //MÉTODOS QUE TRABAJAN CON LA BASE DE DATOS
    //--------------------------------------------------------------------

    //Método encargado de crear la base de datos que guarda los datos de los libro seleccionados
    private void createDataBase()
    {
        Libros_DbHelper dbHelper = new Libros_DbHelper(Pantalla_Principal.this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        if(db != null){
            Toast.makeText(Pantalla_Principal.this, "SE HA CREADO LA BASE DE DATOS", Toast.LENGTH_LONG);

        }
        else{
            Toast.makeText(Pantalla_Principal.this, "HA OCURRIDO UN  ERROR CON LA CREACIÓN DE LA BASE DE DATOS", Toast.LENGTH_LONG);

        }
    }

    //Método que muestra los libros guardados en la base de datos en el gridview principal
    private void mostrarLibrosGrid()
    {
        DbLibros db = new DbLibros(Pantalla_Principal.this);
        listaLibros = db.mostrarLibros();

        adapter = new Adapter_Grid(Pantalla_Principal.this, listaLibros);
        gridview.setAdapter(adapter);
    }

    //--------------------------------------------------------------------
    //MÉTODOS DEL MENÚ DEL ACTION BAR
    //--------------------------------------------------------------------

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_principal, menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.menuItem_addBook:
                agregarLibro();
                break;

            case R.id.menuItem_bookmarks:
                Intent intent = new Intent(Pantalla_Principal.this, BookMarks.class);
                startActivity(intent);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        adapter.filtracion(s, false);
        return false;
    }
}