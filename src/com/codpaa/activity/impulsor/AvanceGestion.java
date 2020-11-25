package com.codpaa.activity.impulsor;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.fragment.app.FragmentManager;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;


import com.codpaa.R;
import com.codpaa.adapter.generic.AvanceGestionRecyclerAdaptar;
import com.codpaa.db.BDopenHelper;
import com.codpaa.fragment.DialogFragmentContrato;
import com.codpaa.model.AvanceGestionModel;
import com.codpaa.model.JsonUpdateFirma;
import com.codpaa.provider.DbEstructure;
import com.codpaa.response.ResponseUpdateFirmaProducto;
import com.codpaa.util.QuickstartPreferences;
import com.codpaa.util.Utilities;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;

import java.util.ArrayList;
import java.util.List;

/*
 * Created by Grim on 21/05/2017.
 */

public class AvanceGestion extends AppCompatActivity implements AvanceGestionRecyclerAdaptar.ItemCheckListener, View.OnClickListener, DialogFragmentContrato.SignatureListener {

    private int idPromotor, idTienda;
    private RecyclerView recyclerView;
    private FloatingActionButton floatingActionButton;
    AvanceGestionRecyclerAdaptar adaptar;
    private BroadcastReceiver broadcastReceiver;
    private boolean isReceiverMessageRegistered;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_avance_gestion);



        idPromotor = getIntent().getIntExtra("idPromotor", 0);
        idTienda = getIntent().getIntExtra("idTienda", 0);


        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        floatingActionButton = (FloatingActionButton) findViewById(R.id.float_button);

        if (floatingActionButton != null) {
            floatingActionButton.setOnClickListener(this);
        }

        ActionBar actionBar = getSupportActionBar();

        if(actionBar != null){

            actionBar.setDisplayHomeAsUpEnabled(true);

        }


        if(recyclerView != null){

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);

            recyclerView.setLayoutManager(linearLayoutManager);


            refreshRecycler();


        }

        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(QuickstartPreferences.SEND)) {

                    refreshRecycler();
                }


            }
        };


    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!isReceiverMessageRegistered){
            LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver,
                    new IntentFilter(QuickstartPreferences.SEND));
            isReceiverMessageRegistered = true;
        }

    }

    @Override
    protected void onPause() {

        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);
        isReceiverMessageRegistered = false;


        super.onPause();
    }



    private void refreshRecycler(){

        List<AvanceGestionModel> list = getList();

        adaptar = new AvanceGestionRecyclerAdaptar(this, list, this);
        recyclerView.setAdapter(adaptar);
        recyclerView.setItemViewCacheSize(list.size());

    }




    private List<AvanceGestionModel> getList(){

        List<AvanceGestionModel> array = new ArrayList<>();
        SQLiteDatabase db = new BDopenHelper(this).getReadableDatabase();

        String sql = "select pct.idTienda, pct.idPromotor, pct.fecha_captura, pct.idProducto, p.nombre, p.presentacion, pct.cantidad, pct.firma, pct.estatus_registro, pct.folio  " +
                " from producto_catalogado_tienda as pct " +
                " left join producto as p on p.idProducto=pct.idProducto " +
                " where pct.idTienda=" + idTienda + "  and pct.estatus_producto=4 " +
                " group by pct.idTienda, pct.fecha_captura, pct.idProducto order by pct.fecha_captura desc";


        Cursor cursor = db.rawQuery(sql, null);

        if (cursor.getCount() > 0){


            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()){

                final AvanceGestionModel model = new AvanceGestionModel();

                model.setFecha(cursor.getString(cursor.getColumnIndex("fecha_captura")));
                model.setIdTienda(cursor.getInt(cursor.getColumnIndex("idTienda")));
                model.setIdProducto(cursor.getInt(cursor.getColumnIndex("idProducto")));

                String content = cursor.getString(cursor.getColumnIndex("nombre")) + " " + cursor.getString(cursor.getColumnIndex("presentacion")) +
                        " Cantidad: " + cursor.getInt(cursor.getColumnIndex("cantidad"));

                model.setContent(content);
                model.setFirma(cursor.getString(cursor.getColumnIndex("firma")));

                int estatus = cursor.getInt(cursor.getColumnIndex("estatus_registro"));

                if (estatus == 3){

                    model.setEstatus("firma enviada");


                }else if(estatus == 2){
                    model.setEstatus("no enviada");
                }


                model.setFolio(cursor.getInt(cursor.getColumnIndex("folio")));

                //Log.d("Avance en la gestion", " " + cursor.getInt(cursor.getColumnIndex("folio")));



                array.add(model);

            }


        }


        cursor.close();
        db.close();

        return array;
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
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_avance_gestion, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        int itemId = item.getItemId();
        if (itemId == android.R.id.home) {
            finish();
            return true;
        } else if (itemId == R.id.enviar) {
            sendFirmasToServer();


            return true;
        }
        return super.onOptionsItemSelected(item);

    }


    private void showDialogContrato(){

        FragmentManager fm = getSupportFragmentManager();


        DialogFragmentContrato dialog = DialogFragmentContrato.getIntance();

        dialog.setOnSignatureListener(this);

        dialog.show(fm, "dialog_contrato");


    }


    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.float_button) {
            showDialogContrato();
        }


    }


    private void firmarProductos(String firma){

        List<AvanceGestionModel> list = adaptar.getSelectedItems();


        if (list.size() > 0){

            SQLiteDatabase db = new BDopenHelper(this).getWritableDatabase();
            for (AvanceGestionModel model : list){

                ContentValues contentValues = new ContentValues();
                contentValues.put(DbEstructure.ProductoCatalogadoTienda.FIRMA, firma);

                db.update(DbEstructure.ProductoCatalogadoTienda.TABLE_NAME, contentValues, "" +
                        DbEstructure.ProductoCatalogadoTienda.ID_PRODUCTO + "=" + model.getIdProducto() + " and " +
                        DbEstructure.ProductoCatalogadoTienda.ID_TIENDA + "=" + model.getIdTienda() + " and " +
                        DbEstructure.ProductoCatalogadoTienda.FECHA_CAPTURA +"='" + model.getFecha() + "'", null);

            }



            sendFirmasToServer();


            refreshRecycler();

        }


    }


    private void sendFirmasToServer(){


        if (!Utilities.verificarConexion(this)){

            Toast.makeText(this, "no hay conexion a internet", Toast.LENGTH_SHORT).show();


        }


        SQLiteDatabase db = new BDopenHelper(this).getReadableDatabase();


        Cursor cursor = db.rawQuery("select * from producto_catalogado_tienda where firma " +
                          " is not null and estatus_registro <= 2 and estatus_producto = 4 " +
                          " and folio is null", null);

        if (cursor.getCount() > 0){

            List<AvanceGestionModel> lista = new ArrayList<>();

            for (cursor.moveToFirst(); !cursor.isAfterLast() ; cursor.moveToNext()){
                final AvanceGestionModel model = new AvanceGestionModel();


                model.setIdProducto(cursor.getInt(cursor.getColumnIndex("idProducto")));
                model.setIdTienda(cursor.getInt(cursor.getColumnIndex("idTienda")));
                model.setFecha(cursor.getString(cursor.getColumnIndex("fecha_captura")));
                model.setFirma(cursor.getString(cursor.getColumnIndex("firma")));
                model.setCantidad(cursor.getInt(cursor.getColumnIndex("cantidad")));



                lista.add(model);

            }


            JsonUpdateFirma json = new JsonUpdateFirma(idPromotor, lista);


            Gson gson = new Gson();

            AsyncHttpClient client = new AsyncHttpClient();

            RequestParams rp = new RequestParams();

            rp.put("solicitud", "update_firma_producto");
            rp.put("json", gson.toJson(json));

            //Log.d("Json", gson.toJson(json));

            Toast.makeText(this, "Generando Folio..", Toast.LENGTH_SHORT).show();


            client.post(this, Utilities.WEB_SERVICE_CODPAA + "update_producto_firma.php", rp, new ResponseUpdateFirmaProducto(this));


        }else{
            Toast.makeText(this, "no hay productos pendientes de folio", Toast.LENGTH_SHORT).show();
        }


        cursor.close();

    }





    @Override
    public void onCompleteSignature(String firma) {

        firmarProductos(firma);

    }
}
