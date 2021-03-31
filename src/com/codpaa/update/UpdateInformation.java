package com.codpaa.update;


import android.content.Context;


import com.codpaa.listener.ResponseEncuesta;
import com.codpaa.response.HttpResponseInfo;
import com.codpaa.response.HttpResponseProducts;
import com.codpaa.util.Utilities;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;



public class UpdateInformation{


    private final static String URL_INFO = "serv.php";

    private Context _context;


    public UpdateInformation(Context context){
        this._context = context;
    }



    public void updateInfo(int idPromotor){


        AsyncHttpClient client = new AsyncHttpClient(true, 80, 443);

        HttpResponseInfo responseInfo = new HttpResponseInfo(_context);

        RequestParams rp = new RequestParams();
        rp.put("solicitud" , "info");
        rp.put("id", idPromotor);

        rp.put("idPromotor", idPromotor);

        client.addHeader("Authorization", "324S35574324S13D45463_-r2333434+4");



        //client.get(_context, Utilities.WEB_SERVICE_CODPAA + URL_INFO, rp, responseInfo);


        client.get(Utilities.API_PRODUCTION + "promotores/promo-data",rp, responseInfo);




    }


    public void downloadProduct(int idPromotor){

        AsyncHttpClient client = new AsyncHttpClient(true, 80, 443);

        HttpResponseProducts responseInfo = new HttpResponseProducts(_context);

        RequestParams rp = new RequestParams();
        rp.put("solicitud" , "info");
        rp.put("id", idPromotor);

        rp.put("idPromotor", idPromotor);

        client.addHeader("Authorization", "324S35574324S13D45463_-r2333434+4");



        //client.get(_context, Utilities.WEB_SERVICE_CODPAA + URL_INFO, rp, responseInfo);


        client.get(Utilities.API_PRODUCTION + "promotores/promo-data-products",rp, responseInfo);



    }



    public void actualizarEncuesta(int idPromotor, int idTienda){
        AsyncHttpClient client = new AsyncHttpClient();
        ResponseEncuesta response = new ResponseEncuesta(_context, idTienda);
        RequestParams rp = new RequestParams();

        rp.put("solicitud", "encuesta");
        rp.put("id", String.valueOf(idPromotor));
        rp.put("idTienda", String.valueOf(idTienda));

        //client.setTimeout(5000);
        client.get(_context, Utilities.WEB_SERVICE_CODPAA + URL_INFO, rp, response);

    }




}
