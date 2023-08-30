package com.example.android60.chess.model;

import java.util.ArrayList;

/**
 * a debug piece used in testing
 *
 * @author Andrew McKinney
 */
public class DebugPiece extends ChessPiece {
    /**
     * creates a new debug piece of color
     *
     * @param _color the color of the piece
     */
    public DebugPiece(String _color) {
        super(_color);
    }

    /**
     * always returns "ZZ"
     *
     * @return the string "ZZ"
     */
    @Override
    public String getName() {
        return "ZZ";
    }

    /**
     * Gets all valid moves for a debug piece
     *
     * @return null
     */
    @Override
    public ArrayList<Move> getValidMoves(int startPosRow, int startPosCol, ChessBoard board) {
        return null;
    }

    /**
     * copy will return null for a debug piece
     *
     * @return null
     */
    @Override
    ChessPiece copy() {
        return null;
    }

}
