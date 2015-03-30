package com.codpaa.listeners;

import android.app.ProgressDialog;
import android.content.Context;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import BD.BDopenHelper;


public class ResponseExhibiciones extends JsonHttpResponseHandler {


    Context _context;

    private ProgressDialog pdia;

    public ResponseExhibiciones(Context context){
        this._context = context;
        pdia = new ProgressDialog(context);
        pdia.setTitle("Exhibiciones");
        pdia.setMessage("Descargando Exhibiciones");
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
        if (response != null) {
            try {
                parseJSONExhi(response.getJSONArray("E"));
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

    private void parseJSONExhi(JSONArray exhiArray) throws JSONException {
        BDopenHelper b = new BDopenHelper(_context);
        b.vaciarTabla("exhibiciones");

        for(int i= 0; i < exhiArray.length(); i++) {
            b.insertarTipoExhibicion(exhiArray.getJSONObject(i).getInt("IE"),
                    exhiArray.getJSONObject(i).getString("N"));
        }
    }
}
