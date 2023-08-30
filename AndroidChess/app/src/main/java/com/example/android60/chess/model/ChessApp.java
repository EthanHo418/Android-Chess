//ajm458 Andrew Mckinney
//eh479 Ethan Ho

package com.example.android60.chess.model;
import static com.example.android60.chess.model.ChessConstants.COLOR_BLACK;
import static com.example.android60.chess.model.ChessConstants.COLOR_WHITE;

import android.content.Context;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Random;

public class ChessApp {
    ChessBoard chessBoard;
    //which color is playing out their turn
    public String currentTurn;

    public String currentGameConclusion; //draw, black wins, white wins, etc

    //Stores previous boards for the current game
    ArrayList<ChessBoard> previousBoards;

    ArrayList<CompletedChessGame> previousChessGames;

    Context context;

    public ChessApp(Context _context) {
        context = _context.getApplicationContext();
        previousChessGames = loadDataFromDisc();
    }

    public void startNewGame() {
        //we start a new game
        chessBoard = new ChessBoard();
        currentTurn = COLOR_WHITE;
        previousBoards = new ArrayList<>();
    }

    private void saveDataToDisk() {
        for(CompletedChessGame completed : previousChessGames) {
            String filename = completed.getName();
            String content = completed.exportToString();
            try (FileOutputStream fos = context.openFileOutput(filename, Context.MODE_PRIVATE)) {
                fos.write(content.getBytes("UTF-8"));
            } catch (Exception e) {
                //so much shit could have gone wrong
                e.printStackTrace();
            }
        }
    }

    private ArrayList<CompletedChessGame> loadDataFromDisc() {
        ArrayList<CompletedChessGame> rtn = new ArrayList<CompletedChessGame>();
        for (String filename : context.fileList()) {
            try {
                FileInputStream fis = context.openFileInput(filename);
                InputStreamReader inputStreamReader = new InputStreamReader(fis, StandardCharsets.UTF_8);
                BufferedReader reader = new BufferedReader(inputStreamReader);
                String line = reader.readLine();
                //This is the LocalDateTimeLine
                LocalDateTime localDateTime = LocalDateTime.parse( line.substring("Date:".length()).trim());
                //next line should just be Boards:
                line = reader.readLine();
                if(!line.equals("Boards:")) {
                    throw new Exception("loading failed for file " + filename + ", line was : " + line);
                }
                ArrayList<ChessBoard> chessBoards = new ArrayList<ChessBoard>();
                while(!line.equals("Done")) {
                    String input = "";
                    for(int i = 0; i < 8; i++) {
                        input += reader.readLine() + " ";
                    }
                    chessBoards.add(ChessBoard.fromString(input));
                    line = reader.readLine();
                }
                //not sure if I have to close everything or not
                reader.close();
                inputStreamReader.close();
                fis.close();
                rtn.add(new CompletedChessGame(chessBoards,localDateTime, filename));
            } catch (Exception e) {
                //so many things...
                e.printStackTrace();
            }
        }
        return rtn;
    }

    public void saveGame(String name) {
        previousChessGames.add( new CompletedChessGame(previousBoards, LocalDateTime.now(), name));
        previousBoards = null;
        saveDataToDisk();
    }

    public ChessBoard makeMove(Move move) throws InvalidMoveException {
        //there might be no current game ongoing
        if(chessBoard == null) {
            throw new InvalidMoveException("no game in progress");
        }
        //this might throw an exception
        ChessBoard nextChessBoard = chessBoard.movePiece(move);
        if (nextChessBoard.isInCheck(currentTurn)) {
            throw new InvalidMoveException("is in check, not good move");
        }
        //if it does not
        previousBoards.add(chessBoard);
        chessBoard = nextChessBoard;
        switchCurrentTurn();
        upgradeAllPawns();
        return chessBoard;
    }

    private void upgradeAllPawns() {
        for(int i = 0; i < 8; i++) {
            if(getPiece(0, i) != null && getPiece(0,i).getName().equals("whitep")) {
                chessBoard.replaceWithQueen(0,i,COLOR_WHITE);
                //this pawn needs to be upgraded
                //chessBoard.
            }
        }
        for(int i = 0; i < 8; i++) {
            if(getPiece(7, i) != null && getPiece(7,i).getName().equals("blackp")) {
                chessBoard.replaceWithQueen(7,i,COLOR_BLACK);
                //this pawn needs to be upgraded
                //chessBoard.
            }
        }
    }

    public void endGame(String conclusion) {
        currentGameConclusion = conclusion;
        previousBoards.add(chessBoard);
        chessBoard = null;
    }

    public String handleResign(){
        if(currentTurn.equals((COLOR_BLACK))) {
            endGame("white wins");
            return "white wins";
        } else {
            endGame("black wins");
            return "black wins";
        }
    }

    public ChessBoard makeMove(String rankFileStart, String rankFileEnd) throws InvalidMoveException {
        return makeMove(new Move(rankFileStart, rankFileEnd));
    }

    //returns the new chess board
    public void undo() {
        if(chessBoard == null) {
            return;
        }
        switchCurrentTurn();
        chessBoard = previousBoards.get(previousBoards.size() -1 );
        previousBoards.remove(previousBoards.size() -1);
    }

    private void switchCurrentTurn() {
        if(currentTurn.equals(COLOR_WHITE)) {
            currentTurn = COLOR_BLACK;
        } else{
            currentTurn = COLOR_WHITE;
        }
    }

    public ChessPiece getPiece(int row, int col) {
        if(chessBoard == null) {
            return null;
        }
        return chessBoard.getPiece(row,col);
    }

    public void makeAIMove(){
        if(chessBoard == null) {
            return;
        }
        ArrayList<Move> possibleMoves = chessBoard.filteredAllPossibleAttackMovesForColor(currentTurn);
        try {
            Random ran = new Random();
            int randomIndex = ran.nextInt(possibleMoves.size());
            makeMove(possibleMoves.get(randomIndex));
        } catch (InvalidMoveException e) {
            //this should NEVER happen
            e.printStackTrace();
        }
    }

    public boolean isInCheckMate(String color) {
        return chessBoard.isInCheckMate(color);
    }

    public void resign() {
        //current turn player quits
        //whatever
    }

    public ArrayList<CompletedChessGame> getPreviousChessGames() {
        return previousChessGames;
    }
}
