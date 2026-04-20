package com.knighttour.backend.algorithm;
import org.springframework.stereotype.Component;
import java.util.List;

/**
 * Validates individual knight moves and full tour submissions.
 */
@Component
public class MoveValidator {

    private static final int[] DX = {-2, -1, 1, 2, 2, 1, -1, -2};
    private static final int[] DY = {1, 2, 2, 1, -1, -2, -2, -1};

    /**
     * Returns true if moving from (x1,y1) to (x2,y2) is a valid knight move.
     */
    public boolean isValidKnightMove(int x1, int y1, int x2, int y2, int boardSize) {
        if (!isInBounds(x2, y2, boardSize)) return false;
        int dx = Math.abs(x2 - x1);
        int dy = Math.abs(y2 - y1);
        return (dx == 1 && dy == 2) || (dx == 2 && dy == 1);
    }

    /**
     * Validates a complete player-submitted tour:
     * - Must have exactly boardSize² moves
     * - Every move must be a valid knight move from the previous
     * - Every square must be visited exactly once
     */
    public ValidationResult validateTour(List<int[]> moves, int boardSize) {
        int totalSquares = boardSize * boardSize;

        if (moves == null || moves.isEmpty()) {
            return ValidationResult.fail("No moves provided.");
        }
        if (moves.size() != totalSquares) {
            return ValidationResult.fail(
                    "Tour must cover all " + totalSquares + " squares. Got: " + moves.size()
            );
        }

        boolean[][] visited = new boolean[boardSize][boardSize];

        for (int i = 0; i < moves.size(); i++) {
            int[] pos = moves.get(i);
            int x = pos[0], y = pos[1];

            if (!isInBounds(x, y, boardSize)) {
                return ValidationResult.fail(
                        "Move " + (i + 1) + " is out of bounds: (" + x + "," + y + ")"
                );
            }

            if (visited[y][x]) {
                return ValidationResult.fail(
                        "Square (" + x + "," + y + ") visited more than once at step " + (i + 1)
                );
            }

            if (i > 0) {
                int[] prev = moves.get(i - 1);
                if (!isValidKnightMove(prev[0], prev[1], x, y, boardSize)) {
                    return ValidationResult.fail(
                            "Invalid knight move from (" + prev[0] + "," + prev[1] +
                                    ") to (" + x + "," + y + ") at step " + (i + 1)
                    );
                }
            }

            visited[y][x] = true;
        }

        return ValidationResult.success();
    }

    private boolean isInBounds(int x, int y, int size) {
        return x >= 0 && x < size && y >= 0 && y < size;
    }

    // ---- Inner result class ----
    public static class ValidationResult {
        public final boolean valid;
        public final String message;

        private ValidationResult(boolean valid, String message) {
            this.valid = valid;
            this.message = message;
        }

        public static ValidationResult success() {
            return new ValidationResult(true, "Valid tour!");
        }

        public static ValidationResult fail(String reason) {
            return new ValidationResult(false, reason);
        }
    }
}

