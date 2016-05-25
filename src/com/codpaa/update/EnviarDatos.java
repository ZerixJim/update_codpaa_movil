package com.codpaa.update;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;


import org.json.JSONException;
import org.json.JSONObject;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import com.codpaa.listener.HttpResponse;
import com.codpaa.util.Utilities;
import com.loopj.android.http.*;

import com.codpaa.db.BDopenHelper;

import cz.msebera.android.httpclient.Header;


public class EnviarDatos {
	
	AsyncHttpClient cliente = new AsyncHttpClient();

	RequestParams rp = new RequestParams();
	SQLiteDatabase base;
	BDopenHelper DB;
	
	private Activity activity;

	AsyncHttpResponseHandler respuesta = new AsyncHttpResponseHandler(){

		@Override
		public void onFailure(int arg0, Header[] arg1, byte[] arg2,Throwable arg3) {
			
			
		}

		@Override
		public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
			
		}
	};
	Locale local;
	
	
	
	public EnviarDatos(Activity a) {
		activity = a;
		DB = new BDopenHelper(activity);
		local = new Locale("es_MX");
		
	}
	
	
	
	private String getPhoneNumber(){
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(activity, new String[] {Manifest.permission.READ_PHONE_STATE}, 125);
        }
		TelephonyManager mTelephonyManager;
		mTelephonyManager = (TelephonyManager) activity.getSystemService(Context.TELEPHONY_SERVICE); 
		return mTelephonyManager.getLine1Number();
	}
	
	
	
	public void enviarVisitas() {
		try {
			
			RequestParams rpVisitas = new RequestParams();
			Cursor curVisitas = DB.datosVisitas();
		
			
			if(curVisitas.getCount() != 0 && verificarConexion()) {
				
				
				for(curVisitas.moveToFirst(); !curVisitas.isAfterLast(); curVisitas.moveToNext()) {
					rpVisitas.put("idTien", Integer.toString(curVisitas.getInt(0)));
					rpVisitas.put("idCel", Integer.toString(curVisitas.getInt(1)));
					rpVisitas.put("fecha", curVisitas.getString(2));
					rpVisitas.put("hora", curVisitas.getString(3));
					rpVisitas.put("lati", Double.toString(curVisitas.getDouble(4)));
					rpVisitas.put("lon", Double.toString(curVisitas.getDouble(5)));
					rpVisitas.put("tipo", curVisitas.getString(6));
					rpVisitas.put("numerocel", getPhoneNumber());


					cliente.get(Utilities.WEB_SERVICE_CODPAA+"sendvisitasnew.php",rpVisitas,
							new HttpResponse(activity, curVisitas.getInt(0), curVisitas.getString(2), curVisitas.getString(6)));
					
				
				}
				
				
			}

			DB.close();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void vistasPendientes(){
		try {
			
			
			if(verificarConexion()){
				Calendar c = Calendar.getInstance();
				SimpleDateFormat dFecha = new SimpleDateFormat("w",local);
					
				String semana = dFecha.format(c.getTime());
				int sem = Integer.parseInt(semana);
				Cursor curVisitas = DB.visitaPendiente(sem);
				
				base = new BDopenHelper(activity).getWritableDatabase();
				AsyncHttpClient clienteVP = new AsyncHttpClient();
				RequestParams rpVP = new RequestParams();
				if(curVisitas.getCount() != 0) {
					
					Toast.makeText(activity, "Se van a enviar \n("+curVisitas.getCount()+") Registros", Toast.LENGTH_SHORT).show();
					for(curVisitas.moveToFirst(); !curVisitas.isAfterLast(); curVisitas.moveToNext()) {
						
						rpVP.put("idTien", Integer.toString(curVisitas.getInt(0)));
						rpVP.put("idCel", Integer.toString(curVisitas.getInt(1)));
						rpVP.put("fecha", curVisitas.getString(2));
						rpVP.put("hora", curVisitas.getString(3));
						rpVP.put("lati", Double.toString(curVisitas.getDouble(4)));
						rpVP.put("lon", Double.toString(curVisitas.getDouble(5)));
						rpVP.put("tipo", curVisitas.getString(6));
						
						
						
						clienteVP.post(Utilities.WEB_SERVICE_CODPAA+"sentInfo.php", rpVP,respuesta);
						
					
					}
					
					
				}else{
					Toast.makeText(activity, "No hay Registros", Toast.LENGTH_SHORT).show();
				}
			}else{
				Toast.makeText(activity, "No hay conexion a internet", Toast.LENGTH_SHORT).show();
			}
			
			base.close();
			DB.close();
		}catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public void enviarCajasMay(){
		try {
			Cursor curCajasM = DB.datosCajasMay();
			base = new BDopenHelper(activity).getWritableDatabase();
			
			RequestParams rpC = new RequestParams();
			
			if(verificarConexion()){
				if(curCajasM.getCount() != 0){
					for(curCajasM.moveToFirst(); !curCajasM.isAfterLast(); curCajasM.moveToNext()){
						
						rpC.put("idCel", Integer.toString(curCajasM.getInt(0)));
						rpC.put("idMarca", Integer.toString(curCajasM.getInt(1)));
						rpC.put("fecha", curCajasM.getString(2));
						rpC.put("cajas", Integer.toString(curCajasM.getInt(3)));
						
						cliente.post(Utilities.WEB_SERVICE_CODPAA+"sendcajasmay.php", rpC, new HttpResponseCaMa(activity,curCajasM.getInt(1),curCajasM.getString(2)));
					}
					
				}else{
					Log.d("Enviar Caj May", "no hay registros");
				}
				
			}else{
				Log.d("Enviar Caj May", "no hay conexion");
			}
			
			base.close();
			DB.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private class HttpResponseCaMa extends JsonHttpResponseHandler{
		private int idMarca;
		private String fecha;
	
		SQLiteDatabase base;
		Activity act;
		
		public HttpResponseCaMa(Activity activity,int idMar, String fecha){
			
			this.idMarca = idMar;
			this.fecha = fecha;
			this.act = activity;
		}



		@Override
		public void onSuccess(int statusCode,Header[] headers ,JSONObject response) {
			if(response != null){
				try {
					
					base = new BDopenHelper(act).getWritableDatabase();
					if(response.getBoolean("insert")){
						base.execSQL("Update cajasMayoreo set status=2 where idMarca="+idMarca+" and fecha='"+fecha+"' ");
						Toast.makeText(act, "Registro Recibido", Toast.LENGTH_SHORT).show();
						
						base.close();
						
					}else{
						
						Toast.makeText(act, "No se Recibio", Toast.LENGTH_SHORT).show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			
			
		}
		
	}
	
	public void enviarFrentes() {
		try {
			
			
			Cursor curFrentes = DB.datosFrentes();

			if(curFrentes.getCount() != 0 && verificarConexion()) {
				
				for(curFrentes.moveToFirst(); !curFrentes.isAfterLast(); curFrentes.moveToNext()) {
					rp.put("idTien", Integer.toString(curFrentes.getInt(0)));
					rp.put("idCel", Integer.toString(curFrentes.getInt(1)));
					rp.put("fecha", curFrentes.getString(2));
					rp.put("idMarc", Integer.toString(curFrentes.getInt(3)));
					rp.put("idProdu",Integer.toString(curFrentes.getInt(4)));
					rp.put("cha1", Integer.toString(curFrentes.getInt(5)));
					rp.put("cha2", Integer.toString(curFrentes.getInt(6)));
					rp.put("cha3", Integer.toString(curFrentes.getInt(7)));
					rp.put("cha4", Integer.toString(curFrentes.getInt(8)));
					rp.put("cha5", Integer.toString(curFrentes.getInt(9)));
					rp.put("cha6", Integer.toString(curFrentes.getInt(10)));
                    rp.put("unifila", Integer.toString(curFrentes.getInt(11)));
                    rp.put("f1", Integer.toString(curFrentes.getInt(12)));
                    rp.put("f2", Integer.toString(curFrentes.getInt(13)));
                    rp.put("f3", Integer.toString(curFrentes.getInt(14)));
                    rp.put("f4", Integer.toString(curFrentes.getInt(15)));
                    rp.put("f5", Integer.toString(curFrentes.getInt(16)));
                    rp.put("f6", Integer.toString(curFrentes.getInt(17)));
                    rp.put("f7", Integer.toString(curFrentes.getInt(18)));
                    rp.put("f8", Integer.toString(curFrentes.getInt(19)));
                    rp.put("f9", Integer.toString(curFrentes.getInt(20)));
                    rp.put("f10", Integer.toString(curFrentes.getInt(21)));
                    rp.put("f11", Integer.toString(curFrentes.getInt(22)));
                    rp.put("f12", Integer.toString(curFrentes.getInt(23)));
                    rp.put("f13", Integer.toString(curFrentes.getInt(24)));
                    rp.put("f14", Integer.toString(curFrentes.getInt(25)));


					
					
					cliente.post(Utilities.WEB_SERVICE_CODPAA+"sendfront.php", rp,
							new HttpResponseFrentes(activity, curFrentes.getInt(0),curFrentes.getString(2), curFrentes.getInt(3)));
					

				}
				
				
			}
			

			DB.close();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	
	private class HttpResponseFrentes extends JsonHttpResponseHandler{
		
		private int idTienda;
		private String fecha;
		private int idMarca;
		
		SQLiteDatabase base;
		Activity act;
		
		public HttpResponseFrentes(Activity activi,int idTi,String fecha, int idMar){
			this.act = activi;
			this.idTienda = idTi;
			this.fecha = fecha;
			this.idMarca= idMar;
		}

		@Override
		public void onSuccess(int statusCode,Header[] headers ,JSONObject response) {
			if(response != null){
				try {
					
					base = new BDopenHelper(act).getWritableDatabase();
					if(response.getBoolean("insert")){
						base.execSQL("Update frentesCharola set status=2 where idTienda="+idTienda+" and fecha='"+fecha+"' and idMarca="+idMarca);
						Toast.makeText(act, "Registro Recibido", Toast.LENGTH_SHORT).show();
						
						base.close();
						
					}else{
						
						Toast.makeText(act, "No se Recibio", Toast.LENGTH_SHORT).show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}
		
		
	}


	public void enviarAddress(){

        try {

            Cursor cursor = DB.addresses();
            if (cursor.getCount() != 0){
                RequestParams requestParams = new RequestParams();
                AsyncHttpClient client = new AsyncHttpClient();
                for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()){
                    requestParams.put("idTienda", Integer.toString(cursor.getInt(cursor.getColumnIndex("idTienda"))));
                    requestParams.put("idPromotor", Integer.toString(cursor.getInt(cursor.getColumnIndex("idPromotor"))));
                    requestParams.put("direccion", cursor.getString(cursor.getColumnIndex("direccion")));

                    HttpResponseAddress httpResponseAddress =
                            new HttpResponseAddress(activity,cursor.getInt(cursor.getColumnIndex("idTienda")),
                                    cursor.getInt(cursor.getColumnIndex("idPromotor")),cursor.getString(cursor.getColumnIndex("direccion")));

                    client.post(activity, Utilities.WEB_SERVICE_CODPAA + "sendAddress.php", requestParams, httpResponseAddress );

                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }

	}

    private class HttpResponseAddress extends JsonHttpResponseHandler{

		Context context;
		int idTienda, idPromotor;
		String direcicon;
		SQLiteDatabase db;


		public HttpResponseAddress(Context context, int idTienda, int idPromotor, String direccion){
			this.context = context;
			this.idTienda = idTienda;
			this.idPromotor = idPromotor;
			this.direcicon = direccion;
		}

        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            if (response != null){
                try {
                    if (response.getBoolean("insert")){

						db = new BDopenHelper(context).getWritableDatabase();

						db.execSQL("delete from direcciones where idTienda="+idTienda+" and idPromotor="+idPromotor+" and direccion='"+direcicon+"';");

                        Toast.makeText(context, "Recibido", Toast.LENGTH_SHORT).show();
                        db.close();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
            super.onFailure(statusCode, headers, throwable, errorResponse);
        }
    }

	
	public void enviarSurtido() {
		try {
			Cursor curSurtido = DB.Surtido();

			if(curSurtido.getCount() != 0 && verificarConexion()) {
				
				for(curSurtido.moveToFirst(); !curSurtido.isAfterLast(); curSurtido.moveToNext()) {
					rp.put("idTien", Integer.toString(curSurtido.getInt(0)));
					rp.put("idCel", Integer.toString(curSurtido.getInt(1)));
					rp.put("surtido", curSurtido.getString(2));
					rp.put("fecha", curSurtido.getString(3));
					rp.put("idProducto", Integer.toString(curSurtido.getInt(4)));
					rp.put("cajas",Integer.toString(curSurtido.getInt(5)));

                    rp.put("unifila", Integer.toString(curSurtido.getInt(6)));
                    rp.put("c1", Integer.toString(curSurtido.getInt(7)));
                    rp.put("c2",Integer.toString(curSurtido.getInt(8)));
                    rp.put("c3",Integer.toString(curSurtido.getInt(9)));

                    rp.put("c4",Integer.toString(curSurtido.getInt(10)));
                    rp.put("c5",Integer.toString(curSurtido.getInt(11)));
                    rp.put("c6",Integer.toString(curSurtido.getInt(12)));
                    rp.put("c7",Integer.toString(curSurtido.getInt(13)));

                    rp.put("c8",Integer.toString(curSurtido.getInt(14)));
                    rp.put("c9",Integer.toString(curSurtido.getInt(15)));
                    rp.put("c10",Integer.toString(curSurtido.getInt(16)));
                    rp.put("c11", Integer.toString(curSurtido.getInt(17)));

                    rp.put("c12", Integer.toString(curSurtido.getInt(18)));
                    rp.put("c13", Integer.toString(curSurtido.getInt(19)));
                    rp.put("c14", Integer.toString(curSurtido.getInt(20)));


					
					
					HttpResponseSurtido http = new HttpResponseSurtido(activity, curSurtido.getInt(0),curSurtido.getString(3),curSurtido.getInt(4));
					cliente.post(Utilities.WEB_SERVICE_CODPAA+"surti.php", rp, http);
					//base.execSQL();
					
					
					
				}
				
				
			}

			DB.close();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}



    private class HttpResponseSurtido extends JsonHttpResponseHandler{

        private int idTienda, idProducto;
        private String fecha;
        private Context context;
        private SQLiteDatabase db;

        public HttpResponseSurtido(Context context, int idTienda, String fecha, int idProducto){
            this.context = context;
            this.fecha = fecha;
            this.idProducto = idProducto;
            this.idTienda = idTienda;

        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

            if (response != null){
                try {
                    if (response.getBoolean("insert")){
                        db = new BDopenHelper(context).getWritableDatabase();
                        db.execSQL("delete from surtido where idTienda="+idTienda+" and fecha='"+fecha+"' and idProducto="+idProducto+";");
                        Log.d("ResponseSurtido", "eliminado");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
            super.onFailure(statusCode, headers, throwable, errorResponse);
        }
    }
	
	public void enviarInventario() {
		try {
			
			Cursor curInven = DB.Inventario();

			if(curInven.getCount() > 0 && verificarConexion()) {
				
				for(curInven.moveToFirst(); !curInven.isAfterLast(); curInven.moveToNext()) {
					rp.put("idTien", Integer.toString(curInven.getInt(0)));
                    rp.put("idPromo",Integer.toString(curInven.getInt(1)));
					rp.put("fecha", curInven.getString(2));
					rp.put("idProducto", Integer.toString(curInven.getInt(3)));
					rp.put("cantidadFisico",Integer.toString(curInven.getInt(4)));
                    rp.put("cantidadSistema",Integer.toString(curInven.getInt(5)));
                    rp.put("cantidad",Integer.toString(curInven.getInt(6)));
                    rp.put("tipo",curInven.getString(7));
					rp.put("fecha_cad",curInven.getString(8));
					rp.put("lote",curInven.getString(9));
					rp.put("estatus", curInven.getInt(curInven.getColumnIndex("estatus")));

					//Log.d("Estatus"," "+ curInven.getInt(curInven.getColumnIndex("estatus")));
					
					
					cliente.post(Utilities.WEB_SERVICE_CODPAA+"sendinventario.php", rp, new HttpResponseInventario(activity, curInven.getInt(0),curInven.getString(2), curInven.getInt(3)));
					

				}
				
			}

			DB.close();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private class HttpResponseInventario extends JsonHttpResponseHandler{
		
		private int idTienda;
		private String fecha;
		private int idPro;
		
		SQLiteDatabase base;
		Activity act;
		
		public HttpResponseInventario(Activity activi,int idTi,String fecha, int idProd){
			this.act = activi;
			this.idTienda = idTi;
			this.fecha = fecha;
			this.idPro= idProd;
		}

		@Override
		public void onSuccess(int statusCode,Header[] headers ,JSONObject response) {
			if(response != null){
				try {
					
					base = new BDopenHelper(act).getWritableDatabase();
					if(response.getBoolean("insert")){
						base.execSQL("Update invProducto set status=2 where idTienda="+idTienda+" and fecha='"+fecha+"' and idProducto="+idPro);
						Toast.makeText(act, "Registro Recibido", Toast.LENGTH_SHORT).show();
						
						base.close();
						
					}else{
						
						Toast.makeText(act, "No se Recibio", Toast.LENGTH_SHORT).show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}
		
		
	}
	
	
	public void enviarExibiciones() {
		try {
			
			Cursor curExhi = DB.Exhibiciones();

			if(curExhi.getCount() > 0 && verificarConexion()) {
				
				for(curExhi.moveToFirst(); !curExhi.isAfterLast(); curExhi.moveToNext()) {
					rp.put("idTien", Integer.toString(curExhi.getInt(0)));
					rp.put("idCel", Integer.toString(curExhi.getInt(1)));
					rp.put("idExhibicion", Integer.toString(curExhi.getInt(2)));
					rp.put("fecha", curExhi.getString(3));
					rp.put("idProducto", Integer.toString(curExhi.getInt(4)));
					rp.put("cantidad",String.valueOf(curExhi.getFloat(5)));
					
					
					
					cliente.post(Utilities.WEB_SERVICE_CODPAA+"sendexhi.php", rp, new HttpResponseExhi(activity, curExhi.getInt(0), curExhi.getString(3), curExhi.getInt(4), curExhi.getInt(2)));

				}
				
				
			}

			DB.close();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private class HttpResponseExhi extends JsonHttpResponseHandler{
		
		private int idTienda;
		private String fecha;
		private int idPro;
		private int idExhi;
		
		SQLiteDatabase base;
		Activity act;
		
		public HttpResponseExhi(Activity activi,int idTi,String fecha, int idProd, int idExh){
			this.act = activi;
			this.idTienda = idTi;
			this.fecha = fecha;
			this.idPro= idProd;
			this.idExhi = idExh;
		}

		@Override
		public void onSuccess(int statusCode,Header[] headers ,JSONObject response) {
			if(response != null){
				try {
					
					base = new BDopenHelper(act).getWritableDatabase();
					if(response.getBoolean("insert")){
						base.execSQL("Update exhibiciones set status=2 where idTienda="+idTienda+" and idExhibicion="+idExhi+" and fecha='"+fecha+"' and idProducto="+idPro);
						Toast.makeText(act, "Registro Recibido", Toast.LENGTH_SHORT).show();
						
						base.close();
						
					}else{
						
						Toast.makeText(act, "No se Recibio", Toast.LENGTH_SHORT).show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}
		
		
	}
	
	public void enviarEncargado(){
		
		try{
			Cursor curEncargado = DB.encargadoTienda();
			base = new BDopenHelper(activity).getWritableDatabase();
			
			if(curEncargado.getCount() > 0 && verificarConexion()) {
				for(curEncargado.moveToFirst(); !curEncargado.isAfterLast(); curEncargado.moveToNext()) {
					rp.put("idTien", Integer.toString(curEncargado.getInt(0)));
					rp.put("idCel", Integer.toString(curEncargado.getInt(1)));
					rp.put("nombre",curEncargado.getString(2));
					rp.put("puesto",curEncargado.getString(3));
					rp.put("fecha", curEncargado.getString(4));
					
					cliente.post(Utilities.WEB_SERVICE_CODPAA+"sendEncargado.php", rp, respuesta);
					base.delete("encargadotienda", "idTienda="+curEncargado.getInt(0)+" and fecha='"+curEncargado.getString(4)+"'", null);
				}
				
			}
			
			base.close();
			DB.close();
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		
	}
	
	
	public void enviarComentario(){
		
		try{
			Cursor curComentario = DB.ComentariosTienda();
			base = new BDopenHelper(activity).getWritableDatabase();
			
			if(curComentario.getCount() > 0 && verificarConexion()){
				for(curComentario.moveToFirst(); !curComentario.isAfterLast(); curComentario.moveToNext()){
					rp.put("idTien", Integer.toString(curComentario.getInt(0)));
					rp.put("idCel", Integer.toString(curComentario.getInt(1)));
					rp.put("fecha", curComentario.getString(2));
					rp.put("comentario", curComentario.getString(3));
					
					
					cliente.post(Utilities.WEB_SERVICE_CODPAA+"sendComentario.php", rp, respuesta);
					base.delete("comentarioTienda", "idTienda="+curComentario.getInt(0)+" and fecha='"+curComentario.getString(2)+"'", null);
				}
				
			}
			base.close();
			DB.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	public void enviarInteli(){
		try {
			Cursor curInteli = DB.datosInteligenciaMercado();

			RequestParams rpIn = new RequestParams();
			if(curInteli.getCount() > 0 && verificarConexion()){
				for(curInteli.moveToFirst(); !curInteli.isAfterLast(); curInteli.moveToNext()){
					
					rpIn.put("idCel", Integer.toString(curInteli.getInt(0)));
					rpIn.put("idTien", Integer.toString(curInteli.getInt(1)));
					rpIn.put("idProducto", Integer.toString(curInteli.getInt(2)));
					rpIn.put("precioNormal", curInteli.getString(3));
					rpIn.put("precioOferta", curInteli.getString(4));
					rpIn.put("fecha", curInteli.getString(5));
					rpIn.put("ofertaCruz", curInteli.getString(6));
					rpIn.put("productoExtra", curInteli.getString(7));
					rpIn.put("productoEmpla", curInteli.getString(8));
					rpIn.put("cambioIm", curInteli.getString(9));
					rpIn.put("iniofer",curInteli.getString(10));
					rpIn.put("finofer",curInteli.getString(11));
					rpIn.put("preciocaja",curInteli.getString(12));
					rpIn.put("cambioprecio",curInteli.getString(13));
					
					
					cliente.post(Utilities.WEB_SERVICE_CODPAA+"sendinteligencia2.php", rpIn, new HttpResponseInteligen(activity, curInteli.getInt(1), curInteli.getString(5),curInteli.getInt(2)));
					
				}
			}
			

			DB.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private class HttpResponseInteligen extends JsonHttpResponseHandler{
		
		private int idTienda;
		private String fecha;
		private int idPro;
		
		
		SQLiteDatabase base;
		Activity act;
		
		public HttpResponseInteligen(Activity activi,int idTi,String fecha, int idProd){
			this.act = activi;
			this.idTienda = idTi;
			this.fecha = fecha;
			this.idPro= idProd;
			
		}

		@Override
		public void onSuccess(int statusCode,Header[] headers ,JSONObject response) {
			if(response != null){
				try {
					
					base = new BDopenHelper(act).getWritableDatabase();
					if(response.getBoolean("insert")){
						base.execSQL("Update inteligencia set status=2 where idTienda="+idTienda+"  and fecha='"+fecha+"' and idProducto="+idPro);
						Toast.makeText(act, "Registro Recibido", Toast.LENGTH_SHORT).show();
						
						base.close();
						
					}else{
						
						Toast.makeText(act, "No se Recibio", Toast.LENGTH_SHORT).show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}
		
		
	}



	public void sendVentaPromedio(){

		try{
			Cursor curVenta = DB.datosVenta();
			base = new BDopenHelper(activity).getWritableDatabase();

			if(curVenta.getCount() > 0 && verificarConexion()){
				for(curVenta.moveToFirst(); !curVenta.isAfterLast(); curVenta.moveToNext()){
					rp.put("idMarca", Integer.toString(curVenta.getInt(curVenta.getColumnIndex("idMarca"))));
					rp.put("tipo", curVenta.getString(curVenta.getColumnIndex("tipo")));
					rp.put("cantidad", Float.toString(curVenta.getFloat(curVenta.getColumnIndex("cantidad"))));
					rp.put("fechai", curVenta.getString(curVenta.getColumnIndex("fecha_inicio")));
                    rp.put("fechaf", curVenta.getString(curVenta.getColumnIndex("fecha_fin")));
                    rp.put("idTienda", Integer.toString(curVenta.getInt(curVenta.getColumnIndex("idTienda"))));
                    rp.put("idPromotor", Integer.toString(curVenta.getInt(curVenta.getColumnIndex("idPromotor"))));

                    HttpVentaResponse response = new HttpVentaResponse(activity,
                            curVenta.getInt(curVenta.getColumnIndex("idMarca")),
                            curVenta.getString(curVenta.getColumnIndex("tipo")),
                            curVenta.getString(curVenta.getColumnIndex("fecha_inicio")),
                            curVenta.getString(curVenta.getColumnIndex("fecha_fin")));
					cliente.get(Utilities.WEB_SERVICE_CODPAA+"sendVentaPromedio.php", rp, response);
				}

			}
			base.close();
            curVenta.close();
			DB.close();
		}catch(Exception e){
			e.printStackTrace();
		}

	}


    private class HttpVentaResponse extends JsonHttpResponseHandler{


        private int idMarca;
        private String tipo, fechaI, fechaF;
        Context context;

        public HttpVentaResponse(Context context,int idMarca, String tipo, String fechaI, String fechaF){
            this.idMarca = idMarca;
            this.tipo = tipo;
            this.fechaI = fechaI;
            this.fechaF = fechaF;
            this.context = context;

        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

            Log.d("Status", " "+statusCode);

            if (response != null){

                try {
                    if (response.getBoolean("insert")){

                        SQLiteDatabase db = new BDopenHelper(activity).getWritableDatabase();

                        try {

                            db.execSQL("update ventaPromedio set estatus=2 where idMarca="+idMarca+" " +
                                    "and tipo='"+tipo+"' and fecha_inicio='"+fechaI+"' and fecha_fin='"+fechaF+"'");

                            Toast.makeText(activity, "Registro Recibido", Toast.LENGTH_SHORT).show();

                        }catch (SQLiteException e){
                            e.printStackTrace();
                        }


                    }




                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
            super.onFailure(statusCode, headers, throwable, errorResponse);
        }
    }
	
	public boolean verificarConexion() {
	    ConnectivityManager cm = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo netInfo = cm.getActiveNetworkInfo();

	    return netInfo != null && netInfo.isConnected();
	}
	

}
