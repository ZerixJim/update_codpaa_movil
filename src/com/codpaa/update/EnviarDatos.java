package com.codpaa.update;

import java.util.ArrayList;
import java.util.List;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import com.codpaa.listener.ResponseVisitasJson;
import com.codpaa.model.JsonEncuestaVeiw;
import com.codpaa.model.JsonMaterialModel;
import com.codpaa.model.JsonProductoImpulsor;
import com.codpaa.model.JsonProductosView;
import com.codpaa.model.JsonVisitas;
import com.codpaa.model.MaterialModel;
import com.codpaa.model.Respuesta;
import com.codpaa.model.VisitasModel;
import com.codpaa.model.generic.Producto;
import com.codpaa.provider.DbEstructure;
import com.codpaa.response.EncuestaResponse;
import com.codpaa.response.MaterialesJsonResponse;
import com.codpaa.response.ProductoCatalogoResponse;
import com.codpaa.util.Utilities;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.loopj.android.http.*;

import com.codpaa.db.BDopenHelper;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;


public class EnviarDatos {
	
	private AsyncHttpClient cliente = new AsyncHttpClient();

	private RequestParams rp = new RequestParams();
	private ProgressDialog progressDialog;
	private SQLiteDatabase base;
	private final BDopenHelper DB;
	
	private final Context context;

	private AsyncHttpResponseHandler respuesta = new AsyncHttpResponseHandler(){

		@Override
		public void onFailure(int arg0, Header[] arg1, byte[] arg2,Throwable arg3) {
			
			
		}

		@Override
		public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
			
		}
	};

	
	
	
	public EnviarDatos(Context a) {
		context = a;
		DB = new BDopenHelper(context);


		
	}
	
	
	
	private String getPhoneNumber(){
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions((Activity) context, new String[] {Manifest.permission.READ_PHONE_STATE}, 125);
        }
		TelephonyManager mTelephonyManager;
		mTelephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		return mTelephonyManager.getLine1Number();
	}




	public void sendCatalogoProducto(){
		try{


			Cursor cursor = DB.getProductCatalogo();

			if (cursor.getCount() > 0 ){


				Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();


				for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()){
					JsonProductosView view = new JsonProductosView();

					view.setIdPromotor(cursor.getInt(cursor.getColumnIndex(DbEstructure.TiendaProductoCatalogo.ID_PROMOTOR)));
					view.setIdTienda(cursor.getInt(cursor.getColumnIndex(DbEstructure.TiendaProductoCatalogo.ID_TIENDA)));
					view.setFecha(cursor.getString(cursor.getColumnIndex(DbEstructure.TiendaProductoCatalogo.FECHA)));

					final String[] productos = cursor.getString(cursor.getColumnIndex("productos")).split(",");

					view.convert(productos);


					//Log.d("GSON", gson.toJson(view));


					AsyncHttpClient client = new AsyncHttpClient();
					final RequestParams requestParams = new RequestParams();
					requestParams.put("json", gson.toJson(view));
					client.post(context, Utilities.WEB_SERVICE_CODPAA + "sendProdTiend.php", requestParams, new JsonHttpResponseHandler(){


						@Override
						public void onStart() {
							super.onStart();

							progressDialog = new ProgressDialog(context);

							progressDialog.setMessage("Enviando..");

							progressDialog.show();


						}

						@Override
						public void onFinish() {
							super.onFinish();

							progressDialog.setMessage("Se envio Correctamente!!!..");

							new Handler().postDelayed(new Runnable() {
								@Override
								public void run() {

									progressDialog.dismiss();

								}
							},2000);




						}

						@Override
						public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
							super.onSuccess(statusCode, headers, response);

							if (response != null){
								try {

									Log.d("Json Response", response.toString());
									if (response.getBoolean("insert")){

										SQLiteDatabase db = new BDopenHelper(context).getWritableDatabase();

										int idTienda = response.getInt("idTienda");

										String fecha = response.getString("fecha");

										JSONArray array = response.getJSONArray("productos");

										for (int i = 0; i < array.length() ; i++){

											ContentValues contentValues = new ContentValues();
											contentValues.put(DbEstructure.TiendaProductoCatalogo.ESTATUS, 2);

											db.execSQL("update tienda_productos_catalogo set status=2 " +
													"where idTienda="+idTienda+" and fecha='"+fecha+"' and idProducto="+ array.getInt(i));

										}

										//int[] array  =

										db.close();

                                    }
								} catch (JSONException e) {
									e.printStackTrace();
								}
							}
						}

						@Override
						public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
							super.onFailure(statusCode, headers, responseString, throwable);

							Log.e("Error server", responseString + " " + throwable);
						}

						@Override
						public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
							super.onFailure(statusCode, headers, throwable, errorResponse);
						}

						@Override
						public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
							super.onFailure(statusCode, headers, throwable, errorResponse);


						}
					});


				}

			}


		} catch (Exception e){
			e.printStackTrace();
		}
	}

	public void sendEncuesta(){
		try {

			Cursor cursor = DB.getRespuestas();
			if (cursor.getCount() > 0){

				ArrayList<Respuesta> respuestas = new ArrayList<>();

				for (cursor.moveToFirst(); !cursor.isAfterLast() ; cursor.moveToNext()){
					final Respuesta res = new Respuesta();
					res.setIdEncuesta(cursor.getInt(cursor.getColumnIndex("idEncuesta")));
					res.setIdPregunta(cursor.getInt(cursor.getColumnIndex("idPregunta")));
					res.setRespuesta(cursor.getString(cursor.getColumnIndex("respuesta")));
					res.setIdPromotor(cursor.getInt(cursor.getColumnIndex("idPromotor")));
					res.setIdTienda(cursor.getInt(cursor.getColumnIndex("idTienda")));

					respuestas.add(res);

				}

				Gson gson = new Gson();

				JsonEncuestaVeiw view = new JsonEncuestaVeiw();
				view.setRespuestas(respuestas);


				//Log.d("json", gson.toJson(view));


				AsyncHttpClient client = new AsyncHttpClient();
				RequestParams rp = new RequestParams();
				rp.put("json", gson.toJson(view));


				client.post(Utilities.WEB_SERVICE_CODPAA + "sendEncuesta.php",rp, new EncuestaResponse(context));



			}




		}catch (Exception e){
			e.printStackTrace();
		}
	}




	public void enviarVisitasPendientes(){


		try {

			Cursor curVisitas = DB.datosVisitas();

			//Log.d("entro", "enviar visitas");


			if(curVisitas.getCount() > 0) {


				List<VisitasModel> visitas = new ArrayList<>();


				for(curVisitas.moveToFirst(); !curVisitas.isAfterLast(); curVisitas.moveToNext()) {

					final VisitasModel model = new VisitasModel();

					model.setIdTienda(curVisitas.getInt(0));
					model.setIdPromotor(curVisitas.getInt(1));
					model.setFechaCaptura(curVisitas.getString(2));
					model.setHora(curVisitas.getString(3));
					model.setLatitud(curVisitas.getDouble(4));
					model.setLongitud(curVisitas.getDouble(5));
					model.setTipo(curVisitas.getString(6));
					model.setNumeroTelefono(getPhoneNumber());


					visitas.add(model);


				}



				RequestParams rp = new RequestParams();

				JsonVisitas jsonVisitas = new JsonVisitas();
				jsonVisitas.setVisitas(visitas);

				Gson json = new Gson();

				rp.put("json", json.toJson(jsonVisitas));

				AsyncHttpClient client = new AsyncHttpClient();


				//Log.d("registros" ,json.toJson(jsonVisitas));

                client.post(Utilities.WEB_SERVICE_CODPAA+"send_visitas_json.php",rp,

                        new ResponseVisitasJson(context));


				//Log.d("url",Utilities.WEB_SERVICE_CODPAA_TEST + "send_visitas_json.php");




			}else {
				//Toast.makeText(context, "No hay Registros pendientes", Toast.LENGTH_SHORT).show();
				Log.d("visitas Pendientes", "no hay visitas por enviar");
			}
			curVisitas.close();
			DB.close();

		}catch(Exception e) {
			e.printStackTrace();
		}


	}
	

	public void enviarVisitas() {
		try {

			Cursor curVisitas = DB.datosVisitas();

			//Log.d("entro", "enviar visitas");
		
			
			if(curVisitas.getCount() > 0) {


				List<VisitasModel> visitas = new ArrayList<>();


				for(curVisitas.moveToFirst(); !curVisitas.isAfterLast(); curVisitas.moveToNext()) {

					final VisitasModel model = new VisitasModel();

					model.setIdTienda(curVisitas.getInt(0));
					model.setIdPromotor(curVisitas.getInt(1));
					model.setFechaCaptura(curVisitas.getString(2));
					model.setHora(curVisitas.getString(3));
					model.setLatitud(curVisitas.getDouble(4));
					model.setLongitud(curVisitas.getDouble(5));
					model.setTipo(curVisitas.getString(6));
					model.setNumeroTelefono(getPhoneNumber());


					visitas.add(model);


				}



				RequestParams rp = new RequestParams();

				JsonVisitas jsonVisitas = new JsonVisitas();
				jsonVisitas.setVisitas(visitas);

				Toast.makeText(context, "Enviando " + visitas.size() + " visitas ", Toast.LENGTH_SHORT).show();

				Gson json = new Gson();

				rp.put("json", json.toJson(jsonVisitas));

				AsyncHttpClient client = new AsyncHttpClient();


				//Log.d("registros" ,json.toJson(jsonVisitas));


                client.post(context, Utilities.WEB_SERVICE_CODPAA +"send_visitas_json.php",rp,

                        new ResponseVisitasJson(context));


				//Log.d("url",Utilities.WEB_SERVICE_CODPAA_TEST + "send_visitas_json.php");

				

				
			}else {
				//Toast.makeText(context, "No hay Visitas sin enviar", Toast.LENGTH_SHORT).show();
			}
			curVisitas.close();
			DB.close();

		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	

	public void enviarMateriales(int idPromor){

		SQLiteDatabase base = new BDopenHelper(context).getReadableDatabase();

		String sql = "select * from materiales_solicitud where estatus=1;";

		Cursor cursor = base.rawQuery(sql, null);

		if (cursor.getCount() > 0){
			List<MaterialModel> materialList = new ArrayList<>();

			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()){

				MaterialModel material = new MaterialModel();
				material.setIdMaterial(cursor.getInt(cursor.getColumnIndex(DbEstructure.MaterialesSolicitud.ID_MATERIAL)));
				material.setIdPromotor(cursor.getInt(cursor.getColumnIndex(DbEstructure.MaterialesSolicitud.ID_PROMOTOR)));
				material.setIdTienda(cursor.getInt(cursor.getColumnIndex(DbEstructure.MaterialesSolicitud.ID_TIENDA)));
				material.setFecha(cursor.getString(cursor.getColumnIndex(DbEstructure.MaterialesSolicitud.FECHA)));
				material.setCantidad(cursor.getInt(cursor.getColumnIndex(DbEstructure.MaterialesSolicitud.CANTIDAD)));
				material.setIdProducto(cursor.getInt(cursor.getColumnIndex(DbEstructure.MaterialesSolicitud.ID_PRODUCTO)));
				materialList.add(material);

			}

			RequestParams reques = new RequestParams();
			reques.put("solicitud", "upload_materials");

			Gson gson = new Gson();
			JsonMaterialModel materialModel = new JsonMaterialModel();

			materialModel.setMateriales(materialList);
			materialModel.setIdPromotor(idPromor);


			reques.put("json", gson.toJson(materialModel));

			//Log.d("json", gson.toJson(materialModel));

			AsyncHttpClient cliente = new AsyncHttpClient();

			MaterialesJsonResponse response = new MaterialesJsonResponse(context);
			cliente.get(Utilities.WEB_SERVICE_CODPAA + Utilities.SERV_PHP, reques, response);

		}



		cursor.close();

	}

	
	public void enviarFrentes() {
		try {
			
			
			Cursor curFrentes = DB.datosFrentes();



			if(curFrentes.getCount() != 0 && verificarConexion()) {
				
				for(curFrentes.moveToFirst(); !curFrentes.isAfterLast(); curFrentes.moveToNext()) {




					RequestParams rp = new RequestParams();

					Log.d("Datos count", curFrentes.getCount() + "");


					rp.put("idTien", Integer.toString(curFrentes.getInt(0)));
					rp.put("idCel", Integer.toString(curFrentes.getInt(1)));
					rp.put("fecha", curFrentes.getString(2));
					rp.put("idMarc", Integer.toString(curFrentes.getInt(3)));
					rp.put("idProdu",Integer.toString(curFrentes.getInt(4)));
					rp.put("cantidad", Integer.toString(curFrentes.getInt(curFrentes.getColumnIndex("cantidad"))));

					Log.d("Datos", rp.toString());

					/*rp.put("cha1", Integer.toString(curFrentes.getInt(5)));
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
                    rp.put("f14", Integer.toString(curFrentes.getInt(25)));*/


					AsyncHttpClient cliente = new AsyncHttpClient();



					
					cliente.post(Utilities.WEB_SERVICE_CODPAA+"send_front.php", rp,
							new HttpResponseFrentes(context, curFrentes.getInt(0),curFrentes.getString(2), curFrentes.getInt(3)));
					

				}
				
				
			}else{


				Toast.makeText(context, "Error al enviar", Toast.LENGTH_SHORT).show();


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
		Context act;
		
		public HttpResponseFrentes(Context activi,int idTi,String fecha, int idMar){
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
                            new HttpResponseAddress(context,cursor.getInt(cursor.getColumnIndex("idTienda")),
                                    cursor.getInt(cursor.getColumnIndex("idPromotor")),cursor.getString(cursor.getColumnIndex("direccion")));

                    client.post(context, Utilities.WEB_SERVICE_CODPAA + "sendAddress.php", requestParams, httpResponseAddress );

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

                    rp.put("comentario", curSurtido.getString(curSurtido.getColumnIndex("comentario")));




					
					
					HttpResponseSurtido http = new HttpResponseSurtido(context, curSurtido.getInt(0),curSurtido.getString(3),curSurtido.getInt(4));
					cliente.post(Utilities.WEB_SERVICE_CODPAA+"send_surti.php", rp, http);
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
						Toast.makeText(context, "Surtido recibido", Toast.LENGTH_SHORT).show();
                        db = new BDopenHelper(context).getWritableDatabase();
                        db.execSQL("delete from surtido where idTienda="+idTienda+" and fecha='"+fecha+"' and idProducto="+idProducto+";");
                        //Log.d("ResponseSurtido", "eliminado");
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
					rp.put("estatus_producto", curInven.getInt(curInven.getColumnIndex("estatus_producto")));

					//Log.d("Estatus"," "+ curInven.getInt(curInven.getColumnIndex("estatus")));


					Log.d("datos", rp.toString());


					cliente.post(Utilities.WEB_SERVICE_CODPAA +"sendinventario.php", rp,
							new HttpResponseInventario(context, curInven.getInt(0),curInven.getString(2), curInven.getInt(3)));

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
		Context act;
		
		public HttpResponseInventario(Context activi,int idTi,String fecha, int idProd){
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
					
					
					
					cliente.post(Utilities.WEB_SERVICE_CODPAA+"sendexhi.php", rp, new HttpResponseExhi(context, curExhi.getInt(0), curExhi.getString(3), curExhi.getInt(4), curExhi.getInt(2)));

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
		Context act;
		
		public HttpResponseExhi(Context activi,int idTi,String fecha, int idProd, int idExh){
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
			base = new BDopenHelper(context).getWritableDatabase();
			
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
			base = new BDopenHelper(context).getWritableDatabase();
			
			if(curComentario.getCount() > 0 && verificarConexion()){
				for(curComentario.moveToFirst(); !curComentario.isAfterLast(); curComentario.moveToNext()){
					rp.put("idTien", Integer.toString(curComentario.getInt(0)));
					rp.put("idCel", Integer.toString(curComentario.getInt(1)));
					rp.put("fecha", curComentario.getString(2));
					rp.put("comentario", curComentario.getString(3));
					rp.put("idMarca", curComentario.getInt(curComentario.getColumnIndex("idMarca")));


					cliente.post(Utilities.WEB_SERVICE_CODPAA+"send_comentario.php", rp, respuesta);
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
					

					cliente.post(Utilities.WEB_SERVICE_CODPAA+"send_precio.php", rpIn,
							new HttpResponseInteligen(context, curInteli.getInt(1), curInteli.getString(5),curInteli.getInt(2)));
					
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
		Context act;
		
		public HttpResponseInteligen(Context activi,int idTi,String fecha, int idProd){
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

		@Override
		public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
			super.onFailure(statusCode, headers, throwable, errorResponse);

			//Toast.makeText(act, "Registro Recibido", Toast.LENGTH_SHORT).show();
		}
	}



	public void sendVentaPromedio(){

		try{
			Cursor curVenta = DB.datosVenta();
			base = new BDopenHelper(context).getWritableDatabase();

			if(curVenta.getCount() > 0 && verificarConexion()){
				for(curVenta.moveToFirst(); !curVenta.isAfterLast(); curVenta.moveToNext()){
					rp.put("idMarca", Integer.toString(curVenta.getInt(curVenta.getColumnIndex("idMarca"))));
					rp.put("tipo", curVenta.getString(curVenta.getColumnIndex("tipo")));
					rp.put("cantidad", Float.toString(curVenta.getFloat(curVenta.getColumnIndex("cantidad"))));
					rp.put("fechai", curVenta.getString(curVenta.getColumnIndex("fecha_inicio")));
                    rp.put("fechaf", curVenta.getString(curVenta.getColumnIndex("fecha_fin")));
                    rp.put("idTienda", Integer.toString(curVenta.getInt(curVenta.getColumnIndex("idTienda"))));
                    rp.put("idPromotor", Integer.toString(curVenta.getInt(curVenta.getColumnIndex("idPromotor"))));
					rp.put("idProducto", Integer.toString(curVenta.getInt(curVenta.getColumnIndex("idProducto"))));

                    HttpVentaResponse response = new HttpVentaResponse(context,
                            curVenta.getInt(curVenta.getColumnIndex("idMarca")),
                            curVenta.getString(curVenta.getColumnIndex("tipo")),
                            curVenta.getString(curVenta.getColumnIndex("fecha_inicio")),
                            curVenta.getString(curVenta.getColumnIndex("fecha_fin")),
							curVenta.getInt(curVenta.getColumnIndex("idProducto")));
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


        private int idMarca, idProducto;
        private String tipo, fechaI, fechaF;
        Context context;

        public HttpVentaResponse(Context context,int idMarca, String tipo, String fechaI, String fechaF, int idProducto){
            this.idMarca = idMarca;
            this.tipo = tipo;
            this.fechaI = fechaI;
            this.fechaF = fechaF;
            this.context = context;
			this.idProducto = idProducto;

        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

            Log.d("Status", " "+statusCode);

            if (response != null){

                try {
                    if (response.getBoolean("insert")){

                        SQLiteDatabase db = new BDopenHelper(EnviarDatos.this.context).getWritableDatabase();

                        try {

                            db.execSQL("update ventaPromedio set estatus=2 where idMarca="+idMarca+" " +
                                    "and tipo='"+tipo+"' and fecha_inicio='"+fechaI+"' and fecha_fin='"+fechaF+"' and idProducto="+idProducto);

                            Toast.makeText(EnviarDatos.this.context, "Registro Recibido", Toast.LENGTH_SHORT).show();

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
	
	private boolean verificarConexion() {
	    ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo netInfo = cm.getActiveNetworkInfo();

	    return netInfo != null && netInfo.isConnected();
	}


	public void sentEstatusToServer(int idPromotor){



		if (getProductListSendToServer().size() > 0){


			AsyncHttpClient client = new AsyncHttpClient();

			RequestParams rp = new RequestParams();


			Gson gson = new Gson();


			JsonProductoImpulsor json = new JsonProductoImpulsor(getProductListSendToServer(), idPromotor);

			rp.put("solicitud", "sendCatalogo");
			rp.put("json", gson.toJson(json));

			//Log.d("json", gson.toJson(json));

			client.post(Utilities.WEB_SERVICE_CODPAA + "send_impulsor.php", rp , new ProductoCatalogoResponse(context));


			//mRecyclerView.getAdapter().notifyDataSetChanged();
		}



	}


	public void sendTonos(){


		SQLiteDatabase db = new BDopenHelper(context).getReadableDatabase();

		Cursor cursor = db.rawQuery("select * from "+ DbEstructure.TonoPallet.TABLE_NAME + " " +

				" where " + DbEstructure.TonoPallet.STATUS +"= 1 ", null);


		if (cursor.getCount() > 0){

			for (cursor.moveToFirst();!cursor.isAfterLast(); cursor.moveToNext()){


				JSONObject jsonObject = new JSONObject();

				try {
					jsonObject.put(DbEstructure.TonoPallet.ID_TIENDA, cursor.getInt(cursor.getColumnIndex(DbEstructure.TonoPallet.ID_TIENDA)));
					jsonObject.put(DbEstructure.TonoPallet.CATEGORIA, cursor.getString(cursor.getColumnIndex(DbEstructure.TonoPallet.CATEGORIA)));
					jsonObject.put(DbEstructure.TonoPallet.FRENTES, cursor.getInt(cursor.getColumnIndex(DbEstructure.TonoPallet.FRENTES)));
					jsonObject.put(DbEstructure.TonoPallet.TONOS, cursor.getInt(cursor.getColumnIndex(DbEstructure.TonoPallet.TONOS)));
					jsonObject.put(DbEstructure.TonoPallet.PROMOTOR, cursor.getInt(cursor.getColumnIndex(DbEstructure.TonoPallet.PROMOTOR)));
					jsonObject.put(DbEstructure.TonoPallet.FECHA, cursor.getString(cursor.getColumnIndex(DbEstructure.TonoPallet.FECHA)));


					AsyncHttpClient client = new AsyncHttpClient();
					client.addHeader("Authorization", "908203409802309480938209-4395&64");

					StringEntity entity = new StringEntity(jsonObject.toString());

					client.post(context,Utilities.API_PRODUCTION + "tiendas/tono", entity, "application/json", new JsonHttpResponseHandler(){

						@Override
						public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
							super.onSuccess(statusCode, headers, response);


							try {
								if (response.getBoolean("q")){

									SQLiteDatabase db  = new BDopenHelper(context).getWritableDatabase();

									ContentValues cv = new ContentValues();
									cv.put(DbEstructure.TonoPallet.STATUS, 2);

									JSONObject object = response.getJSONObject("datos");

									db.update(DbEstructure.TonoPallet.TABLE_NAME,cv,DbEstructure.TonoPallet.ID_TIENDA+ "=" + object.getInt(DbEstructure.TonoPallet.ID_TIENDA) + "" +
											" and " + DbEstructure.TonoPallet.FECHA + "='" + object.getString(DbEstructure.TonoPallet.FECHA) + "'" , null);


								}
							} catch (JSONException e) {
								e.printStackTrace();
							}


						}

						@Override
						public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
							super.onFailure(statusCode, headers, throwable, errorResponse);
						}
					});



				} catch (Exception e) {
					e.printStackTrace();
				}


			}



		}

		cursor.close();
		db.close();



	}



	public void sendPrecioMarca(){

		SQLiteDatabase db = new BDopenHelper(context).getReadableDatabase();

		Cursor cursor = db.rawQuery("select * from "+ DbEstructure.PrecioMarca.TABLE_NAME + " " +
				"" +
				" where " + DbEstructure.PrecioMarca.STATUS +"= 1", null);




		if (cursor.getCount() > 0) {

			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {


				JSONObject jsonObject = new JSONObject();

				try {
					jsonObject.put(DbEstructure.PrecioMarca.ID_TIENDA, cursor.getInt(cursor.getColumnIndex(DbEstructure.PrecioMarca.ID_TIENDA)));
					jsonObject.put(DbEstructure.PrecioMarca.ID_MARCA, cursor.getInt(cursor.getColumnIndex(DbEstructure.PrecioMarca.ID_MARCA)));
					jsonObject.put(DbEstructure.PrecioMarca.PRICE, cursor.getFloat(cursor.getColumnIndex(DbEstructure.PrecioMarca.PRICE)));
					jsonObject.put(DbEstructure.PrecioMarca.FECHA, cursor.getString(cursor.getColumnIndex(DbEstructure.PrecioMarca.FECHA)));


					AsyncHttpClient client = new AsyncHttpClient();
					client.addHeader("Authorization", "908203409802309480938209-4395&64");

					StringEntity entity = new StringEntity(jsonObject.toString());

					client.post(context, Utilities.API_PRODUCTION + "tiendas/precio-marca", entity, "application/json", new JsonHttpResponseHandler() {

						@Override
						public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
							super.onSuccess(statusCode, headers, response);


							Log.d("successprecio", response.toString());

							try {
								if (response.getBoolean("q")) {

									SQLiteDatabase db2 = new BDopenHelper(context).getWritableDatabase();

									ContentValues cv = new ContentValues();
									cv.put(DbEstructure.PrecioMarca.STATUS, 2);

									JSONObject object = response.getJSONObject("datos");

									db2.update(DbEstructure.PrecioMarca.TABLE_NAME, cv, DbEstructure.PrecioMarca.ID_TIENDA + "=" + object.getInt(DbEstructure.PrecioMarca.ID_TIENDA) + "" +
											" and " + DbEstructure.PrecioMarca.FECHA + "='" + object.getString(DbEstructure.PrecioMarca.FECHA) + "' and " + DbEstructure.PrecioMarca.ID_MARCA + "="
											+ object.getInt(DbEstructure.PrecioMarca.ID_MARCA), null);

									db2.close();


								}
							} catch (JSONException e) {
								e.printStackTrace();
							}


						}

						@Override
						public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
							super.onFailure(statusCode, headers, throwable, errorResponse);
						}
					});


				} catch (Exception e) {
					e.printStackTrace();
				}


			}

		}

		cursor.close();


	}




	private List<Producto> getProductListSendToServer(){
		List<Producto> list = new ArrayList<>();

		SQLiteDatabase db = new BDopenHelper(context).getReadableDatabase();

		Cursor cursor = db.rawQuery("select * from producto_catalogado_tienda where estatus_registro = 1", null);


		if(cursor.getCount() > 0){
			for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()){


				final Producto producto = new Producto();
				producto.setIdProducto(cursor.getInt(cursor.getColumnIndex("idProducto")));
				producto.setIdTienda(cursor.getInt(cursor.getColumnIndex("idTienda")));
				producto.setFecha(cursor.getString(cursor.getColumnIndex("fecha_captura")));
				producto.setEstatus(cursor.getInt(cursor.getColumnIndex("estatus_producto")));
				producto.setCantidad(cursor.getInt(cursor.getColumnIndex("cantidad")));

				if(cursor.getInt(cursor.getColumnIndex("estatus_producto")) == Producto.EstatusTypes.PROCESO_CATALOGACION){


					String sql = "select * from "
							+ DbEstructure.ProcesoCatalogacionObjeciones.TABLE_NAME +" " +
							" where idProducto="+ cursor.getInt(cursor.getColumnIndex("idProducto")) + " and idTienda=" +
							cursor.getInt(cursor.getColumnIndex("idTienda")) + " and fecha_captura='" +
							cursor.getString(cursor.getColumnIndex("fecha_captura")) + "'";

					Cursor cursorObjeciones = db.rawQuery( sql, null);

					//Log.d("sql", sql );

					if (cursorObjeciones.getCount() > 0){

						List<String> lista = new ArrayList<>();
						for (cursorObjeciones.moveToFirst() ; !cursorObjeciones.isAfterLast() ; cursorObjeciones.moveToNext()){

							lista.add(cursorObjeciones.getString(cursorObjeciones.getColumnIndex("descripcion")));

						}

						producto.setObjeciones(lista);

					}

					cursorObjeciones.close();

				}


				list.add(producto);
			}
		}


		cursor.close();
		db.close();
		return list;
	}
	

}
