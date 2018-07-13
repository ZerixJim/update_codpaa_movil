package com.codpaa.activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Spinner;

import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;


import com.codpaa.adapter.MarcasAdapter;
import com.codpaa.model.MarcaModel;
import com.codpaa.update.EnviarDatos;
import com.codpaa.adapter.ProductosCustomAdapter;
import com.codpaa.R;

import com.codpaa.model.SpinnerProductoModel;
import com.codpaa.db.BDopenHelper;


public class Inventario extends AppCompatActivity implements OnItemSelectedListener, SeekBar.OnSeekBarChangeListener{
	
	
	int idTienda, idPromotor;
	SQLiteDatabase base;
    RadioButton piezas, cajas, selec;
    RadioGroup radio;
	TextView txtResultado;
	Toolbar toolbar;
	Spinner marca,producto;
    static Button btnFecha;

	EditText editFisico, editSistema;
	InputMethodManager im;
	ArrayList<MarcaModel> array = new ArrayList<>();
	Locale locale;

	SeekBar estado;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.inventario_bodega_layout);
		locale = new Locale("es_MX");
		Intent i = getIntent();

		idTienda = i.getIntExtra("idTienda",0);
		idPromotor = i.getIntExtra("idPromotor",0);
		
		im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

		marca = (Spinner) findViewById(R.id.spiInMar);
		producto = (Spinner) findViewById(R.id.spiInvPro);


		btnFecha = (Button) findViewById(R.id.button_fecha);

		editFisico = (EditText) findViewById(R.id.editInv);
        editSistema = (EditText) findViewById(R.id.editSistema);
		//editLote = (EditText) findViewById(R.id.editLote);

        radio = (RadioGroup) findViewById(R.id.radioInventario);
		if (radio != null) {
			piezas = (RadioButton) radio.findViewById(R.id.radioTipo1);
		}
		if (radio != null) {
			cajas = (RadioButton) radio.findViewById(R.id.radioTipo2);
		}

		//txtResultado = (TextView) findViewById(R.id.resultado);

        //radioEstatus = (RadioGroup) findViewById(R.id.radio_estatus);


		//estado = (SeekBar) findViewById(R.id.estado);

		if (estado != null) {
			estado.setOnSeekBarChangeListener(this);
		}

		piezas.setChecked(true);
		
		marca.setOnItemSelectedListener(this);

		this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);




        try {
            toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);


            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null){
                actionBar.setDisplayHomeAsUpEnabled(true);


                try {

                    Cursor cNomUser = new BDopenHelper(this).nombrePromotor(idPromotor);
                    cNomUser.moveToFirst();
                    actionBar.setSubtitle(cNomUser.getString(0));
                    cNomUser.close();
                    try {
                        Cursor cTienda = new BDopenHelper(this).tienda(idTienda);
                        cTienda.moveToFirst();
                        actionBar.setTitle(cTienda.getString(0)+" "+cTienda.getString(1));

                        try {

                            loadSpinner();


                        }catch(Exception e) {
                            Toast.makeText(this, "Error  3", Toast.LENGTH_SHORT).show();
                        }
                        cTienda.close();
                    }catch(Exception e) {
                        Toast.makeText(this, "Error  2", Toast.LENGTH_SHORT).show();
                    }


                }catch(Exception e) {
                    Toast.makeText(this, "Error  1", Toast.LENGTH_SHORT).show();

                }

            }

        }catch (NullPointerException e){
            e.printStackTrace();
        }
		
	}



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_inventario, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){

            case android.R.id.home:
                this.finish();
                return true;

            case R.id.save_inventario:
                guardarLosDatos();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int id,long arg3) {
		MarcaModel spM = (MarcaModel) marca.getSelectedItem();
		int idMarca = spM.getId();

		Log.d("idMarca", ""+ idMarca);

		loadSpinnerProd(idMarca);
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		
		
	}

	public void showDatePickerDialog(View v){
		DialogFragment dialogFragment = new TimePickerFragment();

		dialogFragment.show(getSupportFragmentManager(), "datePicker");

	}


	private void guardarLosDatos() {
		try {
			
			int cantidadFisico = 0, cantidadSistema = 0;
			Calendar c = Calendar.getInstance();
			SimpleDateFormat dFecha = new SimpleDateFormat("dd-MM-yyyy", locale);
			
			MarcaModel spM = (MarcaModel) marca.getSelectedItem();
			SpinnerProductoModel spP = (SpinnerProductoModel) producto.getSelectedItem();
			int idMarca = spM.getId();
			int idProdu = spP.getIdProducto();
			
			String fecha = dFecha.format(c.getTime());
			
			if(idMarca != 0){
				if(idProdu != 0){

                    if(editFisico.getText().length() >0 || editSistema.getText().length() >0) {

                        if(editFisico.getText().length()>0)
                            cantidadFisico = Integer.parseInt(editFisico.getText().toString());
                        if(editSistema.getText().length()>0)
                            cantidadSistema = Integer.parseInt(editSistema.getText().toString());

                        try {

                            selec = (RadioButton) findViewById(radio.getCheckedRadioButtonId());
                            //estatusSelected = (RadioButton) findViewById(radioEstatus.getCheckedRadioButtonId());

                            try {
                                new BDopenHelper(this).insertInventarioLite(idTienda,idPromotor ,fecha, idProdu,
                                        cantidadFisico,cantidadSistema,1
                                        ,selec.getText().toString(),getFechaCaducidad());

                                //Log.d("RadioChecked", " " + String.valueOf(radioEstatus.getCheckedRadioButtonId()));
                                Toast.makeText(this,"Datos Guardados", Toast.LENGTH_SHORT).show();
                                editFisico.setText("");
                                editSistema.setText("");
                                //editLote.setText("");
                                btnFecha.setText("fecha");
                                producto.setSelection(0);
                                //estado.setProgress(0);
                                im.hideSoftInputFromWindow(editFisico.getWindowToken(), 0);
                                new EnviarDatos(this).enviarInventario();
                            }catch(Exception e) {
                                e.printStackTrace();
                            }
                        }catch(Exception e) {
                            e.printStackTrace();

                        }

                    } else {
                        Toast.makeText(this,"No se definio la cantidad", Toast.LENGTH_SHORT).show();
                    }


					/*if (estado.getProgress() > 0){

					} else {
						Toast.makeText(this, "Selecciona el estado de producto", Toast.LENGTH_SHORT).show();
					}*/


				}else{
					Toast.makeText(this,"NO seleccionaste Producto", Toast.LENGTH_SHORT).show();
				}
			}else{
				Toast.makeText(this,"NO seleccionaste Marca", Toast.LENGTH_SHORT).show();
			}
			
			
			
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		
	}



    private int getSelectedStatus(RadioGroup radio){
        int id = radio.getCheckedRadioButtonId();



        if (id >= 0){

			View radioButton = radio.findViewById(id);

			int idx = radio.indexOfChild(radioButton);

            radio.check(-1);
            return idx+1;
        } else
            return 0;
    }
	
	private void loadSpinner(){
		try {
			
			
			MarcasAdapter adapter = new MarcasAdapter(this, android.R.layout.simple_spinner_item, getArrayList());
			marca.setAdapter(adapter);
			
		}catch(Exception e) {
			Toast.makeText(this, "Error Mayoreo 3", Toast.LENGTH_SHORT).show();
		}
		
	}

	
	private void loadSpinnerProd(int idM){
		try {

			ProductosCustomAdapter proAdap = new ProductosCustomAdapter(this, android.R.layout.simple_spinner_item, getArrayListPro(idM));
			producto.setAdapter(proAdap);
			
		} catch (Exception e) {
			Toast.makeText(this, "Error Mayoreo 4", Toast.LENGTH_SHORT).show();
		}
	}
	
	private ArrayList<SpinnerProductoModel> getArrayListPro(int idMarca){


		Cursor cursor = new BDopenHelper(this).getProductosByTienda(idMarca, idTienda);
		ArrayList<SpinnerProductoModel> arrayP = new ArrayList<>();

		if (cursor.getCount() <= 0){
			Cursor curPro = new BDopenHelper(this).productos(idMarca);


			Log.d("Productos Inventario", "" + curPro.getCount() + " idTienda "+ idTienda);

			for(curPro.moveToFirst(); !curPro.isAfterLast(); curPro.moveToNext()){
				final SpinnerProductoModel spP = new SpinnerProductoModel();

				Log.d("Nombre Producto", ""+ curPro.getString(1));
				spP.setIdProducto(curPro.getInt(0));
				spP.setNombre(curPro.getString(1));
				spP.setPresentacion(curPro.getString(2));
				spP.setCodigoBarras(curPro.getString(3));
				spP.setIdMarca(curPro.getInt(4));
				arrayP.add(spP);
			}

			curPro.close();
		}else {

			for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()){
				final SpinnerProductoModel spP = new SpinnerProductoModel();

				Log.d("Nombre Producto", ""+ cursor.getString(1));
				spP.setIdProducto(cursor.getInt(0));
				spP.setNombre(cursor.getString(1));
				spP.setPresentacion(cursor.getString(2));
				spP.setCodigoBarras(cursor.getString(3));
				spP.setIdMarca(cursor.getInt(4));
				arrayP.add(spP);
			}

		}
		

		final SpinnerProductoModel spPinicio = new SpinnerProductoModel();
		spPinicio.setIdProducto(0);
		spPinicio.setNombre("Seleccione Producto");
		spPinicio.setPresentacion("producto sin seleccionar");
		
		arrayP.add(0, spPinicio);

		base.close();
		return arrayP;
		
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

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

		if (progress ==0 ){
			txtResultado.setText("No Seleccionado");
		}else if (progress ==1){
			txtResultado.setText("Muy Malo");
		}else if (progress ==2){
			txtResultado.setText("Malo");
		}else if (progress == 3){
			txtResultado.setText("Regular");
		}else if (progress ==4){
			txtResultado.setText("Bueno");
		}else if (progress == 5){
			txtResultado.setText("Muy Bueno");
		}

	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {

	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {

	}


	public static class TimePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener{


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

            btnFecha.setText(dia + "-" + mes + "-" + year);




		}
	}

	public String getFechaCaducidad(){
		String salidaFecha;
		if (!btnFecha.getText().equals("fecha")){
			salidaFecha = btnFecha.getText().toString();
		}else {
			salidaFecha = "";
		}
		return salidaFecha;
	}

	
	

}