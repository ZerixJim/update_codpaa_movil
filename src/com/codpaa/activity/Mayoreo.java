package com.codpaa.activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;


import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.codpaa.adapter.MarcasAdapter;
import com.codpaa.model.MarcaModel;
import com.codpaa.update.EnviarDatos;
import com.codpaa.R;
import com.codpaa.db.BDopenHelper;

public class Mayoreo extends AppCompatActivity implements OnClickListener{
	
	Spinner spMarca;
	EditText cantidad;
	SQLiteDatabase base;
	ArrayList<MarcaModel> array = new ArrayList<>();
	Button guardar, salir;
    Locale locale;
	int idCelular;
	
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mayreportecajas);
        locale = new Locale("es_MX");
		
		Intent i = getIntent();
		idCelular = (Integer) i.getExtras().get("idCelular");
		
		spMarca = (Spinner) findViewById(R.id.spMayMarca);

		
		cantidad = (EditText) findViewById(R.id.edMayCajas);
		
		guardar = (Button) findViewById(R.id.btnGuCajasM);
		salir = (Button) findViewById(R.id.btnSalCaMay);
		

		
		guardar.setOnClickListener(this);
		salir.setOnClickListener(this);
		

		loadSpinner();

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayUseLogoEnabled(true);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setLogo(R.drawable.ic_launcher);
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
	
	private void loadSpinner(){
		try {
			
			
			MarcasAdapter adapter = new MarcasAdapter(this, android.R.layout.simple_spinner_item, getArrayList());
			spMarca.setAdapter(adapter);
			
		}catch(Exception e) {
			Toast.makeText(this, "Error Mayoreo 3", Toast.LENGTH_SHORT).show();
		}
		
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
			SimpleDateFormat dFecha = new SimpleDateFormat("dd-MM-yyyy", locale);
			String fecha = dFecha.format(c.getTime());
			
			
			MarcaModel spm = (MarcaModel)  spMarca.getSelectedItem();

			if(spm.getId() != 0){

				
				if(cantidad.getText().length() >0) {
					cajas = Integer.parseInt(cantidad.getText().toString());
					int idMarca = spm.getId();
					try {

						new BDopenHelper(this).insertarCajasMay(idCelular, idMarca, fecha, cajas, 1);
						Toast.makeText(this, cajas+" Cajas Guardadas de: \n -"+spm.getNombre(), Toast.LENGTH_SHORT).show();
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
