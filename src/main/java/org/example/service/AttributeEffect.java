package org.example.service;

import lombok.Data;

@Data
public class AttributeEffect {
    private int wealthChange;
    private int healthChange;
    private int happinessChange;
    private int knowledgeChange;

    public AttributeEffect(int wealthChange, int healthChange, int happinessChange, int knowledgeChange) {
        this.wealthChange = wealthChange;
        this.healthChange = healthChange;
        this.happinessChange = happinessChange;
        this.knowledgeChange = knowledgeChange;
    }
}