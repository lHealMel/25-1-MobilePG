package com.example.mbpg.util;

public class MbtiCalculator {

    private int eCount = 0, iCount = 0;
    private int sCount = 0, nCount = 0;
    private int tCount = 0, fCount = 0;
    private int jCount = 0, pCount = 0;

    public void addTrait(String trait, boolean choseA) {
        switch (trait) {
            case "EI":
                if (choseA) eCount++;
                else iCount++;
                break;
            case "SN":
                if (choseA) sCount++;
                else nCount++;
                break;
            case "TF":
                if (choseA) tCount++;
                else fCount++;
                break;
            case "JP":
                if (choseA) jCount++;
                else pCount++;
                break;
        }
    }

    public String getResult() {
        return (eCount >= iCount ? "E" : "I") +
                (sCount >= nCount ? "S" : "N") +
                (tCount >= fCount ? "T" : "F") +
                (jCount >= pCount ? "J" : "P");
    }
}
