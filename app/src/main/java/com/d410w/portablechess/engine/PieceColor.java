package com.d410w.portablechess.engine;

public enum PieceColor {
    BLACK,
    WHITE;

    public PieceColor opposite() {
        if (this == BLACK)
            return WHITE;
        else // c == WHITE
            return BLACK;
    }
}
