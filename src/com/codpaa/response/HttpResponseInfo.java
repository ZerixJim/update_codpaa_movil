package com.codpaa.response;/*
 * Created by grim on 03/08/2016.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import com.codpaa.db.BDopenHelper;
import com.codpaa.util.Configuracion;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import cz.msebera.android.httpclient.Header;
import com.codpaa.provider.DbEstructure.ProductByTienda;

public class HttpResponseInfo extends JsonHttpResponseHandler {

    private Context context;

    public HttpResponseInfo(Context context){

        this.context = context;

    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
        super.onSuccess(statusCode, headers, response);

        if (response != null){
            try {
                JSONArray marcas = response.getJSONArray("marcas");
                JSONArray productos = response.getJSONArray("productos");
                JSONArray exhibiciones = response.getJSONArray("exhibiciones");
                JSONArray tiendas = response.getJSONArray("tiendas");
                JSONArray ruta = response.getJSONArray("ruta");
                JSONArray productoTienda = response.getJSONArray("productoTienda");

                parseJSONMarca(marcas);
                parseJSONProductos(productos);
                parseJSONExhi(exhibiciones);
                parseJsonTiendas(tiendas);
                parseJSONRuta(ruta);
                parseJSONProductoByTinda(productoTienda);



            }catch (JSONException e){
                e.printStackTrace();
            }finally {
                Toast.makeText(context, "Informacion descargada!!", Toast.LENGTH_SHORT).show();
            }
        }



    }


    @Override
    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
        super.onFailure(statusCode, headers, throwable, errorResponse);

        Toast.makeText(context, "Error Al descargar la Informacion!!", Toast.LENGTH_SHORT).show();
    }



    private void parseJSONMarca(JSONArray marcaArray) throws JSONException {
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


    private void parseJSONProductos(JSONArray productosArray) throws JSONException {
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
                    productosArray.getJSONObject(i).getString("cb"));




        }


        configuracion.setProducto(fecha);
    }

    private void parseJSONExhi(JSONArray exhiArray) throws JSONException {
        BDopenHelper b = new BDopenHelper(context.getApplicationContext());
        b.vaciarTabla("exhibiciones");

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


    private void parseJsonTiendas(JSONArray tiendasArray) throws JSONException {
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
                    tiendasArray.getJSONObject(i).getInt("idFormato"));
        }

        configuracion.setTiendas(fecha);

    }

    private void parseJSONRuta(JSONArray rutasArray) throws JSONException {
        BDopenHelper b = new BDopenHelper(context.getApplicationContext());
        b.vaciarTabla("visitaTienda");

        Configuracion configuracion = new Configuracion(context);

        Calendar c = Calendar.getInstance();
        SimpleDateFormat dFecha = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        String fecha = dFecha.format(c.getTime());

        for(int i= 0; i < rutasArray.length(); i++) {

            b.insertarRutaVisitas(rutasArray.getJSONObject(i).getInt("idTienda"),
                    rutasArray.getJSONObject(i).getInt("lu"),
                    rutasArray.getJSONObject(i).getInt("ma"),
                    rutasArray.getJSONObject(i).getInt("mi"),
                    rutasArray.getJSONObject(i).getInt("ju"),
                    rutasArray.getJSONObject(i).getInt("vi"),
                    rutasArray.getJSONObject(i).getInt("sa"),
                    rutasArray.getJSONObject(i).getInt("do"),
                    rutasArray.getJSONObject(i).getInt("idPromotor"),
                    rutasArray.getJSONObject(i).getString("rol"));

        }

        configuracion.setRuta(fecha);

    }

    private void parseJSONProductoByTinda(JSONArray array) throws JSONException{
        SQLiteDatabase db = new BDopenHelper(context).getWritableDatabase();

        Configuracion configuracion = new Configuracion(context);

        Calendar c = Calendar.getInstance();
        SimpleDateFormat dFecha = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        String fecha = dFecha.format(c.getTime());

        if (db != null){

            for (int i = 0; i < array.length() ; i++){

                ContentValues contentValues = new ContentValues();
                contentValues.put("idProducto", array.getJSONObject(i).getInt("idProducto"));
                contentValues.put("idFormato", array.getJSONObject(i).getInt("idFormato"));

                db.insert(ProductByTienda.TABLE_NAME, null, contentValues);

            }


        }

        configuracion.setProductoByTienda(fecha);


    }



}
