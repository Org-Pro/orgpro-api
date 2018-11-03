package fr.orgpro.api.remote.trello;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class TrelloApi {

    private static final String API_BASE_URL = "https://api.trello.com/1/";

    private static Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    public static <S> S createService(Class<S> serviceClass){
        return retrofit.create(serviceClass);
    }
}
