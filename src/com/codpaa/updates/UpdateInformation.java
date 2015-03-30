package com.codpaa.updates;


import android.content.Context;

import com.codpaa.listeners.ResponseExhibiciones;
import com.codpaa.listeners.ResponseRuta;
import com.codpaa.listeners.ResponseTiendas;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;


public class UpdateInformation{


    private final static String URL_INFO = "http://promotoressumma.com/codpaa/serv.php";

    Context _context;


    public UpdateInformation(Context context){
        this._context = context;
    }



    public void actualizarRuta(int idPromotor){

        AsyncHttpClient clientRuta = new AsyncHttpClient();
        ResponseRuta responseRuta = new ResponseRuta(_context);
        RequestParams rp = new RequestParams();
        rp.put("solicitud","rutas");
        rp.put("id",String.valueOf(idPromotor));

        clientRuta.get(_context, URL_INFO, rp, responseRuta);

    }

    public void actualizarTiendas(int idPromotor){
        AsyncHttpClient clientTiendas = new AsyncHttpClient();
        ResponseTiendas responseTienda = new ResponseTiendas(_context);
        RequestParams rp = new RequestParams();

        rp.put("solicitud","client");
        rp.put("id",String.valueOf(idPromotor));

        clientTiendas.get(_context,URL_INFO,rp,responseTienda);


    }

    public void actualizarExhibiciones(){
        AsyncHttpClient clientExh = new AsyncHttpClient();
        ResponseExhibiciones responseExh = new ResponseExhibiciones(_context);
        RequestParams rp = new RequestParams();

        rp.put("solicitud","exhibicion");

        clientExh.get(_context,URL_INFO,rp,responseExh);
    }


    public void actualizarMarca(){}

    public void actualizarProducto(){

    }




}
