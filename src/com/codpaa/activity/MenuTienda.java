package com.codpaa.activity;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;


import com.codpaa.activity.impulsor.AvanceGestion;
import com.codpaa.activity.impulsor.Estatus;
import com.codpaa.activity.impulsor.ProcesoAceptacion;
import com.codpaa.adapter.ExhibicionesAdapter;
import com.codpaa.adapter.MenuTiendaAdapter;
import com.codpaa.fragment.DialogEncuestas;
import com.codpaa.fragment.DialogFragmentFotos;
import com.codpaa.model.ExhibicionesModel;
import com.codpaa.model.MenuTiendaModel;
import com.codpaa.update.EnviarDatos;
import com.codpaa.R;
import com.codpaa.update.UpdateInformation;
import com.codpaa.util.Configuracion;
import com.codpaa.widget.DividerItemDecoration;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.loopj.android.http.*;

import android.Manifest;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.codpaa.db.BDopenHelper;
import com.codpaa.adapter.FrentesCustomAdapter;
import com.codpaa.adapter.InventariosCustomAdapter;
import com.codpaa.model.FrentesModel;
import com.codpaa.model.InventarioModel;
import com.codpaa.adapter.MenuTiendaAdapter.MenuTiendaListener;

public class MenuTienda extends AppCompatActivity implements OnClickListener, MenuTiendaListener,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {


    Button btnSalidaTi, btnEntrada, btnEncar, btnExhib, btnInven, btnFrente, btnSurtido, btnTiendaError;
    Button btnVentaPromedio, btnCapturaGeneral;
    Button btnComentario, btnInteligencia, btnUpdaPro, btnFoto, btnMateriales;
    SQLiteDatabase base = null;
    Location location;


    private GoogleApiClient mGoogleApiClient;

    TextView txtEncargado, frentes, surtido, exhi, inventario, fotos;
    final int REQUEST_CODE_ASK_PERMISSIONS = 123;
    int idPromotor, idTienda;
    BDopenHelper DB = null;
    String myVersionName = "not available";
    EnviarDatos enviar;
    Spinner spinnerEnc;
    EditText editNombre;
    RequestParams rp;
    String grupo;
    RecyclerView menuRecycler;
    Toolbar toolbar;
    public static final String TAG = "MenuTienda";
    private boolean Salida = false;
    private boolean Entrada = false;
    private ProgressDialog progress;

    private LocationRequest locationRequest;

    private int intento = 0;

    private Handler handler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.menutienda);


        toolbar = findViewById(R.id.toolbar_menu_principal);


        if (toolbar != null) {

            setSupportActionBar(toolbar);
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(true);

            }
        }


        setUpMenu();


        rp = new RequestParams();
        Intent recibeIdTi = getIntent();
        enviar = new EnviarDatos(this);

        idTienda = recibeIdTi.getIntExtra("idTienda", 0);
        idPromotor = recibeIdTi.getIntExtra("idPromotor", 0);
        //idTipoTienda = recibeIdTi.getIntExtra("idTipo", 0);


        //registrar views
        viewsRegister();


        DB = new BDopenHelper(this);


        //fotos.setText("fotos(0)");


        Context context = getApplicationContext();
        PackageManager packageManager = context.getPackageManager();
        String packageName = context.getPackageName();


        try {
            myVersionName = packageManager.getPackageInfo(packageName, 0).versionName;

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }


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


        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .enableAutoManage(this, this)
                .build();


        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(1000);


        //builder object to verify a gps mode is in high accuracy

        /*LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(locationRequest);

        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());*/



    }

    private void setUpMenu() {

        menuRecycler = findViewById(R.id.recycler_menu_tienda);
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

            final MenuTiendaModel item2 = new MenuTiendaModel();
            item2.setIdMenu(2);
            item2.setNombreMenu("Surtido de mueble");
            item2.setImage("ic_blur_linear_grey600_24dp");
            array.add(item2);

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
            item6.setNombreMenu("Inteligencia de mercado \n(Precios propios y competencias)");
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

            final MenuTiendaModel item11 = new MenuTiendaModel();
            item11.setIdMenu(11);
            item11.setNombreMenu("Actualizar producto");
            item11.setImage("ic_autorenew_grey_600_24dp");
            array.add(item11);

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
        txtEncargado = findViewById(R.id.Encargado);
        frentes =  findViewById(R.id.frentes);
        surtido =  findViewById(R.id.surtido);
        exhi = findViewById(R.id.textExhibicio);
        fotos =  findViewById(R.id.text_fotos);

        btnFrente =  findViewById(R.id.buttonMensaje);
        btnSalidaTi =  findViewById(R.id.salidaTienda);
        btnEntrada =  findViewById(R.id.btnEnTienda);
        btnEncar =  findViewById(R.id.btnEncarg);
        btnExhib = findViewById(R.id.buttonExhib);
        btnUpdaPro =  findViewById(R.id.btnUpdaPro);
        btnInven = findViewById(R.id.btnInvenBode);
        btnSurtido = findViewById(R.id.buttonEnviar);
        btnTiendaError = findViewById(R.id.btnTiendaError);
        btnComentario = findViewById(R.id.btnComentario);
        btnInteligencia = findViewById(R.id.btnMenInt);
        btnFoto = findViewById(R.id.btnfoto);
        btnVentaPromedio = findViewById(R.id.btn_venta_promedio);

        btnCapturaGeneral = findViewById(R.id.captura_general);

        btnMateriales =  findViewById(R.id.btnMateriales);


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
        btnMateriales.setOnClickListener(this);


        // textView with listener
        frentes.setOnClickListener(this);
        inventario.setOnClickListener(this);
        exhi.setOnClickListener(this);
        fotos.setOnClickListener(this);

    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();

        inflater.inflate(R.menu.menu_tienda, menu);



        return true;
    }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:

                if (!Entrada && !Salida) {
                    this.finish();
                } else {
                    dialogoConfirmacionSalida();
                }

                return true;

            case R.id.direccion:

                saveAddress();

                return true;

            case R.id.producto_datos_tienda:

                saveDatosTienda();

                return true;


            case R.id.actualizar_encuesta:

                descargarEncuesta();

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

                        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, locationRequest, MenuTienda.this);


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
        SimpleDateFormat dSema = new SimpleDateFormat("w", Locale.getDefault());

        String fecha = dFecha.format(c.getTime());
        String hora = dHora.format(c.getTime());
        String sem = dSema.format(c.getTime());
        int semana = Integer.parseInt(sem);


        if (!Entrada) {

            try {

                location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

            } catch (SecurityException e) {
                e.printStackTrace();


            }

            BDopenHelper base = new BDopenHelper(getApplicationContext());


            if (location != null) {

                Entrada = true;
                base.insertarLocalizacion(idTienda, idPromotor, fecha, hora, location.getLatitude(), location.getLongitude(), 12, "E", 1, semana);

                Toast.makeText(this, "Entrada Guardada", Toast.LENGTH_SHORT).show();
                btnEntrada.post(new Runnable() {

                    public void run() {


                        btnEntrada.setBackgroundResource(R.drawable.custom_btn_dark_khaki);
                        btnEntrada.setTextColor(Color.WHITE);
                        enviar.enviarVisitas();
                    }

                });

            } else {

                activateGps("entrada");


                base.close();
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
                            SimpleDateFormat dSema = new SimpleDateFormat("w", Locale.getDefault());

                            String fecha = dFecha.format(c.getTime());
                            String hora = dHora.format(c.getTime());
                            String sem = dSema.format(c.getTime());
                            int semana = Integer.parseInt(sem);


                            try {

                                location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

                            } catch (SecurityException e) {
                                e.printStackTrace();
                            }


                            BDopenHelper base = new BDopenHelper(getApplicationContext());


                            if (location != null) {

                                base.insertarLocalizacion(idTienda, idPromotor, fecha, hora, location.getLatitude(), location.getLongitude(), 12, "S", 1, semana);

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
        switch (v.getId()) {

            case R.id.buttonMensaje:
                menuFrentes();

                break;

            case R.id.btnEnTienda:
                //entradaTienda();

                if (!Entrada) {
                    dialogoConfirmarEntrada();
                } else {
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
                Log.v("TextFrentes", "Onclick");
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

            case R.id.btnMateriales:
                openMateriales();
                break;


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
        updateInformation.updateInfo(idPromotor);


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


            Cursor cuEncargados = DB.contadorEncargados(idTienda, fecha);
            //txtEncargado.setText("Encargado ("+cuEncargados.getCount()+")");
            txtEncargado.setText(String.format(Locale.getDefault(), "Encargado %d", cuEncargados.getCount()));
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
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        try {
                            fotos.setText(String.format(Locale.getDefault(), "Fotos %d", DB.contarFotos(idTienda)));

                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } catch (Exception e) {
                e.printStackTrace();
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


        if (encuestaDisponible()) {


            Handler handler = new Handler();

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    dialogoEncuestas();
                }
            }, 3000);


        } else {
            verificarEncuesta();
        }


    }

    private void checkIns(String fecha) {
        try {
            Cursor cuEntra = DB.VisitaTienda(idTienda, fecha, "E");

            if (cuEntra.getCount() > 0) {

                Entrada = true;
                btnEntrada.setBackgroundResource(R.drawable.custom_btn_dark_khaki);


            } else {

                btnEntrada.setBackgroundResource(R.drawable.custom_btn_orange);
                btnEntrada.setTextColor(Color.WHITE);
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
                    Salida = true;
                    DB.close();

                    if (Entrada && Salida)
                        btnTiendaError.setVisibility(View.GONE);
                } else {

                    btnSalidaTi.setBackgroundResource(R.drawable.custom_btn_orange);
                    btnSalidaTi.setTextColor(Color.WHITE);
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


    private boolean encuestaDisponible() {


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


    private void descargarEncuesta() {

        UpdateInformation uI = new UpdateInformation(this);

        uI.actualizarEncuesta(idPromotor, idTienda);

    }

    private String fechaActual() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat dFecha = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        return dFecha.format(c.getTime());
    }

    @Override
    protected void onStart() {
        super.onStart();


        mGoogleApiClient.connect();


        verificarEncuesta();


    }

    private void startGpsAtMoment() {

        if (mGoogleApiClient.isConnected()) {

            final Handler handler = new Handler();

            handler.post(new Runnable() {
                @Override
                public void run() {


                    if (ActivityCompat.checkSelfPermission(MenuTienda.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MenuTienda.this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {


                        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, locationRequest, MenuTienda.this);


                    }


                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            if (mGoogleApiClient.isConnected())
                                LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, MenuTienda.this);
                        }
                    }, 10000);


                }
            });




        }


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


        mGoogleApiClient.disconnect();



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
		
	}




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
				dialogoEncargado();
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

		}


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



    @Override
    public void onConnected(@Nullable Bundle bundle) {


	    startGpsAtMoment();

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {

	    //Log.i("Location", "change");

	    this.location = location;

    }

    private class EscucharDialogoEncargado implements DialogInterface.OnClickListener{

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

	}

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
        View frentes = layoutInflater.inflate(R.layout.lista_capturados, nullParent);
        Listener flistener = new Listener();
        ListView listView = frentes.findViewById(R.id.listCapturados);

        InventariosCustomAdapter adapter = new InventariosCustomAdapter(this,R.layout.lista_capturados,getInventariosCapturados());

        listView.setAdapter(adapter);


        builder.setPositiveButton("Cerrar",flistener).setView(frentes);

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


	

}
