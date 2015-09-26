package com.codpaa.activity;



import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.codpaa.update.EnviarDatos;
import com.codpaa.R;
import com.codpaa.db.BDopenHelper;

public class ComentariosActivity extends AppCompatActivity implements OnClickListener{
	
	
	Button btnGuardar;
	EditText editComentario;
	BDopenHelper BD;
	EnviarDatos EnviaDatos;
	private int idTienda, idPromotor;
	Locale local;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.comentarios);
		
		Intent recibeIdTi = getIntent();
		idTienda = (Integer) recibeIdTi.getExtras().get("idTienda");
		idPromotor = (Integer) recibeIdTi.getExtras().get("idPromotor");
		
		btnGuardar = (Button) findViewById(R.id.btnGuaComentario);

		editComentario = (EditText) findViewById(R.id.editComen);
		
		
		btnGuardar.setOnClickListener(this);

		
		EnviaDatos = new EnviarDatos(this);
		
		local = new Locale("es_MX");

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
            default: return super.onOptionsItemSelected(item);
        }
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

	@Override
	public void onClick(View v) {
		
		switch(v.getId()){
		case R.id.btnGuaComentario:
			guardarComentario();

			break;

		}
		
		
	}
	
	private void guardarComentario(){
		Toast.makeText(this, "guardando..", Toast.LENGTH_SHORT).show();
		BD = new BDopenHelper(this);
		Calendar c = Calendar.getInstance();
		SimpleDateFormat dFecha = new SimpleDateFormat("dd-MM-yyyy",local);
		
			
		String fecha = dFecha.format(c.getTime());
		
		
		try{

			if(editComentario.getText().length() > 0){
				String comentario = editComentario.getText().toString();
				BD.insertarComentarios(idTienda, idPromotor, fecha, comentario);
				editComentario.setText("");
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(editComentario.getWindowToken(), 0);
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
