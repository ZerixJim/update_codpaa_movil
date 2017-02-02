package com.codpaa.activity;

/*
 * Created by grim on 06/07/2016.
 */

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.codpaa.R;
import com.codpaa.adapter.MaterialSpinnerAdapter;
import com.codpaa.adapter.MaterialesSolicitudAdapter;
import com.codpaa.db.BDopenHelper;
import com.codpaa.fragment.DialogMaterialRequest;
import com.codpaa.model.MaterialModel;
import com.codpaa.widget.DividerItemDecoration;


import java.util.ArrayList;
import java.util.List;

public class MaterialesActivity extends AppCompatActivity implements View.OnClickListener, DialogMaterialRequest.SelectMaterial {


    RecyclerView recyclerMateriales;
    TextView txtVacio;
    List<MaterialModel> materialList = new ArrayList<>();
    FloatingActionButton floatingActionButton;

    private int idTienda, idPromor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_materiales);

        idTienda = getIntent().getIntExtra("idTienda", 0);
        idPromor = getIntent().getIntExtra("idPromotor", 0);


        // instaciar todos los widgets
        setUpWidgets();

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }




    }



    private List<MaterialModel> getMaterials() {
        List<MaterialModel> array = new ArrayList<>();

        SQLiteDatabase db = new BDopenHelper(this).getReadableDatabase();
        String sql = "select idMaterial, material,  unidad, solicitudMaxima, tipo_material from materiales order by material asc";


        Cursor cursor = db.rawQuery(sql, null);

        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()){
            final MaterialModel material = new MaterialModel();

            material.setNombreMaterial(cursor.getString(cursor.getColumnIndex("material")));

            material.setIdMaterial(cursor.getInt(cursor.getColumnIndex("idMaterial")));
            material.setIdTipoMaterial(cursor.getInt(cursor.getColumnIndex("tipo_material")));
            material.setUnidad(cursor.getString(cursor.getColumnIndex("unidad")));
            material.setSolicitudMaxima(cursor.getInt(cursor.getColumnIndex("solicitudMaxima")));

            array.add(material);
        }

        final MaterialModel materialDefault = new MaterialModel();
        materialDefault.setNombreMaterial("Selecciona Material");
        materialDefault.setIdMaterial(0);

        array.add(0, materialDefault);


        cursor.close();
        return array;
    }





    private void addItem(MaterialModel item){

        materialList.add(item);

        setUpRecyclerView();
    }


    private void setUpRecyclerView() {

        MaterialesSolicitudAdapter adapter = new MaterialesSolicitudAdapter(materialList);
        recyclerMateriales.setAdapter(adapter);
        recyclerMateriales.addItemDecoration(new DividerItemDecoration(this, null));

        if (materialList.size() > 0){
            txtVacio.setVisibility(View.INVISIBLE);
        } else {
            txtVacio.setVisibility(View.VISIBLE);
        }



    }

    private void setUpWidgets() {

        floatingActionButton = (FloatingActionButton) findViewById(R.id.float_add);
        txtVacio = (TextView) findViewById(R.id.txt_mensaje_vacio);

        if (floatingActionButton != null) {
            floatingActionButton.setOnClickListener(this);
        }


        recyclerMateriales = (RecyclerView) findViewById(R.id.recycler_material);

        if (recyclerMateriales != null){
            LinearLayoutManager linear = new LinearLayoutManager(this);

            recyclerMateriales.setLayoutManager(linear);



        }


    }


    private void showDialog(){
        FragmentManager ft = getSupportFragmentManager();

        // Create and show the dialog.
        DialogMaterialRequest newFragment = DialogMaterialRequest.newInstance();
        Bundle args = new Bundle();
        args.putInt("idPromotor", idPromor);
        args.putInt("idTienda", idTienda);
        newFragment.setArguments(args);
        newFragment.setOnMaterialListener(this);
        newFragment.show(ft, "dialog");
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_materiales, menu);




        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
    }



    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.float_add:

                showDialog();

                break;
        }
    }

    @Override
    public void onAddMaterial(MaterialModel model) {
        addItem(model);
    }

    @Override
    public void onTestId(int idMaterial) {



    }
}
