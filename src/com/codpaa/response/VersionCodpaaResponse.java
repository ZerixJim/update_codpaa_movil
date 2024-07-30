package com.codpaa.response;
/*
 * Created by Gustavo on 29/02/2016.
 */

import android.util.Log;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class VersionCodpaaResponse  extends JsonHttpResponseHandler{

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
}
