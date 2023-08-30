package com.example.android60.chess.model;

import static com.example.android60.chess.model.ChessConstants.CASTLE_KING_SIDE;
import static com.example.android60.chess.model.ChessConstants.CASTLE_QUEEN_SIDE;
import static com.example.android60.chess.model.ChessConstants.COLOR_BLACK;
import static com.example.android60.chess.model.ChessConstants.COLOR_WHITE;
import static com.example.android60.chess.model.ChessConstants.EN_PASSANT;

import android.view.WindowManager;

import com.example.android60.R;

import java.io.Serializable;
import java.util.ArrayList;

public final class ChessBoard implements Serializable {
    private ChessPiece[][] board;
    public final int turnNumber;

    public static ChessBoard fromString(String input) {
        if(input.contains("\n")) {
            input = input.replaceAll("\n", "");
        }
        String[] inputArr = input.split(" ");
        ChessPiece[][] tempBoard = new ChessPiece[8][8];
        for(int r = 0; r < 8; r++) {
            for(int c = 0; c < 8; c++){
                int oneDIndex = (r*8) + c;
                String nextPiece = inputArr[oneDIndex];
                tempBoard[r][c] = getPieceFromString(nextPiece);
            }
        }
        return new ChessBoard(tempBoard);
    }

    private static ChessPiece getPieceFromString(String input) {
        switch (input) {
            case "whitep": return new Pawn(COLOR_WHITE);
            case "whiteK": return new King(COLOR_WHITE);
            case "whiteQ": return new Queen(COLOR_WHITE);
            case "whiteB": return new Bishop(COLOR_WHITE);
            case "whiteN": return new Knight(COLOR_WHITE);
            case "whiteR": return new Rook(COLOR_WHITE);

            case "blackp": return new Pawn(COLOR_BLACK);
            case "blackK": return new King(COLOR_BLACK);
            case "blackQ": return new Queen(COLOR_BLACK);
            case "blackB": return new Bishop(COLOR_BLACK);
            case "blackN": return new Knight(COLOR_BLACK);
            case "blackR": return new Rook(COLOR_BLACK);
            case "X": return null;

            default: return new Rook(COLOR_BLACK);
        }
    }

    public ChessBoard() {
        ChessPiece[][] tmpBoard = new ChessPiece[8][8];
        initBlackSide(tmpBoard);
        initWhiteSide(tmpBoard);
        board = tmpBoard;
        turnNumber = 1;
    }

    public ChessBoard(ChessPiece[][] tmpBoard) {
        board = tmpBoard;
        turnNumber = 1;
    }

    public String exportToString() {
        String rtn = "";
        for(int r = 0; r < 8; r++) {
            for(int c = 0; c < 8; c++){
                ChessPiece chessPiece = this.getPiece(r,c);
                if(chessPiece == null) {
                    rtn += "X";
                } else {
                    rtn += chessPiece.getName();
                }
                //if this is not the last iteration in the row
                if(c != 7) {
                    rtn += " ";
                }
            }
            rtn += "\n";
        }
        return rtn;
    }

    public ChessBoard(ChessPiece[][] _board, int _turnNumber) {
        board = _board;
        turnNumber = _turnNumber;
    }

    public ChessPiece getPiece(int row, int col) {
        if (ChessUtil.isValidPosition(row, col))
            return board[row][col];
        else
            return null;
    }

    public ChessBoard movePiece(Move move) throws InvalidMoveException {
        if (board[move.startRow][move.startCol] == null) {
            throw new InvalidMoveException("There is not piece at position: " + move.startRow + "," + move.startCol);
        }
        //System.out.println(move);
        ArrayList<Move> validMoves = board[move.startRow][move.startCol].getValidMoves(move.startRow, move.startCol, this);
        if (validMoves.contains(move)) {
            //The chess pieces can send in moves with more information
            Move betterMove = validMoves.get(validMoves.indexOf(move));
            if (betterMove instanceof SpecialMove) {
                SpecialMove specialMove = (SpecialMove) betterMove;
                switch (specialMove.specialMoveType) {

                    case EN_PASSANT:
                        return handleEnPassant(specialMove);

                    case CASTLE_KING_SIDE:
                    case CASTLE_QUEEN_SIDE:
                        return handleCastle(specialMove);

                    default:
                        System.out.println("invalid special case requested");
                        return null;
                }
            } else {
                //create a new board for the changes
                ChessPiece[][] newBoard = copyBoard(board);
                newBoard[move.endRow][move.endCol] = newBoard[move.startRow][move.startCol];
                newBoard[move.startRow][move.startCol] = null;
                newBoard[move.endRow][move.endCol].wasMoved(move, turnNumber);
                return new ChessBoard(newBoard, turnNumber + 1);
            }
        } else {
            throw new InvalidMoveException("TODO: make a real error message");
        }

    }

    private ChessBoard handleCastle(SpecialMove specialMove) {
        if (specialMove.specialMoveType == CASTLE_KING_SIDE) {
            ChessPiece[][] newBoard = copyBoard(board);
            //move the rook into position:
            newBoard[specialMove.endRow][specialMove.endCol - 1] = newBoard[specialMove.endRow][7];
            newBoard[specialMove.endRow][7] = null;
            newBoard[specialMove.endRow][specialMove.endCol - 1].wasMoved(specialMove, turnNumber);

            //move the king into position:
            newBoard[specialMove.endRow][specialMove.endCol] = newBoard[specialMove.endRow][specialMove.startCol];
            newBoard[specialMove.endRow][specialMove.startCol] = null;
            newBoard[specialMove.endRow][specialMove.endCol].wasMoved(specialMove, turnNumber);
            return new ChessBoard(newBoard, turnNumber + 1);

        } else {
            ChessPiece[][] newBoard = copyBoard(board);
            //move the rook into position:
            newBoard[specialMove.endRow][specialMove.endCol + 1] = newBoard[specialMove.endRow][0];
            newBoard[specialMove.endRow][0] = null;
            newBoard[specialMove.endRow][specialMove.endCol + 1].wasMoved(specialMove, turnNumber);

            //move the king into position:
            newBoard[specialMove.endRow][specialMove.endCol] = newBoard[specialMove.endRow][specialMove.startCol];
            newBoard[specialMove.endRow][specialMove.startCol] = null;
            newBoard[specialMove.endRow][specialMove.endCol].wasMoved(specialMove, turnNumber);
            return new ChessBoard(newBoard, turnNumber + 1);
        }
    }

    private ChessBoard handleEnPassant(SpecialMove specialMove) {
        ChessPiece[][] newBoard = copyBoard(board);
        String movingPieceColor = newBoard[specialMove.startRow][specialMove.startCol].getColor();
        if (movingPieceColor.equals(COLOR_WHITE)) {
            //We remove the chess piece that attemped to move past us
            newBoard[specialMove.endRow + 1][specialMove.endCol] = null;
        } else {
            //We remove the chess piece that attemped to move past us
            newBoard[specialMove.endRow - 1][specialMove.endCol] = null;
        }
        //Now we move the piece normally
        newBoard[specialMove.endRow][specialMove.endCol] = newBoard[specialMove.startRow][specialMove.startCol];
        newBoard[specialMove.startRow][specialMove.startCol] = null;
        newBoard[specialMove.endRow][specialMove.endCol].wasMoved(specialMove, turnNumber);
        return new ChessBoard(newBoard, turnNumber + 1);
    }

    private ChessPiece[][] copyBoard(ChessPiece[][] oldBoard) {
        ChessPiece[][] newBoard = new ChessPiece[8][8];
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                if (oldBoard[r][c] != null) {
                    newBoard[r][c] = oldBoard[r][c].copy();
                }
            }
        }
        return newBoard;
    }

    public void replaceWithQueen(int row, int col, String color) {
        board[row][col] = new Queen(color);
    }

    private void initBlackSide(ChessPiece[][] newBoard) {
        newBoard[0][0] = new Rook(COLOR_BLACK);
        newBoard[0][1] = new Knight(COLOR_BLACK);
        newBoard[0][2] = new Bishop(COLOR_BLACK);
        newBoard[0][3] = new Queen(COLOR_BLACK);
        newBoard[0][4] = new King(COLOR_BLACK);
        newBoard[0][5] = new Bishop(COLOR_BLACK);
        newBoard[0][6] = new Knight(COLOR_BLACK);
        newBoard[0][7] = new Rook(COLOR_BLACK);

        for (int x = 0; x < 8; x++) {
            newBoard[1][x] = new Pawn(COLOR_BLACK);
        }
    }

    private void initWhiteSide(ChessPiece[][] newBoard) {
        newBoard[7][0] = new Rook(COLOR_WHITE);
        newBoard[7][1] = new Knight(COLOR_WHITE);
        newBoard[7][2] = new Bishop(COLOR_WHITE);
        newBoard[7][3] = new Queen(COLOR_WHITE);
        newBoard[7][4] = new King(COLOR_WHITE);
        newBoard[7][5] = new Bishop(COLOR_WHITE);
        newBoard[7][6] = new Knight(COLOR_WHITE);
        newBoard[7][7] = new Rook(COLOR_WHITE);

        for (int x = 0; x < 8; x++) {
            newBoard[6][x] = new Pawn(COLOR_WHITE);
        }
    }


    public void drawBoard() {
        boolean blackSquare = false;
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {

                if (board[x][y] != null) {
                    System.out.print(board[x][y].getName() + " ");
                } else {
                    if (blackSquare) {
                        System.out.print("## ");
                    } else {
                        System.out.print("   ");
                    }
                }
                blackSquare = !blackSquare;

            }
            System.out.println(8 - x);
            blackSquare = !blackSquare;
        }

        char iterate = 'a';


        for (int x = 0; x < 8; x++) {
            System.out.print(" " + iterate + " ");
            iterate++;
        }
        System.out.println();
        System.out.println();
    }

    public void showMoves(String rankFile) throws InvalidMoveException {
        //just for conversions
        Move move = new Move(rankFile, "a4");
        showMoves(move.startRow, move.startCol);
    }

    //Shows the current valid moves for a chessPiece
    public void showMoves(int row, int col) throws InvalidMoveException {
        if (board[row][col] == null) {
            throw new InvalidMoveException("There is not piece at position: " + row + "," + col);
        }
        //I create a dummy board to work with
        ChessPiece[][] goodBoard = board;
        board = copyBoard(board);
        ArrayList<Move> validMoves = board[row][col].getValidMoves(row, col, this);
        for (Move move : validMoves) {
            int endCol = move.endCol;
            int endRow = move.endRow;
            board[endRow][endCol] = new DebugPiece("ERROR");
        }
        drawBoard();
        board = goodBoard;
    }

    public ArrayList<Move> filteredAllPossibleAttackMovesForColor(String color){
        ArrayList<Move> rtn = getAllPossibleAttackMovesForColor(color);
        //now we get rid of all moves that result in us being in check
        for( int i = rtn.size() -1 ; i >= 0; i--) {
            Move move = rtn.get(i);
            try {
                ChessBoard possibleBoard = this.movePiece(move);
                if(possibleBoard.isInCheck(color)){
                    rtn.remove(i);
                }
            } catch (InvalidMoveException e) {
                //should never really happen
            }
        }
        return rtn;
    }

    public ArrayList<Move> getAllPossibleAttackMovesForColor(String color) {
        ArrayList<Move> rtnArr = new ArrayList<Move>();
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                if (board[r][c] != null && board[r][c].getColor().equals(color)) {
                    //Really scuffed solution
                    if (!(board[r][c] instanceof King)) {
                        rtnArr.addAll(board[r][c].getValidMoves(r, c, this));
                    } else {
                        rtnArr.addAll(((King) board[r][c]).getValidMovesWithoutCastle(r, c, this));
                    }
                }
            }
        }
        return rtnArr;
    }

    public boolean isTileUnderAttackByColor(int row, int col, String color) {
        ArrayList<Move> arr = getAllPossibleAttackMovesForColor(color);
        for (Move move : arr) {
            if (move.endCol == col && move.endRow == row) {
                return true;
            }
        }
        return false;
    }

    public boolean isInCheck(String color) {
        String oppositeColor = "";
        if (color.equals(COLOR_WHITE)) {
            oppositeColor = COLOR_BLACK;
        } else {
            oppositeColor = COLOR_WHITE;
        }
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                if (board[r][c] != null && board[r][c].getColor().equals(color)) {
                    if (board[r][c] instanceof King && board[r][c].getColor().equals(color)) {
                        return isTileUnderAttackByColor(r, c, oppositeColor);
                    }
                }
            }
        }
        System.out.println("isInCheck could not find a king for color: " + color);
        return false;
    }


    public void printCheckmate() {
        String wcolor = COLOR_WHITE;
        String bcolor = COLOR_BLACK;

        if (isInCheckMate(wcolor) == true) {
            System.out.println("Checkmate");
            System.out.println("White wins");

        }
        if (isInCheckMate(bcolor) == true) {
            System.out.println("Checkmate");
            System.out.println("Black wins");

        }
    }

    public boolean isInCheckMate(String color) {
        if (!isInCheck(color)) { //TODO: needs to be tested but probably works
            return false;
        }
        ArrayList<Move> arr = getAllPossibleAttackMovesForColor(color);
        for (Move move : arr) {
            try {
                ChessBoard chessBoard = this.movePiece(move);
                if (!chessBoard.isInCheck(color)) {
                    return false;
                }
            } catch (InvalidMoveException e) {
                System.out.println("THIS SHOULD NEVER HAPPEN: checkmate edition");
                e.printStackTrace();
            }
        }
        return true;
    }

}
