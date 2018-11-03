package fr.orgpro.api.remotetrello;


import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface TrelloInterface {
    @GET("board/5612e4f91b25c15e873722b8?fields=all")
    Call<TrelloBoard> loadBoardExample();
}
