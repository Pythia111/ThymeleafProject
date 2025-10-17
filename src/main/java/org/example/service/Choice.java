package org.example.service;

import lombok.Data;

@Data
public class Choice {
    private String text;
    private int wealthChange;
    private int healthChange;
    private int happinessChange;
    private int knowledgeChange;

    public Choice(String text, int wealth, int health, int happiness, int knowledge) {
        this.text = text;
        this.wealthChange = wealth;
        this.healthChange = health;
        this.happinessChange = happiness;
        this.knowledgeChange = knowledge;
    }
}