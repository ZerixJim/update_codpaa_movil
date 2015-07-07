package com.codpaa.activitys;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.codpaa.adapters.CustomAdapter;
import com.codpaa.updates.EnviarDatos;
import com.codpaa.R;
import com.codpaa.models.SpinnerMarcaModel;
import com.codpaa.db.BDopenHelper;

public class Mayoreo extends Activity implements OnClickListener{
	
	Spinner spMarca;
	EditText cantidad;
	SQLiteDatabase base;
	ArrayList<SpinnerMarcaModel> array = new ArrayList<>();
	Button guardar, salir;
	int idCelular;
	
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mayreportecajas);
		
		Intent i = getIntent();
		idCelular = (Integer) i.getExtras().get("idCelular");
		
		spMarca = (Spinner) findViewById(R.id.spMayMarca);

		
		cantidad = (EditText) findViewById(R.id.edMayCajas);
		
		guardar = (Button) findViewById(R.id.btnGuCajasM);
		salir = (Button) findViewById(R.id.btnSalCaMay);
		

		
		guardar.setOnClickListener(this);
		salir.setOnClickListener(this);
		

		loadSpinner();

	}
	
	private void loadSpinner(){
		try {
			
			
			CustomAdapter adapter = new CustomAdapter(this, android.R.layout.simple_spinner_item, getArrayList());
			spMarca.setAdapter(adapter);
			
		}catch(Exception e) {
			Toast.makeText(this, "Error Mayoreo 3", Toast.LENGTH_SHORT).show();
		}
		
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
		
		array.add(0, spiMfirst);
		cursorMarca.close();
		base.close();
		return array;
		
	}


	@Override
	protected void onPause() {

		super.onPause();
	}

	@Override
	protected void onRestart() {

		super.onRestart();
	}

	@Override
	protected void onResume() {

		super.onResume();
	}
	


	@Override
	public void onClick(View v) {
		switch(v.getId()){
		
		case R.id.btnGuCajasM:
			guardar();
			break;
		case R.id.btnSalCaMay: finish(); break;
		}
		
	}
	
	private void guardar(){
		try {
			
			int cajas;
			Calendar c = Calendar.getInstance();
			SimpleDateFormat dFecha = new SimpleDateFormat("dd-MM-yyyy");
			String fecha = dFecha.format(c.getTime());
			
			
			SpinnerMarcaModel spm = (SpinnerMarcaModel)  spMarca.getSelectedItem();

			if(spm.getId() != 0){

				
				if(cantidad.getText().length() >0) {
					cajas = Integer.parseInt(cantidad.getText().toString());
					int idMarca = spm.getId();
					try {

						new BDopenHelper(this).insertarCajasMay(idCelular, idMarca, fecha, cajas, 1);
						Toast.makeText(this, cajas+" Cajas Guardadas de: \n -", Toast.LENGTH_SHORT).show();
						cantidad.setText("");
						
						new EnviarDatos(this).enviarCajasMay();
					}catch(Exception e) {
						Toast.makeText(this, "No se guardo, error", Toast.LENGTH_SHORT).show();
						
					}
					
				}else{
					Toast.makeText(this, "Campo Cantidad Cajas \n -esta vacio", Toast.LENGTH_SHORT).show();
				}
				
				
			}else{
				Toast.makeText(this, "No Seleccionaste Marca", Toast.LENGTH_SHORT).show();
			}
			
			
			
			
			
			
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	


}
