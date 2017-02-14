package com.codpaa.activity;/*
 * Created by grim on 27/04/2016.
 */

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.codpaa.R;
import com.codpaa.adapter.MarcasAdapter;
import com.codpaa.adapter.ProductosCustomAdapter;
import com.codpaa.db.BDopenHelper;
import com.codpaa.model.MarcaModel;
import com.codpaa.model.ProductosModel;
import com.codpaa.model.SpinnerProductoModel;
import com.codpaa.update.EnviarDatos;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;



public class VentaPromedio extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Spinner spinnerMarca, spinnerProducto;
    private int idPromotor, idTienda;
    private static Button btnFechaI, btnFechaF;
    private RadioGroup radioTipo;
    private EditText editCantidad;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_venta_promedio);

        Intent i = getIntent();
        idPromotor = i.getIntExtra("idPromotor", 0);
        idTienda = i.getIntExtra("idTienda", 0);


        btnFechaI = (Button) findViewById(R.id.btnI);
        btnFechaF = (Button) findViewById(R.id.btnF);

        radioTipo = (RadioGroup) findViewById(R.id.radio);

        editCantidad = (EditText) findViewById(R.id.edit_cantidad);

        spinnerMarca = (Spinner) findViewById(R.id.spinner);
        if (spinnerMarca != null) {
            spinnerMarca.setOnItemSelectedListener(this);
        }
        spinnerProducto = (Spinner) findViewById(R.id.producto);


        loadSpinner();



        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);

        }



    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }



    private void loadSpinner(){


        MarcasAdapter marcasAdapter = new MarcasAdapter(this,android.R.layout.simple_spinner_item, getArrayList());
        spinnerMarca.setAdapter(marcasAdapter);

    }



    private void sendVenta(){

        MarcaModel marcaModel = (MarcaModel) spinnerMarca.getSelectedItem();


        if (marcaModel.getId() > 0){
            SpinnerProductoModel productoModel = (SpinnerProductoModel) spinnerProducto.getSelectedItem();

            if (productoModel.getIdProducto() > 0){

                if (radioTipo.getCheckedRadioButtonId() == -1){
                    Toast.makeText(this, "Tipo no Seleccionado", Toast.LENGTH_SHORT).show();
                }else {

                    if (editCantidad.getText().toString().trim().length() > 0){


                        if (!btnFechaI.getText().equals("fecha") && !btnFechaF.getText().equals("fecha")){

                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

                            try {
                                Date fechaInicio = simpleDateFormat.parse(btnFechaI.getText().toString());
                                Date fechaFin = simpleDateFormat.parse(btnFechaF.getText().toString());

                                if (fechaInicio.getTime() < fechaFin.getTime()){

                                    Toast.makeText(this, "Enviando..", Toast.LENGTH_SHORT).show();

                                    BDopenHelper bd = new BDopenHelper(this);

                                    ContentValues cv = new ContentValues();

                                    int idRadioSelect = radioTipo.getCheckedRadioButtonId();
                                    RadioButton radioSelected = (RadioButton) findViewById(idRadioSelect);

                                    cv.put("idMarca", marcaModel.getId());
                                    if (radioSelected != null) {
                                        cv.put("tipo", radioSelected.getText().toString().toUpperCase());
                                    }
                                    cv.put("cantidad", editCantidad.getText().toString().trim().toUpperCase());
                                    cv.put("fecha_inicio", btnFechaI.getText().toString());
                                    cv.put("fecha_fin", btnFechaF.getText().toString());
                                    cv.put("idTienda", idTienda );
                                    cv.put("idPromotor", idPromotor);
                                    cv.put("idProducto", productoModel.getIdProducto());

                                    bd.insertar("ventaPromedio", cv);



                                    EnviarDatos envia = new EnviarDatos(this);
                                    envia.sendVentaPromedio();

                                    spinnerMarca.setSelection(0);
                                    radioTipo.clearCheck();
                                    editCantidad.setText("");
                                    btnFechaI.setText(getResources().getText(R.string.fecha));
                                    btnFechaF.setText(getResources().getText(R.string.fecha));

                                }else {
                                    Toast.makeText(this, "La fecha de Inicio No puede se Mayor a la fecha final",
                                            Toast.LENGTH_LONG).show();
                                }


                            } catch (ParseException e) {
                                e.printStackTrace();
                            }



                        }else {

                            Toast.makeText(this, "Rango de fecha no seleccionado", Toast.LENGTH_SHORT).show();

                        }


                    }else {
                        Toast.makeText(this, "Escribe la cantidad", Toast.LENGTH_SHORT).show();
                    }



                }

            }else {
                Toast.makeText(this, "Selecciona Producto", Toast.LENGTH_SHORT).show();
            }



        }else {
            Toast.makeText(this, "Selecciona Marca", Toast.LENGTH_SHORT).show();
        }



    }



    private ArrayList<MarcaModel> getArrayList(){

        SQLiteDatabase base = new BDopenHelper(this).getReadableDatabase();
        ArrayList<MarcaModel> array = new ArrayList<>();
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
        spiMfirst.setNombre("Selecciona Marca");
        spiMfirst.setId(0);

        array.add(0,spiMfirst);

        cursorMarca.close();
        base.close();
        return array;

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_venta_promedio, menu);


        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {


            case android.R.id.home:
                finish();
                return true;

            case R.id.save_venta:

                sendVenta();

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void showDateInicio(View v){
        DialogFragment dialogInicio = new DatePickerInicio();

        dialogInicio.show(getSupportFragmentManager(), "datePickerInicio");

    }

    public void showDateFin(View v){
        DialogFragment dialogFin = new DatePickerFin();

        dialogFin.show(getSupportFragmentManager(), "datePickerFin");

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        MarcaModel model = (MarcaModel) adapterView.getSelectedItem();

        if (model.getId() > 0){

            spinnerProducto.setVisibility(View.VISIBLE);

            loadSpinnerProd(model.getId());


        }else {
            spinnerProducto.setVisibility(View.GONE);
        }


    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }


    public static class DatePickerInicio extends DialogFragment implements DatePickerDialog.OnDateSetListener{


        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            //estableciendo el tiempo actual como tiempo para el picker
            final Calendar c = Calendar.getInstance();


            return new DatePickerDialog(getActivity(),this,
                    c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
        }



        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {



            String dia = dayOfMonth+"";
            String mes = (monthOfYear+1)+"";
            if (dayOfMonth < 10){
                dia = 0 + dia;
            }

            if (monthOfYear + 1 < 10){
                mes = 0 + mes;
            }

            btnFechaI.setText(String.format(Locale.getDefault(),"%d-%s-%s", year, mes, dia));



        }
    }

    private void loadSpinnerProd(int idM){
        try {
            ProductosCustomAdapter proAdap = new ProductosCustomAdapter(this, android.R.layout.simple_spinner_item, getArrayListProByTiensda(idM, idTienda));
            spinnerProducto.setAdapter(proAdap);

        } catch (Exception e) {
            Toast.makeText(this, "Error Mayoreo 4", Toast.LENGTH_SHORT).show();
        }
    }



    private ArrayList<SpinnerProductoModel> getArrayListProByTiensda(int idMarca, int idTienda){

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

    public static class DatePickerFin extends DialogFragment implements DatePickerDialog.OnDateSetListener{


        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            //estableciendo el tiempo actual como tiempo para el picker
            final Calendar c = Calendar.getInstance();


            DatePickerDialog dialog = new DatePickerDialog(getActivity(),this,
                    c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));

            dialog.getDatePicker().setMaxDate(new Date().getTime());
            /*return new DatePickerDialog(getActivity(),this,
                    c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));*/
            return dialog;

        }



        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {


            Log.d("mes",Integer.toString(monthOfYear));
            String dia = dayOfMonth+"";
            String mes = (monthOfYear+1)+"";
            if (dayOfMonth < 10){
                dia = 0 + dia;
            }

            if (monthOfYear + 1 < 10){
                mes = 0 + mes;
            }

            btnFechaF.setText(String.format(Locale.getDefault(),"%d-%s-%s", year, mes, dia));




        }
    }
}
