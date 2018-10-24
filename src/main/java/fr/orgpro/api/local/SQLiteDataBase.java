package fr.orgpro.api.local;

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
                    "google_id_liste char(255)" +
                    ");");

            getStatement().execute("create table if not exists tache (" +
                    "uuid char(255) primary key not null" +
                    ");");

            getStatement().execute("create table if not exists synchro (" +
                    "uuid_tache char(255) not null," +
                    "pseudo_collaborateur char(255) not null," +
                    "google_id_tache char(255)," +
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

    /**
     * Ajoute un collaborateur à la base de données locale
     * @param collaborateur Pseudo du collaborateur à ajouter
     * @param nom Nom du collaborateur à ajouter
     * @param prenom Prenom du collaborateur à ajouter
     * @param google_id_liste L'id de la liste créée par l'API Google du collaborateur à ajouter
     * @return False en cas d'erreur
     */
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

    /**
     * Modifie le pseudo d'un collaborateur déjà existant
     * @param oldCollaborateur Le pseudo du collaborateur à modifier
     * @param newCollaborateur Le nouveau pseudo du collaborateur à modifier
     * @return False en cas d'erreur
     */
    public static boolean updateCollaborateur(@Nonnull String oldCollaborateur, @Nonnull String newCollaborateur){
        try {
            int rst = getStatement().executeUpdate("update collaborateur set pseudo='" + newCollaborateur + "' where pseudo='" + oldCollaborateur + "';");
            if (rst == 0) return false;
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

    /**
     * Récupère l'id de la liste de l'API google d'un collaborateur déjà existant
     * @param collaborateur Le pseudo du collaborateur concerné
     * @return L'id de la liste de l'API google, null sinon
     */
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

    /**
     * Récupère les synchro d'un collaborateur déjà existant
     * @param collaborateur Le pseudo du collaborateur concerné
     * @return La liste des synchro
     */
    public static List<SQLSynchro> getAllSynchroByCollaborateur(@Nonnull String collaborateur){
        try {
            List<SQLSynchro> listeSynchro = new ArrayList<SQLSynchro>();
            ResultSet rst = getStatement().executeQuery("select * from synchro where pseudo_collaborateur='" + collaborateur + "';");
            while(rst.next()){
                listeSynchro.add(new SQLSynchro(rst.getString("uuid_tache"), rst.getString("pseudo_collaborateur"), rst.getString("google_id_tache"), rst.getBoolean("est_synchro")));
            }
            return listeSynchro;
        }catch ( Exception e ) {
            //System.err.println(e.getClass().getName() + ": " + e.getMessage());
            return null;
        }
    }


    /**
     * Ajoute un lien entre une tâche et un collaborateur avec des paramètres supplémentaires liés aux APIs
     * @param tache La tâche à lier
     * @param collaborateur Le collaborateur à lier
     * @param google_id_tache L'id de la tâche créée par l'API Google
     * @param estSynchro L'état de la synchronisation avec les APIs en ligne
     * @return False en cas d'erreur
     */
    public static boolean synchroAddTacheCollaborateur(@Nonnull Tache tache, @Nonnull String collaborateur, @Nullable String google_id_tache, @Nullable Boolean estSynchro){
        try {
            boolean synchro_req = false;
            // TODO google_id_tache != null -> à verifier ?
            if(google_id_tache != null && estSynchro != null) synchro_req = estSynchro;
            getStatement().execute("insert into synchro(uuid_tache, pseudo_collaborateur, google_id_tache, est_synchro) values('"
                    + tache.getId() + "', '" + collaborateur + "', " + stringReqValue(google_id_tache) + ",'" + synchro_req + "');");
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
     * Modifie l'état de synchronisation d'un lien entre une tâche et un collaborateur
     * @param tache La tâche liée
     * @param collaborateur Le pseudo du collaborateur lié
     * @param estSynchro L'état de la synchronisation avec les APIs en ligne à modifier
     * @return False en cas d'erreur
     */
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
     * Modifie l'état de synchronisation de tous les liens entre une tâche et un collaborateur selon une tâche choisie
     * @param tache La tâche concernée
     * @param estSynchro L'état de la synchronisation avec les APIs en ligne à modifier
     * @return False en cas d'erreur
     */
    public static boolean synchroUpdateAllEstSynchroByTache(@Nonnull Tache tache, boolean estSynchro){
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
     * Modifie l'id de tâche liée à l'API Google d'un lien entre une tâche et un collaborateur
     * @param tache La tâche liée
     * @param collaborateur Le pseudo du collaborateur lié
     * @param google_id_tache L'id de la tâche liée à l'API Google à modifier
     * @return False en cas d'erreur
     */
    public static boolean synchroUpdateGoogleIdTache(@Nonnull Tache tache, @Nonnull String collaborateur, @Nullable String google_id_tache){
        try {
            int rst = getStatement().executeUpdate("update synchro set google_id_tache=" + stringReqValue(google_id_tache) + " where uuid_tache='" + tache.getId() + "' and pseudo_collaborateur='" + collaborateur + "';");
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


}
