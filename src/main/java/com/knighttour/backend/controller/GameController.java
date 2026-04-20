package com.knighttour.backend.controller;

import com.knighttour.backend.model.Dto;
import com.knighttour.model.Dto;
import com.knighttour.service.GameService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/game")
@RequiredArgsConstructor
@Slf4j
public class GameController {

    private final GameService gameService;

    /**
     * POST /api/game/start
     * Start a new game. Returns board size and random start position.
     */
    @PostMapping("/start")
    public ResponseEntity<Dto.StartGameResponse> startGame(
            @Valid @RequestBody Dto.StartGameRequest request) {
        return ResponseEntity.ok(gameService.startGame(request.getBoardSize()));
    }

    /**
     * POST /api/game/validate-move
     * Validates whether a single move is a legal knight move.
     */
    @PostMapping("/validate-move")
    public ResponseEntity<Dto.ValidateMoveResponse> validateMove(
            @Valid @RequestBody Dto.ValidateMoveRequest request) {
        return ResponseEntity.ok(gameService.validateMove(request));
    }

    /**
     * POST /api/game/submit
     * Submit a complete tour. Validates and saves to DB on success.
     */
    @PostMapping("/submit")
    public ResponseEntity<Dto.SolutionResponse> submitSolution(
            @Valid @RequestBody Dto.SubmitSolutionRequest request) {
        return ResponseEntity.ok(gameService.submitSolution(request));
    }

    /**
     * POST /api/game/hint
     * Get a hint: next best move or full solution path using chosen algorithm.
     */
    @PostMapping("/hint")
    public ResponseEntity<Dto.HintResponse> getHint(
            @Valid @RequestBody Dto.GetHintRequest request) {
        return ResponseEntity.ok(gameService.getHint(request));
    }

    /**
     * GET /api/game/solve?boardSize=8&startX=0&startY=0
     * Runs both algorithms and returns both solution paths.
     */
    @GetMapping("/solve")
    public ResponseEntity<Map<String, List<int[]>>> solve(
            @RequestParam int boardSize,
            @RequestParam int startX,
            @RequestParam int startY) {
        return ResponseEntity.ok(gameService.solveBothAlgorithms(boardSize, startX, startY));
    }

    /**
     * GET /api/game/leaderboard?boardSize=8
     * Returns top 20 fastest solutions for the given board size.
     */
    @GetMapping("/leaderboard")
    public ResponseEntity<List<Dto.LeaderboardEntry>> leaderboard(
            @RequestParam(defaultValue = "8") int boardSize) {
        return ResponseEntity.ok(gameService.getLeaderboard(boardSize));
    }

    /**
     * GET /api/game/player/{playerId}/solutions
     * Returns all solutions submitted by a given player.
     */
    @GetMapping("/player/{playerId}/solutions")
    public ResponseEntity<List<Dto.SolutionResponse>> playerSolutions(
            @PathVariable Long playerId) {
        return ResponseEntity.ok(gameService.getPlayerSolutions(playerId));
    }
}
