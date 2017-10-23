package com.weather.vcsathya.weatherbot.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.weather.vcsathya.weatherbot.R;

public class NetworkUtil {
    public boolean checkInternet(Context context) {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        } else {
            new AppUtil().makeToast(context, context.getResources().getString(R.string.internet_connection));
            return false;
        }
    }

    public String buildUrl(Context context) {
        // TODO: Use Retrofit
        // Construct url params
        String url = context.getResources().getString(R.string.base_url);
        String param = "?q="+new AppUtil().getLocation(context);
        String apiKey = context.getResources().getString(R.string.api_key);
        String units = context.getResources().getString(R.string.units);
        return url+param+apiKey+units;
    }
}