package com.codpaa.update;


import android.content.Context;

import com.codpaa.listener.ResponseEncuesta;
import com.codpaa.listener.ResponseExhibiciones;
import com.codpaa.listener.ResponseMarcas;
import com.codpaa.listener.ResponseProductos;
import com.codpaa.listener.ResponseRuta;
import com.codpaa.response.HttpResponseInfo;
import com.codpaa.util.Utilities;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;



public class UpdateInformation{


    private final static String URL_INFO = "serv.php";

    Context _context;


    public UpdateInformation(Context context){
        this._context = context;
    }



    public void updateInfo(int idPromotor){

        AsyncHttpClient client = new AsyncHttpClient();

        HttpResponseInfo responseInfo = new HttpResponseInfo(_context);

        RequestParams rp = new RequestParams();
        rp.put("solicitud" , "info");
        rp.put("id", idPromotor);

        client.get(_context, Utilities.WEB_SERVICE_CODPAA + URL_INFO, rp, responseInfo);



    }



    public void actualizarRuta(int idPromotor){

        
        AsyncHttpClient clientRuta = new AsyncHttpClient();
        ResponseRuta responseRuta = new ResponseRuta(_context);
        RequestParams rp = new RequestParams();
        rp.put("solicitud","rutas");
        rp.put("id",String.valueOf(idPromotor));


        clientRuta.setTimeout(5000);
        clientRuta.get(_context, Utilities.WEB_SERVICE_CODPAA + URL_INFO, rp, responseRuta);

    }


    public void actualizarExhibiciones(int idPromotor){
        AsyncHttpClient clientExh = new AsyncHttpClient();
        ResponseExhibiciones responseExh = new ResponseExhibiciones(_context);
        RequestParams rp = new RequestParams();

        rp.put("solicitud","exhibicion");
        rp.put("id", idPromotor);

        clientExh.setTimeout(5000);
        clientExh.get(_context, Utilities.WEB_SERVICE_CODPAA + URL_INFO,rp,responseExh);
    }


    public void actualizarMarca(int idPromotor){
        AsyncHttpClient clientMarc = new AsyncHttpClient();
        ResponseMarcas responseMarc = new ResponseMarcas(_context);
        RequestParams rp = new RequestParams();

        rp.put("solicitud","marcaid");
        rp.put("id",String.valueOf(idPromotor));

        clientMarc.setTimeout(5000);
        clientMarc.get(_context, Utilities.WEB_SERVICE_CODPAA + URL_INFO,rp,responseMarc);

    }

    public void actualizarProducto(int idPromotor){
        AsyncHttpClient clientPro = new AsyncHttpClient();
        ResponseProductos responseProductos = new ResponseProductos(_context);
        RequestParams rp = new RequestParams();

        rp.put("solicitud","productoid");
        rp.put("id",String.valueOf(idPromotor));

        clientPro.setTimeout(5000);
        clientPro.get(_context, Utilities.WEB_SERVICE_CODPAA + URL_INFO,rp,responseProductos);

    }

    public void actualizarEncuesta(int idPromotor){
        AsyncHttpClient client = new AsyncHttpClient();
        ResponseEncuesta response = new ResponseEncuesta(_context);
        RequestParams rp = new RequestParams();

        rp.put("solicitud", "encuesta");
        rp.put("id", String.valueOf(idPromotor));

        client.setTimeout(5000);
        client.get(_context, Utilities.WEB_SERVICE_CODPAA_TEST + URL_INFO, rp, response);

    }




}
