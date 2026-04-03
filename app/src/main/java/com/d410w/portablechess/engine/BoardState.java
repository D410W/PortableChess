package com.d410w.portablechess.engine;

import android.util.Pair;

import java.util.ArrayList;
import java.util.List;

public class BoardState {
    ChessPieceCollection pieces;
    int width = 8;
    int height = 8;

    final ArrayList<Pair<Integer, Integer>> diagonal_dirs;
    final ArrayList<Pair<Integer, Integer>> parallel_dirs;

    public BoardState(int b_width, int b_height) {
        diagonal_dirs = new ArrayList<>(List.of(Pair.create(1,1), Pair.create(-1,1), Pair.create(1,-1), Pair.create(-1,-1)));
        parallel_dirs = new ArrayList<>(List.of(Pair.create(1,0), Pair.create(-1,0), Pair.create(0,1), Pair.create(0,-1)));

        width = b_width;
        height = b_height;

        pieces = new ChessPieceCollection(width, height);
        for (int i = 0; i < width; ++i) {
            addPiece(new ChessPiece(PieceColor.WHITE, PieceType.PAWN, i, 6));
            addPiece(new ChessPiece(PieceColor.BLACK, PieceType.PAWN, i, 1));
        }

        for (PieceColor col : PieceColor.values()) {
            int y = height-1;
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
        pieces.add(p);
    }

    private boolean isInsideBoard(Pair<Integer, Integer> pos) {
        boolean x = 0 <= pos.first && pos.first < width;
        boolean y = 0 <= pos.second && pos.second < height;

        return x && y;
    }
    private boolean isInsideBoard(ChessPiece piece) {
        boolean x = 0 <= piece.x_pos && piece.x_pos < width;
        boolean y = 0 <= piece.y_pos && piece.y_pos < height;

        return x && y;
    }

    public ArrayList<ChessPiece> getPieces() {
        ArrayList<ChessPiece> list = new ArrayList<>();
        pieces.iterator().forEachRemaining(p -> list.add(p.clone()));

        return list;
    }

    private ArrayList<Integer> getMovesHelper(ChessPiece piece) {
        ArrayList<Integer> valid = new ArrayList<>();

        for (Pair<Integer, Integer> pos : piece.extraMoves()) {
            if (!isInsideBoard(pos)) continue;
            if (pieces.getAt(pos.first + pos.second * width) != null &&
                pieces.getAt(pos.first + pos.second * width).color == piece.color) continue;

            valid.add(pos.first + pos.second * width);
        }
        for (Pair<Integer, Integer> pos : piece.noAttackMoves()) {
            if (!isInsideBoard(pos)) continue;
            if (pieces.getAt(pos.first + pos.second * width) != null) continue;

            valid.add(pos.first + pos.second * width);
        }

        for (Pair<Integer, Integer> pos : piece.attackMoves()) {
            if (!isInsideBoard(pos)) continue;
            if (pieces.getAt(pos.first + pos.second * width) != null &&
                pieces.getAt(pos.first + pos.second * width).color != piece.color) {

                valid.add(pos.first + pos.second * width);
            }
        }

        for (Pair<Integer, Integer> dir : parallel_dirs) {
            ChessPiece helper = piece.clone();
            for (int i = 0; i != piece.horizontalRange(); ++i) {
                helper.addOffset(dir);

                if (!isInsideBoard(helper)) break;

                if (pieces.getAt(helper.posPair()) != null) {
                    if (pieces.getAt(helper.posPair()).color != piece.color)
                        valid.add(helper.posInt(width));
                    break;
                }

                valid.add(helper.posInt(width));
            }
        }
        for (Pair<Integer, Integer> dir : diagonal_dirs) {
            ChessPiece helper = piece.clone();
            for (int i = 0; i != piece.diagonalRange(); ++i) {
                helper.addOffset(dir);

                if (!isInsideBoard(helper)) break;

                if (pieces.getAt(helper.posPair()) != null) {
                    if (pieces.getAt(helper.posPair()).color != piece.color)
                        valid.add(helper.posInt(width));
                    break;
                }

                valid.add(helper.posInt(width));
            }
        }

        return valid;
    }

    public ArrayList<Integer> getMovesFromPiece(ChessPiece p) {
        ArrayList<Integer> valid = getMovesHelper(p);

        return valid;
    }

    public boolean isValid(ChessPiece piece, int x_pos, int y_pos) {
        ArrayList<Integer> possible = getMovesFromPiece(piece);

        return possible.contains(x_pos + y_pos * width);
    }

    public PieceEvent movePiece(ChessPiece piece, int x_pos, int y_pos) {
        int old_pos = piece.y_pos * width + piece.x_pos;
        ChessPiece temp = pieces.getAt(old_pos);
        pieces.remove(old_pos);

        temp.x_pos = x_pos;
        temp.y_pos = y_pos;

        pieces.add(temp);
        return PieceEvent.MOVE_SELF;
    }
}
