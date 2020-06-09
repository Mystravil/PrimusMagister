package com.elitedev.primusmagister;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class ComDatabase {
    static Connection conn = connectToDatabase("primusmagister.db");

    public static void main(String[] args) {
        conn = connectToDatabase("primusmagister.db");
        createDefaultData();
        List<Vocable> vocables = getVocableList("german");
        Vocable voc = getVocable("german", 1);
        List<VocablePair> vocpairs = getPairList("german", "english");
        System.out.println("test");
        List<String> test = getLanguages();
    }

    /**
     * Connect to SQLite3 .db file, creates it if it does not exist
     *
     * @param fileName Filename of the .db file
     * @return conn
     */
    public static Connection connectToDatabase(String fileName) {
        String url = "jdbc:sqlite:" + fileName;
        System.out.println(url);

        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try {
            conn = DriverManager.getConnection(url);
            if (conn != null) {
                DatabaseMetaData meta = conn.getMetaData();
                System.out.println("The driver name is " + meta.getDriverName());
                return conn;
            }
        } catch (Exception e) {
            System.out.println("ERROR");
            System.out.println(e.getMessage());
        }
        return null;
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

    public static ArrayList<String> getLanguages() {
        String sql = "SELECT name FROM sqlite_master WHERE name LIKE 't_dictionary_%' AND type = 'table';";
        ArrayList<String> languages = new ArrayList<>();

        try (Statement statement = conn.createStatement()) {
            ResultSet rs = statement.executeQuery(sql);

            while (rs.next()) {
                languages.add(rs.getString("name").substring(13));
            }
            rs.close();
            return languages;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            return null;
        }
    }

    /**
     * Creates a dictionary table
     *
     * @param language the language you want to create
     */
    public static void createDictionaryTable(String language) {
        language = language.toLowerCase();

        executeSQL("CREATE TABLE IF NOT EXISTS t_dictionary_" + language + " (name TEXT UNIQUE);");
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

        executeSQL("CREATE TABLE IF NOT EXISTS t_vocable_pairs_" + language1 + "_" + language2 + " (vocableID1 INTEGER, vocableID2 INTEGER, skill_value INTEGER DEFAULT 0);");
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

   /* public static List<Vocable> getVocableID(String language, String name) {
        List<Vocable> vocables = new ArrayList<>();

        String sql = "SELECT rowid FROM t_dictionary_" + language + " WHERE vocable = '" + name + "';";
        try (Statement statement = conn.createStatement()) {
            ResultSet rs = statement.executeQuery(sql);

            while (rs.next()) {
                Vocable vocable = new Vocable();
                vocable.id = rs.getInt("rowid");
                // vocable.name = rs.getString("name");
                vocables.add(vocable);
            }

            return vocables;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        return null;
    }*/

    public static Vocable getVocable(String language, int vocableID) {
        String sql = "SELECT rowid, name FROM t_dictionary_" + language + " WHERE rowid = '" + vocableID + "';";

        try (Statement statement = conn.createStatement()) {
            ResultSet rs = statement.executeQuery(sql);
            Vocable vocable = new Vocable();
            while (rs.next()) {
                vocable.id = rs.getInt("rowid");
                vocable.name = rs.getString("name");
            }

            rs.close();
            return vocable;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            return null;
        }
    }

    public static List<Vocable> getVocableList(String language) {
        List<Vocable> vocables = new ArrayList<>();
        String sql = "SELECT rowid, name FROM t_dictionary_" + language + ";";

        try (Statement statement = conn.createStatement()) {
            ResultSet rs = statement.executeQuery(sql);

            while (rs.next()) {
                Vocable vocable = new Vocable();
                vocable.id = rs.getInt("rowid");
                vocable.name = rs.getString("name");
                vocables.add(vocable);
            }

            rs.close();
            return vocables;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            return null;
        }
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
     * @param vocable    the vocable
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
     * @param language the language of vocable
     * @param vocable  the vocable
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

    public static List<VocablePair> getPairList(String language1, String language2) {
        List<VocablePair> vocablepairs = new ArrayList<>();
        String sql = "SELECT * FROM t_vocable_pairs_" + language1 + "_" + language2 + ";";

        try (Statement statement = conn.createStatement()) {
            ResultSet rs = statement.executeQuery(sql);

            while (rs.next()) {
                VocablePair vocablepair = new VocablePair();
                vocablepair.language1 = language1;
                vocablepair.language2 = language2;
                vocablepair.vocId1 = rs.getInt("vocableID1");
                vocablepair.vocId2 = rs.getInt("vocableID2");
                vocablepair.voc1 = getVocable(vocablepair.language1, vocablepair.vocId1);
                vocablepair.voc2 = getVocable(vocablepair.language2, vocablepair.vocId2);
                vocablepairs.add(vocablepair);
            }

            rs.close();
            return vocablepairs;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        return null;
    }

    public static VocablePair getPair(String language1, String language2, int vocableID1, int vocableID2) {
        VocablePair vocablepair = new VocablePair();
        String sql = "SELECT * FROM t_vocable_pairs_" + language1 + "_" + language2 + " WHERE vocableID1 = '" + vocableID1 + "' AND vocableID2 = '" + vocableID2 + "';";

        try (Statement statement = conn.createStatement()) {
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()) {
                vocablepair.language1 = language1;
                vocablepair.language2 = language2;
                vocablepair.vocId1 = rs.getInt("vocableID1");
                vocablepair.vocId2 = rs.getInt("vocableID2");
                vocablepair.voc1 = getVocable(vocablepair.language1, vocablepair.vocId1);
                vocablepair.voc2 = getVocable(vocablepair.language2, vocablepair.vocId2);
                vocablepair.skill_value = rs.getInt("skill_value");
            }
            return vocablepair;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        return null;
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
