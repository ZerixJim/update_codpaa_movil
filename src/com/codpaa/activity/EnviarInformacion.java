package com.codpaa.activity;


import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.codpaa.update.EnviarDatos;
import com.codpaa.R;


public class EnviarInformacion extends AppCompatActivity implements OnClickListener{

	EnviarDatos enviar;
	Button btnVisitas, btnFotos;
	SQLiteDatabase base;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.enviar);
		
		btnVisitas = (Button) findViewById(R.id.btnVisitasP);

		btnFotos = (Button) findViewById(R.id.btnFotosNo);
		
		
		btnVisitas.setOnClickListener(this);

		btnFotos.setOnClickListener(this);
	
		
		
		enviar = new EnviarDatos(this);

		try {
			assert getSupportActionBar() != null;
			ActionBar actionBar = getSupportActionBar();
			actionBar.setDisplayHomeAsUpEnabled(true);
			actionBar.setDisplayUseLogoEnabled(true);
			actionBar.setHomeButtonEnabled(true);
			actionBar.setLogo(R.drawable.ic_launcher);
		}catch (NullPointerException e){
			e.printStackTrace();
		}
		
		
	}


	@Override
	public void onClick(View v) {
		
		switch(v.getId()) {
		case R.id.btnVisitasP:
			visitas();
			
			break;

			
		case R.id.btnFotosNo:
			Intent i = new Intent(this,ImageSheduler.class);
			startActivity(i);
			break;

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
	
	
	public void visitas(){
		
		Toast.makeText(this, "Enviando..",Toast.LENGTH_LONG).show();
		
		enviar.enviarVisitas();
		
	}
	
	

	@Override
	protected void onStart() {
		super.onStart();
		
		
	}
	
	


}
