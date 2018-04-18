package com.codpaa.activity.impulsor;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.codpaa.R;
import com.codpaa.adapter.ProcesoAceptacionRecyclerAdapter;
import com.codpaa.db.BDopenHelper;
import com.codpaa.model.JsonProductoImpulsor;
import com.codpaa.model.generic.Producto;
import com.codpaa.provider.DbEstructure;
import com.codpaa.response.JsonResponseUpdateProceso;
import com.codpaa.util.Utilities;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

/*
 * Created by grim on 18/05/2017.
 */

public class ProcesoAceptacion extends AppCompatActivity implements ProcesoAceptacionRecyclerAdapter.ItemCheckListener, View.OnClickListener {

    private RecyclerView recyclerView;
    private int idTienda, idPromotor;
    private ProcesoAceptacionRecyclerAdapter adapter;
    private FloatingActionButton floatingActionButton;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proceso_aceptacion);


        idPromotor = getIntent().getIntExtra("idPromotor", 0);
        idTienda = getIntent().getIntExtra("idTienda", 0);


        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        floatingActionButton = (FloatingActionButton) findViewById(R.id.float_button);

        if(recyclerView !=  null){

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);


            recyclerView.setLayoutManager(linearLayoutManager);


            List<Producto> productos = getListProducts();

            int size = productos.size();


            adapter = new ProcesoAceptacionRecyclerAdapter(this, productos, this);

            recyclerView.setAdapter(adapter);


            recyclerView.setItemViewCacheSize(size);



        }

        if (floatingActionButton != null) {
            floatingActionButton.setOnClickListener(this);
        }

        ActionBar actionBar = getSupportActionBar();

        if(actionBar != null){

            actionBar.setDisplayHomeAsUpEnabled(true);

        }


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

    private List<Producto> getListProducts(){
        List<Producto> array = new ArrayList<>();

        SQLiteDatabase db = new BDopenHelper(this).getReadableDatabase();

        Cursor cursor = db.rawQuery("select pct.idProducto, pct.idTienda, pct.fecha_captura, pct.cantidad , p.nombre, p.presentacion, pct.estatus_proceso, pct.estatus_registro  " +
                "        from producto_catalogado_tienda as pct " +
                " left join producto as p on p.idProducto=pct.idProducto " +
                " where pct.idTienda=" + idTienda +  " and pct.folio is not null " +
                " and estatus_registro>=3 group by pct.idProducto,pct.fecha_captura ", null);

        if(cursor.getCount() > 0){


            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()){

                final Producto producto = new Producto();

                producto.setIdProducto(cursor.getInt(cursor.getColumnIndex("idProducto")));
                producto.setIdTienda(cursor.getInt(cursor.getColumnIndex("idTienda")));
                producto.setFecha(cursor.getString(cursor.getColumnIndex("fecha_captura")));
                producto.setCantidad(cursor.getInt(cursor.getColumnIndex("cantidad")));
                producto.setNombre(cursor.getString(cursor.getColumnIndex("nombre")));
                producto.setPresentacion(cursor.getString(cursor.getColumnIndex("presentacion")));
                producto.setEstatus(cursor.getInt(cursor.getColumnIndex("estatus_registro")));

                producto.setEstatusProceso(cursor.getString(cursor.getColumnIndex("estatus_proceso")));


                array.add(producto);
            }


        }



        cursor.close();
        db.close();

        return array;

    }



    private void saveList(){

        List<Producto> productos = adapter.getSelectProducts();

        SQLiteDatabase db = new BDopenHelper(this).getWritableDatabase();

        for (Producto producto: productos){

            ContentValues values = new ContentValues();

            values.put("estatus_proceso", producto.getEstatusProceso());
            values.put("cantidad_entrega", producto.getCantidad());

            db.update(DbEstructure.ProductoCatalogadoTienda.TABLE_NAME, values, "" +
                    DbEstructure.ProductoCatalogadoTienda.ID_PRODUCTO + "=" + producto.getIdProducto() + " and " +
                    DbEstructure.ProductoCatalogadoTienda.ID_TIENDA + "=" + producto.getIdTienda() + " and " +
                    DbEstructure.ProductoCatalogadoTienda.FECHA_CAPTURA +"='" + producto.getFecha() + "'", null);


        }


        updateProductOnServer();

    }


    private void updateProductOnServer(){


        SQLiteDatabase db = new BDopenHelper(this).getReadableDatabase();

        Cursor cursor = db.rawQuery("select pct.idProducto, pct.idTienda, pct.fecha_captura, pct.estatus_proceso, pct.cantidad_entrega " +
                "                 from producto_catalogado_tienda as pct " +
                "                 where pct.estatus_proceso is not null " +
                "                 and estatus_registro>=3", null);
        if (cursor.getCount() > 0){

            List<Producto> list = new ArrayList<>();

            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()){
                final Producto producto = new Producto();


                producto.setIdProducto(cursor.getInt(cursor.getColumnIndex("idProducto")));
                producto.setIdTienda(cursor.getInt(cursor.getColumnIndex("idTienda")));
                producto.setFecha(cursor.getString(cursor.getColumnIndex("fecha_captura")));
                producto.setEstatusProceso(cursor.getString(cursor.getColumnIndex("estatus_proceso")));
                producto.setCantidad(cursor.getInt(cursor.getColumnIndex("cantidad_entrega")));

                list.add(producto);

            }



            AsyncHttpClient client = new AsyncHttpClient();


            RequestParams rp = new RequestParams();


            Gson gson = new Gson();

            JsonProductoImpulsor json = new JsonProductoImpulsor(list, idPromotor);

            rp.put("solicitud", "update");
            rp.put("json", gson.toJson(json));

            Log.d("json", gson.toJson(json));

            client.post(this, Utilities.WEB_SERVICE_CODPAA + "update_producto_proceso.php", rp, new JsonResponseUpdateProceso(this));


        }


        cursor.close();
        db.close();



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

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }



    @Override
    public void onItemCheck(int cantidadCkecked) {

        if(cantidadCkecked > 0){

            if (floatingActionButton.getVisibility() == View.INVISIBLE || floatingActionButton.getVisibility() == View.GONE)
                floatingActionButton.show();

        }else {

            if(floatingActionButton.getVisibility() == View.VISIBLE)
                floatingActionButton.hide();

        }


    }

    @Override
    public void onClick(View v) {

        Toast.makeText(this, "enviando", Toast.LENGTH_SHORT).show();
        saveList();

    }
}
