package com.weather.vcsathya.weatherbot.view;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.weather.vcsathya.weatherbot.R;
import com.weather.vcsathya.weatherbot.util.DateUtil;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static android.widget.Toast.LENGTH_SHORT;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.current_date) TextView currentDate;
    @BindView(R.id.current_location) ImageButton currentLocation;
    @BindView(R.id.edit_location) ImageButton editLocationButton;
    @BindView(R.id.location) TextView locationTextView;
    @BindView(R.id.location_edit) EditText locationEditTextView;
    @BindView(R.id.temperature) LinearLayout temperatureLayout;
    @BindView(R.id.degree) TextView degree;
    @BindView(R.id.weather_icon) ImageView weatherIcon;
    @BindView(R.id.weather_detail) RecyclerView weatherDetail;
    @BindView(R.id.navigation) BottomNavigationView navigation;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    locationTextView.setText(R.string.title_home);
                    return true;
                case R.id.navigation_dashboard:
                    locationTextView.setText(R.string.title_dashboard);
                    return true;
                case R.id.navigation_notifications:
                    locationTextView.setText(R.string.title_notifications);
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

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        String url = this.getString(R.string.base_url);
        String apiKey = this.getString(R.string.api_key);
        String param = "?q=chennai,IN&appid=";
        String units = "&units=imperial";

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url+param+apiKey+units, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            System.out.println("Response: " + response.toString());
                            degree.setText((response.getJSONObject("main")).getString("temp"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub

                    }
                });

        // Access the RequestQueue through your singleton class.
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(jsObjRequest);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }

    @OnClick(R.id.edit_location)
    public void locationButton() {
        editLocation();
    }

    @OnClick(R.id.location)
    public void locationEdit() {
        editLocation();
    }

    private void editLocation() {
        locationTextView.setVisibility(GONE);
        locationEditTextView.setVisibility(VISIBLE);
        locationEditTextView.setSingleLine(true);
    }

    @OnClick(R.id.current_location)
    public void updateCurrentLocation() {
        // get current location with permission
        Toast.makeText(this, "Loading your location...", LENGTH_SHORT).show();
    }

    private void setCurrentDate() {
        currentDate.setText(new DateUtil().getCurrentDate());
    }

    public void loadData() {
        setCurrentDate();
        locationEditTextView.setVisibility(GONE);
    }
}