package com.codpaa.activity;



import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import android.view.inputmethod.InputMethodManager;

import android.widget.EditText;
import android.widget.Toast;

import com.codpaa.update.EnviarDatos;
import com.codpaa.R;
import com.codpaa.db.BDopenHelper;

public class ComentariosActivity extends AppCompatActivity {
	
	

	EditText editComentario;
	BDopenHelper BD;
	EnviarDatos EnviaDatos;
	private int idTienda, idPromotor;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.comentarios);
		
		Intent recibeIdTi = getIntent();
		idTienda = recibeIdTi.getExtras().getInt("idTienda", 0);
		idPromotor = recibeIdTi.getExtras().getInt("idPromotor", 0);

		editComentario = (EditText) findViewById(R.id.editComen);
		

		
		EnviaDatos = new EnviarDatos(this);


		ActionBar actionBar = getSupportActionBar();
		if (actionBar != null){
			actionBar.setDisplayHomeAsUpEnabled(true);
		}
		
	}

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;


			case R.id.save_comentarios:

				guardarComentario();

				return true;
            default: return super.onOptionsItemSelected(item);
        }
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

		BD = new BDopenHelper(this);
		Calendar c = Calendar.getInstance();
		SimpleDateFormat dFecha = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
		
			
		String fecha = dFecha.format(c.getTime());
		
		
		try{

			if(editComentario.getText().length() > 0){
				String comentario = editComentario.getText().toString();
				BD.insertarComentarios(idTienda, idPromotor, fecha, comentario);
				editComentario.setText("");
				Toast.makeText(this, "Guardado", Toast.LENGTH_SHORT).show();
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				if (imm != null) {
					imm.hideSoftInputFromWindow(editComentario.getWindowToken(), 0);
				}
				Toast.makeText(this, "Comentario Guardado", Toast.LENGTH_SHORT).show();
				EnviaDatos.enviarComentario();
				
				finish();
				
			}else{
				Toast.makeText(this, "Escriba comentario", Toast.LENGTH_SHORT).show();
			}
			
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		
		
	}
	
	
	

}
