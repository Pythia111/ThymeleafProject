package org.example.entity;

import lombok.Data;
import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "game_records")
@Data
public class GameRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String characterName;
    private Integer finalAge; // Add finalAge field
    private Integer finalWealth;
    private Integer finalHealth;
    private Integer finalHappiness;
    private Integer finalKnowledge;
    private String endingType;
    private Integer totalEvents;

    @Lob
    private String lifePath; // 使用大字段存储完整的人生路径

    private LocalDateTime playTime = LocalDateTime.now();
}