package fr.orgpro.api.local;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class SQLiteConnection {
    private static final String DB_NAME = "orgpro.db";
    private static Connection connection = null;
    private static Statement stmt = null;

    private SQLiteConnection(){
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:" + DB_NAME );
            stmt = connection.createStatement();
            stmt.execute("PRAGMA foreign_keys = ON");
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
}
