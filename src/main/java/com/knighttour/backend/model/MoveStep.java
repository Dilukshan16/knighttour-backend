package com.knighttour.backend.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "move_steps")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MoveStep {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "solution_id", nullable = false)
    private Solution solution;

    @Column(name = "step_number", nullable = false)
    private int stepNumber;

    @Column(name = "x_pos", nullable = false)
    private int x;

    @Column(name = "y_pos", nullable = false)
    private int y;
}
