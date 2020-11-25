package com.codpaa.activity;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.codpaa.R;
import com.codpaa.db.BDopenHelper;

public class ListarLocation extends Activity{
	
	TextView tienda,entrada,salida, sucursal, textFecha;
	LinearLayout linea;
	TableLayout tabla;
	TableRow fila;
	SimpleDateFormat dia, fechaFormat;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.listarlocalizacion);
		
		Calendar c = Calendar.getInstance();
		dia = new SimpleDateFormat("E-dd-MM-yyyy", Locale.getDefault());
		fechaFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
	
		
		String fechaDia = dia.format(c.getTime());
		String fecha = fechaFormat.format(c.getTime());
		
		
		linea = (LinearLayout) findViewById(R.id.linearL);
		textFecha = (TextView) findViewById(R.id.fecha);
		tabla = (TableLayout) findViewById(R.id.tablaEntradas);
		
		textFecha.setText(fechaDia);
		
		
		try{
			
			SQLiteDatabase base = new BDopenHelper(this).getReadableDatabase();
			
			Cursor cursor = base.rawQuery("select grupo,sucursal,ingreso,salida from clientes inner join coordenadasEnviar on clientes.idTienda=coordenadasEnviar.idTienda where coordenadasEnviar.fecha='"+fecha+"';", null);
			
			cursor.moveToFirst();
			
			while(cursor.moveToNext()){
			
				fila = new TableRow(this);
				LinearLayout vertical = new LinearLayout(this);
				tienda = new TextView(this);
				sucursal = new TextView(this);
				entrada = new TextView(this);
				salida = new TextView(this);
				tienda.setText(cursor.getString(0));
				sucursal.setText(cursor.getString(1));
				vertical.setOrientation(LinearLayout.VERTICAL);
				vertical.addView(tienda);
				vertical.addView(sucursal);
				entrada.setText(cursor.getString(2));
				salida.setText(cursor.getString(3));
				fila.addView(vertical);
				fila.addView(entrada);
				fila.addView(salida);
				fila.setPadding(0, 0, 0, 15);
				
				tabla.addView(fila);
				
				
				
				
			}
			cursor.close();
			
			base.close();
			
		}catch(Exception e){
			Toast.makeText(this, "error 4", Toast.LENGTH_SHORT).show();
		}
		
	}
	
	

}
