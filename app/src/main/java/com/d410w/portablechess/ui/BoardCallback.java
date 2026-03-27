package com.d410w.portablechess.ui;

import com.d410w.portablechess.engine.ChessPiece;

public interface BoardCallback {
    void selectedPiece(ChessPiece p);
    void movedPiece(ChessPiece start, int x_pos, int y_pos);
}
