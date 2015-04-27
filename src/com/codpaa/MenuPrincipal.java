package com.codpaa;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;




import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;

import com.codpaa.updates.UpdateInformation;

import BD.BDopenHelper;


public class MenuPrincipal extends Activity implements OnClickListener, LocationListener{
	
	
	TextView nombreUsuario, conexion, bien, version, cartera;
	Button btnC, btnTienda, btnRuta, btnEnviar, btnCajasM;
	ImageButton btnActua;
	Spinner spinnerTien;
	SQLiteDatabase base;
	LocationManager lM = null;
	String myVersionName = "not available";


	boolean GpsEncendido = false;

	int idUsuario;
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.menu);
		
		Intent recibe = getIntent();
		String valor = (String) recibe.getExtras().get("nombre");
		
	
		btnC = (Button) findViewById(R.id.cerrarApli);
	
		btnActua = (ImageButton) findViewById(R.id.imageBtnRefresh);
	
		bien = (TextView) findViewById(R.id.bienvenido);
		nombreUsuario = (TextView) findViewById(R.id.txtnomUser);
		conexion = (TextView) findViewById(R.id.txtconexion);
		version = (TextView) findViewById(R.id.textV);
		cartera = (TextView) findViewById(R.id.textCartera);
		
		
		btnTienda = (Button) findViewById(R.id.buttonTienda);
		btnRuta = (Button) findViewById(R.id.buttonRuta);
		btnEnviar = (Button) findViewById(R.id.buttonEnviar);
		btnCajasM = (Button) findViewById(R.id.btnGuCajasM);
		
		
		
		
		btnC.setOnClickListener(this);
		btnActua.setOnClickListener(this);
		
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
			
		}
		
		
		
		
		
		if(verificarConexion()){
			conexion.setText("Conexion");
			conexion.setBackgroundColor(Color.GREEN);


            UpdateInformation upinfo = new UpdateInformation(this);
            upinfo.actualizarTiendas(idUsuario);
            upinfo.actualizarRuta(idUsuario);



            Calendar c = Calendar.getInstance();
            SimpleDateFormat dFecha = new SimpleDateFormat("dd-MM-yyyy");
            String fechaActual = dFecha.format(c.getTime());

            BDopenHelper actuali = new BDopenHelper(this);


            //actualizarInfo();



            Cursor infoAc = actuali.infoActualizada("producto", fechaActual);
            //Log.d("Re Ac pro", Integer.toString(infoAc.getCount()));
            if(infoAc.getCount() <= 0){
                upinfo.actualizarExhibiciones();
                upinfo.actualizarMarca(idUsuario);
                upinfo.actualizarProducto(idUsuario);

            }

            actuali.close();
	
			/*try {

				
				
			} catch (JSONException e) {
				
				e.printStackTrace();
			}*/
			
		}else{
			conexion.setText("Sin conexion");
			conexion.setBackgroundColor(Color.RED);
			Toast.makeText(this, "No fue posible Actualizar, No hay conexion a Internet", Toast.LENGTH_SHORT).show();
		}
		
		
		borrarRegistros();
		
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
		
		case R.id.imageBtnRefresh:
			
			
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
			
			break;
			
			
		case R.id.cerrarApli:
			
			finish();
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
		

		
		TiendasCustomAdapter adap = new TiendasCustomAdapter(this, android.R.layout.simple_spinner_item, getArrayList());
		spinnerTien.setAdapter(adap);
		DListener listener = new DListener();
		
			
		builder.setPositiveButton("Aceptar Seleccion", listener).setNegativeButton("Cancelar", listener).setView(vistaLocal);
		
		
		builder.create().show();
		
		base.close();
	
		
	}
	
	private ArrayList<SpinnerTiendasModel> getArrayList(){
		
		base = new BDopenHelper(this).getReadableDatabase();
		String sql = "select idTienda as _id, grupo, sucursal from clientes order by grupo asc;";
		Cursor cursorTienda = base.rawQuery(sql, null);
		ArrayList<SpinnerTiendasModel> arrayT = new ArrayList<MenuPrincipal.SpinnerTiendasModel>();
		for(cursorTienda.moveToFirst(); !cursorTienda.isAfterLast(); cursorTienda.moveToNext()){
			
			final SpinnerTiendasModel spT = new SpinnerTiendasModel();
			spT.set_idTienda(cursorTienda.getInt(0));
			spT.set_nombre(cursorTienda.getString(1));
			spT.set_sucursal(cursorTienda.getString(2));
			
			arrayT.add(spT);			
		}
		
		final SpinnerTiendasModel spT2 = new SpinnerTiendasModel();
		spT2.set_idTienda(0);
		spT2.set_nombre("Seleccione Tienda");
		spT2.set_sucursal("Tienda no seleccionada");
		
		arrayT.add(0,spT2);
		
		base.close();
		return arrayT;
		
	}
	
	private void loadSpinner(){
		try {
			
			
			TiendasCustomAdapter adapter = new TiendasCustomAdapter(this, android.R.layout.simple_spinner_item, getArrayList());
			spinnerTien.setAdapter(adapter);
			
		}catch(Exception e) {
			Toast.makeText(this, "Error Mayoreo 3", Toast.LENGTH_SHORT).show();
		}
		
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
			SpinnerTiendasModel stm = (SpinnerTiendasModel) spinnerTien.getSelectedItem();
			//int idt = (int) spinnerTien.getItemIdAtPosition(posicion);
			int idTienda = stm.get_idTienda();
			if(which == DialogInterface.BUTTON_POSITIVE){
				
				
				
				if(stm.get_idTienda() != 0){
					
					
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


	
	
	
	
	public void gpsStart(){
		
		
		if(!GpsEncendido){
			
			lM.requestLocationUpdates(LocationManager.GPS_PROVIDER, 500, 10, this);
			GpsEncendido = true;
		

			
		}
		
		
	}
	
	
	@Override
	public void onLocationChanged(Location location) {
	
		
	}



	@Override
	public void onProviderDisabled(String provider) {
	
		
	}



	@Override
	public void onProviderEnabled(String provider) {

		
	}



	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		
		
	}
	
	
	private void borrarRegistros(){
		try {
			BDopenHelper base = new BDopenHelper(this);
			
			Calendar c = Calendar.getInstance();
			SimpleDateFormat dFecha = new SimpleDateFormat("dd-MM-yyyy");
			String fechaActual = dFecha.format(c.getTime());
			
			SimpleDateFormat dSem = new SimpleDateFormat("w");
			String semana = dSem.format(c.getTime());
			int sem = Integer.parseInt(semana);
			
			base.borrarInven(fechaActual, 2);
			base.borrarExhi(fechaActual, 2);
			base.borrarInteli(fechaActual, 2);
			base.borrarFrentes(fechaActual, 2);
			base.borrarCajasMa(fechaActual, 2);
            base.borrarFotos();
			base.borrarVisitas(sem);
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	private class SpinnerTiendasModel{
		
		private int _idTienda;
		private String _nombre;
		private String _sucursal;
		
		public int get_idTienda() {
			return _idTienda;
		}
		public void set_idTienda(int idTienda) {
			this._idTienda = idTienda;
		}
		public String get_nombre() {
			return _nombre;
		}
		public void set_nombre(String nombre) {
			this._nombre = nombre;
		}
		public String get_sucursal() {
			return _sucursal;
		}
		public void set_sucursal(String sucursal) {
			this._sucursal = sucursal;
		}

		
	}
	
	private class TiendasCustomAdapter extends ArrayAdapter<SpinnerTiendasModel>{
		Activity _context;
		private ArrayList<SpinnerTiendasModel> _datos;
		

		public TiendasCustomAdapter(Activity con, int textViewResourceId,ArrayList<SpinnerTiendasModel> objects) {
			super(con, textViewResourceId, objects);
			
			this._context= con;
			this._datos = objects;
			
		}

		@Override
		public View getDropDownView(int position, View convertView, ViewGroup parent) {
			 return getCustomView(position, convertView, parent);
			
		}
		
		
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			
			return getCustomView(position, convertView, parent);
		}

		public View getCustomView(int position, View convertView, ViewGroup parent){
			View row = convertView;
			
			if(row == null){
				LayoutInflater inflater = _context.getLayoutInflater();
				row = inflater.inflate(R.layout.custom_spinner_list, parent, false);
				
			}
			
			SpinnerTiendasModel temp = _datos.get(position);
			
			TextView name = (TextView) row.findViewById(R.id.txtCusSpi1);
			name.setText(temp.get_nombre());
			TextView sucursal = (TextView) row.findViewById(R.id.txtCusSpi2);
			sucursal.setText(temp.get_sucursal());
			if(position > 0){
				
				name.setTextColor(Color.BLUE);
			}


			
			
			return row;
		}
	}
	
	private void turnGPSOn(){
	    String provider = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);

	    
	}

	private void turnGPSOff(){
	    String provider = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);

	    if(provider.contains("gps")){ //if gps is enabled
	        final Intent poke = new Intent();
	        poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
	        poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
	        poke.setData(Uri.parse("3")); 
	        sendBroadcast(poke);


	   }
	}
	
	private void turnGPSOnoff(){
	     try{
	    	 String provider = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);

	    	 if(!provider.contains("gps")){
	    		 final Intent poke = new Intent();
	    		 poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider"); 
	    		 poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
	    		 poke.setData(Uri.parse("3"));  //SET 3 for gps,3 for bluthooth
	    		 sendBroadcast(poke);
	    	 }
	     }
	     catch(Exception e){
	    	 Log.d("Location", " exception thrown in enabling GPS "+e);
	     }
	 }
	
	private boolean isGPSEnable() {

        String str = Settings.Secure.getString(getContentResolver(),Settings.Secure.LOCATION_PROVIDERS_ALLOWED);


        return str != null && str.contains("gps");


    }

    public void actualizarInfo(){
        UpdateInformation uInf = new UpdateInformation(this);
        uInf.actualizarTiendas(idUsuario);
        uInf.actualizarRuta(idUsuario);
        uInf.actualizarExhibiciones();
    }




}
