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
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import android.view.inputmethod.InputMethodManager;

import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.codpaa.adapter.MarcasAdapter;
import com.codpaa.model.MarcaModel;
import com.codpaa.update.EnviarDatos;
import com.codpaa.R;
import com.codpaa.db.BDopenHelper;

public class ComentariosActivity extends AppCompatActivity {
	
	

	private EditText editComentario;
	private Spinner spinnerMarca;
	private EnviarDatos EnviaDatos;
	private int idTienda, idPromotor;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.comentarios);
		
		Intent recibeIdTi = getIntent();
		idTienda = recibeIdTi.getExtras().getInt("idTienda", 0);
		idPromotor = recibeIdTi.getExtras().getInt("idPromotor", 0);

		editComentario = findViewById(R.id.editComen);
		spinnerMarca =  findViewById(R.id.spinner_marca);

		
		EnviaDatos = new EnviarDatos(this);


		ActionBar actionBar = getSupportActionBar();
		if (actionBar != null){
			actionBar.setDisplayHomeAsUpEnabled(true);
		}

		loadSpinnerMarca();
	}


	private void loadSpinnerMarca(){

		MarcasAdapter adapter = new MarcasAdapter(this, android.R.layout.simple_spinner_item, getMarcas());
		spinnerMarca.setAdapter(adapter);


	}


	private ArrayList<MarcaModel> getMarcas(){

		SQLiteDatabase base = new BDopenHelper(this).getReadableDatabase();
		String sql = "select idMarca, nombre, img from marca order by nombre asc;";
		Cursor cursorMarca = base.rawQuery(sql, null);
		ArrayList<MarcaModel> array = new ArrayList<>();

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
    public boolean onOptionsItemSelected(MenuItem item) {
		int itemId = item.getItemId();
		if (itemId == android.R.id.home) {
			finish();
			return true;
		} else if (itemId == R.id.save_comentarios) {
			guardarComentario();

			return true;
		}
		return super.onOptionsItemSelected(item);
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		getMenuInflater().inflate(R.menu.comentarios, menu);

		return true;
	}

	@Override
	protected void onPause() {
		super.onPause();
		
	}

	@Override
	protected void onResume() {
		super.onResume();
		
	}

	@Override
	protected void onStart() {
		super.onStart();
		
	}


	
	private void guardarComentario(){

		BDopenHelper BD = new BDopenHelper(this);
		Calendar c = Calendar.getInstance();
		SimpleDateFormat dFecha = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
		
			
		String fecha = dFecha.format(c.getTime());



		
		try{

			MarcaModel marcaModel = (MarcaModel) spinnerMarca.getSelectedItem();

			int idMarca = marcaModel.getId();

			if(editComentario.getText().length() > 0){

				if (idMarca>0){

					String comentario = editComentario.getText().toString();
					BD.insertarComentarios(idTienda, idPromotor, fecha, comentario, idMarca);
					editComentario.setText("");
					Toast.makeText(this, "Guardado", Toast.LENGTH_SHORT).show();
					InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					if (imm != null) {
						imm.hideSoftInputFromWindow(editComentario.getWindowToken(), 0);
					}
					Toast.makeText(this, "Comentario Guardado", Toast.LENGTH_SHORT).show();
					EnviaDatos.enviarComentario();

					finish();


				}else {
					Toast.makeText(this, "Marca faltante", Toast.LENGTH_SHORT).show();
				}




				
			}else{
				Toast.makeText(this, "Escriba comentario", Toast.LENGTH_SHORT).show();
			}
			
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		
		
	}
	
	
	

}
