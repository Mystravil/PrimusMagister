package com.elitedev.primusmagister;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.DatabaseMetaData;
import java.sql.Statement;


public class ComDatabase {
    static Connection conn = null;

    public static void main(String[] args) {
        connectToDatabase("learning.db");
    }

    public static void connectToDatabase(String fileName) {
        String url = "jdbc:sqlite:" + fileName;
        System.out.println(url);

        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try (Connection connection = DriverManager.getConnection(url)) {
            conn = connection;
            if (conn != null) {
                DatabaseMetaData meta = conn.getMetaData();
                System.out.println("The driver name is " + meta.getDriverName());
                createTables();
            }
        } catch (Exception e) {
            System.out.println("ERROR");
            System.out.println(e.getMessage());
        }
    }

    public static void executeSQL(String sql) {
        try (Statement statement = conn.createStatement()) {
            System.out.println(conn);
            // create a new table
            statement.execute(sql);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }

    public static void createTables() {
        executeSQL("CREATE TABLE IF NOT EXISTS t_vocable_list (german TEXT, english TEXT, french TEXT, spanish TEXT);");
        executeSQL("CREATE TABLE IF NOT EXISTS t_learning_value (vocable_id INTEGER);");
    }
}
