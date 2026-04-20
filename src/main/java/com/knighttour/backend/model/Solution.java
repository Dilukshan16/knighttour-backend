package com.knighttour.backend.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "solutions")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Solution {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "player_id", nullable = false)
    private Player player;

    @Column(name = "board_size", nullable = false)
    private int boardSize; // 8 or 16

    @Column(name = "start_x", nullable = false)
    private int startX;

    @Column(name = "start_y", nullable = false)
    private int startY;

    @Column(name = "algorithm_used", length = 30)
    private String algorithmUsed; // WARNSDORFF or BACKTRACKING

    @Column(name = "solved_at", nullable = false)
    private LocalDateTime solvedAt;

    @Column(name = "time_taken_seconds")
    private Long timeTakenSeconds;

    @OneToMany(mappedBy = "solution", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @OrderBy("stepNumber ASC")
    private List<MoveStep> moveSteps;

    @PrePersist
    protected void onCreate() {
        this.solvedAt = LocalDateTime.now();
    }
}
