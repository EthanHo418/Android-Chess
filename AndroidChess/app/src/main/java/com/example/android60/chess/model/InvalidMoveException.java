package com.example.android60.chess.model;

/**
 * invalid moves exception for invalid moves
 *
 * @author Ethan Ho
 */
public class InvalidMoveException extends Exception {
    /**
     * creates a new invalidMovesException
     *
     * @param errMsg error message
     */
    public InvalidMoveException(String errMsg) {
        super(errMsg);
    }
}
