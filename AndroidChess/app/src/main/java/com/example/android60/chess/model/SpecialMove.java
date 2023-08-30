package com.example.android60.chess.model;

public class SpecialMove extends Move {

    public SpecialMove(int _startRow, int _startCol, int _endRow, int _endCol, String _specialMoveType) {
        super(_startRow, _startCol, _endRow, _endCol);
        specialMoveType = _specialMoveType;
        // TODO Auto-generated constructor stub
    }

    public String specialMoveType;
}
