package com.example.android60.chess.model;

import java.util.ArrayList;

/**
 * The knight chess piece class. For handling knight logic
 *
 * @author Andrew McKinney
 */
public class Knight extends ChessPiece {
    /**
     * basic constructor for this piece
     *
     * @param _color color of the piece
     */
    public Knight(String _color) {
        super(_color);
    }

    /**
     * basic constructor for this piece
     *
     * @param _color    color of the piece
     * @param _hasMoved whether or not the Knight has moved
     */
    public Knight(String _color, boolean _hasMoved) {
        super(_color, _hasMoved);

    }

    /**
     * returns the name of this piece
     *
     * @return the name of this piece
     */
    @Override
    public String getName() {
        // TODO Auto-generated method stub
        return super.getColor() + "N";
    }

    /**
     * returns an ArrayList of all valid moves for this piece
     *
     * @param startPosRow starting row
     * @param startPosCol starting Col
     * @param board       chessboard this piece is a part of
     * @return all valid moves
     */
    @Override
    public ArrayList<Move> getValidMoves(int startPosRow, int startPosCol, ChessBoard board) {
        ArrayList<Move> arr = new ArrayList<Move>();
        addBasicMoves(startPosRow, startPosCol, board, arr);
        return arr;
    }

    /**
     * adds all basic moves to the array
     *
     * @param startPosRow starting row
     * @param startPosCol starting Col
     * @param board       chessboard this piece is a part of
     * @param arr         array to add moves to
     */
    private void addBasicMoves(int startRow, int startCol, ChessBoard board, ArrayList<Move> arr) {

        addIfValidMove(startRow, startCol, startRow - 1, startCol + 2, board, arr);
        addIfValidMove(startRow, startCol, startRow - 1, startCol - 2, board, arr);
        addIfValidMove(startRow, startCol, startRow - 2, startCol + 1, board, arr);
        addIfValidMove(startRow, startCol, startRow - 2, startCol - 1, board, arr);
        addIfValidMove(startRow, startCol, startRow + 2, startCol + 1, board, arr);
        addIfValidMove(startRow, startCol, startRow + 2, startCol - 1, board, arr);
        addIfValidMove(startRow, startCol, startRow + 1, startCol + 2, board, arr);
        addIfValidMove(startRow, startCol, startRow + 1, startCol - 2, board, arr);
    }

    /**
     * adds a move to the array if it is valid
     *
     * @param startRow starting row
     * @param startCol starting col
     * @param endRow   ending row
     * @param endCol   ending col
     * @param board    board that the piece is on
     * @param arr      array to add moves to
     */
    private void addIfValidMove(int startRow,
                                int startCol,
                                int endRow,
                                int endCol,
                                ChessBoard board,
                                ArrayList<Move> arr) {
        if (ChessUtil.isValidPosition(endRow, endCol)) {
            ChessPiece otherPiece = board.getPiece(endRow, endCol);
            if (otherPiece == null) {
                //nothing is there, valid move
                arr.add(new Move(startRow, startCol, endRow, endCol));
                return;
            } else if (!(otherPiece.getColor().equals(super.getColor()))) {
                arr.add(new Move(startRow, startCol, endRow, endCol));
            }
        }
        //otherwise dont add
    }

    /**
     * returns a perfect copy of this piece
     *
     * @return a perfect copy
     */
    @Override
    ChessPiece copy() {
        return new Knight(super.getColor(), super.getHasMoved());
    }

}
