package com.rubixq.rubixkiosk.http;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RubixClient {
    private static RubixService rubixService = null;
    private static HttpLoggingInterceptor interceptor = null;

    static {
        interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
    }

    private static Retrofit retrofit = null;

    // TODO : Change how baseUrl is passed
    public static RubixService getInstance(String baseUrl) {
        if (rubixService == null) {
            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(interceptor)
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();

            rubixService = retrofit.create(RubixService.class);
        }

        return rubixService;
    }
}
