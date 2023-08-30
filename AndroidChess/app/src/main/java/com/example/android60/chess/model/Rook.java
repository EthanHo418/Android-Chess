package com.example.android60.chess.model;

import java.util.ArrayList;

public class Rook extends ChessPiece {
    /*
     * contructor for rook
     * @param String color: color of Piece
     */
    public Rook(String _color) {
        super(_color);
    }

    public Rook(String _color, boolean _hasMoved) {
        super(_color, _hasMoved);
    }

    @Override
    public ChessPiece copy() {
        // TODO Auto-generated method stub
        return new Rook(super.getColor(), super.getHasMoved());
    }

    @Override
    public String getName() {
        //TODO: determine name of the rook, probably R or something
        return super.getColor() + "R";
    }

    @Override
    //TODO: flesh this out and move a Rook around!
    public ArrayList<Move> getValidMoves(int startPosRow, int startPosCol, ChessBoard board) {
        ArrayList<Move> arr = new ArrayList<Move>();
        addBasicMoves(startPosRow, startPosCol, board, arr);
        // TODO ADD CASTLEING
        return arr;
    }

    private void addBasicMoves(int startRow, int startCol, ChessBoard board, ArrayList<Move> arr) {

        int i;

        //down

        for (i = startRow; i < 7; i++) {
            if (!addIfValidMove(startRow, startCol, i + 1, startCol, board, arr)) {
                break;
            }
        }


        //up
        for (i = startRow; i >= 1; i--) {
            if (!addIfValidMove(startRow, startCol, i - 1, startCol, board, arr)) {
                break;
            }
        }


        //right
        for (i = startCol; 7 - i > 0; i++) {
            if (!addIfValidMove(startRow, startCol, startRow, i + 1, board, arr)) {
                break;
            }
        }


        //left
        for (i = startCol; i >= 1; i--) {
            if (!addIfValidMove(startRow, startCol, startRow, i - 1, board, arr)) {
                break;
            }
        }

    }


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

}
