package com.codpaa.updates;


import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

public class UpdateProductos extends JsonHttpResponseHandler {

    public UpdateProductos(){

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
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onProgress(int bytesWritten, int totalSize) {
        super.onProgress(bytesWritten, totalSize);
    }
}
