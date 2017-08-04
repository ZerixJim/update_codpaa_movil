package com.codpaa.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class Utilities {
    static final String SERVER_IP = "162.144.139.133";
    static final String SERVER_IP_2 = "162.144.139.31";
    static final String BASE_URL = "http://plataformavanguardia.net";
    public static final String WEB_SERVICE_PATH = "http://plataformavanguardia.net/codpaa/webservice";
    public static final String WEB_SERVICE_CODPAA = "http://service.plataformavanguardia.net/";
    public static final String SERV_PHP = "serv.php";
    public static final String PRODUCT_PATH = "http://codpaa.plataformavanguardia.net/images/productos/";
    public static final String MARCA_PATH = "http://codpaa.plataformavanguardia.net/images/marcas/";
    public static final String WEB_SERVICE_CODPAA_TEST = "http://plataformavanguardia.net/test/webservice/";


    public static final String TABLE_MENSAJE = "mensaje";



    public static boolean verificarConexion(Context context) {

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();

        return netInfo != null && netInfo.isConnected();
    }


}
