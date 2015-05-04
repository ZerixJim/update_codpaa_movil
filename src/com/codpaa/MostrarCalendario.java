package com.codpaa;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import BD.BDopenHelper;

public class MostrarCalendario extends AppCompatActivity implements OnClickListener{
	
	SQLiteDatabase base;
	TextView rol;
	LinearLayout lunes, martes, miercoles,jueves, viernes, sabado;
	Button regresar;



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.calendario);



		
		lunes = (LinearLayout) findViewById(R.id.colunes);
		martes = (LinearLayout) findViewById(R.id.columnaMartes);
		miercoles = (LinearLayout)findViewById(R.id.columnaMiercoles);
		jueves = (LinearLayout) findViewById(R.id.columnaJueves);
		viernes = (LinearLayout) findViewById(R.id.columnaViernes);
		sabado = (LinearLayout) findViewById(R.id.columnaSabado);
		regresar = (Button) findViewById(R.id.btncalendario);
		regresar.setOnClickListener(this);
		
		lunes();
		martes();
		miercoles();
		jueves();
		viernes();
		sabado();
		
		
			
	}



	public void lunes(){
			
		base = new BDopenHelper(this).getReadableDatabase();
		String Lunes="select clientes.grupo, clientes.sucursal, rol from clientes inner join visitaTienda on clientes.idTienda = visitaTienda.idTienda and visitaTienda.lunes=1";
		Cursor cursor = base.rawQuery(Lunes, null);
			
		int contColor = 1;
		
		while(cursor.moveToNext()) {
				
				
			TextView grupo= new TextView(this);
			TextView sucursal = new TextView(this);
			rol = new TextView(this);
			LinearLayout acomodo = new LinearLayout(this);
			
				
			acomodo.setOrientation(LinearLayout.VERTICAL);
			acomodo.setPadding(3, 2, 3, 1);
			
			
			grupo.setText(cursor.getString(0));
			grupo.setTextColor(Color.BLACK);
			
			sucursal.setText(cursor.getString(1));
			sucursal.setTextColor(Color.parseColor("#303030"));
		
			
			rol.setText(cursor.getString(2));
			rol.setTextColor(Color.parseColor("#555b52"));
			
				
			acomodo.addView(grupo);
			acomodo.addView(sucursal);
			acomodo.addView(rol);
			
			
				
			if((contColor %2) == 0)
				acomodo.setBackgroundColor(Color.parseColor("#62C2E3")); 
					
			else
				acomodo.setBackgroundColor(Color.parseColor("#d7e7e8")); 
					
					
				
			lunes.addView(acomodo);
				
			contColor++;
			
		}
		
		cursor.close();
		base.close();
		
		
		
	}
	
	public void martes(){
		
		base = new BDopenHelper(this).getReadableDatabase();
		String Lunes="select clientes.grupo, clientes.sucursal, rol from clientes inner join visitaTienda on clientes.idTienda = visitaTienda.idTienda and visitaTienda.martes=1";
		Cursor cursor = base.rawQuery(Lunes, null);
			
		int contColor = 1;
			
		while(cursor.moveToNext()) {
				
				
			TextView grupo= new TextView(this);
			TextView sucursal = new TextView(this);
			rol = new TextView(this);
			LinearLayout acomodo = new LinearLayout(this);
				
			acomodo.setOrientation(LinearLayout.VERTICAL);
			acomodo.setPadding(3, 2, 3, 1);
				
			grupo.setText(cursor.getString(0));
			grupo.setTextColor(Color.BLACK);
			
			sucursal.setText(cursor.getString(1));
			sucursal.setTextColor(Color.parseColor("#303030"));
		
			
			rol.setText(cursor.getString(2));
			rol.setTextColor(Color.parseColor("#555b52"));
				
			acomodo.addView(grupo);
			acomodo.addView(sucursal);
			acomodo.addView(rol);
				
			if((contColor %2) == 0)
				acomodo.setBackgroundColor(Color.parseColor("#d7e7e8")); 
					
			else
				acomodo.setBackgroundColor(Color.parseColor("#62C2E3")); 
					
					
				
			martes.addView(acomodo);
				
			contColor++;
			
		}
		cursor.close();
		base.close();
		
		
	}
	
	public void miercoles(){
		
		
		base = new BDopenHelper(this).getReadableDatabase();
		String Lunes="select clientes.grupo, clientes.sucursal, rol from clientes inner join visitaTienda on clientes.idTienda = visitaTienda.idTienda and visitaTienda.miercoles=1";
		Cursor cursor = base.rawQuery(Lunes, null);
			
		int contColor = 1;
			
		while(cursor.moveToNext()) {
			TextView grupo= new TextView(this);
			TextView sucursal = new TextView(this);
			rol = new TextView(this);
			LinearLayout acomodo = new LinearLayout(this);
				
			acomodo.setOrientation(LinearLayout.VERTICAL);
			acomodo.setPadding(3, 2, 3, 1);
				
			grupo.setText(cursor.getString(0));
			grupo.setTextColor(Color.BLACK);
			
			sucursal.setText(cursor.getString(1));
			sucursal.setTextColor(Color.parseColor("#303030"));
		
			
			rol.setText(cursor.getString(2));
			rol.setTextColor(Color.parseColor("#555b52"));
				
			acomodo.addView(grupo);
			acomodo.addView(sucursal);
			acomodo.addView(rol);
				
			if((contColor %2) == 0)
				acomodo.setBackgroundColor(Color.parseColor("#62C2E3")); 
					
			else
				acomodo.setBackgroundColor(Color.parseColor("#d7e7e8")); 
					
					
				
			miercoles.addView(acomodo);
				
			contColor++;
			
		}
		cursor.close();
		base.close();
		
		
	}
	
	public void jueves(){
		
		base = new BDopenHelper(this).getReadableDatabase();
		String Lunes="select clientes.grupo, clientes.sucursal, rol from clientes inner join visitaTienda on clientes.idTienda = visitaTienda.idTienda and visitaTienda.jueves=1";
		Cursor cursor = base.rawQuery(Lunes, null);
			
		int contColor = 1;
			
		while(cursor.moveToNext()) {
				
				
			TextView grupo= new TextView(this);
			TextView sucursal = new TextView(this);
			rol = new TextView(this);
			LinearLayout acomodo = new LinearLayout(this);
				
			acomodo.setOrientation(LinearLayout.VERTICAL);
			acomodo.setPadding(3, 2, 3, 1);
				
			grupo.setText(cursor.getString(0));
			grupo.setTextColor(Color.BLACK);
			
			sucursal.setText(cursor.getString(1));
			sucursal.setTextColor(Color.parseColor("#303030"));
		
			
			rol.setText(cursor.getString(2));
			rol.setTextColor(Color.parseColor("#555b52"));
				
			acomodo.addView(grupo);
			acomodo.addView(sucursal);
			acomodo.addView(rol);
				
			if((contColor %2) == 0)
				acomodo.setBackgroundColor(Color.parseColor("#d7e7e8")); 
					
			else
				acomodo.setBackgroundColor(Color.parseColor("#62C2E3")); 
					
					
				
			jueves.addView(acomodo);
				
			contColor++;
			
		}
		cursor.close();
		base.close();
		
	}
	
	public void viernes(){
		
		base = new BDopenHelper(this).getReadableDatabase();
		String Lunes="select clientes.grupo, clientes.sucursal, rol from clientes inner join visitaTienda on clientes.idTienda = visitaTienda.idTienda and visitaTienda.viernes=1";
		Cursor cursor = base.rawQuery(Lunes, null);
			
		int contColor = 1;
			
		while(cursor.moveToNext()) {
				
				
			TextView grupo= new TextView(this);
			TextView sucursal = new TextView(this);
			rol = new TextView(this);
			LinearLayout acomodo = new LinearLayout(this);
				
			acomodo.setOrientation(LinearLayout.VERTICAL);
			acomodo.setPadding(3, 2, 3, 1);
				
			grupo.setText(cursor.getString(0));
			grupo.setTextColor(Color.BLACK);
			
			sucursal.setText(cursor.getString(1));
			sucursal.setTextColor(Color.parseColor("#303030"));
		
			
			rol.setText(cursor.getString(2));
			rol.setTextColor(Color.parseColor("#555b52"));
				
			acomodo.addView(grupo);
			acomodo.addView(sucursal);
			acomodo.addView(rol);
				
			if((contColor %2) == 0)
				acomodo.setBackgroundColor(Color.parseColor("#62C2E3")); 
					
			else
				acomodo.setBackgroundColor(Color.parseColor("#d7e7e8")); 
					
					
				
			viernes.addView(acomodo);
				
			contColor++;
			
		}
		cursor.close();
		base.close();
		
	}
	
	public void sabado(){
		
		base = new BDopenHelper(this).getReadableDatabase();
		String Lunes="select clientes.grupo, clientes.sucursal, rol from clientes inner join visitaTienda on clientes.idTienda = visitaTienda.idTienda and visitaTienda.sabado=1";
		Cursor cursor = base.rawQuery(Lunes, null);
			
		int contColor = 1;
			
		while(cursor.moveToNext()) {
				
			TextView grupo= new TextView(this);
			TextView sucursal = new TextView(this);
			rol = new TextView(this);
			LinearLayout acomodo = new LinearLayout(this);
			acomodo.setOrientation(LinearLayout.VERTICAL);
			acomodo.setPadding(3, 2, 3, 1);
				
			grupo.setText(cursor.getString(0));
			grupo.setTextColor(Color.BLACK);
			
			sucursal.setText(cursor.getString(1));
			sucursal.setTextColor(Color.parseColor("#303030"));
		
			
			rol.setText(cursor.getString(2));
			rol.setTextColor(Color.parseColor("#555b52"));
			
			acomodo.addView(grupo);
			acomodo.addView(sucursal);
			acomodo.addView(rol);
				
			if((contColor %2) == 0)
				acomodo.setBackgroundColor(Color.parseColor("#d7e7e8")); 
					
			else
				acomodo.setBackgroundColor(Color.parseColor("#62C2E3")); 
					
					
				
			sabado.addView(acomodo);
				
			contColor++;
			
		}
		
		cursor.close();
		base.close();
	}

	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.btncalendario)
			finish();
		
	}
	

	
	

	
	
	
	
	

}
