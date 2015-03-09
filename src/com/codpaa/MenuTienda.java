package com.codpaa;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;


import com.loopj.android.http.*;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import BD.BDopenHelper;
import adapters.FrentesCustomAdapter;
import adapters.InventariosCustomAdapter;
import modelos.FrentesModel;
import modelos.InventarioModel;

public class MenuTienda extends Activity implements OnClickListener{
	
	
	Button btnSalidaTi,btnEntrada, btnEncar, btnExhib, btnInven, btnFrente, btnSurtido, btnTiendaError;
	Button btnComentario, btnInteligencia, btnUpdaPro, btnFoto;
	SQLiteDatabase base = null;
	Location locGps,locNet;
	LocationManager localizar = null;
	TextView tiendaSeleccionada, promotor, txtEncargado, frentes, surtido, exhi,inventario;
	int idPromotor, idTienda;
	String gpsProvedor, netProvedor;
	BDopenHelper DB = null;
	String myVersionName = "not available";
	EnviarDatos enviar;
	Spinner spinnerEnc;
	EditText editNombre;
	AsyncHttpClient cliente;
	RequestParams rp;
	private Boolean Salida = false;
	private Boolean Entrada = false;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.menutienda);
		cliente = new AsyncHttpClient();
		rp = new RequestParams();
		Intent recibeIdTi = getIntent();
		enviar = new EnviarDatos(this);
		
		idTienda = (Integer) recibeIdTi.getExtras().get("idTienda");
		idPromotor = (Integer) recibeIdTi.getExtras().get("idPromotor");
		
		
		tiendaSeleccionada = (TextView) findViewById(R.id.tiendaSeleccio);
		promotor = (TextView) findViewById(R.id.promotor);
        inventario = (TextView) findViewById(R.id.inventario);
        txtEncargado = (TextView) findViewById(R.id.Encargado);
        frentes = (TextView) findViewById(R.id.frentes);
        surtido = (TextView) findViewById(R.id.surtido);
        exhi = (TextView) findViewById(R.id.textExhibicio);
        btnFrente = (Button) findViewById(R.id.buttonMensaje);
        btnSalidaTi = (Button) findViewById(R.id.salidaTienda);
        btnEntrada = (Button) findViewById(R.id.btnEnTienda);
        btnEncar = (Button) findViewById(R.id.btnEncarg);
        btnExhib = (Button) findViewById(R.id.buttonExhib);
        btnUpdaPro = (Button) findViewById(R.id.btnUpdaPro);
        btnInven = (Button) findViewById(R.id.btnInvenBode);
        btnSurtido = (Button) findViewById(R.id.buttonEnviar);
        btnTiendaError = (Button) findViewById(R.id.btnTiendaError);
		btnComentario = (Button) findViewById(R.id.btnComentario);
		btnInteligencia = (Button) findViewById(R.id.btnMenInt);
		btnFoto = (Button) findViewById(R.id.btnfoto);
		
		
		DB = new BDopenHelper(this);
		
		btnFrente.setOnClickListener(this);
		btnEntrada.setOnClickListener(this);
		btnSurtido.setOnClickListener(this);
		btnInven.setOnClickListener(this);
		btnSalidaTi.setOnClickListener(this);
		btnTiendaError.setOnClickListener(this);
		btnEncar.setOnClickListener(this);
		btnExhib.setOnClickListener(this);
		btnComentario.setOnClickListener(this);
		btnInteligencia.setOnClickListener(this);
		btnUpdaPro.setOnClickListener(this);
		btnFoto.setOnClickListener(this);

        // textView with listener
        frentes.setOnClickListener(this);
		inventario.setOnClickListener(this);

		localizar = (LocationManager) getSystemService(LOCATION_SERVICE);
		
		Context context = getApplicationContext();
		PackageManager packageManager = context.getPackageManager();
		String packageName = context.getPackageName();


		try {
		    myVersionName = packageManager.getPackageInfo(packageName, 0).versionName;
		    
		} catch (PackageManager.NameNotFoundException e) {
		    e.printStackTrace();
		}
		

		
		gpsProvedor = LocationManager.GPS_PROVIDER;
		netProvedor = LocationManager.NETWORK_PROVIDER;
		
		
		localizar.requestLocationUpdates(netProvedor, 30000, 10, new localizacion());
			
		
		try{
			
			Cursor cTienda = new BDopenHelper(this).tienda(idTienda);
			cTienda.moveToFirst();
			
			tiendaSeleccionada.setText(cTienda.getString(0)+" "+cTienda.getString(1));
			tiendaSeleccionada.setTextColor(Color.rgb(175, 237, 252));
			cTienda.close();
			
			try {
				Cursor cNomPromo = new BDopenHelper(this).nombrePromotor(idPromotor);
				cNomPromo.moveToFirst();
				
				promotor.setText(cNomPromo.getString(0));
				cNomPromo.close();
				

				
			}catch(Exception e) {
				Toast.makeText(this, "error menuTienda 2", Toast.LENGTH_SHORT).show();
			}
			

			
		}catch(Exception e){
			
			Toast.makeText(this, "error", Toast.LENGTH_SHORT).show();			
		}
		
		
		
		
		
	}
	
	public void entradaTienda() {
		
		Calendar c = Calendar.getInstance();
		SimpleDateFormat dFecha = new SimpleDateFormat("dd-MM-yyyy");
		SimpleDateFormat dHora = new SimpleDateFormat("HH:mm:ss a");
		SimpleDateFormat dSema = new SimpleDateFormat("w");
			
		String fecha = dFecha.format(c.getTime());
		String hora = dHora.format(c.getTime());
		String sem = dSema.format(c.getTime());
		int semana = Integer.parseInt(sem);
		
		
		if(!Entrada){
			btnTiendaError.setVisibility(View.GONE);
			
				
			locGps = localizar.getLastKnownLocation(gpsProvedor);
			locNet = localizar.getLastKnownLocation(netProvedor);
				
			BDopenHelper base = new BDopenHelper(getApplicationContext());
				
		
			if(locGps != null) {
				
				Entrada = true;
				base.insertarLocalizacion(idTienda, idPromotor, fecha ,hora ,locGps.getLatitude(), locGps.getLongitude(), 12, "E",1,semana);

				Toast.makeText(this, "Entrada Guardada", Toast.LENGTH_SHORT).show();
				btnEntrada.post(new Runnable() {
					
					public void run() {
							
						
						btnEntrada.setBackgroundResource(R.drawable.custom_btn_dark_khaki);
						enviar.enviarVisitas();
					}
						
				});
					
			}else if(locNet != null){
				
				Entrada = true;
				base.insertarLocalizacion(idTienda, idPromotor, fecha ,hora ,locNet.getLatitude(), locNet.getLongitude(), 12, "E",1,semana);
			
				
				Toast.makeText(this, "Entrada Guardada", Toast.LENGTH_SHORT).show();
				btnEntrada.post(new Runnable() {
					
					public void run() {
							
					
						btnEntrada.setBackgroundResource(R.drawable.custom_btn_dark_khaki);
						enviar.enviarVisitas();
					}
						
				});
				
				
			}else {
				
				Toast.makeText(this, "Imposible Guardar Entrada", Toast.LENGTH_SHORT).show();
				base.close();
			}
		}else{
			Toast.makeText(this, "Ya existe una Entrada", Toast.LENGTH_LONG).show();
		}
		
		
	
				
	}
			
		
	
	
	private void salidaTienda() {
		
		if(Entrada){
			Thread hiloSalida = new Thread() {
				public void run() {
	
					
						if(!Salida){
							
							Calendar c = Calendar.getInstance();
							SimpleDateFormat dFecha = new SimpleDateFormat("dd-MM-yyyy");
							SimpleDateFormat dHora = new SimpleDateFormat("HH:mm:ss a");
							SimpleDateFormat dSema = new SimpleDateFormat("w");
								
							String fecha = dFecha.format(c.getTime());
							String hora = dHora.format(c.getTime());
							String sem = dSema.format(c.getTime());
							int semana = Integer.parseInt(sem);
							
							locGps = localizar.getLastKnownLocation(gpsProvedor);
							locNet = localizar.getLastKnownLocation(netProvedor);
							
							BDopenHelper base = new BDopenHelper(getApplicationContext());
							
							
							
							if(locGps != null) {
								
								base.insertarLocalizacion(idTienda, idPromotor, fecha ,hora ,locGps.getLatitude(), locGps.getLongitude(), 12, "S",1,semana);
							
								Salida = true;
								
								btnSalidaTi.post(new Runnable() {
									public void run() {
										
										
										btnSalidaTi.setBackgroundResource(R.drawable.custom_btn_dark_khaki);
										Toast.makeText(getApplicationContext(), "Salida Registrada", Toast.LENGTH_SHORT).show();
										
										enviar.enviarVisitas();

									}
									
								});
								
								try {
									sleep(2000);
								} catch (InterruptedException e) {
									
									e.printStackTrace();
								}
								MenuTienda.this.finish();
							}else if(locNet != null){
								base.insertarLocalizacion(idTienda, idPromotor, fecha ,hora ,locNet.getLatitude(), locNet.getLongitude(), 12, "S",1,semana);
							
								
								
								Salida = true;
								
								btnSalidaTi.post(new Runnable() {
									public void run() {
										
										btnSalidaTi.setBackgroundResource(R.drawable.custom_btn_dark_khaki);
										Toast.makeText(getApplicationContext(), "Salida Registrada", Toast.LENGTH_SHORT).show();
										
										enviar.enviarVisitas();
										
									}
									
								});
								
								try {
									sleep(2000);
								} catch (InterruptedException e) {
									
									e.printStackTrace();
								}
								MenuTienda.this.finish();
								
							}
						}else{
							MenuTienda.this.finish();
						}
					
					
				}
				
			};
		hiloSalida.start();
		
		}else{
			Toast.makeText(this, "No has registrado Entrada", Toast.LENGTH_SHORT).show();
		}
	}
	

	public void onClick(View v) {
		switch(v.getId()) {
		
		case R.id.buttonMensaje:
			menuFrentes();
			
			break;
			
		case R.id.btnEnTienda:
			entradaTienda();
			
			break;
			
		case R.id.salidaTienda:
			
			dialogoConfirmacionSalida();

			break;
			
		case R.id.btnEncarg:
			dialogoEncargado();

			break;
			
		case R.id.buttonEnviar:
			menuSurtido();
			break;
		case R.id.btnInvenBode:
			menuInventario();
			break;
			
		case R.id.buttonExhib:
			menuExhibicion();
			break;
			
		case R.id.btnTiendaError:
			tiendaErronea();
			break;
		case R.id.btnComentario:
			menuComentario();
			break;
			
		case R.id.btnMenInt:
			inteligenciaMercado();
			break;
		case R.id.btnfoto:
			capturaFoto();
			break;
		case R.id.btnUpdaPro:
			actualizarPro();
			break;
        case R.id.frentes:
            Log.v("TextFrentes","Onclick");
            dialogoFrentesCapturados();
            break;

            case R.id.inventario:
                dialogoInventariosCapturados();
                break;
		}
		
	}
	
	

	

	private void actualizarPro() {
		try {
			JSONParseAndroid productos = new JSONParseAndroid(this);
			productos.readAndParseProdcutos(idPromotor);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}

	@Override
	protected void onResume() {
		super.onResume();
		
		Calendar c = Calendar.getInstance();
		SimpleDateFormat dFecha = new SimpleDateFormat("dd-MM-yyyy");
		String fecha = dFecha.format(c.getTime());
		
		try {
			
			
			Cursor cuEncargados = DB.contadorEncargados(idTienda, fecha);
			txtEncargado.setText("Encargado ("+cuEncargados.getCount()+")");
			DB.close();
			
			
			try {
				Cursor cuFrentes = DB.contadorFrentes(idTienda, fecha);
				frentes.setText("Frentes: ("+cuFrentes.getCount()+")");
				DB.close();
				
				
				try {
					Cursor cuSurt = DB.SurtidoCantidad(idTienda, fecha);
					surtido.setText("Surtido: ("+cuSurt.getCount()+")");
					DB.close();
					
					try {
						Cursor cuInventario = DB.contarInventario(idTienda, fecha);
						inventario.setText("Inventario ("+cuInventario.getCount()+")");
						DB.close();
						try {
							Cursor cuEntra = DB.VisitaTienda(idTienda, fecha, "E");
							
							if(cuEntra.getCount() >0) {
								
								
								btnEntrada.setBackgroundResource(R.drawable.custom_btn_dark_khaki);
								Entrada = true;
								DB.close();
								
							}else {
								btnEntrada.setBackgroundResource(R.drawable.custom_btn_orange);
								Entrada = false;
								
							}
							try {
								Cursor cuSalida = DB.VisitaTienda(idTienda, fecha, "S");
								Settings.System.putInt(getContentResolver(), Settings.System.AUTO_TIME,1);
								
								if(cuSalida.getCount() >0) {
									
									
									btnSalidaTi.setBackgroundResource(R.drawable.custom_btn_dark_khaki);
									Salida = true;
									DB.close();
									
									if(Entrada && Salida)
										btnTiendaError.setVisibility(View.GONE);
								}else {
									
									btnSalidaTi.setBackgroundResource(R.drawable.custom_btn_orange);
									Salida = false;
								}
								
								DB.close();
								if(base != null)
									base.close();
							}catch(Exception e) {
                                e.printStackTrace();
							}
							
							//Mostrar Tienda Erronea
							if(!Entrada && !Salida){
								btnTiendaError.setVisibility(View.VISIBLE);
							}else if(Entrada){
								btnTiendaError.setVisibility(View.GONE);
							}
						}catch(Exception e){
                            e.printStackTrace();
						}
						
					}catch(Exception e) {
                        e.printStackTrace();
					}
				}catch(Exception e) {
                    e.printStackTrace();
				}
				
			}catch(Exception e) {
                e.printStackTrace();
			}
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		
	}



	@Override
	protected void onStart() {
		super.onStart();
		
		
		
	}
	
	private void menuInventario() {
		if(Entrada){
			Intent i = new Intent(this, InventarioBodega.class);
			i.putExtra("idTienda", idTienda);
			i.putExtra("idPromotor", idPromotor);
			startActivity(i);
		}else{
			Toast.makeText(this, "No has registrado Entrada", Toast.LENGTH_SHORT).show();
		}
		
		
	}


	private void menuFrentes() {
		if(Entrada){
			Intent i = new Intent(this, Frentes.class);
			i.putExtra("idTienda", idTienda);
			i.putExtra("idPromotor", idPromotor);
			startActivity(i);
		}else{
			Toast.makeText(this, "No has registrado Entrada", Toast.LENGTH_SHORT).show();
		}
	}
	
	private void menuSurtido() {
		if(Entrada){
			Intent i = new Intent(this, SurtidoMueble.class);
			i.putExtra("idTienda", idTienda);
			i.putExtra("idPromotor", idPromotor);
			startActivity(i);
		}else{
			Toast.makeText(this, "No has registrado Entrada", Toast.LENGTH_SHORT).show();
		}
		
	}
	
	private void menuExhibicion() {
		if(Entrada){
			Intent i = new Intent(this, Exhibiciones.class);
			i.putExtra("idTienda", idTienda);
			i.putExtra("idPromotor", idPromotor);
			startActivity(i);
		}else{
			Toast.makeText(this, "No has registrado Entrada", Toast.LENGTH_SHORT).show();
		}
		
	}
	
	private void menuComentario(){
		if(Entrada){
			Intent i = new Intent(this, ComentariosActivity.class);
			i.putExtra("idTienda", idTienda);
			i.putExtra("idPromotor", idPromotor);
			startActivity(i);
		}else{
			Toast.makeText(this, "No has registrado Entrada", Toast.LENGTH_SHORT).show();
		}
	}
	
	private void inteligenciaMercado(){
		if(Entrada){
			Intent i = new Intent(this, InteligenciaMercado.class);
			i.putExtra("idTienda", idTienda);
			i.putExtra("idPromotor", idPromotor);
			startActivity(i);
			
		}else{
			Toast.makeText(this, "No has registrado Entrada", Toast.LENGTH_SHORT).show();
		}
	}
	
	private void capturaFoto(){

		if(Entrada){
            if(!Salida){
                Intent i = new Intent(this, PhotoCapture.class);
                i.putExtra("idTienda", idTienda);
                i.putExtra("idPromotor", idPromotor);
                startActivity(i);
            }else {
                Toast.makeText(getApplicationContext(), "Ya Existe Salida...\n No es posible tomar foto", Toast.LENGTH_LONG).show();
            }

		}else{
			Toast.makeText(getApplicationContext(), "Entrada no Registrada...\n No es posible tomar foto", Toast.LENGTH_LONG).show();
		}
		
	}

	@Override
	protected void onPause() {
		super.onPause();
		
		localizar.removeUpdates(new localizacion());
		
	}
	
	

	private void dialogoEncargado() {
		
		
		Builder builder  = new AlertDialog.Builder(this);
		View vistaEncargado = LayoutInflater.from(this).inflate(R.layout.encargadotienda, null);
		String[] tipoEncargado = new String[] {"ENCARGADO","AUXILIAR"};
		spinnerEnc = (Spinner) vistaEncargado.findViewById(R.id.spinnerMarca);
		editNombre = (EditText) vistaEncargado.findViewById(R.id.nombreEnca);
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, tipoEncargado);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerEnc.setAdapter(adapter);
		
		EscucharDialogoEncargado listener = new EscucharDialogoEncargado();
		
		builder.setTitle("Selecciona Encargado").setPositiveButton("Guardar", listener).setNegativeButton("Cancelar", listener).setView(vistaEncargado);
		builder.create().show();
		
	}
	


	
	public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
        	
        	if(Entrada && !Salida){
        		Toast.makeText(this,"No has Registrado Salida", Toast.LENGTH_SHORT).show();
        	}else if(!Entrada && !Salida){
        		finish();
        	}else{
        		finish();
        	}
        		
        	
        }
        return true;
	}
	
	private class EscucharDialogoEncargado implements DialogInterface.OnClickListener{
		
		@Override
		public void onClick(DialogInterface dialog, int which) {
			
			if(which == DialogInterface.BUTTON_POSITIVE){
				
				
				String tipo = spinnerEnc.getSelectedItem().toString();
				String nombre = editNombre.getText().toString();
				
				Calendar c = Calendar.getInstance();
				SimpleDateFormat dFecha = new SimpleDateFormat("dd-MM-yyyy");

				
				String fecha = dFecha.format(c.getTime());
				if(!nombre.trim().equals("") && !nombre.isEmpty()) {
					new BDopenHelper(MenuTienda.this).insertarEncargadoTienda(idTienda, idPromotor, nombre, tipo, fecha);
					txtEncargado.setText("Encargado: OK");
					new EnviarDatos(MenuTienda.this).enviarEncargado();
				}else {
					Toast.makeText(getApplicationContext(),"no escribiste nombre del encargado", Toast.LENGTH_SHORT).show();
				}
				
				
				
				
			}else if(which == DialogInterface.BUTTON_NEGATIVE){
				Toast.makeText(getApplicationContext(),"Cancelaste la Seleccion", Toast.LENGTH_SHORT).show();
			}
			
		}
		
	}
	
	private void tiendaErronea(){
		if(!Entrada && !Salida){
			finish();
		}else{
			Toast.makeText(this, "check de Entrada detectado", Toast.LENGTH_SHORT).show();
		}
	}
	
	private class localizacion implements LocationListener{

		@Override
		public void onLocationChanged(Location arg0) {
			Log.d("Localizacion", "Cambio");
			
		}

		@Override
		public void onProviderDisabled(String arg0) {
			Log.d("Localizacion", "Provedor Desabilitado");
			
		}

		@Override
		public void onProviderEnabled(String arg0) {
	
			Log.d("Localizacion", "Provedor Activado");
		}

		@Override
		public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
			Log.d("Localizacion", "Status "+arg0);
			
		}
		
	}
	
	
	private void dialogoFrentesCapturados(){

        Builder builder = new AlertDialog.Builder(this);
        LayoutInflater layoutInflater =  (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View frentes = layoutInflater.inflate(R.layout.lista_frentes_capturados,null);
        Listener flistener = new Listener();
        ListView listView = (ListView) frentes.findViewById(R.id.listFrentesCap);
        FrentesCustomAdapter adapter = new FrentesCustomAdapter(this,R.layout.lista_frentes_capturados,getFrentesCapturados());
        listView.setAdapter(adapter);


        builder.setPositiveButton("Cerrar",flistener).setView(frentes);

        builder.create().show();
    }

    private void dialogoInventariosCapturados(){

        Builder builder = new AlertDialog.Builder(this);
        LayoutInflater layoutInflater =  (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View frentes = layoutInflater.inflate(R.layout.lista_capturados,null);
        Listener flistener = new Listener();
        ListView listView = (ListView) frentes.findViewById(R.id.listCapturados);

        InventariosCustomAdapter adapter = new InventariosCustomAdapter(this,R.layout.lista_capturados,getInventariosCapturados());
        //Inventarios

        //FrentesCustomAdapter adapter = new FrentesCustomAdapter(this,R.layout.lista_frentes_capturados,getFrentesCapturados());
        listView.setAdapter(adapter);


        builder.setPositiveButton("Cerrar",flistener).setView(frentes);

        builder.create().show();
    }

    private ArrayList<FrentesModel> getFrentesCapturados(){
        base = new BDopenHelper(this).getReadableDatabase();
        String sql = "select m.nombre, p.nombre || ' ' || p.presentacion, (f.cha1+f.cha2+f.cha3+f.cha4+f.cha5+f.cha6) as total, f.status, f.fecha  " +
                "from frentesCharola as f inner join marca as m on f.idMarca=m.idMarca " +
                "inner join producto as p on f.idProducto=p.idProducto where f.idTienda="+idTienda;
        Cursor frentes = base.rawQuery(sql,null);
        ArrayList<FrentesModel> arrayFrentes = new ArrayList<FrentesModel>();
        Log.v("Cursor","CAntidad: "+frentes.getCount());
        if(frentes.getCount() > 0){
            for (frentes.moveToFirst();!frentes.isAfterLast();frentes.moveToNext()){
                final FrentesModel fm = new FrentesModel();
                fm.setMarca(frentes.getString(0));
                fm.setProducto(frentes.getString(1));
                fm.setCantidad(frentes.getInt(2));
                fm.setStatus(frentes.getInt(3));
                fm.setFecha(frentes.getString(4));


                arrayFrentes.add(fm);
            }
        }else {
            final FrentesModel fm = new FrentesModel();
            fm.setMarca("No Existen Registros");
            arrayFrentes.add(fm);
        }

        base.close();

        return arrayFrentes;
    }

    private ArrayList<InventarioModel> getInventariosCapturados(){
        base = new BDopenHelper(this).getReadableDatabase();

        String sql = "select m.nombre, p.nombre || ' ' || p.presentacion,ivp.status,ivp.tipo,ivp.cantidadFisico,ivp.cantidadSistema  " +
                "from invProducto as ivp " +
                "inner join producto as p on ivp.idProducto=p.idProducto " +
                "inner join marca as m on m.idMarca=p.idMarca " +
                "where ivp.idTienda="+idTienda;

        Cursor inventarios = base.rawQuery(sql,null);
        ArrayList<InventarioModel> arrayInventarios = new ArrayList<InventarioModel>();
        Log.v("Cursor","CAntidad: "+inventarios.getCount());
        if(inventarios.getCount() > 0){
            for (inventarios.moveToFirst();!inventarios.isAfterLast();inventarios.moveToNext()){
                final InventarioModel Ip = new InventarioModel();
                Ip.setMarca(inventarios.getString(0));
                Ip.setProducto(inventarios.getString(1));
                Ip.setStatus(inventarios.getInt(2));
                Ip.setTipo(inventarios.getString(3));
                Ip.setCantidadFisico(inventarios.getInt(4));
                Ip.setCantidadSistema(inventarios.getInt(5));

                arrayInventarios.add(Ip);
            }
        }else {
            final InventarioModel Ip = new InventarioModel();
            Ip.setMarca("No Existen Registros");
            arrayInventarios.add(Ip);
        }

        base.close();

        return arrayInventarios;
    }


    private class Listener implements DialogInterface.OnClickListener{

        @Override
        public void onClick(DialogInterface dialog, int which) {

            if(which == DialogInterface.BUTTON_POSITIVE){
                Log.v("listener","Positive");
            }


        }

    }

    private void dialogoConfirmacionSalida(){
        Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("¿Estas Seguro(a) que quieres Registrar tu Salida?");
        ListenerSAlida listener = new ListenerSAlida();
        builder.setPositiveButton("Aceptar",listener).setNegativeButton("Cancelar",listener);
        builder.create().show();

    }

    private class ListenerSAlida implements DialogInterface.OnClickListener{

        @Override
        public void onClick(DialogInterface dialog, int which) {

            if(which == DialogInterface.BUTTON_POSITIVE){
                salidaTienda();
                Log.v("listener","Positive");
            }


        }

    }



	
	
	

}
