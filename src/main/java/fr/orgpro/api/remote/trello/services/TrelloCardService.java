package fr.orgpro.api.remote.trello.services;

import fr.orgpro.api.remote.trello.models.TrelloCard;
import retrofit2.Call;
import retrofit2.http.*;

public interface TrelloCardService {

    @GET("card/{id}")
    Call<TrelloCard> getCard(
            @Path("id") String id,
            @Query("key") String key,
            @Query("token") String token
    );

    @FormUrlEncoded
    @POST("cards/")
    Call<TrelloCard> addCard(
            @Field("key") String key,
            @Field("token") String token,
            @Field("idList") String idList,
            @Field("name") String name,
            @Field("due") String date
    );

    @FormUrlEncoded
    @PUT("cards/{id}")
    Call<TrelloCard> updateCard(
            @Path("id") String id,
            @Field("key") String key,
            @Field("token") String token,
            @Field("idList") String idList,
            @Field("name") String name,
            @Field("due") String date
    );
}
