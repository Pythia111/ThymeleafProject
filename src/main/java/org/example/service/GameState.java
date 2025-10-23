package org.example.service;

import lombok.Data;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Data
public class GameState {
    private String characterName;
    private int age = 0;
    private int wealth = 0;
    private int health = 0;
    private int happiness = 0;
    private int knowledge = 0;
    private int availablePoints = 20;
    private boolean attributesAllocated = false;
    // 获取事件历史
    private List<String> lifeEvents = new ArrayList<>();
    private boolean gameEnded = false;
    private String currentEvent;
    private List<String> currentChoices;
    private String lastAttributeChanges = "";
    private String currentNodeId = "start";

    // 新增字段用于决策树
    private String lastKeyNode = "start";
    private String pendingKeyNode;
    private RandomEvent pendingRandomEvent;

    public void addEvent(String event) {
        // 生成完整的事件描述
        String eventWithAge = "年龄" + this.age + "岁: " + event;

        // 避免重复添加相同事件 - 检查最近的几个事件
        boolean isDuplicate = false;
        int checkCount = Math.min(3, lifeEvents.size()); // 检查最近3个事件

        for (int i = lifeEvents.size() - checkCount; i < lifeEvents.size(); i++) {
            String existingEvent = lifeEvents.get(i);
            // 如果事件内容完全相同，则认为是重复
            if (existingEvent.equals(eventWithAge)) {
                isDuplicate = true;
                break;
            }
            // 如果是相同年龄的相同事件内容（不同选择），也认为是潜在重复
            String existingEventContent = existingEvent.substring(existingEvent.indexOf(": ") + 2);
            String newEventContent = event;
            if (existingEvent.startsWith("年龄" + this.age + "岁:") &&
                existingEventContent.split(" → ")[0].equals(newEventContent.split(" → ")[0])) {
                isDuplicate = true;
                break;
            }
        }

        if (!isDuplicate) {
            lifeEvents.add(eventWithAge);
        }
    }

    public void applyEffect(int wealthChange, int healthChange,
                            int happinessChange, int knowledgeChange) {
        this.wealth = Math.max(0, Math.min(100, this.wealth + wealthChange));
        this.health = Math.max(0, Math.min(100, this.health + healthChange));
        this.happiness = Math.max(0, Math.min(100, this.happiness + happinessChange));
        this.knowledge = Math.max(0, Math.min(100, this.knowledge + knowledgeChange));

        // 跟踪变化用于显示
        StringBuilder changes = new StringBuilder();
        if (wealthChange != 0) changes.append("财富").append(wealthChange > 0 ? "+" : "").append(wealthChange).append(" ");
        if (healthChange != 0) changes.append("健康").append(healthChange > 0 ? "+" : "").append(healthChange).append(" ");
        if (happinessChange != 0) changes.append("快乐").append(happinessChange > 0 ? "+" : "").append(happinessChange).append(" ");
        if (knowledgeChange != 0) changes.append("智慧").append(knowledgeChange > 0 ? "+" : "").append(knowledgeChange).append(" ");

        this.lastAttributeChanges = changes.toString().trim();
    }

    public void allocateInitialPoints(int wealth, int health, int happiness, int knowledge) {
        if (wealth + health + happiness + knowledge <= availablePoints) {
            this.wealth += wealth;
            this.health += health;
            this.happiness += happiness;
            this.knowledge += knowledge;
            this.availablePoints -= (wealth + health + happiness + knowledge);
            if (this.availablePoints == 0) {
                this.attributesAllocated = true;
            }
        }
    }

    public boolean isGameOver() {
        return health <= 0 || age >= 100 || gameEnded;
    }

    // 检查是否触发即时结局
    public String checkImmediateEnding() {
        if (health <= 0) {
            if (age < 1) return "IMMEDIATE_DEATH_INFANT";
            if (age < 18) return "IMMEDIATE_DEATH_YOUNG";
            if (age < 40) return "IMMEDIATE_DEATH_PRIME";
            if (age < 60) return "IMMEDIATE_DEATH_MIDDLE";
            return "IMMEDIATE_DEATH_OLD";
        }
        if (wealth >= 100) return "IMMEDIATE_SUPER_RICH";
        if (knowledge >= 100) return "IMMEDIATE_GENIUS";
        if (happiness >= 100) return "IMMEDIATE_BLISS";
        if (wealth <= 0 && age >= 18) return "IMMEDIATE_BANKRUPTCY";
        return null;
    }

}