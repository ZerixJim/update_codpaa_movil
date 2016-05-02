package com.codpaa.activity;/*
 * Created by grim on 27/04/2016.
 */

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Spinner;

import com.codpaa.R;
import com.codpaa.adapter.MarcasAdapter;
import com.codpaa.db.BDopenHelper;
import com.codpaa.model.MarcaModel;

import java.util.ArrayList;

public class VentaPromedio extends AppCompatActivity{

    private Spinner spinnerMarca;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_venta_promedio);

        loadSpinner();







    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }



    private void loadSpinner(){
        spinnerMarca = (Spinner) findViewById(R.id.spinner);

        MarcasAdapter marcasAdapter = new MarcasAdapter(this,android.R.layout.simple_spinner_item, getArrayList());
        spinnerMarca.setAdapter(marcasAdapter);

    }

    //TODO: implementar el envio

    private ArrayList<MarcaModel> getArrayList(){

        SQLiteDatabase base = new BDopenHelper(this).getReadableDatabase();
        ArrayList<MarcaModel> array = new ArrayList<>();
        String sql = "select idMarca, nombre, img from marca order by nombre asc;";
        Cursor cursorMarca = base.rawQuery(sql, null);

        for(cursorMarca.moveToFirst(); !cursorMarca.isAfterLast(); cursorMarca.moveToNext()){

            final MarcaModel spiM = new MarcaModel();
            spiM.setNombre(cursorMarca.getString(1));
            spiM.setId(cursorMarca.getInt(0));
            spiM.setUrl(cursorMarca.getString(2));

            array.add(spiM);
        }

        final MarcaModel spiMfirst = new MarcaModel();
        spiMfirst.setNombre("Selecciona Marca");
        spiMfirst.setId(0);

        array.add(0,spiMfirst);

        cursorMarca.close();
        base.close();
        return array;

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_venta_promedio, menu);


        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {


            case android.R.id.home:
                finish();
                return true;

            case R.id.save_venta:


                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
