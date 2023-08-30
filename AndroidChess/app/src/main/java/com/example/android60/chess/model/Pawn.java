package com.example.android60.chess.model;

import static com.example.android60.chess.model.ChessConstants.CASTLE_KING_SIDE;
import static com.example.android60.chess.model.ChessConstants.CASTLE_QUEEN_SIDE;
import static com.example.android60.chess.model.ChessConstants.COLOR_BLACK;
import static com.example.android60.chess.model.ChessConstants.COLOR_WHITE;
import static com.example.android60.chess.model.ChessConstants.EN_PASSANT;

import java.util.ArrayList;

/**
 * pawn ChessPiece class. Contains logic for handling pawns
 *
 * @author Andrew McKinney
 */
public class Pawn extends ChessPiece {

    /**
     * turn the pawn did enPassont, if applicaple
     */
    private int turnDidTwoSpaceMove = -1;

    /**
     * basic constructor for Pawn
     *
     * @param _color color of piece
     */
    public Pawn(String _color) {
        super(_color);
        // TODO Auto-generated constructor stub
    }

    /**
     * basic constructor for Pawn
     *
     * @param _color               color
     * @param _hasMoved            hasMoved
     * @param _turnDidTwoSpaceMove turnDidTwoSpaceMove
     */
    public Pawn(String _color, boolean _hasMoved, int _turnDidTwoSpaceMove) {
        super(_color, _hasMoved);
        turnDidTwoSpaceMove = _turnDidTwoSpaceMove;
    }

    /**
     * gets the name of this Pawn
     *
     * @return name of pawn
     */
    @Override
    public String getName() {
        // TODO Auto-generated method stub
        return super.getColor() + "p";
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
    public ArrayList<Move> getValidMoves(int startRow, int startCol, ChessBoard board) {
        ArrayList<Move> arr = new ArrayList<Move>();
        addBasicMoves(arr, startRow, startCol, board); //tested and works
        addDiagonalMoves(arr, startRow, startCol, board); //tested and works
        addEnPassontMoves(arr, startRow, startCol, board); //tested and works
        //TODO: code EnPassont
        return arr;
    }

    /**
     * Adds Diagonal moves for to arr
     *
     * @param arr         array to add moves to
     * @param startPosRow starting row
     * @param startPosCol starting Col
     * @param board       chessboard this piece is a part of
     */
    private void addDiagonalMoves(ArrayList<Move> arr, int startRow, int startCol, ChessBoard board) {
        if (super.getColor().equals(COLOR_WHITE)) {
            ChessPiece diagonalLeft = board.getPiece(startRow - 1, startCol - 1);
            if (diagonalLeft != null && diagonalLeft.getColor().equals(COLOR_BLACK)) {
                arr.add(new Move(startRow, startCol, startRow - 1, startCol - 1));
            }
            ChessPiece diagonalRight = board.getPiece(startRow - 1, startCol + 1);
            if (diagonalRight != null && diagonalRight.getColor().equals(COLOR_BLACK)) {
                arr.add(new Move(startRow, startCol, startRow - 1, startCol + 1));
            }
        } else {
            ChessPiece diagonalLeft = board.getPiece(startRow + 1, startCol - 1);
            if (diagonalLeft != null && diagonalLeft.getColor().equals(COLOR_WHITE)) {
                arr.add(new Move(startRow, startCol, startRow + 1, startCol - 1));
            }
            ChessPiece diagonalRight = board.getPiece(startRow + 1, startCol + 1);
            if (diagonalRight != null && diagonalRight.getColor().equals(COLOR_WHITE)) {
                arr.add(new Move(startRow, startCol, startRow + 1, startCol + 1));
            }
        }

    }

    /**
     * adds enPassont moves to arr
     *
     * @param arr         array to add moves to
     * @param startPosRow starting row
     * @param startPosCol starting Col
     * @param board       chessboard this piece is a part of
     */
    private void addEnPassontMoves(ArrayList<Move> arr, int startRow, int startCol, ChessBoard board) {
        if (super.getColor().equals(COLOR_WHITE)) {
            //first lets get our neighbors
            ChessPiece leftNeighbor = board.getPiece(startRow, startCol - 1);
            if (checkEnPassontConditionsOnNeighbor(leftNeighbor, board)) {
                SpecialMove specialMove = new SpecialMove(startRow,
                        startCol,
                        startRow - 1,
                        startCol - 1,
                        EN_PASSANT);
                arr.add(specialMove);
            }
            ChessPiece rightNeighbor = board.getPiece(startRow, startCol + 1);
            if (checkEnPassontConditionsOnNeighbor(rightNeighbor, board)) {
                SpecialMove specialMove = new SpecialMove(startRow,
                        startCol,
                        startRow - 1,
                        startCol + 1,
                        EN_PASSANT);
                arr.add(specialMove);
            }
        } else {
            ChessPiece leftNeighbor = board.getPiece(startRow, startCol - 1);
            if (checkEnPassontConditionsOnNeighbor(leftNeighbor, board)) {
                SpecialMove specialMove = new SpecialMove(startRow,
                        startCol,
                        startRow + 1,
                        startCol - 1,
                        EN_PASSANT);
                arr.add(specialMove);
            }
            ChessPiece rightNeighbor = board.getPiece(startRow, startCol + 1);
            if (checkEnPassontConditionsOnNeighbor(rightNeighbor, board)) {
                SpecialMove specialMove = new SpecialMove(startRow,
                        startCol,
                        startRow + 1,
                        startCol + 1,
                        EN_PASSANT);
                arr.add(specialMove);
            }
        }
    }

    /**
     * checks to see if Neighbor matches condidions for enPassont
     *
     * @param neighbor chesspiece to the left or right of this piece
     * @param board    board that this piece is a part of
     * @return true if enpassont conditions are met
     */
    private boolean checkEnPassontConditionsOnNeighbor(ChessPiece neighbor, ChessBoard board) {
        if (neighbor != null && neighbor instanceof Pawn) {
            Pawn neighborPawn = (Pawn) neighbor;
            return neighborPawn.turnDidTwoSpaceMove == board.turnNumber - 1 &&
                    !(neighborPawn.getColor().equals(super.getColor()));
        } else {
            return false;
        }
    }

    /**
     * adds basic moves to move arr
     *
     * @param arr         array to add moves to
     * @param startPosRow starting row
     * @param startPosCol starting Col
     * @param board       chessboard this piece is a part of
     */
    private void addBasicMoves(ArrayList<Move> arr, int startRow, int startCol, ChessBoard board) {
        if (super.getColor().equals(COLOR_WHITE)) {
            //white is always on the bottom,so we are trying to go up
            if (!super.getHasMoved()) {
                //Can only move on to empty tiles
                if (addIfEmptyAndValidSpace(startRow, startCol, startRow - 1, startCol, arr, board))
                    addIfEmptyAndValidSpace(startRow, startCol, startRow - 2, startCol, arr, board);
            } else {
                addIfEmptyAndValidSpace(startRow, startCol, startRow - 1, startCol, arr, board);
            }
        } else {
            //Color must be black
            if (!super.getHasMoved()) {
                //Can only move on to empty tiles
                if (addIfEmptyAndValidSpace(startRow, startCol, startRow + 1, startCol, arr, board))
                    addIfEmptyAndValidSpace(startRow, startCol, startRow + 2, startCol, arr, board);
            } else {
                addIfEmptyAndValidSpace(startRow, startCol, startRow + 1, startCol, arr, board);
            }
        }
    }

    /**
     * adds Move to arr if empty and valid move
     *
     * @param startRow start row
     * @param startCol start col
     * @param endRow   end row
     * @param endCol   end col
     * @param arr      ArrayList
     * @param board    the board this piece is a part of
     * @return true if empty and valid
     */
    private boolean addIfEmptyAndValidSpace(int startRow,
                                            int startCol,
                                            int endRow,
                                            int endCol,
                                            ArrayList<Move> arr,
                                            ChessBoard board) {
        if (ChessUtil.isValidPosition(endRow, endCol) && board.getPiece(endRow, endCol) == null) {
            arr.add(new Move(startRow, startCol, endRow, endCol));
            return true;
        } else {
            return false;
        }


    }

    /**
     * notifies the pawn that it was moved
     *
     * @param move       move that occured
     * @param turnNumber the turnNumber the move occured on
     */
    @Override
    public void wasMoved(Move move, int turnNumber) {
        super.wasMoved(move, turnNumber);
        if (move.getDistance() == 2) {
            turnDidTwoSpaceMove = turnNumber;
        }
    }

    /**
     * returns a perfect copy of this Pawn
     *
     * @return perfect copy
     */
    @Override
    ChessPiece copy() {
        // TODO Auto-generated method stub
        return new Pawn(super.getColor(), super.getHasMoved(), turnDidTwoSpaceMove);
    }

}
