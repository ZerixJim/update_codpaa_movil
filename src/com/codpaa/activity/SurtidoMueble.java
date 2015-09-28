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
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.codpaa.adapter.CustomAdapter;
import com.codpaa.adapter.MarcasAdapter;
import com.codpaa.model.MarcaModel;
import com.codpaa.update.EnviarDatos;
import com.codpaa.adapter.ProductosCustomAdapter;
import com.codpaa.R;
import com.codpaa.model.SpinnerMarcaModel;
import com.codpaa.model.SpinnerProductoModel;
import com.codpaa.db.BDopenHelper;

public class SurtidoMueble extends AppCompatActivity implements OnClickListener, OnItemSelectedListener,OnCheckedChangeListener{
	
	Spinner spiMar, spiPro;
	EditText cantidad;
	TextInputLayout cantidadLayout;
	TextView usuario,tienda;
	InputMethodManager im;
	SQLiteDatabase base;
	RadioGroup radio;
	RadioButton si,no,selec;
	Button guardar;
	ArrayList<MarcaModel> array = new ArrayList<>();
	int idTienda, idPromotor;
	Locale locale;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.surtidomueble);
		
		Intent i = getIntent();
		idTienda = (Integer) i.getExtras().get("idTienda");
		idPromotor = (Integer) i.getExtras().get("idPromotor");
		
		spiMar = (Spinner) findViewById(R.id.spiSurM);
		spiPro = (Spinner) findViewById(R.id.spiSurP);
		cantidad = (EditText) findViewById(R.id.editSur);
		guardar = (Button) findViewById(R.id.btnGurSur);

		
		usuario = (TextView) findViewById(R.id.textSuser);
		tienda = (TextView) findViewById(R.id.textSurT);
		radio = (RadioGroup) findViewById(R.id.radioGroup1);
		si = (RadioButton) radio.findViewById(R.id.radio0);
		no = (RadioButton) radio.findViewById(R.id.radio1);

		cantidadLayout = (TextInputLayout) findViewById(R.id.txt_input_cantidad);
		
		guardar.setOnClickListener(this);


		spiMar.setOnItemSelectedListener(this);
		
		//cantidad.setVisibility(View.INVISIBLE);
		cantidadLayout.setVisibility(View.INVISIBLE);
		im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		
		radio.setOnCheckedChangeListener(this);
		no.setChecked(true);
		
		
		
		try {
			
			Cursor cNomUser = new BDopenHelper(this).nombrePromotor(idPromotor);
			cNomUser.moveToFirst();
			usuario.setText(cNomUser.getString(0));
			try {
				Cursor cTienda = new BDopenHelper(this).tienda(idTienda);
				cTienda.moveToFirst();
				tienda.setText(cTienda.getString(0)+" "+cTienda.getString(1));
				
				try {

					
					loadSpinner();
					
					
				}catch(Exception e) {
					Toast.makeText(this, "Error Surtido 3", Toast.LENGTH_SHORT).show();
				}
				
			}catch(Exception e) {
				Toast.makeText(this, "Error surtido 2", Toast.LENGTH_SHORT).show();
			}
			
			
		}catch(Exception e) {
			Toast.makeText(this, "Error surtido 1", Toast.LENGTH_SHORT).show();
			
		}

		locale = new Locale("es_MX");

		ActionBar actionBar = getSupportActionBar();
		if (actionBar != null){
			actionBar.setDisplayHomeAsUpEnabled(true);
			actionBar.setDisplayUseLogoEnabled(true);
			actionBar.setHomeButtonEnabled(true);


		}
		
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
	public void onItemSelected(AdapterView<?> adapter, View v, int id,long arg3) {

		MarcaModel spM = (MarcaModel) spiMar.getSelectedItem();
		int idMarca = spM.getId();

		loadSpinnerProd(idMarca);
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		
		
	}

	
	@Override
	public void onClick(View v) {
		
		switch(v.getId()) {
		
		case R.id.btnGurSur: guardar();

			break;
		
		}
		
		
	}



	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		
		switch(group.getCheckedRadioButtonId()) {
		
		case R.id.radio0: //cantidad.setVisibility(View.VISIBLE);
                            cantidadLayout.setVisibility(View.VISIBLE);
						  cantidad.requestFocus();
						  im.showSoftInput(cantidad, 0);
		
						  break;
		case R.id.radio1: //cantidad.setVisibility(View.INVISIBLE);
                            cantidadLayout.setVisibility(View.INVISIBLE);
						  im.hideSoftInputFromWindow(cantidad.getWindowToken(), 0);
					      break;
		}
		
	}
	
	private void guardar() {
		try {
			
			int cajas = 0;
			Calendar c = Calendar.getInstance();
			SimpleDateFormat dFecha = new SimpleDateFormat("dd-MM-yyyy", locale);
			String fecha = dFecha.format(c.getTime());
			
			
			selec = (RadioButton) findViewById(radio.getCheckedRadioButtonId());
			MarcaModel spM = (MarcaModel) spiMar.getSelectedItem();
			SpinnerProductoModel spP = (SpinnerProductoModel) spiPro.getSelectedItem();
			
			//int idProdu = (int) spiPro.getItemIdAtPosition(spiPro.getSelectedItemPosition());
			int idProdu =  spP.getIdProducto();
			int idMarca = spM.getId();
			
			if(idMarca != 0){
				if(idProdu != 0){
					if(cantidad.isShown() && cantidad.getText().length() >0) {
						cajas = Integer.parseInt(cantidad.getText().toString());
						
					} 
					
					try {
						Toast.makeText(this, "Surtido guardado de:\n "+spP.getNombre(), Toast.LENGTH_SHORT).show();
						new BDopenHelper(this).insertarSurtido(idTienda, idPromotor, selec.getText().toString(), fecha, idProdu, cajas);
						im.hideSoftInputFromWindow(cantidad.getWindowToken(), 0);
						cantidad.setText("");
						new EnviarDatos(this).enviarSurtido();
					}catch(Exception e) {
						Toast.makeText(this, "No se guardo, error", Toast.LENGTH_SHORT).show();
						
					}
				}else{
					Toast.makeText(this, "No Selecionaste Producto", Toast.LENGTH_SHORT).show();
				}
			}else{
				Toast.makeText(this, "No Selecionaste Marca", Toast.LENGTH_SHORT).show();
			}
			
			
			
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	private void loadSpinner(){
		try {
			
			
			MarcasAdapter adapter = new MarcasAdapter(this, android.R.layout.simple_spinner_item, getArrayList());
			spiMar.setAdapter(adapter);
			
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
            spP.setCodigoBarras(curPro.getString(3));
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
	
	

}
