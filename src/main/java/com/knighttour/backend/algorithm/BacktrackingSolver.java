package com.knighttour.backend.algorithm;

import org.springframework.stereotype.Component;
import java.util.*;

/**
 * Backtracking Algorithm for Knight's Tour.
 *
 * Exhaustive DFS with pruning:
 * - Tries all 8 knight moves recursively.
 * - Backtracks when no valid move is available.
 * - Uses Warnsdorff ordering to prune the search (ordered backtracking),
 *   dramatically reducing time on larger boards.
 *
 * Pure backtracking (no ordering) is feasible for 8×8 but too slow for 16×16.
 * With Warnsdorff ordering it typically finds a solution on the first branch.
 */
@Component
public class BacktrackingSolver {

    private static final int[] DX = {-2, -1, 1, 2, 2, 1, -1, -2};
    private static final int[] DY = {1, 2, 2, 1, -1, -2, -2, -1};

    // Max recursion depth guard for 16x16 (256 moves)
    private static final int MAX_DEPTH = 256;

    /**
     * Solves using ordered backtracking (Warnsdorff move ordering + backtrack).
     *
     * @param boardSize 8 or 16
     * @param startX    starting column
     * @param startY    starting row
     * @return ordered path of [x,y] moves, or empty list if unsolvable
     */
    public List<int[]> solve(int boardSize, int startX, int startY) {
        validateInput(boardSize, startX, startY);

        int[][] board = new int[boardSize][boardSize];
        for (int[] row : board) Arrays.fill(row, -1);

        List<int[]> path = new ArrayList<>();
        board[startY][startX] = 0;
        path.add(new int[]{startX, startY});

        boolean found = backtrack(board, startX, startY, 1, boardSize, path);
        return found ? path : Collections.emptyList();
    }

    private boolean backtrack(int[][] board, int x, int y, int moveNum,
                              int size, List<int[]> path) {
        if (moveNum == size * size) return true; // All squares visited
        if (moveNum > MAX_DEPTH) return false;

        // Get candidate moves sorted by Warnsdorff degree (fewest onward moves first)
        List<int[]> candidates = getSortedMoves(board, x, y, size);

        for (int[] move : candidates) {
            int nx = move[0];
            int ny = move[1];

            board[ny][nx] = moveNum;
            path.add(new int[]{nx, ny});

            if (backtrack(board, nx, ny, moveNum + 1, size, path)) {
                return true;
            }

            // Backtrack
            board[ny][nx] = -1;
            path.remove(path.size() - 1);
        }

        return false;
    }

    /**
     * Returns valid neighbor moves sorted by their degree (Warnsdorff ordering).
     * This is the key optimization that makes backtracking tractable.
     */
    private List<int[]> getSortedMoves(int[][] board, int x, int y, int size) {
        List<int[]> moves = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            int nx = x + DX[i];
            int ny = y + DY[i];
            if (isValid(nx, ny, size) && board[ny][nx] == -1) {
                int degree = getDegree(board, nx, ny, size);
                moves.add(new int[]{nx, ny, degree});
            }
        }
        // Sort ascending by degree (Warnsdorff rule)
        moves.sort(Comparator.comparingInt(m -> m[2]));
        return moves;
    }

    private int getDegree(int[][] board, int x, int y, int size) {
        int count = 0;
        for (int i = 0; i < 8; i++) {
            int nx = x + DX[i];
            int ny = y + DY[i];
            if (isValid(nx, ny, size) && board[ny][nx] == -1) count++;
        }
        return count;
    }

    private boolean isValid(int x, int y, int size) {
        return x >= 0 && x < size && y >= 0 && y < size;
    }

    private void validateInput(int boardSize, int startX, int startY) {
        if (boardSize != 8 && boardSize != 16) {
            throw new IllegalArgumentException("Board size must be 8 or 16");
        }
        if (startX < 0 || startX >= boardSize || startY < 0 || startY >= boardSize) {
            throw new IllegalArgumentException(
                    "Start position (" + startX + "," + startY + ") out of bounds"
            );
        }
    }
}

