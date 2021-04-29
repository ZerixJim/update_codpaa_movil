package com.codpaa.activity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;


import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.codpaa.R;
import com.codpaa.adapter.AgotadosRecyclerAdapter;
import com.codpaa.adapter.MarcasAdapter;
import com.codpaa.db.BDopenHelper;
import com.codpaa.model.MarcaModel;
import com.codpaa.model.ProductosModel;
import com.codpaa.widget.SingleSpinnerSelect;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class Agotados extends AppCompatActivity implements AdapterView.OnItemSelectedListener {


    private int idPromotor, idTienda;
    private Spinner spinnerMarca;
    private RecyclerView recyclerView;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.agotados_layout);



        Toolbar toolbar = findViewById(R.id.toolbar_agotados);

        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);

        }



        idPromotor = getIntent().getIntExtra("idPromotor", 0);
        idTienda = getIntent().getIntExtra("idTienda", 0);







        recyclerView = findViewById(R.id.recycler_producto);
        //recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        spinnerMarca = findViewById(R.id.marca);
        spinnerMarca.setOnItemSelectedListener(this);

        //load spinner marca
        // load first
        loadSpinner();



    }


    private void loadSpinner(){


        MarcasAdapter adapter = new MarcasAdapter(this,
                android.R.layout.simple_spinner_item, getBrands());

        spinnerMarca.setAdapter(adapter);



    }


    private void loadProductsByMarca(int idMarca){

        ArrayList<ProductosModel> list = getProducts(idMarca);

        AgotadosRecyclerAdapter adapter = new AgotadosRecyclerAdapter(list);

        recyclerView.setItemViewCacheSize(list.size());
        recyclerView.setAdapter(adapter);


    }


    private ArrayList<MarcaModel> getBrands(){
        ArrayList<MarcaModel> array = new ArrayList<>();

        SQLiteDatabase bd = new BDopenHelper(this).getReadableDatabase();
        String sql = "select m.nombre, m.idMarca  from marca m " +
                " left join producto p on (p.idMarca = m.idMarca ) " +
                " where p.agotado = 1" +
                " group by m.idMarca ";
        Cursor cursorMarca = bd.rawQuery(sql, null);

        for(cursorMarca.moveToFirst(); !cursorMarca.isAfterLast(); cursorMarca.moveToNext()){

            final MarcaModel spiM = new MarcaModel();
            spiM.setNombre(cursorMarca.getString(cursorMarca.getColumnIndex("nombre")));
            spiM.setId(cursorMarca.getInt(cursorMarca.getColumnIndex("idMarca")));

            array.add(spiM);
        }

        final MarcaModel spiMfirst = new MarcaModel();
        spiMfirst.setNombre("Selecciona Marca");
        spiMfirst.setId(0);

        array.add(0,spiMfirst);

        cursorMarca.close();
        bd.close();


        return array;

    }



    private ArrayList<ProductosModel> getProducts(int idMarca){
        ArrayList<ProductosModel> array = new ArrayList<>();

        SQLiteDatabase bd = new BDopenHelper(this).getReadableDatabase();
        String sql = "select p.idProducto, p.nombre, p.presentacion, p.idMarca, p.cb  from producto p " +
                " where p.idMarca = " + idMarca + " and p.agotado = 1 ";
        Cursor curProdu = bd.rawQuery(sql, null);

        for(curProdu.moveToFirst(); !curProdu.isAfterLast(); curProdu.moveToNext()){

            final ProductosModel pro = new ProductosModel();

            pro.setIdProducto(curProdu.getInt(curProdu.getColumnIndex("idProducto")));
            pro.setNombre(curProdu.getString(curProdu.getColumnIndex("nombre")));
            pro.setPresentacion(curProdu.getString(curProdu.getColumnIndex("presentacion")));
            pro.setIdMarca(curProdu.getInt(curProdu.getColumnIndex("idMarca")));
            pro.setCodigoBarras(curProdu.getString(curProdu.getColumnIndex("cb")));

            array.add(pro);
        }
/*
        final ProductosModel spiMfirst = new ProductosModel();
        spiMfirst.setNombre("Selecciona Marca");
        spiMfirst.setIdProducto(0);

        array.add(0,spiMfirst);*/

        curProdu.close();
        bd.close();


        return array;
    }



    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    protected void onStart() {
        super.onStart();
    }


    @Override
    protected void onPause() {
        super.onPause();
    }


    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        MarcaModel marca = (MarcaModel) adapterView.getSelectedItem();

        loadProductsByMarca(marca.getId());


    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
