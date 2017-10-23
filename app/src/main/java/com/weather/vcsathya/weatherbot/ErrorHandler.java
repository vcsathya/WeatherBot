package com.weather.vcsathya.weatherbot;

import android.content.Context;
import android.util.Log;

import com.weather.vcsathya.weatherbot.util.AppUtil;

import org.json.JSONObject;

public class ErrorHandler {

    public void getErrorMessage(String errorResponse, Context context) {
        try {
            JSONObject jsonObject = new JSONObject(errorResponse);
            handleError(jsonObject.getInt("cod"), context);
            Log.e("N/W response: ", errorResponse);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("n/w response JSON: ", e.getMessage());
        }
    }

    private void handleError(int cod, Context context) {
        AppUtil appUtil = new AppUtil();
        switch(cod) {
            case 404 :
                appUtil.makeToast(context, context.getResources().getString(R.string.invalid_location));
                break;
            case 400 :
                appUtil.makeToast(context, context.getResources().getString(R.string.empty_location));
                break;
            default:
                appUtil.makeToast(context, context.getResources().getString(R.string.network_error));

            // TODO Check all error codes from the API to handle
        }
    }
}
