package com.codpaa.launcher;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;

import com.codpaa.R;
import com.codpaa.activity.Disclosure;
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




		try{
			Intent myIntent = new Intent(this, GeoLocalizar.class);


			startService(myIntent);

			if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1  ){

				//startForegroundService(myIntent);
			}else {

				//startService(myIntent);

			}


		}catch (Exception e){

			e.printStackTrace();

		}
		
	}
	
	private Runnable getRunnableStartApp(){
		
		return new Runnable(){
			public void run(){
		
				Intent intent = new Intent(Lanzador.this, Disclosure.class); //Aquí iba MainActivity
				startActivity(intent);
				finish();
			}
		};

	}
	
	
	 

}
