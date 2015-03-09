package com.codpaa;




import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;




public class EnviarInformacion extends Activity implements OnClickListener{

	EnviarDatos enviar;
	Button btnVisitas, btnSalir, btnFotos;
	SQLiteDatabase base;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.enviar);
		
		btnVisitas = (Button) findViewById(R.id.btnVisitasP);
		btnSalir = (Button) findViewById(R.id.bntSaEnvi);
		btnFotos = (Button) findViewById(R.id.btnFotosNo);
		
		
		btnVisitas.setOnClickListener(this);
		btnSalir.setOnClickListener(this);
		btnFotos.setOnClickListener(this);
	
		
		
		enviar = new EnviarDatos(this);
		
		
	}


	@Override
	public void onClick(View v) {
		
		switch(v.getId()) {
		case R.id.btnVisitasP:
			visitas();
			
			break;
			
		case R.id.bntSaEnvi:
			finish();
			break;
			
		case R.id.btnFotosNo:
			Intent i = new Intent(this,Imagesheduler.class);
			startActivity(i);
			break;
		
		
		}

		
	}
	
	
	public void visitas(){
		
		Toast.makeText(this, "Enviando..",Toast.LENGTH_LONG).show();
		
		enviar.vistasPendientes();
		
	}
	
	

	@Override
	protected void onStart() {
		super.onStart();
		
		
	}
	
	
	
	
	

}
