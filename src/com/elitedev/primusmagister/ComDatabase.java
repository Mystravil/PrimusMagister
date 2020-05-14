package com.elitedev.primusmagister;

import java.sql.*;


public class ComDatabase {
    static Connection conn = null;

    public static void main(String[] args) {
        connectToDatabase("primusmagister.db");
    }

    /**
     * Connect to SQLite3 .db file, creates it if it does not exist
     *
     * @param fileName Filename of the .db file
     */
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
                createDefaultData();
            }
        } catch (Exception e) {
            System.out.println("ERROR");
            System.out.println(e.getMessage());
        }
    }

    /**
     * Helper function for SQL execution
     *
     * @param sql SQL string to be executed
     */
    public static void executeSQL(String sql) {
        try (Statement statement = conn.createStatement()) {
            System.out.println(conn);
            statement.execute(sql);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }

    /**
     * Creates default data for testing purposes
     */
    public static void createDefaultData() {
        deleteDefaultData();
        createDictionaryTable("german");
        createDictionaryTable("english");
        createPairTable("german", "english");
        addVocable("german", "Baum");
        addVocable("german", "Auto");
        addVocable("german", "Straße");
        addVocable("english", "street");
        addVocable("english", "car");
        addVocable("english", "tree");
        addPair("german", "english", 1, 3);
        addPair("german", "english", 2, 2);
    }

    /**
     * Deletes default data for testing purposes
     */
    public static void deleteDefaultData() {
        executeSQL("DROP TABLE IF EXISTS t_dictionary_german;");
        executeSQL("DROP TABLE IF EXISTS t_dictionary_english;");
        executeSQL("DROP TABLE IF EXISTS t_vocable_pairs_german_english;");
    }

    public static ResultSet getLanguages() {
        String sql = "SELECT name FROM sqlite_master WHERE name LIKE 't_dictionary_%' AND type = table;";
        try (Statement statement = conn.createStatement()) {
            ResultSet rs = statement.executeQuery(sql);
            System.out.println(rs);
            return rs;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        return null;
    }

    /**
     * Creates a dictionary table
     *
     * @param language the language you want to create
     */
    public static void createDictionaryTable(String language) {
        language = language.toLowerCase();

        executeSQL("CREATE TABLE IF NOT EXISTS t_dictionary_" + language + " (vocable TEXT);");
    }

    /**
     * Deletes a dictionary table
     *
     * @param language the language you want to delete
     */
    public static void deleteDictionaryTable(String language) {
        executeSQL("DROP TABLE IF EXISTS t_dictionary_" + language + ";");
    }

    /**
     * Creates a vocable pair table
     *
     * @param language1 the first language of the pairs
     * @param language2 the second language of the pairs
     */
    public static void createPairTable(String language1, String language2) {
        language1 = language1.toLowerCase();
        language2 = language2.toLowerCase();

        executeSQL("CREATE TABLE IF NOT EXISTS t_vocable_pairs_" + language1 + "_" + language2 + " (vocableID1 INTEGER, vocableID2 INTEGER);");
    }

    /**
     * Deletes a vocable pair table
     *
     * @param language1 the first language of the pairs
     * @param language2 the second language of the pairs
     */
    public static void deletePairTable(String language1, String language2) {
        executeSQL("DROP TABLE IF EXISTS t_vocable_pairs_" + language1 + "_" + language2 + ";");
    }

    public static ResultSet getVocableID(String language, String vocable) {
        String sql = "SELECT rowid FROM t_dictionary_" + language + " WHERE vocable = " + vocable + ";";
        try (Statement statement = conn.createStatement()) {
            ResultSet rs = statement.executeQuery(sql);
            System.out.println(rs);
            return rs;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        return null;
    }

    public static ResultSet getVocable(String language, int vocableID) {
        String sql = "SELECT rowid FROM t_dictionary_" + language + " WHERE rowid = " + vocableID + ";";
        try (Statement statement = conn.createStatement()) {
            ResultSet rs = statement.executeQuery(sql);
            System.out.println(rs);
            return rs;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        return null;
    }

    public static ResultSet getVocableList(String language) {
        String sql = "SELECT * FROM t_dictionary_" + language + ";";
        try (Statement statement = conn.createStatement()) {
            ResultSet rs = statement.executeQuery(sql);
            System.out.println(rs);
            return rs;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        return null;
    }

    /**
     * Adds a vocable
     *
     * @param language the language of vocable
     * @param vocable  the vocable string
     */
    public static void addVocable(String language, String vocable) {
        language = language.toLowerCase();

        String sql = "INSERT INTO t_dictionary_" + language + " VALUES(?)";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, vocable);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Updates a vocable
     *
     * @param language   the language of vocable
     * @param vocable  the vocable
     * @param newVocable the new string for the vocable
     */
    public static void updateVocable(String language, String vocable, String newVocable) {
        language = language.toLowerCase();

        String sql = "UPDATE t_dictionary_" + language + " SET vocable = ? WHERE rowid = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, newVocable);
            pstmt.setString(1, vocable);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Deletes a vocable
     *
     * @param language  the language of vocable
     * @param vocable the vocable
     */
    public static void deleteVocable(String language, String vocable) {
        language = language.toLowerCase();

        String sql = "DELETE FROM t_dictionary_" + language + " WHERE rowid = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, vocable);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Adds a vocable pair
     *
     * @param language1  the first language of the pair
     * @param language2  the second language of the pair
     * @param vocableID1 the first vocable rowid of the pair
     * @param vocableID2 the second vocable rowid of the pair
     */
    public static void addPair(String language1, String language2, int vocableID1, int vocableID2) {
        language1 = language1.toLowerCase();
        language2 = language2.toLowerCase();

        String sql = "INSERT INTO t_vocable_pairs_" + language1 + "_" + language2 + " VALUES(?,?)";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, vocableID1);
            pstmt.setInt(2, vocableID2);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Deletes a vocable pair
     *
     * @param language1 the first language of the pair
     * @param language2 the second language of the pair
     * @param pairID    the rowid of the pair
     */
    public static void deletePair(String language1, String language2, int pairID) {
        language1 = language1.toLowerCase();
        language2 = language2.toLowerCase();

        String sql = "DELETE FROM t_vocable_pairs_" + language1 + "_" + language2 + " WHERE rowid = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, pairID);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
