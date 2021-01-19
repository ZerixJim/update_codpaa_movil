package com.codpaa.activity;



import org.json.JSONException;
import org.json.JSONObject;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.AlertDialog;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import android.content.IntentSender;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import android.graphics.Color;

import android.net.Uri;

import android.os.Bundle;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.pm.PackageInfoCompat;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.codpaa.util.Configuracion;
import com.codpaa.R;
import com.codpaa.util.Utilities;

import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import com.codpaa.db.BDopenHelper;
import com.codpaa.db.DBAdapter;


import cz.msebera.android.httpclient.Header;


public class MainActivity extends AppCompatActivity implements OnClickListener, OnKeyListener {

	private DBAdapter dbAdapter;
	private EditText txtUserName;
	private EditText txtPassword;
	private Button btnLogin;
	private TextView valido;
	private Configuracion conf;
	private String username, password;
	private ProgressBar progressBar;
    private Activity act;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_main);
 
		txtUserName = findViewById(R.id.nombreEnca);
		txtPassword = findViewById(R.id.editCha2);
		btnLogin =  findViewById(R.id.buttonlogin);
		valido = findViewById(R.id.valido);
		progressBar = findViewById(R.id.progressUser);

		if (progressBar != null)
        	progressBar.setVisibility(View.GONE);

		btnLogin.setOnClickListener(this);

        act = this;
 
		conf = new Configuracion(this);
		
		dbAdapter = new DBAdapter(this);
		dbAdapter.open();


		this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		
		txtPassword.setOnKeyListener(this);
		
		if(conf.getUserName() != null){
			txtUserName.setText(conf.getUserName());
			
		}




		/*try {

			AccountManager accountManager = (AccountManager) act.getSystemService(Context.ACCOUNT_SERVICE);

			Account account = new Account(getResources().getString(R.string.app_name), "com.codpaa");

			if (accountManager.getPassword(account) != null){

				Log.d("Account", "" + accountManager.getAccounts().toString());


				String user = accountManager.getUserData(account, "user");



				iniciarActivity(user);

			}else{

				SQLiteDatabase db = new BDopenHelper(this).getWritableDatabase();

				db.delete(DbEstructure.Usuario.TABLE_NAME, null, null);

				db.close();


			}

		}catch (SecurityException e){

			e.printStackTrace();
		}*/


		AsyncHttpClient clienteVersion = new AsyncHttpClient();
        clienteVersion.post(Utilities.WEB_SERVICE_CODPAA+"codpaaVersion.json",new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                try {

                    try {
                        Context context = getApplicationContext();

                        PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
                        long versionCode = PackageInfoCompat.getLongVersionCode(packageInfo);

                        int versionCodeRemote = response.getInt("versionCode");

                        if(versionCode < versionCodeRemote){
                            crearPrompt(response.getString("version"));
                        }


                    } catch (PackageManager.NameNotFoundException e) {
                        e.printStackTrace();
                    }



                }catch (JSONException e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });


	}

	private void checkPermisosAndGpsEnable() {



		LocationRequest locationRequest = LocationRequest.create();

		locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);


		LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
		builder.addLocationRequest(locationRequest);

		SettingsClient client = LocationServices.getSettingsClient(this);

		Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());


		task.addOnFailureListener(this, new OnFailureListener() {
			@Override
			public void onFailure(@NonNull Exception e) {

				if (e instanceof ResolvableApiException) {
					// Location settings are not satisfied, but this can be fixed
					// by showing the user a dialog.
					try {
						// Show the dialog by calling startResolutionForResult(),
						// and check the result in onActivityResult().
						ResolvableApiException resolvable = (ResolvableApiException) e;
						resolvable.startResolutionForResult(MainActivity.this,
								150);
					} catch (IntentSender.SendIntentException sendEx) {
						// Ignore the error.
					}
				}

			}
		});



       /* final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT ){

            try {



				Log.d("LocationServices", client.checkLocationSettings())


                int locationMode = Settings.Secure.getInt(this.getContentResolver(), Settings.Secure.LOCATION_MODE);
                //Log.i("location Mode", " " + locationMode);


                if (locationMode != Settings.Secure.LOCATION_MODE_HIGH_ACCURACY || !manager.isProviderEnabled(LocationManager.GPS_PROVIDER)){


                    androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
                    builder.setTitle("Aviso")
                            .setMessage("por politicas de vanguardia, y para el buen funcionamiento de la plataforma, es necesario " +
                                    " que el GPS este Activado y en modo de PRECISION ALTA, gracias por su comprension ")
                            .setPositiveButton("Activar", new DialogInterface.OnClickListener(){

                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {



                                    startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));

                                }
                            }).setCancelable(false).create().show();

                }


            } catch (Exception e) {
                e.printStackTrace();
            }

        }else {


		    String locationMode = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);

		    Log.i("LocationMode", " " + locationMode);


        }*/



	}

	Runnable runProgress = new Runnable() {
        @Override
        public void run() {
            progressBar.setVisibility(View.GONE);

        }
    };




	@Override
	public void onClick(View arg0) {
		if (arg0.getId() == R.id.buttonlogin) {
			login();
		}
		
		
	}


    private void loginAsync(final String usuario, final String pass){

        AsyncHttpClient cliente = new AsyncHttpClient();
		//cliente.setTimeout(5000);
        RequestParams rp = new RequestParams();
        rp.put("solicitud","usersensitive");
        rp.put("user",usuario);
        rp.put("pass", pass);




        cliente.get(this, Utilities.WEB_SERVICE_CODPAA+"serv.php", rp, new JsonHttpResponseHandler() {


            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                if (response != null) {

                    try {
                        if (response.getBoolean("response")) {


                            BDopenHelper b = new BDopenHelper(act);
                            b.vaciarTabla("usuarios");


							AccountManager accountManager  = (AccountManager) getApplicationContext().getSystemService(Context.ACCOUNT_SERVICE);


							Account account = new Account( getResources().getString(R.string.app_name) , "com.codpaa");

							if (accountManager.getPassword(account) == null && response.getString("status").equals("a")){


								final Bundle bundle = new Bundle();
								bundle.putString("user", username);

								accountManager.addAccountExplicitly(account,password, bundle);


							}



                            b.insertarUsuarios(response.getInt("i"), response.getString("nombre"),
                                    response.getString("usuario").trim(),
                                    response.getString("password").trim(), response.getString("status"));
                            act.runOnUiThread(runProgress);
                            valido.post(new Runnable() {
                                public void run() {
                                    valido.setText(R.string.loaded_user);
                                    valido.setTextColor(Color.GREEN);

                                }

                            });

                            if(dbAdapter.Login(response.getString("usuario").trim(),response.getString("password").trim())){

                                String[] separated = response.getString("nombre").split(" ");

                                Toast.makeText(MainActivity.this,"Bienvenido(a) "+separated[0]+" que tengas un excelente dia", Toast.LENGTH_SHORT).show();
                                conf.setUserUser(username);
                                iniciarActivity(username);
                            }else {

								valido.post(new Runnable() {
									public void run() {
										valido.setText(R.string.user_disable);
										valido.setTextColor(Color.RED);

									}

								});

							}
                        } else {
                            Toast.makeText(getApplicationContext(), "Usuario o Contraseña incorrecto", Toast.LENGTH_SHORT).show();
                            act.runOnUiThread(runProgress);
                            valido.post(new Runnable() {
                                public void run() {
                                    valido.setText(R.string.invalid_user);
                                    valido.setTextColor(Color.RED);

                                }

                            });
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Toast.makeText(getApplicationContext(), "Se perdio la conexion, intentelo mas tarde", Toast.LENGTH_SHORT).show();
                act.runOnUiThread(runProgress);
                valido.post(new Runnable() {
                    public void run() {
                        valido.setText(R.string.out_conection);
                        valido.setTextColor(Color.RED);

                    }

                });
            }


        });

    }
	
	
	
	private void login(){
		
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		if (imm != null) {
			imm.hideSoftInputFromWindow(txtUserName.getWindowToken(), 0);
			imm.hideSoftInputFromWindow(txtPassword.getWindowToken(), 0);
		}
		username = txtUserName.getText().toString().trim();
		password = txtPassword.getText().toString().trim();
		valido.setText("");

		btnLogin.setClickable(false);

		if (username.length() > 0 && password.length() > 0) {
			try {
				
				if (dbAdapter.Login(username, password)) {

					conf.setUserUser(username);
					iniciarActivity(username);
					
				}else{
					Toast.makeText(this, "Espere....verificando en el Servidor", Toast.LENGTH_SHORT).show();
					progressBar.setVisibility(View.VISIBLE);
					loginAsync(username,password);

					
				}

			} catch (Exception e) {
				Toast.makeText(MainActivity.this, "Ocurrio algun problema",Toast.LENGTH_SHORT).show();
				Log.e("Error", "Exception", e);

			}
			
		} else {
			Toast.makeText(MainActivity.this,"Escriba Usuario y Contraseña", Toast.LENGTH_SHORT).show();
		}

		btnLogin.setClickable(true);



	}
	
	private void iniciarActivity(String value){
		
		Intent intent = new Intent(this, MenuPrincipal.class);
		intent.putExtra("nombre", value);
		startActivity(intent);
		
		finish();

	}


	@Override
	protected void onResume() {
		super.onResume();
		if(txtUserName.getText().length() > 0){
			txtPassword.requestFocus();
		}else{
			txtUserName.requestFocus();
		}


        checkPermisosAndGpsEnable();


	}

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbAdapter.close();
    }

    @Override
	public boolean onKey(View v, int keyCode, KeyEvent event) {


		
        if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN){
            Log.d("KeyEvent","Enter");
        	login();
           
            return true;
        }
        return false;
	}
	
	public void iniciarMarket(){
		final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
		try {
		    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
		} catch (android.content.ActivityNotFoundException anfe) {
		    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + appPackageName)));
		}
	}
	
	public void crearPrompt(String version){
		LayoutInflater li = LayoutInflater.from(this);
		View prompt = li.inflate(R.layout.promptversion, null);

        TextView textVersion = prompt.findViewById(R.id.textVersionPrompt);
        textVersion.setText(version);
		
		AlertDialog.Builder alertDialogB = new AlertDialog.Builder(this);
		alertDialogB.setView(prompt);
		
		alertDialogB.setCancelable(false).setPositiveButton("Actualizar", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
				iniciarMarket();
				finish();
			}
		});

		
		AlertDialog alertDialog = alertDialogB.create();
		alertDialog.show();
	}




	
}
