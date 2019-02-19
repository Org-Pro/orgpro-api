package fr.orgpro.api.remote.trello.services;


import fr.orgpro.api.remote.trello.models.TrelloBoard;
import retrofit2.Call;
import retrofit2.http.*;

public interface TrelloBoardService {
    /**
     * Récupère un tableau Trello selon l'identifiant
     * @param id Identifiant du tableau
     * @param key Clef de l'API dédiée à l'utilisateur
     * @param token Token Trello de l'utilisateur
     * @return L'objet du tableau Trello
     */
    @GET("board/{id}")
    Call<TrelloBoard> getBoard(
            @Path("id") String id,
            @Query("key") String key,
            @Query("token") String token
    );

    /**
     * Créé un tableau Trello
     * @param key Clef de l'API dédiée à l'utilisateur
     * @param token Token Trello de l'utilisateur
     * @param name Nom du tableau
     * @return L'objet du tableau Trello
     */
    @FormUrlEncoded
    @POST("boards/")
    Call<TrelloBoard> addBoard(
            @Field("key") String key,
            @Field("token") String token,
            @Field("name") String name
    );
}
