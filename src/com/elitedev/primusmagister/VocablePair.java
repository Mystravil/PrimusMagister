package com.elitedev.primusmagister;

public class VocablePair {
    public int id;

    public int vocId1;
    public String language1;
    public Vocable voc1 = ComDatabase.getVocable(language1, vocId1);

    public int vocId2;
    public String language2;
    public Vocable voc2 = ComDatabase.getVocable(language2, vocId2);

    public int skill_value;
}
