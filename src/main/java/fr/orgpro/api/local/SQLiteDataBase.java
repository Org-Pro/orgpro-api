package fr.orgpro.api.local;

import fr.orgpro.api.project.Tache;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.sql.*;
import java.util.List;

public class SQLiteDataBase {
    private static final String DB_NAME = "orgpro.db";

    /**
     * Connexion avec la base de données locale
     * @return La connexion si elle est établie, null sinon
     */
    private static Connection connection(){
        Connection c;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:" + DB_NAME );
        } catch ( Exception e ) {
            //System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            return null;
        }
        //System.out.println("Opened database successfully");
        return c;
    }

    /**
     * Crée la structure de la base de données locale
     * @return False en cas d'erreur
     */
    public static boolean createTable(){
        Statement stmt;
        Connection c = connection();
        try {
            stmt = c.createStatement();
            stmt.execute("PRAGMA foreign_keys = ON");

            stmt.execute("create table if not exists collaborateur (" +
                    "pseudo char(255) primary key not null," +
                    "nom char(255)," +
                    "prenom char(255)," +
                    "google_id_liste char(255)" +
                    ");");

            stmt.execute("create table if not exists tache (" +
                    "uuid char(255) primary key not null" +
                    ");");

            stmt.execute("create table if not exists synchro (" +
                    "uuid_tache char(255) not null," +
                    "pseudo_collaborateur char(255) not null," +
                    "google_id_tache char(255)," +
                    "est_synchro boolean not null default false," +
                    "unique (uuid_tache, pseudo_collaborateur)," +
                    "foreign key (uuid_tache) references tache(uuid) on delete cascade DEFERRABLE INITIALLY DEFERRED," +
                    "foreign key (pseudo_collaborateur) references collaborateur(pseudo) on delete cascade DEFERRABLE INITIALLY DEFERRED" +
                    ");");

            stmt.close();
            c.close();
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
        Statement stmt;
        Connection c = connection();
        try {
            stmt = c.createStatement();
            stmt.execute("PRAGMA foreign_keys = ON");
            stmt.execute("insert into tache(uuid) values('" + tache.getId() + "');");

            stmt.close();
            c.close();
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
        Statement stmt;
        Connection c = connection();
        try {
            stmt = c.createStatement();
            stmt.execute("PRAGMA foreign_keys = ON");
            for (Tache tache : tacheListe){
                stmt.execute("insert into tache(uuid) values('" + tache.getId() + "');");
            }

            stmt.close();
            c.close();
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
        Statement stmt;
        Connection c = connection();
        try {
            stmt = c.createStatement();
            stmt.execute("PRAGMA foreign_keys = ON");
            int rst = stmt.executeUpdate("delete from tache where uuid = ('" + tache.getId() + "');");

            stmt.close();
            c.close();

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
        Statement stmt;
        Connection c = connection();
        try {
            stmt = c.createStatement();
            stmt.execute("PRAGMA foreign_keys = ON");
            stmt.execute("insert into collaborateur(pseudo, nom, prenom, google_id_liste) values('"
                    + collaborateur + "', " + stringReqValue(nom) + ", " + stringReqValue(prenom) + ", " + stringReqValue(google_id_liste) + ");");

            stmt.close();
            c.close();
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
        Statement stmt;
        Connection c = connection();
        try {
            stmt = c.createStatement();
            stmt.execute("PRAGMA foreign_keys = ON");
            int rst = stmt.executeUpdate("delete from collaborateur where pseudo = ('" + collaborateur + "');");

            stmt.close();
            c.close();

            if (rst == 0) return false;
        }catch ( Exception e ) {
            //System.err.println(e.getClass().getName() + ": " + e.getMessage());
            return false;
        }
        return true;
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
        Statement stmt;
        Connection c = connection();
        try {
            stmt = c.createStatement();
            stmt.execute("PRAGMA foreign_keys = ON");

            boolean synchro_req = false;
            // TODO google_id_tache != null -> à verifier ?
            if(google_id_tache != null && estSynchro != null) synchro_req = estSynchro;
            stmt.execute("insert into synchro(uuid_tache, pseudo_collaborateur, google_id_tache, est_synchro) values('"
                    + tache.getId() + "', '" + collaborateur + "', " + stringReqValue(google_id_tache) + ",'" + synchro_req + "');");

            stmt.close();
            c.close();
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
        Statement stmt;
        Connection c = connection();
        try {
            stmt = c.createStatement();
            stmt.execute("PRAGMA foreign_keys = ON");
            int rst = stmt.executeUpdate("delete from synchro where uuid_tache='" + tache.getId() + "' and pseudo_collaborateur='" + collaborateur + "';");

            stmt.close();
            c.close();

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
        Statement stmt;
        Connection c = connection();
        try {
            stmt = c.createStatement();
            stmt.execute("PRAGMA foreign_keys = ON");
            int rst = stmt.executeUpdate("update synchro set est_synchro='" + estSynchro + "' where uuid_tache='" + tache.getId() + "' and pseudo_collaborateur='" + collaborateur + "';");

            stmt.close();
            c.close();

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
        Statement stmt;
        Connection c = connection();
        try {
            stmt = c.createStatement();
            stmt.execute("PRAGMA foreign_keys = ON");
            int rst = stmt.executeUpdate("update synchro set google_id_tache=" + stringReqValue(google_id_tache) + " where uuid_tache='" + tache.getId() + "' and pseudo_collaborateur='" + collaborateur + "';");

            stmt.close();
            c.close();

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
