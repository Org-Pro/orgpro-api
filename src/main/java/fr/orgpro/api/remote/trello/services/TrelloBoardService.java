package fr.orgpro.api.remote.trello.services;


import fr.orgpro.api.remote.trello.models.TrelloBoard;
import retrofit2.Call;
import retrofit2.http.*;

public interface TrelloBoardService {
    @GET("board/{id}")
    Call<TrelloBoard> getBoard(
            @Path("id") String id,
            @Query("key") String key,
            @Query("token") String token
    );


    @FormUrlEncoded
    @POST("boards/")
    Call<TrelloBoard> addBoard(
            @Field("key") String key,
            @Field("token") String token,
            @Field("name") String name
    );
}
