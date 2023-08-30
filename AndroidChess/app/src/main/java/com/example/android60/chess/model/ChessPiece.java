package com.example.android60.chess.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * basic abstract class that all chess pieces inherit from
 *
 * @author Ethan Ho
 */
public abstract class ChessPiece implements Serializable {
    /**
     * whether or not this piece has moved
     */
    private boolean hasMoved = false;
    /**
     * color of this piece
     */
    public final String color;

    /**
     * basic constructor for a chess piece
     *
     * @param _color the color of the chess piece
     */
    public ChessPiece(String _color) {
        color = _color;
    }

    /**
     * basic constuctor for a chess piece
     *
     * @param _color    the color of the chess piece
     * @param _hasMoved whether or not this piece has moved
     */
    public ChessPiece(String _color, boolean _hasMoved) {
        color = _color;
        hasMoved = _hasMoved;
    }

    /**
     * returns whether or not this piece has moved
     *
     * @return whether or not this piece has moved
     */
    public boolean getHasMoved() {
        return hasMoved;
    }

    /**
     * gets the name of a piece
     *
     * @return the name of the piece
     */
    public abstract String getName();

    /**
     * returns the color of this piece
     *
     * @return the color of the piece
     */
    public String getColor() {
        return color;
    }

    /**
     * returns an arrayList of all valid moves
     *
     * @param startPosRow starting row
     * @param startPosCol starting col
     * @param board       chessBoard that the piece is on
     * @return ArrayList of all valid moves
     */
    public abstract ArrayList<Move> getValidMoves(int startPosRow, int startPosCol, ChessBoard board);

    /**
     * communicates to this piece that it was moved
     *
     * @param move       the move that was executed
     * @param turnNumber the turn number the move was executed on
     */
    public void wasMoved(Move move, int turnNumber) {
        hasMoved = true;
    }

    /**
     * creates an exact copy of this
     *
     * @return an exact copy of this object
     */
    abstract ChessPiece copy();

}
