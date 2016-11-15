package com.codpaa.service;

import com.codpaa.R;
import com.codpaa.model.JsonPhotoUpload;
import com.codpaa.util.Configuracion;
import com.codpaa.util.Utilities;
import com.google.gson.Gson;
import com.loopj.android.http.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONException;
import org.json.JSONObject;


import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.support.v4.app.NotificationCompat;
import android.telephony.TelephonyManager;
import android.util.Log;



import com.codpaa.db.BDopenHelper;

import cz.msebera.android.httpclient.Header;


public class GeoLocalizar extends Service implements LocationListener{
	
	LocationManager lm;
	Location loGps, loNet, loGeneral;
	Timer tiempoEspera = null, tiempoRastreo = null;
	SQLiteDatabase base = null;
	BDopenHelper DBhelper;
	AsyncHttpClient cliente;
	String myVersionName = "not available";
	String fecha, hora;
	RequestParams rp;
	int idCel;


	Intent resultIntent;
	PendingIntent pendingIntent;
	Context con;
	

	
	AsyncHttpResponseHandler respuesta = new AsyncHttpResponseHandler(){
		
		@Override
		public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {}

		@Override
		public void onFailure(int arg0, Header[] arg1, byte[] arg2,Throwable arg3) {}
	
	};
	
	JsonHttpResponseHandler resVisitas = new JsonHttpResponseHandler(){
		
		SQLiteDatabase baseEven=null;
		@Override
		public void onSuccess(int statusCode,Header[] headers,JSONObject response) {
			try {
				if(response.getBoolean("insert")){
					
					baseEven = new BDopenHelper(con).getWritableDatabase();

					baseEven.execSQL("UPDATE coordenadas SET status=2 WHERE idTienda="+
							response.getInt("idTienda")+" and fecha='"+
							response.getString("fecha")+"' and tipo='"+
							response.getString("tipo")+"';");
					if (baseEven != null)
						baseEven.close();

				}else{
					Log.d("EnvioRegistroThread", response.getString("mensaje"));
				}
			} catch (JSONException e) {
				
				e.printStackTrace();
			}
		}

		@Override
		public void onFailure(int statusCode, Header[] headers,
				String responseString, Throwable throwable) {
			
			super.onFailure(statusCode, headers, responseString, throwable);
			
			Log.d("EnvioRegistroThread", "onFailure :(");
			throwable.printStackTrace();
			
		}
		
		
	};

    JsonHttpResponseHandler respuestaFrentes = new JsonHttpResponseHandler(){

        SQLiteDatabase bdF=null;
        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

            if(response != null) {
                try {
                    Log.d("ResponseFrentes",response.toString());
                    if (response.getBoolean("insert")) {
                        bdF = new BDopenHelper(con).getWritableDatabase();
                        bdF.execSQL("Update frentesCharola set status=2 where idTienda=" +
								response.getInt("idTienda") + " and fecha='" +
								response.getString("fecha") + "' and idMarca=" +
								response.getInt("idMarca") + " and idProducto=" +
								response.getInt("idProducto"));
                        Log.d("ResponseFrentes", "insertado :)");

                        bdF.close();

                    } else {
                        Log.d("ResponseFrentes", "fallo :(");
                    }

                } catch (JSONException e) {
                    Log.d("ResponseFrentes", "Error");
                    e.printStackTrace();
                }
            }

        }

        @Override
        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
            super.onFailure(statusCode, headers, throwable, errorResponse);
            Log.d("ResponseFrentes","Se perdio la coneccion :(");

        }
    };
	
    JsonHttpResponseHandler respuestaInteligencia = new JsonHttpResponseHandler(){
        SQLiteDatabase bdInt;
        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

            if(response != null) {
                try {
                    Log.d("ResponseInteligencia",response.toString());
                    if (response.getBoolean("insert")) {
                        bdInt = new BDopenHelper(con).getWritableDatabase();
                        bdInt.execSQL("Update inteligencia set status=2 where idTienda="+
								response.getInt("idTienda")+"  and fecha='"+
								response.getString("fecha")+"' and idProducto="+
								response.getInt("idProducto"));


                        bdInt.close();
                    } else {
                        Log.d("ResponseInteligencia", "fallo :(");
                    }

                } catch (JSONException e) {
                    Log.d("ResponseInteligencia", "Error");
                    e.printStackTrace();
                }
            }


        }

        @Override
        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
            super.onFailure(statusCode, headers, throwable, errorResponse);

            Log.d("ResponseInteligencia", "Se perdio la conexion :(");
        }
    };

    JsonHttpResponseHandler responseInventario = new JsonHttpResponseHandler(){
        SQLiteDatabase bdIn;
        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            if(response != null) {
                try {
                    Log.d("ResponseInventario",response.toString());
                    if (response.getBoolean("insert")) {
                        bdIn = new BDopenHelper(con).getWritableDatabase();
                        bdIn.execSQL("UPDATE invProducto SET status=2 where idTienda="+
								response.getInt("idTienda")+" and fecha='"+
								response.getString("fecha")+"' and idProducto="+
								response.getInt("idProducto")+";");


                        bdIn.close();
                    } else {
                        Log.d("ResponseInventario", "fallo :(");
                    }

                } catch (JSONException e) {
                    Log.d("ResponseInventario", "Error");
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
            super.onFailure(statusCode, headers, throwable, errorResponse);
            Log.d("ResponseInventario", "se perdio la conexion");
        }
    };


    JsonHttpResponseHandler respuestaExhibiciones = new JsonHttpResponseHandler(){
        SQLiteDatabase bdInt;
        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

            if(response != null) {
                try {
                    Log.d("ResponseInteligencia",response.toString());
                    if (response.getBoolean("insert")) {
                        bdInt = new BDopenHelper(con).getWritableDatabase();
                        bdInt.execSQL("Update exhibiciones set status=2 where idTienda="+
								response.getInt("idTienda")+" and idExhibicion="+
								response.getInt("idExhi")+" and fecha='"+
								response.getString("fecha")+"' and idProducto="+
								response.getInt("idProducto"));

                        bdInt.close();
                    } else {
                        Log.d("ResponseInteligencia", "fallo :(");
                    }

                } catch (JSONException e) {
                    Log.d("ResponseInteligencia", "Error");
                    e.printStackTrace();
                }
            }


        }

        @Override
        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
            super.onFailure(statusCode, headers, throwable, errorResponse);

            Log.d("ResponseInteligencia", "Se perdio la conexion :(");
        }
    };



	
	private final Handler handler = new Handler();

	Runnable start = new Runnable( ) {
	    public void run() {
			Log.i("Geo", "start");
	        startGPS();

			try {
				loNet = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
				loGps = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
			}catch(SecurityException e){
				e.printStackTrace();
			}
	        


	        handler.postDelayed(stop, (60 * 1000) * 2);
	    }
	};

	Runnable stop = new Runnable( ) {
	    public void run( ) {
			Log.i("Geo", "stop");
	        stopGPS();
	    }
	};

	Runnable onePeriod = new Runnable( ) {
	    public void run( ) {
			Log.i("Geo", "onePeriod");
			//
	        handler.postDelayed(onePeriod, (60 * 1000) * 5);

	        handler.post(start);
	    }
	};

	public GeoLocalizar(){}

	public void startGPS(){
		lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

		try {
			lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 0, this);
			lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 10000, 0, this);
		}catch (SecurityException e){
			e.printStackTrace();
		}

	}
	

	public void startContiniuosListening( ) {
	    handler.post(onePeriod);
	}

	public void stopContiniousListening( ) {
	    handler.removeCallbacks(stop);
	    handler.removeCallbacks(onePeriod);
	    stopGPS();
	}

    
    public void stopGPS(){
        try {
            lm.removeUpdates(this);
        }catch (SecurityException e){
            e.printStackTrace();
        }
    }
    


	@Override
	public void onCreate() {
		super.onCreate();
		
		DBhelper = new BDopenHelper(this);
		cliente = new AsyncHttpClient();
		rp = new RequestParams();


		con = this;

		startContiniuosListening();
		

		tiempoEspera = new Timer();
		tiempoRastreo = new Timer();
		
		
		tiempoEspera.scheduleAtFixedRate(new TimerTask() {
			
			@Override
			public void run() {
				
				Log.d("Timer", "Entro");
				iniciarTarea();
				
				
			}
		}, 0, (1000 * 60)* 20);
		
		tiempoRastreo.scheduleAtFixedRate(new TimerTask() {

            @Override
            public void run() {
                Log.d("TimerRastreo", "inicio");
                rastreo();

            }
        }, 0, (1000 * 60) * 10);



		resultIntent = new Intent(this, GeoLocalizar.class);
		pendingIntent = PendingIntent.getActivity(this,0, resultIntent,PendingIntent.FLAG_UPDATE_CURRENT);

	}


	
	
	
	public class MyBinder extends Binder{



		public GeoLocalizar getService(){
	    	return GeoLocalizar.this;
	    }
	    
	  
	}

	@Override
	public IBinder onBind(Intent arg0) {
		return new MyBinder();
	}
	
	


	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		super.onStartCommand(intent, flags, startId);
		return START_STICKY;
	}


	/**
	 * metodo para inicir el hilo de envío de informacion en segundo plano
	 */


	public void iniciarTarea(){
		
		Thread hiloP = new Thread(){
			
			@Override
			public void run(){
				Looper.prepare();
				try {
					
					enviarVisitas();
                    verifyVersionSent();
					enviarFrentes();
					//enviarSurtido();
					enviarInteli();
					enviarInventario();
					enviarExibiciones();
					enviarEncargado();
					enviarComentario();
					enviarRastreo();

                    enviarFotos();
					
					
					Looper.loop();
				}catch(Exception e){
					
					
					Log.e("Excepption", "Tipo", e);
				}
				
			}

			
		};
		
		hiloP.start();
		
	
	}


    private String fechaActual(){
        Calendar c = Calendar.getInstance();
        Locale locale = new Locale("es_MX");
        SimpleDateFormat dFecha = new SimpleDateFormat("dd-MM-yyyy", locale);
        return dFecha.format(c.getTime());
    }

    /*
        this method verify if the codpaa´s version was send
     */
    private void verifyVersionSent(){
        Configuracion config = new Configuracion(con);
        if (config.getVersion() != null){

            if (!config.getVersion().equals(fechaActual())){
                enviarVersion();
            }else {
                Log.d("Geo_service", "version enviada");
            }

        }else {
            enviarVersion();
        }
    }



	

	protected void enviarVersion() {
		try {
			AsyncHttpClient clienteVersio = new AsyncHttpClient();
			RequestParams rpV = new RequestParams();
			
			//Calendar c = Calendar.getInstance();
			//SimpleDateFormat dFecha = new SimpleDateFormat("dd-MM-yyyy",local).format(new Date());
				
			String fecha = new SimpleDateFormat("dd-MM-yyyy",Locale.getDefault()).format(new Date());
			
			final Context context = getApplicationContext();
			PackageManager packageManager = context.getPackageManager();
			String packageName = context.getPackageName();
			
			try {
			    myVersionName = packageManager.getPackageInfo(packageName, 0).versionName;
			    try{
					idCel = DBhelper.idUser();
					
					if(idCel > 0){
						
						rpV.put("id", Integer.toString(idCel));
						rpV.put("ve", myVersionName);
						rpV.put("fecha", fecha);
						
						clienteVersio.post(Utilities.WEB_SERVICE_CODPAA + "sendVersion.php", rpV, new AsyncHttpResponseHandler(Looper.getMainLooper()) {
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                                Log.d("ResponseVesion", "Success");

                                if (statusCode == 200){

                                    Configuracion config = new Configuracion(con);
                                    config.setVersionDate(fechaActual());

                                    Log.d("ResponseVersion", "200");
                                }
                            }

                            @Override
                            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                            }
                        });

					}else{
						Log.d("Env Version","id NO Asignado");
					}
					
				}catch(Exception e){
					e.printStackTrace();
				}
				
			    
			} catch (PackageManager.NameNotFoundException e) {
			    e.printStackTrace();
			}
			
	
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	public void rastreo(){
		Thread hiloRastreo = new Thread() {
			
			@Override
			public void run() {

				insertarRastreo();

			}
		};
		
		hiloRastreo.start();
	}
	
	
	public void enviarVisitas() {
		try {
			
			
			Cursor curVisitas = DBhelper.datosVisitas();
			AsyncHttpClient cli = new AsyncHttpClient();
			RequestParams rpVis = new RequestParams();

			
			if(verificarConexion()){
				if(curVisitas.getCount() >= 1) {
					
					
					for(curVisitas.moveToFirst(); !curVisitas.isAfterLast(); curVisitas.moveToNext()) {
						rpVis.put("idTien", Integer.toString(curVisitas.getInt(0)));
						rpVis.put("idCel", Integer.toString(curVisitas.getInt(1)));
						rpVis.put("fecha", curVisitas.getString(2));
						rpVis.put("hora", curVisitas.getString(3));
						rpVis.put("lati", Double.toString(curVisitas.getDouble(4)));
						rpVis.put("lon", Double.toString(curVisitas.getDouble(5)));
						rpVis.put("tipo", curVisitas.getString(6));
						rpVis.put("numerocel", getPhoneNumber());
						
						cli.get(Utilities.WEB_SERVICE_CODPAA+"sendvisitasnewresponse.php",rpVis, resVisitas);
						

						
					}
					
				}else{
					Log.d("Visitas pendientes","no hay registros");
				}
				
			}else{
				Log.d("Visitas pendientes","no hay connexion");
			}
			
			
			curVisitas.close();
			DBhelper.close();

		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	


	public void enviarFrentes() {
		try {
			
			
			
			Cursor curFrentes = DBhelper.datosFrentes();
			base = new BDopenHelper(this).getWritableDatabase();
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


					cliente.post(Utilities.WEB_SERVICE_CODPAA+"sendfrentesnew.php", rp, respuestaFrentes);

				}
				
				
			}
			curFrentes.close();

			base.close();
			DBhelper.close();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/*public void enviarSurtido() {
		try {
			Cursor curSurtido = DBhelper.Surtido();
			base = new BDopenHelper(this).getWritableDatabase();
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


					//ResponseHttpSurtido response = new ResponseHttpSurtido()
					cliente.post(Utilities.WEB_SERVICE_CODPAA+"surti.php", rp, respuesta);

					
					
					
				}
				
				
			}
			curSurtido.close();
			base.close();
			DBhelper.close();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}*/
	
	public void enviarInventario() {
		try {
			
			Cursor curInven = DBhelper.Inventario();
			base = new BDopenHelper(this).getWritableDatabase();
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
					
					
					
					cliente.post(Utilities.WEB_SERVICE_CODPAA+"sendinventario.php", rp,responseInventario);

					
				}
				
				
			}
			curInven.close();
			base.close();
			DBhelper.close();
			
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public void enviarExibiciones() {
		try {
			
			Cursor curExhi = DBhelper.Exhibiciones();
			base = new BDopenHelper(this).getWritableDatabase();
			if(curExhi.getCount() > 0 && verificarConexion()) {
				
				for(curExhi.moveToFirst(); !curExhi.isAfterLast(); curExhi.moveToNext()) {
					rp.put("idTien", Integer.toString(curExhi.getInt(0)));
					rp.put("idCel", Integer.toString(curExhi.getInt(1)));
					rp.put("idExhibicion", Integer.toString(curExhi.getInt(2)));
					rp.put("fecha", curExhi.getString(3));
					rp.put("idProducto", Integer.toString(curExhi.getInt(4)));
					rp.put("cantidad",String.valueOf(curExhi.getFloat(5)));
					
					
					
					cliente.post(Utilities.WEB_SERVICE_CODPAA+"sendexhi.php", rp, respuestaExhibiciones);
					//base.execSQL("Update exhibiciones set status=2 where idTienda="+curExhi.getInt(0)+" and idExhibicion="+curExhi.getInt(2)+" and fecha='"+curExhi.getString(3)+"' and idProducto="+curExhi.getInt(4));
					
					
					
				}

				
				
			}
			curExhi.close();
			base.close();
			DBhelper.close();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void enviarEncargado(){
		
		try{
			Cursor curEncargado = DBhelper.encargadoTienda();
			base = new BDopenHelper(this).getWritableDatabase();
			
			if(curEncargado.getCount() > 0 && verificarConexion()) {
				for(curEncargado.moveToFirst(); !curEncargado.isAfterLast(); curEncargado.moveToNext()) {
					rp.put("idTien", Integer.toString(curEncargado.getInt(0)));
					rp.put("idCel", Integer.toString(curEncargado.getInt(1)));
					rp.put("nombre",curEncargado.getString(2));
					rp.put("puesto",curEncargado.getString(3));
					rp.put("fecha", curEncargado.getString(4));
					
					cliente.post(Utilities.WEB_SERVICE_CODPAA+"sendEncargado.php", rp, respuesta);
					base.delete("encargadotienda", "idTienda=" +
							curEncargado.getInt(0) + " and fecha='" +
							curEncargado.getString(4) + "'", null);
				}
				
			}
			curEncargado.close();
			base.close();
			DBhelper.close();
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		
	}
	
	
	public void enviarComentario(){
		
		try{
			Cursor curComentario = DBhelper.ComentariosTienda();
			base = new BDopenHelper(this).getWritableDatabase();
			
			if(curComentario.getCount() > 0 && verificarConexion()){
				for(curComentario.moveToFirst(); !curComentario.isAfterLast(); curComentario.moveToNext()){
					rp.put("idTien", Integer.toString(curComentario.getInt(0)));
					rp.put("idCel", Integer.toString(curComentario.getInt(1)));
					rp.put("fecha", curComentario.getString(2));
					rp.put("comentario", curComentario.getString(3));
					
					
					cliente.post(Utilities.WEB_SERVICE_CODPAA + "sendComentario.php", rp, respuesta);
					base.delete("comentarioTienda", "idTienda=" +
							curComentario.getInt(0) + " and fecha='" +
							curComentario.getString(2) + "'", null);
				}
				
			}
			curComentario.close();
			base.close();
			DBhelper.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	
	
	public void enviarRastreo(){
		try{
			
			
			Cursor curRastreo = DBhelper.datosRastreo();
			SQLiteDatabase bases = new BDopenHelper(this).getWritableDatabase();
			RequestParams rpRastreo = new RequestParams();
			
			
			
			if(curRastreo.getCount() > 0 && verificarConexion()){
				
				

				Log.d("Registros rastreo", Integer.toString(curRastreo.getCount()));
				for(curRastreo.moveToFirst(); !curRastreo.isAfterLast(); curRastreo.moveToNext()){
					rpRastreo.put("idCel", Integer.toString(curRastreo.getInt(0)));
					rpRastreo.put("fecha", curRastreo.getString(1));
					rpRastreo.put("hora", curRastreo.getString(2));
					rpRastreo.put("latitud", Double.toString(curRastreo.getDouble(3)));
					rpRastreo.put("longitud", Double.toString(curRastreo.getDouble(4)));
					rpRastreo.put("altitud", Double.toString(curRastreo.getDouble(5)));
					rpRastreo.put("numero_tel", curRastreo.getString(6));
							
							
					cliente.post(Utilities.WEB_SERVICE_CODPAA+"sendRastreo.php", rpRastreo, respuesta);
							
							
					bases.delete("rastreo", "idCelular="+curRastreo.getInt(0)+" and fecha='"+
							curRastreo.getString(1)+"'", null);
				}
						
			}else{
				Log.d("EnviarRastreo", Integer.toString(curRastreo.getCount()));
				if(verificarConexion()){
					Log.d("EnviarRastreo", "con conexion");
				}
			}
			
			bases.close();
			DBhelper.close();
			Log.d("TimerRastreo", "Termino");
		}catch(Exception e){
			Log.d("enviarRastreo", "Excep", e);
		}
		
		Log.d("enviarRastreo", "termino envio de rastreo");
		
	}
	
	public void insertarRastreo(){
		
		try{
			

			Calendar c = Calendar.getInstance();
			SimpleDateFormat dFecha = new SimpleDateFormat("dd-MM-yyyy",Locale.getDefault());
			SimpleDateFormat dHora = new SimpleDateFormat("HH:mm:ss a", Locale.getDefault());
			fecha = dFecha.format(c.getTime());
			hora = dHora.format(c.getTime());
			
			
			try{
				idCel = DBhelper.idUser();
				
			}catch(Exception e){
				e.printStackTrace();
			}
			
			
			
			if(idCel != 0){
				
				Log.d("IdCEl", Integer.toString(idCel));
				
				if(loGps != null){
					
					DBhelper.insertarRastreo(idCel, fecha, hora, loGps.getLatitude(),
							loGps.getLongitude(), loGps.getAltitude(),getPhoneNumber());
				}else if(loNet != null){
					DBhelper.insertarRastreo(idCel, fecha, hora, loNet.getLatitude(),
							loNet.getLongitude(), loNet.getAltitude(),getPhoneNumber());
				}else if(loGeneral != null){
					
					DBhelper.insertarRastreo(idCel, fecha, hora, loGeneral.getLatitude(),
							loGeneral.getLongitude(), loGeneral.getAltitude(), getPhoneNumber());

				}else{
					Log.d("Rastreo", "nullos");
				}
				

				
			}
			
			
			
			
		}catch(Exception e){
			Log.d("Insertar Rastreo", "no se inserto", e);
			
			
		}
		
		
	}
	
	public void enviarInteli(){
		try {
			Cursor curInteli = DBhelper.datosInteligenciaMercado();
			base = new BDopenHelper(this).getWritableDatabase();
			RequestParams rpIn = new RequestParams();
			if(curInteli.getCount() > 0 ){
				
				
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
					rpIn.put("cambioprecio", curInteli.getString(13));

                    cliente.post(Utilities.WEB_SERVICE_CODPAA + "sendinteligencia.php", rpIn, respuestaInteligencia);
					

					
				}
			}
			
			base.close();
			DBhelper.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

    public void enviarFotos(){

        BDopenHelper base = new BDopenHelper(this);


        Cursor curFoto = base.fotos();
		AsyncHttpClient cliente = new SyncHttpClient();

        Log.e("Geolocalizar","paso 1");

        if(curFoto.getCount() >= 1) {

            for(curFoto.moveToFirst(); !curFoto.isAfterLast(); curFoto.moveToNext()){


				Gson gson = new Gson();
				RequestParams rpFoto = new RequestParams();


				JsonPhotoUpload upload = new JsonPhotoUpload();

				upload.setIdTienda(curFoto.getInt(curFoto.getColumnIndex("idTienda")));
				upload.setIdPromotor(curFoto.getInt(curFoto.getColumnIndex("idCelular")));
				upload.setIdMarca(curFoto.getInt(curFoto.getColumnIndex("idMarca")));
				upload.setIdExhibicion(curFoto.getInt(curFoto.getColumnIndex("idExhibicion")));
				upload.setFecha(curFoto.getString(curFoto.getColumnIndex("fecha")));
				upload.setFechaCaptura(curFoto.getString(curFoto.getColumnIndex("fecha_captura")));
				upload.setDia(curFoto.getInt(curFoto.getColumnIndex("dia")));
				upload.setMes(curFoto.getInt(curFoto.getColumnIndex("mes")));
				upload.setAnio(curFoto.getInt(curFoto.getColumnIndex("anio")));
				upload.setEvento(curFoto.getInt(curFoto.getColumnIndex("evento")));


				if (curFoto.getString(curFoto.getColumnIndex("productos")) != null){
					Log.d("foto", "productos disponibles");
					String[] productos = curFoto.getString(curFoto.getColumnIndex("productos")).split(",");
					upload.convert(productos);
				}


				rpFoto.put("json", gson.toJson(upload));


                File file = new File(curFoto.getString(curFoto.getColumnIndex("imagen")));
                try {
                    rpFoto.put("file", file);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                cliente.setTimeout(5000);

				if (verificarConexion()){

					cliente.post(Utilities.WEB_SERVICE_CODPAA + "uploadimage2.php",rpFoto, jr);

				}


            }


        }
        base.close();


    }

    JsonHttpResponseHandler jr = new JsonHttpResponseHandler(){




        int id = 100;

        NotificationManager notification;
        NotificationCompat.Builder notificationBuilder;
        SQLiteDatabase db = null;



        @Override
        public void onStart() {
            super.onStart();

            Log.w("JR","onStart");

            notification = (NotificationManager) con.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationBuilder = new NotificationCompat.Builder(con);

            notificationBuilder.setContentTitle("CODPAA Enviando imagen")
                    .setContentText("subiendo la foto")
					.setContentIntent(pendingIntent)
                    .setSmallIcon(R.drawable.subirimagen);

            notification.notify(id,notificationBuilder.build());
            Log.e("ResponseImage","paso 2");

        }

        @Override
        public void onProgress(long bytesWritten, long totalSize) {
            super.onProgress(bytesWritten, totalSize);
            notificationBuilder.setProgress((int)totalSize,(int)bytesWritten,false).setContentIntent(pendingIntent);
            notification.notify(id,notificationBuilder.build());
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            super.onSuccess(statusCode, headers, response);

            if(response != null){
                try {

                    //Log.d("Respuestas Ima", response.getString("message"));
                    //BDopenHelper bs = new BDopenHelper(con);
                    if(response.getBoolean("insert")){
						Log.d("Geolocalizar", "insertado satisfactoriamente");

                        notificationBuilder.setContentText("se envio correctamente")
								.setContentIntent(pendingIntent)
                                .setProgress(0, 0, false);
                        notification.notify(id,notificationBuilder.build());
                        db = new BDopenHelper(con).getWritableDatabase();


                        db.execSQL("update photo set status=2 where idTienda="+
                                response.getInt("idTienda")+" and " +
                                "idMarca="+response.getInt("idMarca")+" and fecha='"+
                                response.getString("fecha")+"';");

						//borrar la imagen
                        //deleteArchivo(bs.fotoPath(response.getInt("idFotoCel")));

                    }else{
                        if(response.getInt("code") == 3){
                            //deleteArchivo(bs.fotoPath(response.getInt("idFotoCel")));

                            db.execSQL("update photo set status=2 where idTienda="+
                                    response.getInt("idTienda")+" and " +
                                    "idMarca="+response.getInt("idMarca")+" and fecha='"+
                                    response.getString("fecha")+"';");
                        }
						Log.d("Respuestas Ima", response.getString("message"));


                    }
                } catch (JSONException e) {

                    e.printStackTrace();
                } finally {

					if (db != null)
                    	db.close();
                }

            }else{
                Log.d("Response image", "Sin respuesta");
            }
        }



        @Override
        public void onFailure(int statusCode, Header[] headers, Throwable e, JSONObject errorResponse) {
            super.onFailure(statusCode, headers, e, errorResponse);
            notificationBuilder.setContentText("fallo, el envio")
					.setContentIntent(pendingIntent)
                    .setProgress(0, 0, false);
            notification.notify(id,notificationBuilder.build());
        }

        @Override
        public void onFinish() {
            super.onFinish();
        }


    };


	public boolean verificarConexion() {

	    ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo netInfo = cm.getActiveNetworkInfo();

	    return netInfo != null && netInfo.isConnected();
	}
	
	

	@Override
	public void onLocationChanged(Location location) {
		
		
		if(location != null){
			
			loGeneral = location;
			//Log.d("Cambio Localizacion", "lat: "+location.getLatitude()+" lon: "+location.getLongitude());
			
		}
		
		
	}

	
	


	@Override
	public void onDestroy() {
		super.onDestroy();

		stopContiniousListening();

        try {
            lm.removeUpdates(this);
        }catch (SecurityException e){
            e.printStackTrace();
        }
	}




	@Override
	public void onProviderDisabled(String provider) {}




	@Override
	public void onProviderEnabled(String provider) {}




	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {}
	
	private String getPhoneNumber(){
		TelephonyManager mTelephonyManager;
		mTelephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

		return mTelephonyManager.getLine1Number();
	}
	
	


}