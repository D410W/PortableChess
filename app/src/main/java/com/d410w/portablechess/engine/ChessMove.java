package com.d410w.portablechess.engine;

import android.util.Pair;

public class ChessMove {

    int start_x;
    int start_y;
    int end_x;
    int end_y;

    PieceEvent event;
    ChessPiece captured_piece;
    boolean was_moved;

    public ChessMove(int start_x, int start_y, int end_x, int end_y) {
        this.start_x = start_x;
        this.start_y = start_y;
        this.end_x = end_x;
        this.end_y = end_y;
    }

    public ChessMove(int start_x, int start_y, int end_x, int end_y, ChessPiece captured) {
        this.captured_piece = captured;
        this(start_x, start_y, end_x, end_y);
    }

    public Pair<Integer, Integer> end() {
        return Pair.create(end_x, end_y);
    }

    public Pair<Integer, Integer> start() {
        return Pair.create(start_x, start_y);
    }

    public void setInfo(ChessPiece captured, PieceEvent event, boolean was_moved) {
        captured_piece = captured;
        this.event = event;
        this.was_moved = was_moved;
    }

    public PieceEvent getEvent() {
        return this.event;
    }

    public ChessPiece getCaptured() {
        if (this.captured_piece == null) {
            return null;
        } else {
            return this.captured_piece;
        }
    }
}