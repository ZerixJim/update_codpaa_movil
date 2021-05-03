package com.codpaa.activity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.codpaa.R;
import com.codpaa.adapter.AgotadosRecyclerAdapter;
import com.codpaa.adapter.MarcasAdapter;
import com.codpaa.db.BDopenHelper;
import com.codpaa.model.MarcaModel;
import com.codpaa.model.ProductosModel;
import com.codpaa.update.EnviarDatos;
import com.codpaa.util.Utilities;


import java.util.ArrayList;
import java.util.List;


public class Agotados extends AppCompatActivity implements AdapterView.OnItemSelectedListener {


    private int idPromotor, idTienda;
    private Spinner spinnerMarca;
    private RecyclerView recyclerView;
    private TextView txtAvailable;
    private AgotadosRecyclerAdapter adapter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.agotados_layout);



        Toolbar toolbar = findViewById(R.id.toolbar_agotados);

        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);

        }



        idPromotor = getIntent().getIntExtra("idPromotor", 0);
        idTienda = getIntent().getIntExtra("idTienda", 0);



        txtAvailable = findViewById(R.id.available);




        recyclerView = findViewById(R.id.recycler_producto);
        //recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        spinnerMarca = findViewById(R.id.marca);
        spinnerMarca.setOnItemSelectedListener(this);

        //load spinner marca
        // load first
        loadSpinner();



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_agotados, menu);


        return true;


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if (id == android.R.id.home){
            finish();
            return true;

        }else if(id == R.id.save){

            saveData();
            return true;

        } else

            return super.onOptionsItemSelected(item);
    }

    private void loadSpinner(){


        MarcasAdapter adapter = new MarcasAdapter(this,
                android.R.layout.simple_spinner_item, getBrands());

        spinnerMarca.setAdapter(adapter);



    }


    private void loadProductsByMarca(int idMarca){

        try {
            ArrayList<ProductosModel> list = getProducts(idMarca);

            if (list.size() > 0 ){
                txtAvailable.setVisibility(View.INVISIBLE);
            }else{
                txtAvailable.setVisibility(View.VISIBLE);
            }

            adapter = new AgotadosRecyclerAdapter(this,list);

            recyclerView.setItemViewCacheSize(list.size());
            recyclerView.setAdapter(adapter);
        }catch (Exception e){
            e.printStackTrace();
        }

    }


    private ArrayList<MarcaModel> getBrands(){
        ArrayList<MarcaModel> array = new ArrayList<>();

        SQLiteDatabase bd = new BDopenHelper(this).getReadableDatabase();
        String sql = "select m.nombre, m.idMarca  from marca m " +
                " left join producto p on (p.idMarca = m.idMarca ) " +
                " where p.agotado = 1" +
                " group by m.idMarca ";
        Cursor cursorMarca = bd.rawQuery(sql, null);

        for(cursorMarca.moveToFirst(); !cursorMarca.isAfterLast(); cursorMarca.moveToNext()){

            final MarcaModel spiM = new MarcaModel();
            spiM.setNombre(cursorMarca.getString(cursorMarca.getColumnIndex("nombre")));
            spiM.setId(cursorMarca.getInt(cursorMarca.getColumnIndex("idMarca")));

            array.add(spiM);
        }

        final MarcaModel spiMfirst = new MarcaModel();
        spiMfirst.setNombre("Selecciona Marca");
        spiMfirst.setId(0);

        array.add(0,spiMfirst);

        cursorMarca.close();
        bd.close();


        return array;

    }



    private ArrayList<ProductosModel> getProducts(int idMarca){
        ArrayList<ProductosModel> array = new ArrayList<>();

        SQLiteDatabase bd = new BDopenHelper(this).getReadableDatabase();
        String sql = "select p.idProducto, p.nombre, p.presentacion, p.idMarca, p.cb, p.has_image, case when sa.id_producto is null then 0 else 1 end as captura , sa.estatus," +
                "  case when sa.estatus_producto is null then 0 else sa.estatus_producto end as ep  " +
                " from producto p " +
                " left join solicitud_agotados sa " +
                " on (sa.id_producto = p.idProducto and  strftime('%Y-%m-%d',sa.fecha) = date('now') and sa.id_tienda = "+ idTienda +" ) " +
                "  where p.idMarca = "+ idMarca +" and p.agotado = 1 " +
                " group by p.idProducto";
        Cursor curProdu = bd.rawQuery(sql, null);

        for(curProdu.moveToFirst(); !curProdu.isAfterLast(); curProdu.moveToNext()){

            final ProductosModel pro = new ProductosModel();

            pro.setIdProducto(curProdu.getInt(curProdu.getColumnIndex("idProducto")));
            pro.setNombre(curProdu.getString(curProdu.getColumnIndex("nombre")));
            pro.setPresentacion(curProdu.getString(curProdu.getColumnIndex("presentacion")));
            pro.setIdMarca(curProdu.getInt(curProdu.getColumnIndex("idMarca")));
            pro.setCodigoBarras(curProdu.getString(curProdu.getColumnIndex("cb")));
            pro.setHasImage(curProdu.getInt(curProdu.getColumnIndex("has_image")));


            pro.setCapturated(curProdu.getInt(curProdu.getColumnIndex("captura")));

            pro.setTextStatus(curProdu.getInt(curProdu.getColumnIndex("ep")));


            array.add(pro);
        }
/*
        final ProductosModel spiMfirst = new ProductosModel();
        spiMfirst.setNombre("Selecciona Marca");
        spiMfirst.setIdProducto(0);

        array.add(0,spiMfirst);*/

        curProdu.close();
        bd.close();


        return array;
    }



    private void saveData(){


        if (adapter != null){

            String fecha = Utilities.getDateTime();

            List<ProductosModel> li = adapter.getItemsModified();

            if (li.size() > 0){
                Toast.makeText(this, "Enviando", Toast.LENGTH_SHORT).show();
                BDopenHelper bd = new BDopenHelper(this);
                for (ProductosModel p :li){

                    bd.insertAgotados(idTienda, idPromotor, p.getIdProducto(),p.getIdStatusProduct(), fecha);
                }
            }else{

                Toast.makeText(this, "Producto no modificado", Toast.LENGTH_SHORT).show();

            }


            try {
                MarcaModel model = (MarcaModel) spinnerMarca.getSelectedItem();

                adapter.setData(getProducts(model.getId()));
            }catch (Exception e){
                e.printStackTrace();
            }


        }

        EnviarDatos en = new EnviarDatos(this);
        en.sendAgotados();
    }



    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    protected void onStart() {
        super.onStart();
    }


    @Override
    protected void onPause() {
        super.onPause();
    }


    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {


        try{
            MarcaModel marca = (MarcaModel) adapterView.getSelectedItem();

            loadProductsByMarca(marca.getId());

        }catch (Exception e){ e.printStackTrace(); }



    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
