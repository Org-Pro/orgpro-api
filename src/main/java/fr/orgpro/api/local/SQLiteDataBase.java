package fr.orgpro.api.local;

import fr.orgpro.api.local.models.SQLCollaborateur;
import fr.orgpro.api.local.models.SQLSynchro;
import fr.orgpro.api.project.Tache;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import static fr.orgpro.api.local.SQLiteConnection.getStatement;

public class SQLiteDataBase {

    /**
     * Crée la structure de la base de données locale
     * @return False en cas d'erreur
     */
    public static boolean createTable(){
        try {
            getStatement().execute("create table if not exists collaborateur (" +
                    "pseudo char(255) primary key not null," +
                    "nom char(255)," +
                    "prenom char(255)," +
                    "google_id_liste char(255)," +
                    "trello_id_board char(255)," +
                    "trello_id_list char(255)," +
                    "trello_key char(255)," +
                    "trello_token char(255)" +
                    ");");

            getStatement().execute("create table if not exists tache (" +
                    "uuid char(255) primary key not null" +
                    ");");

            getStatement().execute("create table if not exists synchro (" +
                    "uuid_tache char(255) not null," +
                    "pseudo_collaborateur char(255) not null," +
                    "google_id_tache char(255)," +
                    "trello_id_card char(255)," +
                    "est_synchro boolean not null default false," +
                    "unique (uuid_tache, pseudo_collaborateur)," +
                    "foreign key (uuid_tache) references tache(uuid) on delete cascade DEFERRABLE INITIALLY DEFERRED," +
                    "foreign key (pseudo_collaborateur) references collaborateur(pseudo) on delete cascade on update cascade DEFERRABLE INITIALLY DEFERRED" +
                    ");");
        } catch ( Exception e ) {
            //System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            return false;
        }
        //System.out.println("Table created successfully");
        return true;
    }




    // ----- TACHE -----

    /**
     * Ajoute les tâches de la liste à la base de données locale
     * @param tacheListe La liste contenant les tâches à ajouter
     * @return False en cas d'erreur
     */
    public static boolean addTacheListe(@Nonnull List<Tache> tacheListe){
        try {
            for (Tache tache : tacheListe){
                getStatement().execute("insert into tache(uuid) values('" + tache.getId() + "');");
            }
        }catch ( Exception e ) {
            //System.err.println(e.getClass().getName() + ": " + e.getMessage());
            return false;
        }
        return true;
    }

    /**
     * Ajoute la tâche de la base de données locale
     * @param tache La tâche à ajouter
     * @return False en cas d'erreur
     */
    public static boolean addTache(@Nonnull Tache tache){
        try {
            getStatement().execute("insert into tache(uuid) values('" + tache.getId() + "');");
        }catch ( Exception e ) {
            //System.err.println(e.getClass().getName() + ": " + e.getMessage());
            return false;
        }
        return true;
    }

    /**
     * Supprime la tâche de la base de données locale
     * @param tache La tâche à supprimer
     * @return False en cas d'erreur
     */
    public static boolean deleteTache(@Nonnull Tache tache){
        try {
            int rst = getStatement().executeUpdate("delete from tache where uuid = ('" + tache.getId() + "');");
            if (rst == 0) return false;
        }catch ( Exception e ) {
            //System.err.println(e.getClass().getName() + ": " + e.getMessage());
            return false;
        }
        return true;
    }




    // ----- COLLABORATEUR -----

    public static boolean addCollaborateur(@Nonnull SQLCollaborateur collaborateur){
        try {
            getStatement().execute("insert into collaborateur values("
                    + stringReqValue(collaborateur.getPseudo()) + ", "
                    + stringReqValue(collaborateur.getNom()) + ", "
                    + stringReqValue(collaborateur.getPrenom()) + ", "
                    + stringReqValue(collaborateur.getGoogle_id_liste()) + ", "
                    + stringReqValue(collaborateur.getTrello_id_board()) + ", "
                    + stringReqValue(collaborateur.getTrello_id_liste()) + ", "
                    + stringReqValue(collaborateur.getTrello_key()) + ", "
                    + stringReqValue(collaborateur.getTrello_token())
                    + ");");
        }catch ( Exception e ) {
            //System.err.println(e.getClass().getName() + ": " + e.getMessage());
            return false;
        }
        return true;
    }

    /**
     * Supprime un collaborateur de la base de données locale
     * @param collaborateur Pseudo du collaborateur à supprimer
     * @return False en cas d'erreur
     */
    public static boolean deleteCollaborateur(@Nonnull String collaborateur){
        try {
            int rst = getStatement().executeUpdate("delete from collaborateur where pseudo = ('" + collaborateur + "');");
            if (rst == 0) return false;
        }catch ( Exception e ) {
            //System.err.println(e.getClass().getName() + ": " + e.getMessage());
            return false;
        }
        return true;
    }

    public static boolean deleteCollaborateur(@Nonnull SQLCollaborateur collaborateur){
        try {
            int rst = getStatement().executeUpdate("delete from collaborateur where pseudo = (" + stringReqValue(collaborateur.getPseudo()) + ");");
            if (rst == 0) return false;
        }catch ( Exception e ) {
            //System.err.println(e.getClass().getName() + ": " + e.getMessage());
            return false;
        }
        return true;
    }

    /**
     * Modifie le pseudo d'un collaborateur déjà existant
     * @param oldCollaborateur Le pseudo du collaborateur à modifier
     * @param newCollaborateur Le nouveau pseudo du collaborateur à modifier
     * @return False en cas d'erreur
     */
    public static boolean updateCollaborateurPseudo(@Nonnull String oldCollaborateur, @Nonnull String newCollaborateur){
        try {
            int rst = getStatement().executeUpdate("update collaborateur set pseudo='" + newCollaborateur + "' where pseudo='" + oldCollaborateur + "';");
            if (rst == 0) return false;
        }catch ( Exception e ) {
            //System.err.println(e.getClass().getName() + ": " + e.getMessage());
            return false;
        }
        return true;
    }

    public static boolean updateCollaborateur(@Nonnull SQLCollaborateur collaborateur){
        try {
            int rst = getStatement().executeUpdate("update collaborateur set "
                    + "nom=" + stringReqValue(collaborateur.getNom()) + ","
                    + "prenom=" + stringReqValue(collaborateur.getPrenom()) + ","
                    + "google_id_liste=" + stringReqValue(collaborateur.getGoogle_id_liste()) + ","
                    + "trello_id_board=" + stringReqValue(collaborateur.getTrello_id_board()) + ","
                    + "trello_id_list=" + stringReqValue(collaborateur.getTrello_id_liste()) + ","
                    + "trello_key=" + stringReqValue(collaborateur.getTrello_key()) + ","
                    + "trello_token=" + stringReqValue(collaborateur.getTrello_token())
                    + " where pseudo=" + stringReqValue(collaborateur.getPseudo()) + ";");
            if (rst == 0) return false;
        }catch ( Exception e ) {
            //System.err.println(e.getClass().getName() + ": " + e.getMessage());
            return false;
        }
        return true;
    }

    public static SQLCollaborateur getCollaborateur(@Nonnull String collaborateurPseudo){
        try {
            SQLCollaborateur collaborateur = null;
            ResultSet rst = getStatement().executeQuery("select * from collaborateur where pseudo='" + collaborateurPseudo + "';");
            if(rst.next()){
                collaborateur = new SQLCollaborateur(rst.getString("pseudo"));
                collaborateur.setNom(rst.getString("nom"));
                collaborateur.setPrenom(rst.getString("prenom"));
                collaborateur.setGoogle_id_liste(rst.getString("google_id_liste"));
                collaborateur.setTrello_id_board(rst.getString("trello_id_board"));
                collaborateur.setTrello_id_liste(rst.getString("trello_id_list"));
                collaborateur.setTrello_key("trello_key");
                collaborateur.setTrello_token("trello_token");
            }
            return collaborateur;
        }catch ( Exception e ) {
            //System.err.println(e.getClass().getName() + ": " + e.getMessage());
            return null;
        }
    }



    // ----- SYNCHRO -----

    /**
     * Récupère les synchro d'un collaborateur déjà existant
     * @param collaborateur Le pseudo du collaborateur concerné
     * @return La liste des synchro
     */
    public static List<SQLSynchro> getAllSynchroByCollaborateur(@Nonnull String collaborateur){
        try {
            List<SQLSynchro> listeSynchro = new ArrayList<SQLSynchro>();
            ResultSet rst = getStatement().executeQuery("select * from synchro where pseudo_collaborateur='" + collaborateur + "';");
            SQLSynchro tmpSynchro = null;
            while(rst.next()){
                tmpSynchro = new SQLSynchro(rst.getString("uuid_tache"), rst.getString("pseudo_collaborateur"));
                tmpSynchro.setGoogle_id_tache(rst.getString("google_id_tache"));
                tmpSynchro.setTrello_id_card(rst.getString("trello_id_card"));
                tmpSynchro.setEst_synchro(Boolean.parseBoolean(rst.getString("est_synchro")));
                listeSynchro.add(tmpSynchro);
            }
            return listeSynchro;
        }catch ( Exception e ) {
            //System.err.println(e.getClass().getName() + ": " + e.getMessage());
            return null;
        }
    }

    public static SQLSynchro getSynchroTacheCollaborateur(@Nonnull SQLCollaborateur collaborateur, @Nonnull Tache tache){
        try {
            SQLSynchro synchro = null;
            ResultSet rst = getStatement().executeQuery("select * from synchro where "
                    + "pseudo_collaborateur=" + stringReqValue(collaborateur.getPseudo()) + " and "
                    + "uuid_tache=" + stringReqValue(tache.getId())
                    + ";");
            if(rst.next()){
                synchro = new SQLSynchro(rst.getString("uuid_tache"), rst.getString("pseudo_collaborateur"));
                synchro.setGoogle_id_tache(rst.getString("google_id_tache"));
                synchro.setTrello_id_card(rst.getString("trello_id_card"));
                synchro.setEst_synchro(Boolean.parseBoolean(rst.getString("est_synchro")));
            }
            return synchro;
        }catch ( Exception e ) {
            //System.err.println(e.getClass().getName() + ": " + e.getMessage());
            return null;
        }
    }

    public static boolean addSynchroTacheCollaborateur(@Nonnull SQLSynchro synchro){
        try {
            getStatement().execute("insert into synchro values("
                    + stringReqValue(synchro.getUuid_tache()) + ", "
                    + stringReqValue(synchro.getPseudo_collaborateur()) + ", "
                    + stringReqValue(synchro.getGoogle_id_tache()) + ", "
                    + stringReqValue(synchro.getTrello_id_card()) + ", "
                    + "'" + synchro.isEst_synchro() + "'"
                    + ");");
        }catch ( Exception e ) {
            //System.err.println(e.getClass().getName() + ": " + e.getMessage());
            return false;
        }
        return true;
    }

    public static boolean updateSynchroTacheCollaborateur(@Nonnull SQLSynchro synchro){
        try {
            int rst = getStatement().executeUpdate("update synchro set "
                    + "google_id_tache=" + stringReqValue(synchro.getGoogle_id_tache()) + ","
                    + "trello_id_card=" + stringReqValue(synchro.getTrello_id_card()) + ","
                    + "est_synchro='" + synchro.isEst_synchro() + "'"
                    + " where uuid_tache=" + stringReqValue(synchro.getUuid_tache()) + " and pseudo_collaborateur=" + stringReqValue(synchro.getPseudo_collaborateur()) + ";");
            if (rst == 0) return false;
        }catch ( Exception e ) {
            //System.err.println(e.getClass().getName() + ": " + e.getMessage());
            return false;
        }
        return true;
    }

    public static boolean deleteSynchroTacheCollaborateur(@Nonnull SQLSynchro synchro){
        try {
            int rst = getStatement().executeUpdate("delete from synchro where uuid_tache=" + stringReqValue(synchro.getUuid_tache()) + " and pseudo_collaborateur=" + stringReqValue(synchro.getPseudo_collaborateur()) + ";");
            if (rst == 0) return false;
        }catch ( Exception e ) {
            //System.err.println(e.getClass().getName() + ": " + e.getMessage());
            return false;
        }
        return true;
    }

    /**
     * Modifie l'état de synchronisation de tous les liens entre une tâche et un collaborateur selon une tâche choisie
     * @param tache La tâche concernée
     * @param estSynchro L'état de la synchronisation avec les APIs en ligne à modifier
     * @return False en cas d'erreur
     */
    public static boolean updateAllSynchroEstSynchroByTache(@Nonnull Tache tache, boolean estSynchro){
        try {
            int rst = getStatement().executeUpdate("update synchro set est_synchro='" + estSynchro + "' where uuid_tache='" + tache.getId() + "';");
            if (rst == 0) return false;
        }catch ( Exception e ) {
            //System.err.println(e.getClass().getName() + ": " + e.getMessage());
            return false;
        }
        return true;
    }

    /**
     * Permet de retouner un string entre guillemets même s'il est null
     * @param val Le string à traiter
     * @return Le string entre guillemets
     */
    private static String stringReqValue(@Nullable String val){
        if(val == null) return "null";
        else return "'" + val + "'";
    }




    // ------------------------------------------------ DEPRECATED -----------------------------------------------------


    /**
     * Ajoute un lien entre une tâche et un collaborateur avec des paramètres supplémentaires liés aux APIs
     * @param tache La tâche à lier
     * @param collaborateur Le collaborateur à lier
     * @param googleIdTache L'id de la tâche créée par l'API Google
     * @param estSynchro L'état de la synchronisation avec les APIs en ligne
     * @return False en cas d'erreur
     */
    @Deprecated
    public static boolean synchroAddTacheCollaborateur(@Nonnull Tache tache, @Nonnull String collaborateur, @Nullable String googleIdTache, @Nullable Boolean estSynchro){
        try {
            boolean synchro_req = false;
            // TODO google_id_tache != null -> à verifier ?
            if(googleIdTache != null && estSynchro != null) synchro_req = estSynchro;
            getStatement().execute("insert into synchro(uuid_tache, pseudo_collaborateur, google_id_tache, est_synchro) values('"
                    + tache.getId() + "', '" + collaborateur + "', " + stringReqValue(googleIdTache) + ",'" + synchro_req + "');");
        }catch ( Exception e ) {
            //System.err.println(e.getClass().getName() + ": " + e.getMessage());
            return false;
        }
        return true;
    }

    /**
     * Supprime le lien entre une tâche et un collaborateur
     * @param tache La tâche à délier
     * @param collaborateur Le collaborateur à délier
     * @return False en cas d'erreur
     */
    @Deprecated
    public static boolean synchroDeleteTacheCollaborateur(@Nonnull Tache tache, @Nonnull String collaborateur){
        try {
            int rst = getStatement().executeUpdate("delete from synchro where uuid_tache='" + tache.getId() + "' and pseudo_collaborateur='" + collaborateur + "';");
            if (rst == 0) return false;
        }catch ( Exception e ) {
            //System.err.println(e.getClass().getName() + ": " + e.getMessage());
            return false;
        }
        return true;
    }

    /**
     * Modifie l'id de tâche liée à l'API Google d'un lien entre une tâche et un collaborateur
     * @param tache La tâche liée
     * @param collaborateur Le pseudo du collaborateur lié
     * @param googleIdTache L'id de la tâche liée à l'API Google à modifier
     * @return False en cas d'erreur
     */
    @Deprecated
    public static boolean synchroUpdateGoogleIdTache(@Nonnull Tache tache, @Nonnull String collaborateur, @Nullable String googleIdTache){
        try {
            int rst = getStatement().executeUpdate("update synchro set google_id_tache=" + stringReqValue(googleIdTache) + " where uuid_tache='" + tache.getId() + "' and pseudo_collaborateur='" + collaborateur + "';");
            if (rst == 0) return false;
        }catch ( Exception e ) {
            //System.err.println(e.getClass().getName() + ": " + e.getMessage());
            return false;
        }
        return true;
    }

    /*@Deprecated
    public static boolean synchroUpdateTrelloIdCard(@Nonnull Tache tache, @Nonnull String collaborateur, @Nullable String trelloIdCard){
        try {
            int rst = getStatement().executeUpdate("update synchro set trello_id_card=" + stringReqValue(trelloIdCard) + " where uuid_tache='" + tache.getId() + "' and pseudo_collaborateur='" + collaborateur + "';");
            if (rst == 0) return false;
        }catch ( Exception e ) {
            //System.err.println(e.getClass().getName() + ": " + e.getMessage());
            return false;
        }
        return true;
    }*/

    /**
     * Modifie l'état de synchronisation d'un lien entre une tâche et un collaborateur
     * @param tache La tâche liée
     * @param collaborateur Le pseudo du collaborateur lié
     * @param estSynchro L'état de la synchronisation avec les APIs en ligne à modifier
     * @return False en cas d'erreur
     */
    @Deprecated
    public static boolean synchroUpdateEstSynchro(@Nonnull Tache tache, @Nonnull String collaborateur, boolean estSynchro){
        try {
            int rst = getStatement().executeUpdate("update synchro set est_synchro='" + estSynchro + "' where uuid_tache='" + tache.getId() + "' and pseudo_collaborateur='" + collaborateur + "';");
            if (rst == 0) return false;
        }catch ( Exception e ) {
            //System.err.println(e.getClass().getName() + ": " + e.getMessage());
            return false;
        }
        return true;
    }

    /**
     * Ajoute un collaborateur à la base de données locale
     * @param collaborateur Pseudo du collaborateur à ajouter
     * @param nom Nom du collaborateur à ajouter
     * @param prenom Prenom du collaborateur à ajouter
     * @param google_id_liste L'id de la liste créée par l'API Google du collaborateur à ajouter
     * @return False en cas d'erreur
     */
    @Deprecated
    public static boolean addCollaborateur(@Nonnull String collaborateur, @Nullable String nom, @Nullable String prenom, @Nullable String google_id_liste){
        try {
            getStatement().execute("insert into collaborateur(pseudo, nom, prenom, google_id_liste) values('"
                    + collaborateur + "', " + stringReqValue(nom) + ", " + stringReqValue(prenom) + ", " + stringReqValue(google_id_liste) + ");");
        }catch ( Exception e ) {
            //System.err.println(e.getClass().getName() + ": " + e.getMessage());
            return false;
        }
        return true;
    }

    /**
     * Modifie l'id de la liste de l'API google d'un collaborateur déjà existant
     * @param collaborateur Le pseudo du collaborateur concerné
     * @param googleIdListe Nouvel id de la liste de l'API google
     * @return False en cas d'erreur
     */
    @Deprecated
    public static boolean updateCollaborateurGoogleIdListe(@Nonnull String collaborateur, @Nullable String googleIdListe){
        try {
            int rst;
            if(googleIdListe == null)
                rst = getStatement().executeUpdate("update collaborateur set google_id_liste=null where pseudo='" + collaborateur + "';");
            else
                rst = getStatement().executeUpdate("update collaborateur set google_id_liste='" + googleIdListe + "' where pseudo='" + collaborateur + "';");
            if (rst == 0) return false;
        }catch ( Exception e ) {
            //System.err.println(e.getClass().getName() + ": " + e.getMessage());
            return false;
        }
        return true;
    }

    /*@Deprecated
    public static boolean updateCollaborateurTrelloIdBoard(@Nonnull String collaborateur, @Nullable String trelloIdBoard){
        try {
            int rst;
            rst = getStatement().executeUpdate("update collaborateur set trello_id_board=" + stringReqValue(trelloIdBoard) + " where pseudo='" + collaborateur + "';");
            if (rst == 0) return false;
        }catch ( Exception e ) {
            //System.err.println(e.getClass().getName() + ": " + e.getMessage());
            return false;
        }
        return true;
    }*/

    /*@Deprecated
    public static boolean updateCollaborateurTrelloIdList(@Nonnull String collaborateur, @Nullable String trelloIdList){
        try {
            int rst;
            if(trelloIdList == null)
                rst = getStatement().executeUpdate("update collaborateur set trello_id_list=null where pseudo='" + collaborateur + "';");
            else
                rst = getStatement().executeUpdate("update collaborateur set trello_id_list='" + trelloIdList + "' where pseudo='" + collaborateur + "';");
            if (rst == 0) return false;
        }catch ( Exception e ) {
            //System.err.println(e.getClass().getName() + ": " + e.getMessage());
            return false;
        }
        return true;
    }*/

    /**
     * Récupère l'id de la liste de l'API google d'un collaborateur déjà existant
     * @param collaborateur Le pseudo du collaborateur concerné
     * @return L'id de la liste de l'API google, null sinon
     */
    @Deprecated
    public static String getCollaborateurGoogleIdListe(@Nonnull String collaborateur){
        try {
            ResultSet rst = getStatement().executeQuery("select google_id_liste from collaborateur where pseudo='" + collaborateur + "';");
            rst.next();
            return rst.getString("google_id_liste");
        }catch ( Exception e ) {
            //System.err.println(e.getClass().getName() + ": " + e.getMessage());
            return null;
        }
    }

    /*@Deprecated
    public static String getCollaborateurTrelloIdBoard(@Nonnull String collaborateur){
        try {
            ResultSet rst = getStatement().executeQuery("select trello_id_board from collaborateur where pseudo='" + collaborateur + "';");
            rst.next();
            return rst.getString("trello_id_board");
        }catch ( Exception e ) {
            //System.err.println(e.getClass().getName() + ": " + e.getMessage());
            return null;
        }
    }*/

    /*@Deprecated
    public static String getCollaborateurTrelloIdList(@Nonnull String collaborateur){
        try {
            ResultSet rst = getStatement().executeQuery("select trello_id_list from collaborateur where pseudo='" + collaborateur + "';");
            rst.next();
            return rst.getString("trello_id_list");
        }catch ( Exception e ) {
            //System.err.println(e.getClass().getName() + ": " + e.getMessage());
            return null;
        }
    }*/






}
