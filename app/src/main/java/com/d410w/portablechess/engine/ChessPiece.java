package com.d410w.portablechess.engine;

import android.util.Pair;

import java.util.ArrayList;

public class ChessPiece implements Cloneable {
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
        boolean x = 0 <= x_pos && x_pos < b_width;
        boolean y = 0 <= y_pos && y_pos < b_height;

        return x && y;
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

    public ArrayList<Pair<Integer, Integer>> noAttackMoves() {
        ArrayList<Pair<Integer, Integer>> possible = new ArrayList<>();

        if (type == PieceType.PAWN) {
            int y = -1;
            if (color == PieceColor.BLACK) y = 1;

            possible.add(Pair.create(x_pos, y_pos + y));
        }

        return possible;
    }

    public ArrayList<Pair<Integer, Integer>> extraMoves() { // asymmetric moves, knight moves
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

    @Override
    public ChessPiece clone() {
        try {
            ChessPiece clone = (ChessPiece) super.clone();
            clone.type = type;
            clone.color = color;
            clone.x_pos = x_pos;
            clone.y_pos = y_pos;
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
