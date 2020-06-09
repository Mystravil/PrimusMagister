package com.elitedev.primusmagister;

public class ComViewModel {
    private static String sourceLang;
    public static String getSourceLang() { return sourceLang; }
    public static void setSourceLang(String newSourceVoc) { sourceLang = newSourceVoc; }

    private static String targetLang;
    public static String getTargetLang() { return targetLang; }
    public static void setTargetLang(String newTargetVoc) { targetLang = newTargetVoc; }

    public String selectVocablePair(boolean source) {
        if (source) {
            // Select Vocable
            String test = "2";
            return test;


        }
        String test = "2";
        return test;
    }
}
