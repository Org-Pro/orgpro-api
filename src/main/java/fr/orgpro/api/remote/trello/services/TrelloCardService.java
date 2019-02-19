package fr.orgpro.api.remote.trello.services;

import fr.orgpro.api.remote.trello.models.TrelloCard;
import retrofit2.Call;
import retrofit2.http.*;

public interface TrelloCardService {

    /**
     * Récupère une carte Trello selon l'identifiant
     * @param id Identifiant de la carte
     * @param key Clef de l'API dédiée à l'utilisateur
     * @param token Token Trello de l'utilisateur
     * @return L'objet de la carte Trello
     */
    @GET("card/{id}")
    Call<TrelloCard> getCard(
            @Path("id") String id,
            @Query("key") String key,
            @Query("token") String token
    );

    /**
     * Créé une carte Trello
     * @param key Clef de l'API dédiée à l'utilisateur
     * @param token Token Trello de l'utilisateur
     * @param idList L'identifiant de la liste où la carte doit être insérée
     * @param name Contenu de la carte
     * @param date Deadline
     * @return L'objet de la carte Trello
     */
    @FormUrlEncoded
    @POST("cards/")
    Call<TrelloCard> addCard(
            @Field("key") String key,
            @Field("token") String token,
            @Field("idList") String idList,
            @Field("name") String name,
            @Field("due") String date
    );

    /**
     * Modidife une carte Trello
     * @param id Identifiant de la carte
     * @param key Clef de l'API dédiée à l'utilisateur
     * @param token Token Trello de l'utilisateur
     * @param idList L'identifiant de la liste où la carte doit être déplacée
     * @param name Contenu de la carte
     * @param date Deadline
     * @return L'objet de la carte Trello
     */
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
