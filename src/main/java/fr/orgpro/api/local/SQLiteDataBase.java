package fr.orgpro.api.local;

import fr.orgpro.api.project.Tache;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.sql.*;
import java.util.List;

public class SQLiteDataBase {
    private static final String DB_NAME = "orgpro.db";

    /*public static void main( String args[] ) {
        //createTable();
        List<Tache> list = new ArrayList<>();
        list.add(new Tache(""));
        list.add(new Tache(""));

        addTacheListe(list);
        //deleteTache(list.get(0));
        addCollaborateur("col2", "qsdfzq", "qdazd", null);
        synchroAddTacheCollaborateur(list.get(1), "col", "aqdqd", null);

        synchroUpdateEstSynchro(list.get(1), true);
        System.out.println(synchroUpdateGoogleIdTache(list.get(1), "test"));
        //synchAddTacheCollaborateur(list.get(1), "col");
        //System.out.println(deleteCollaborateur("col"));
        //synchDeleteTacheCollaborateur(list.get(1), "col");

    }*/

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
     * Créé la structure de la base de données locale
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
     * Supprime la tâche de la base de données locale
     * @param tache La tâche à supprimer
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

    public static boolean deleteCollaborateur(@Nonnull String collaborateur){
        Statement stmt;
        Connection c = connection();
        try {
            stmt = c.createStatement();
            stmt.execute("PRAGMA foreign_keys = ON");
            int rst = stmt.executeUpdate("delete from collaborateur where pseudo = ('" + collaborateur + "');");
            System.out.println(rst);

            stmt.close();
            c.close();

            if (rst == 0) return false;
        }catch ( Exception e ) {
            //System.err.println(e.getClass().getName() + ": " + e.getMessage());
            return false;
        }
        return true;
    }

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

    public static boolean synchroUpdateEstSynchro(@Nonnull Tache tache, boolean estSynchro){
        Statement stmt;
        Connection c = connection();
        try {
            stmt = c.createStatement();
            stmt.execute("PRAGMA foreign_keys = ON");
            int rst = stmt.executeUpdate("update synchro set est_synchro='" + estSynchro + "' where uuid_tache='" + tache.getId() + "';");

            stmt.close();
            c.close();

            if (rst == 0) return false;
        }catch ( Exception e ) {
            //System.err.println(e.getClass().getName() + ": " + e.getMessage());
            return false;
        }
        return true;
    }

    public static boolean synchroUpdateGoogleIdTache(@Nonnull Tache tache,@Nullable String google_id_tache){
        Statement stmt;
        Connection c = connection();
        try {
            stmt = c.createStatement();
            stmt.execute("PRAGMA foreign_keys = ON");
            int rst = stmt.executeUpdate("update synchro set google_id_tache=" + stringReqValue(google_id_tache) + " where uuid_tache='" + tache.getId() + "';");

            stmt.close();
            c.close();

            if (rst == 0) return false;
        }catch ( Exception e ) {
            //System.err.println(e.getClass().getName() + ": " + e.getMessage());
            return false;
        }
        return true;
    }

    private static String stringReqValue(@Nullable String val){
        if(val == null) return "null";
        else return "'" + val + "'";
    }


}
