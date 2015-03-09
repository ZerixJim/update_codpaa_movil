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
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

import BD.BDopenHelper;

public class InventarioBodega extends Activity implements OnClickListener,OnItemSelectedListener{
	
	
	int idTienda, idPromotor;
	TextView tienda, promotor;
	SQLiteDatabase base;
    RadioButton piezas, cajas, selec;
    RadioGroup radio;
	Spinner marca,producto;
	Button guardar, salir;
	EditText editFisico, editSistema;
	InputMethodManager im;
	ArrayList<SpinnerMarcaModel> array = new ArrayList<SpinnerMarcaModel>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.inventariobodega);
		Intent i = getIntent();
		idTienda = (Integer) i.getExtras().get("idTienda");
		idPromotor = (Integer) i.getExtras().get("idPromotor");
		
		im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		
		tienda = (TextView) findViewById(R.id.textInvTie);
		promotor = (TextView) findViewById(R.id.textInvPromo);
		marca = (Spinner) findViewById(R.id.spiInMar);
		producto = (Spinner) findViewById(R.id.spiInvPro);
	
		guardar = (Button) findViewById(R.id.bInvGuar);
		salir = (Button) findViewById(R.id.bInvSalir);
		editFisico = (EditText) findViewById(R.id.editInv);
        editSistema = (EditText) findViewById(R.id.editSistema);

        radio = (RadioGroup) findViewById(R.id.radioInventario);
        piezas = (RadioButton) radio.findViewById(R.id.radioTipo1);
        cajas = (RadioButton) radio.findViewById(R.id.radioTipo2);

		piezas.setChecked(true);
		
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
		
		case R.id.bInvGuar:
			guardarLosDatos();
			break;
			
		case R.id.bInvSalir:
			finish();
			break;
		}
		
		
	}

	private void guardarLosDatos() {
		try {
			
			int cantidadFisico = 0, cantidadSistema = 0;
			Calendar c = Calendar.getInstance();
			SimpleDateFormat dFecha = new SimpleDateFormat("dd-MM-yyyy");
			
			SpinnerMarcaModel spM = (SpinnerMarcaModel) marca.getSelectedItem();
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
							
							try {
								new BDopenHelper(this).insertarInventario(idTienda,idPromotor ,fecha, idProdu, cantidadFisico,cantidadSistema,1,selec.getText().toString());
								Toast.makeText(this,"Datos Guardados", Toast.LENGTH_SHORT).show();
								editFisico.setText("");
                                editSistema.setText("");
                                producto.setSelection(0);
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