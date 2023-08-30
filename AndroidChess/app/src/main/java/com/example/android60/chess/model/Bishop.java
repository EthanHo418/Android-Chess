package com.example.android60.chess.model;

import java.util.ArrayList;

/**
 * Bishop class that handles movement for a bishop
 *
 * @author Andrew McKinney
 */
public class Bishop extends ChessPiece {
    /**
     * constructor for a bishop, hasMoved is false by default
     *
     * @param _color the color of the chesspiece
     */
    public Bishop(String _color) {
        super(_color);
        // TODO Auto-generated constructor stub
    }

    /**
     * constructor for a bishop
     *
     * @param _color    the color of the chesspiece
     * @param _hasMoved whether or not this piece has moved
     */
    public Bishop(String _color, boolean _hasMoved) {
        super(_color, _hasMoved);
    }

    /**
     * gets the name of this chess piece
     *
     * @return the name of this chess piece
     */
    @Override
    public String getName() {
        // TODO Auto-generated method stub
        return super.getColor() + "B";
    }

    /**
     * returns an array of all valid moves based on a starting point and chessboard
     *
     * @param startPosRow start row of this piece
     * @param startPosCol start col of this piece
     * @param board       chessboard that this piece is on
     * @return array of all valid moves
     */
    @Override
    public ArrayList<Move> getValidMoves(int startPosRow, int startPosCol, ChessBoard board) {
        ArrayList<Move> arr = new ArrayList<Move>();
        addBasicMoves(startPosRow, startPosCol, board, arr);
        return arr;
    }

    /**
     * adds all basic moves to the array passed as an arguement
     *
     * @param startRow the start row of this piece
     * @param startCol the start col of this piece
     * @param board    chessboard that this piece is on
     * @param arr      array that will be added to
     */
    private void addBasicMoves(int startRow, int startCol, ChessBoard board, ArrayList<Move> arr) {
        //up right
        for (int i = startRow, j = startCol; i > -20 && j < 20; i--, j++) {
            if (!addIfValidMove(startRow, startCol, i - 1, j + 1, board, arr)) {
                break;
            }
        }


        for (int i = startRow, j = startCol; i < 20 && j < 20; i--, j--) {
            if (!addIfValidMove(startRow, startCol, i - 1, j - 1, board, arr)) {
                break;
            }
        }


        for (int i = startRow, j = startCol; i > -20 && j > -20; i++, j++) {
            if (!addIfValidMove(startRow, startCol, i + 1, j + 1, board, arr)) {
                break;
            }
        }


        for (int i = startRow, j = startCol; i < 20 && j < 20; i++, j--) {
            if (!addIfValidMove(startRow, startCol, i + 1, j - 1, board, arr)) {
                break;
            }
        }

    }

    /**
     * adds a move if it is valid
     *
     * @param startRow start row
     * @param startCol start col
     * @param endRow   end row
     * @param endCol   end col
     * @param board    the chessboard this move occurs on
     * @param arr      array to add move to
     * @return if the move was valid
     */
    private boolean addIfValidMove(int startRow, int startCol,
                                   int endRow, int endCol,
                                   ChessBoard board, ArrayList<Move> arr) {


        if (ChessUtil.isValidPosition(endRow, endCol)) {
            ChessPiece otherPiece = board.getPiece(endRow, endCol);
            if (otherPiece == null) {
                //nothing is there, valid move
                arr.add(new Move(startRow, startCol, endRow, endCol));
                return true;
                //we are different colors
            } else if (!(otherPiece.getColor().equals(super.getColor()))) {

                arr.add(new Move(startRow, startCol, endRow, endCol));
                //to communicate to stop loop
                return false;
            } else {
                ///The other piece is the same color
                //we cant move there, stop loop
                return false;
            }
        } else {
            //index out of bounds return false
            //to communicate to stop loop
            return false;
        }

    }

    /**
     * Returns a copy of this ChessPiece
     *
     * @return exact copy of this ChessPiece
     */
    @Override
    public ChessPiece copy() {
        return new Bishop(super.getColor(), super.getHasMoved());
    }

}
