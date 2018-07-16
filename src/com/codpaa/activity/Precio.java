package com.codpaa.activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;


import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import android.support.design.widget.TextInputLayout;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;


import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.codpaa.adapter.MarcasAdapter;
import com.codpaa.model.MarcaModel;
import com.codpaa.update.EnviarDatos;
import com.codpaa.adapter.ProductosCustomAdapter;
import com.codpaa.R;
import com.codpaa.model.SpinnerProductoModel;
import com.codpaa.db.BDopenHelper;

public class Precio extends AppCompatActivity implements OnClickListener, OnItemSelectedListener{
	
	Button guardar;
	Spinner spMarca, spProducto;

    /*private static Button btnFechaInicio;
	private static Button btnFechaFin;*/

	private TextInputLayout tilPrecioNormal;
	private TextInputLayout tilPrecioCaja;
	/*private TextInputLayout tilPrecioOferta;*/
	SQLiteDatabase base;
	EditText editProNormal, editProOfer, editPrecioCaja;
	//CheckBox chOferCr, chProExtra, chProEmp, chCambioI, chCambioP;
	InputMethodManager im;
	ArrayList<MarcaModel> array = new ArrayList<>();


	int idTienda, idPromotor;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.inteligenciamercado);

		Intent i = getIntent();

		idTienda = i.getIntExtra("idTienda", 0);
		idPromotor = i.getIntExtra("idPromotor", 0);
		
		im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			

		guardar = (Button) findViewById(R.id.btnInteligua);
		
		spMarca = (Spinner) findViewById(R.id.spInMar);
		spProducto = (Spinner) findViewById(R.id.spInProd);
		
		editProNormal = (EditText) findViewById(R.id.editPreN);
		editProOfer = (EditText) findViewById(R.id.editPreOfer);
		editPrecioCaja = (EditText) findViewById(R.id.editTextPreCaj);
		
		/*chOferCr = (CheckBox) findViewById(R.id.checkOferC);
		chProExtra = (CheckBox) findViewById(R.id.checkProdEx);
		chProEmp = (CheckBox) findViewById(R.id.checkProEmp);
		chCambioI = (CheckBox) findViewById(R.id.checkCambio);
		chCambioP = (CheckBox) findViewById(R.id.checkCampre);*/

        //buttons
     /*   btnFechaInicio = (Button) findViewById(R.id.btnFechaInicio);
        btnFechaFin = (Button) findViewById(R.id.btnFechaFin);*/


		tilPrecioNormal = (TextInputLayout) findViewById(R.id.til_precio_normal);
		tilPrecioCaja = (TextInputLayout) findViewById(R.id.til_precio_caja);
		//tilPrecioOferta = (TextInputLayout) findViewById(R.id.til_precio_oferta);

		guardar.setOnClickListener(this);
		spMarca.setOnItemSelectedListener(this);
		
		
	
		try {

			loadSpinner();
			
			im.hideSoftInputFromWindow(editProNormal.getWindowToken(), 0);
			im.hideSoftInputFromWindow(editProOfer.getWindowToken(), 0);

			getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
			
			
			
		}catch(Exception e) {
			Toast.makeText(this, "Error Frentes 3", Toast.LENGTH_SHORT).show();
		}


		try {
			assert getSupportActionBar() != null;
			ActionBar actionBar = getSupportActionBar();
			actionBar.setDisplayHomeAsUpEnabled(true);
			actionBar.setDisplayUseLogoEnabled(true);
			actionBar.setHomeButtonEnabled(true);
			actionBar.setLogo(R.drawable.ic_launcher);
		}catch (NullPointerException e){
			e.printStackTrace();
		}


		/*
		 * keyboard soft hide
		 */
		this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		
	}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_inteligencia,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {

            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
		editProNormal.clearFocus();
		editProOfer.clearFocus();
	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	public void onClick(View v) {
		
		switch(v.getId()){
		case R.id.btnInteligua:
			guardar();
			break;

		
		}

		
	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int id,long arg3) {
		MarcaModel spM = (MarcaModel) spMarca.getSelectedItem();
		
		int idMarca = spM.getId();

		loadSpinnerProd(idMarca);
		
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		
		
	}
	
	private void resetCampos(){
		
		/*chOferCr.setChecked(false);
		chProExtra.setChecked(false);
		chProEmp.setChecked(false);
		chCambioI.setChecked(false);
		chCambioP.setChecked(false);*/
		
		if(editProNormal.getText().length() > 0)
			editProNormal.setText("");
		if(editProOfer.getText().length() > 0)
			editProOfer.setText("");
		if(editPrecioCaja.getText().length() > 0)
			editPrecioCaja.setText("");
	}
	
	
	private void guardar(){
		try {


			String normal = editProNormal.getText().toString();
			String caja = editPrecioCaja.getText().toString();
			String oferta = editProOfer.getText().toString();

			if (isNotEmptyNormal(normal)){
				tilPrecioNormal.setError(null);
			}else {
				tilPrecioNormal.setError("Campo Requerido");
			}

			if (isNotEmptyCaja(caja)){
				tilPrecioCaja.setError(null);
			}else {
				tilPrecioCaja.setError("Campo Requerido");
			}

			/*if (isNotEmptyOferta(oferta)){
				tilPrecioOferta.setError(null);
			}else {
				tilPrecioOferta.setError("Campo Requerido");
			}*/
			
			/*String oferCru, produExtr, proEmpl, cambioImagen, cambioPr;*/
			BDopenHelper baseH = new BDopenHelper(this);
			EnviarDatos enviar = new EnviarDatos(this);
			
			/*if(chOferCr.isChecked()){
				
				oferCru = "SI";
			}else{
				oferCru = "NO";
			}
			
			if(chProExtra.isChecked()){
				produExtr = "SI";
			}else{
				produExtr = "NO";
			}
			
			if(chProEmp.isChecked()){
				proEmpl = "SI";
			}else{
				proEmpl = "NO";
			}
			
			if(chCambioI.isChecked()){
				cambioImagen = "SI";
			}else{
				cambioImagen = "NO";
			}

			
			if(chCambioP.isChecked()){
				cambioPr = "SI";
			}else{
				cambioPr = "NO";
			}*/

			try {
				MarcaModel spM = (MarcaModel) spMarca.getSelectedItem();
				SpinnerProductoModel spP = (SpinnerProductoModel) spProducto.getSelectedItem();
				Calendar c = Calendar.getInstance();
				SimpleDateFormat dFecha = new SimpleDateFormat("dd-MM-yyyy",Locale.getDefault());

				String fecha = dFecha.format(c.getTime());
				int idMarca = spM.getId();
				int idProdu = spP.getIdProducto();
				if(idMarca != 0){
					if(idProdu != 0){

						if (isNotEmptyOferta(normal) && isNotEmptyCaja(caja)) {

							baseH.insertarPrecio(idPromotor, idTienda, idProdu, normal, caja ,oferta, fecha, 1);
							//Log.d("InteligMer", "idProm " + idPromotor + " idT " + idTienda + " idP " + normal + " precOfer " + oferta + " fecha " + fecha + " pferCr " + oferCru + " proE" + proEmpl);
							enviar.enviarInteli();
							Toast.makeText(getApplicationContext(), "Guardando.. y Enviando...", Toast.LENGTH_SHORT).show();

							resetCampos();
							spProducto.setSelection(0);

							/*btnFechaInicio.setText(R.string.fecha);
							btnFechaFin.setText(R.string.fecha);*/
						} else {
							Toast.makeText(this, "Campos de Precio son Requeridos", Toast.LENGTH_SHORT).show();
						}

					}else{
						Toast.makeText(getApplicationContext(), "NO seleccionaste Marca", Toast.LENGTH_SHORT).show();
					}
				}else{
					Toast.makeText(getApplicationContext(), "NO seleccionaste Marca", Toast.LENGTH_SHORT).show();
				}


			} catch (Exception e) {
				e.printStackTrace();
			}
			

			
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	

	private void loadSpinner(){
		try {
			
			
			MarcasAdapter adapter = new MarcasAdapter(this, android.R.layout.simple_spinner_item, getArrayList());
			spMarca.setAdapter(adapter);
			
		}catch(Exception e) {
			Toast.makeText(this, "Error Mayoreo 3", Toast.LENGTH_SHORT).show();
		}
		
	}

	
	private void loadSpinnerProd(int idM){
		try {
			ProductosCustomAdapter proAdap = new ProductosCustomAdapter(this, android.R.layout.simple_spinner_item, getArrayListPro(idM));
			spProducto.setAdapter(proAdap);
			
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
		
		array.add(0, spiMfirst);
		cursorMarca.close();
		base.close();
		return array;
		
	}

    /*public String getFechaInicio(){
        String salidaFecha;
        if (!btnFechaInicio.getText().equals("fecha")){
            salidaFecha = btnFechaInicio.getText().toString();
        }else {
            salidaFecha = "";
        }
        return salidaFecha;
    }

    public String getFechaFin(){
        String salidaFecha;
        if (!btnFechaFin.getText().equals("fecha")){
            salidaFecha = btnFechaFin.getText().toString();
        }else {
            salidaFecha = "";
        }
        return salidaFecha;
    }*/

    /*public void showDatePickerDialogInicio(View v){
        DialogFragment dialogInicio = new DatePickerInicio();

        dialogInicio.show(getSupportFragmentManager(), "datePickerInicio");

    }

    public void showDatePickerDialogFin(View v){
        DialogFragment dialogFin = new DatePickerFin();

        dialogFin.show(getSupportFragmentManager(), "datePickerFin");

    }*/


	private boolean isNotEmptyNormal(String normal){
		return normal.trim().length() > 0;
	}

	private boolean isNotEmptyCaja(String caja){
		return caja.trim().length() > 0;
	}

	private boolean isNotEmptyOferta(String oferta){
		return oferta.trim().length() > 0;
	}



    /*public static class DatePickerInicio extends DialogFragment implements DatePickerDialog.OnDateSetListener{


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

            btnFechaInicio.setText(String.format(Locale.getDefault(),"%s-%s-%d", dia, mes, year));


        }
    }

    public static class DatePickerFin extends DialogFragment implements DatePickerDialog.OnDateSetListener{


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


			Log.d("mes",Integer.toString(monthOfYear));
            String dia = dayOfMonth+"";
            String mes = (monthOfYear+1)+"";
            if (dayOfMonth < 10){
                dia = 0 + dia;
            }

            if (monthOfYear + 1 < 10){
                mes = 0 + mes;
            }

            btnFechaFin.setText(String.format(Locale.getDefault(),"%s-%s-%d", dia, mes, year));


        }
    }*/

}
