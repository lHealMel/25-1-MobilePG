package com.example.mbpg.model;

public class Question {
    private String question;
    private String optionA;
    private String optionB;
    private String trait; // EI, SN, TF, JP

    public Question(String question, String optionA, String optionB, String trait) {
        this.question = question;
        this.optionA = optionA;
        this.optionB = optionB;
        this.trait = trait;
    }

    public String getQuestion() {
        return question;
    }

    public String getOptionA() {
        return optionA;
    }

    public String getOptionB() {
        return optionB;
    }

    public String getTrait() {
        return trait;
    }
}
