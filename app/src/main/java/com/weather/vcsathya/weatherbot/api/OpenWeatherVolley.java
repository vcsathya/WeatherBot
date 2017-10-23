package com.weather.vcsathya.weatherbot.api;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.LruCache;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

import static com.android.volley.toolbox.Volley.newRequestQueue;

public class OpenWeatherVolley {

    private static OpenWeatherVolley openWeatherVolleyInstance;
    private RequestQueue requestQueue;
    private ImageLoader imageLoader;
    private static Context weatherContext;

    private OpenWeatherVolley(Context context) {
        weatherContext = context;
        requestQueue = getRequestQueue();

        imageLoader = new ImageLoader(newRequestQueue(context), new ImageLoader.ImageCache() {
            LruCache<String, Bitmap> cache = new LruCache<>(20);

            @Override
            public Bitmap getBitmap(String url) {
                return cache.get(url);
            }

            @Override
            public void putBitmap(String url, Bitmap bitmap) {
                cache.put(url, bitmap);
            }

        });
    }

    public static synchronized OpenWeatherVolley getOpenWeatherVolleyInstance(Context context) {
        if (openWeatherVolleyInstance == null) {
            openWeatherVolleyInstance = new OpenWeatherVolley(context);
        }
        return openWeatherVolleyInstance;
    }

    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(weatherContext.getApplicationContext());
        }
        return requestQueue;
    }

    public <T> void addToRequestQueue(Request<T> request) {
        getRequestQueue().add(request);
    }

    public ImageLoader getImageLoader() {
        return imageLoader;
    }
}
