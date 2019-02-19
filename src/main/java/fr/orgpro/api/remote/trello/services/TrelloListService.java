package fr.orgpro.api.remote.trello.services;

import fr.orgpro.api.remote.trello.models.TrelloList;
import retrofit2.Call;
import retrofit2.http.*;

public interface TrelloListService {

    /**
     * Récupère une liste (colonne) Trello selon l'identifiant
     * @param id Identifiant de la liste
     * @param key Clef de l'API dédiée à l'utilisateur
     * @param token Token Trello de l'utilisateur
     * @return L'objet de la liste Trello
     */
    @GET("list/{id}")
    Call<TrelloList> getList(
            @Path("id") String id,
            @Query("key") String key,
            @Query("token") String token
    );

    /**
     * Créé une liste (colonne) Trello
     * @param key Clef de l'API dédiée à l'utilisateur
     * @param token Token Trello de l'utilisateur
     * @param name Nom de la liste
     * @param idBoard L'identifiant du tableau où la carte doit être insérée
     * @return L'objet de la liste Trello
     */
    @FormUrlEncoded
    @POST("lists/")
    Call<TrelloList> addList(
            @Field("key") String key,
            @Field("token") String token,
            @Field("name") String name,
            @Field("idBoard") String idBoard
    );
}
