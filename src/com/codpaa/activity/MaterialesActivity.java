package com.codpaa.activity;

/*
 * Created by grim on 06/07/2016.
 */

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.codpaa.R;
import com.codpaa.adapter.MarcasAdapter;
import com.codpaa.adapter.MaterialSpinnerAdapter;
import com.codpaa.adapter.ProductosCustomAdapter;
import com.codpaa.db.BDopenHelper;
import com.codpaa.model.MarcaModel;
import com.codpaa.model.MaterialModel;
import com.codpaa.model.SpinnerProductoModel;

import java.util.ArrayList;
import java.util.List;

public class MaterialesActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Spinner material, marca, producto;
    RecyclerView recyclerMateriales;
    Button btnAgregar;
    EditText cantidad;

    private int idTienda, idPromor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_materiales);

        idTienda = getIntent().getIntExtra("idTienda", 0);
        idPromor = getIntent().getIntExtra("idPromotor", 0);


        // instaciar todos los widgets
        setUpWidgets();

        // poblar spinner material
        setUpMaterial();

        //inicializar el listado de materiales agregados
        setUpRecyclerView();





    }

    private void setUpMaterial() {

        MaterialSpinnerAdapter adapter = new MaterialSpinnerAdapter(this,
                android.R.layout.simple_spinner_item, getMaterials());

        if (material != null){
            material.setAdapter(adapter);
            material.setOnItemSelectedListener(this);
        }

    }

    private List<MaterialModel> getMaterials() {
        List<MaterialModel> array = new ArrayList<>();

        SQLiteDatabase db = new BDopenHelper(this).getReadableDatabase();
        String sql = "select idMaterial, material,  unidad, solicitudMaxima, tipo_material from materiales";


        Cursor cursor = db.rawQuery(sql, null);

        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()){
            final MaterialModel material = new MaterialModel();

            material.setNombreMaterial(cursor.getString(cursor.getColumnIndex("material")));

            material.setIdMaterial(cursor.getInt(cursor.getColumnIndex("idMaterial")));
            material.setIdTipoMaterial(cursor.getInt(cursor.getColumnIndex("tipo_material")));
            material.setUnidad(cursor.getString(cursor.getColumnIndex("unidad")));
            material.setSolicitudMaxima(cursor.getInt(cursor.getColumnIndex("solicitudMaxima")));

            array.add(material);
        }

        final MaterialModel materialDefault = new MaterialModel();
        materialDefault.setNombreMaterial("Selecciona Material");
        materialDefault.setIdMaterial(0);

        array.add(0, materialDefault);


        cursor.close();
        return array;
    }

    private void setUpRecyclerView() {



    }

    private void setUpWidgets() {

        material = (Spinner) findViewById(R.id.material);
        producto = (Spinner) findViewById(R.id.producto);
        marca = (Spinner) findViewById(R.id.marca);
        cantidad = (EditText) findViewById(R.id.cantidad);

        recyclerMateriales = (RecyclerView) findViewById(R.id.recycler_material);

        if (recyclerMateriales != null){
            LinearLayoutManager linear = new LinearLayoutManager(this);

            recyclerMateriales.setLayoutManager(linear);



        }


    }


    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_materiales, menu);


        MenuItem item = menu.findItem(R.id.menu_spinner);
        Spinner spinner = (Spinner) MenuItemCompat.getActionView(item);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, new String[]{"marca 1", "marca 2"});

        spinner.setAdapter(adapter);



        return true;
    }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        Spinner spinner = (Spinner) adapterView;

        switch (spinner.getId()){
            case R.id.material:

                Log.d("Entro", "materiales listener");
                MaterialModel material = (MaterialModel) adapterView.getSelectedItem();

                if (material.getUnidad() != null)
                    cantidad.setHint( "Maximo: " + material.getSolicitudMaxima() + material.getUnidad()+"s");


                if (material.getIdTipoMaterial() == 2){
                    producto.setVisibility(View.VISIBLE);
                    marca.setVisibility(View.VISIBLE);
                    marca.setOnItemSelectedListener(this);
                    loadSpinnerMarca();
                    if (material.getIdMaterial() == 18){


                        marca.setVisibility(View.GONE);
                        producto.setVisibility(View.VISIBLE);
                    }

                }else {
                    producto.setVisibility(View.GONE);
                }

                break;

            case R.id.marca:


                MarcaModel marca = (MarcaModel) adapterView.getSelectedItem();
                productoSpinner(marca.getId());

                break;
        }



    }

    private void loadSpinnerMarca(){
        try {


            MarcasAdapter adapter = new MarcasAdapter(this, android.R.layout.simple_spinner_item, getArrayList());
            marca.setAdapter(adapter);

        }catch(Exception e) {
            Toast.makeText(this, "Error Mayoreo 3", Toast.LENGTH_SHORT).show();
        }

    }
    //
    private ArrayList<MarcaModel> getArrayList(){

        SQLiteDatabase base = new BDopenHelper(this).getReadableDatabase();
        ArrayList<MarcaModel> array = new ArrayList<>();
        String sql = "select idMarca as _id, nombre, img from marca order by nombre asc;";
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

    private void productoSpinner(int idMarca){

        ProductosCustomAdapter adapter = new ProductosCustomAdapter(this, android.R.layout.simple_spinner_item, getArrayListProByTienda(idMarca,idTienda));
        producto.setAdapter(adapter);

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }


    private ArrayList<SpinnerProductoModel> getArrayListProByTienda(int idMarca, int idTienda){

        Cursor curProByTienda = new BDopenHelper(this).getProductosByTienda(idMarca, idTienda);
        ArrayList<SpinnerProductoModel> arrayP = new ArrayList<>();
        if (curProByTienda.getCount() <= 0){

            Cursor curPro = new BDopenHelper(this).productos(idMarca);

            for(curPro.moveToFirst(); !curPro.isAfterLast(); curPro.moveToNext()){
                final SpinnerProductoModel spP = new SpinnerProductoModel();
                spP.setIdProducto(curPro.getInt(0));
                spP.setNombre(curPro.getString(1));
                spP.setPresentacion(curPro.getString(2));
                spP.setCodigoBarras(curPro.getString(3));
                spP.setIdMarca(curPro.getInt(4));
                arrayP.add(spP);
            }


            curPro.close();
        } else {

            for(curProByTienda.moveToFirst(); !curProByTienda.isAfterLast(); curProByTienda.moveToNext()){
                final SpinnerProductoModel spP = new SpinnerProductoModel();
                spP.setIdProducto(curProByTienda.getInt(0));
                spP.setNombre(curProByTienda.getString(1));
                spP.setPresentacion(curProByTienda.getString(2));
                spP.setCodigoBarras(curProByTienda.getString(3));
                spP.setIdMarca(curProByTienda.getInt(4));
                arrayP.add(spP);
            }

        }



        final SpinnerProductoModel spPinicio = new SpinnerProductoModel();
        spPinicio.setIdProducto(0);
        spPinicio.setNombre("Seleccione Producto");
        spPinicio.setPresentacion("producto sin seleccionar");
        spPinicio.setCodigoBarras(" ");

        arrayP.add(0,spPinicio);

        curProByTienda.close();
        return arrayP;

    }
}
