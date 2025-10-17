package org.example.dao;

import org.example.entity.GameRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface GameRecordRepository extends JpaRepository<GameRecord, Long> {
    List<GameRecord> findByUserIdOrderByPlayTimeDesc(Long userId);
}