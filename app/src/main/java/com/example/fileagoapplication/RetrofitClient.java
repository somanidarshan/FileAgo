package com.example.fileagoapplication;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    public static Retrofit getRetrofitInstance(){
        HttpLoggingInterceptor httpLoggingInterceptor=new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient okHttpClient=new OkHttpClient.Builder().addInterceptor(httpLoggingInterceptor).build();
       Retrofit   retrofit=new Retrofit.Builder()
                    .baseUrl("https://ocean.fileago.com/")
                    .addConverterFactory(GsonConverterFactory.create())
               .client(okHttpClient)
               .build();
        return retrofit;
    }
public  static ApiInterface getApiInterface(){
        ApiInterface apiInterface=getRetrofitInstance().create(ApiInterface.class);
        return apiInterface;
}

}
