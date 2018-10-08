package fr.orgpro.api.local;

import fr.orgpro.api.project.Tache;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.UUID;

public class SQLiteConnection {
    private static String DB_NAME = null;
    private static final String DB_FOLDER = "db";
    private static Connection connection = null;
    private static Statement stmt = null;

    /**
     * Connexion à la base de données locale
     */
    private SQLiteConnection(){
        try {
            // Création du dossier contenant les bases de données s'il n'existe pas
            File[] files = new File(DB_FOLDER).listFiles();
            if(files == null) new File(DB_FOLDER).mkdirs();

            boolean db_existe = true;
            // Récupère le nom de la base lié au fichier .org
            DB_NAME = Tache.getEnTete(Tache.HEADER_DB);

            // Si la base n'existe pas
            if(DB_NAME == null){
                db_existe = false;
                // Génère un nom aléatoire pour la base de données
                DB_NAME = UUID.randomUUID().toString() + ".db";
                // Ajoute son nom au fichier .org pour garder le lien entre les deux
                if(!Tache.addEnTete(Tache.HEADER_DB, DB_NAME, true)){
                    return;
                }
            }

            // Connexion
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:" + DB_FOLDER  + "/" + DB_NAME );
            stmt = connection.createStatement();
            stmt.execute("PRAGMA foreign_keys = ON");

            // Génération de la structure de la base de données s'il elle vient d'être créée
            if (!db_existe) SQLiteDataBase.createTable();

        } catch ( Exception e ) {
            connection = null;
            stmt = null;
        }
    }

    /**
     * Récupère le singleton de connexion
     * @return 
     */
    public static Statement getStatement(){
        if (stmt != null) return stmt;
        else {
            new SQLiteConnection();
            return stmt;
        }
    }

    /**
     * Ferme l'accès à la base de données
     */
    public static void closeConnection(){
        try {
            stmt.close();
            connection.close();
        }catch (Exception e){
            stmt = null;
            connection = null;
        }
    }

    /**
     * @return Le nom de base de données, null sinon
     */
    public static String getDbName(){
        return DB_NAME;
    }

    /**
     * @return Le nom du dossier où sont stockées les bases de données
     */
    public static String getDbFolder(){
        return DB_FOLDER;
    }
}
