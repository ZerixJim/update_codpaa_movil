package com.codpaa.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.provider.Settings;
import android.text.format.DateUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Utilities {

    static final String BASE_URL = "http://plataformavanguardia.net";
    public static final String WEB_SERVICE_PATH = "http://plataformavanguardia.net/codpaa/webservice";
    public static final String WEB_SERVICE_CODPAA = "http://service.plataformavanguardia.net/";
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




}
