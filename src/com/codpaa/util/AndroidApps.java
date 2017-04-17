package com.codpaa.util;

/*
 * Created by grim on 04/04/2017.
 */

import android.Manifest;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.codpaa.model.Application;
import com.codpaa.model.JsonInstallApps;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import cz.msebera.android.httpclient.Header;


public class AndroidApps {

    private Context context;
    private static final String LOG = "AndriodApps";
    private int idUsuario;

    public AndroidApps(Context context, int idUsuario) {
        this.context = context;
        this.idUsuario = idUsuario;
    }

    private List<Application> getGpsInstallApps(){
        PackageManager pm = context.getPackageManager();

        List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);
        List<Application> packageReporter = new ArrayList<>();

        for (ApplicationInfo packageInfo : packages) {

            if (packageInfo.packageName.contains("gps") || packageInfo.packageName.contains("fake")){

                final Application app = new Application();

                app.setIdGoogle(packageInfo.packageName);

                packageReporter.add(app);
            }

        }


        return packageReporter;
    }

    private List<Application> getInstallAppsByWeek(){

        PackageManager pm = context.getPackageManager();

        List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);
        List<Application> packageReporter = new ArrayList<>();

        for (ApplicationInfo packageInfo : packages) {

            final Application app = new Application();

            app.setIdGoogle(packageInfo.packageName);

            packageReporter.add(app);

        }


        return packageReporter;


    }


    public void sentInstallAppsByWeek() {

        Calendar c = Calendar.getInstance();
        SimpleDateFormat dFecha = new SimpleDateFormat("w", Locale.getDefault());
        final SimpleDateFormat fFecha = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        final String semana = dFecha.format(c.getTime());
        final String fecha = fFecha.format(c.getTime());



        final Configuracion conf = new Configuracion(context);

        Log.d(LOG, "Entro " + conf.getInstallAppsWeek());

        if (conf.getInstallAppsWeek() == -1 || conf.getInstallAppsWeek() != Integer.valueOf(semana)){
            AsyncHttpClient client = new AsyncHttpClient();

            Gson gson = new Gson();

            JsonInstallApps json = new JsonInstallApps();

            json.setIdPromotor(idUsuario);
            json.setFecha(fecha);
            json.setImei(getIMEI());
            json.setGoogleApplication(getInstallAppsByWeek());
            RequestParams rp = new RequestParams();

            rp.put("json", gson.toJson(json));

            client.post(context,Utilities.WEB_SERVICE_CODPAA + "sent_install_apps.php", rp, new JsonHttpResponseHandler(){
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                    Log.d("Apps", response.toString());


                    try {
                        if (response.getBoolean("success")){
                            Log.d("Apps", "Recibidas");

                            conf.setInstallApssWeek(Integer.valueOf(semana));


                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {

                    Log.e("Apps", errorResponse.toString());

                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                    Log.e("Apps", errorResponse.toString());
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, String responseString) {
                    Log.d("Apps", responseString);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                    Log.e("Apps", "Error al enviar " + responseString + " " + throwable + " " + statusCode);

                }
            });




        }



    }


    public void sentInstallApps(){


        final Configuracion conf = new Configuracion(context);


        Calendar c = Calendar.getInstance();
        SimpleDateFormat dFecha = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        final String fecha = dFecha.format(c.getTime());

        Log.d(LOG, "IMEI " + getIMEI());

        if (conf.getInstallApps() == null || !conf.getInstallApps().equals(fecha)){

            if (getGpsInstallApps().size() > 0){



                AsyncHttpClient client = new AsyncHttpClient();

                Gson gson = new Gson();

                JsonInstallApps json = new JsonInstallApps();

                json.setIdPromotor(idUsuario);
                json.setFecha(fecha);
                json.setImei(getIMEI());
                json.setGoogleApplication(getGpsInstallApps());
                RequestParams rp = new RequestParams();

                rp.put("json", gson.toJson(json));

                client.post(context,Utilities.WEB_SERVICE_CODPAA + "sent_install_apps.php", rp, new JsonHttpResponseHandler(){
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                        Log.d("Apps", response.toString());


                        try {
                            if (response.getBoolean("success")){
                                Log.d("Apps", "Recibidas");

                                conf.setInstallApps(fecha);



                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {

                        Log.e("Apps", errorResponse.toString());

                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                        Log.e("Apps", errorResponse.toString());
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String responseString) {
                        Log.d("Apps", responseString);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                        Log.e("Apps", "Error al enviar " + responseString + " " + throwable + " " + statusCode);

                    }
                });


            }



        }else {
            Log.d(LOG, "ya se envio");
        }




    }

    public String getIMEI(){
        String imei = "sin Permiso";

        if(ContextCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) ==  PackageManager.PERMISSION_GRANTED){

            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

            imei = telephonyManager.getDeviceId();
        }

        return imei;
    }



}
