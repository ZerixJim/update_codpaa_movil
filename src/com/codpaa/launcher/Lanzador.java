package com.codpaa.launcher;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.codpaa.R;
import com.codpaa.activity.MainActivity;
import com.codpaa.service.GeoLocalizar;

public class Lanzador extends Activity{
	
	private final static int SPlASH_TIME_LENGTH = 2000;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lanzador);
		
		
		
		Handler handler = new Handler();
		handler.postDelayed(getRunnableStartApp(), SPlASH_TIME_LENGTH);
		
		Intent servicio = new Intent(this, GeoLocalizar.class);
		startService(servicio);
		
	}
	
	private Runnable getRunnableStartApp(){
		
		return new Runnable(){
			public void run(){
		
				Intent intent = new Intent(Lanzador.this, MainActivity.class);
				startActivity(intent);
				finish();
			}
		};

	}
	
	
	 

}
