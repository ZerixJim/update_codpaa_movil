/*
 * Created by grim on 23/08/2016.
 */
package com.codpaa.activity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;

import com.codpaa.R;
import com.codpaa.adapter.MarcasAdapter;
import com.codpaa.adapter.RecyclerFrentesAdapter;
import com.codpaa.db.BDopenHelper;
import com.codpaa.model.MarcaModel;
import com.codpaa.model.SpinnerProductoModel;

import java.util.ArrayList;

import java.util.Locale;


public class CapturaGeneral extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    int idPromotor, idTienda;

    InputMethodManager im;
    BDopenHelper baseH;
    Toolbar toolbar;
    Locale locale;
    RecyclerView recyclerView;


    ArrayList<MarcaModel> array = new ArrayList<>();
    SQLiteDatabase base;
    Spinner spiMarca;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_captura_general);

        toolbar = (Toolbar) findViewById(R.id.toolbar);

        if(toolbar != null){
            setSupportActionBar(toolbar);
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null){
                actionBar.setDisplayHomeAsUpEnabled(true);

            }
        }

        locale = new Locale("es_MX");

        Intent i = getIntent();
        idTienda = (Integer) i.getExtras().get("idTienda");
        idPromotor = (Integer) i.getExtras().get("idPromotor");

        im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);


        spiMarca = (Spinner) findViewById(R.id.spinner);



        recyclerView = (RecyclerView) findViewById(R.id.recycler);


        spiMarca.setOnItemSelectedListener(this);


        try {

            baseH = new BDopenHelper(this);

            try {
                Cursor cTienda = baseH.tienda(idTienda);
                cTienda.moveToFirst();
                if(getSupportActionBar() != null){
                    getSupportActionBar().setSubtitle(cTienda.getString(0)+" "+cTienda.getString(1));
                }
                cTienda.close();
                try {

                    loadSpinner();


                }catch(Exception e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Error Frentes 3", Toast.LENGTH_SHORT).show();
                }

            }catch(Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "Error Frentes 2", Toast.LENGTH_SHORT).show();
            }


        }catch(Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error Frentes 1", Toast.LENGTH_SHORT).show();

        }


        /**
         * keyboard soft hide
         */
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);



    }



    @Override
    protected void onPause() {
        super.onPause();


    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {


            case android.R.id.home:
                this.finish();
                return true;

            case R.id.save_frentes:

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_frentes, menu);


        return true;
    }

    @Override
    public void onClick(View v) {

    }





    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int id, long arg3) {

        MarcaModel spm = (MarcaModel)  spiMarca.getSelectedItem();


        int idMarca = spm.getId();
        loadSpinnerProd(idMarca);

    }



    @Override
    public void onNothingSelected(AdapterView<?> arg0) {


    }

    private void loadSpinner(){
        try {


            MarcasAdapter adapter = new MarcasAdapter(this,
                    android.R.layout.simple_spinner_item, getArrayList());
            spiMarca.setAdapter(adapter);

        }catch(Exception e) {
            Toast.makeText(this, "Error Mayoreo 3", Toast.LENGTH_SHORT).show();
        }

    }


    private void loadSpinnerProd(int idM){

        try{

            RecyclerFrentesAdapter adapter = new RecyclerFrentesAdapter(this, getArrayListProByTiensda(idM, idTienda));
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.setHasFixedSize(true);
            recyclerView.setAdapter(adapter);
        }catch (Exception e){
            e.printStackTrace();
        }


    }


    private ArrayList<SpinnerProductoModel> getArrayListProByTiensda(int idMarca, int idTienda){

        Cursor curProByTienda = new BDopenHelper(this).productosByTienda(idMarca, idTienda);
        ArrayList<SpinnerProductoModel> arrayP = new ArrayList<>();
        if (curProByTienda.getCount() <= 0){

            Cursor curPro = new BDopenHelper(this).productos(idMarca);

            for(curPro.moveToFirst(); !curPro.isAfterLast(); curPro.moveToNext()){
                final SpinnerProductoModel spP = new SpinnerProductoModel();
                spP.setIdProducto(curPro.getInt(0));
                spP.setNombre(curPro.getString(1));
                spP.setPresentacion(curPro.getString(2));
                spP.setCodigoBarras(curPro.getString(3));
                spP.setIdMarca(curPro.getInt(4));
                arrayP.add(spP);
            }


            curPro.close();
        } else {

            for(curProByTienda.moveToFirst(); !curProByTienda.isAfterLast(); curProByTienda.moveToNext()){
                final SpinnerProductoModel spP = new SpinnerProductoModel();
                spP.setIdProducto(curProByTienda.getInt(0));
                spP.setNombre(curProByTienda.getString(1));
                spP.setPresentacion(curProByTienda.getString(2));
                spP.setCodigoBarras(curProByTienda.getString(3));
                spP.setIdMarca(curProByTienda.getInt(4));
                arrayP.add(spP);
            }

        }



        final SpinnerProductoModel spPinicio = new SpinnerProductoModel();
        spPinicio.setIdProducto(0);
        spPinicio.setNombre("Seleccione Producto");
        spPinicio.setPresentacion("producto sin seleccionar");
        spPinicio.setCodigoBarras(" ");

        arrayP.add(0,spPinicio);


        base.close();
        return arrayP;

    }



    private ArrayList<MarcaModel> getArrayList(){

        base = new BDopenHelper(this).getReadableDatabase();
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
}
