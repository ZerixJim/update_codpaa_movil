package com.codpaa.activity;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;


import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;

import com.codpaa.R;
import com.codpaa.adapter.TiendasAdapter;
import com.codpaa.model.TiendasModel;
import com.codpaa.update.UpdateInformation;

import com.codpaa.db.BDopenHelper;
import com.codpaa.util.Configuracion;


public class MenuPrincipal extends AppCompatActivity implements OnClickListener, LocationListener{
	
	
	TextView nombreUsuario, conexion, bien, version, cartera;
	Button btnTienda, btnRuta, btnEnviar, btnCajasM;

	Spinner spinnerTien;
	SQLiteDatabase base;
	LocationManager lM = null;
	String myVersionName = "not available";
	Locale locale;

    Configuracion configuracion;


	boolean GpsEncendido = false;

	int idUsuario;

	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
        locale = new Locale("es_MX");
		setContentView(R.layout.menu);
		
		Intent recibe = getIntent();
		String valor = (String) recibe.getExtras().get("nombre");

	
		bien = (TextView) findViewById(R.id.bienvenido);
		nombreUsuario = (TextView) findViewById(R.id.txtnomUser);
		conexion = (TextView) findViewById(R.id.txtconexion);
		version = (TextView) findViewById(R.id.textV);
		cartera = (TextView) findViewById(R.id.textCartera);
		
		
		btnTienda = (Button) findViewById(R.id.buttonTienda);
		btnRuta = (Button) findViewById(R.id.buttonRuta);
		btnEnviar = (Button) findViewById(R.id.buttonEnviar);
		btnCajasM = (Button) findViewById(R.id.btnGuCajasM);

		
		btnTienda.setOnClickListener(this);
		btnRuta.setOnClickListener(this);
		btnEnviar.setOnClickListener(this);
	
		btnCajasM.setOnClickListener(this);


		Context context = getApplicationContext();
		PackageManager packageManager = context.getPackageManager();
		String packageName = context.getPackageName();
	

		try {
		    myVersionName = packageManager.getPackageInfo(packageName, 0).versionName;
		    bien.setText("Bienvenido(a)");
			version.setText("versión: "+myVersionName);
		} catch (PackageManager.NameNotFoundException e) {
		    e.printStackTrace();
		}
		
	
		
		lM = (LocationManager) getSystemService(LOCATION_SERVICE);
		
		
		
		try{
			
		
			
			BDopenHelper recibeDatos = new BDopenHelper(this);
			Cursor cursorDatosUser = recibeDatos.datosUser(valor);
			cursorDatosUser.moveToFirst();
			idUsuario = cursorDatosUser.getInt(0);
			nombreUsuario.setText(cursorDatosUser.getString(1));
			cartera.setText("Cartera: "+cursorDatosUser.getInt(0));
			recibeDatos.close();
			
			if(!isGPSEnable()){
		    	
				//turnGPSOn();
		    	//turnGPSOnoff();
		    	Log.v("GPS", "gps activando..");
			}else{
				Log.v("GPS", "ya esta activado");
			}
	
			
		}catch(SQLiteException e){
			Toast.makeText(this, "no se pudo asignar el usurio "+valor, Toast.LENGTH_SHORT).show();
			e.printStackTrace();
			
		}catch (CursorIndexOutOfBoundsException e){
			Toast.makeText(getApplicationContext(), "Salga de la aplicacion e inicie sesion de nuevo", Toast.LENGTH_SHORT).show();
			e.printStackTrace();
		}
		
		
		
		
		
		if(verificarConexion()){
			conexion.setText("Conexion");
			conexion.setBackgroundColor(Color.GREEN);

            updateInfo();


		}else{
			conexion.setText("Sin conexion");
			conexion.setBackgroundColor(Color.RED);
			Toast.makeText(this, "No fue posible Actualizar, No hay conexion a Internet", Toast.LENGTH_SHORT).show();
		}
		
		
		borrarRegistros();

		try {

			ActionBar actionBar = getSupportActionBar();
			if (actionBar != null){
				actionBar.setDisplayHomeAsUpEnabled(true);
				actionBar.setDisplayUseLogoEnabled(true);
				actionBar.setHomeButtonEnabled(true);
				actionBar.setLogo(R.drawable.ic_launcher);
			}

		}catch (NullPointerException e){
			e.printStackTrace();
		}

		//estadisticas();






	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_principal, menu);

		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()){
			case R.id.refresh_info:
				if(verificarConexion()){

					UpdateInformation upinfo = new UpdateInformation(this);
					Toast.makeText(this,"Actualizando Informacion",Toast.LENGTH_SHORT).show();
					upinfo.actualizarTiendas(idUsuario);
					upinfo.actualizarRuta(idUsuario);
					upinfo.actualizarExhibiciones();
					upinfo.actualizarMarca(idUsuario);
					upinfo.actualizarProducto(idUsuario);

				}else{

					Toast.makeText(this, "Se perdio la conexion a Internet", Toast.LENGTH_SHORT).show();
				}
				return true;
			case android.R.id.home:
				this.finish();
				return true;

			default:
				return super.onOptionsItemSelected(item);
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		
		// prepare
	    
	    int roundRadius = 15; // 8dp
	    int fillColor = Color.parseColor("#2AF110");
	    int fillColor2 = Color.parseColor("#FC2424");

	    GradientDrawable gd = new GradientDrawable();
	    GradientDrawable gd2 = new GradientDrawable();
	    
	    gd2.setColor(fillColor2);
	    gd2.setCornerRadius(roundRadius);
	  
	    gd.setColor(fillColor);
	    gd.setCornerRadius(roundRadius);
	    
	    if(!isGPSEnable()){
	    	
			//turnGPSOn();
	    	//turnGPSOnoff();
	    	Log.v("GPS", "gps activando..");
		}else{
			Log.v("GPS", "ya esta activado");
		}
		
		if(verificarConexion()){
			
			conexion.setText("Conexion");
			conexion.setBackgroundDrawable(gd);
			//conexion.setBackgroundColor(Color.GREEN);
		}else{
			conexion.setText("Sin conexion");
			conexion.setBackgroundDrawable(gd2);
			//conexion.setBackgroundColor(Color.RED);
		}

	}


    private void updateInfo(){
        configuracion = new Configuracion(this);
        UpdateInformation uI = new UpdateInformation(this);

        if (configuracion.getTiendas() != null){
            if (!configuracion.getTiendas().equals(fechaActual())){
                uI.actualizarTiendas(idUsuario);
            }else {
                Log.d("Shared", "tien:" + configuracion.getTiendas());
            }
        }else {
            uI.actualizarTiendas(idUsuario);
        }

        if (configuracion.getRuta() != null){
            if (!configuracion.getRuta().equals(fechaActual())){
              uI.actualizarRuta(idUsuario);
            }else {
                Log.d("Shared", "ruta:" +configuracion.getRuta());
            }
        }else {
            uI.actualizarRuta(idUsuario);
        }

        if (configuracion.getExhi() != null){
            if (!configuracion.getExhi().equals(fechaActual())){
                uI.actualizarExhibiciones();
            }else {
                Log.d("Shared", "exhi:" +configuracion.getExhi());
            }
        }else {
            uI.actualizarExhibiciones();
        }

        if (configuracion.getMarca() != null){
            if (!configuracion.getMarca().equals(fechaActual())){
               uI.actualizarMarca(idUsuario);
            }else {
                Log.d("Shared", "marca:" + configuracion.getMarca());
            }
        }else {
            uI.actualizarMarca(idUsuario);
        }

        if (configuracion.getProducto() != null){
            if (!configuracion.getProducto().equals(fechaActual())){
                uI.actualizarProducto(idUsuario);

            }else {
                Log.d("Shared", "pro:" + configuracion.getProducto());
            }
        }else {
            uI.actualizarProducto(idUsuario);
        }


    }



	@Override
	protected void onStart() {
		super.onStart();

		
	}
	
	

	@Override
	protected void onDestroy() {
		super.onDestroy();
		
		lM.removeUpdates(this);
		
	}
	



	@Override
	public void onClick(View v) {
		
		switch(v.getId()){
		
		case R.id.buttonTienda:
			crearD();
			break;
		case R.id.buttonRuta:
			calendario();
			break;
		

		case R.id.buttonEnviar:
			
			Intent in = new Intent(this, EnviarInformacion.class);
			startActivity(in);
			
			break;
		case R.id.btnGuCajasM:
			Intent i = new Intent(this, Mayoreo.class);
			i.putExtra("idCelular", idUsuario);
			startActivity(i);
			break;
			
		}
		
	}

	@Override
	protected void onPause() {
		super.onPause();
		lM.removeUpdates(this);
		GpsEncendido = false;
		
	}
	
	
	
	private void crearD() {
		
		
		Builder builder  = new AlertDialog.Builder(this);

		
		
		View vistaLocal = LayoutInflater.from(this).inflate(R.layout.listatiendaspinner, null);
		
		spinnerTien = (Spinner) vistaLocal.findViewById(R.id.spinnerTiendas);
		

		
		TiendasAdapter adap = new TiendasAdapter(this, android.R.layout.simple_spinner_item, getArrayList());
		spinnerTien.setAdapter(adap);
		DListener listener = new DListener();
		
			
		builder.setPositiveButton("Aceptar Seleccion", listener).setNegativeButton("Cancelar", listener).setView(vistaLocal);
		
		
		builder.create().show();
		
		base.close();
	
		
	}
	
	private ArrayList<TiendasModel> getArrayList(){
		
		base = new BDopenHelper(this).getReadableDatabase();
		String sql = "select idTienda as _id, grupo, sucursal from clientes order by grupo asc;";
		Cursor cursorTienda = base.rawQuery(sql, null);
		ArrayList<TiendasModel> arrayT = new ArrayList<>();
		for(cursorTienda.moveToFirst(); !cursorTienda.isAfterLast(); cursorTienda.moveToNext()){
			
			final TiendasModel spT = new TiendasModel();
			spT.setIdTienda(cursorTienda.getInt(0));
			spT.setNombre(cursorTienda.getString(1));
			spT.setSucursal(cursorTienda.getString(2));
			
			arrayT.add(spT);			
		}
		
		final TiendasModel spT2 = new TiendasModel();
		spT2.setIdTienda(0);
		spT2.setNombre("Seleccione Tienda");
		spT2.setSucursal("Tienda no seleccionada");
		
		arrayT.add(0, spT2);

		cursorTienda.close();
		base.close();
		return arrayT;
		
	}
	

	
	
	
	
	private void calendario() {
		Intent intenCalen = new Intent(this,MostrarCalendario.class);
		intenCalen.putExtra("idPromotor", idUsuario);
		startActivity(intenCalen);
	}
	
	
	
	public boolean verificarConexion() {
	    ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo netInfo = cm.getActiveNetworkInfo();
	    return netInfo != null && netInfo.isConnected();

	}
	
	
	
	public class DListener implements DialogInterface.OnClickListener{

		@Override
		public void onClick(DialogInterface dialog, int which) {
			
			//int posicion = spinnerTien.getSelectedItemPosition();
			TiendasModel stm = (TiendasModel) spinnerTien.getSelectedItem();
			//int idt = (int) spinnerTien.getItemIdAtPosition(posicion);
			int idTienda = stm.getIdTienda();
			if(which == DialogInterface.BUTTON_POSITIVE){
				
				
				
				if(stm.getIdTienda() != 0){
					
					
					Intent abrirMenuTienda = new Intent(MenuPrincipal.this , MenuTienda.class);
					
					abrirMenuTienda.putExtra("idTienda", idTienda);
					abrirMenuTienda.putExtra("idPromotor", idUsuario);
					
					startActivity(abrirMenuTienda);
				}else{
					Toast.makeText(MenuPrincipal.this, "No seleccionaste tienda", Toast.LENGTH_LONG).show();
				}
				
				
				
			}else if(which == DialogInterface.BUTTON_NEGATIVE){
				Toast.makeText(getApplicationContext(),"Cancelaste la Seleccion", Toast.LENGTH_SHORT).show();
			}
						
				
			
		}
		
	}


    private String fechaActual(){
        Calendar c = Calendar.getInstance();
        SimpleDateFormat dFecha = new SimpleDateFormat("dd-MM-yyyy", locale);
        return dFecha.format(c.getTime());
    }
	
	
	@Override
	public void onLocationChanged(Location location) {}


	@Override
	public void onProviderDisabled(String provider) {}


	@Override
	public void onProviderEnabled(String provider) {}


	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {}
	
	
	private void borrarRegistros(){
		try {
			BDopenHelper base = new BDopenHelper(this);
			
			Calendar c = Calendar.getInstance();
			SimpleDateFormat dFecha = new SimpleDateFormat("dd-MM-yyyy", locale);
			String fechaActual = dFecha.format(c.getTime());
			
			//SimpleDateFormat dSem = new SimpleDateFormat("w");
			//String semana = dSem.format(c.getTime());
			//int sem = Integer.parseInt(semana);
			
			base.borrarInven(fechaActual, 2);
			base.borrarExhi(fechaActual, 2);
			base.borrarInteli(fechaActual, 2);
			base.borrarFrentes(fechaActual, 2);
			base.borrarCajasMa(fechaActual, 2);
            base.borrarFotos(fechaActual);
			base.borrarVisitas(fechaActual, 2);
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	


	
	private boolean isGPSEnable() {

        String str = Settings.Secure.getString(getContentResolver(),Settings.Secure.LOCATION_PROVIDERS_ALLOWED);


        return str != null && str.contains("gps");


    }



	/*public void estadisticas(){

        int uid = getApplication().getApplicationInfo().uid;


		double totalBytes = (double) TrafficStats.getTotalRxBytes() + TrafficStats.getTotalTxBytes();
		double mobileBytes = TrafficStats.getMobileRxBytes() + TrafficStats.getMobileTxBytes();
        double datosCodpaa = (TrafficStats.getUidRxBytes(uid) + TrafficStats.getUidTxBytes(uid)) / 1000000.0;
		totalBytes -= mobileBytes;
		totalBytes /= 1000000;
		mobileBytes /= 1000000;

		NumberFormat nf = new DecimalFormat("#.##");
		String totalStr = nf.format(totalBytes);
		String mobileStr = nf.format(mobileBytes);
        String codpaaStr = nf.format(datosCodpaa);
		String info = String.format("\tWifi Data Usage: %s MB\tMobile Data Usage, %s mb \tCodpaa: %s mb", totalStr, mobileStr, codpaaStr );
		Log.d("DATOS", info);
	}*/




}
