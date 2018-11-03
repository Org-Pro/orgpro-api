package fr.orgpro.api.remote.trello.services;

import fr.orgpro.api.remote.trello.models.TrelloList;
import retrofit2.Call;
import retrofit2.http.*;

public interface TrelloListService {

    @GET("list/{id}")
    Call<TrelloList> getList(
            @Path("id") String id,
            @Query("key") String key,
            @Query("token") String token
    );

    @FormUrlEncoded
    @POST("lists/")
    Call<TrelloList> addList(
            @Field("key") String key,
            @Field("token") String token,
            @Field("name") String name,
            @Field("idBoard") String idBoard
    );
}
