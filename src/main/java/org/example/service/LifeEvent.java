package org.example.service;

import lombok.Data;
import java.util.List;

@Data
public class LifeEvent {
    private String description;
    private List<Choice> choices;

    public LifeEvent(String description, List<Choice> choices) {
        this.description = description;
        this.choices = choices;
    }
}