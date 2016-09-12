package com.codpaa.activity;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;


import com.codpaa.adapter.ExhibicionesAdapter;
import com.codpaa.fragment.DialogFragmentFotos;
import com.codpaa.model.ExhibicionesModel;
import com.codpaa.update.EnviarDatos;
import com.codpaa.R;
import com.codpaa.update.UpdateInformation;
import com.loopj.android.http.*;

import android.Manifest;
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
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.codpaa.db.BDopenHelper;
import com.codpaa.adapter.FrentesCustomAdapter;
import com.codpaa.adapter.InventariosCustomAdapter;
import com.codpaa.model.FrentesModel;
import com.codpaa.model.InventarioModel;

public class MenuTienda extends AppCompatActivity implements OnClickListener{
	
	
	Button btnSalidaTi,btnEntrada, btnEncar, btnExhib, btnInven, btnFrente, btnSurtido, btnTiendaError;
	Button btnVentaPromedio, btnCapturaGeneral;
	Button btnComentario, btnInteligencia, btnUpdaPro, btnFoto;
	SQLiteDatabase base = null;
	Location locGps,locNet;
	LocationManager localizar = null;
	TextView txtEncargado, frentes, surtido, exhi,inventario, fotos;
	int idPromotor, idTienda;
	String gpsProvedor, netProvedor;
	BDopenHelper DB = null;
	String myVersionName = "not available";
	EnviarDatos enviar;
	Locale locale;
	Spinner spinnerEnc;
	EditText editNombre;
	AsyncHttpClient cliente;
	RequestParams rp;
	String nombreTienda;

	Toolbar toolbar;
	private Boolean Salida = false;
	private Boolean Entrada = false;


	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.menutienda);

		toolbar = (Toolbar) findViewById(R.id.toolbar_menu_principal);


		if(toolbar != null){

			setSupportActionBar(toolbar);
			ActionBar actionBar = getSupportActionBar();
			if (actionBar != null){
				actionBar.setDisplayHomeAsUpEnabled(true);

			}
		}

		locale = new Locale("es_MX");
		cliente = new AsyncHttpClient();
		rp = new RequestParams();
		Intent recibeIdTi = getIntent();
		enviar = new EnviarDatos(this);

		idTienda = recibeIdTi.getIntExtra("idTienda",0);
		idPromotor = recibeIdTi.getIntExtra("idPromotor",0);
		//idTipoTienda = recibeIdTi.getIntExtra("idTipo", 0);


		//registrar views
		viewsRegister();




		DB = new BDopenHelper(this);



        //fotos.setText("fotos(0)");

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


		try {

			localizar.requestLocationUpdates(netProvedor, 30000, 10, new localizacion());
		}catch (SecurityException e){
			e.printStackTrace();
		}



		try{

			Cursor cTienda = DB.tienda(idTienda);
			cTienda.moveToFirst();

			nombreTienda = cTienda.getString(0) + " " + cTienda.getString(1);


			if (getSupportActionBar() != null)
				getSupportActionBar().setTitle(nombreTienda);
			cTienda.close();

			try {

				Log.d("idPromotor", " "+idPromotor);

				Cursor cNomPromo = DB.nombrePromotor(idPromotor);
				cNomPromo.moveToFirst();

				if(getSupportActionBar() != null)
					getSupportActionBar().setSubtitle(cNomPromo.getString(0));
				cNomPromo.close();



			}catch(Exception e) {
				Toast.makeText(this, "error menuTienda 2", Toast.LENGTH_SHORT).show();
			}



		}catch(Exception e){

			Toast.makeText(this, "error", Toast.LENGTH_SHORT).show();
		}finally {
			DB.close();
		}



		
	}


	private void viewsRegister(){

		inventario = (TextView) findViewById(R.id.inventario);
		txtEncargado = (TextView) findViewById(R.id.Encargado);
		frentes = (TextView) findViewById(R.id.frentes);
		surtido = (TextView) findViewById(R.id.surtido);
		exhi = (TextView) findViewById(R.id.textExhibicio);
		fotos = (TextView) findViewById(R.id.text_fotos);

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
		btnVentaPromedio = (Button) findViewById(R.id.btn_venta_promedio);

		btnCapturaGeneral = (Button) findViewById(R.id.captura_general);


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
		btnVentaPromedio.setOnClickListener(this);

		btnCapturaGeneral.setOnClickListener(this);



		// textView with listener
		frentes.setOnClickListener(this);
		inventario.setOnClickListener(this);
		exhi.setOnClickListener(this);
		fotos.setOnClickListener(this);

	}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();

        inflater.inflate(R.menu.menu_tienda, menu);


        return true;
    }

    @Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()){
			case android.R.id.home:

				if(!Entrada && !Salida){
					this.finish();
				}else {
					dialogoConfirmacionSalida();
				}

				return true;

            case R.id.direccion:

                saveAddress();

                return true;

			case R.id.producto_datos_tienda:

				saveDatosTienda();

				return true;

			default:
				return super.onOptionsItemSelected(item);

		}

	}

	private void saveDatosTienda() {

		Intent i = new Intent(this, TiendaDatos.class);
		i.putExtra("idTienda", idTienda);
		i.putExtra("idPromotor", idPromotor);

		startActivity(i);
	}

	private void saveAddress() {

        Intent i = new Intent(this, AddressActivity.class);
        i.putExtra("idTienda", idTienda);
        i.putExtra("idPromotor", idPromotor);

        startActivity(i);

    }


    public void entradaTienda() {

        permiso();

		Calendar c = Calendar.getInstance();
		SimpleDateFormat dFecha = new SimpleDateFormat("dd-MM-yyyy",locale);
		SimpleDateFormat dHora = new SimpleDateFormat("HH:mm:ss a",locale);
		SimpleDateFormat dSema = new SimpleDateFormat("w",locale);
			
		String fecha = dFecha.format(c.getTime());
		String hora = dHora.format(c.getTime());
		String sem = dSema.format(c.getTime());
		int semana = Integer.parseInt(sem);
		
		
		if(!Entrada){
			btnTiendaError.setVisibility(View.GONE);
			

			try {

				locGps = localizar.getLastKnownLocation(gpsProvedor);
				locNet = localizar.getLastKnownLocation(netProvedor);
			}catch (SecurityException e){
				e.printStackTrace();
			}

			BDopenHelper base = new BDopenHelper(getApplicationContext());
				
		
			if(locGps != null) {
				
				Entrada = true;
				base.insertarLocalizacion(idTienda, idPromotor, fecha ,hora ,locGps.getLatitude(), locGps.getLongitude(), 12, "E",1,semana);

				Toast.makeText(this, "Entrada Guardada", Toast.LENGTH_SHORT).show();
				btnEntrada.post(new Runnable() {
					
					public void run() {
							
						
						btnEntrada.setBackgroundResource(R.drawable.custom_btn_dark_khaki);
                        btnEntrada.setTextColor(Color.WHITE);
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
				
				Toast.makeText(this, "No fue posible registrar la Entrada" +
						" \n -Verifique que el Gps esta activado \n - De lo contrario comuniquese con Mesa de control", Toast.LENGTH_LONG).show();
				base.close();
			}
		}
	
				
	}

    private void permiso(){

        final int REQUEST_CODE_ASK_PERMISSIONS = 123;

        int locationPermission = ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION);
        if (locationPermission != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_CODE_ASK_PERMISSIONS);

        }


    }
			
		
	

	private void salidaTienda() {
		
		if(Entrada){
			Thread hiloSalida = new Thread() {
				public void run() {
	
					
						if(!Salida){
							
							Calendar c = Calendar.getInstance();
							SimpleDateFormat dFecha = new SimpleDateFormat("dd-MM-yyyy",locale);
							SimpleDateFormat dHora = new SimpleDateFormat("HH:mm:ss a",locale);
							SimpleDateFormat dSema = new SimpleDateFormat("w",locale);
								
							String fecha = dFecha.format(c.getTime());
							String hora = dHora.format(c.getTime());
							String sem = dSema.format(c.getTime());
							int semana = Integer.parseInt(sem);


							try{

								locGps = localizar.getLastKnownLocation(gpsProvedor);
								locNet = localizar.getLastKnownLocation(netProvedor);
							}catch (SecurityException e){
								e.printStackTrace();
							}


							BDopenHelper base = new BDopenHelper(getApplicationContext());
							
							
							
							if(locGps != null) {
								
								base.insertarLocalizacion(idTienda, idPromotor, fecha ,hora ,locGps.getLatitude(), locGps.getLongitude(), 12, "S",1,semana);
							
								Salida = true;
								
								btnSalidaTi.post(new Runnable() {
									public void run() {
										
										
										btnSalidaTi.setBackgroundResource(R.drawable.custom_btn_dark_khaki);
                                        btnSalidaTi.setTextColor(Color.WHITE);
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
			    //entradaTienda();

				if (!Entrada){
					dialogoConfirmarEntrada();
				}else {
					Toast.makeText(this, "Ya existe una Entrada", Toast.LENGTH_SHORT).show();
				}
			
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
            case R.id.textExhibicio:
                dialogoExhibiciones();
                break;
            case R.id.text_fotos:
                dialogoFotos();
                break;

            case R.id.btn_venta_promedio:
                subMenuVenta();
                break;

			case R.id.captura_general:
				capturaGeneral();
				break;
		}
		
	}

    private void subMenuVenta() {

		if (Entrada){
			Intent i = new Intent(this, VentaPromedio.class);
			i.putExtra("idPromotor", idPromotor);
			i.putExtra("idTienda", idTienda);
			startActivity(i);
		}else {
			Toast.makeText(this, "No has registrado Entrada", Toast.LENGTH_SHORT).show();
		}


    }

    private void dialogoFotos() {

        FragmentManager fm = getSupportFragmentManager();
        DialogFragmentFotos dialog = new DialogFragmentFotos();
        Bundle bundle = new Bundle();
        bundle.putInt("idTienda", idTienda);
        dialog.setArguments(bundle);

        dialog.show(fm, "Dialog fotos");

    }


    private void actualizarPro() {
        UpdateInformation updateInformation = new UpdateInformation(this);
        updateInformation.actualizarMarca(idPromotor);
        updateInformation.actualizarProducto(idPromotor);
        updateInformation.actualizarExhibiciones(idPromotor);
		
		
	}

	@Override
	protected void onResume() {
		super.onResume();
		
		Calendar c = Calendar.getInstance();
		SimpleDateFormat dFecha = new SimpleDateFormat("dd-MM-yyyy",locale);
		String fecha = dFecha.format(c.getTime());
		
		try {
			
			
			Cursor cuEncargados = DB.contadorEncargados(idTienda, fecha);
			//txtEncargado.setText("Encargado ("+cuEncargados.getCount()+")");
			txtEncargado.setText(String.format(Locale.getDefault(),"Encargado %d",cuEncargados.getCount()));
			DB.close();
			
			
			try {
				Cursor cuFrentes = DB.contadorFrentes(idTienda, fecha);
				//frentes.setText("Frentes: ("+cuFrentes.getCount()+")");
				frentes.setText(String.format(Locale.getDefault(), "Frentes %d", cuFrentes.getCount()));
				DB.close();
				
				
				try {
					Cursor cuSurt = DB.SurtidoCantidad(idTienda, fecha);
					//surtido.setText("Surtido: ("+cuSurt.getCount()+")");
					surtido.setText(String.format(Locale.getDefault(), "Surtido %d", cuSurt.getCount()));
					DB.close();
					
					try {
						Cursor cuInventario = DB.contarInventario(idTienda, fecha);
						//inventario.setText("Inventario ("+cuInventario.getCount()+")");
						inventario.setText(String.format(Locale.getDefault(), "Inventario %d", cuInventario.getCount()));
						DB.close();


						try {

							exhi.setText(String.format(Locale.getDefault(), "Exhibiciones %d", DB.contarExhibiciones(idTienda, fecha)));
						}catch (Exception e){
							e.printStackTrace();
						}

						try {
							fotos.setText(String.format(Locale.getDefault(),"Fotos %d", DB.contarFotos(idTienda)));

						}catch (Exception e){
							e.printStackTrace();
						}


						try {
							Cursor cuEntra = DB.VisitaTienda(idTienda, fecha, "E");

							if(cuEntra.getCount() >0) {
								
								
								btnEntrada.setBackgroundResource(R.drawable.custom_btn_dark_khaki);
								Entrada = true;
								DB.close();
								
							}else {

								btnEntrada.setBackgroundResource(R.drawable.custom_btn_orange);
								btnEntrada.setTextColor(Color.WHITE);
								Entrada = false;
								
							}
							try {
								Cursor cuSalida = DB.VisitaTienda(idTienda, fecha, "S");

								//auto time
								Settings.System.putInt(getContentResolver(), Settings.System.AUTO_TIME,1);

								
								if(cuSalida.getCount() >0) {
									
									
									btnSalidaTi.setBackgroundResource(R.drawable.custom_btn_dark_khaki);
									Salida = true;
									DB.close();
									
									if(Entrada && Salida)
										btnTiendaError.setVisibility(View.GONE);
								}else {
									
									btnSalidaTi.setBackgroundResource(R.drawable.custom_btn_orange);
									btnSalidaTi.setTextColor(Color.WHITE);
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


		if (new BDopenHelper(this).tipoTienda(idTienda) ==2){
			if (!verifyCatalogo()){
				dialogCapturaDeCatalogo();
			}
		}


		
		
	}



	@Override
	protected void onStart() {
		super.onStart();
		
		
		
	}
	
	private void menuInventario() {
		if(Entrada){
			Intent i = new Intent(this, Inventario.class);
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


	private void capturaGeneral(){
		if(Entrada){
			Intent i = new Intent(this, CapturaGeneral.class);
			i.putExtra("idTienda", idTienda);
			i.putExtra("idPromotor", idPromotor);
			startActivity(i);
		}else{
			Toast.makeText(this, "No has registrado Entrada", Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	protected void onPause() {
		super.onPause();

	}

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (DB != null){
            DB.close();
        }

    }

    private void dialogoEncargado() {
		
		
		Builder builder  = new AlertDialog.Builder(this);
		View vistaEncargado = LayoutInflater.from(this).inflate(R.layout.encargadotienda, null);
		String[] tipoEncargado = new String[] {"ENCARGADO","AUXILIAR"};
		spinnerEnc = (Spinner) vistaEncargado.findViewById(R.id.spinnerMarca);
		editNombre = (EditText) vistaEncargado.findViewById(R.id.nombreEnca);
		
		ArrayAdapter<String> adapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item, tipoEncargado);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerEnc.setAdapter(adapter);
		
		EscucharDialogoEncargado listener = new EscucharDialogoEncargado();
		
		builder.setTitle("Selecciona Encargado")
				.setIcon(R.drawable.ic_timer_auto_grey600_24dp)
				.setPositiveButton("Guardar", listener)
				.setNegativeButton("Cancelar", listener)
				.setView(vistaEncargado);
		builder.create().show();
		
	}


	private void dialogCapturaDeCatalogo(){

		Builder builder = new AlertDialog.Builder(this);

		builder.setTitle("Catalogo de Producto");
		builder.setMessage("Es necesario que captures los productos que tienes catalogados en la Tienda");
		builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialogInterface, int i) {
				saveDatosTienda();

			}
		});

		builder.setNegativeButton("Cancelar", null);

		builder.create().show();

	}

	private boolean verifyCatalogo(){


		Calendar c = Calendar.getInstance();

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM", Locale.getDefault());

		String anioMes = format.format(c.getTime());

		String sql = "select * from tienda_productos_catalogo as tpc " +
				"left join clientes as c on c.idTienda=tpc.idTienda " +
				" where " +
				" strftime('%Y-%m', tpc.fecha) = '"+anioMes+"'  and tpc.idTienda="+idTienda;

		SQLiteDatabase db = new BDopenHelper(this).getReadableDatabase();
		Cursor cursor = db.rawQuery(sql, null);

		if (cursor.getCount() > 0){
			cursor.close();
			return true;
		} else {

			cursor.close();
			return false;
		}

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
				SimpleDateFormat dFecha = new SimpleDateFormat("dd-MM-yyyy",locale);

				
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

        listView.setAdapter(adapter);


        builder.setPositiveButton("Cerrar",flistener).setView(frentes);

        builder.create().show();
    }


    private void dialogoExhibiciones(){
        Builder builder = new AlertDialog.Builder(this);
        LayoutInflater layoutInflater =  (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View exhiList = layoutInflater.inflate(R.layout.list_view_exhi,null);
        Listener flistener = new Listener();
        ListView listView = (ListView) exhiList.findViewById(R.id.list_exhi);

    	ExhibicionesAdapter adapter = new ExhibicionesAdapter(this, R.layout.row_exhi, getExhibicionesByIdTienda(idTienda));

        listView.setAdapter(adapter);


        builder.setPositiveButton("Cerrar",flistener).setView(exhiList);

        builder.create().show();


    }

    private ArrayList<FrentesModel> getFrentesCapturados(){
        base = new BDopenHelper(this).getReadableDatabase();
        String sql = "select m.nombre, p.nombre || ' ' || p.presentacion, (f.cha1+f.cha2+f.cha3+f.cha4+f.cha5+f.cha6) as total, f.status, f.fecha," +
				" (f.unifila+f.fila1+f.fila2+f.fila3+f.fila4+f.fila5+f.fila6+f.fila7+f.fila8+f.fila9+f.fila10+f.fila11+f.fila12+f.fila13+f.fila14) as filas " +
                "from frentesCharola as f inner join marca as m on f.idMarca=m.idMarca " +
                "inner join producto as p on f.idProducto=p.idProducto where f.idTienda="+idTienda;
        Cursor frentes = base.rawQuery(sql,null);
        ArrayList<FrentesModel> arrayFrentes = new ArrayList<>();
        Log.v("Cursor", "CAntidad: " + frentes.getCount());
        if(frentes.getCount() > 0){
            for (frentes.moveToFirst();!frentes.isAfterLast();frentes.moveToNext()){
                final FrentesModel fm = new FrentesModel();
                fm.setMarca(frentes.getString(0));
                fm.setProducto(frentes.getString(1));
                fm.setCantidad(frentes.getInt(2));
                fm.setStatus(frentes.getInt(3));
                fm.setFecha(frentes.getString(4));
				fm.setFilas(frentes.getInt(5));


                arrayFrentes.add(fm);
            }
        }else {
            final FrentesModel fm = new FrentesModel();
            fm.setMarca("No Existen Registros");
            arrayFrentes.add(fm);
        }
		frentes.close();
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
        ArrayList<InventarioModel> arrayInventarios = new ArrayList<>();
        Log.v("Cursor", "CAntidad: " + inventarios.getCount());
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
		inventarios.close();
        base.close();

        return arrayInventarios;
    }

	private ArrayList<ExhibicionesModel> getExhibicionesByIdTienda(int idTienda){
		base = new BDopenHelper(this).getReadableDatabase();
        ArrayList<ExhibicionesModel> arrayList = new ArrayList<>();
        String sql = "select m.nombre as marca, p.nombre as producto,te.nombre as tipo, e.cantidad as cantidad " +
                "from exhibiciones as e " +
                "left join tipoexhibicion as te on te.idExhibicion=e.idExhibicion " +
                "left join producto as p on p.idProducto=e.idProducto " +
                "left join marca as m on m.idMarca=p.idMarca " +
                "where e.idTienda="+idTienda;

        Cursor exhibiciones = base.rawQuery(sql, null);
        if (exhibiciones.getCount() > 0){
            for (exhibiciones.moveToFirst(); !exhibiciones.isAfterLast(); exhibiciones.moveToNext()){
                final ExhibicionesModel em = new ExhibicionesModel();
                em.setMarca(exhibiciones.getString(exhibiciones.getColumnIndex("marca")));
                em.setProducto(exhibiciones.getString(exhibiciones.getColumnIndex("producto")));
                em.setCantidad(exhibiciones.getString(exhibiciones.getColumnIndex("cantidad")));
                em.setTipo(exhibiciones.getString(exhibiciones.getColumnIndex("tipo")));

                arrayList.add(em);
            }
        }else {
            final ExhibicionesModel em = new ExhibicionesModel();
            em.setMarca("No existen registros");
            arrayList.add(em);
        }

        exhibiciones.close();
        base.close();

        return arrayList;
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

		if (Salida){
			this.finish();
		}else {
			if (Entrada){
				Builder builder = new AlertDialog.Builder(this);
				builder.setMessage("¿Estas Seguro(a) que quieres Registrar tu Salida?");
				ListenerSAlida listener = new ListenerSAlida();
				builder.setPositiveButton("Aceptar", listener).setNegativeButton("Cancelar", listener);
				builder.create().show();
			}else {
				Toast.makeText(this,"Entrada no Registrada", Toast.LENGTH_SHORT).show();
			}

		}
    }

	private void dialogoConfirmarEntrada(){

		Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("¿Estas seguro(a) que quieres Registrar tu Entrada en \n " + nombreTienda + "?");
		builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {

				entradaTienda();

			}
		}).setNegativeButton("Tienda Incorrecta", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				MenuTienda.this.finish();
			}
		}).create().show();

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
