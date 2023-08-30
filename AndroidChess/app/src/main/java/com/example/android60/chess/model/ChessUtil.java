package com.example.android60.chess.model;

/**
 * utility class containing some general use functions
 *
 * @author Andrew McKinney
 */
public class ChessUtil {
    /**
     * returns true if row and col and both in the range [0,7]
     *
     * @param row the row
     * @param col the col
     * @return true if row and col are both in the range [0,7]
     */
    public static boolean isValidPosition(int row, int col) {
        if (row >= 0 && row <= 7 && col >= 0 && col <= 7) {
            return true;
        } else {
            return false;
        }
    }

}
