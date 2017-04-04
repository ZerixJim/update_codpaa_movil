package com.codpaa.util;

/*
 * Created by grim on 04/04/2017.
 */

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
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

    private List<Application> getInstallApps(){
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


    public void sentInstallApps(){


        final Configuracion conf = new Configuracion(context);


        Calendar c = Calendar.getInstance();
        SimpleDateFormat dFecha = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        final String fecha = dFecha.format(c.getTime());

        if (conf.getInstallApps() == null || !conf.getInstallApps().equals(fecha)){

            if (getInstallApps().size() > 0){

                Log.d(LOG, "entró a la condicion");

                AsyncHttpClient client = new AsyncHttpClient();

                Gson gson = new Gson();

                JsonInstallApps json = new JsonInstallApps();

                json.setIdPromotor(idUsuario);
                json.setFecha(fecha);
                json.setGoogleApplication(getInstallApps());
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



}
