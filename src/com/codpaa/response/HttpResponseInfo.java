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
import com.codpaa.util.Configuracion;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import com.codpaa.provider.DbEstructure.ProductByFormato;
import com.codpaa.provider.DbEstructure.ProductoByTienda;
import com.codpaa.provider.DbEstructure.Materiales;

import cz.msebera.android.httpclient.Header;

public class HttpResponseInfo extends JsonHttpResponseHandler {

    private Context context;
    private ProgressDialog progressDialog;

    public HttpResponseInfo(Context context){

        this.context = context;



        this.progressDialog = new ProgressDialog(context);
        this.progressDialog.setMessage("Descargando...");
        this.progressDialog.setCancelable(false);
        this.progressDialog.setMax(100);


    }

    @Override
    public void onStart() {
        super.onStart();


        progressDialog.show();

        //Log.d("Thread" ,  );


    }

    @Override
    public void onFinish() {
        super.onFinish();



    }




    @Override
    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
        super.onSuccess(statusCode, headers, response);

        //Log.d("OnSuccess Info", "1");
        Handler handler = new Handler();

        if (response != null){
            try {
                JSONArray marcas = response.getJSONArray("marcas");
                JSONArray productos = response.getJSONArray("productos");
                JSONArray exhibiciones = response.getJSONArray("exhibiciones");
                JSONArray tiendas = response.getJSONArray("tiendas");
                JSONArray ruta = response.getJSONArray("ruta");
                JSONArray productoFormato = response.getJSONArray("productoFormato");
                JSONArray productoTienda = response.getJSONArray("productoTienda");
                JSONArray materiales = response.getJSONArray("materiales");

                parseJSONMarca(marcas);
                parseJSONProductos(productos);
                parseJSONExhi(exhibiciones);
                parseJsonTiendas(tiendas);
                parseJSONRuta(ruta);
                parseJSONProductoByFormato(productoFormato);
                parseJSONProductoByTienda(productoTienda);
                parseJSONMateriales(materiales);

                //progressDialog.setContentView(R.layout.dialog_done);


                progressDialog.setMessage("Informacion Cargada Satisfactoriamente");




                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        progressDialog.dismiss();

                    }
                }, 2500);



                Toast.makeText(context, "informacion cargada ", Toast.LENGTH_SHORT).show();

                //Toast.makeText(context, "Informacion Cargada con Exito!!", Toast.LENGTH_SHORT).show();
            }catch (JSONException e){
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






    }

    @Override
    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
        super.onFailure(statusCode, headers, throwable, errorResponse);

        Toast.makeText(context, "Error Al descargar la Informacion!!", Toast.LENGTH_SHORT).show();
        progressDialog.dismiss();
    }


    private void parseJSONMateriales(JSONArray materiales) throws JSONException{

        BDopenHelper b = new BDopenHelper(context.getApplicationContext());
        b.vaciarTabla(DbEstructure.Materiales.TABLE_NAME);
        Configuracion config = new Configuracion(context);
        Calendar c = Calendar.getInstance();
        SimpleDateFormat dFecha = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        String fecha = dFecha.format(c.getTime());

        int length = materiales.length();

        for (int i = 0; i < length; i++ ){
            ContentValues content = new ContentValues();
            content.put(Materiales.ID_MATERIAL, materiales.getJSONObject(i).getInt("idMateria"));
            content.put(Materiales.MATERIAL, materiales.getJSONObject(i).getString("nombre"));
            content.put(Materiales.UNIDAD, materiales.getJSONObject(i).getString("unidad"));
            content.put(Materiales.SOLICITUD_MAXIMA, materiales.getJSONObject(i).getInt("solicitudMaxima"));
            content.put(Materiales.TIPO_MATERIAL, materiales.getJSONObject(i).getInt("tipoMaterial"));

            b.insertar(Materiales.TABLE_NAME, content);
        }

        config.setMaterialesUpdate(fecha);

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
                    productosArray.getJSONObject(i).getString("cb"),
                    productosArray.getJSONObject(i).getInt("tester"),
                    productosArray.getJSONObject(i).getDouble("precio_compra"),
                    productosArray.getJSONObject(i).getDouble("precio_sugerido"),
                    productosArray.getJSONObject(i).getString("precio_update"),
                    productosArray.getJSONObject(i).getString("descripcion"));




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
                    tiendasArray.getJSONObject(i).getInt("idFormato"),
                    tiendasArray.getJSONObject(i).getInt("idTipoTienda"));
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
                    rutasArray.getJSONObject(i).getString("rol"),
                    rutasArray.getJSONObject(i).getInt("idModo"));


        }

        configuracion.setRuta(fecha);

    }

    private void parseJSONProductoByFormato(JSONArray array) throws JSONException{
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

                db.insert(DbEstructure.ProductByFormato.TABLE_NAME, null, contentValues);

            }


        }

        configuracion.setProductoByFormato(fecha);


    }


    private void parseJSONProductoByTienda(JSONArray array) throws JSONException {
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

            db.insert(ProductoByTienda.TABLE_NAME, null, contentValues);
        }

        configuracion.setProductoByTienda(fecha);

    }



}
