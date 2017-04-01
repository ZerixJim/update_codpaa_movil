package com.codpaa.activity;


import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
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
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import com.codpaa.db.BDopenHelper;
import com.codpaa.db.DBAdapter;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;


public class MainActivity extends AppCompatActivity implements OnClickListener, OnKeyListener {

	DBAdapter dbAdapter;
	EditText txtUserName;
	EditText txtPassword;
	Button btnLogin;
	TextView valido;
	Configuracion conf;
	String username, password;
	ProgressBar progressBar;
    Activity act;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_main);
 
		txtUserName = (EditText) findViewById(R.id.nombreEnca);
		txtPassword = (EditText) findViewById(R.id.editCha2);
		btnLogin = (Button) findViewById(R.id.buttonlogin);
		valido = (TextView) findViewById(R.id.valido);
		progressBar = (ProgressBar) findViewById(R.id.progressUser);

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


		PackageManager pm = getPackageManager();

		List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);
		List<ApplicationInfo> packageReporter = new ArrayList<>();

		for (ApplicationInfo packageInfo : packages) {

			if (packageInfo.packageName.contains("gps")){

				if (packageInfo.packageName.contains("fake"))
					packageReporter.add(packageInfo);

			}

		}


		for (ApplicationInfo packagess: packageReporter){
			Log.d("Application Gps", "Package:" + packagess.packageName);
		}




		AsyncHttpClient clienteVersion = new AsyncHttpClient();
        clienteVersion.post(Utilities.WEB_SERVICE_CODPAA+"codpaaVersion.json",new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                try {

                    try {
                        Context context = getApplicationContext();
                        PackageManager packageManager = context.getPackageManager();
                        String packageName = context.getPackageName();

                        int versionName = packageManager.getPackageInfo(packageName, 0).versionCode;


                        if(versionName < response.getInt("versionCode")){
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

    Runnable runProgress = new Runnable() {
        @Override
        public void run() {
            progressBar.setVisibility(View.GONE);

        }
    };

	
	
	@Override
	protected void onStart() {
		super.onStart();
		
		
	}



	@Override
	public void onClick(View arg0) {
		switch(arg0.getId()){
		
		
		case R.id.buttonlogin:
			
			login();
			
			break;
			
		
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
		imm.hideSoftInputFromWindow(txtUserName.getWindowToken(), 0);
		imm.hideSoftInputFromWindow(txtPassword.getWindowToken(), 0);
		username = txtUserName.getText().toString().trim();
		password = txtPassword.getText().toString().trim();
		valido.setText("");

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
	}
	
	private void iniciarActivity(String value){
		
		Intent intent = new Intent(this, MenuPrincipal.class);
		intent.putExtra("nombre", value);
		startActivity(intent);
		
		finish();

	}


	@Override
	protected void onResume() {
		if(txtUserName.getText().length() > 0){
			txtPassword.requestFocus();
		}else{
			txtUserName.requestFocus();
		}
		super.onResume();
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
		final ViewGroup nullParent = null;
		View prompt = li.inflate(R.layout.promptversion, nullParent);

        TextView textVersion = (TextView) prompt.findViewById(R.id.textVersionPrompt);
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
