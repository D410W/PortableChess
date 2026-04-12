package com.d410w.portablechess.engine;

import android.util.Pair;
import androidx.annotation.NonNull;

import java.util.ArrayList;

public class ChessPiece implements Cloneable {
    public PieceType type;
    public PieceColor color;
    public int x_pos;
    public int y_pos;
    boolean moved;

    ChessPiece(PieceColor color, PieceType type) {
        this(color, type, -1, -1);
    }

    ChessPiece(PieceColor color, PieceType type, int x_pos, int y_pos) {
        this.x_pos = x_pos;
        this.y_pos = y_pos;
        this.type = type;
        this.color = color;
        moved = false;
    }

    public Pair<Integer, Integer> posPair() {
        return Pair.create(x_pos, y_pos);
    }

    public int posInt(int width) {
        return x_pos + y_pos * width;
    }

    public void addOffset(Pair<Integer, Integer> offset) {
        x_pos += offset.first;
        y_pos += offset.second;
    }

    public boolean isInsideBoard(int b_width, int b_height) {
        boolean inx = 0 <= x_pos && x_pos < b_width;
        boolean iny = 0 <= y_pos && y_pos < b_height;

        return inx && iny;
    }

    public int horizontalRange() {
        if (type == PieceType.QUEEN || type == PieceType.ROOK)
            return -1;
        else if (type == PieceType.KING)
            return 1;
        else
            return 0;
    }

    public int diagonalRange() {
        if (type == PieceType.QUEEN || type == PieceType.BISHOP)
            return -1;
        else if (type == PieceType.KING)
            return 1;
        else
            return 0;
    }

    public ArrayList<Pair<Integer, Integer>> attackMoves() {
        ArrayList<Pair<Integer, Integer>> possible = new ArrayList<>();

        if (type == PieceType.PAWN) {
            int y_offset = -1;
            if (color == PieceColor.BLACK) y_offset = 1;

            possible.add(Pair.create(x_pos + 1, y_pos + y_offset));
            possible.add(Pair.create(x_pos - 1, y_pos + y_offset));
        }

        return possible;
    }

    public ArrayList<Pair<Integer, Integer>> noAttackMoves(ChessPieceCollection pieces) {
        ArrayList<Pair<Integer, Integer>> possible = new ArrayList<>();

        if (type == PieceType.PAWN) {
            int y_dir = -1;
            if (color == PieceColor.BLACK) y_dir = 1;

            possible.add(Pair.create(x_pos, y_pos + y_dir));

            if (!moved && pieces.getAt(x_pos, y_pos + y_dir) == null)
                possible.add(Pair.create(x_pos, y_pos + 2 * y_dir));
        }

        return possible;
    }

    public ArrayList<Pair<Integer, Integer>> normalMoves() { // knight moves
        ArrayList<Pair<Integer, Integer>> possible = new ArrayList<>();
        if (type == PieceType.KNIGHT) {
            for (int i = -1; i <= 1; i += 2) {
                for (int j = -1; j <= 1; j += 2) {
                    possible.add(Pair.create(x_pos + i * 2, y_pos + j));
                    possible.add(Pair.create(x_pos + i, y_pos + j * 2));
                }
            }
        }

        return possible;
    }

    public ArrayList<Pair<Integer, Integer>> enPassantMoves(ChessPiece en_passant_pawn) { // en passant
        ArrayList<Pair<Integer, Integer>> possible = new ArrayList<>();

        if (type != PieceType.PAWN) return possible;

        if (en_passant_pawn != null && en_passant_pawn.color != color && en_passant_pawn.y_pos == y_pos) {
            int y_dir = -1;
            if (color == PieceColor.BLACK) y_dir = 1;

            if (en_passant_pawn.x_pos == x_pos + 1) {
                possible.add(Pair.create(x_pos + 1, y_pos + y_dir));
            } else if (en_passant_pawn.x_pos == x_pos - 1) {
                possible.add(Pair.create(x_pos - 1, y_pos + y_dir));
            }
        }

        return possible;
    }

    public ArrayList<Pair<Integer, Integer>> castlingMoves(ArrayList<Pair<Integer, Integer>> attacking_squares, ChessPieceCollection pieces) { // castling
        ArrayList<Pair<Integer, Integer>> possible = new ArrayList<>();

        if (type != PieceType.KING) return possible;
        if (moved) return possible;
        if (attacking_squares.contains(Pair.create(x_pos, y_pos))) return possible;

        // right side
        ChessPiece rook = pieces.getRefAt(7, y_pos);
        if (rook != null && rook.type == PieceType.ROOK && !rook.moved) {
            if (pieces.getAt(5, y_pos) == null && pieces.getAt(6, y_pos) == null)
                if (!attacking_squares.contains(Pair.create(5, y_pos)) && !attacking_squares.contains(Pair.create(6, y_pos)))
                    possible.add(Pair.create(6, y_pos));
        }
        // left side
        rook = pieces.getRefAt(0, y_pos);
        if (rook != null && rook.type == PieceType.ROOK && !rook.moved) {
            if (pieces.getAt(3, y_pos) == null && pieces.getAt(2, y_pos) == null)
                if (!attacking_squares.contains(Pair.create(3, y_pos)) && !attacking_squares.contains(Pair.create(2, y_pos)))
                    possible.add(Pair.create(2, y_pos));
        }

        return possible;
    }

    @NonNull
    @Override
    public ChessPiece clone() {
        try {
            return (ChessPiece) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
