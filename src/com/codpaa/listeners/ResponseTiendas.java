package com.codpaa.listeners;

import android.app.ProgressDialog;
import android.content.Context;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import BD.BDopenHelper;


public class ResponseTiendas extends JsonHttpResponseHandler{

    Context _context;
    private ProgressDialog pdia;

    public ResponseTiendas(Context context){
        this._context = context;
        pdia = new ProgressDialog(context);
        pdia.setTitle("Tiendas");
        pdia.setMessage("Descargando Tiendas");
        pdia.setIndeterminate(false);
        pdia.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
    }

    @Override
    public void onStart() {
        super.onStart();

        pdia.show();

    }

    @Override
    public void onProgress(int bytesWritten, int totalSize) {
        super.onProgress(bytesWritten, totalSize);

        pdia.setMax(totalSize);
        pdia.setProgress(bytesWritten);
    }

    @Override
    public void onSuccess(int statusCode, JSONObject response) {
        super.onSuccess(statusCode, response);

        if (response != null){
            try {

                parseJsonTiendas(response.getJSONArray("C"));
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onFailure(int statusCode, Throwable e, JSONObject errorResponse) {
        super.onFailure(statusCode, e, errorResponse);
    }

    @Override
    public void onFinish() {
        super.onFinish();

        pdia.dismiss();
    }

    private void parseJsonTiendas(JSONArray tiendasArray) throws JSONException {
        BDopenHelper b = new BDopenHelper(_context);
        b.vaciarTabla("clientes");
        for(int i=0; i<tiendasArray.length(); i++){
            b.insertarClientes(tiendasArray.getJSONObject(i).getInt("IT"),
                    tiendasArray.getJSONObject(i).getString("G"),
                    tiendasArray.getJSONObject(i).getString("S"),
                    tiendasArray.getJSONObject(i).getString("X"),
                    tiendasArray.getJSONObject(i).getString("Y"));
        }

    }
}
