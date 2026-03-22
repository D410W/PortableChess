package com.d410w.portablechess.engine;

import java.util.Collections;
import java.util.ArrayList;
import java.util.List;

public class BoardState {
    ArrayList<ChessPiece> pieces;

    public BoardState() {
        pieces = new ArrayList<>();
        for (int i = 0; i < 8; ++i) {
            pieces.add(new ChessPiece(PieceColor.WHITE, PieceType.PAWN, i, 6));
            pieces.add(new ChessPiece(PieceColor.BLACK, PieceType.PAWN, i, 1));
        }

        for (PieceColor col : PieceColor.values()) {
            int y = 7;
            if (col == PieceColor.BLACK) y = 0;
            pieces.add(new ChessPiece(col, PieceType.ROOK, 0, y));
            pieces.add(new ChessPiece(col, PieceType.KNIGHT, 1, y));
            pieces.add(new ChessPiece(col, PieceType.BISHOP, 2, y));
            pieces.add(new ChessPiece(col, PieceType.QUEEN, 3, y));
            pieces.add(new ChessPiece(col, PieceType.KING, 4, y));
            pieces.add(new ChessPiece(col, PieceType.BISHOP, 5, y));
            pieces.add(new ChessPiece(col, PieceType.KNIGHT, 6, y));
            pieces.add(new ChessPiece(col, PieceType.ROOK, 7, y));
        }
    }

    public List<ChessPiece> getPieces() {
        return Collections.unmodifiableList(this.pieces);
    }
}
