package com.codpaa.activity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.codpaa.R;
import com.codpaa.adapter.MarcasAdapter;
import com.codpaa.db.BDopenHelper;
import com.codpaa.model.MarcaModel;
import com.codpaa.provider.DbEstructure;
import com.codpaa.update.EnviarDatos;
import com.codpaa.util.Utilities;
import com.google.android.material.textfield.TextInputLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class CapturaEspecial extends AppCompatActivity implements View.OnClickListener {

    private RadioGroup radioTono;
    private EditText editFrentes, editTonos, editPrecio;
    private TextInputLayout inputTono, inputFrente;
    private Spinner spinnerMarca;
    private int idTienda, idPromotor;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.captura_especial);

        inputTono = findViewById(R.id.layout_input_tonos);
        inputFrente = findViewById(R.id.layout_input_frente);

        Button btnSaveTonos = findViewById(R.id.btn_save_tonos);
        Button btnSavePrecio = findViewById(R.id.btn_save_precio_marca);

        editFrentes = findViewById(R.id.edit_fretes_tono);
        editTonos = findViewById(R.id.edit_tonos_cantidad);
        editPrecio = findViewById(R.id.edit_precio);

        idTienda = getIntent().getIntExtra("idTienda", 0);
        idPromotor = getIntent().getIntExtra("idPromotor", 0);


        radioTono = findViewById(R.id.radioGroup);

        radioTono.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {

                RadioButton radioChecked = radioGroup.findViewById(i);


                if(radioChecked.getText().toString().equals("Si")){

                    inputTono.setVisibility(View.VISIBLE);
                    inputFrente.setVisibility(View.VISIBLE);


                }else{

                    inputTono.setVisibility(View.GONE);
                    inputFrente.setVisibility(View.GONE);

                }


            }
        });


        btnSaveTonos.setOnClickListener(this);
        btnSavePrecio.setOnClickListener(this);


        loadSpinner();


    }

    private void loadSpinner() {

        spinnerMarca = findViewById(R.id.spinner);

        MarcasAdapter adapter = new MarcasAdapter(this, android.R.layout.simple_spinner_item, getBrands());

        spinnerMarca.setAdapter(adapter);

    }

    private ArrayList<MarcaModel> getBrands() {

        ArrayList<MarcaModel> array = new ArrayList<>();

        SQLiteDatabase base = new BDopenHelper(this).getReadableDatabase();
        String sql = "select idMarca, nombre, img from marca  " +
                " where marca.idMarca in (81, 315, 158 ) " +
                " order by nombre asc;";
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



    private void guardarTonos(){

        int radioId = radioTono.getCheckedRadioButtonId();

        if(radioId > 0){

            RadioButton radio = findViewById(radioId);


            SQLiteDatabase db = new BDopenHelper(this).getWritableDatabase();


            Calendar calendar = Calendar.getInstance();

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Utilities.DATE_FORMAT_USA, Locale.getDefault());


            String date = simpleDateFormat.format(calendar.getTime());


            if (radio.getText().toString().equals("Si")){

                //validar campos de frentes y tonos

                if (editFrentes.getText().toString().length() > 0 && editTonos.getText().toString().length() > 0){



                    ContentValues cv = new ContentValues();

                    cv.put(DbEstructure.TonoPallet.ID_TIENDA, idTienda);
                    cv.put(DbEstructure.TonoPallet.PROMOTOR, idPromotor);
                    cv.put(DbEstructure.TonoPallet.CATEGORIA, radio.getText().toString());
                    cv.put(DbEstructure.TonoPallet.FECHA, date);
                    cv.put(DbEstructure.TonoPallet.FRENTES, editFrentes.getText().toString());
                    cv.put(DbEstructure.TonoPallet.TONOS, editTonos.getText().toString());



                    if(db.insertWithOnConflict(DbEstructure.TonoPallet.TABLE_NAME, null, cv, SQLiteDatabase.CONFLICT_REPLACE) > 0){

                        Toast.makeText(this, "Guardado", Toast.LENGTH_SHORT).show();

                        clearTonos();
                    }else {
                        Toast.makeText(this, "erro al guardar", Toast.LENGTH_SHORT).show();

                    }


                }else {

                    Toast.makeText(this, "Es necesario llenar los campos de frentes y tonos", Toast.LENGTH_SHORT).show();


                }



            }else {

                ContentValues cv = new ContentValues();

                cv.put(DbEstructure.TonoPallet.ID_TIENDA, idTienda);
                cv.put(DbEstructure.TonoPallet.PROMOTOR, idPromotor);
                cv.put(DbEstructure.TonoPallet.CATEGORIA, radio.getText().toString());
                cv.put(DbEstructure.TonoPallet.FECHA, date);

                if(db.insertWithOnConflict(DbEstructure.TonoPallet.TABLE_NAME, null, cv, SQLiteDatabase.CONFLICT_REPLACE) > 0){

                    Toast.makeText(this, "Guardado", Toast.LENGTH_SHORT).show();
                    clearTonos();

                }else {
                    Toast.makeText(this, "erro al guardar", Toast.LENGTH_SHORT).show();
                }

            }

            db.close();



        }else{

            Toast.makeText(this, "Selecciona si existen tonos de tinte", Toast.LENGTH_SHORT).show();

        }

        EnviarDatos env = new EnviarDatos(this);
        env.sendTonos();


    }

    private void clearTonos() {
        editTonos.setText("");
        editFrentes.setText("");

    }

    private void guardarPrecios(){

        MarcaModel m = (MarcaModel) spinnerMarca.getSelectedItem();

        if (m.getId() > 0 ) {


            if (editPrecio.getText().toString().length() > 0 ){


                Calendar calendar = Calendar.getInstance();

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Utilities.DATE_FORMAT_USA, Locale.getDefault());


                String date = simpleDateFormat.format(calendar.getTime());


                SQLiteDatabase db = new BDopenHelper(this).getWritableDatabase();


                ContentValues cv = new ContentValues();
                cv.put(DbEstructure.PrecioMarca.ID_TIENDA, idTienda);
                cv.put(DbEstructure.PrecioMarca.ID_MARCA, m.getId());
                cv.put(DbEstructure.PrecioMarca.PRICE, editPrecio.getText().toString());
                cv.put(DbEstructure.PrecioMarca.FECHA, date);

                if(db.insertWithOnConflict(DbEstructure.PrecioMarca.TABLE_NAME, null, cv, SQLiteDatabase.CONFLICT_REPLACE) > 0){


                    Toast.makeText(this, "Guardado", Toast.LENGTH_SHORT).show();

                    spinnerMarca.setSelection(0);
                    editPrecio.setText("");


                    EnviarDatos env = new EnviarDatos(this);
                    env.sendPrecioMarca();



                }else {

                    Toast.makeText(this, "error al guardar", Toast.LENGTH_SHORT).show();

                }


                db.close();

            }else {

                Toast.makeText(this, "precio faltante", Toast.LENGTH_SHORT).show();
            }



        }else{

            Toast.makeText(this, "Selecciona Marca", Toast.LENGTH_SHORT).show();


        }




    }


    @Override
    public void onClick(View view) {

        int id = view.getId();

        if (id == R.id.btn_save_tonos){
            guardarTonos();
        }else if (id == R.id.btn_save_precio_marca){

            guardarPrecios();

        }



    }
}
