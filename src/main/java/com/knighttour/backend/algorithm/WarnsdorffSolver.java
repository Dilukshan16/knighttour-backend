package com.knighttour.backend.algorithm;

import org.springframework.stereotype.Component;
import java.util.*;

/**
 * Warnsdorff's Rule Algorithm for Knight's Tour.
 *
 * Heuristic: At each step, move the knight to the square
 * from which the knight has the FEWEST onward moves.
 * This greedy heuristic solves most boards in O(n²) time.
 */
@Component
public class WarnsdorffSolver {

    // All 8 possible knight moves (dx, dy)
    private static final int[] DX = {-2, -1, 1, 2, 2, 1, -1, -2};
    private static final int[] DY = {1, 2, 2, 1, -1, -2, -2, -1};

    /**
     * Solves the Knight's Tour using Warnsdorff's heuristic.
     *
     * @param boardSize size of the board (8 or 16)
     * @param startX    starting column (0-indexed)
     * @param startY    starting row    (0-indexed)
     * @return ordered list of [x, y] positions, or empty if no solution found
     */
    public List<int[]> solve(int boardSize, int startX, int startY) {
        validateInput(boardSize, startX, startY);

        int[][] board = new int[boardSize][boardSize];
        for (int[] row : board) Arrays.fill(row, -1);

        List<int[]> path = new ArrayList<>();
        int x = startX;
        int y = startY;
        board[y][x] = 0;
        path.add(new int[]{x, y});

        int totalSquares = boardSize * boardSize;

        for (int step = 1; step < totalSquares; step++) {
            int[] next = warnsdorffNext(board, x, y, boardSize);
            if (next == null) {
                // No valid next move — heuristic failed (rare on valid boards)
                return Collections.emptyList();
            }
            x = next[0];
            y = next[1];
            board[y][x] = step;
            path.add(new int[]{x, y});
        }

        return path;
    }

    /**
     * Picks the next cell by Warnsdorff's rule:
     * choose the neighbor with the minimum degree (fewest onward moves).
     * Ties are broken by a secondary heuristic (closest to center).
     */
    private int[] warnsdorffNext(int[][] board, int x, int y, int size) {
        int minDegree = Integer.MAX_VALUE;
        int[] best = null;

        // Shuffle offsets to break ties randomly (gives different valid tours)
        List<Integer> indices = new ArrayList<>();
        for (int i = 0; i < 8; i++) indices.add(i);
        Collections.shuffle(indices);

        for (int i : indices) {
            int nx = x + DX[i];
            int ny = y + DY[i];
            if (isValid(nx, ny, size) && board[ny][nx] == -1) {
                int degree = getDegree(board, nx, ny, size);
                if (degree < minDegree) {
                    minDegree = degree;
                    best = new int[]{nx, ny};
                }
            }
        }
        return best;
    }

    /**
     * Counts the number of unvisited neighbors from position (x, y).
     * This is the "degree" in Warnsdorff's heuristic.
     */
    private int getDegree(int[][] board, int x, int y, int size) {
        int count = 0;
        for (int i = 0; i < 8; i++) {
            int nx = x + DX[i];
            int ny = y + DY[i];
            if (isValid(nx, ny, size) && board[ny][nx] == -1) {
                count++;
            }
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
                    "Start position (" + startX + "," + startY + ") is out of bounds for board size " + boardSize
            );
        }
    }
}
