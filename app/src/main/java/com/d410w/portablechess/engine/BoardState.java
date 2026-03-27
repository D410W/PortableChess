package com.d410w.portablechess.engine;

import java.util.Collections;
import java.util.ArrayList;
import java.util.Objects;
import java.util.stream.Collectors;

public class BoardState {
    ArrayList<ChessPiece> pieces;
    int width = 8;
    int height = 8;

    public BoardState(int b_width, int b_height) {
        pieces = new ArrayList<>(Collections.nCopies(b_width * b_height, null));
        for (int i = 0; i < 8; ++i) {
            addPiece(new ChessPiece(PieceColor.WHITE, PieceType.PAWN, i, 6));
            addPiece(new ChessPiece(PieceColor.BLACK, PieceType.PAWN, i, 1));
        }

        for (PieceColor col : PieceColor.values()) {
            int y = 7;
            if (col == PieceColor.BLACK) y = 0;
            addPiece(new ChessPiece(col, PieceType.ROOK, 0, y));
            addPiece(new ChessPiece(col, PieceType.KNIGHT, 1, y));
            addPiece(new ChessPiece(col, PieceType.BISHOP, 2, y));
            addPiece(new ChessPiece(col, PieceType.QUEEN, 3, y));
            addPiece(new ChessPiece(col, PieceType.KING, 4, y));
            addPiece(new ChessPiece(col, PieceType.BISHOP, 5, y));
            addPiece(new ChessPiece(col, PieceType.KNIGHT, 6, y));
            addPiece(new ChessPiece(col, PieceType.ROOK, 7, y));
        }
    }

    private void addPiece(ChessPiece p) {
        pieces.set(p.y_pos * width + p.x_pos, p);
    }

    public ArrayList<ChessPiece> getPieces() {
        return pieces.stream()
            .filter(Objects::nonNull)
            .collect(Collectors.toCollection(ArrayList::new));
    }

    public ArrayList<Integer> getValidFromPiece(ChessPiece p) {
        ArrayList<Integer> valid = new ArrayList<>();
        return valid;
    }

    public boolean isValid(ChessPiece piece, int x_pos, int y_pos) {
        return true;
    }

    public PieceEvent movePiece(ChessPiece piece, int x_pos, int y_pos) {
        int old_pos = piece.y_pos * width + piece.x_pos;
        ChessPiece temp = pieces.get(old_pos);
        pieces.set(old_pos, null);

        temp.x_pos = x_pos;
        temp.y_pos = y_pos;

        pieces.set(y_pos * width + x_pos, temp);
        return PieceEvent.MOVE_SELF;
    }
}
