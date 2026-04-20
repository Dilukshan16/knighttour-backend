package com.knighttour.backend.repository;

import com.knighttour.backend.model.Solution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface SolutionRepository extends JpaRepository<Solution, Long> {

    List<Solution> findByPlayer_IdOrderBySolvedAtDesc(Long playerId);

    List<Solution> findByBoardSizeOrderByTimeTakenSecondsAsc(int boardSize);

    @Query("""
        SELECT s FROM Solution s
        JOIN FETCH s.player p
        WHERE s.boardSize = :boardSize
        ORDER BY s.timeTakenSeconds ASC NULLS LAST
        LIMIT 20
    """)
    List<Solution> findLeaderboard(@Param("boardSize") int boardSize);

    long countByBoardSize(int boardSize);
}
