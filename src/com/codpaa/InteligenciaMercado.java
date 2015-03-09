package com.codpaa;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import BD.BDopenHelper;

public class InteligenciaMercado extends Activity implements OnClickListener, OnItemSelectedListener{
	
	Button guardar, salir;
	Spinner spMarca, spProducto;
	DatePicker dateInicio, dateFin;
	SQLiteDatabase base;
	EditText editProNormal, editProOfer, editPrecioCaja;
	CheckBox chOferCr, chProExtra, chProEmp, chCambioI, chCambioP;
	InputMethodManager im;
	ArrayList<SpinnerMarcaModel> array = new ArrayList<SpinnerMarcaModel>();
	
	int idTienda, idPromotor;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.inteligenciamercado);
		
		Intent i = getIntent();
		idTienda = (Integer) i.getExtras().get("idTienda");
		idPromotor = (Integer) i.getExtras().get("idPromotor");
		
		im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			
		salir = (Button) findViewById(R.id.btnSalirIntel);
		guardar = (Button) findViewById(R.id.btnInteligua);
		
		spMarca = (Spinner) findViewById(R.id.spInMar);
		spProducto = (Spinner) findViewById(R.id.spInProd);
		
		editProNormal = (EditText) findViewById(R.id.editPreN);
		editProOfer = (EditText) findViewById(R.id.editPreOfer);
		editPrecioCaja = (EditText) findViewById(R.id.editTextPreCaj);
		
		chOferCr = (CheckBox) findViewById(R.id.checkOferC);
		chProExtra = (CheckBox) findViewById(R.id.checkProdEx);
		chProEmp = (CheckBox) findViewById(R.id.checkProEmp);
		chCambioI = (CheckBox) findViewById(R.id.checkCambio);
		chCambioP = (CheckBox) findViewById(R.id.checkCampre);
		
		dateInicio = (DatePicker) findViewById(R.id.datePickIni);
		dateFin = (DatePicker) findViewById(R.id.datePickFin);
		
		
		
		salir.setOnClickListener(this);
		guardar.setOnClickListener(this);
		spMarca.setOnItemSelectedListener(this);
		
		
	
		try {
			/*
			base = new BDopenHelper(this).getReadableDatabase();
			String sql = "select idMarca as _id, nombre from marca order by nombre asc;";
			Cursor cursorMarca = base.rawQuery(sql, null);
			
			SimpleCursorAdapter adaptadorMarca = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_1, cursorMarca, new String[]{"nombre"},new int[]{android.R.id.text1});
			adaptadorMarca.setDropDownViewResource(android.R.layout.simple_list_item_2);
			spMarca.setAdapter(adaptadorMarca);
			base.close();*/
			
			loadSpinner();
			
			im.hideSoftInputFromWindow(editProNormal.getWindowToken(), 0);
			im.hideSoftInputFromWindow(editProOfer.getWindowToken(), 0);
			
			
			
		}catch(Exception e) {
			Toast.makeText(this, "Error Frentes 3", Toast.LENGTH_SHORT).show();
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
		case R.id.btnSalirIntel:
			finish();
			break;
		
		}

		
	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int id,long arg3) {
		SpinnerMarcaModel spM = (SpinnerMarcaModel) spMarca.getSelectedItem();
		
		int idMarca = (int) spM.getId();
		/*
		Cursor cursorMarca = new BDopenHelper(this).productos(idMarca);
		
		SimpleCursorAdapter adaptadorPro = new SimpleCursorAdapter(InteligenciaMercado.this, android.R.layout.simple_list_item_2, cursorMarca, new String[]{"nombre","presentacion"},new int[]{android.R.id.text1,android.R.id.text2});
		adaptadorPro.setDropDownViewResource(android.R.layout.simple_list_item_2);
		spProducto.setAdapter(adaptadorPro);
		base.close();*/
		loadSpinnerProd(idMarca);
		
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		
		
	}
	
	private void resetCampos(){
		
		chOferCr.setChecked(false);
		chProExtra.setChecked(false);
		chProEmp.setChecked(false);
		chCambioI.setChecked(false);
		chCambioP.setChecked(false);
		
		if(editProNormal.getText().length() > 0)
			editProNormal.setText("");
		if(editProOfer.getText().length() > 0)
			editProOfer.setText("");
		if(editPrecioCaja.getText().length() > 0)
			editPrecioCaja.setText("");
	}
	
	
	private void guardar(){
		try {
			
			String oferCru, produExtr, proEmpl, cambioImagen, precioN, precioOfer, cambioPr, precioCaja;
			BDopenHelper baseH = new BDopenHelper(this);
			EnviarDatos enviar = new EnviarDatos(this);
			
			if(chOferCr.isChecked()){
				
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
			
			if(editProNormal.getText().length() > 0){
				precioN = editProNormal.getText().toString();
			}else{
				precioN = "---";
			}
			
			if(editProOfer.getText().length() > 0){
				precioOfer = editProOfer.getText().toString();
			}else{
				precioOfer = "---";
			}
			
			if(editPrecioCaja.getText().length() > 0){
				precioCaja = editPrecioCaja.getText().toString();
			}else{
				precioCaja = "---";
			}
			
			if(chCambioP.isChecked()){
				cambioPr = "SI";
			}else{
				cambioPr = "NO";
			}
			
			if(oferCru=="NO" && produExtr=="NO" && proEmpl=="NO" && cambioImagen=="NO" && precioN=="NO" && precioOfer=="NO" && precioN=="---" && precioOfer=="---" ){
				Toast.makeText(getApplicationContext(), "Seleccione un campo", Toast.LENGTH_SHORT).show();
			}else{
				try {
					SpinnerMarcaModel spM = (SpinnerMarcaModel) spMarca.getSelectedItem();
					SpinnerProductoModel spP = (SpinnerProductoModel) spProducto.getSelectedItem();
					Calendar c = Calendar.getInstance();
					SimpleDateFormat dFecha = new SimpleDateFormat("dd-MM-yyyy");
					
					String fecha = dFecha.format(c.getTime());
					int idMarca = (int) spM.getId();
					int idProdu = (int) spP.getIdProducto();
					if(idMarca != 0){
						if(idProdu != 0){
							baseH.insertarInteligencia(idPromotor, idTienda, idProdu, precioN, precioOfer, fecha, oferCru, produExtr, proEmpl,cambioImagen,1,getInicioOferta(),getFinoferta(),precioCaja,cambioPr);
							Log.d("InteligMer", "idProm "+idPromotor+" idT "+idTienda+" idP "+precioN+" precOfer "+precioOfer+" fecha "+fecha+" pferCr "+oferCru+" proE"+proEmpl);
							enviar.enviarInteli();
							Toast.makeText(getApplicationContext(), "Guardando.. y Enviando...", Toast.LENGTH_SHORT).show();
							setTimePicker();
							resetCampos();
							spProducto.setSelection(0);
							
						}else{
							Toast.makeText(getApplicationContext(), "NO seleccionaste Marca", Toast.LENGTH_SHORT).show();
						}
					}else{
						Toast.makeText(getApplicationContext(), "NO seleccionaste Marca", Toast.LENGTH_SHORT).show();
					}
					
					
				} catch (Exception e) {
					
				}
			}
			
			
			
			
		} catch (Exception e) {
			
		}
		
	}
	
	private String getInicioOferta(){
		
		int dia = dateInicio.getDayOfMonth();
		int mes = dateInicio.getMonth();
		int ano = dateInicio.getYear();
		
		String diaf = "", mesf = "",df;
		
		
		if(dia<10){
			diaf = "0"+dia;
		}else{
			diaf = Integer.toString(dia);
		}
			
		if((mes+1)<10){
			mesf = "0"+(mes+1);
		}else{
			mesf = Integer.toString(mes);
		}
			
		
		df = diaf+"-"+mesf+"-"+ano;
		
		
		return df;
		
	}
	private String getFinoferta(){
		int dia = dateFin.getDayOfMonth();
		int mes = dateFin.getMonth();
		int ano = dateFin.getYear();
		
		String diaf = "", mesf = "",df;
		
		
		if(dia<10){
			diaf = "0"+dia;
		}else{
			diaf = Integer.toString(dia);
		}
			
		if((mes+1)<10){
			mesf = "0"+(mes+1);
		}else{
			mesf = Integer.toString(mes);
		}
			
		
		df = diaf+"-"+mesf+"-"+ano;
		
		
		return df;
		
	}
	
	private void loadSpinner(){
		try {
			
			
			CustomAdapter adapter = new CustomAdapter(this, android.R.layout.simple_spinner_item, getArrayList());
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
		
		Cursor curPro = new BDopenHelper(this).productos(idMarca);
		ArrayList<SpinnerProductoModel> arrayP = new ArrayList<SpinnerProductoModel>();
		for(curPro.moveToFirst(); !curPro.isAfterLast(); curPro.moveToNext()){
			final SpinnerProductoModel spP = new SpinnerProductoModel();
			spP.setIdProducto(curPro.getInt(0));
			spP.setNombre(curPro.getString(1));
			spP.setPresentacion(curPro.getString(2));
			arrayP.add(spP);
		}
		final SpinnerProductoModel spPinicio = new SpinnerProductoModel();
		spPinicio.setIdProducto(0);
		spPinicio.setNombre("Seleccione Producto");
		spPinicio.setPresentacion("producto sin seleccionar");
		
		arrayP.add(0,spPinicio);
		
		base.close();
		return arrayP;
		
	}
	
	private ArrayList<SpinnerMarcaModel> getArrayList(){
		
		base = new BDopenHelper(this).getReadableDatabase();
		String sql = "select idMarca as _id, nombre from marca order by nombre asc;";
		Cursor cursorMarca = base.rawQuery(sql, null);
		
		for(cursorMarca.moveToFirst(); !cursorMarca.isAfterLast(); cursorMarca.moveToNext()){
			
			final SpinnerMarcaModel spiM = new SpinnerMarcaModel();
			spiM.setNombre(cursorMarca.getString(1));
			spiM.setId(cursorMarca.getInt(0));
			
			array.add(spiM);			
		}
		
		final SpinnerMarcaModel spiMfirst = new SpinnerMarcaModel();
		spiMfirst.setNombre("Selecciona Marca");
		spiMfirst.setId(0);
		
		array.add(0,spiMfirst);
		
		base.close();
		return array;
		
	}
	
	public void setTimePicker(){
		Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH);
		int day = c.get(Calendar.DAY_OF_MONTH); 
		
		dateInicio.init(year, month, day, null);
		dateFin.init(year, month, day, null);
		
	}

}
