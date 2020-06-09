package com.elitedev.primusmagister;

import java.util.List;

public class ComViewModel {
    private static String sourceVoc;
    public static String getSourceVoc() { return sourceVoc; }
    public static void setSourceVoc(String newSourceVoc) { sourceVoc = newSourceVoc; }

    private static String targetVoc;
    public static String getTargetVoc() { return targetVoc; }
    public static void setTargetVoc(String newTargetVoc) { targetVoc = newTargetVoc; }

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
