package com.codpaa.response;
/*
 * Created by grim on 03/08/2016.
 */

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.widget.Toast;

import com.codpaa.db.BDopenHelper;
import com.codpaa.provider.DbEstructure;
import com.codpaa.provider.DbEstructure.Materiales;
import com.codpaa.provider.DbEstructure.ProductByFormato;
import com.codpaa.provider.DbEstructure.ProductoByTienda;
import com.codpaa.util.Configuracion;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import cz.msebera.android.httpclient.Header;

public class HttpResponseProducts extends JsonHttpResponseHandler {

    private Context context;
    private ProgressDialog progressDialog;



    public HttpResponseProducts(Context context){

        this.context = context;



        this.progressDialog = new ProgressDialog(context);
        this.progressDialog.setMessage("Descargando...");
        this.progressDialog.setCancelable(false);


    }

    @Override
    public void onStart() {
        super.onStart();


        progressDialog.show();



    }

    @Override
    public void onFinish() {
        super.onFinish();



    }


    @Override
    public void onSuccess(int statusCode, Header[] headers, final JSONObject response) {


        //Log.d("OnSuccess Info", "1");
        final Handler handler = new Handler();


        if (response != null){


            new Thread(new Runnable() {
                @Override
                public void run() {


                    try {
                        JSONArray marcas = response.getJSONArray("marcas");
                        JSONArray productos = response.getJSONArray("productos");
                        JSONArray exhibiciones = response.getJSONArray("exhibiciones");


                        JSONArray productoFormato = response.getJSONArray("productoFormato");
                        JSONArray productoTienda = response.getJSONArray("productoTienda");
                        JSONArray tiendaMarca = response.getJSONArray("tienda_marca");


                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                progressDialog.setMessage("Descargando Productos ...");

                            }
                        });

                        parseJSONMarca(marcas);
                        parseJSONProductos(productos);
                        parseJSONExhi(exhibiciones);


                        parseJSONProductoByFormato(productoFormato);
                        parseJSONProductoByTienda(productoTienda);



                        insertTiendaMarca(tiendaMarca);

                        handler.post(new Runnable() {
                            @Override
                            public void run() {

                                progressDialog.setMessage("Descargado!!!");

                            }
                        });



                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                progressDialog.dismiss();

                            }
                        }, 1000);



                    } catch (JSONException e) {
                        e.printStackTrace();


                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                progressDialog.setMessage("Ocurrio un problema al guardar la informacion, intentelo mas tarde");
                                progressDialog.dismiss();
                            }
                        }, 4000);
                    } catch (Exception e){
                        e.printStackTrace();

                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                progressDialog.setMessage("Ocurrio un problema al guardar la informacion, intentelo mas tarde");
                                progressDialog.dismiss();
                            }
                        }, 4000);
                    }


                }
            }).start();



        }



    }

    @Override
    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
        super.onFailure(statusCode, headers, throwable, errorResponse);

        Toast.makeText(context, "Error Al descargar la Informacion!!", Toast.LENGTH_SHORT).show();
        progressDialog.dismiss();
    }





    private synchronized void insertTiendaMarca(JSONArray tiendaMarca){


        BDopenHelper helper = new BDopenHelper(context.getApplicationContext());

        helper.vaciarTabla(DbEstructure.TiendaMarca.TABLE_NAME);

        SQLiteDatabase manager = helper.getWritableDatabase();

        int length = tiendaMarca.length();

        if (length> 0){


            for (int i = 0; i < length ; i++){

                ContentValues cv = new ContentValues();

                try {
                    cv.put(DbEstructure.TiendaMarca.ID_TIENDA, tiendaMarca.getJSONObject(i).getInt("id_tienda"));
                    cv.put(DbEstructure.TiendaMarca.ID_MARCA, tiendaMarca.getJSONObject(i).getInt("id_marca"));


                } catch (JSONException e) {
                    e.printStackTrace();
                }

                manager.insert(DbEstructure.TiendaMarca.TABLE_NAME,null, cv);

            }


        }

        manager.close();

    }





    private synchronized void parseJSONMarca(JSONArray marcaArray) throws JSONException {
        BDopenHelper b = new BDopenHelper(context.getApplicationContext());
        b.vaciarTabla("marca");

        Configuracion configuracion = new Configuracion(context);

        Calendar c = Calendar.getInstance();
        SimpleDateFormat dFecha = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        String fecha = dFecha.format(c.getTime());

        for(int i= 0; i < marcaArray.length(); i++) {

            b.insertarMarca(marcaArray.getJSONObject(i).getInt("idMarca")
                    ,marcaArray.getJSONObject(i).getString("nombre")
                    ,"logo");
        }

        configuracion.setMarca(fecha);
    }


    private synchronized void parseJSONProductos(JSONArray productosArray) throws JSONException {
        BDopenHelper b = new BDopenHelper(context.getApplicationContext());
        Configuracion configuracion = new Configuracion(context);

        Calendar c = Calendar.getInstance();
        SimpleDateFormat dFecha = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        String fecha = dFecha.format(c.getTime());

        b.deleteTable("producto");

        for(int i= 0; i < productosArray.length(); i++) {

            b.insertarProducto(productosArray.getJSONObject(i).getInt("idProducto"),
                    productosArray.getJSONObject(i).getString("nombre"),
                    productosArray.getJSONObject(i).getString("presentacion"),
                    productosArray.getJSONObject(i).getInt("idMarca"),
                    productosArray.getJSONObject(i).getString("cb"),
                    productosArray.getJSONObject(i).getInt("tester"),
                    productosArray.getJSONObject(i).getDouble("precio_compra"),
                    productosArray.getJSONObject(i).getDouble("precio_sugerido"),
                    productosArray.getJSONObject(i).getString("precio_update"),
                    productosArray.getJSONObject(i).getString("descripcion"),
                    productosArray.getJSONObject(i).getInt("has_image")
            );




        }


        configuracion.setProducto(fecha);
    }

    private synchronized void parseJSONExhi(JSONArray exhiArray) throws JSONException {
        BDopenHelper b = new BDopenHelper(context.getApplicationContext());
        b.vaciarTabla("tipoexhibicion");

        Configuracion configuracion = new Configuracion(context);

        Calendar c = Calendar.getInstance();
        SimpleDateFormat dFecha = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        String fecha = dFecha.format(c.getTime());

        for(int i= 0; i < exhiArray.length(); i++) {
            b.insertarTipoExhibicion(exhiArray.getJSONObject(i).getInt("IE"),
                    exhiArray.getJSONObject(i).getString("N"));
        }

        configuracion.setExhibicion(fecha);
    }


    private synchronized void parseJsonTiendas(JSONArray tiendasArray) throws JSONException {
        BDopenHelper b = new BDopenHelper(context.getApplicationContext());
        b.vaciarTabla("clientes");
        Configuracion configuracion = new Configuracion(context);

        Calendar c = Calendar.getInstance();
        SimpleDateFormat dFecha = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        String fecha = dFecha.format(c.getTime());

        for(int i=0; i<tiendasArray.length(); i++){
            b.insertarClientes(tiendasArray.getJSONObject(i).getInt("idTienda"),
                    tiendasArray.getJSONObject(i).getString("grupo"),
                    tiendasArray.getJSONObject(i).getString("sucursal"),
                    tiendasArray.getJSONObject(i).getString("x"),
                    tiendasArray.getJSONObject(i).getString("y"),
                    tiendasArray.getJSONObject(i).getInt("idFormato"),
                    tiendasArray.getJSONObject(i).getInt("idTipoTienda"));
        }

        configuracion.setTiendas(fecha);

    }



    private synchronized void parseJSONProductoByFormato(JSONArray array) throws JSONException{
        SQLiteDatabase db = new BDopenHelper(context).getWritableDatabase();
        new BDopenHelper(context.getApplicationContext()).vaciarTabla(ProductByFormato.TABLE_NAME);

        Configuracion configuracion = new Configuracion(context);

        Calendar c = Calendar.getInstance();
        SimpleDateFormat dFecha = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        String fecha = dFecha.format(c.getTime());

        if (db != null){

            for (int i = 0; i < array.length() ; i++){

                ContentValues contentValues = new ContentValues();
                contentValues.put("idProducto", array.getJSONObject(i).getInt("idProducto"));
                contentValues.put("idFormato", array.getJSONObject(i).getInt("idFormato"));

                db.insert(ProductByFormato.TABLE_NAME, null, contentValues);

            }


        }

        configuracion.setProductoByFormato(fecha);


    }


    private synchronized void parseJSONProductoByTienda(JSONArray array) throws JSONException {
        SQLiteDatabase db = new BDopenHelper(context).getWritableDatabase();
        new BDopenHelper(context.getApplicationContext()).vaciarTabla(ProductoByTienda.TABLE_NAME);

        Configuracion configuracion = new Configuracion(context);

        Calendar c = Calendar.getInstance();
        SimpleDateFormat dFecha = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        String fecha = dFecha.format(c.getTime());

        for (int i = 0; i < array.length() ; i++){
            ContentValues contentValues = new ContentValues();
            contentValues.put(ProductoByTienda.ID_PRODUCTO, array.getJSONObject(i).getInt("idProducto"));
            contentValues.put(ProductoByTienda.ID_TIENDA, array.getJSONObject(i).getInt("idTienda"));
            contentValues.put(ProductoByTienda.ESTATUS, array.getJSONObject(i).getInt("estatus"));
            contentValues.put(ProductoByTienda.FECHA_UPDATE, array.getJSONObject(i).getString("update"));

            db.insert(ProductoByTienda.TABLE_NAME, null, contentValues);
        }

        configuracion.setProductoByTienda(fecha);

    }



}
