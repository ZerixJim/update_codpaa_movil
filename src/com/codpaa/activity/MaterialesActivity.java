package com.codpaa.activity;

/*
 * Created by grim on 06/07/2016.
 */

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
import android.widget.TextView;
import android.widget.Toast;

import com.codpaa.R;
import com.codpaa.adapter.MaterialesSolicitudAdapter;
import com.codpaa.fragment.DialogMaterialRequest;
import com.codpaa.model.JsonMaterialModel;
import com.codpaa.model.MarcaModel;
import com.codpaa.model.MaterialModel;
import com.codpaa.response.MaterialesJsonResponse;
import com.codpaa.widget.DividerItemDecoration;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;


import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class MaterialesActivity extends AppCompatActivity implements View.OnClickListener, DialogMaterialRequest.SelectMaterial {


    RecyclerView recyclerMateriales;
    TextView txtVacio;
    List<MaterialModel> materialList = new ArrayList<>();
    FloatingActionButton floatingActionButton;

    private int idTienda, idPromor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_materiales);

        idTienda = getIntent().getIntExtra("idTienda", 0);
        idPromor = getIntent().getIntExtra("idPromotor", 0);


        // instaciar todos los widgets
        setUpWidgets();

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }




    }


    private void addItem(MaterialModel item){

        materialList.add(item);

        setUpRecyclerView();
    }


    private void setUpRecyclerView() {

        MaterialesSolicitudAdapter adapter = new MaterialesSolicitudAdapter(materialList);
        recyclerMateriales.setAdapter(adapter);
        recyclerMateriales.addItemDecoration(new DividerItemDecoration(this, null));

        if (materialList.size() > 0){
            txtVacio.setVisibility(View.INVISIBLE);
        } else {
            txtVacio.setVisibility(View.VISIBLE);
        }



    }

    private void setUpWidgets() {

        floatingActionButton = (FloatingActionButton) findViewById(R.id.float_add);
        txtVacio = (TextView) findViewById(R.id.txt_mensaje_vacio);

        if (floatingActionButton != null) {
            floatingActionButton.setOnClickListener(this);
        }


        recyclerMateriales = (RecyclerView) findViewById(R.id.recycler_material);

        if (recyclerMateriales != null){
            LinearLayoutManager linear = new LinearLayoutManager(this);

            recyclerMateriales.setLayoutManager(linear);



        }


    }


    private void showDialog(){
        FragmentManager ft = getSupportFragmentManager();

        // Create and show the dialog.
        DialogMaterialRequest newFragment = DialogMaterialRequest.newInstance();
        Bundle args = new Bundle();
        args.putInt("idPromotor", idPromor);
        args.putInt("idTienda", idTienda);
        newFragment.setArguments(args);
        newFragment.setOnMaterialListener(this);
        newFragment.show(ft, "dialog");
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_materiales, menu);




        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;

            case R.id.enviar:

                enviarDatos();

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void enviarDatos() {

        if (materialList.size() > 0){

            RequestParams reques = new RequestParams();
            reques.put("solicitud", "upload_materials");

            Gson gson = new Gson();
            JsonMaterialModel materialModel = new JsonMaterialModel();

            materialModel.setMateriales(materialList);
            materialModel.setIdPromotor(idPromor);


            reques.put("json", gson.toJson(materialModel));

            Log.d("json", gson.toJson(materialModel));

            AsyncHttpClient cliente = new AsyncHttpClient();

            MaterialesJsonResponse response = new MaterialesJsonResponse(this);
            cliente.get("http://plataformavanguardia.net/test/webservice/serv.php", reques, response);

            materialList.clear();
            setUpRecyclerView();


        }else {
            Toast.makeText(this, "No hay Materiales seleccionados", Toast.LENGTH_SHORT).show();
        }









    }

    @Override
    protected void onResume() {
        super.onResume();
    }



    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.float_add:


                showDialog();

                break;
        }
    }

    @Override
    public void onAddMaterial(MaterialModel model) {
        addItem(model);
    }

    @Override
    public void onTestId(int idMaterial) {



    }
}
