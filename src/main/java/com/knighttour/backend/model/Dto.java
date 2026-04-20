package com.knighttour.backend.model;

import jakarta.validation.constraints.*;
import lombok.*;
import org.antlr.v4.runtime.misc.NotNull;

import java.time.LocalDateTime;
import java.util.List;

// DTO classes for REST API communication

public class Dto {

    // ---- Requests ----

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StartGameRequest {
        @NotNull(message = "Board size is required")
        @Min(value = 8, message = "Minimum board size is 8")
        @Max(value = 16, message = "Maximum board size is 16")
        private Integer boardSize; // Must be 8 or 16
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ValidateMoveRequest {
        @NotNull private Integer boardSize;
        private int fromX;
        private int fromY;
        private int toX;
        private int toY;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SubmitSolutionRequest {
        @NotBlank(message = "Player name is required")
        @Size(min = 2, max = 50)
        private String playerName;

        @NotNull private Integer boardSize;
        private int startX;
        private int startY;

        @NotEmpty(message = "Move list cannot be empty")
        private List<int[]> moves;

        private Long timeTakenSeconds;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GetHintRequest {
        @NotNull private Integer boardSize;
        private int currentX;
        private int currentY;
        private String algorithm; // WARNSDORFF or BACKTRACKING
    }

    // ---- Responses ----

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StartGameResponse {
        private int boardSize;
        private int startX;
        private int startY;
        private String gameId;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ValidateMoveResponse {
        private boolean valid;
        private String message;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SolutionResponse {
        private Long id;
        private String playerName;
        private int boardSize;
        private int startX;
        private int startY;
        private String algorithmUsed;
        private List<int[]> moves;
        private LocalDateTime solvedAt;
        private Long timeTakenSeconds;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LeaderboardEntry {
        private Long playerId;
        private String playerName;
        private int boardSize;
        private Long timeTakenSeconds;
        private LocalDateTime solvedAt;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class HintResponse {
        private int suggestedX;
        private int suggestedY;
        private String algorithm;
        private List<int[]> fullSolution; // optional - show full path
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ApiError {
        private int status;
        private String error;
        private String message;
        private LocalDateTime timestamp;
    }
}
