package com.codpaa.response;

import android.util.Log;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/*
 * Created by grim on 11/11/2016.
 */

public class ImageUpload extends JsonHttpResponseHandler {

    public ImageUpload(){

    }


    @Override
    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
        super.onSuccess(statusCode, headers, response);



    }

    @Override
    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
        super.onFailure(statusCode, headers, throwable, errorResponse);

        if(errorResponse != null) {
            Log.e("ERROR HTTP RESPONSE", errorResponse.toString());
        } else {
            Log.e("ERROR HTTP RESPONSE", "ErrorResponse is null");
        }
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onFinish() {
        super.onFinish();
    }
}
