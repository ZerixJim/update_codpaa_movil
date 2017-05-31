package com.codpaa.activity.impulsor;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
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
import android.widget.Toast;

import com.codpaa.R;
import com.codpaa.adapter.MarcasAdapter;
import com.codpaa.adapter.generic.ProductoRecyclerAdapter;
import com.codpaa.db.BDopenHelper;
import com.codpaa.model.JsonProductoImpulsor;
import com.codpaa.model.MarcaModel;
import com.codpaa.model.generic.Producto;
import com.codpaa.provider.DbEstructure;


import com.codpaa.provider.DbEstructure.ProductoCatalogadoTienda;
import com.codpaa.response.ProductoCatalogoResponse;
import com.codpaa.util.QuickstartPreferences;
import com.codpaa.util.Utilities;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;


/*
 * Created by grim on 18/05/2017.
 */

public class Estatus extends AppCompatActivity implements AdapterView.OnItemSelectedListener {


    private RecyclerView mRecyclerView;
    private int idPromotor, idTienda;
    private BroadcastReceiver broadcastReceiver;


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
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){


            case android.R.id.home:

                finish();

                return true;


            case R.id.send_status:

                sendEstatus();

                return true;


            default:
                return super.onOptionsItemSelected(item);
        }


    }

    private void sendEstatus() {

        ProductoRecyclerAdapter adapter = (ProductoRecyclerAdapter) mRecyclerView.getAdapter();

        List<Producto> productos = adapter.getProductListValidation();



        if (productos.size() > 0){



            SQLiteDatabase db = new BDopenHelper(this).getWritableDatabase();

            Calendar calendar = Calendar.getInstance();

            SimpleDateFormat fFecha = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

            String fecha = fFecha.format(calendar.getTime());


            for (Producto producto: productos){

                ContentValues contentValues = new ContentValues();

                contentValues.put(ProductoCatalogadoTienda.ID_PRODUCTO, producto.getIdProducto());
                contentValues.put(ProductoCatalogadoTienda.ID_PROMOTOR, idPromotor);
                contentValues.put(ProductoCatalogadoTienda.ID_TIENDA, idTienda);
                contentValues.put(ProductoCatalogadoTienda.FECHA_CAPTURA,  fecha);
                contentValues.put(ProductoCatalogadoTienda.ESTATUS_PRODUCTO, producto.getEstatus());
                contentValues.put(ProductoCatalogadoTienda.CANTIDAD, producto.getCantidad());

                db.insert(DbEstructure.ProductoCatalogadoTienda.TABLE_NAME, null, contentValues);


                if (producto.getEstatus() == Producto.EstatusTypes.PROCESO_CATALOGACION){


                    List<String> objeciones = producto.getObjeciones();

                    if(objeciones.size() > 0){

                        for (String s: objeciones){

                            ContentValues contentValues1 = new ContentValues();

                            contentValues1.put(DbEstructure.ProcesoCatalogacionObjeciones.ID_TIENDA, idTienda);
                            contentValues1.put(DbEstructure.ProcesoCatalogacionObjeciones.ID_PRODUCTO, producto.getIdProducto());
                            contentValues1.put(DbEstructure.ProcesoCatalogacionObjeciones.DESCRIPCION, s);
                            contentValues1.put(DbEstructure.ProcesoCatalogacionObjeciones.FECHA, fecha);

                            db.insert(DbEstructure.ProcesoCatalogacionObjeciones.TABLE_NAME, null, contentValues1);

                        }

                    }




                }




            }

            Toast.makeText(this, "Productos Guardados", Toast.LENGTH_SHORT).show();




        }else {

            Toast.makeText(this, "Debes seleccionar por lo menos un producto", Toast.LENGTH_SHORT).show();


        }

        sendToServer();






    }


    private void sendToServer(){


        if (getProductListSendToServer().size() > 0){


            AsyncHttpClient client = new AsyncHttpClient();

            RequestParams rp = new RequestParams();


            Gson gson = new Gson();


            JsonProductoImpulsor json = new JsonProductoImpulsor(getProductListSendToServer(), idPromotor);

            rp.put("solicitud", "sendCatalogo");
            rp.put("json", gson.toJson(json));

            Log.d("json", gson.toJson(json));




            client.post(Utilities.WEB_SERVICE_CODPAA + "send_impulsor.php", rp , new ProductoCatalogoResponse(this));


            //mRecyclerView.getAdapter().notifyDataSetChanged();
        }






    }

    private List<Producto> getProductListSendToServer(){
        List<Producto> list = new ArrayList<>();

        SQLiteDatabase db = new BDopenHelper(this).getReadableDatabase();

        Cursor cursor = db.rawQuery("select * from producto_catalogado_tienda where estatus_registro = 1", null);


        if(cursor.getCount() > 0){
            for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()){


                final Producto producto = new Producto();
                producto.setIdProducto(cursor.getInt(cursor.getColumnIndex("idProducto")));
                producto.setIdTienda(cursor.getInt(cursor.getColumnIndex("idTienda")));
                producto.setFecha(cursor.getString(cursor.getColumnIndex("fecha_captura")));
                producto.setEstatus(cursor.getInt(cursor.getColumnIndex("estatus_producto")));
                producto.setCantidad(cursor.getInt(cursor.getColumnIndex("cantidad")));

                if(cursor.getInt(cursor.getColumnIndex("estatus_producto")) == Producto.EstatusTypes.PROCESO_CATALOGACION){


                    String sql = "select * from "
                            + DbEstructure.ProcesoCatalogacionObjeciones.TABLE_NAME +" " +
                            " where idProducto="+ cursor.getInt(cursor.getColumnIndex("idProducto")) + " and idTienda=" +
                            cursor.getInt(cursor.getColumnIndex("idTienda")) + " and fecha_captura='" +
                            cursor.getString(cursor.getColumnIndex("fecha_captura")) + "'";

                    Cursor cursorObjeciones = db.rawQuery( sql, null);

                    Log.d("sql", sql );

                    if (cursorObjeciones.getCount() > 0){

                        List<String> lista = new ArrayList<>();
                        for (cursorObjeciones.moveToFirst() ; !cursorObjeciones.isAfterLast() ; cursorObjeciones.moveToNext()){

                            lista.add(cursorObjeciones.getString(cursorObjeciones.getColumnIndex("descripcion")));

                        }

                        producto.setObjeciones(lista);

                    }

                    cursorObjeciones.close();

                }


                list.add(producto);
            }
        }


        cursor.close();
        db.close();
        return list;
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

        ArrayList<Producto> arrayP = new ArrayList<>();

        SQLiteDatabase db = new BDopenHelper(this).getReadableDatabase();


        String sql = "select  p.idProducto, p.nombre, p.presentacion, p.cb, p.idMarca, pc.estatus_producto, pc.fecha_captura," +
                " p.precio_compra, p.precio_sugerido, fecha_precio " +
                     " from productotienda as pt " +
                     " left  join producto as p on pt.idProducto=p.idProducto "+
                     " left join producto_catalogado_tienda as pc on pc.idProducto=p.idProducto " +
                     " where p.idMarca=" + idMarca +  " and pt.idTienda=" + idTienda + " " +
                     " group by p.idProducto order by p.nombre asc";

        Cursor curProByTienda = db.rawQuery(sql, null);


        if(curProByTienda.getCount() > 0){

            for(curProByTienda.moveToFirst(); !curProByTienda.isAfterLast(); curProByTienda.moveToNext()){
                final Producto spP = new Producto();
                spP.setIdProducto(curProByTienda.getInt(0));
                spP.setNombre(curProByTienda.getString(1));
                spP.setPresentacion(curProByTienda.getString(2));
                spP.setCodeBarras(curProByTienda.getString(3));
                spP.setIdMarca(curProByTienda.getInt(4));
                spP.setEstatus(curProByTienda.getInt(curProByTienda.getColumnIndex("estatus_producto")));
                spP.setFecha(curProByTienda.getString(curProByTienda.getColumnIndex("fecha_captura")));

                spP.setPrecioCompra(curProByTienda.getFloat(curProByTienda.getColumnIndex("precio_compra")));
                spP.setPrecioVenta(curProByTienda.getFloat(curProByTienda.getColumnIndex("precio_sugerido")));
                spP.setFechaPrecio(curProByTienda.getString(curProByTienda.getColumnIndex("fecha_precio")));


                arrayP.add(spP);
            }
        }

        curProByTienda.close();
        db.close();

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
