package com.d410w.portablechess.engine;

public class ChessPiece {
    public PieceType type;
    public PieceColor color;
    public int x_pos;
    public int y_pos;

    ChessPiece(PieceColor color, PieceType type) {
        this(color, type, -1, -1);
    }

    ChessPiece(PieceColor color, PieceType type, int x_pos, int y_pos) {
        this.x_pos = x_pos;
        this.y_pos = y_pos;
        this.type = type;
        this.color = color;
    }
}
