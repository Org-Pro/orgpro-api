package fr.orgpro.api.remotetrello;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;
import java.io.InterruptedIOException;

public class TrelloTaskApi {

    public static void main(String... args) throws InterruptedException, IOException {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.trello.com/1/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        TrelloInterface ti = retrofit.create(TrelloInterface.class);
        TrelloBoard o = ti.loadBoardExample().execute().body();
        Thread.sleep(4000);
        System.out.println(o.toString());
    }

}
