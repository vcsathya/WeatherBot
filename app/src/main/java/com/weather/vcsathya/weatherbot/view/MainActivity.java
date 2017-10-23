package com.weather.vcsathya.weatherbot.view;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.weather.vcsathya.weatherbot.ErrorHandler;
import com.weather.vcsathya.weatherbot.R;
import com.weather.vcsathya.weatherbot.api.OpenWeatherVolley;
import com.weather.vcsathya.weatherbot.model.Main;
import com.weather.vcsathya.weatherbot.model.Weather;
import com.weather.vcsathya.weatherbot.model.WeatherInfo;
import com.weather.vcsathya.weatherbot.util.AppUtil;
import com.weather.vcsathya.weatherbot.util.DateUtil;
import com.weather.vcsathya.weatherbot.util.NetworkUtil;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.view.View.VISIBLE;
import static android.widget.Toast.LENGTH_SHORT;
import static com.android.volley.toolbox.Volley.newRequestQueue;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.current_date) TextView currentDate;
    @BindView(R.id.current_location) ImageButton currentLocation;
    @BindView(R.id.location_edit) EditText locationEditTextView;
    @BindView(R.id.temperature) LinearLayout temperatureLayout;
    @BindView(R.id.degree) TextView degree;
    @BindView(R.id.weather_icon) NetworkImageView weatherIcon;
    @BindView(R.id.weather_description) TextView weatherDescription;
    @BindView(R.id.weather_detail) RecyclerView weatherDetail;
    @BindView(R.id.navigation) BottomNavigationView navigation;

    Main main = new Main();
    Weather[] weather = new Weather[0];
    WeatherInfo weatherInfo = new WeatherInfo();
    AppUtil appUtil = new AppUtil();
    NetworkUtil networkUtil = new NetworkUtil();
    String TAG;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    locationEditTextView.setText(appUtil.getLocation(MainActivity.this));
                    return true;
                case R.id.navigation_dashboard:
                    locationEditTextView.setText(R.string.title_dashboard);
                    return true;
                case R.id.navigation_notifications:
                    locationEditTextView.setText(R.string.title_notifications);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        TAG = getResources().getString(R.string.app_name);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        locationEditTextView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    appUtil.saveLocation(MainActivity.this, String.valueOf(locationEditTextView.getText()));

                    locationEditTextView.clearFocus();
                    appUtil.hideKeyboard(locationEditTextView, MainActivity.this);
                    networkCall();
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        networkCall();
    }

    @OnClick(R.id.current_location)
    public void updateCurrentLocation() {
        // get current location with permission
        appUtil.makeToast(this, getString(R.string.loading));
    }

    public void loadData() {
        setCurrentDate();
        setLocation();
        setWeatherDescription();
        setTemperature();
        setWeatherIcon();
    }

    private void setCurrentDate() {
        // Set the currentDate in the TextView
        currentDate.setText(new DateUtil().getCurrentDate());
    }

    private void setLocation() {
        // Get location from SharedPreference
        // Set last location in editText
        System.out.println("savedlocation" + appUtil.getLocation(this));

        if ((appUtil.getLocation(this)).length() <= 0) {
            locationEditTextView.setText(weatherInfo.getCity());
        } else {
            locationEditTextView.setText(appUtil.getLocation(this));
            locationEditTextView.setAllCaps(true);
        }
    }

    private void setWeatherDescription() {
        // Set the short description of weather like "clear sky", "moderate rain", etc
        if (weather.length > 0) {
            weatherDescription.setText(weather[0].getDescription());
            weatherDescription.setAllCaps(true);
        } else {
            appUtil.makeToast(this, getString(R.string.network_error));
        }
    }

    private void setTemperature() {
        // Round double value and cast to integer
        degree.setText((int) Math.round(main.getTemperature()) + getString(R.string.fahrenheit));
    }

    private void setWeatherIcon() {
        if (weather.length > 0) {
            weatherIcon.setImageUrl(getString(R.string.image_url) + weather[0].getIcon()
                    + getString(R.string.image_type), OpenWeatherVolley
                    .getOpenWeatherVolleyInstance(this).getImageLoader());
        } else {
            appUtil.makeToast(this, getString(R.string.network_error));
        }
    }

    // TODO This should ideally be an RxJava implementation with observable, observer and valid subscription
    // TODO Use MVP pattern to have presenter control the data communication between view and model

    private void networkCall() {
        String url = networkUtil.buildUrl(this);

        if (networkUtil.checkInternet(this)) {
            final JsonObjectRequest jsObjRequest = new JsonObjectRequest
                    (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            System.out.println("Response: " + response.toString());

                            try {
                                Gson gson = new GsonBuilder().create();
                                weather = gson.fromJson(
                                        String.valueOf((response.getJSONArray("weather"))), Weather[].class);
                                main.setTemperature((response.getJSONObject("main")).getDouble("temp"));
                                weatherInfo.setCity(response.getString("name"));
                                loadData();
                            } catch (JSONException e) {
                                Log.e(TAG, e.getMessage());
                            }
                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            if (error.networkResponse != null) {
                                Log.e("Status Code: ", String.valueOf(error.networkResponse.statusCode));
                                if (error.networkResponse.data != null) {
                                    new ErrorHandler().getErrorMessage(new String(
                                            error.networkResponse.data), MainActivity.this);
                                }
                            }
                            // Reset Location if the response is unsuccessful
                            reset();
                        }
                    });
            (OpenWeatherVolley.getOpenWeatherVolleyInstance(
                    getApplicationContext())).addToRequestQueue(jsObjRequest);
        }
    }

    private void reset() {
        appUtil.saveLocation(MainActivity.this, "");
        degree.setText("");
        weatherDescription.setText("");
        weatherIcon.setImageBitmap(null);
    }
}