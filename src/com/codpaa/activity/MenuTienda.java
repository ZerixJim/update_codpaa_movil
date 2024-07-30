package com.codpaa.activity;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


import com.codpaa.activity.impulsor.AvanceGestion;
import com.codpaa.activity.impulsor.Estatus;
import com.codpaa.activity.impulsor.ProcesoAceptacion;
import com.codpaa.adapter.ExhibicionesAdapter;
import com.codpaa.adapter.MenuTiendaAdapter;
import com.codpaa.fragment.DialogEncuestas;
import com.codpaa.fragment.DialogFragmentFotos;
import com.codpaa.fragment.DialogFragmentMarcas;
import com.codpaa.model.ExhibicionesModel;
import com.codpaa.model.MenuTiendaModel;
import com.codpaa.update.EnviarDatos;
import com.codpaa.R;
import com.codpaa.update.UpdateInformation;
import com.codpaa.util.Configuracion;
import com.codpaa.util.Utilities;
import com.codpaa.widget.DividerItemDecoration;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;

import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import androidx.annotation.NonNull;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import android.widget.HorizontalScrollView;
import android.widget.ListView;

import android.widget.TextView;
import android.widget.Toast;

import com.codpaa.db.BDopenHelper;
import com.codpaa.adapter.FrentesCustomAdapter;
import com.codpaa.adapter.InventariosCustomAdapter;
import com.codpaa.model.FrentesModel;
import com.codpaa.model.InventarioModel;
import com.codpaa.adapter.MenuTiendaAdapter.MenuTiendaListener;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class MenuTienda extends AppCompatActivity implements OnClickListener, MenuTiendaListener{


    private Button btnSalidaTi;
    private Button btnEntrada;
    private Button btnTiendaError;
    private SQLiteDatabase base = null;
    private Location location;


    //private TextView txtEncargado;
    private TextView frentes, surtido, inventario, fotos, muebles;
    final int REQUEST_CODE_ASK_PERMISSIONS = 123;
    int idPromotor, idTienda, idTipo;
    int salidaValida = 0;
    String fechaCaptura;
    private BDopenHelper DB = null;
    private EnviarDatos enviar;
    //private Spinner spinnerEnc;
    //private EditText editNombre;
    private String grupo;
    public static final String TAG = MenuTienda.class.getSimpleName();
    private boolean Salida = false;
    private boolean Entrada = false;
    private ProgressDialog progress;
    private LocalBroadcastManager broadcastManager;

    private final BroadcastReceiver mBroadcastReceiber = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent.getAction().equals("com.codpaa.action.close")){

                Intent i = new Intent(MenuTienda.this, MainActivity.class);

                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                startActivity(i);

                finish();

            }

        }
    };


    private FusedLocationProviderClient fusedLocationClient;

    private LocationRequest locationRequest;

    private LocationCallback locationCallback;

    private int intento = 0;

    private Handler handler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.menutienda);


        Intent recibeIdTi = getIntent();

        idTienda = recibeIdTi.getIntExtra("idTienda", 0);
        idPromotor = recibeIdTi.getIntExtra("idPromotor", 0);
        idTipo = recibeIdTi.getIntExtra("idTipo", 0);




        Toolbar toolbar = findViewById(R.id.toolbar_menu_principal);


        if (toolbar != null) {

            setSupportActionBar(toolbar);
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(true);

            }
        }


        //setting a main menu
        setUpMenu();



        // object to send data
        enviar = new EnviarDatos(this);



        //registrar views
        viewsRegister();


        DB = new BDopenHelper(this);


        try {

            Cursor cTienda = DB.tienda(idTienda);
            cTienda.moveToFirst();

            grupo = cTienda.getString(0);
            String sucursal = cTienda.getString(1);


            if (getSupportActionBar() != null)
                getSupportActionBar().setTitle(grupo);
            cTienda.close();

            try {


                if (getSupportActionBar() != null)
                    getSupportActionBar().setSubtitle(sucursal);


            } catch (Exception e) {
                Toast.makeText(this, "error menuTienda 2", Toast.LENGTH_SHORT).show();
            }


        } catch (Exception e) {

            Toast.makeText(this, "error", Toast.LENGTH_SHORT).show();
        } finally {
            DB.close();
        }


        handler = new Handler();

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        locationCallback = new LocationCallback(){

            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null){

                    return;
                }
                for (Location lot : locationResult.getLocations()){
                    location = lot;

                    Log.d("GPS", "locationResutl" + lot.getLatitude() + "," + lot.getLongitude());


                }

            }
        };



        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setFastestInterval(1000)
                .setInterval(10000);



        broadcastManager = LocalBroadcastManager.getInstance(this);

        IntentFilter filter = new IntentFilter();
        filter.addAction("com.codpaa.action.close");
        broadcastManager.registerReceiver(mBroadcastReceiber, filter);

        //OBTENER EL TOTAL DE FOTOS QUE LLEVA EN LA SEMANA



    }

    private void setUpMenu() {

        RecyclerView menuRecycler = findViewById(R.id.recycler_menu_tienda);
        if (menuRecycler != null) {
            menuRecycler.setHasFixedSize(true);
            LinearLayoutManager linear = new LinearLayoutManager(this);
            menuRecycler.setLayoutManager(linear);
            menuRecycler.addItemDecoration(new DividerItemDecoration(this, null));

            MenuTiendaAdapter adapter = new MenuTiendaAdapter(getMenuItems(), this);
            menuRecycler.setAdapter(adapter);

        }

    }


    public List<MenuTiendaModel> getMenuItems() {
        List<MenuTiendaModel> array = new ArrayList<>();

        Configuracion c = new Configuracion(this);

        HorizontalScrollView horizontal = findViewById(R.id.horizontal_view);

        if (c.getPromotorMode() == 1) {

            if (horizontal != null) {
                horizontal.setVisibility(View.VISIBLE);
            }

            final MenuTiendaModel item1 = new MenuTiendaModel();
            item1.setIdMenu(1);
            item1.setNombreMenu("Frentes");
            item1.setImage("ic_grid_on_grey600_24dp");
            array.add(item1);


            // surtido disabled

            /*final MenuTiendaModel item2 = new MenuTiendaModel();
            item2.setIdMenu(2);
            item2.setNombreMenu("Surtido de mueble");
            item2.setImage("ic_blur_linear_grey600_24dp");
            array.add(item2);*/

            final MenuTiendaModel item3 = new MenuTiendaModel();
            item3.setIdMenu(3);
            item3.setNombreMenu("Comentarios");
            item3.setImage("ic_comment_grey_600_24dp");
            array.add(item3);

            final MenuTiendaModel item4 = new MenuTiendaModel();
            item4.setIdMenu(4);
            item4.setNombreMenu("Inventario");
            item4.setImage("ic_assignment_grey600_24dp");
            array.add(item4);

            /*final MenuTiendaModel item5 = new MenuTiendaModel();
            item5.setIdMenu(5);
            item5.setNombreMenu("Exhibiciones especiales");
            item5.setImage("ic_border_bottom_grey600_24dp");
            array.add(item5);*/

            final MenuTiendaModel item6 = new MenuTiendaModel();
            item6.setIdMenu(6);
            item6.setNombreMenu("Inteligencia de mercado");
            item6.setSubDescription("(Precios propios y competencias)");
            item6.setImage("ic_assessment_grey_600_24dp");
            array.add(item6);

            /*final MenuTiendaModel item7 = new MenuTiendaModel();
            item7.setIdMenu(7);
            item7.setNombreMenu("Materiales");
            item7.setImage("ic_apps_grey_600_24dp");
            array.add(item7);*/

            /*final MenuTiendaModel item8 = new MenuTiendaModel();
            item8.setIdMenu(8);
            item8.setNombreMenu("Venta promedio");
            item8.setImage("ic_timeline_grey_600_24dp");
            array.add(item8);*/


            /*final MenuTiendaModel item9 = new MenuTiendaModel();
            item9.setIdMenu(9);
            item9.setNombreMenu("Encargado de tienda");
            item9.setImage("ic_timer_auto_grey600_24dp");
            array.add(item9);*/

            final MenuTiendaModel item10 = new MenuTiendaModel();
            item10.setIdMenu(10);
            item10.setNombreMenu("Foto");
            item10.setImage("ic_camera_grey600_24dp");
            array.add(item10);


            //palette disabled
           /* if (idTipo == 2 && marcaChecker()){

                final MenuTiendaModel item15 = new MenuTiendaModel();
                item15.setIdMenu(15);
                item15.setNombreMenu("Censo Palette");
                item15.setImage("ic_warning");
                array.add(item15);

            }*/
            //Encuestas
            final MenuTiendaModel item16 = new MenuTiendaModel();
            item16.setIdMenu(19);
            item16.setNombreMenu("Encuestas");
            item16.setImage("ic_assignment_grey600_24dp");
            array.add(item16);

            //agotados
            final MenuTiendaModel item12 = new MenuTiendaModel();
            item12.setIdMenu(16);
            item12.setNombreMenu("Agotado");
            item12.setImage("ic_assignment_grey600_24dp");
            array.add(item12);


            final MenuTiendaModel item11 = new MenuTiendaModel();
            item11.setIdMenu(11);
            item11.setNombreMenu("Actualizar producto");
            item11.setImage("ic_autorenew_grey_600_24dp");
            array.add(item11);

            /*final MenuTiendaModel item17 = new MenuTiendaModel();
            item17.setIdMenu(17);
            item17.setNombreMenu("Productos disponibles por marca");
            item17.setImage("ic_assignment_grey600_24dp");
            array.add(item17);*/

            /*final MenuTiendaModel item18 = new MenuTiendaModel();
            item18.setIdMenu(18);
            item18.setNombreMenu("Medición de muebles");
            item18.setImage("ic_grid_on_grey600_24dp");
            array.add(item18);*/



        } else if (c.getPromotorMode() == 2) {


            final MenuTiendaModel item13 = new MenuTiendaModel();
            item13.setIdMenu(13);
            item13.setNombreMenu("Estatus");
            item13.setImage("ic_assignment_grey_600_24dp");
            array.add(item13);


            final MenuTiendaModel item14 = new MenuTiendaModel();
            item14.setIdMenu(14);
            item14.setNombreMenu("Avance de la Gestion");
            item14.setImage("ic_input_grey_600_24dp");
            array.add(item14);


            final MenuTiendaModel item12 = new MenuTiendaModel();
            item12.setIdMenu(12);
            item12.setNombreMenu("Proceso aceptacion");
            item12.setImage("ic_description_grey_600_24dp");
            array.add(item12);


            final MenuTiendaModel item3 = new MenuTiendaModel();
            item3.setIdMenu(3);
            item3.setNombreMenu("Comentarios");
            item3.setImage("ic_comment_grey_600_24dp");
            array.add(item3);


            final MenuTiendaModel item11 = new MenuTiendaModel();
            item11.setIdMenu(11);
            item11.setNombreMenu("Actualizar producto");
            item11.setImage("ic_autorenew_grey_600_24dp");
            array.add(item11);


        }






        return array;
    }


    private void viewsRegister() {

        inventario = findViewById(R.id.inventario);
        //txtEncargado = findViewById(R.id.Encargado);
        frentes =  findViewById(R.id.frentes);
        surtido =  findViewById(R.id.surtido);
        //exhi = findViewById(R.id.textExhibicio);
        fotos =  findViewById(R.id.text_fotos);
        muebles = findViewById(R.id.medMuebles);

        Button btnFrente = findViewById(R.id.buttonMensaje);
        btnSalidaTi =  findViewById(R.id.salidaTienda);
        btnEntrada =  findViewById(R.id.btnEnTienda);
        Button btnEncar = findViewById(R.id.btnEncarg);
        //Button btnExhib = findViewById(R.id.buttonExhib);
        Button btnUpdaPro = findViewById(R.id.btnUpdaPro);
        Button btnInven = findViewById(R.id.btnInvenBode);
        Button btnSurtido = findViewById(R.id.buttonEnviar);
        btnTiendaError = findViewById(R.id.btnTiendaError);
        Button btnComentario = findViewById(R.id.btnComentario);
        Button btnInteligencia = findViewById(R.id.btnMenInt);
        Button btnFoto = findViewById(R.id.btnfoto);
        Button btnVentaPromedio = findViewById(R.id.btn_venta_promedio);

        Button btnCapturaGeneral = findViewById(R.id.captura_general);

        Button btnMateriales = findViewById(R.id.btnMateriales);


        btnFrente.setOnClickListener(this);
        btnEntrada.setOnClickListener(this);
        btnSurtido.setOnClickListener(this);
        btnInven.setOnClickListener(this);
        btnSalidaTi.setOnClickListener(this);
        btnTiendaError.setOnClickListener(this);
        btnEncar.setOnClickListener(this);
        //btnExhib.setOnClickListener(this);
        btnComentario.setOnClickListener(this);
        btnInteligencia.setOnClickListener(this);
        btnUpdaPro.setOnClickListener(this);
        btnFoto.setOnClickListener(this);
        btnVentaPromedio.setOnClickListener(this);

        btnCapturaGeneral.setOnClickListener(this);
        btnMateriales.setOnClickListener(this);


        // textView with listener
        frentes.setOnClickListener(this);
        inventario.setOnClickListener(this);
        //exhi.setOnClickListener(this);
        fotos.setOnClickListener(this);
        muebles.setOnClickListener(this);

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();

        inflater.inflate(R.menu.menu_tienda, menu);


        MenuItem marcas = menu.findItem(R.id.btn_marcas);

        marcas.setVisible(getTotalBrands(idTienda) > 0);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == android.R.id.home) {
            if (!Entrada && !Salida) {
                this.finish();
            } else {
                dialogoConfirmacionSalida();
            }

            return true;
        } else if (itemId == R.id.btn_marcas) {
            dialogMarcasFaltantes();

            return true;

            /*case R.id.direccion:

                saveAddress();

                return true;

            case R.id.producto_datos_tienda:

                saveDatosTienda();

                return true;


            case R.id.actualizar_encuesta:

                descargarEncuesta();

                return true;*/
        }else if (itemId == R.id.sync){

            actualizarPro();

            return true;
        }else

            return super.onOptionsItemSelected(item);

    }

    /*private void saveDatosTienda() {

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

    }*/


    private void activateGps(String tipo) {

        if (location == null) {


            Log.d(TAG, " locgps nullo");

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_CODE_ASK_PERMISSIONS);
            } else {

                activeGps(tipo);
            }


        } else {

            Log.d(TAG, " " + location.getLatitude());


        }


    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {


    }


    private void activeGps(final String tipo) {


        intento++;
        if (intento <= 3) {


            handler.post(new Runnable() {
                @Override
                public void run() {

                    progress = new ProgressDialog(MenuTienda.this);
                    progress.setMessage("Registrando intento " + intento);
                    progress.setCancelable(false);
                    progress.show();


                    if (ActivityCompat.checkSelfPermission(MenuTienda.this, Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED
                            && ActivityCompat.checkSelfPermission(MenuTienda.this, Manifest.permission.ACCESS_COARSE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        //locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 0, MenuTienda.this);

                        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());


                    }

                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            //locationManager.removeUpdates(MenuTienda.this);
                            progress.dismiss();

                            if (tipo.equals("entrada")) {
                                entradaTienda();

                            } else if (tipo.equals("salida")) {

                                salidaTienda();

                            }


                        }
                    }, 10000);


                }
            });


        } else if (intento > 3) {

            Handler mainLoop = new Handler(Looper.getMainLooper());

            mainLoop.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(MenuTienda.this, "No fue posible registrar la Entrada" +
                            " \n -Verifique que el Gps esta activado \n - De lo contrario comuniquese " +
                            "con Mesa de control", Toast.LENGTH_LONG).show();
                }
            });


        }


    }

    public void entradaTienda() {

        //permiso();

        Calendar c = Calendar.getInstance();
        SimpleDateFormat dFecha = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        SimpleDateFormat dHora = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());


        final String fecha = dFecha.format(c.getTime());
        final String hora = dHora.format(c.getTime());




        if (!Entrada) {


            if (location != null) {

                try {



                    fusedLocationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location loca) {

                            if (loca != null){
                                location = loca;


                                BDopenHelper base = new BDopenHelper(getApplicationContext());

                                Entrada = true;
                                int autoTime = Utilities.isAutoTime(MenuTienda.this) ? 1: 0;
                                base.insertarLocalizacion(idTienda, idPromotor, fecha, hora, location.getLatitude(), location.getLongitude(), 12, "E", 1, Utilities.getCurrentDate(), autoTime);

                                Toast.makeText(MenuTienda.this, "Entrada Guardada", Toast.LENGTH_SHORT).show();
                                btnEntrada.post(new Runnable() {

                                    public void run() {

                                        btnEntrada.setBackgroundResource(R.drawable.custom_btn_dark_khaki);
                                        btnEntrada.setTextColor(Color.WHITE);
                                        enviar.enviarVisitas();
                                    }

                                });
                            }else{

                                Toast.makeText(MenuTienda.this, "problema para obtener la ubicacion ", Toast.LENGTH_SHORT).show();
                            }


                        }

                    });


                    //fusedLocationClient.getLastLocation().addOnFailureListener();

                } catch (SecurityException e) {
                    e.printStackTrace();


                }



            } else {

                activateGps("entrada");



            }
        }


    }


    private void salidaTienda() {


        if (Entrada) {


            Thread hiloSalida = new Thread() {
                public void run() {

                    Configuracion co = new Configuracion(getApplicationContext());

                    if (co.getPromotorMode() == 2 && folioPendiente()) {


                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(), "Productos pendientes de firma", Toast.LENGTH_SHORT).show();

                            }
                        });


                    } else {


                        if (!Salida) {
                            Calendar c = Calendar.getInstance();
                            SimpleDateFormat dFecha = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                            SimpleDateFormat dHora = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());

                            final String fecha = dFecha.format(c.getTime());
                            final String hora = dHora.format(c.getTime());


                            //Se obtiene el año actual
                            Date date = null;

                            int currYear = 0;

                            try {
                                date = dFecha.parse(fecha);
                            }catch(ParseException pe){
                                pe.printStackTrace();
                            }

                            try {
                                c.setTime(date);
                                currYear = c.get(Calendar.YEAR);
                            }catch (Exception e){
                                //Toast.makeText(MenuTienda.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }

                            if (location != null) {

                                try {

                                    //rutaPromotor.close();
                                    final int finalCurrYear = currYear;
                                    fusedLocationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                                        @Override
                                        public void onSuccess(Location locat) {

                                            if (locat != null){
                                                location = locat;

                                                BDopenHelper base = new BDopenHelper(getApplicationContext());

                                                int autoTime = Utilities.isAutoTime(MenuTienda.this) ? 1 : 0;

                                                if (DB.tipoTienda(idTienda) > 0) {
                                                    //PARA CUALQUIER TIENDA, REQUIERE MÍNIMO DE FOTOS

                                                    if(DB.contarFotos(idTienda) >= 2) { //Cambiar a dos fotos mínimo

                                                        base.insertarLocalizacion(idTienda, idPromotor, fecha, hora, locat.getLatitude(), locat.getLongitude(), 12, "S", 1, Utilities.getCurrentDate(), autoTime);

                                                        Salida = true;

                                                        btnSalidaTi.post(new Runnable() {
                                                            public void run() {


                                                                btnSalidaTi.setBackgroundResource(R.drawable.custom_btn_dark_khaki);
                                                                btnSalidaTi.setTextColor(Color.WHITE);
                                                                Toast.makeText(getApplicationContext(), "Salida Registrada", Toast.LENGTH_SHORT).show();

                                                                enviar.enviarVisitas();

                                                            }

                                                        });

                                                    }
                                                    else{
                                                        Toast.makeText(MenuTienda.this, "Requiere tomar el mínimo de fotos para marcar salida", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                                else{
                                                    //SI ES DE MAYOREO, NO REQUIERE MÍNIMO DE FOTOS
                                                    base.insertarLocalizacion(idTienda, idPromotor, fecha, hora, locat.getLatitude(), locat.getLongitude(), 12, "S", 1, Utilities.getCurrentDate(), autoTime);

                                                    Salida = true;

                                                    btnSalidaTi.post(new Runnable() {
                                                        public void run() {


                                                            btnSalidaTi.setBackgroundResource(R.drawable.custom_btn_dark_khaki);
                                                            btnSalidaTi.setTextColor(Color.WHITE);
                                                            Toast.makeText(getApplicationContext(), "Salida Registrada", Toast.LENGTH_SHORT).show();

                                                            enviar.enviarVisitas();

                                                        }

                                                    });
                                                }
                                            }
                                            else {
                                                Toast.makeText(MenuTienda.this, "Problema para obtener la ubicación ", Toast.LENGTH_SHORT).show();
                                            }


                                        }
                                    });

                                    //location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

                                } catch (SecurityException e) {
                                    e.printStackTrace();
                                }



                                try {
                                    sleep(2000);
                                } catch (InterruptedException e) {

                                    e.printStackTrace();
                                }
                                MenuTienda.this.finish();
                            } else {

                                activateGps("salida");


                            }
                        } else {
                            MenuTienda.this.finish();
                        }


                    }


                }

            };
            hiloSalida.start();

        } else {
            Toast.makeText(this, "No has registrado Entrada", Toast.LENGTH_SHORT).show();
        }
    }


    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.buttonMensaje) {
            menuFrentes();
        } else if (id == R.id.btnEnTienda) {//entradaTienda();

            if (!Entrada) {
                dialogoConfirmarEntrada();
            } else {
                Toast.makeText(this, "Ya existe una Entrada", Toast.LENGTH_SHORT).show();
            }
        } else if (id == R.id.salidaTienda) {
            dialogoConfirmacionSalida();
        }
        //else if (id == R.id.btnEncarg) {
            //dialogoEncargado();
        //}

        else if (id == R.id.buttonEnviar) {
            menuSurtido();
        } else if (id == R.id.btnInvenBode) {
            menuInventario();
        } else if (id == R.id.buttonExhib) {
            menuExhibicion();
        } else if (id == R.id.btnTiendaError) {
            tiendaErronea();
        } else if (id == R.id.btnComentario) {
            menuComentario();
        } else if (id == R.id.btnMenInt) {
            inteligenciaMercado();
        } else if (id == R.id.btnfoto) {
            capturaFoto();
        } else if (id == R.id.btnUpdaPro) {
            actualizarPro();
        } else if (id == R.id.frentes) {
            //Log.v("TextFrentes", "Onclick");
            dialogoFrentesCapturados();
        } else if (id == R.id.inventario) {
            dialogoInventariosCapturados();
        } else if (id == R.id.textExhibicio) {
            dialogoExhibiciones();
        } else if (id == R.id.text_fotos) {
            dialogoFotos();
        } else if (id == R.id.btn_venta_promedio) {
            subMenuVenta();
        } else if (id == R.id.captura_general) {
            capturaGeneral();
        } else if (id == R.id.btnMateriales) {
            openMateriales();
        } else if(id == R.id.btnMedMuebles){
            medicionMuebles();
        }


    }

    private void productosDisponibles() {
        if(Entrada) {
            Intent intent = new Intent(this, ProductosDisponibles.class);
            intent.putExtra("idTienda", idTienda);
            intent.putExtra("idPromotor", this.idPromotor);
            startActivity(intent);
        }
        else{
            Toast.makeText(this, "No has registrado Entrada", Toast.LENGTH_SHORT).show();
        }
    }

    private void medicionMuebles(){
        if(Entrada){
            Intent intent = new Intent(this, MedicionMuebles.class);
            intent.putExtra("idTienda", idTienda);
            intent.putExtra("idPromotor", idPromotor);
            startActivity(intent);
        }
        else{
            Toast.makeText(this, "No has registrado Entrada", Toast.LENGTH_SHORT).show();
        }
    }
    private void encuestadlr(){
        if(Entrada){
            Intent intent = new Intent(this, EncuestaActivity.class);
            intent.putExtra("idTienda", idTienda);
            intent.putExtra("idPromotor", idPromotor);
            startActivity(intent);
        }
        else{
            Toast.makeText(this, "No has registrado Entrada", Toast.LENGTH_SHORT).show();
        }
    }
    private void openMateriales() {

        Intent intent = new Intent(this, MaterialesActivity.class);
        intent.putExtra("idTienda", idTienda);
        intent.putExtra("idPromotor", idPromotor);
        startActivity(intent);

    }

    private void subMenuVenta() {

        if (Entrada) {
            Intent i = new Intent(this, VentaPromedio.class);
            i.putExtra("idPromotor", idPromotor);
            i.putExtra("idTienda", idTienda);
            startActivity(i);
        } else {
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
        updateInformation.downloadProduct(idPromotor);

    }



    @Override
    protected void onResume() {
        super.onResume();

        startGpsAtMoment();


        intento = 0;

        Calendar c = Calendar.getInstance();
        SimpleDateFormat dFecha = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        String fecha = dFecha.format(c.getTime());


        checkIns(fecha);

        try {


            /*
             * encargago
             */
            //Cursor cuEncargados = DB.contadorEncargados(idTienda, fecha);
            //txtEncargado.setText("Encargado ("+cuEncargados.getCount()+")");
            //txtEncargado.setText(String.format(Locale.getDefault(), "Encargado %d", cuEncargados.getCount()));
            //DB.close();

            Cursor cuFrentes = DB.contadorFrentes(idTienda, fecha);
            //frentes.setText("Frentes: ("+cuFrentes.getCount()+")");
            if(cuFrentes.getCount() > 0){
                frentes.setVisibility(View.VISIBLE);
                frentes.setText(String.format(Locale.getDefault(), "Frentes %d", cuFrentes.getCount()));
                DB.close();
            }

            Cursor curMuebles = DB.contadorMedicionMuebles(idTienda, fecha);
            if(curMuebles.getCount() > 0){
                muebles.setVisibility(View.VISIBLE);
                muebles.setText(String.format(Locale.getDefault(), "Mediciones %d", curMuebles.getCount()));
                DB.close();
            }


            Cursor cuSurt = DB.SurtidoCantidad(idTienda, fecha);
            if (cuSurt.getCount() > 0){
                surtido.setVisibility(View.VISIBLE);
                surtido.setText(String.format(Locale.getDefault(), "Surtido %d", cuSurt.getCount()));
                DB.close();
            }

            Cursor cuInventario = DB.contarInventario(idTienda, fecha);
            if (cuInventario.getCount() > 0){
                inventario.setVisibility(View.VISIBLE);
                inventario.setText(String.format(Locale.getDefault(), "Inventario %d", cuInventario.getCount()));
                DB.close();
            }


            //exhibiciones
            //exhi.setText(String.format(Locale.getDefault(), "Exhibiciones %d", DB.contarExhibiciones(idTienda, fecha)));

            if (DB.contarFotos(idTienda) > 0){
                fotos.setVisibility(View.VISIBLE);
                fotos.setText(String.format(Locale.getDefault(), "Fotos %d", DB.contarFotos(idTienda)));
            }




        } catch (Exception e) {
            e.printStackTrace();
        }

		/*
		if (new BDopenHelper(this).tipoTienda(idTienda) ==2){
			if (!verifyCatalogo()){
				dialogCapturaDeCatalogo();
			}
		}*/


        /*if (encuestaDisponible()) {


            Handler handler = new Handler();

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    dialogoEncuestas();
                }
            }, 3000);


        } else {
            verificarEncuesta();
        }*/


        BDopenHelper bDopenHelper = new BDopenHelper(this);

        if (bDopenHelper.isProductsEmpty() || bDopenHelper.isBrandEmpty()){
            actualizarPro();

        }

        // sending visits on app is visible
        enviar.enviarVisitas();





    }

    private void checkIns(String fecha) {
        try {
            Cursor cuEntra = DB.VisitaTienda(idTienda, fecha, "E");

            if (cuEntra.getCount() > 0) {

                Entrada = true;
                btnEntrada.setBackgroundResource(R.drawable.custom_btn_dark_khaki);
                btnEntrada.setTextColor(Color.parseColor("#0aa82c"));


            } else {

                btnEntrada.setBackgroundResource(R.drawable.custom_btn_orange);
                btnEntrada.setTextColor(Color.parseColor("#a8270a"));
                Entrada = false;

            }
            cuEntra.close();
            DB.close();

            try {
                Cursor cuSalida = DB.VisitaTienda(idTienda, fecha, "S");

                //auto time

                //if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1)

                //// TODO: 17/05/2017 investigar la forma de implementar el autotime
                //Settings.System.putInt(getContentResolver(), Settings.System.AUTO_TIME,1);


                if (cuSalida.getCount() > 0) {


                    btnSalidaTi.setBackgroundResource(R.drawable.custom_btn_dark_khaki);
                    btnSalidaTi.setTextColor(Color.parseColor("#0aa82c"));
                    Salida = true;
                    DB.close();

                    if (Entrada && Salida){


                        btnTiendaError.setVisibility(View.GONE);
                        btnEntrada.setVisibility(View.GONE);
                        btnSalidaTi.setVisibility(View.GONE);

                    }
                } else {

                    btnSalidaTi.setBackgroundResource(R.drawable.custom_btn_orange);
                    btnSalidaTi.setTextColor(Color.parseColor("#a8270a"));
                    Salida = false;
                }

                cuSalida.close();
                DB.close();
                if (base != null)
                    base.close();
            } catch (Exception e) {
                e.printStackTrace();
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private boolean encuestaDisponible(){


        SQLiteDatabase db = new BDopenHelper(this).getReadableDatabase();
        String sql = "select * from preguntas as pre where  " +
                " pre.id_encuesta not in  " +
                " ( select idEncuesta from  encuesta_respuestas where pre.id_encuesta=idEncuesta " +
                "  and idPromotor=" + idPromotor + " and  idTienda =" + idTienda + " ) group by id_encuesta";

        Cursor cursor = db.rawQuery(sql, null);
        cursor.moveToFirst();


        int countDisponibles = cursor.getCount();

        cursor.close();
        db.close();


        return countDisponibles > 0;
    }

    private void dialogoEncuestas() {

        FragmentManager fragmentManager = getSupportFragmentManager();
        DialogEncuestas dialogEncuestas = new DialogEncuestas();

        Bundle bundle = new Bundle();
        bundle.putInt("idTienda", idTienda);
        bundle.putInt("idPromotor", idPromotor);

        dialogEncuestas.setArguments(bundle);
        dialogEncuestas.setStyle(DialogFragment.STYLE_NORMAL, R.style.CustomDialog);


        dialogEncuestas.show(fragmentManager, "Dialogo encuestas");
    }


    private void verificarEncuesta() {
        Configuracion configuracion = new Configuracion(this);
        UpdateInformation uI = new UpdateInformation(this);

        Log.d("menuPrincipal", "verificar");

        if (configuracion.getKeyByTag(String.valueOf(idTienda)) != null) {
            Log.d("menuPrincipal", "verificar2");
            if (!configuracion.getKeyByTag(String.valueOf(idTienda)).equals(fechaActual())) {
                uI.actualizarEncuesta(idPromotor, idTienda);
                Log.d("menuPrincipal", "verificar2.5");
            }

        } else {
            Log.d("menuPrincipal", "verificar3");

            uI.actualizarEncuesta(idPromotor, idTienda);
        }


    }


    /*private void descargarEncuesta() {

        UpdateInformation uI = new UpdateInformation(this);

        uI.actualizarEncuesta(idPromotor, idTienda);

    }*/

    private String fechaActual() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat dFecha = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        return dFecha.format(c.getTime());
    }

    @Override
    protected void onStart() {
        super.onStart();


        //mGoogleApiClient.connect();


        verificarEncuesta();


    }

    private void startGpsAtMoment() {



        final Handler handler = new Handler();

        handler.post(new Runnable() {
            @Override
            public void run() {


                if (ActivityCompat.checkSelfPermission(MenuTienda.this, Manifest.permission.ACCESS_FINE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MenuTienda.this, Manifest.permission.ACCESS_COARSE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {


                    //LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, locationRequest, MenuTienda.this);

                    fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);


                }


                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        fusedLocationClient.removeLocationUpdates(locationCallback);

                        //if (mGoogleApiClient.isConnected())


                        //LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, MenuTienda.this);
                    }
                }, 10000);


            }
        });



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
			Intent i = new Intent(this, Precio.class);
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


    private void capturaAgotados(){

        if(Entrada){
            Intent i = new Intent(this, Agotados.class);
            i.putExtra("idTienda", idTienda);
            i.putExtra("idPromotor", idPromotor);
            startActivity(i);

        }else{
            Toast.makeText(getApplicationContext(), "Entrada no Registrada...\n No es posible Capturar agotados", Toast.LENGTH_LONG).show();
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
    protected void onStop() {
        super.onStop();


        //mGoogleApiClient.disconnect();



    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (DB != null){
            DB.close();
        }


        broadcastManager.unregisterReceiver(mBroadcastReceiber);

    }

    /*private void dialogoEncargado() {
		
		
		Builder builder  = new AlertDialog.Builder(this);
		final ViewGroup nullParent = null;
		View vistaEncargado = LayoutInflater.from(this).inflate(R.layout.encargadotienda, nullParent);
		String[] tipoEncargado = new String[] {"ENCARGADO","AUXILIAR"};
		spinnerEnc =  vistaEncargado.findViewById(R.id.spinnerMarca);
		editNombre = vistaEncargado.findViewById(R.id.nombreEnca);
		
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
		
	}*/




	/*private void dialogCapturaDeCatalogo(){

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

	}*/

	/*private boolean verifyCatalogo(){


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

	}*/




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

    @Override
    public void onMenuItemClick(MenuTiendaModel clickMenuItem) {

		switch (clickMenuItem.getIdMenu()){
			case 1:
				menuFrentes();
				break;
			case 2:
				menuSurtido();
				break;

			case 3:
				menuComentario();
				break;

			case 4:
				menuInventario();
				break;

			case 5:
				menuExhibicion();
				break;

			case 6:
				inteligenciaMercado();
				break;

			case 7:
				openMateriales();
				break;
			case 8:
				subMenuVenta();
				break;
			case 9:
				//dialogoEncargado();
				break;
			case 10:
				capturaFoto();
				break;

			case 11:
				actualizarPro();
				break;

			case 12:
				procesoAceptacion();
				break;

			case 13:
				estatusProducto();
				break;

			case 14:
				avanceGestion();

				break;
            case 15:
                pallet();

                break;
            case 16:
                capturaAgotados();
                break;

            case 17:
                productosDisponibles();
                break;

            case 18:
                medicionMuebles();
                break;
            case 19:
                encuestadlr();
                break;

		}


    }

    private void pallet() {

	    Intent intent = new Intent(this, CapturaEspecial.class);
	    intent.putExtra("idTienda", idTienda);
	    intent.putExtra("idPromotor", idPromotor);

	    startActivity(intent);

    }

    private void avanceGestion() {

		Intent intent = new Intent(this, AvanceGestion.class);

		intent.putExtra("idTienda", idTienda);
		intent.putExtra("idPromotor", idPromotor);

		startActivity(intent);



	}

	private void estatusProducto() {

		if(Entrada){
			Intent i = new Intent(this, Estatus.class);
			i.putExtra("idPromotor", idPromotor);
			i.putExtra("idTienda", idTienda);

			startActivity(i);
		}else {

			Toast.makeText(this, "Debe registrar entrada", Toast.LENGTH_SHORT).show();

		}



	}



   /* private class EscucharDialogoEncargado implements DialogInterface.OnClickListener{

		@Override
		public void onClick(DialogInterface dialog, int which) {

			if(which == DialogInterface.BUTTON_POSITIVE){


				String tipo = spinnerEnc.getSelectedItem().toString();
				String nombre = editNombre.getText().toString();

				Calendar c = Calendar.getInstance();
				SimpleDateFormat dFecha = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());


				String fecha = dFecha.format(c.getTime());
				if(!nombre.trim().equals("") && !nombre.isEmpty()) {
					new BDopenHelper(MenuTienda.this).insertarEncargadoTienda(idTienda, idPromotor, nombre, tipo, fecha);
					txtEncargado.setText(R.string.encargado_ok);
					new EnviarDatos(MenuTienda.this).enviarEncargado();
				}else {
					Toast.makeText(getApplicationContext(),"no escribiste nombre del encargado", Toast.LENGTH_SHORT).show();
				}




			}else if(which == DialogInterface.BUTTON_NEGATIVE){
				Toast.makeText(getApplicationContext(),"Cancelaste la Seleccion", Toast.LENGTH_SHORT).show();
			}

		}

	}*/

	private void tiendaErronea(){
		if(!Entrada && !Salida){
			finish();
		}else{
			Toast.makeText(this, "check de Entrada detectado", Toast.LENGTH_SHORT).show();
		}
	}

	
	
	private void dialogoFrentesCapturados(){

        Builder builder = new AlertDialog.Builder(this);
        LayoutInflater layoutInflater =  (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		final ViewGroup nullParent = null;

        View frentes = layoutInflater.inflate(R.layout.lista_frentes_capturados, nullParent);
        Listener flistener = new Listener();
        ListView listView = frentes.findViewById(R.id.listFrentesCap);
        FrentesCustomAdapter adapter = new FrentesCustomAdapter(this,R.layout.lista_frentes_capturados,getFrentesCapturados());
        listView.setAdapter(adapter);


        builder.setPositiveButton("Cerrar",flistener).setView(frentes);

        builder.create().show();
    }

    private void dialogoInventariosCapturados(){

        Builder builder = new AlertDialog.Builder(this);
        LayoutInflater layoutInflater =  (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		final ViewGroup nullParent = null;
        View inventarios = layoutInflater.inflate(R.layout.lista_capturados, nullParent);
        Listener flistener = new Listener();
        ListView listView = inventarios.findViewById(R.id.listCapturados);

        InventariosCustomAdapter adapter = new InventariosCustomAdapter(this,R.layout.lista_capturados,getInventariosCapturados());

        listView.setAdapter(adapter);


        builder.setPositiveButton("Cerrar",flistener).setView(inventarios);

        builder.create().show();
    }


    private void dialogoExhibiciones(){
        Builder builder = new AlertDialog.Builder(this);
        LayoutInflater layoutInflater =  (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		final ViewGroup nullParent = null;
        View exhiList = layoutInflater.inflate(R.layout.list_view_exhi, nullParent);
        Listener flistener = new Listener();
        ListView listView = exhiList.findViewById(R.id.list_exhi);

    	ExhibicionesAdapter adapter = new ExhibicionesAdapter(this, R.layout.row_exhi, getExhibicionesByIdTienda(idTienda));

        listView.setAdapter(adapter);


        builder.setPositiveButton("Cerrar",flistener).setView(exhiList);

        builder.create().show();


    }

    private ArrayList<FrentesModel> getFrentesCapturados(){
        base = new BDopenHelper(this).getReadableDatabase();
        String sql = "select m.nombre, p.nombre || ' ' || p.presentacion, f.cantidad as total, f.status, f.fecha," +
				" (f.unifila+f.fila1+f.fila2+f.fila3+f.fila4+f.fila5+f.fila6+f.fila7+f.fila8+f.fila9+f.fila10+f.fila11+f.fila12+f.fila13+f.fila14) as filas, f.idCategoria " +
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
				fm.setCategoria(frentes.getInt(frentes.getColumnIndex("idCategoria")));


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


    private static class Listener implements DialogInterface.OnClickListener{

        @Override
        public void onClick(DialogInterface dialog, int which) {

            if(which == DialogInterface.BUTTON_POSITIVE){
                Log.v("listener","Positive");
            }


        }

    }

    private void dialogoConfirmacionSalida(){

		if (Salida){

            Configuracion configuracion = new Configuracion(this);



            if(configuracion.getPromotorMode() == 2 && folioPendiente()){


                Toast.makeText(this,"Productos sin firmar", Toast.LENGTH_SHORT).show();

            }else {

                this.finish();
            }

		}else {
			if (Entrada){

				if (!encuestaDisponible()){

					Builder builder = new AlertDialog.Builder(this);
					builder.setMessage("¿Estas Seguro(a) que quieres Registrar tu Salida?");
					ListenerSAlida listener = new ListenerSAlida();
					builder.setPositiveButton("Aceptar", listener).setNegativeButton("Cancelar", listener);
					builder.create().show();

				}else {
					Toast.makeText(this, "Tienes encuenstas disponibles", Toast.LENGTH_LONG).show();

					Handler handler = new Handler();
					handler.postDelayed(new Runnable() {
						@Override
						public void run() {
							dialogoEncuestas();
						}
					}, 1000);



				}



			}else {
				Toast.makeText(this,"Entrada no Registrada", Toast.LENGTH_SHORT).show();
			}

		}
    }

	private void dialogoConfirmarEntrada(){

		Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("¿Estas seguro(a) que quieres Registrar tu Entrada en \n " + grupo + "?");
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


    private void procesoAceptacion(){

		Intent i = new Intent(this, ProcesoAceptacion.class);

		i.putExtra("idPromotor", idPromotor);
		i.putExtra("idTienda", idTienda);

		startActivity(i);


	}


	private boolean folioPendiente(){
        int count = 0;

        Calendar c = Calendar.getInstance();

        SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        String fecha = formato.format(c.getTime());


        String sql = "select * from producto_catalogado_tienda as pct " +
                " where idTienda="+ idTienda +" and " +
                " firma is null and estatus_producto = 4 and date(fecha_captura)='" + fecha + "'";

        Log.d("sql", sql);

        SQLiteDatabase db = new BDopenHelper(this).getReadableDatabase();

        Cursor cursor =  db.rawQuery(sql, null);


        if (cursor.getCount() > 0){

            count=cursor.getCount();

        }

        cursor.close();
        db.close();
        return count > 0;

    }


    private void dialogMarcasFaltantes(){

        DialogFragmentMarcas dm = new DialogFragmentMarcas();
        FragmentManager fm = getSupportFragmentManager();

        Bundle b = new Bundle();
        b.putInt("idTienda", idTienda);

        dm.setArguments(b);


        dm.show(fm, "dialog marcas");



    }


    private int getTotalBrands(int idTienda){

	    SQLiteDatabase db = new BDopenHelper(this).getReadableDatabase();

	    Cursor cursor = db.rawQuery("select * from tienda_marca tm where tm.idTienda=" + idTienda , null);

	    int count = cursor.getCount();

	    db.close();
	    cursor.close();

	    return count;
    }

    private void registrarSalida(Location locat, String fecha, String hora){

        //PARTE IMPORTANTE
        location = locat;


        BDopenHelper base = new BDopenHelper(getApplicationContext());

        int autoTime = Utilities.isAutoTime(MenuTienda.this) ? 1 : 0;
        base.insertarLocalizacion(idTienda, idPromotor, fecha, hora, locat.getLatitude(), locat.getLongitude(), 12, "S", 1, Utilities.getCurrentDate(), autoTime);

        Salida = true;

        btnSalidaTi.post(new Runnable() {
            public void run() {


                btnSalidaTi.setBackgroundResource(R.drawable.custom_btn_dark_khaki);
                btnSalidaTi.setTextColor(Color.WHITE);
                Toast.makeText(getApplicationContext(), "Salida Registrada", Toast.LENGTH_SHORT).show();

                enviar.enviarVisitas();

            }

        });
        //FIN DE PARTE IMPORTANTE
    }

    private int visitasPorSemana(int idTienda, int idPromotor){
        //OBTENER EL TOTAL DE VISITAS POR SEMANA DE LA TIENDA
        int n = DB.obtenerRuta(idTienda, idPromotor).getCount();
        int lun, mar, mie, jue, vie, sab;
        int total = 0;

        Cursor route = DB.obtenerRuta(idTienda, idPromotor);

        route.moveToFirst();

        try {
            lun = route.getInt(route.getColumnIndex("lunes"));
            mar = route.getInt(route.getColumnIndex("martes"));
            mie = route.getInt(route.getColumnIndex("miercoles"));
            jue = route.getInt(route.getColumnIndex("jueves"));
            vie = route.getInt(route.getColumnIndex("viernes"));
            sab = route.getInt(route.getColumnIndex("sabado"));

            total = lun+mar+mie+jue+vie+sab;

            Toast.makeText(MenuTienda.this, "NUM: " + total, Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            Toast.makeText(MenuTienda.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        route.close();

        return total;
    }

    private int totalVisits(int flag, int visits, int brands){
	    int limitBrands = 0;
	    int totalPhotos = 0;

	    //Si en la tienda se atienden más de 5 marcas, se establece el límite de marcas a 5 para no requerir demasiadas fotos.
        limitBrands = (brands > 5) ? 5 : brands;

	    if(flag == 1){
	        //Si en la tienda solo se atiende Clorox, las fotos totales se determina por
            //la cantidad de días que se visita la tienda multiplicado por 4, porque Clorox
            //requiere 4 fotos por visita.

            totalPhotos = visits * 4;
        }
	    else if(flag == 2){
	        //Si en la tienda se atienden más de una marca y una es Clorox, las fotos totales se
            //determina con la siguiente expresión, donde a las visitas semanales se le resta 1
            //porque ese día está destinado a Clorox, y a las marcas se le resta 1 porque no se considera Clorox

	        totalPhotos = (visits - 1) * 2 * (limitBrands - 1) + 4;
        }
	    else{
	        //Si en la tienda no se visita Clorox, el total de fotos se determina multiplicando los
            //días de visita por la cantidad de marcas por dos.

            totalPhotos = visits * limitBrands * 2;
        }

	    return totalPhotos;
    }

    private int getPhotosTaken(int idPromotor, int idTienda, int semana, int year){

        final AsyncHttpClient photos = new AsyncHttpClient();
        final int[] photosCounter = {0};
        RequestParams rp = new RequestParams();
        rp.put("FotosMarca", "fotosAcumuladas");
        rp.put("idTienda", String.valueOf(idTienda));
        rp.put("idPromotor", String.valueOf(idPromotor));
        rp.put("semana", semana);
        rp.put("year", year);

        photos.get(Utilities.WEB_SERVICE_CODPAA+"getFotos.php", rp, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response){
                if(response != null){
                    int photosTaken = 0;
                    try {
                        photosTaken = response.getInt("rows");
                        Toast.makeText(getApplicationContext(), "Fotos Taken 1: " + photosTaken, Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    photosCounter[0] = photosTaken;
                    Toast.makeText(getApplicationContext(), "Fotos Taken 2: " + photosCounter[0], Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(getApplicationContext(), "El servidor no responde", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
                Toast.makeText(getApplicationContext(), "Error al recibir datos (" + statusCode + ")", Toast.LENGTH_SHORT).show();
            }
        });

        Toast.makeText(getApplicationContext(), "Fotos Taken 3: " + photosCounter[0], Toast.LENGTH_SHORT).show();
	    return photosCounter[0];
    }

    private int getPhotosCurrentDate(int idPromotor, int idTienda, String fecha){
        final AsyncHttpClient photosCurrentDate = new AsyncHttpClient();
        final int[] photosCounter = {0};
        RequestParams rp = new RequestParams();
        rp.put("FotosMarca", "fotosFechaActual");
        rp.put("idTienda", String.valueOf(idTienda));
        rp.put("idPromotor", String.valueOf(idPromotor));
        rp.put("fecha", fecha);

        photosCurrentDate.get(Utilities.WEB_SERVICE_CODPAA+"getFotos.php", rp, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response){
                if(response != null){
                    int photosTaken = 0;
                    try {
                        photosTaken = response.getInt("rows");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    photosCounter[0] = photosTaken;
                }
                else{
                    Toast.makeText(getApplicationContext(), "El servidor no responde", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
                Toast.makeText(getApplicationContext(), "Error al recibir datos (" + statusCode + ")", Toast.LENGTH_SHORT).show();
            }
        });

        return photosCounter[0];
    }


}
