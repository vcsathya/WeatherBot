package com.weather.vcsathya.weatherbot.api;
//
//import android.content.res.Resources;
//import com.weather.vcsathya.weatherbot.R;
//
//import java.io.IOException;
//
//import okhttp3.HttpUrl;
//import okhttp3.Interceptor;
//import okhttp3.OkHttpClient;
//import okhttp3.Request;
//import okhttp3.Response;
//import okhttp3.logging.HttpLoggingInterceptor;
//import retrofit2.Retrofit;
//import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
//import retrofit2.converter.gson.GsonConverterFactory;
//
public class OpenWeatherAPI {
//
//    private final String BASE_URL = Resources.getSystem().getString(R.string.base_url);
//    private final String API_KEY = Resources.getSystem().getString(R.string.api_key);
//
//    public OkHttpClient provideClient() {
//        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
//        interceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);
//
//        return new OkHttpClient.Builder().addInterceptor(interceptor).addInterceptor(new Interceptor() {
//            @Override
//            public Response intercept(Chain chain) throws IOException {
//                Request request = chain.request();
//                HttpUrl url = request.url().newBuilder()
//                        .addQueryParameter("appid", API_KEY).build();
//                request = request.newBuilder().url(url).build();
//                return chain.proceed(request);
//            }
//        }).build();
//    }
//
//    public Retrofit provideRetrofit(String baseUrl, OkHttpClient client) {
//        return new Retrofit.Builder()
//                .baseUrl(baseUrl)
//                .client(client)
//                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//    }
//
//    public OpenWeatherAPIService providesApiService() {
//        return provideRetrofit(BASE_URL, provideClient()).create(OpenWeatherAPIService.class);
//    }
}