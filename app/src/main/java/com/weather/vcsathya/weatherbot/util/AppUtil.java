package com.weather.vcsathya.weatherbot.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.weather.vcsathya.weatherbot.R;

public class AppUtil {

    private SharedPreferences sharedPreferences;

    public void hideKeyboard(EditText locationEditTextView, Context context) {
        // To hide the soft keyboard after typing the search keyword
        InputMethodManager in = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        in.hideSoftInputFromWindow(locationEditTextView.getWindowToken(), 0);
    }

    public void saveLocation(Context context, String location) {
        sharedPreferences = context.getSharedPreferences(context.getResources().
                getString(R.string.app_name),Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // Store the location user entered in the cache for loading on next app launch
        editor.putString(context.getResources().getString(R.string.location), location);
        editor.apply();
    }

    public String getLocation(Context context) {
        // Load location user typed during the last app usage from cache(shared preferences)
        String defaultLocation = context.getResources().getString(R.string.default_location);
        sharedPreferences = context.getSharedPreferences(context.getResources().
                getString(R.string.app_name),Context.MODE_PRIVATE);
        return sharedPreferences.getString(context.getResources().getString(R.string.location), defaultLocation);
    }

    public void makeToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}