package com.example.android60.chess.model;

/**
 * Represents a move for a chess game
 *
 * @author Andrew McKinney
 */
public class Move {
    /**
     * starting row for the Move
     */
    public int startRow;
    /**
     * starting col for the Move
     */
    public int startCol;
    /**
     * starting rank for the move
     */
    private char startRank;
    /**
     * starting file for the move
     */
    private char startFile;

    /**
     * ending row for the move
     */
    public int endRow;
    /**
     * ending col for the move
     */
    public int endCol;
    /**
     * ending rank for the move
     */
    private char endRank;
    /**
     * ending file for the move
     */
    private char endFile;

    /**
     * constructor for a move from rankFile
     *
     * @param rankFileStart start in rank file
     * @param rankFileEnd   end in rank file
     */
    public Move(String rankFileStart, String rankFileEnd) {
        startRank = rankFileStart.charAt(1);
        startFile = rankFileStart.charAt(0);
        startRow = RankToRow(startRank);
        startCol = FileToCol(startFile);

        endRank = rankFileEnd.charAt(1);
        endFile = rankFileEnd.charAt(0);
        endRow = RankToRow(endRank);
        endCol = FileToCol(endFile);
    }

    /**
     * constructor for a move using coordinates
     *
     * @param _startRow starting row
     * @param _startCol starting col
     * @param _endRow   ending row
     * @param _endCol   ending col
     */
    public Move(int _startRow, int _startCol, int _endRow, int _endCol) {
        startRow = _startRow;
        startCol = _startCol;
        startRank = RowToRank(startRow);
        startFile = ColToFile(startCol);

        endRow = _endRow;
        endCol = _endCol;
        endRank = RowToRank(endRow);
        endFile = ColToFile(endCol);
    }

    /**
     * gets starting position in rankFile
     *
     * @return starting position in rankFile
     */
    public String getRankFileStart() {
        char[] temp = {startRank, startFile};
        return new String(temp);
    }

    /**
     * gets ending position in rankFile
     *
     * @return ending position in rankFile
     */
    public String getRankFileEnd() {
        char[] temp = {endRank, endFile};
        return new String(temp);
    }

    /**
     * checks to see if another object is equal to this Move
     *
     * @return true if other object is equal to this object
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj.getClass() == Move.class || obj.getClass() == SpecialMove.class)) {
            return false;
        }
        Move otherMove = (Move) obj;
        if (this.startRow == otherMove.startRow &&
                this.startCol == otherMove.startCol &&
                this.endRow == otherMove.endRow &&
                this.endCol == otherMove.endCol) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * converts from rank to row
     *
     * @param rank rank
     * @return row
     */
    private static int RankToRow(char rank) {
        return 8 - Character.getNumericValue(rank);
    }

    /**
     * converts from file to col
     *
     * @param file file
     * @return col
     */
    private static int FileToCol(char file) {
        return ((int) file) - 97;
    }

    /**
     * converts from col to file
     *
     * @param col
     * @return file
     */
    private static char ColToFile(int y) {
        return (char) (y + 97);
    }

    /**
     * converts from row to rank
     *
     * @param row
     * @return rank
     */
    private static char RowToRank(int x) {
        return (char) (8 - x + 48);
    }

    /**
     * gets  total distanced covered by this moves, in tiles
     *
     * @return total distance covered
     */
    public double getDistance() {
        int rowDiff = startRow - endRow;
        int colDiff = startCol - endCol;
        return Math.sqrt(rowDiff * rowDiff + colDiff * colDiff);
    }

    /**
     * converts to the move to a string
     *
     * @return a string representing the move
     */
    @Override
    public String toString() {
        return "Move from: " + startRow + "," + startCol + " to " + endRow + "," + endCol;
    }
}
