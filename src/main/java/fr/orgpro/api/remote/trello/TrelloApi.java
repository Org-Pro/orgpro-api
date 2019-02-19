package fr.orgpro.api.remote.trello;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class TrelloApi {

    private static final String API_BASE_URL = "https://api.trello.com/1/";

    /**
     * Setup de Retrofit
     */
    private static Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    /**
     * Accès aux méthodes de l'API Trello via Retrofit
     * @param serviceClass Choix de l'interface à utiliser
     * @param <S>
     * @return Un objet contenant l'accès aaux méthodes
     */
    public static <S> S createService(Class<S> serviceClass){
        return retrofit.create(serviceClass);
    }
}
