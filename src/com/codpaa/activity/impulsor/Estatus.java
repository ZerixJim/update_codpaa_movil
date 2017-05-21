package com.codpaa.activity.impulsor;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.codpaa.R;
import com.codpaa.adapter.MarcasAdapter;
import com.codpaa.adapter.generic.ProductoRecyclerAdapter;
import com.codpaa.db.BDopenHelper;
import com.codpaa.model.MarcaModel;
import com.codpaa.model.SpinnerProductoModel;
import com.codpaa.model.generic.Producto;

import java.util.ArrayList;

/*
 * Created by grim on 18/05/2017.
 */

public class Estatus extends AppCompatActivity implements AdapterView.OnItemSelectedListener {


    private RecyclerView mRecyclerView;
    private int idPromotor, idTienda;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estatus_impulsor);


        idPromotor = getIntent().getIntExtra("idPromotor",0);
        idTienda = getIntent().getIntExtra("idTienda", 0);


        mRecyclerView = (RecyclerView) findViewById(R.id.rectycle_estatus);
        if (mRecyclerView != null){

            mRecyclerView.setHasFixedSize(true);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);

            mRecyclerView.setLayoutManager(linearLayoutManager);



        }

        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null){


            actionBar.setDisplayHomeAsUpEnabled(true);


        }




    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_estatus, menu);

        MenuItem item = menu.findItem(R.id.spinner_marca_estatus);

        Spinner spinner = (Spinner) MenuItemCompat.getActionView(item);

        if(spinner != null){
            MarcasAdapter adapter = new MarcasAdapter(this,
                    android.R.layout.simple_spinner_item, getArrayList());



            spinner.setAdapter(adapter);

            spinner.setOnItemSelectedListener(this);


        }else {
            Log.d("Estatus", "spinner null");
        }





        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    private ArrayList<MarcaModel> getArrayList(){

        ArrayList<MarcaModel> array = new ArrayList<>();

        SQLiteDatabase base = new BDopenHelper(this).getReadableDatabase();
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


    private ArrayList<Producto> getArrayListProByTienda(int idMarca, int idTienda){

        Cursor curProByTienda = new BDopenHelper(this).getProductosByTienda(idMarca, idTienda);
        ArrayList<Producto> arrayP = new ArrayList<>();
        if (curProByTienda.getCount() <= 0){

            Cursor curPro = new BDopenHelper(this).productos(idMarca);

            for(curPro.moveToFirst(); !curPro.isAfterLast(); curPro.moveToNext()){
                final Producto spP = new Producto();
                spP.setIdProducto(curPro.getInt(0));
                spP.setNombre(curPro.getString(1));
                spP.setPresentacion(curPro.getString(2));
                spP.setCodeBarras(curPro.getString(3));
                spP.setIdMarca(curPro.getInt(4));
                arrayP.add(spP);
            }


            curPro.close();
        } else {

            for(curProByTienda.moveToFirst(); !curProByTienda.isAfterLast(); curProByTienda.moveToNext()){
                final Producto spP = new Producto();
                spP.setIdProducto(curProByTienda.getInt(0));
                spP.setNombre(curProByTienda.getString(1));
                spP.setPresentacion(curProByTienda.getString(2));
                spP.setCodeBarras(curProByTienda.getString(3));
                spP.setIdMarca(curProByTienda.getInt(4));
                arrayP.add(spP);
            }

        }


        curProByTienda.close();

        return arrayP;

    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        MarcaModel spm = (MarcaModel)  parent.getItemAtPosition(position);

        int idMarca = spm.getId();

        loadProducto(idMarca);


    }


    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }




    private void loadProducto(int idMarca){

        try{

            ArrayList<Producto> productos = getArrayListProByTienda(idMarca, idTienda);

            int size = productos.size();

            ProductoRecyclerAdapter adapter = new ProductoRecyclerAdapter(this, productos);

            mRecyclerView.setAdapter(adapter);
            mRecyclerView.setItemViewCacheSize(size);

        }catch (Exception e){
            e.printStackTrace();
        }



    }





}
