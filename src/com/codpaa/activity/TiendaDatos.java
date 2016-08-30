package com.codpaa.activity;
/*
 * Created by grim on 29/08/2016.
 */

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.codpaa.R;
import com.codpaa.adapter.MarcasAdapter;
import com.codpaa.adapter.RecyclerProductosMultiSelect;
import com.codpaa.db.BDopenHelper;
import com.codpaa.model.JsonProductosView;
import com.codpaa.model.MarcaModel;
import com.codpaa.model.ProductosModel;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;


public class TiendaDatos extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    private Spinner spinner;
    private EditText metros;
    private int idTienda, idPromotor;
    private RecyclerView recyclerView;
    RecyclerProductosMultiSelect adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datos_producto);

        idTienda = getIntent().getIntExtra("idTienda", 0);
        idPromotor = getIntent().getIntExtra("idPromotor", 0);


        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }



        spinner = (Spinner) findViewById(R.id.spinner_marca);
        metros = (EditText) findViewById(R.id.metros);
        recyclerView = (RecyclerView) findViewById(R.id.recycler);


        spinner.setOnItemSelectedListener(this);


        loadSpinner();


        /**
         * keyboard soft hide
         */
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_productos_tienda, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:

                finish();

                return true;

            case R.id.save_productos:

                try {
                    jsonConverter();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void jsonConverter() throws JSONException {

        if (adapter != null){
            Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();

            //Type listOfTestOj = new TypeToken<List<ProductosModel>>(){}.getType();


            Calendar c = Calendar.getInstance();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

            JsonProductosView view = new JsonProductosView();
            view.setProductos(adapter.getSelectedItems());
            view.setIdTienda(idTienda);
            view.setIdPromotor(idPromotor);
            view.setFecha(simpleDateFormat.format(c.getTime()));

            String s = gson.toJson(view);


            Log.d("JSON:", s);

        }

    }


    private void loadSpinner(){
        try {


            MarcasAdapter adapter = new MarcasAdapter(this,
                    android.R.layout.simple_spinner_item, getArrayList());
            spinner.setAdapter(adapter);

        }catch(Exception e) {
            Toast.makeText(this, "Error al cargar las marcas", Toast.LENGTH_SHORT).show();
        }

    }





    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        MarcaModel marcaModel = (MarcaModel) parent.getItemAtPosition(position);

        Log.d("idMarca", " " +marcaModel.getId());

        if (marcaModel.getId() > 0 ){
            loadRecyclerView(marcaModel.getId());
        } else {
            if (adapter != null){
                adapter.clearAdapter();
            }
        }

    }

    private void loadRecyclerView(int idMarca) {


        try {
            adapter = new RecyclerProductosMultiSelect(getProductos(idMarca), this);
            LinearLayoutManager linearManager = new LinearLayoutManager(this);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(linearManager);
            recyclerView.setAdapter(adapter);
        }catch (Exception e){
            e.printStackTrace();
        }



    }

    private ArrayList<ProductosModel> getProductos(int idMarca) {

        Cursor curPro = new BDopenHelper(this).productos(idMarca);
        ArrayList<ProductosModel> arrayP = new ArrayList<>();

        for (curPro.moveToFirst(); !curPro.isAfterLast(); curPro.moveToNext()){
            final ProductosModel pm = new ProductosModel();

            pm.setIdProducto(curPro.getInt(curPro.getColumnIndex("_id")));
            pm.setNombre(curPro.getString(curPro.getColumnIndex("nombre")));
            pm.setPresentacion(curPro.getString(curPro.getColumnIndex("presentacion")));
            pm.setIdMarca(curPro.getInt(curPro.getColumnIndex("idMarca")));

            arrayP.add(pm);
        }

        return arrayP;

    }


    private ArrayList<MarcaModel> getArrayList(){

        SQLiteDatabase base = new BDopenHelper(this).getReadableDatabase();
        String sql = "select idMarca, nombre, img from marca order by nombre asc;";
        Cursor cursorMarca = base.rawQuery(sql, null);

        ArrayList<MarcaModel> array = new ArrayList<>();

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
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
