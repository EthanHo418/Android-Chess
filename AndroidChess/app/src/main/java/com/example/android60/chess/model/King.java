package com.example.android60.chess.model;

import static com.example.android60.chess.model.ChessConstants.CASTLE_KING_SIDE;
import static com.example.android60.chess.model.ChessConstants.CASTLE_QUEEN_SIDE;
import static com.example.android60.chess.model.ChessConstants.COLOR_BLACK;
import static com.example.android60.chess.model.ChessConstants.COLOR_WHITE;
import static com.example.android60.chess.model.ChessConstants.EN_PASSANT;

import java.util.ArrayList;

/**
 * King chess Piece class for handling logic having to do with the king
 *
 * @author Andrew McKinney
 */
public class King extends ChessPiece {
    /**
     * basic constructor with color, has moved is initialized to false
     *
     * @param _color color of the King
     */
    public King(String _color) {
        super(_color);
        // TODO Auto-generated constructor stub
    }

    /**
     * basic constructor for the King
     *
     * @param _color    color of the King
     * @param _hasMoved whether or not the King has moved
     */
    public King(String _color, boolean _hasMoved) {
        super(_color, _hasMoved);
    }

    /**
     * Return the name of this piece
     *
     * @return the name of the piece
     */
    @Override
    public String getName() {
        // TODO Auto-generated method stub
        return super.getColor() + "K";
    }

    /**
     * gets all valid moves for the king
     *
     * @param startPosRow the row starting position
     * @param startPosCol the col part of the starting position
     * @param borad       the chessboard this king piece is a part of
     * @return An arraylist of all valid moves
     */
    @Override
    public ArrayList<Move> getValidMoves(int startPosRow, int startPosCol, ChessBoard board) {
        ArrayList<Move> arr = new ArrayList<Move>();
        addBasicMoves(startPosRow, startPosCol, board, arr);
        addCastleMoves(startPosRow, startPosCol, board, arr);
        return arr;
    }

    /**
     * gets all valid moves without the castling moves
     *
     * @param startPosRow the row starting position
     * @param startPosCol the col part of the starting position
     * @param board       the chessboard this king piece is a part of
     * @return An arraylist of all valid moves without castling moves
     */
    public ArrayList<Move> getValidMovesWithoutCastle(int startPosRow, int startPosCol, ChessBoard board) {
        ArrayList<Move> arr = new ArrayList<Move>();
        addBasicMoves(startPosRow, startPosCol, board, arr);
        //addCastleMoves(startPosRow, startPosCol, board, arr);
        return arr;
    }

    /**
     * adds the castling moves to the array
     *
     * @param startPosRow the row starting position
     * @param startPosCol the col part of the starting position
     * @param board       the chessboard the king piece is a part of
     * @param arr         the arrayList to add moves to
     */
    private void addCastleMoves(int startPosRow, int startPosCol, ChessBoard board, ArrayList<Move> arr) {
        ChessPiece king = board.getPiece(startPosRow, startPosCol);

        //the King has moved, we are done
        if (king.getHasMoved()) {
            return;
        }
        String opposingColor = "";
        if (super.getColor().equals(COLOR_BLACK)) {
            opposingColor = COLOR_WHITE;
        } else {
            opposingColor = COLOR_BLACK;
        }

        //first we try conditions for the king side move
        ChessPiece rook = board.getPiece(startPosRow, 7);

        //Also checks to see if the king is in check
        boolean intermediateTilesUnderAttack = board.isTileUnderAttackByColor(startPosRow, startPosCol, opposingColor) ||
                board.isTileUnderAttackByColor(startPosRow, startPosCol + 1, opposingColor) ||
                board.isTileUnderAttackByColor(startPosRow, startPosCol + 2, opposingColor);

        boolean intermediateSpacesEmpty = (board.getPiece(startPosRow, startPosCol + 1) == null) &&
                (board.getPiece(startPosRow, startPosCol + 2) == null);

        if (rook != null && !rook.getHasMoved() && !intermediateTilesUnderAttack && intermediateSpacesEmpty) {
            arr.add(new SpecialMove(startPosRow, startPosCol, startPosRow, startPosCol + 2, CASTLE_KING_SIDE));
        }

        //now we try the conditions for the queen side move
        rook = board.getPiece(startPosRow, 0);

        intermediateTilesUnderAttack = board.isTileUnderAttackByColor(startPosRow, startPosCol, opposingColor) ||
                board.isTileUnderAttackByColor(startPosRow, startPosCol - 1, opposingColor) ||
                board.isTileUnderAttackByColor(startPosRow, startPosCol - 2, opposingColor);

        intermediateSpacesEmpty = (board.getPiece(startPosRow, startPosCol - 1) == null) &&
                (board.getPiece(startPosRow, startPosCol - 2) == null) &&
                (board.getPiece(startPosRow, startPosCol - 2) == null);
        if (rook != null && !rook.getHasMoved() && !intermediateTilesUnderAttack && intermediateSpacesEmpty) {
            arr.add(new SpecialMove(startPosRow, startPosCol, startPosRow, startPosCol - 2, CASTLE_QUEEN_SIDE));
        }
    }

    /**
     * adds the basic  moves to the array
     *
     * @param startPosRow the row starting position
     * @param startPosCol the col part of the starting position
     * @param board       the chessboard the king piece is a part of
     * @param arr         the arrayList to add moves to
     */
    private void addBasicMoves(int startRow, int startCol, ChessBoard board, ArrayList<Move> arr) {
        //3 tiles above
        addIfValidMove(startRow, startCol, startRow - 1, startCol + 1, board, arr);
        addIfValidMove(startRow, startCol, startRow - 1, startCol, board, arr);
        addIfValidMove(startRow, startCol, startRow - 1, startCol - 1, board, arr);
        //2 tiles to the left/right
        addIfValidMove(startRow, startCol, startRow, startCol + 1, board, arr);
        addIfValidMove(startRow, startCol, startRow, startCol - 1, board, arr);
        //3 tiles below
        addIfValidMove(startRow, startCol, startRow + 1, startCol + 1, board, arr);
        addIfValidMove(startRow, startCol, startRow + 1, startCol, board, arr);
        addIfValidMove(startRow, startCol, startRow + 1, startCol - 1, board, arr);

    }

    /**
     * adds a move if it is valid
     *
     * @param startRow starting row
     * @param startCol starting col
     * @param endRow   ending row
     * @param endCol   ending Col
     * @param board    the chessboard this piece is a part of
     * @param arr      arrayList to add the move to
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
     * @return a copy of this piece
     */
    @Override
    ChessPiece copy() {
        return new King(super.getColor(), super.getHasMoved());
    }

}
