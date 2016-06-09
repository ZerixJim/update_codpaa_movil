package com.codpaa.activity;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.regex.Pattern;


import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
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
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Patterns;
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
import com.codpaa.model.TiendasModel;
import com.codpaa.service.RegistrationIntentService;
import com.codpaa.update.UpdateInformation;

import com.codpaa.db.BDopenHelper;
import com.codpaa.util.Configuracion;
import com.codpaa.util.QuickstartPreferences;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;


public class MenuPrincipal extends AppCompatActivity implements OnClickListener, LocationListener{

    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static final String TAG = "MainActivity";
	Button btnRuta, btnEnviar, btnCajasM, btnMensaje;
	private final int MY_PERMISSION_GET_ACCOUNDS = 126;

	private DrawerLayout drawerLayout;

	Spinner spinnerTien;
	SQLiteDatabase base;
	LocationManager lM = null;
	String myVersionName = "not available";
	Locale locale;
    Configuracion configuracion;

    private BroadcastReceiver mRegistrationBroadcastReceiver;

    private boolean isReceiverRegistered;
	

	int idUsuario;

	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
        locale = new Locale("es_MX");
		setContentView(R.layout.menu_principal);

		
		Intent recibe = getIntent();
		String valor = (String) recibe.getExtras().get("nombre");



		btnRuta = (Button) findViewById(R.id.buttonRuta);
		btnEnviar = (Button) findViewById(R.id.buttonEnviar);
		btnCajasM = (Button) findViewById(R.id.btnGuCajasM);
        btnMensaje = (Button) findViewById(R.id.buttonMensaje);



		

		btnRuta.setOnClickListener(this);
		btnEnviar.setOnClickListener(this);
	
		btnCajasM.setOnClickListener(this);

        btnMensaje.setOnClickListener(this);


		Context context = getApplicationContext();
		PackageManager packageManager = context.getPackageManager();
		String packageName = context.getPackageName();



	

		try {
		    myVersionName = packageManager.getPackageInfo(packageName, 0).versionName;
		} catch (PackageManager.NameNotFoundException e) {
		    e.printStackTrace();
		}
		
	
		
		lM = (LocationManager) getSystemService(LOCATION_SERVICE);
		
		
		
		try{
			
		
			
			BDopenHelper recibeDatos = new BDopenHelper(this);
			Cursor cursorDatosUser = recibeDatos.datosUser(valor);
			cursorDatosUser.moveToFirst();
			idUsuario = cursorDatosUser.getInt(0);


            drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

            if (navigationView != null){
                setupDrawerContent(navigationView);

                if (ContextCompat.checkSelfPermission(this, Manifest.permission.GET_ACCOUNTS) != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.GET_ACCOUNTS},
                            MY_PERMISSION_GET_ACCOUNDS);
                }

                View view = navigationView.getHeaderView(0);
                TextView nomPromo = (TextView) view.findViewById(R.id.user_name);
                TextView email = (TextView) view.findViewById(R.id.user_mail);
                TextView ver = (TextView) view.findViewById(R.id.version);
                TextView id = (TextView) view.findViewById(R.id.id);

                id.setText(String.format(Locale.getDefault(),"ID: %d", cursorDatosUser.getInt(0)));
                ver.setText(String.format("versión: %s", myVersionName));
                nomPromo.setText(cursorDatosUser.getString(1));

                if (getUserName() != null){
                    email.setText(getUserName());
                }

            }


			recibeDatos.close();
			

	
			
		}catch(SQLiteException e){
			Toast.makeText(this, "no se pudo asignar el usurio "+valor, Toast.LENGTH_SHORT).show();
			e.printStackTrace();
			
		}catch (CursorIndexOutOfBoundsException e){
			Toast.makeText(getApplicationContext(), "Salga de la aplicacion e inicie sesion de nuevo", Toast.LENGTH_SHORT).show();
			e.printStackTrace();
		}
		
		
		
		
		
		if(verificarConexion()){

            updateInfo();

		}else{

			Toast.makeText(this, "No fue posible Actualizar, No hay conexion a Internet", Toast.LENGTH_SHORT).show();
		}
		
		
		borrarRegistros();

        setToolbar();





        /* implementacion de BroadCast para el registro de Gcm */
		mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                SharedPreferences sharedPreferences =
                        PreferenceManager.getDefaultSharedPreferences(MenuPrincipal.this);
                boolean sentToken =
                        sharedPreferences.getBoolean(QuickstartPreferences.SENT_TOKEN_TO_SERVER, false);

                if (sentToken) {
                    Log.d("Register", "Registrado");
                } else {

                    Log.d("Register", " no se pudo registrar");
                }
            }
        };

		//Registro de Brodcast para verificar si el token fue enviado al servidor
        registerReceiver();
		//estadisticas();


        //intent para registrar nuestro dispositivo en gcm
        if (checkPlayServices()){
            Intent intent = new Intent(this, RegistrationIntentService.class);

			intent.putExtra("idPromo", idUsuario);

            startService(intent);

            //Log.d("MenuPrincipal", "Servicio iniciado");
        }


	}

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case MY_PERMISSION_GET_ACCOUNDS:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    // TODO: 26/05/2016 implementar cambios
                }
        }
    }

    //metodo para registrar broadcast
    private void registerReceiver(){
		//Log.d("MenuPrincipal", "RegisterReciver");
        if(!isReceiverRegistered) {
            LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                    new IntentFilter(QuickstartPreferences.REGISTRATION_COMPLETE));
            isReceiverRegistered = true;

            //Log.d("MenuPrincipal", "RegisterRecive2");
        }
    }

    //metodo para implementar el toolbar
	private void setToolbar(){
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);
			actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
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
                    upinfo.actualizarExhibiciones(idUsuario);
                    upinfo.actualizarMarca(idUsuario);
                    upinfo.actualizarProducto(idUsuario);

				}else{

					Toast.makeText(this, "Se perdio la conexion a Internet", Toast.LENGTH_SHORT).show();
				}
				return true;
			case android.R.id.home:

				drawerLayout.openDrawer(GravityCompat.START);
				//this.finish();
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
	    


        registerReceiver();

		if (new BDopenHelper(this).countMessege() > 0){
            btnMensaje.setBackgroundColor(getResources().getColor(R.color.alert_color));
            btnMensaje.setTextColor(Color.WHITE);
        }else {
            btnMensaje.setBackgroundColor(Color.WHITE);
            btnMensaje.setTextColor(Color.BLACK);

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
                uI.actualizarExhibiciones(idUsuario);
            }else {
                Log.d("Shared", "exhi:" +configuracion.getExhi());
            }
        }else {
            uI.actualizarExhibiciones(idUsuario);
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

		uI.actualizarEncuesta(idUsuario);


    }



	@Override
	protected void onStart() {
		super.onStart();

		
	}
	/* @encuesta
	private void dialogoEncuestas(){
		FragmentManager fragmentManager = getSupportFragmentManager();
		DialogEncuestas dialogEncuestas = new DialogEncuestas();

		dialogEncuestas.show(fragmentManager, "Dialogo encuestas");
	}
	*/
	

	@Override
	protected void onDestroy() {
		super.onDestroy();
		try {

			lM.removeUpdates(this);
		}catch (SecurityException e){
			e.printStackTrace();
		}

	}
	



	@Override
	public void onClick(View v) {
		
		switch(v.getId()){

            case R.id.buttonRuta:
                startCalendarioActivity();
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

            case R.id.buttonMensaje:
                Intent iM = new Intent(this, ListaMensajesActivity.class);
				iM.putExtra("idCelular", idUsuario);
				startActivity(iM);
                break;



        }
		
	}

	@Override
	protected void onPause() {


		try {
			lM.removeUpdates(this);

		}catch (SecurityException e){
			e.printStackTrace();
		}

        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        isReceiverRegistered = false;

        super.onPause();
		
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




	public void startCalendarioActivity(){
		Intent i = new Intent(this, CalendarioRuta.class);
		i.putExtra("idCelular",idUsuario);
		startActivity(i);
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

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }

    }

    //DrawerListener

    private void setupDrawerContent(NavigationView navigationView){
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener(){

            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                //Log.d("Item press",item.getTitle().toString());

				switch (item.getItemId()){
                    case R.id.shutdown:
                        drawerLayout.closeDrawer(GravityCompat.START);
                        finish();
                        return true;

					case R.id.configuracion:
						drawerLayout.closeDrawer(GravityCompat.START);
						return true;
				}

                return true;
            }
        });
    }

	public String getUserName(){




        Pattern emailPattern = Patterns.EMAIL_ADDRESS;
        Account[] acccounts = AccountManager.get(this).getAccounts();

        String mail = null;
        for (Account account : acccounts){
            if (emailPattern.matcher(account.name).matches()){
                mail = account.name;
            }
        }

        return mail;

    }

	private boolean checkPlayServices(){
		GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
		int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
		if (resultCode != ConnectionResult.SUCCESS){
			if (apiAvailability.isUserResolvableError(resultCode)){
				apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
			}else {
                Log.i(TAG, "This service is not supported");
                finish();

            }
            return false;
		}
        return true;
	}



}
