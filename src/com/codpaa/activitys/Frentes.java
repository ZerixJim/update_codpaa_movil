package com.codpaa.activitys;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.codpaa.adapters.CustomAdapter;
import com.codpaa.updates.EnviarDatos;
import com.codpaa.adapters.ProductosCustomAdapter;
import com.codpaa.R;
import com.codpaa.models.SpinnerMarcaModel;
import com.codpaa.models.SpinnerProductoModel;
import com.codpaa.db.BDopenHelper;

public class Frentes extends AppCompatActivity implements OnClickListener, OnItemSelectedListener{
	
	int idPromotor, idTienda;
	TextView nUser, nTienda;
	Button btnRegresar, btnGuar, btn1,btn2,btn3,btn4,btn5,btn6;
	 
	EditText Echa1,Echa2,Echa3,Echa4,Echa5,Echa6;
	InputMethodManager im;
	BDopenHelper baseH;

	Locale locale;

	ArrayList<SpinnerMarcaModel> array = new ArrayList<>();
 	SQLiteDatabase base;
	Spinner spiMarca, spiPro;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.frentes);

		locale = new Locale("es_MX");
		
		Intent i = getIntent();
		idTienda = (Integer) i.getExtras().get("idTienda");
		idPromotor = (Integer) i.getExtras().get("idPromotor");
		
		im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		
		nUser = (TextView) findViewById(R.id.userFrente);
		nTienda = (TextView) findViewById(R.id.tiendaFrente);
		spiMarca = (Spinner) findViewById(R.id.spinnerMarFre);
		spiPro = (Spinner) findViewById(R.id.spinnerFrePro);

		//asiganacion de botones
		btn1 = (Button) findViewById(R.id.btnf1);
		btn2 = (Button) findViewById(R.id.btnExit);
		btn3 = (Button) findViewById(R.id.btnf3);
		btn4 = (Button) findViewById(R.id.btnfoto);
		btn5 = (Button) findViewById(R.id.btnf5);
		btn6 = (Button) findViewById(R.id.btnf6);

		//asignacion de campos de texto
		Echa1 = (EditText) findViewById(R.id.editCha1);
		Echa2 = (EditText) findViewById(R.id.editCha2);
		Echa3 = (EditText) findViewById(R.id.editCha3);
		Echa4 = (EditText) findViewById(R.id.editCha4);
		Echa5 = (EditText) findViewById(R.id.editCha5);
		Echa6 = (EditText) findViewById(R.id.editCha6);

		//asignacion de escuchadores
		Echa1.setVisibility(View.INVISIBLE);
		Echa2.setVisibility(View.INVISIBLE);
		Echa3.setVisibility(View.INVISIBLE);
		Echa4.setVisibility(View.INVISIBLE);
		Echa5.setVisibility(View.INVISIBLE);
		Echa6.setVisibility(View.INVISIBLE);
		
		
		
		
		btnRegresar = (Button) findViewById(R.id.buttonExhib);
		btnGuar = (Button) findViewById(R.id.btonChFr);
		
		
		
		btnRegresar.setOnClickListener(this);
		btnGuar.setOnClickListener(this);
		spiMarca.setOnItemSelectedListener(this);
		btn1.setOnClickListener(this);
		btn2.setOnClickListener(this);
		btn3.setOnClickListener(this);
		btn4.setOnClickListener(this);
		btn5.setOnClickListener(this);
		btn6.setOnClickListener(this);

		
		
		try {
			
			baseH = new BDopenHelper(this);
			Cursor cNomUser = baseH.nombrePromotor(idPromotor);
			cNomUser.moveToFirst();
			nUser.setText(cNomUser.getString(0));
			baseH.close();
			try {
				Cursor cTienda = baseH.tienda(idTienda);
				cTienda.moveToFirst();
				nTienda.setText(cTienda.getString(0)+" "+cTienda.getString(1));
				cTienda.close();
				try {

					loadSpinner();
					
					
					
					
				}catch(Exception e) {
					Toast.makeText(this, "Error Frentes 3", Toast.LENGTH_SHORT).show();
				}
				
			}catch(Exception e) {
				Toast.makeText(this, "Error Frentes 2", Toast.LENGTH_SHORT).show();
			}
			
			
		}catch(Exception e) {
			Toast.makeText(this, "Error Frentes 1", Toast.LENGTH_SHORT).show();
			
		}


		try {
			//assert getSupportActionBar() != null;
			ActionBar actionBar = getSupportActionBar();
			actionBar.setDisplayHomeAsUpEnabled(true);
			actionBar.setDisplayUseLogoEnabled(true);
			actionBar.setHomeButtonEnabled(true);
			actionBar.setIcon(R.drawable.ic_launcher);
		}catch (NullPointerException e){
			e.printStackTrace();
		}
		
	}
	
	

	@Override
	protected void onPause() {
		super.onPause();
		
		
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
	public void onClick(View v) {
		
		switch(v.getId()) {
			case R.id.btnf1: reQuesFocus(Echa1);break;
			case R.id.btnExit: reQuesFocus(Echa2);break;
			case R.id.btnf3: reQuesFocus(Echa3);break;
			case R.id.btnfoto: reQuesFocus(Echa4);break;
			case R.id.btnf5: reQuesFocus(Echa5);break;
			case R.id.btnf6: reQuesFocus(Echa6);break;
			case R.id.btonChFr: guardarDatos(); break;
			case R.id.buttonExhib: finish(); break;
		}
		
		
	}

	
	
	private void reQuesFocus(View v){
		v.setVisibility(View.VISIBLE);
		v.requestFocus();
		
		im.showSoftInput(v, 0);

		
	}
	

	
	public void guardarDatos() {
		int cha1 = 0,cha2 = 0, cha3 = 0, cha4 = 0, cha5 = 0, cha6 = 0; 
		
		try {
			
			if(Echa1.isShown() && Echa1.getText().length() >0) {
				cha1 = Integer.parseInt(Echa1.getText().toString());
				
			} 
			if(Echa2.isShown() && Echa2.getText().length() >0) {
				cha2 = Integer.parseInt(Echa2.getText().toString());
				
			}
			
			if(Echa3.isShown() && Echa3.getText().length() >0) {
				cha3 = Integer.parseInt(Echa3.getText().toString());
				
			}
			
			if(Echa4.isShown() && Echa4.getText().length() >0) {
				cha4 = Integer.parseInt(Echa4.getText().toString());
				
			}
			
			if(Echa5.isShown() && Echa5.getText().length() >0) {
				cha5 = Integer.parseInt(Echa5.getText().toString());
				
			}
			if(Echa6.isShown() && Echa6.getText().length() >0) {
				cha6 = Integer.parseInt(Echa6.getText().toString());
				
			}
			
			try {
			
				Calendar c = Calendar.getInstance();
				SimpleDateFormat dFecha = new SimpleDateFormat("dd-MM-yyyy", locale);
				SpinnerMarcaModel spm = (SpinnerMarcaModel) spiMarca.getSelectedItem();
				SpinnerProductoModel spPm = (SpinnerProductoModel) spiPro.getSelectedItem();

				
				String fecha = dFecha.format(c.getTime());

				
				int idMarca = spm.getId();
				int idProdu = spPm.getIdProducto();
				String nombreP = spPm.getNombre();
				if(idMarca != 0){
					if(idProdu != 0){
						if(cha1 >= 0 || cha2 >= 0 || cha3 >= 0 || cha4 >= 0 || cha5 >= 0 || cha6 >= 0){
							
							new BDopenHelper(this).insertarFrentes(idTienda, idPromotor, fecha, idMarca, idProdu,cha1,cha2,cha3,cha4,cha5,cha6,1);
							int totalFrentes = cha1+cha2+cha3+cha4+cha5+cha6;
							Toast.makeText(this,"("+totalFrentes+") Frentes Guardados de: \n  "+nombreP, Toast.LENGTH_SHORT).show();
							//spiMarca.setSelection(0);
							spiPro.setSelection(0);
							
							try {
								
								new EnviarDatos(this).enviarFrentes();
								
								
								Echa1.setVisibility(View.INVISIBLE);
								Echa2.setVisibility(View.INVISIBLE);
								Echa3.setVisibility(View.INVISIBLE);
								Echa4.setVisibility(View.INVISIBLE);
								Echa5.setVisibility(View.INVISIBLE);
								Echa6.setVisibility(View.INVISIBLE);
								
								
								Echa1.setText("");
								Echa2.setText("");
								Echa3.setText("");
								Echa4.setText("");
								Echa5.setText("");
								Echa6.setText("");
								
								im.hideSoftInputFromWindow(Echa1.getWindowToken(), 0);
								im.hideSoftInputFromWindow(Echa2.getWindowToken(), 0);
								im.hideSoftInputFromWindow(Echa3.getWindowToken(), 0);
								im.hideSoftInputFromWindow(Echa4.getWindowToken(), 0);
								im.hideSoftInputFromWindow(Echa5.getWindowToken(), 0);
								im.hideSoftInputFromWindow(Echa6.getWindowToken(), 0);
								
							}catch(Exception e) {
								Toast.makeText(this,"error al ocultar", Toast.LENGTH_SHORT).show();
							}
						}else {
							Toast.makeText(this,"No llenaste ninguna charola", Toast.LENGTH_SHORT).show();
						}
					}else{
						Toast.makeText(this,"No seleccionaste Producto", Toast.LENGTH_SHORT).show();
					}
				}else{
					Toast.makeText(this,"No seleccionaste Marca", Toast.LENGTH_SHORT).show();
				}
				
				
				
			}catch(Exception e) {
				Toast.makeText(this,"error 4", Toast.LENGTH_SHORT).show();
			}
			
			
		}catch(Exception e) {
			Toast.makeText(this, "Excep guar"+Echa1.getVisibility(), Toast.LENGTH_SHORT).show();
		}
		

		
	}



	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int id,long arg3) {

		SpinnerMarcaModel spm = (SpinnerMarcaModel)  spiMarca.getSelectedItem();

		
		int idMarca = spm.getId();
		loadSpinnerProd(idMarca);
		
	}



	@Override
	public void onNothingSelected(AdapterView<?> arg0) {

		
	}
	
	private void loadSpinner(){
		try {
			
			
			CustomAdapter adapter = new CustomAdapter(this, android.R.layout.simple_spinner_item, getArrayList());
			spiMarca.setAdapter(adapter);
			
		}catch(Exception e) {
			Toast.makeText(this, "Error Mayoreo 3", Toast.LENGTH_SHORT).show();
		}
		
	}

	
	private void loadSpinnerProd(int idM){
		try {
			ProductosCustomAdapter proAdap = new ProductosCustomAdapter(this, android.R.layout.simple_spinner_item, getArrayListPro(idM));
			spiPro.setAdapter(proAdap);
			
		} catch (Exception e) {
			Toast.makeText(this, "Error Mayoreo 4", Toast.LENGTH_SHORT).show();
		}
	}
	
	private ArrayList<SpinnerProductoModel> getArrayListPro(int idMarca){
		
		Cursor curPro = new BDopenHelper(this).productos(idMarca);
		ArrayList<SpinnerProductoModel> arrayP = new ArrayList<>();
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

		curPro.close();
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

		cursorMarca.close();
		base.close();
		return array;
		
	}
	
		
	
	

}
