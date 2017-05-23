package com.codpaa.activity.impulsor;

import android.content.ContentValues;
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
import android.view.Window;

import com.codpaa.R;
import com.codpaa.adapter.generic.AvanceGestionRecyclerAdaptar;
import com.codpaa.db.BDopenHelper;
import com.codpaa.fragment.DialogFragmentContrato;
import com.codpaa.model.AvanceGestionModel;
import com.codpaa.model.JsonUpdateFirma;
import com.codpaa.provider.DbEstructure;
import com.codpaa.response.ResponseUpdateFirmaProducto;
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

        String sql = "select pct.idTienda, pct.idPromotor, pct.fecha_captura, pct.idProducto, p.nombre, p.presentacion, pct.cantidad, pct.firma " +
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


        switch (item.getItemId()){

            case android.R.id.home:
                finish();
                return true;

            case R.id.enviar:




                return true;


            default:
                return super.onOptionsItemSelected(item);
        }

    }


    private void showDialogContrato(){

        FragmentManager fm = getSupportFragmentManager();


        DialogFragmentContrato dialog = DialogFragmentContrato.getIntance();

        dialog.setOnSignatureListener(this);

        dialog.show(fm, "dialog_contrato");


    }


    @Override
    public void onClick(View v) {

        switch (v.getId()){


            case R.id.float_button:

                showDialogContrato();


                break;


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




        SQLiteDatabase db = new BDopenHelper(this).getReadableDatabase();


        Cursor cursor = db.rawQuery("select * from producto_catalogado_tienda where firma is not null  and estatus_registro=2", null);

        if (cursor.getCount() > 0){

            List<AvanceGestionModel> lista = new ArrayList<>();

            for (cursor.moveToFirst(); !cursor.isAfterLast() ; cursor.moveToNext()){
                final AvanceGestionModel model = new AvanceGestionModel();


                model.setIdProducto(cursor.getInt(cursor.getColumnIndex("idProducto")));
                model.setIdTienda(cursor.getInt(cursor.getColumnIndex("idTienda")));
                model.setFecha(cursor.getString(cursor.getColumnIndex("fecha_captura")));
                model.setFirma(cursor.getString(cursor.getColumnIndex("firma")));


                lista.add(model);

            }


            JsonUpdateFirma json = new JsonUpdateFirma();

            json.setList(lista);

            Gson gson = new Gson();

            AsyncHttpClient client = new AsyncHttpClient();

            RequestParams rp = new RequestParams();

            rp.put("solicitud", "update_firma_producto");
            rp.put("json", gson.toJson(json));

            Log.d("Json", gson.toJson(json));

            client.post(this, Utilities.WEB_SERVICE_CODPAA_TEST + "update_producto_firma.php", rp, new ResponseUpdateFirmaProducto(this));



        }


        cursor.close();

    }





    @Override
    public void onCompleteSignature(String firma) {

        firmarProductos(firma);

    }
}
