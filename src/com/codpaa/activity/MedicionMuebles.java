package com.codpaa.activity;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import android.app.backup.BackupDataOutput;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.codpaa.adapter.CategoriasProductoAdapter;
import com.codpaa.adapter.MarcasAdapter;
import com.codpaa.adapter.MedicionAdapter;
import com.codpaa.model.MarcaModel;
import com.codpaa.model.ProductosModel;
import com.codpaa.model.SpinnerCateProdModel;
import com.codpaa.model.SpinnerMarcaModel;
import com.codpaa.update.EnviarDatos;
import com.codpaa.adapter.ProductosCustomAdapter;
import com.codpaa.R;
import com.codpaa.model.SpinnerProductoModel;
import com.codpaa.db.BDopenHelper;
import com.codpaa.widget.SingleSpinnerSelect;
import com.google.android.material.textfield.TextInputLayout;

public class MedicionMuebles extends AppCompatActivity implements OnClickListener, OnItemSelectedListener{

    private int idPromotor, idTienda;

    private EditText editCantidad, medicion1, medicion2, medicion3;
    BDopenHelper baseH;
    Toolbar toolbar;

    private ArrayList<MarcaModel> array = new ArrayList<>();
    private SQLiteDatabase base;
    private Spinner spiMarca, spinnerCategoria;
    private TextView marca1, marca2, marca3, catecap1, catecap2, catecap3, catecap4, catecap5, mensaje;
    private ImageView editCapt1, editCapt2, editCapt3, editCapt4, editCapt5;
    private Button guardarMedicion, editarMedicion;
    private ConstraintLayout bloque1, bloque2, bloque3, bloque4, bloque5, bloque6, bloque7, bloque8;
    private TextInputLayout bloqueMedicion;
    //private SingleSpinnerSelect spinnerCategoria;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.medicion_muebles_2);

        toolbar = findViewById(R.id.toolbar_frentes);

        if(toolbar != null){
            setSupportActionBar(toolbar);
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null){
                actionBar.setDisplayHomeAsUpEnabled(true);

            }
        }


        Intent i = getIntent();
        idTienda = i.getIntExtra("idTienda", 0);
        idPromotor = i.getIntExtra("idPromotor", 0);

        //SPINNERS
        spiMarca =  findViewById(R.id.spinnerMarca2);
        spinnerCategoria = findViewById(R.id.spinner_categorias_prod);

        //TEXTVIEW DE MARCAS
        marca1 = findViewById(R.id.marca1);
        marca2 = findViewById(R.id.marca2);
        marca3 = findViewById(R.id.marca3);

        //TEXTVIEWS DE CATEGORÍAS CAPTURADAS
        catecap1 = findViewById(R.id.cateCap1);
        catecap2 = findViewById(R.id.cateCap2);
        catecap3 = findViewById(R.id.cateCap3);
        catecap4 = findViewById(R.id.cateCap4);
        catecap5 = findViewById(R.id.cateCap5);

        //IMAGEVIEWS PARA EDITAR CATEGORÍAS CAPTURADAS
        editCapt1 = findViewById(R.id.editarCate1);
        editCapt2 = findViewById(R.id.editarCate2);
        editCapt3 = findViewById(R.id.editarCate3);
        editCapt4 = findViewById(R.id.editarCate4);
        editCapt5 = findViewById(R.id.editarCate5);

        //TEXTVIEW DEL MENSAJE DE CATEGORÍAS CAPTURADAS
        mensaje = findViewById(R.id.mensajeCategoriasCapt);

        //BOTONES
        guardarMedicion = findViewById(R.id.guardarMedicion);
        editarMedicion = findViewById(R.id.btnEditarMedicion);

        //TEXTINPUTS DE MEDICIONES
        editCantidad = findViewById(R.id.cantidadProd);
        medicion1 = findViewById(R.id.medida_marca1);
        medicion2 = findViewById(R.id.medida_marca2);
        medicion3 = findViewById(R.id.medida_marca3);

        //RELATIVE LAYOUTS
        bloque1 = findViewById(R.id.marcaBloque1);
        bloque2 = findViewById(R.id.marcaBloque2);
        bloque3 = findViewById(R.id.marcaBloque3);
        bloque4 = findViewById(R.id.captBloque1);
        bloque5 = findViewById(R.id.captBloque2);
        bloque6 = findViewById(R.id.captBloque3);
        bloque7 = findViewById(R.id.captBloque4);
        bloque8 = findViewById(R.id.captBloque5);
        bloqueMedicion = findViewById(R.id.bloqueEditarMed);

        //ONCLICKLISTENER
        spinnerCategoria.setOnItemSelectedListener(this);
        guardarMedicion.setOnClickListener(this);
        editarMedicion.setOnClickListener(this);
        editCapt1.setOnClickListener(this);
        editCapt2.setOnClickListener(this);
        editCapt3.setOnClickListener(this);
        editCapt4.setOnClickListener(this);
        editCapt5.setOnClickListener(this);

        //FUNCIÓN PARA LLENAR SPINNER DE CATEGORÍA
        spinnerCate();



        try {

            baseH = new BDopenHelper(this);

            try {
                /*
                Cursor cTienda = baseH.tienda(idTienda);
                cTienda.moveToFirst();
                if(getSupportActionBar() != null){
                    getSupportActionBar().setSubtitle(cTienda.getString(0)+" "+cTienda.getString(1));
                }
                cTienda.close();*/
                try {
                    loadSpinner();
                }catch(Exception e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Error Frentes 3", Toast.LENGTH_SHORT).show();
                }
            }catch(Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "Error Frentes 2", Toast.LENGTH_SHORT).show();
            }
        }catch(Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error Frentes 1", Toast.LENGTH_SHORT).show();
        }
        /*
         * keyboard soft hide
         */
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);


    }

  @Override
    protected void onPause() {
        super.onPause();

    }

   @Override
    protected void onPostResume() {
        super.onPostResume();

        Calendar c = Calendar.getInstance();
        SimpleDateFormat dFecha = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());

        String fecha = dFecha.format(c.getTime());

        int cont, medicionPorCategoria;
        cont = new BDopenHelper(this).cuentaMediciones(idPromotor, idTienda, fecha);

        if(cont > 0){
            mensaje.setVisibility(View.GONE);

            for(int i=1; i<6; i++){
                medicionPorCategoria = new BDopenHelper(this).existenciaMedicionCategoria(idPromotor, idTienda, i, fecha);

                if(medicionPorCategoria > 0){
                    if(i==1){
                        bloque4.setVisibility(View.VISIBLE);
                        catecap1.setText("BLEACH / BLANQUEADORES");
                    }else if(i==2){
                        bloque5.setVisibility(View.VISIBLE);
                        catecap2.setText("LIMPIADORES");
                    }else if(i==3){
                        bloque6.setVisibility(View.VISIBLE);
                        catecap3.setText("LAUNDRY");
                    }else if(i==4){
                        bloque7.setVisibility(View.VISIBLE);
                        catecap4.setText("CDW");
                    }else if(i==5){
                        bloque8.setVisibility(View.VISIBLE);
                        catecap5.setText("OTROS");
                    }
                }
            }
        }else{
            mensaje.setVisibility(View.VISIBLE);
        }

        /*Toast.makeText(this, String.valueOf(idPromotor), Toast.LENGTH_SHORT).show();
        Toast.makeText(this, String.valueOf(idTienda), Toast.LENGTH_SHORT).show();
        Toast.makeText(this, fecha, Toast.LENGTH_SHORT).show();
        Toast.makeText(this, String.valueOf(cont), Toast.LENGTH_SHORT).show();*/
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        int itemId = item.getItemId();
        if (itemId == android.R.id.home) {
            this.finish();
            return true;
        } else if (itemId == R.id.save_frentes) {
            guardarDatos();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_frentes, menu);
        return true;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.buttonExhib) {
            finish();
        }else if(v.getId() == R.id.guardarMedicion){
            guardarDatosLocal();
        }else if(v.getId() == R.id.btnEditarMedicion){
            editarDatosLocal();
        }else if(v.getId() == R.id.editarCate1){
            editarMedicionCategoria(1);
        }
        else if(v.getId() == R.id.editarCate2){
            editarMedicionCategoria(2);
        }
        else if(v.getId() == R.id.editarCate3){
            editarMedicionCategoria(3);
        }
        else if(v.getId() == R.id.editarCate4){
            editarMedicionCategoria(4);
        }
        else if(v.getId() == R.id.editarCate5){
            editarMedicionCategoria(5);
        }

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(position == 1){
            spiMarca.setVisibility(View.GONE);
            bloqueMedicion.setVisibility(View.GONE);

            bloque1.setVisibility(View.VISIBLE);
            bloque2.setVisibility(View.VISIBLE);
            bloque3.setVisibility(View.VISIBLE);

            marca1.setText("Clorox");
            marca2.setText("Cloralex");
            marca3.setText("Otras marcas");
        }else if(position == 2){
            spiMarca.setVisibility(View.GONE);
            bloqueMedicion.setVisibility(View.GONE);

            bloque1.setVisibility(View.VISIBLE);
            bloque2.setVisibility(View.VISIBLE);
            bloque3.setVisibility(View.VISIBLE);

            marca1.setText("Clorox (Poett)");
            marca2.setText("Brasso + Flash");
            marca3.setText("Otras marcas");
        }else if(position == 3){
            spiMarca.setVisibility(View.GONE);
            bloqueMedicion.setVisibility(View.GONE);

            bloque1.setVisibility(View.VISIBLE);
            bloque2.setVisibility(View.VISIBLE);
            bloque3.setVisibility(View.VISIBLE);

            marca1.setText("Clorox");
            marca2.setText("Vanish");
            marca3.setText("Otras marcas");
        }else if(position == 4){
            spiMarca.setVisibility(View.GONE);
            bloqueMedicion.setVisibility(View.GONE);

            bloque1.setVisibility(View.VISIBLE);
            bloque2.setVisibility(View.VISIBLE);
            bloque3.setVisibility(View.VISIBLE);

            marca1.setText("Clorox");
            marca2.setText("Lysol + Cloralex");
            marca3.setText("Otras marcas");
        }else if(position == 5){
            spiMarca.setVisibility(View.VISIBLE);
            bloqueMedicion.setVisibility(View.VISIBLE);

            //editCantidad.setText("");

            bloque1.setVisibility(View.GONE);
            bloque2.setVisibility(View.GONE);
            bloque3.setVisibility(View.GONE);
        }

        /*medicion1.setText("");
        medicion2.setText("");
        medicion3.setText("");*/
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void guardarDatosLocal(){
        int cantidad, cantidad1, cantidad2, cantidad3;
        int medicionPorCategoria;
        int flagGuardarDatos;

        SpinnerCateProdModel spPm = (SpinnerCateProdModel) spinnerCategoria.getSelectedItem();
        MarcaModel spm = (MarcaModel) spiMarca.getSelectedItem();

        //LECTURA DE SPINNERS Y ENTRADAS DE TEXTO
        int idMarca = spm.getId();
        int idCate = spPm.getId();

        //MOSTRAR MENSAJES AL USUARIO
        if(idCate < 5){
            if(medicion1.getText().length() <= 0 || medicion2.getText().length() <= 0 || medicion3.getText().length() <= 0){
                flagGuardarDatos = 0;
                Toast.makeText(this, "Todas las mediciones deben ser capturadas", Toast.LENGTH_SHORT).show();
            }else{
                flagGuardarDatos = 1;
            }
        }else{
            if(editCantidad.getText().length() <= 0){
                flagGuardarDatos = 0;
                Toast.makeText(this, "La medición debe ser capturada", Toast.LENGTH_SHORT).show();
            }else{
                flagGuardarDatos = 1;
            }
        }

        //SE PROCEDE A GUARDAR LOS DATOS
        if(flagGuardarDatos == 1){
            try {
                Calendar c = Calendar.getInstance();
                SimpleDateFormat dFecha = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                String fecha = dFecha.format(c.getTime());

                medicionPorCategoria = new BDopenHelper(this).existenciaMedicionCategoria(idPromotor, idTienda, idCate, fecha);

                if(medicionPorCategoria <= 0) {
                    cantidad = editCantidad.getText().length() > 0 ? Integer.parseInt(editCantidad.getText().toString()) : 0;
                    cantidad1 = medicion1.getText().length() > 0 ? Integer.parseInt(medicion1.getText().toString()) : 0;
                    cantidad2 = medicion2.getText().length() > 0 ? Integer.parseInt(medicion2.getText().toString()) : 0;
                    cantidad3 = medicion3.getText().length() > 0 ? Integer.parseInt(medicion3.getText().toString()) : 0;

                    try {
                        if (idCate > 0 && idCate <= 4) {
                            //SE VERIFICA QUE SE HAYA REGISTRADO UN VALOR EN CADA ENTRADA DENTRO DE LAS 4 PRIMERAS CATEGORÍAS
                            //Y SE GUARDA EN LA BD LOCAL
                            if (cantidad1 >= 0 && cantidad2 >= 0 && cantidad3 >= 0) {
                                if (idCate == 1) {
                                    new BDopenHelper(this).insertarMedicionMuebles(idTienda, idPromotor, 249, idCate, cantidad1, fecha);
                                    new BDopenHelper(this).insertarMedicionMuebles(idTienda, idPromotor, 356, idCate, cantidad2, fecha);
                                    new BDopenHelper(this).insertarMedicionMuebles(idTienda, idPromotor, 360, idCate, cantidad3, fecha);

                                    bloque4.setVisibility(View.VISIBLE);
                                    catecap1.setText("BLEACH / BLANQUEADORES");
                                } else if (idCate == 2) {
                                    new BDopenHelper(this).insertarMedicionMuebles(idTienda, idPromotor, 249, idCate, cantidad1, fecha);
                                    new BDopenHelper(this).insertarMedicionMuebles(idTienda, idPromotor, 359, idCate, cantidad2, fecha);
                                    new BDopenHelper(this).insertarMedicionMuebles(idTienda, idPromotor, 360, idCate, cantidad3, fecha);

                                    bloque5.setVisibility(View.VISIBLE);
                                    catecap2.setText("LIMPIADORES");
                                } else if (idCate == 3) {
                                    new BDopenHelper(this).insertarMedicionMuebles(idTienda, idPromotor, 249, idCate, cantidad1, fecha);
                                    new BDopenHelper(this).insertarMedicionMuebles(idTienda, idPromotor, 357, idCate, cantidad2, fecha);
                                    new BDopenHelper(this).insertarMedicionMuebles(idTienda, idPromotor, 360, idCate, cantidad3, fecha);

                                    bloque6.setVisibility(View.VISIBLE);
                                    catecap3.setText("LAUNDRY");
                                } else if (idCate == 4) {
                                    new BDopenHelper(this).insertarMedicionMuebles(idTienda, idPromotor, 249, idCate, cantidad1, fecha);
                                    new BDopenHelper(this).insertarMedicionMuebles(idTienda, idPromotor, 358, idCate, cantidad2, fecha);
                                    new BDopenHelper(this).insertarMedicionMuebles(idTienda, idPromotor, 360, idCate, cantidad3, fecha);

                                    bloque7.setVisibility(View.VISIBLE);
                                    catecap4.setText("CDW");
                                }

                                mensaje.setVisibility(View.GONE);

                                //SE LIMPIAN LAS ENTRADAS DE TEXTO Y EL SPINNER
                                medicion1.setText("");
                                medicion2.setText("");
                                medicion3.setText("");

                                spinnerCategoria.setSelection(0);

                                Toast.makeText(this, "Mediciones guardadas", Toast.LENGTH_SHORT).show();

                                //guardarMedicion.setEnabled(true);
                                //editarMedicion.setEnabled(true);
                            } else {
                                Toast.makeText(this, "Debes escribir una cantidad", Toast.LENGTH_SHORT).show();
                            }
                        } else if (idCate == 5) {
                            if (cantidad >= 0) {
                                if (idMarca >= 0) {
                                    new BDopenHelper(this).insertarMedicionMuebles(idTienda, idPromotor, idMarca, idCate, cantidad, fecha);

                                    bloque8.setVisibility(View.VISIBLE);
                                    catecap5.setText("OTROS");

                                    mensaje.setVisibility(View.GONE);

                                    editCantidad.setText("");

                                    Toast.makeText(this, "Mediciones guardadas", Toast.LENGTH_SHORT).show();

                                    spinnerCategoria.setSelection(0);
                                }else {
                                    Toast.makeText(this, "Debes elegir una marca", Toast.LENGTH_SHORT).show();
                                }
                            }else {
                                Toast.makeText(this, "Debes escribir una cantidad", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(this, "Selecciona una categoría", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        Log.e("Error Med Mueble 1: ", e.getMessage());
                        Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(this, "Ya capturaste mediciones de esta categoría", Toast.LENGTH_SHORT).show();
                }

            }catch (Exception e){
                e.printStackTrace();
                Log.e("Error Med Mueble 2: ", e.getMessage());
                Toast.makeText(this,e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void editarDatosLocal(){

        int cantidad, cantidad1, cantidad2, cantidad3, cantidadMediciones, idCate, registros;

        try {
            Calendar c = Calendar.getInstance();
            SimpleDateFormat dFecha = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
            SpinnerCateProdModel spPm = (SpinnerCateProdModel) spinnerCategoria.getSelectedItem();
            MarcaModel spm = (MarcaModel) spiMarca.getSelectedItem();
            String fecha = dFecha.format(c.getTime());

            cantidadMediciones = new BDopenHelper(this).cuentaMediciones(idPromotor, idTienda, fecha);

            idCate = spPm.getId();
            int idMarca = spm.getId();

            if(cantidadMediciones < 1){
                Toast.makeText(this,"No hay mediciones registradas",Toast.LENGTH_SHORT).show();
            }else{
                registros = new BDopenHelper(this).getMedicionParaEditar(idPromotor, idTienda, idCate, fecha);
                cantidad = editCantidad.getText().length();
                cantidad1 = medicion1.getText().length();
                cantidad2 = medicion2.getText().length();
                cantidad3 = medicion3.getText().length();

                if(idCate < 5) {
                    if (registros == 3 && (cantidad1 > 0 && cantidad2 > 0 && cantidad3 > 0)) {
                        cantidad1 = Integer.parseInt(medicion1.getText().toString());
                        cantidad2 = Integer.parseInt(medicion2.getText().toString());
                        cantidad3 = Integer.parseInt(medicion3.getText().toString());
                        if (idCate == 1) {
                            new BDopenHelper(this).editarMedicionMueble(idPromotor, idTienda, idCate, 249, cantidad1, 1, fecha);
                            new BDopenHelper(this).editarMedicionMueble(idPromotor, idTienda, idCate, 356, cantidad2, 1, fecha);
                            new BDopenHelper(this).editarMedicionMueble(idPromotor, idTienda, idCate, 360, cantidad3, 1, fecha);
                        } else if (idCate == 2) {
                            new BDopenHelper(this).editarMedicionMueble(idPromotor, idTienda, idCate, 249, cantidad1, 1, fecha);
                            new BDopenHelper(this).editarMedicionMueble(idPromotor, idTienda, idCate, 359, cantidad2, 1, fecha);
                            new BDopenHelper(this).editarMedicionMueble(idPromotor, idTienda, idCate, 360, cantidad3, 1, fecha);
                        } else if (idCate == 3) {
                            new BDopenHelper(this).editarMedicionMueble(idPromotor, idTienda, idCate, 249, cantidad1, 1, fecha);
                            new BDopenHelper(this).editarMedicionMueble(idPromotor, idTienda, idCate, 357, cantidad2, 1, fecha);
                            new BDopenHelper(this).editarMedicionMueble(idPromotor, idTienda, idCate, 360, cantidad3, 1, fecha);
                        } else if (idCate == 4) {
                            new BDopenHelper(this).editarMedicionMueble(idPromotor, idTienda, idCate, 249, cantidad1, 1, fecha);
                            new BDopenHelper(this).editarMedicionMueble(idPromotor, idTienda, idCate, 358, cantidad2, 1, fecha);
                            new BDopenHelper(this).editarMedicionMueble(idPromotor, idTienda, idCate, 360, cantidad3, 1, fecha);
                        }

                        Toast.makeText(this, "Mediciones actualizadas", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Faltan registros o valores para modificar", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    if(cantidad > 0) {
                        cantidad = Integer.parseInt(editCantidad.getText().toString());

                        new BDopenHelper(this).editarMedicionMueble(idPromotor, idTienda, idCate, idMarca, cantidad, 1, fecha);
                    }else{
                        Toast.makeText(this, "Falta valor para modificar", Toast.LENGTH_SHORT).show();
                    }
                }
                /*Toast.makeText(this,"REGISTROS: " + registros,Toast.LENGTH_SHORT).show();
                Toast.makeText(this,"CANTIDAD1: " + cantidad1,Toast.LENGTH_SHORT).show();*/
            }

            //Toast.makeText(this,"ID CATE: " + idCate,Toast.LENGTH_SHORT).show();

        }catch (Exception e){
            e.printStackTrace();
            Log.e("Error Medicion Mueble 3", e.getMessage());
            Toast.makeText(this,e.getMessage(),Toast.LENGTH_SHORT).show();
        }

        //guardarMedicion.setEnabled(true);
        //editarMedicion.setEnabled(false);
    }

    public void editarMedicionCategoria(int idCategoria){
        Cursor cursor;
        MarcaModel marca;

        int med1, med2, med3, contador;

        med1 = 0;
        med2 = 0;
        med3 = 0;

        spinnerCategoria.setSelection(idCategoria);

        Calendar c = Calendar.getInstance();
        SimpleDateFormat dFecha = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        String fecha = dFecha.format(c.getTime());

        contador = 0;

        if(idCategoria > 0 && idCategoria < 5){
            cursor = new BDopenHelper(this).getMedicionCategoria(idPromotor, idTienda, idCategoria, fecha);
            for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()){
                if(contador == 0){
                    med1 = cursor.getInt(4);
                }else if(contador == 1){
                    med2 = cursor.getInt(4);
                }else if(contador == 2){
                    med3 = cursor.getInt(4);
                }
                contador++;
            }

            medicion1.setText(String.valueOf(med1));
            medicion2.setText(String.valueOf(med2));
            medicion3.setText(String.valueOf(med3));
        }else if(idCategoria == 5){
            int idMarca, auxIdMarca, totalMarcas;
            long marcaPos;

            idMarca = 0;
            marcaPos = 0;

            cursor = new BDopenHelper(this).getMedicionCategoria(idPromotor, idTienda, idCategoria, fecha);

            for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                med1 = cursor.getInt(4); //cantidad
                idMarca = cursor.getInt(2); //idMarca
            }

            totalMarcas = spiMarca.getAdapter().getCount();

            for(int i=0; i<totalMarcas; i++){
                marca = (MarcaModel)spiMarca.getAdapter().getItem(i);
                auxIdMarca = marca.getId();
                if(idMarca == auxIdMarca){
                    marcaPos = spiMarca.getAdapter().getItemId(i);
                }

            }

            //Toast.makeText(this,"CURSOR: " + marcaPos,Toast.LENGTH_SHORT).show();

            editCantidad.setText(String.valueOf(med1));
            spiMarca.setSelection((int)marcaPos);
        }

        //spinnerCategoria.setSelection(idCategoria);
    }


    public void guardarDatos() {
        int cantidad;

        try {

            try {

                Calendar c = Calendar.getInstance();
                SimpleDateFormat dFecha = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                MarcaModel spm = (MarcaModel) spiMarca.getSelectedItem();
                SpinnerCateProdModel spPm = (SpinnerCateProdModel) spinnerCategoria.getSelectedItem();

                String fecha = dFecha.format(c.getTime());

                cantidad = editCantidad.getText().length() > 0 ? Integer.parseInt(editCantidad.getText().toString()) : 0;

                int idMarca = spm.getId();
                int idCate = spPm.getId();
                String nombreP = spPm.getCategoria();

                Log.i("FECHA MED", fecha);

                //new BDopenHelper(this).insertarMedicionMuebles(idTienda, idPromotor, idMarca, idCate, cantidad, fecha);

                //RESET DE SPINNERS Y ENTRADAS
                spinnerCategoria.setSelection(0);
                spiMarca.setSelection(0);
                editCantidad.setText("");
                medicion1.setText("");
                medicion2.setText("");
                medicion3.setText("");

                Cursor infoMuebles = new BDopenHelper(this).datosMuebles();
                int registrosPendientes = infoMuebles.getCount();

                if(registrosPendientes > 0) {
                    try {
                        Cursor mediciones = new BDopenHelper(this).getMedicionesEnviadas(idPromotor, idTienda, fecha);
                        int medEnviadas = mediciones.getCount();

                        if(registrosPendientes > 0) {
                            //SE ENVÍAN MEDICIONES AL SERVIDOR
                            new EnviarDatos(this).enviarMedicionMueble();

                            Toast.makeText(this, "Mediciones enviadas al sistema", Toast.LENGTH_SHORT).show();

                            //SE CAMBIA STATUS A 3 CUANDO LOS REGISTROS SE HAN ENVIADO PARA EVITAR VOLVER A MANDAR LO MISMO.
                            new BDopenHelper(this).marcarMedicionesEnviadas(idPromotor, idTienda, fecha);
                        }else{
                            Toast.makeText(this, "Ya se han enviado las mediciones capturadas", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Error al conectar con servidor, inténtelo nuevamente", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(this, "No hay mediciones capturadas o pendientes", Toast.LENGTH_SHORT).show();
                }

            }catch(Exception e) {
                e.printStackTrace();
                Toast.makeText(this,"error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }


        }catch(Exception e) {
            e.printStackTrace();
        }



    }

    /*
    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int id,long arg3) {

        MarcaModel spm = (MarcaModel)  spiMarca.getSelectedItem();


        int idMarca = spm.getId();
        loadSpinnerProd(idMarca);

    }*/


    /*
    @Override
    public void onNothingSelected(AdapterView<?> arg0) {


    }*/

    private void  loadSpinner(){
        try {
            MarcasAdapter adapter = new MarcasAdapter(this,
                    android.R.layout.simple_spinner_item, getArrayList());
            spiMarca.setAdapter(adapter);

        }catch(Exception e) {
            Toast.makeText(this, "Error Mayoreo 3", Toast.LENGTH_SHORT).show();
        }

    }

    private void spinnerCate(){
        try {
            CategoriasProductoAdapter adapterCate = new CategoriasProductoAdapter(this, android.R.layout.simple_spinner_item, getArrayCate());
            spinnerCategoria.setAdapter(adapterCate);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private ArrayList<SpinnerCateProdModel> getArrayCate(){

        base = new BDopenHelper(this).getReadableDatabase();
        ArrayList<SpinnerCateProdModel> arrayE = new ArrayList<>();
        String sql = "select id, categoria from categoriasProducto order by id asc;";
        Cursor cursorE = base.rawQuery(sql, null);

        for(cursorE.moveToFirst(); !cursorE.isAfterLast(); cursorE.moveToNext()){

            final SpinnerCateProdModel spiM = new SpinnerCateProdModel();
            spiM.setCategoria(cursorE.getString(1));
            spiM.setId(cursorE.getInt(0));
            Log.v("cate", spiM.getCategoria());

            arrayE.add(spiM);
        }

        final SpinnerCateProdModel spiMfirst = new SpinnerCateProdModel();
        spiMfirst.setCategoria("Categoría del producto");
        spiMfirst.setId(0);

        arrayE.add(0,spiMfirst);

        cursorE.close();
        base.close();
        return arrayE;

    }

    /*
    private void loadSpinnerProd(int idM){
        try {

            spinnerCategoria.setItems(getArrayListProByTien2(idM, idTienda), "Selecciona producto");

        } catch (Exception e) {
            Toast.makeText(this, "Error E25", Toast.LENGTH_SHORT).show();
        }
    }*/



    private ArrayList<MarcaModel> getArrayList(){

        base = new BDopenHelper(this).getReadableDatabase();
        String sql = "select idMarca, nombre, img from marca order by nombre asc;";
        Cursor cursorMarca = base.rawQuery(sql, null);

        for(cursorMarca.moveToFirst(); !cursorMarca.isAfterLast(); cursorMarca.moveToNext()){

            final MarcaModel spiM = new MarcaModel();
            spiM.setNombre(cursorMarca.getString(1));
            spiM.setId(cursorMarca.getInt(0));
            spiM.setUrl(cursorMarca.getString(2));

            array.add(spiM);
        }

        final MarcaModel spiMfirst = new MarcaModel();
        spiMfirst.setNombre("Seleccionar marca");
        spiMfirst.setId(0);

        array.add(0,spiMfirst);

        cursorMarca.close();
        base.close();
        return array;

    }





}
