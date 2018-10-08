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

    private SQLiteConnection(){
        try {
            File[] files = new File(DB_FOLDER).listFiles();
            if(files == null) new File(DB_FOLDER).mkdirs();

            boolean db_existe = true;
            DB_NAME = Tache.getEnTete(Tache.HEADER_DB);

            if(DB_NAME == null){
                db_existe = false;
                DB_NAME = UUID.randomUUID().toString() + ".db";
                if(!Tache.addEnTete(Tache.HEADER_DB, DB_NAME, true)){
                    return;
                }
            }

            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:" + DB_FOLDER  + "/" + DB_NAME );
            stmt = connection.createStatement();
            stmt.execute("PRAGMA foreign_keys = ON");

            if (!db_existe) SQLiteDataBase.createTable();

        } catch ( Exception e ) {
            connection = null;
            stmt = null;
        }
    }

    public static Statement getStatement(){
        if (stmt != null) return stmt;
        else {
            new SQLiteConnection();
            return stmt;
        }
    }

    public static void closeConnection(){
        try {
            stmt.close();
            connection.close();
        }catch (Exception e){
            stmt = null;
            connection = null;
        }
    }

    public static String getDbName(){
        return DB_NAME;
    }

    public static String getDbFolder(){
        return DB_FOLDER;
    }
}
