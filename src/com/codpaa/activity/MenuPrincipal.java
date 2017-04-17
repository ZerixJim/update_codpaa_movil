package com.codpaa.activity;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;


import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.codpaa.R;
import com.codpaa.adapter.MenuAdapter;
import com.codpaa.model.MenuModel;
import com.codpaa.service.RegistrationIntentService;
import com.codpaa.update.UpdateInformation;

import com.codpaa.db.BDopenHelper;
import com.codpaa.util.AndroidApps;
import com.codpaa.util.Configuracion;
import com.codpaa.util.QuickstartPreferences;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import de.hdodenhof.circleimageview.CircleImageView;


public class MenuPrincipal extends AppCompatActivity implements OnClickListener, LocationListener {

    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static final String TAG = "MainActivity";
    private final int MY_PERMISSION_GET_ACCOUNDS = 126;

    private DrawerLayout drawerLayout;
    TextView email;
    CircleImageView avatar;

    SQLiteDatabase base;
    LocationManager lM = null;
    String myVersionName = "not available";

    Configuracion configuracion;
    RecyclerView recycler;
    MenuAdapter adapter;

    private BroadcastReceiver mRegistrationBroadcastReceiver, mNewMessageBroadcastReceiver;
    private boolean isReceiverRegistered, isReceiverMessageRegistered;
    private int idUsuario;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.menu_principal);
        setToolbar();


        Intent recibe = getIntent();
        String valor = (String) recibe.getExtras().get("nombre");


        Context context = getApplicationContext();
        PackageManager packageManager = context.getPackageManager();
        String packageName = context.getPackageName();

        avatar = (CircleImageView) findViewById(R.id.avatar);
        if (avatar != null) {
            avatar.setOnClickListener(this);
        }


        try {
            myVersionName = packageManager.getPackageInfo(packageName, 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }


        lM = (LocationManager) getSystemService(LOCATION_SERVICE);


        try {


            BDopenHelper recibeDatos = new BDopenHelper(this);
            Cursor cursorDatosUser = recibeDatos.datosUser(valor);
            cursorDatosUser.moveToFirst();
            idUsuario = cursorDatosUser.getInt(0);

            Log.i("idPromo Menu", "" + idUsuario);


            createMenu();


            drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

            if (navigationView != null) {
                setupDrawerContent(navigationView);

                if (ContextCompat.checkSelfPermission(this, Manifest.permission.GET_ACCOUNTS) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.GET_ACCOUNTS},
                            MY_PERMISSION_GET_ACCOUNDS);
                }

                View view = navigationView.getHeaderView(0);
                TextView nomPromo = (TextView) view.findViewById(R.id.user_name);
                email = (TextView) view.findViewById(R.id.user_mail);
                TextView ver = (TextView) view.findViewById(R.id.version);
                TextView id = (TextView) view.findViewById(R.id.id);
                //CircleImageView imageView = (CircleImageView) view.findViewById(R.id.circle_image);


                /*Picasso.with(this).load("http://test.plataformavanguardia.net/images/promoPic/1000/13122797_10208823644806220_2530392818702619653_o.jpg")
                        .into(imageView);*/

                id.setText(String.format(Locale.getDefault(), "ID: %d", cursorDatosUser.getInt(0)));
                ver.setText(String.format("versión: %s", myVersionName));
                nomPromo.setText(cursorDatosUser.getString(1));

                if (getUserName() != null) {
                    email.setText(getUserName());
                }

            }


            recibeDatos.close();


        } catch (SQLiteException e) {
            Toast.makeText(this, "no se pudo asignar el usurio " + valor, Toast.LENGTH_SHORT).show();
            e.printStackTrace();

        } catch (CursorIndexOutOfBoundsException e) {
            Toast.makeText(getApplicationContext(), "Salga de la aplicacion e inicie sesion de nuevo", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }


        if (verificarConexion()) {

            updateInfo();

        } else {

            Toast.makeText(this, "No fue posible Actualizar, No hay conexion a Internet", Toast.LENGTH_SHORT).show();
        }


        borrarRegistros();



        AndroidApps apps = new AndroidApps(this, idUsuario);
        apps.sentInstallApps();
        apps.sentInstallAppsByWeek();




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


        mNewMessageBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {


                switch (intent.getAction()) {
                    case QuickstartPreferences.NEW_MESSAGE:
                        updateMessage();
                        break;
                }


            }
        };


        //Registro de Brodcast para verificar si el token fue enviado al servidor
        registerReceiver();
        //estadisticas();


        //intent para registrar nuestro dispositivo en gcm
        if (checkPlayServices()) {
            Intent intent = new Intent(this, RegistrationIntentService.class);

            intent.putExtra("idPromo", idUsuario);

            startService(intent);

            //Log.d("MenuPrincipal", "Servicio iniciado");
        }





    }


    private void createMenu() {

        recycler = (RecyclerView) findViewById(R.id.recyclerview);

        adapter = new MenuAdapter(getMenus(), this);

        if (recycler != null) {

            recycler.setHasFixedSize(true);
            recycler.setLayoutManager(new GridLayoutManager(this, 2));
            recycler.setAdapter(adapter);
        }


    }

    private List<MenuModel> getMenus() {

        List<MenuModel> array = new ArrayList<>();

        final MenuModel ruta = new MenuModel();
        ruta.setId(1);
        ruta.setImage("ic_directions_blue_grey_700_36dp");
        ruta.setMenu("Ruta");
        ruta.setIdPromotor(idUsuario);
        array.add(ruta);


        final MenuModel mensajes = new MenuModel();
        mensajes.setId(2);
        mensajes.setImage("ic_message_blue_grey_700_36dp");
        mensajes.setMenu("Mensajes");
        array.add(mensajes);


        final MenuModel enviar = new MenuModel();
        enviar.setId(3);
        enviar.setImage("ic_send_blue_grey_700_36dp");
        enviar.setMenu("Enviar");
        array.add(enviar);


        return array;


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSION_GET_ACCOUNDS:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (getUserName() != null) {
                        email.setText(getUserName());
                    }
                }
        }
    }

    //metodo para registrar broadcast
    private void registerReceiver() {
        //Log.d("MenuPrincipal", "RegisterReciver");
        if (!isReceiverRegistered) {
            LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                    new IntentFilter(QuickstartPreferences.REGISTRATION_COMPLETE));
            isReceiverRegistered = true;

            //Log.d("MenuPrincipal", "RegisterRecive2");
        }


        if (!isReceiverMessageRegistered) {


            LocalBroadcastManager.getInstance(this).registerReceiver(mNewMessageBroadcastReceiver,
                    new IntentFilter(QuickstartPreferences.NEW_MESSAGE));

            isReceiverMessageRegistered = true;
        }
    }

    //metodo para implementar el toolbar
    private void setToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            //actionBar.setHomeAsUpIndicator(R.drawable.ic_account_circle_white_24dp);
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(true);


        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_principal, menu);

        //sView view = (View) menu.getItem(R.id.toolbar);


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.refresh_info:
                if (verificarConexion()) {

                    UpdateInformation upinfo = new UpdateInformation(this);
                    //Toast.makeText(this,"Actualizando Informacion",Toast.LENGTH_SHORT).show();


                    upinfo.updateInfo(idUsuario);

                } else {

                    Toast.makeText(this, "Se perdio la conexion a Internet", Toast.LENGTH_SHORT).show();
                }
                return true;
            case android.R.id.home:

                //drawerLayout.openDrawer(GravityCompat.START);
                this.finish();
                return true;

            case R.id.avatar:

                drawerLayout.openDrawer(GravityCompat.START);

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        updateMessage();

        registerReceiver();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        Calendar c = Calendar.getInstance();

        String fecha = simpleDateFormat.format(c.getTime());


        openTienda(idUsuario, fecha);


    }


    private void updateMessage() {
        int count = new BDopenHelper(this).countMessege();

        if (count > 0) {


            List<MenuModel> menu = adapter.getAllItems();

            for (MenuModel item : menu) {
                if (item.getId() == 2) {
                    item.setChange(1);
                    item.setCount(count);
                }
            }

            adapter.notifyDataSetChanged();
        } else {

            List<MenuModel> menu = adapter.getAllItems();

            for (MenuModel item : menu) {
                if (item.getId() == 2) {
                    item.setChange(0);
                    item.setCount(0);
                }
            }

            adapter.notifyDataSetChanged();
        }

    }

    private void updateInfo() {
        configuracion = new Configuracion(this);
        UpdateInformation uI = new UpdateInformation(this);


        if (configuracion.getMarca() != null
                && configuracion.getProducto() != null
                && configuracion.getExhi() != null
                && configuracion.getTiendas() != null
                && configuracion.getProductoByFormato() != null
                && configuracion.getProductoByTienda() != null) {
            if (!configuracion.getMarca().equals(fechaActual())
                    && !configuracion.getProducto().equals(fechaActual())
                    && !configuracion.getExhi().equals(fechaActual())
                    && !configuracion.getTiendas().equals(fechaActual())
                    && !configuracion.getProductoByFormato().equals(fechaActual())
                    && !configuracion.getProductoByTienda().equals(fechaActual())) {
                //uI.actualizarMarca(idUsuario);
                uI.updateInfo(idUsuario);
            } else {
                Log.d("Shared", "marca:" + configuracion.getMarca() + " produ:" + configuracion.getProducto());
            }
        } else {
            uI.updateInfo(idUsuario);
        }


        /*if (configuracion.getKEY_ENCUESTA() != null){

            if (!configuracion.getKEY_ENCUESTA().equals(fechaActual())){
                uI.actualizarEncuesta(idUsuario);
            }

        } else {

            uI.actualizarEncuesta(idUsuario);
        }*/



    }


    @Override
    protected void onStart() {
        super.onStart();


    }





    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {

            lM.removeUpdates(this);
        } catch (SecurityException e) {
            e.printStackTrace();
        }

    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.avatar:
                drawerLayout.openDrawer(GravityCompat.START);
                break;

        }
    }

    @Override
    protected void onPause() {


        try {
            lM.removeUpdates(this);

        } catch (SecurityException e) {
            e.printStackTrace();
        }

        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        isReceiverRegistered = false;

        LocalBroadcastManager.getInstance(this).unregisterReceiver(mNewMessageBroadcastReceiver);
        isReceiverMessageRegistered = false;

        super.onPause();

    }


    public boolean verificarConexion() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnected();

    }




    private String fechaActual() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat dFecha = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        return dFecha.format(c.getTime());
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


    private void borrarRegistros() {
        try {
            BDopenHelper base = new BDopenHelper(this);

            Calendar c = Calendar.getInstance();
            SimpleDateFormat dFecha = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
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
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }

    }

    //DrawerListener

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                //Log.d("Item press",item.getTitle().toString());

                switch (item.getItemId()) {
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

    public String getUserName() {


        Pattern emailPattern = Patterns.EMAIL_ADDRESS;
        String mail = null;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.GET_ACCOUNTS) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[] {
                    Manifest.permission.GET_ACCOUNTS
            } , MY_PERMISSION_GET_ACCOUNDS);

        } else {
            Account[] acccounts = AccountManager.get(this).getAccounts();


            for (Account account : acccounts){
                if (emailPattern.matcher(account.name).matches()){
                    mail = account.name;
                }
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

    private Cursor tiendaPendiente(String fecha, int idPromotor){

        SQLiteDatabase db = new BDopenHelper(this).getReadableDatabase();


        return db.rawQuery("select co1.idTienda, co1.tipo, co1.fecha, co1.hora, co1.idPromotor from coordenadas as co1 " +
                " where co1.fecha='"+fecha+"' and co1.tipo='E' and co1.idPromotor=" + idPromotor +
                " and idTienda not in " +
                " (select co2.idTienda from coordenadas as co2 " +
                " where co2.idTienda=co1.idTienda and co2.fecha=co1.fecha " +
                "  and co2.idPromotor=co1.idPromotor and co2.tipo='S') order by co1.hora asc limit 1;", null);
    }

    private void openTienda(int idPromotor, String fecha){

        Cursor cursor = tiendaPendiente(fecha, idPromotor);

        if (cursor.getCount() > 0){
            cursor.moveToFirst();

            Intent i = new Intent(this, MenuTienda.class);
            i.putExtra("idTienda", cursor.getInt(cursor.getColumnIndex("idTienda")));
            i.putExtra("idPromotor", cursor.getInt(cursor.getColumnIndex("idPromotor")));
            startActivity(i);

        }

        cursor.close();



    }



}
