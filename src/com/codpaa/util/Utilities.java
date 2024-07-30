package com.codpaa.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.provider.Settings;
import android.text.format.DateUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.codpaa.db.BDopenHelper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import cz.msebera.android.httpclient.Header;

public class Utilities {

    public final static String TAG = Utilities.class.getSimpleName();

    static final String BASE_URL = "http://plataformavanguardia.net";
    public static final String WEB_SERVICE_PATH = "http://plataformavanguardia.net/codpaa/webservice";
    public static final String WEB_SERVICE_CODPAA = "https://service.plataformavanguardia.net/";
    public static final String SERV_PHP = "serv.php";
    public static final String PRODUCT_PATH = "https://codpaa.plataformavanguardia.net/images/productos/";
    public static final String MARCA_PATH = "https://codpaa.plataformavanguardia.net/images/marcas/";
    public static final String FORMATOS_PATH = "https://codpaa.plataformavanguardia.net/images/formatos/";
    public static final String WEB_SERVICE_CODPAA_TEST = "http://plataformavanguardia.net/test/webservice/";


    public static final String API_TEST = "https://apitest.vanguardiagps.com/v2/";
    public static final String API_PRODUCTION = "https://api.plataformavanguardia.net/v2/";

    public static final int HOST_MODE_PRODCTION = 1;
    public static final int HOST_MODE_TEST = 2;


    public static final String TABLE_MENSAJE = "mensaje";

    public static final String DATETIME_FORMAT_USA = "yyyy-MM-dd HH:mm:ss";
    public static final String DATETIME_FORMAT_MX = "dd-MM-yyyy HH:mm:ss";

    public static final String DATE_FORMAT_USA = "yyyy-MM-dd";



    public static boolean verificarConexion(Context context) {

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();

        return netInfo != null && netInfo.isConnected();
    }


    public static String getHostService(int mode){


        if (mode == HOST_MODE_TEST ){

            return WEB_SERVICE_CODPAA_TEST;

        }else {

            return WEB_SERVICE_CODPAA;

        }

    }


    public static String getTimeAgo(String formato, String myTime){
        String response = "";
        SimpleDateFormat dateFormat = new SimpleDateFormat(formato, Locale.getDefault());

        try {
            Date date = dateFormat.parse(myTime);

            long dateMilli = date.getTime();
            long currentTime = System.currentTimeMillis();

            response = DateUtils.getRelativeTimeSpanString(dateMilli,
                    currentTime, DateUtils.SECOND_IN_MILLIS).toString();

            //Log.d("fecha", " " + DateUtils.getRelativeTimeSpanString(dateMilli, currentTime, DateUtils.MINUTE_IN_MILLIS));

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return response;
    }


    public static String getCurrentDate(){

        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT_USA, Locale.getDefault());

        return dateFormat.format(new Date());

    }


    public static boolean isAutoTime(Context c){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1){
            return Settings.Global.getInt(c.getContentResolver(), Settings.Global.AUTO_TIME, 0) == 1;

        }else {


            return android.provider.Settings.System.getInt(c.getContentResolver(), Settings.System.AUTO_TIME, 0) == 1;
        }

    }

    public static void getFirebaseToken(final Context context){

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()){
                            Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        String token = task.getResult();
                        //Log.d(TAG, "token " + token);

                        sendRegistrationToServer(context, token);


                    }
                });

    }

    public static void sendRegistrationToServer(Context context,final String token) {

        BDopenHelper db = new BDopenHelper(context);
        int idPromotor = db.getIdPromotor();

        Log.w(TAG, "idPromo" + idPromotor);

        // Add custom implementation, as needed.
        if (idPromotor > 0){

            final Configuracion conf = new Configuracion(context);

            AsyncHttpClient client = new AsyncHttpClient();

            RequestParams rp = new RequestParams();
            rp.put("id", idPromotor);
            rp.put("solicitud", "update_token");
            rp.put("token_gcm", token);

            client.get(Utilities.WEB_SERVICE_CODPAA + "serv.php", rp, new JsonHttpResponseHandler() {

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);
                    Log.d(TAG, "token send to server "+ token);

                    if (response != null) {
                        try {
                            if (response.getInt("success") == 1) {

                                conf.setToken(token);
                                conf.setTokenFirebaseSent(true);

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                    //sharedPreferences.edit().putBoolean(QuickstartPreferences.SENT_TOKEN_TO_SERVER, false).apply();

                    if(errorResponse != null) {
                        Log.e("ERROR HTTP RESPONSE", errorResponse.toString());
                    } else {
                        Log.e("ERROR HTTP RESPONSE", "ErrorResponse is null");
                    }


                }


            });






        }



    }

    public static String getDateTime(){

        Calendar c = Calendar.getInstance();
        SimpleDateFormat dFecha = new SimpleDateFormat(DATETIME_FORMAT_USA, Locale.getDefault());
        return dFecha.format(c.getTime());


    }







}
