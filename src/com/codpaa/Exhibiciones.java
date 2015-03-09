
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
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

import BD.BDopenHelper;

public class Exhibiciones extends Activity implements OnClickListener,OnItemSelectedListener{
	
	int idTienda, idPromotor;
	TextView tienda, promotor;
	SQLiteDatabase base;
	Spinner marca,producto,exhibicion, spinum;
    EditText cantidadExhi;
	Button guardar, salir;
	InputMethodManager im;
	String numeros[] = {"0","0.25","0.5","0.75","1","1.5","1.75","2","2.25","2.50","2.75","3","3.5","3.75","4"};
	ArrayList<SpinnerMarcaModel> array = new ArrayList<SpinnerMarcaModel>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.exhibiciones);
		Intent i = getIntent();
		idTienda = (Integer) i.getExtras().get("idTienda");
		idPromotor = (Integer) i.getExtras().get("idPromotor");
		
		im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		
		tienda = (TextView) findViewById(R.id.ExhiTienda);
		promotor = (TextView) findViewById(R.id.ExhiPromo);
		marca = (Spinner) findViewById(R.id.spiExhMarca);
		spinum = (Spinner) findViewById(R.id.spinnerNumeros);
		producto = (Spinner) findViewById(R.id.spiExhProd);
		exhibicion = (Spinner) findViewById(R.id.spiExhibi);
		guardar = (Button) findViewById(R.id.btnGuaEx);
		salir = (Button) findViewById(R.id.bntSaEnvi);

        cantidadExhi = (EditText) findViewById(R.id.cantidadExhibicion);

		marca.setOnItemSelectedListener(this);
		guardar.setOnClickListener(this);
		salir.setOnClickListener(this);
		
		
		try {
			
			Cursor cNomUser = new BDopenHelper(this).nombrePromotor(idPromotor);
			cNomUser.moveToFirst();
			promotor.setText(cNomUser.getString(0));
			try {
				Cursor cTienda = new BDopenHelper(this).tienda(idTienda);
				cTienda.moveToFirst();
				tienda.setText(cTienda.getString(0)+" "+cTienda.getString(1));
				
				try {

					loadSpinner();
					
					try {
						base = new BDopenHelper(this).getReadableDatabase();
						Cursor cuExhi = base.rawQuery("select idExhibicion as _id, nombre from tipoexhibicion order by nombre asc;", null);
						SimpleCursorAdapter adaptadorExi = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_1, cuExhi, new String[]{"nombre"},new int[]{android.R.id.text1});
						adaptadorExi.setDropDownViewResource(android.R.layout.simple_list_item_2);
						exhibicion.setAdapter(adaptadorExi);
						base.close();
						
						try {

							ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_selectable_list_item, numeros);
							spinum.setAdapter(spinnerArrayAdapter);
							
							
						} catch (Exception e) {
							e.printStackTrace();
						}
						
						
					}catch(Exception e){
						Toast.makeText(this, "Error  4", Toast.LENGTH_SHORT).show();
					}
					
				}catch(Exception e) {
					Toast.makeText(this, "Error  3", Toast.LENGTH_SHORT).show();
				}
				
			}catch(Exception e) {
				Toast.makeText(this, "Error  2", Toast.LENGTH_SHORT).show();
			}
			
			
		}catch(Exception e) {
			Toast.makeText(this, "Error  1", Toast.LENGTH_SHORT).show();
			
		}
		
	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int id,long arg3) {
		SpinnerMarcaModel spM = (SpinnerMarcaModel) marca.getSelectedItem();
		int idMarca = spM.getId();
	
		
		loadSpinnerProd(idMarca);
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		
		
	}

	@Override
	public void onClick(View v) {
		
		switch(v.getId()) {
		
		case R.id.btnGuaEx:
			guardarLosDatos();
			break;
			
		case R.id.bntSaEnvi:
			finish();
			break;
		}
		
		
	}

	private void guardarLosDatos() {
		try {
			
			float cantidad = 0;
			Calendar c = Calendar.getInstance();
			SimpleDateFormat dFecha = new SimpleDateFormat("dd-MM-yyyy");
			
			String fecha = dFecha.format(c.getTime());
			if(cantidadExhi.getText().toString().length() > 0){
                cantidad = Float.valueOf(cantidadExhi.getText().toString());
            }

			//String can = (String) spinum.getSelectedItem();
			//cantidad = Float.parseFloat(can);
			
			
			try {
				SpinnerMarcaModel spM = (SpinnerMarcaModel) marca.getSelectedItem();
				SpinnerProductoModel spP = (SpinnerProductoModel) producto.getSelectedItem();
				
				int idMarca = spM.getId();
				int idProdu = spP.getIdProducto();
				int idEx = (int) exhibicion.getItemIdAtPosition(exhibicion.getSelectedItemPosition());
				
				if(idMarca != 0){
					if(idProdu != 0){
						try {
							new BDopenHelper(this).insertarExhibiciones(idTienda, idPromotor, idEx, fecha, idProdu, cantidad,1);
							Toast.makeText(this,"Datos Guardados", Toast.LENGTH_SHORT).show();
							
							new EnviarDatos(this).enviarExibiciones();
						}catch(Exception e) {
							Toast.makeText(this,"Error al guardar", Toast.LENGTH_SHORT).show();
						}
					}else{
						Toast.makeText(this,"No seleccionaste Producto", Toast.LENGTH_SHORT).show();
					}
				}else{
					Toast.makeText(this,"No seleccionaste Marca", Toast.LENGTH_SHORT).show();
				}
				
				
			}catch(Exception e) {
				
				Toast.makeText(this,"No seleccionaste producto", Toast.LENGTH_SHORT).show();
			}
			
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	private void loadSpinner(){
		try {
			
			
			CustomAdapter adapter = new CustomAdapter(this, android.R.layout.simple_spinner_item, getArrayList());
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
	
	

}
