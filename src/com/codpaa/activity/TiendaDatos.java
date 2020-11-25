package com.codpaa.activity;
/*
 * Created by grim on 29/08/2016.
 */

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
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
import com.codpaa.model.MarcaModel;
import com.codpaa.model.ProductosModel;
import com.codpaa.provider.DbEstructure;
import com.codpaa.update.EnviarDatos;
import com.codpaa.util.Utilities;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;


import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import cz.msebera.android.httpclient.Header;



public class TiendaDatos extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    private Spinner spinner;
    private EditText metros, checkouts;
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
        metros = (EditText) findViewById(R.id.metros);
        checkouts = (EditText) findViewById(R.id.checkouts);


        spinner.setOnItemSelectedListener(this);


        loadSpinner();


        /*
          keyboard soft hide
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

        int itemId = item.getItemId();
        if (itemId == android.R.id.home) {
            finish();

            return true;
        } else if (itemId == R.id.save_productos) {
            save();

            return true;
        }
        return super.onOptionsItemSelected(item);

    }

    /*private String jsonConverter() throws JSONException {

        if (adapter != null){
            Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();

            Calendar c = Calendar.getInstance();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

            JsonProductosView view = new JsonProductosView();
            view.setProductos(adapter.getSelectedItems());
            view.setIdTienda(idTienda);
            view.setIdPromotor(idPromotor);
            view.setFecha(simpleDateFormat.format(c.getTime()));


            return gson.toJson(view);

        }

        return null;

    }*/


    private void save(){

        MarcaModel mModel = (MarcaModel) spinner.getSelectedItem();


        if (adapter != null && mModel.getId() > 0){

            if (adapter.isJustOneSelected()){

                SQLiteDatabase db = new BDopenHelper(this).getWritableDatabase();
                Calendar c = Calendar.getInstance();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

                List<Integer> pro = adapter.getSelectedItems();
                for (Integer i: pro){

                    ContentValues cv = new ContentValues();
                    cv.put(DbEstructure.TiendaProductoCatalogo.ID_PRODUCTO, i);
                    cv.put(DbEstructure.TiendaProductoCatalogo.ID_PROMOTOR, idPromotor);
                    cv.put(DbEstructure.TiendaProductoCatalogo.FECHA, simpleDateFormat.format(c.getTime()));
                    cv.put(DbEstructure.TiendaProductoCatalogo.ID_TIENDA, idTienda);


                    try {
                        db.insertOrThrow(DbEstructure.TiendaProductoCatalogo.TABLE_NAME, null, cv);
                    }catch (SQLiteConstraintException e){

                        Toast.makeText(this, "Uno o varios Productos ya fueron registrados", Toast.LENGTH_SHORT).show();

                    }



                }

                if (metros.getText().length() > 0 && checkouts.getText().length() >0){

                    AsyncHttpClient client = new AsyncHttpClient();

                    RequestParams rp = new RequestParams();
                    rp.put("solicitud", "update_datos_tienda");
                    rp.put("tamanio", metros.getText().toString());
                    rp.put("cajas", checkouts.getText().toString());
                    rp.put("idTienda", idTienda);

                    client.get(this, Utilities.WEB_SERVICE_CODPAA + "serv.php", rp, new JsonHttpResponseHandler(){
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                            super.onSuccess(statusCode, headers, response);

                            if (response != null){
                                try {
                                    if (response.getBoolean("update")){
                                        Log.d("Update", "Datos Actualizados correctamente");

                                        metros.setText("");
                                        checkouts.setText("");
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }


                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                            super.onFailure(statusCode, headers, throwable, errorResponse);
                        }
                    });

                }


                EnviarDatos enviarDatos = new EnviarDatos(this);
                enviarDatos.sendCatalogoProducto();

            }else {
                Toast.makeText(this, "Selecciona por lo menos un producto", Toast.LENGTH_SHORT).show();
            }

        } else {
            Toast.makeText(this, "No Seleccionaste Marca", Toast.LENGTH_SHORT).show();
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
