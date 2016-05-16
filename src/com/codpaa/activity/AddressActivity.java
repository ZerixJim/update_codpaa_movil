package com.codpaa.activity;
/*
 * Created by grim on 12/05/2016.
 */

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.codpaa.R;
import com.codpaa.db.BDopenHelper;
import com.codpaa.update.EnviarDatos;

public class AddressActivity extends AppCompatActivity{

    Toolbar toolbar;
    EditText editCalle, editNumero, editColonia, editCodigo;
    TextInputLayout tCalle, tNumero, tColonia, tCodigo;
    int idTienda, idPromotor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_address);
        Intent i = getIntent();
        idTienda = i.getIntExtra("idTienda",0);
        idPromotor = i.getIntExtra("idPromotor", 0);

        toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }


        editCalle = (EditText) findViewById(R.id.edit_calle);
        editNumero = (EditText) findViewById(R.id.edit_numero);
        editColonia = (EditText) findViewById(R.id.edit_colonia);
        editCodigo = (EditText) findViewById(R.id.edit_codigo);



        tCalle = (TextInputLayout) findViewById(R.id.text_input_calle);
        tNumero = (TextInputLayout) findViewById(R.id.text_input_numero);
        tColonia = (TextInputLayout) findViewById(R.id.text_input_colonia);
        tCodigo = (TextInputLayout) findViewById(R.id.text_input_codigo);






    }




    private void saveAddress(){

        BDopenHelper bd = new BDopenHelper(this);

        ContentValues contentValues = new ContentValues();

        contentValues.put("idTienda", idTienda);
        contentValues.put("idPromotor", idPromotor);
        contentValues.put("direccion", getAddresses());

        bd.insertar("direcciones", contentValues);


        EnviarDatos enviarDatos = new EnviarDatos(this);
        enviarDatos.enviarAddress();


    }

    private String getAddresses() {

        return String.format("%s/%s/%s/%s", editCalle.getText().toString(), editNumero.getText().toString(),
                editColonia.getText().toString(), editCodigo.getText().toString());
    }

    private boolean validateStreet(){
        if (editCalle.getText().length() <= 0){
            tCalle.setError("requerido");
            return false;
        }else {
            tCalle.setError(null);
            return true;
        }
    }

    private boolean validateNumber(){
        if (editNumero.getText().length() <=0){
            tNumero.setError("requerido");

            return false;
        } else {
            tNumero.setError(null);
            return true;
        }
    }

    private boolean validateColonia(){
        if (editColonia.getText().length() <= 0){
            tColonia.setError("requerido");
            return false;
        } else {
            tColonia.setError(null);
            return true;
        }
    }

    private boolean validateCode(){
        if (editCodigo.getText().length() <= 0){
            tCodigo.setError("requerido");
            return false;
        } else {
            tCodigo.setError(null);
            return true;
        }
    }


    private void validarDatos(){
        if (validateStreet() && validateNumber() && validateColonia() && validateCode()){
            Toast.makeText(this, "enviando..",Toast.LENGTH_SHORT).show();

            saveAddress();

            editCalle.setText("");
            editNumero.setText("");
            editColonia.setText("");
            editCodigo.setText("");
        }else{
            Toast.makeText(this, "completa todos los campos requeridos", Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_address, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
            case R.id.save_address:
                validarDatos();
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }

    }


}
