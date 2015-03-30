package com.codpaa.listeners;

import android.app.ProgressDialog;
import android.content.Context;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;


public class ResponseMarcas extends JsonHttpResponseHandler{



    Context _context;

    private ProgressDialog pdia;

    public ResponseMarcas(Context context){
        this._context = context;
        pdia = new ProgressDialog(context);
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
}
