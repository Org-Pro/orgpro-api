package fr.orgpro.api.local;

import java.sql.*;

public class SQLiteJDBC {
    private static String DB_NAME = "orgpro.db";

    public static void main( String args[] ) {
        createTable();
    }

    private static Connection connection(){
        Connection c;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:" + DB_NAME);
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            return null;
        }
        System.out.println("Opened database successfully");
        return c;
    }

    public static void createTable(){
        Statement stmt;
        Connection c = connection();
        try {
            stmt = c.createStatement();
            stmt.execute("PRAGMA foreign_keys = ON");

            stmt.execute("create table if not exists collaborateur (" +
                    "id integer primary key AUTOINCREMENT not null," +
                    "pseudo char(255) unique not null," +
                    "nom char(255)," +
                    "prenom char(255)," +
                    "google_id_liste char(255)" +
                    ");");

            stmt.execute("create table if not exists tache (" +
                    "id integer primary key AUTOINCREMENT not null," +
                    "uuid char(255) unique not null" +
                    ");");

            stmt.execute("create table if not exists synchro (" +
                    "id_tache char(255) not null," +
                    "id_collaborateur char(255) not null," +
                    "est_synchro boolean not null default false," +
                    "unique (id_tache, id_collaborateur)," +
                    "foreign key (id_tache) references tache(id) on delete cascade DEFERRABLE INITIALLY DEFERRED," +
                    "foreign key (id_collaborateur) references collaborateur(id) on delete cascade DEFERRABLE INITIALLY DEFERRED" +
                    ");");

            stmt.close();
            c.close();
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            return;
        }
        System.out.println("Table created successfully");
    }

    
}
