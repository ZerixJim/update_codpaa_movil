package com.codpaa.activity.impulsor;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.codpaa.R;
import com.codpaa.adapter.generic.AvanceGestionRecyclerAdaptar;
import com.codpaa.db.BDopenHelper;
import com.codpaa.model.AvanceGestionModel;

import java.util.ArrayList;
import java.util.List;

/*
 * Created by Grim on 21/05/2017.
 */

public class AvanceGestion extends AppCompatActivity implements AvanceGestionRecyclerAdaptar.ItemCheckListener{

    private int idPromotor, idTienda;
    private RecyclerView recyclerView;
    private FloatingActionButton floatingActionButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_avance_gestion);



        idPromotor = getIntent().getIntExtra("idPromotor", 0);
        idTienda = getIntent().getIntExtra("idTienda", 0);


        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        floatingActionButton = (FloatingActionButton) findViewById(R.id.float_button);

        if(recyclerView != null){

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);

            recyclerView.setLayoutManager(linearLayoutManager);

            List<AvanceGestionModel> list = getList();

            AvanceGestionRecyclerAdaptar adaptar = new AvanceGestionRecyclerAdaptar(this, list, this);
            recyclerView.setAdapter(adaptar);
            recyclerView.setItemViewCacheSize(list.size());

            Log.d("listSize", list.size() + " ");


        }


    }



    private List<AvanceGestionModel> getList(){
        List<AvanceGestionModel> array = new ArrayList<>();
        SQLiteDatabase db = new BDopenHelper(this).getReadableDatabase();

        String sql = "select pct.idTienda, pct.idPromotor, pct.fecha_captura, pct.idProducto, p.nombre, p.presentacion, pct.cantidad " +
                " from producto_catalogado_tienda as pct " +
                " left join producto as p on p.idProducto=pct.idProducto " +
                " where pct.idTienda=" + idTienda + "  and pct.estatus_producto=4 " +
                "group by pct.idTienda, pct.fecha_captura, pct.idProducto order by pct.fecha_captura desc";


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

                /*Cursor cursorProductos = db.rawQuery("select p.nombre, p.presentacion, pct.cantidad " +
                        " from producto_catalogado_tienda as pct " +
                        " left join producto as p on p.idProducto=pct.idProducto " +
                        " where pct.fecha_captura='" + cursor.getString(cursor.getColumnIndex("fecha_captura")) +  "' " +
                        " and pct.idTienda="  + cursor.getInt(cursor.getColumnIndex("idTienda")) +  "  " +
                        " and pct.idProducto=" + cursor.getInt(cursor.getColumnIndex("idProducto")) +
                        " and pct.estatus_producto=4", null);

                String list = "";

                for (cursorProductos.moveToFirst(); !cursorProductos.isAfterLast() ; cursorProductos.moveToNext()){

                    String data = cursorProductos.getString(cursorProductos.getColumnIndex("nombre")) + " " +
                            " " + cursorProductos.getString(cursorProductos.getColumnIndex("presentacion")) +
                            " Cantidad:"+ cursorProductos.getInt(cursorProductos.getColumnIndex("cantidad")) +
                            " \n";

                    list += data;


                }

                cursorProductos.close();

                model.setContent(list);*/

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
}
