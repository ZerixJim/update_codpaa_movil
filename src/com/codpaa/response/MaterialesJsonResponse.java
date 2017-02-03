package com.codpaa.response;

import android.content.Context;
import android.util.Log;

import com.loopj.android.http.JsonHttpResponseHandler;


import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/*
 * Created by grim on 2/02/17.
 */

public class MaterialesJsonResponse extends JsonHttpResponseHandler {

    private Context context;

    public MaterialesJsonResponse(Context context) {
        this.context = context;
    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
        super.onSuccess(statusCode, headers, response);

        if (response != null){
            Log.d("JsonResponse", response.toString());

        }
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
        super.onFailure(statusCode, headers, throwable, errorResponse);
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
        super.onFailure(statusCode, headers, responseString, throwable);
    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, String responseString) {
        super.onSuccess(statusCode, headers, responseString);
    }
}
