package com.elitedev.primusmagister;

import java.sql.*;
import java.util.List;

public class ComConfig {
    private ComDatabase _comDatabase = new ComDatabase();

    // --- Language Config ---
    public void constructLanguages(ResultSet languagesRS) {

    }

    public void addLanguage(String language) {

    }

    public void removeLanguage(String language) {

    }

    public void editLanguage(String language) {

    }

    // --- Vocable Config ---
    public List<Vocable> constructVocables(String language) {
        return _comDatabase.getVocableList(language);
    }

    public void addVocable(String vocable, String language) {

    }

    public void removeVocable(String vocable, String language) {

    }

    public void editVocable(String vocable, String language) {

    }

    // --- VocablePair Config ---
    public void constructVocablePairs(ResultSet vocablePairsRS) {

    }

    public void addVocablePair(String vocablePair, String language_1, String language_2) {

    }

    public void removeVocablePair(String vocablePair, String language_1, String language_2) {

    }

    private void _parseVocablePair(String vocablePair) {

    }
}
