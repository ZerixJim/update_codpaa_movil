package com.codpaa.activity;

/*
 * Created by grim on 3/02/16.
 */

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.codpaa.R;
import com.codpaa.adapter.PregustasRecyclerAdapter;
import com.codpaa.db.BDopenHelper;
import com.codpaa.model.Pregunta;

import com.codpaa.provider.DbEstructure.EncustaPreguntas;
import com.codpaa.update.EnviarDatos;
import com.github.clans.fab.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class EncuestaActivity extends AppCompatActivity  {


    RecyclerView recyclerView;
    PregustasRecyclerAdapter adapter;
    int idEncuesta;
    private int idPromotor;
    private int idTienda;
    private int tipoEncuesta;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_preguntas);
        toolbar = findViewById(R.id.toolbar_frentes);

        if(toolbar != null){
            setSupportActionBar(toolbar);
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null){
                actionBar.setDisplayHomeAsUpEnabled(true);

            }
        }

        Intent intent = getIntent();
        idEncuesta = intent.getIntExtra("idEncuesta", 0);
        idPromotor = intent.getIntExtra("idPromotor", 0);
        idTienda = intent.getIntExtra("idTienda", 0);
        tipoEncuesta = intent.getIntExtra("tipoEncuesta", 0);

        //Log.d("EncuestaActivity", "idEncuesta: "+idEncuesta);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);

        recyclerView =  findViewById(R.id.recycler_preguntas);
        if (recyclerView != null) {
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(linearLayoutManager);
        }

        recyclerView.setItemViewCacheSize(getPreguntas(idEncuesta).size());

        adapter = new PregustasRecyclerAdapter(getPreguntas(idEncuesta), this);

        recyclerView.setAdapter(adapter);



        if (tipoEncuesta > 0 && tipoEncuesta == 4){

            Log.d("TipoEncuesta" , "" + tipoEncuesta);

            FloatingActionButton fab = findViewById(R.id.menu_item_2);

            if (fab != null) {

                fab.setVisibility(View.VISIBLE);
                fab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getApplicationContext(), "Menu item 1", Toast.LENGTH_SHORT).show();

                        adapter.addItem(getPreguntas(idEncuesta));

                    }
                });
            }

        }





    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_encuesta, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        if (item.getItemId() == R.id.save_encuesta) {//Log.d("Save Encuesta", "Menu " + adapter.getItemCount());

            if (adapter.faltanPreguntas()) {
                Toast.makeText(this, "Contesta todas las preguntas", Toast.LENGTH_SHORT).show();
            } else {
                guardarRespuestas();

                EnviarDatos datos = new EnviarDatos(this);

                datos.sendEncuesta();
            }


            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void guardarRespuestas() {

        List<Pregunta> preguntas = adapter.getPreguntas();

        SQLiteDatabase db = new BDopenHelper(this).getWritableDatabase();
        ContentValues content = new ContentValues();

        for (Pregunta pre : preguntas){

            content.put(EncustaPreguntas.ID_PREGUNTA, pre.getIdPregunta());
            content.put(EncustaPreguntas.ID_ENCUESTA, pre.getIdEncuesta());
            content.put(EncustaPreguntas.ID_PROMOTOR, idPromotor);
            content.put(EncustaPreguntas.ID_TIENDA, idTienda);
            content.put(EncustaPreguntas.RESPUESTA, pre.getRespuesta());

            db.insert(EncustaPreguntas.TABLE_NAME, null ,content);

        }

        Toast.makeText(this, "Encuesta Guardada", Toast.LENGTH_SHORT).show();

        adapter.clearList();
        db.close();

    }

    private List<Pregunta> getPreguntas(int idEncuesta) {
        SQLiteDatabase db = new BDopenHelper(this).getReadableDatabase();
        List<Pregunta> array = new ArrayList<>();
        String sql = "select p.id_pregunta,p.descripcion as contenido, id_tipo, p.id_encuesta " +
                "from preguntas as p where p.id_encuesta=" + idEncuesta;

        Cursor cursor = db.rawQuery(sql, null);
        Log.d("EncuestaActivity", "Cursor: "+ cursor.getCount());
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()){
            final Pregunta pre = new Pregunta();
            int numero = cursor.getPosition() + 1;
            pre.setContenidoPregunta(cursor.getString(cursor.getColumnIndex("contenido")));
            pre.setIdPregunta(cursor.getInt(cursor.getColumnIndex("id_pregunta")));
            pre.setNumeroPregunta(numero);
            pre.setIdTipo(cursor.getInt(cursor.getColumnIndex("id_tipo")));
            pre.setIdEncuesta(cursor.getInt(cursor.getColumnIndex("id_encuesta")));
            array.add(pre);
        }

        cursor.close();
        db.close();

        return array;
    }


}
