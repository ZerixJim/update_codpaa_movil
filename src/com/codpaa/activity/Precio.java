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

import com.codpaa.adapter.CategoriasProductoAdapter;
import com.codpaa.model.ProductosModel;
import com.codpaa.model.SpinnerCateProdModel;
import com.codpaa.util.NumberTextWatcher;
import com.codpaa.widget.SingleSpinnerSelect;
import com.google.android.material.textfield.TextInputLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;


import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;


import android.widget.DatePicker;
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

public class Precio extends AppCompatActivity implements OnItemSelectedListener{

	Spinner spMarca, spinnerCategoria;
	private SingleSpinnerSelect spProducto;

    private static Button btnFechaInicio;
	private static Button btnFechaFin;

	private TextInputLayout tilPrecioNormal;
	private TextInputLayout tilPrecioCaja;
	/*private TextInputLayout tilPrecioOferta;*/
	SQLiteDatabase base;
	EditText editProNormal, editProOfer, editPrecioCaja, editPreOferCaja;
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
			

		
		spMarca =  findViewById(R.id.spInMar);
		spProducto =  findViewById(R.id.spInProd);
		//spinnerCategoria = findViewById(R.id.spinner_categorias_prod2);
		
		editProNormal =  findViewById(R.id.editPreN);
		editProOfer =  findViewById(R.id.editPreOfer);
		editPrecioCaja = findViewById(R.id.editTextPreCaj);
		editPreOferCaja = findViewById(R.id.editPreOferCaja);


		//editProNormal.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(4,2)});
		//editProOfer.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(4,2)});
		//editPrecioCaja.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(4,2)});

		editProNormal.addTextChangedListener(new NumberTextWatcher(editProNormal));
		editProOfer.addTextChangedListener(new NumberTextWatcher(editProOfer));
		editPrecioCaja.addTextChangedListener(new NumberTextWatcher(editPrecioCaja));
		editPreOferCaja.addTextChangedListener(new NumberTextWatcher(editPreOferCaja));

		
		/*chOferCr = (CheckBox) findViewById(R.id.checkOferC);
		chProExtra = (CheckBox) findViewById(R.id.checkProdEx);
		chProEmp = (CheckBox) findViewById(R.id.checkProEmp);
		chCambioI = (CheckBox) findViewById(R.id.checkCambio);
		chCambioP = (CheckBox) findViewById(R.id.checkCampre);*/

        //buttons
        btnFechaInicio = findViewById(R.id.btnFechaInicio);
        btnFechaFin = findViewById(R.id.btnFechaFin);


		tilPrecioNormal = findViewById(R.id.til_precio_normal);
		tilPrecioCaja =  findViewById(R.id.til_precio_caja);
		//tilPrecioOferta = (TextInputLayout) findViewById(R.id.til_precio_oferta);
		spMarca.setOnItemSelectedListener(this);
		

		try {

			loadSpinner();
			
			im.hideSoftInputFromWindow(editProNormal.getWindowToken(), 0);
			im.hideSoftInputFromWindow(editProOfer.getWindowToken(), 0);

			getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
			
			
			
		}catch(Exception e) {
			Toast.makeText(this, "Error Frentes 3", Toast.LENGTH_SHORT).show();
		}

		//Se cargan las categorías
		//spinnerCate();


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
		int itemId = item.getItemId();
		if (itemId == android.R.id.home) {
			this.finish();
			return true;
		} else if (itemId == R.id.save_inteligencia) {
			guardar();

			return true;
		}
		return super.onOptionsItemSelected(item);
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
	public void onItemSelected(AdapterView<?> arg0, View arg1, int id,long arg3) {
		MarcaModel spM = (MarcaModel) spMarca.getSelectedItem();
		
		int idMarca = spM.getId();

		loadSpinnerProd(idMarca);

		/*if(idMarca == 249){
			//Si la marca es Clorox se muestra el apartado de categorías
			spinnerCategoria.setVisibility(View.VISIBLE);
		}
		else{
			spinnerCategoria.setVisibility(View.INVISIBLE);
		}*/
		
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
			editProNormal.setText("0.00");
		if(editProOfer.getText().length() > 0)
			editProOfer.setText("0.00");
		if(editPrecioCaja.getText().length() > 0)
			editPrecioCaja.setText("0.00");
		if(editPreOferCaja.getText().length() > 0)
			editPreOferCaja.setText("0.00");
	}
	
	
	private void guardar(){
		try {


			String normal = editProNormal.getText().toString();
			String caja = editPrecioCaja.getText().toString();
			String oferta = editProOfer.getText().toString();
			String ofertaCaja = editPreOferCaja.getText().toString();



			if (isNotEmptyField(normal)){
				tilPrecioNormal.setError(null);
			}else {
				tilPrecioNormal.setError("Campo Requerido");
			}

			/** not required  **/
			/*if (isNotEmptyField(caja)){
				tilPrecioCaja.setError(null);
			}else {
				tilPrecioCaja.setError("Campo Requerido");
			}*/

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
				ProductosModel spP = (ProductosModel) spProducto.getSelectedItem();
				//SpinnerCateProdModel spC = (SpinnerCateProdModel) spinnerCategoria.getSelectedItem();
				Calendar c = Calendar.getInstance();
				SimpleDateFormat dFecha = new SimpleDateFormat("dd-MM-yyyy",Locale.getDefault());

				String fecha = dFecha.format(c.getTime());
				int idMarca = spM.getId();
				int idProdu = spP.getIdProducto();
				//int idCateProd = spC.getId();

				if(idMarca != 0){

					if(idProdu != 0){

						if(idMarca != 0) {

							//Si es cualquier marca menos Clorox, se puede registrar la información
							if (isNotEmptyField(normal)) {


								if (isNotEmptyField(oferta) && (getFechaInicio().isEmpty() || getFechaFin().isEmpty())) {

									Toast.makeText(this, "fecha de la oferta faltante ", Toast.LENGTH_SHORT).show();

								} else {
									int contador = 0;
									contador = baseH.cuentaPreciosV1(idPromotor, idTienda, idProdu, fecha);

									if(contador == 0){

										baseH.insertarPrecio(idPromotor, idTienda, idProdu, normal, caja, oferta, fecha, 1, getFechaInicio(), getFechaFin(), 0, ofertaCaja);
										//Log.d("InteligMer", "idProm " + idPromotor + " idT " + idTienda + " idP " + normal + " precOfer " + oferta + " fecha " + fecha + " pferCr " + oferCru + " proE" + proEmpl);
										enviar.enviarInteli();
										Toast.makeText(getApplicationContext(), "Guardando.. y Enviando...", Toast.LENGTH_SHORT).show();

										resetCampos();
										spProducto.resetFilter();
										spProducto.setSelection(0);

										btnFechaInicio.setText(R.string.fecha);
										btnFechaFin.setText(R.string.fecha);
										Log.e("PRECIOSCAPT", "Precio capturado");
									}else{
										Log.e("PRECIOSCAPT", "Ese registro ya existe");
									}

								}


							} else {
								Toast.makeText(this, "Campos de Precio son Requeridos", Toast.LENGTH_SHORT).show();
							}
						}
						/*else{
							if(idCateProd != 0) {

								if (isNotEmptyField(normal)) {


									if (isNotEmptyField(oferta) && (getFechaInicio().isEmpty() || getFechaFin().isEmpty())) {

										Toast.makeText(this, "fecha de la oferta faltante ", Toast.LENGTH_SHORT).show();


									} else {

										baseH.insertarPrecio(idPromotor, idTienda, idProdu, normal, caja, oferta, fecha, 1, getFechaInicio(), getFechaFin(), idCateProd, ofertaCaja);
										//Log.d("InteligMer", "idProm " + idPromotor + " idT " + idTienda + " idP " + normal + " precOfer " + oferta + " fecha " + fecha + " pferCr " + oferCru + " proE" + proEmpl);
										enviar.enviarInteli();
										Toast.makeText(getApplicationContext(), "Guardando.. y Enviando...", Toast.LENGTH_SHORT).show();

										resetCampos();
										spProducto.resetFilter();
										spProducto.setSelection(0);

										btnFechaInicio.setText(R.string.fecha);
										btnFechaFin.setText(R.string.fecha);
									}


								} else {
									Toast.makeText(this, "Campos de Precio son Requeridos", Toast.LENGTH_SHORT).show();
								}

							}else{
								Toast.makeText(this, "No seleccionaste la categoría", Toast.LENGTH_SHORT).show();
							}
						}*/

					}else{
						Toast.makeText(getApplicationContext(), "No seleccionaste un producto", Toast.LENGTH_SHORT).show();
					}
				}
				else{
					Toast.makeText(getApplicationContext(), "No seleccionaste una marca", Toast.LENGTH_SHORT).show();
				}


			} catch (Exception e) {
				e.printStackTrace();
			}

			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}



	/*public static class DecimalDigitsInputFilter implements InputFilter{

		Pattern mPattern;
		public DecimalDigitsInputFilter(int digitBeforeZero, int digitAfterZero) {
			mPattern = Pattern.compile("[0-9]{0," + (digitBeforeZero-1) + "}+((\\.[0-9]{0," + (digitAfterZero-1) + "})?)||(\\.)?");

		}

		@Override
		public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

			Matcher matcher = mPattern.matcher(dest);
			if (!matcher.matches())
				return "";
			return null;
		}
	}*/

	

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
			//ProductosCustomAdapter proAdap = new ProductosCustomAdapter(this, android.R.layout.simple_spinner_item, getArrayListPro(idM));
			//spProducto.setAdapter(proAdap);

			spProducto.setItems(getArrayListPro(idM), "Selecciona Producto");

			
		} catch (Exception e) {
			Toast.makeText(this, "Error Mayoreo 4", Toast.LENGTH_SHORT).show();
		}
	}

	/*private void spinnerCate(){
		try {
			CategoriasProductoAdapter adapterCate = new CategoriasProductoAdapter(this, android.R.layout.simple_spinner_item, getArrayCate());
			spinnerCategoria.setAdapter(adapterCate);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}*/

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

	private ArrayList<ProductosModel> getArrayListPro(int idMarca){


		Cursor cursor = new BDopenHelper(this).getProductosByTienda(idMarca, idTienda);
		ArrayList<ProductosModel> arrayP = new ArrayList<>();

		if (cursor.getCount() <= 0){
			Cursor curPro = new BDopenHelper(this).productos(idMarca);


			Log.d("Productos Inventario", "" + curPro.getCount() + " idTienda "+ idTienda);

			for(curPro.moveToFirst(); !curPro.isAfterLast(); curPro.moveToNext()){
				final ProductosModel spP = new ProductosModel();

				Log.d("Nombre Producto", ""+ curPro.getString(1));
				spP.setIdProducto(curPro.getInt(0));
				spP.setNombre(curPro.getString(1));
				spP.setPresentacion(curPro.getString(2));
				spP.setCodigoBarras(curPro.getString(3));
				spP.setIdMarca(curPro.getInt(4));
				spP.setHasImage(curPro.getInt(curPro.getColumnIndex("has_image")));
				arrayP.add(spP);
			}

			curPro.close();
		}else {

			for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()){
				final ProductosModel spP = new ProductosModel();

				Log.d("Nombre Producto", ""+ cursor.getString(1));
				spP.setIdProducto(cursor.getInt(0));
				spP.setNombre(cursor.getString(1));
				spP.setPresentacion(cursor.getString(2));
				spP.setCodigoBarras(cursor.getString(3));
				spP.setIdMarca(cursor.getInt(4));
				spP.setHasImage(cursor.getInt(cursor.getColumnIndex("has_image")));
				arrayP.add(spP);
			}

		}


		final ProductosModel spPinicio = new ProductosModel();
		spPinicio.setIdProducto(0);
		spPinicio.setNombre("Seleccione Producto");
		spPinicio.setPresentacion("producto sin seleccionar");

		arrayP.add(0, spPinicio);

		base.close();
		return arrayP;

	}
	

	
	private ArrayList<MarcaModel> getArrayList(){
		
		base = new BDopenHelper(this).getReadableDatabase();
		//String sql = "select idMarca as _id, nombre, img from marca order by nombre asc;";
		String sql = "select tm.idMarca as _id, m.nombre, m.img " +
					 "from tienda_marca tm " +
					 "left join marca m on tm.idMarca = m.idMarca " +
					 "where tm.idTienda = " + idTienda + " " +
					 "and tm.idMarca not in(356, 357, 358, 359, 360)" + ";";

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

    public String getFechaInicio(){
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
    }

    public void showDatePickerDialogInicio(View v){
        DialogFragment dialogInicio = new DatePickerInicio();

        dialogInicio.show(getSupportFragmentManager(), "datePickerInicio");

    }

    public void showDatePickerDialogFin(View v){
        DialogFragment dialogFin = new DatePickerFin();

        dialogFin.show(getSupportFragmentManager(), "datePickerFin");

    }


	private boolean isNotEmptyField(String field){
		return field.trim().length() > 0 && !field.trim().equals("0.00");
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
    }

}
