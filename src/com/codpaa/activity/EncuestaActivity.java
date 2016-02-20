package com.codpaa.activity;

/*
 * Created by grim on 3/02/16.
 */

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.codpaa.R;
import com.codpaa.adapter.PregustasRecyclerAdapter;
import com.codpaa.db.BDopenHelper;
import com.codpaa.model.Pregunta;

import java.util.ArrayList;
import java.util.List;

public class EncuestaActivity extends AppCompatActivity  {


    RecyclerView recyclerView;
    PregustasRecyclerAdapter adapter;
    int idEncuesta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_preguntas);

        Intent intent = getIntent();
        idEncuesta = intent.getIntExtra("idEncuesta", 0);

        //Log.d("EncuestaActivity", "idEncuesta: "+idEncuesta);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_preguntas);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);


        adapter = new PregustasRecyclerAdapter(getPreguntas(idEncuesta), this);

        recyclerView.setAdapter(adapter);

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
        switch (item.getItemId()) {


            case R.id.save_encuesta:

                //Log.d("Save Encuesta", "Menu " + adapter.getItemCount());
                if(adapter.getRespuestaFaltante()){
                    Toast.makeText(this, "Faltan preguntas por contestar", Toast.LENGTH_SHORT).show();
                }
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private List<Pregunta> getPreguntas(int idEncuesta) {
        SQLiteDatabase db = new BDopenHelper(this).getReadableDatabase();
        List<Pregunta> array = new ArrayList<>();
        String sql = "select p.id_pregunta,p.descripcion as contenido " +
                "from preguntas as p where p.id_encuesta=" + idEncuesta;

        Cursor cursor = db.rawQuery(sql, null);
        Log.d("EncuestaActivity", "Cursor: "+ cursor.getCount());
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()){
            final Pregunta pre = new Pregunta();
            int numero = cursor.getPosition() + 1;
            pre.setContenidoPregunta(cursor.getString(cursor.getColumnIndex("contenido")));
            pre.setIdPregunta(cursor.getInt(cursor.getColumnIndex("id_pregunta")));
            pre.setNumeroPregunta(numero);
            array.add(pre);
        }

        cursor.close();
        db.close();

        return array;
    }


}
