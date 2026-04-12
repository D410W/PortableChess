package com.d410w.portablechess.ui;

import com.d410w.portablechess.engine.ChessMove;
import com.d410w.portablechess.engine.ChessPiece;

public interface BoardCallback {
    void selectedPiece(ChessPiece p);
    void movedPiece(ChessMove move);
}
