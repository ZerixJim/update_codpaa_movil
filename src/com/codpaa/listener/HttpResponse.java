package com.codpaa.listener;


import org.json.JSONException;
import org.json.JSONObject;



import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;

import com.codpaa.db.BDopenHelper;

import cz.msebera.android.httpclient.Header;

public class HttpResponse extends JsonHttpResponseHandler{
	


	private int idTienda;
	private String fecha;
	private String tipo;
	
	SQLiteDatabase base;
	Context miCon;
	
	
	public HttpResponse(Context con,int idTi,String fecha, String tipo){
		
		this.miCon = con;
		this.idTienda = idTi;
		this.fecha = fecha;
		this.tipo = tipo;
		
	}

	
	@Override
	public void onSuccess(int statusCode,Header[] headers ,JSONObject response) {
		
		if(response != null){
			try {
				
				base = new BDopenHelper(miCon).getWritableDatabase();
				if(response.getBoolean("insert")){

					base.execSQL("UPDATE coordenadas SET status=2 WHERE idTienda="+idTienda+" and fecha='"+fecha+"' and tipo='"+tipo+"';");
					Toast.makeText(miCon, "Registro Recibido", Toast.LENGTH_SHORT).show();

					Log.d("Response Entrada", "2");

					
				}else{
					Log.d("Response Entrada", "1");
					//Toast.makeText(miCon, "No se Recibio", Toast.LENGTH_SHORT).show();
				}
			} catch (JSONException e) {
				e.printStackTrace();
			} finally {
                base.close();
            }
        }
	}
	
	

}
