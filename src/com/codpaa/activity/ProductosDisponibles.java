package com.codpaa.activity;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

import com.codpaa.update.EnviarDatos;
import com.codpaa.widget.SingleSpinnerSelect;

import com.codpaa.R;
import com.codpaa.adapter.MarcasAdapter;
import com.codpaa.db.BDopenHelper;
import com.codpaa.model.MarcaModel;
import com.codpaa.model.ProductosModel;
import com.codpaa.model.SpinnerMarcaModel;
import com.codpaa.provider.DbEstructure;
import com.codpaa.widget.MultiSpinnerSelect;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;

import com.codpaa.activity.PhotoCapture;
import com.loopj.android.http.AsyncHttpClient;

public class ProductosDisponibles extends AppCompatActivity implements OnItemSelectedListener{

    private Spinner spinnerMarca;
    private Button btnEnviar;
    private MultiSpinnerSelect multiSpinnerSelect;
    private int idPromotor, idTienda;
    private SQLiteDatabase base, base2;
    private final ArrayList<MarcaModel> array = new ArrayList<>();

    protected void onCreate(Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.productos_disponibles);

        Intent i = getIntent();
        idTienda = i.getIntExtra("idTienda", 0);
        idPromotor = i.getIntExtra("idPromotor", 0);

        multiSpinnerSelect = findViewById(R.id.multi_spinner);
        spinnerMarca = findViewById(R.id.spiMar);
        btnEnviar = (Button) findViewById(R.id.btnEnviar);


        spinnerMarca.setOnItemSelectedListener(this);
        loadSpinner();



        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enviarReporte();
            }
        });
    }


    public void enviarReporte() {
        try {
            Calendar c = Calendar.getInstance();
            SimpleDateFormat dFecha = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
            MarcaModel marca = (MarcaModel) spinnerMarca.getSelectedItem();
            ArrayList<ProductosModel> selected = multiSpinnerSelect.getSelectedItems();

            String fecha = dFecha.format(c.getTime());

            int idMarca = marca.getId();


            if(idMarca != 0){
                if(selected.size() >= 1){

                    for (ProductosModel producto : selected) {
                        int idProducto = producto.getIdProducto();
                        new BDopenHelper(getApplicationContext()).insertarProdDisp(idTienda, idMarca, idPromotor, idProducto, fecha);
                    }
                    new EnviarDatos(getApplicationContext()).enviarProdDisp();
                    Toast.makeText(getApplicationContext(), "Producto enviado", Toast.LENGTH_SHORT).show();
                    spinnerMarca.setSelection(0);


                } else{
                    Toast.makeText(getApplicationContext(), "Selecciona al menos un producto", Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(getApplicationContext(), "Selecciona una marca", Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            e.printStackTrace();

            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        MarcaModel marca = (MarcaModel) adapterView.getSelectedItem();

        int idMarca = marca.getId();
        //String marcaString = Integer.toString(idMarca);

        loadMultiSpinner(idMarca);
        /*if(idMarca != 0){
            Toast.makeText(getApplicationContext(), marcaString, Toast.LENGTH_SHORT).show();
            Log.v("prueba", marcaString);
        } else{
        }*/

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {}

    public void loadMultiSpinner(int idMarca){

        multiSpinnerSelect.setItems(getArrayListProByTiensda(idMarca, idTienda),
                "Seleccione Producto");

    }




    private ArrayList<ProductosModel> getArrayListProByTiensda(int idMarca, int idTienda){

        Cursor curProByTienda = new BDopenHelper(this).getProductosByTienda(idMarca, idTienda);
        ArrayList<ProductosModel> arrayP = new ArrayList<>();
        if (curProByTienda.getCount() <= 0){

            Cursor curPro = new BDopenHelper(this).productos(idMarca);

            for(curPro.moveToFirst(); !curPro.isAfterLast(); curPro.moveToNext()){
                final ProductosModel spP = new ProductosModel();
                spP.setIdProducto(curPro.getInt(0));
                spP.setNombre(curPro.getString(1));
                spP.setPresentacion(curPro.getString(2));
                spP.setCodigoBarras(curPro.getString(3));
                spP.setIdMarca(curPro.getInt(4));
                spP.setHasImage(curPro.getInt(curPro.getColumnIndex("has_image")));



                arrayP.add(spP);
            }


            curPro.close();
        } else {

            Cursor curProByTienda2 = new BDopenHelper(this).getProductosByTienda(idMarca, idTienda);

            for(curProByTienda2.moveToFirst(); !curProByTienda2.isAfterLast(); curProByTienda2.moveToNext()){
                final ProductosModel spP = new ProductosModel();
                spP.setIdProducto(curProByTienda2.getInt(0));
                spP.setNombre(curProByTienda2.getString(1));
                spP.setPresentacion(curProByTienda2.getString(2));
                spP.setCodigoBarras(curProByTienda2.getString(3));
                spP.setIdMarca(curProByTienda2.getInt(4));
                spP.setHasImage(curProByTienda2.getInt(curProByTienda.getColumnIndex("has_image")));


                arrayP.add(spP);
            }

            curProByTienda2.close();

        }



        final ProductosModel spPinicio = new ProductosModel();
        spPinicio.setIdProducto(0);
        spPinicio.setNombre("Seleccione Producto");
        spPinicio.setPresentacion("producto sin seleccionar");
        spPinicio.setCodigoBarras(" ");

        arrayP.add(0,spPinicio);


        base.close();
        return arrayP;

    }

    private void loadSpinner(){
        try {
            MarcasAdapter adapter = new MarcasAdapter(this, android.R.layout.simple_spinner_item, getArrayList());
            spinnerMarca.setAdapter(adapter);

        }catch(Exception e) {
            Toast.makeText(this, "Error Mayoreo 3", Toast.LENGTH_SHORT).show();
        }
    }

    private ArrayList<MarcaModel> getArrayList(){

        base = new BDopenHelper(this).getReadableDatabase();
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
}
