package com.example.android60.chess.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class CompletedChessGame implements Serializable {
    public CompletedChessGame(ArrayList<ChessBoard> _allBoards, LocalDateTime _dateCompleted, String _name) {
        allBoards = _allBoards;
        dateCompleted = _dateCompleted;
        name = _name;
    }

    public ArrayList<ChessBoard> allBoards;
    public LocalDateTime dateCompleted;
    public String name;

    @Override
    public String toString() {
        return "Date: " + dateCompleted.toString() + "\n" + "Name: " + name;
    }

    public String exportToString(){
        //same is included in the name of the file
        String rtn = "Date:" + dateCompleted.toString() + "\n" +
                "Boards:\n";
        for(int i = 0; i < allBoards.size(); i++) {
            ChessBoard chessBoard = allBoards.get(i);
            rtn += chessBoard.exportToString();
            if(i == allBoards.size() -1) {
                rtn += "Done";
            } else {
                rtn += "NextBoard:\n";
            }
        }
        return rtn;
    }

    public ArrayList<ChessBoard> getAllBoards() {
        return allBoards;
    }

    public void setAllBoards(ArrayList<ChessBoard> allBoards) {
        this.allBoards = allBoards;
    }

    public LocalDateTime getDateCompleted() {
        return dateCompleted;
    }

    public void setDateCompleted(LocalDateTime dateCompleted) {
        this.dateCompleted = dateCompleted;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
