package com.d410w.portablechess.engine;

import android.util.Pair;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.abs;

public class BoardState {
    ChessPieceCollection pieces;
    int width;
    int height;

    final ArrayList<Pair<Integer, Integer>> diagonal_dirs;
    final ArrayList<Pair<Integer, Integer>> parallel_dirs;

    ArrayList<ChessMove> history;
    int history_pos;

    public BoardState(int b_width, int b_height) {
        diagonal_dirs = new ArrayList<>(List.of(Pair.create(1,1), Pair.create(-1,1), Pair.create(1,-1), Pair.create(-1,-1)));
        parallel_dirs = new ArrayList<>(List.of(Pair.create(1,0), Pair.create(-1,0), Pair.create(0,1), Pair.create(0,-1)));

        history = new ArrayList<>();
        history_pos = -1;

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

    private void addHistory(ChessMove move) {
        ++history_pos;

        if (history.size() < history_pos + 1) {
            history.add(move);
        } else {
            history.set(history_pos, move);

            while (history.size() > history_pos + 1) {
                history.remove(history.size() - 1);
            }
        }
    }

    public boolean undoMove() {
        if (history.isEmpty() || history_pos < 0)
            return false;

        ChessMove last = history.get(history_pos);
        System.out.println(last);
        if (last.event.isPromotion()) {
            // todo
        } else if (last.event == PieceEvent.CASTLE) {
            // todo
        } else {
            ChessPiece piece = pieces.getRefAt(last.end());
            pieces.remove(last.end());

            if (last.getCaptured() != null)
                pieces.add(last.getCaptured());

            piece.x_pos = last.start_x;
            piece.y_pos = last.start_y;
            piece.moved = last.was_moved;

            pieces.add(piece);
        }

        --history_pos;
        return true;
    }
    
    public PieceEvent redoMove() {
        if (history_pos >= history.size() - 1)
            return PieceEvent.NONE;
        
        ChessMove next = history.get(history_pos + 1);

        if (next.event.isPromotion()) {
            // todo
        } else if (next.event == PieceEvent.CASTLE) {
            // todo
        } else {
            ChessPiece piece = pieces.getRefAt(next.start());
            pieces.remove(next.start());

            piece.x_pos = next.end_x;
            piece.y_pos = next.end_y;
            piece.moved = true;

            pieces.add(piece);
        }
        
        ++history_pos;
        return next.event;
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

    private ArrayList<Pair<Integer, Integer>> iterativeMoves(ChessPiece piece) {
        ArrayList<Pair<Integer, Integer>> valid = new ArrayList<>();

        for (Pair<Integer, Integer> dir : parallel_dirs) {
            ChessPiece helper = piece.clone();
            for (int i = 0; i != piece.horizontalRange(); ++i) {
                helper.addOffset(dir);

                if (!isInsideBoard(helper)) break;

                if (pieces.getAt(helper.posPair()) != null) {
                    if (pieces.getAt(helper.posPair()).color != piece.color)
                        valid.add(helper.posPair());
                    break;
                }

                valid.add(helper.posPair());
            }
        }
        for (Pair<Integer, Integer> dir : diagonal_dirs) {
            ChessPiece helper = piece.clone();
            for (int i = 0; i != piece.diagonalRange(); ++i) {
                helper.addOffset(dir);

                if (!isInsideBoard(helper)) break;

                if (pieces.getAt(helper.posPair()) != null) {
                    if (pieces.getAt(helper.posPair()).color != piece.color)
                        valid.add(helper.posPair());
                    break;
                }

                valid.add(helper.posPair());
            }
        }

        return valid;
    }

    private ArrayList<Integer> getMovesHelper(ChessPiece piece) {
        ArrayList<Integer> valid = new ArrayList<>();

        for (Pair<Integer, Integer> pos : piece.normalMoves()) {
            if (!isInsideBoard(pos)) continue;
            if (pieces.getAt(pos.first + pos.second * width) != null &&
                    pieces.getAt(pos.first + pos.second * width).color == piece.color) continue;

            valid.add(pos.first + pos.second * width);
        }

        for (Pair<Integer, Integer> pos : piece.noAttackMoves(pieces)) {
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

        if (piece.type == PieceType.PAWN)
            for (Pair<Integer, Integer> pos : piece.enPassantMoves(pieces.en_passantable_pawn)) {
                if (!isInsideBoard(pos)) continue;
                valid.add(pos.first + pos.second * width);
            }

        if (piece.type == PieceType.KING)
            for (Pair<Integer, Integer> pos : piece.castlingMoves(attackingSquares(piece.color.opposite()), pieces)) {
                if (!isInsideBoard(pos)) continue;
                valid.add(pos.first + pos.second * width);
            }

        for (Pair<Integer, Integer> pos : iterativeMoves(piece)) {
            if (!isInsideBoard(pos)) continue;
            valid.add(pos.first + pos.second * width);
        }

        return valid;
    }

    public ArrayList<Integer> getMovesFromPiece(ChessPiece p) {
        ArrayList<Integer> valid = getMovesHelper(p);

        // checking if resulting move leaves the king in danger
        if (pieces.getKing(p.color) != null)
            valid.removeIf(idx -> {
                BoardSimulation sim = new BoardSimulation(this);

                ChessPiece actual = sim.pieces.getAt(p.posPair());
                sim.pieces.remove(actual.posPair());
                actual.x_pos = idx % width;
                actual.y_pos = idx / width;
                sim.pieces.add(actual);

                ArrayList<Pair<Integer, Integer>> attacking = sim.attackingSquares(actual.color.opposite());

                return attacking.contains(sim.pieces.getKing(actual.color).posPair());
            });

        if (p.type == PieceType.KING) {
            ArrayList<Pair<Integer, Integer>> attacking = attackingSquares(p.color.opposite());
            valid.removeIf(idx -> attacking.contains(Pair.create(idx % width, idx / width)));
        }

        return valid;
    }

    public boolean isValid(ChessPiece piece, int x_pos, int y_pos) {
        ArrayList<Integer> possible = getMovesFromPiece(piece);

        return possible.contains(x_pos + y_pos * width);
    }

    public boolean isValid(ChessPiece piece, Pair<Integer, Integer> end) {
        return isValid(piece, end.first, end.second);
    }

    public boolean isValid(ChessMove move) {
        return isValid(pieces.getAt(move.start()), move.end());
    }

    public ArrayList<Pair<Integer, Integer>> attackingSquares(PieceColor color) {
        ArrayList<Pair<Integer, Integer>> attacking = new ArrayList<>();

        for (ChessPiece p : pieces) {
            if (p.color == color) {
                attacking.addAll(p.attackMoves());
                attacking.addAll(p.normalMoves());
                attacking.addAll(iterativeMoves(p));
            }
        }

        return attacking;
    }

    public PieceEvent movePiece(ChessMove move) {
        PieceEvent event = PieceEvent.MOVE;
        ChessPiece piece = pieces.getAt(move.start());
        ChessPiece removed = null;
        boolean was_moved = piece.moved;

        // checking if was en passant
        if (piece.type == PieceType.PAWN &&
                piece.enPassantMoves(pieces.en_passantable_pawn).contains(move.end())) {
            // removing en passant-ed pawn
            removed = pieces.getAt(move.end_x, move.start_y);
            pieces.remove(removed.posPair());
        }

        // checking if castled
        if (piece.type == PieceType.KING &&
                piece.castlingMoves(attackingSquares(piece.color.opposite()), pieces).contains(move.end())) {
            event = PieceEvent.CASTLE;
            // moving rook to correct position
            if (move.end_x == 6) { // right side
                movePiece(pieces.getAt(7, move.end_y), 5, move.end_y);
            } else if (move.end_x == 2) { // left side
                movePiece(pieces.getAt(0, move.end_y), 3, move.end_y);
            }
        }

        // checking if was attack
        if (pieces.getAt(move.end()) != null) {
            removed = pieces.getAt(move.end());
            event = PieceEvent.CAPTURE;
        }

        // checking if was promotion
        if (piece.type == PieceType.PAWN && (move.end_y == 0 || move.end_y == 7)){
            event = event.promote();
        }

        piece.moved = true;

        // updating en_passantable_pawn of ChessPieceCollection
        pieces.en_passantable_pawn = null;
        if (piece.type == PieceType.PAWN && abs(move.start_y - move.end_y) == 2) {
            pieces.en_passantable_pawn = piece;
        }

        pieces.remove(move.start());

        piece.x_pos = move.end_x;
        piece.y_pos = move.end_y;

        pieces.add(piece.clone());

        move.setInfo(removed, event, was_moved);
        addHistory(move);
        return event;
    }

    public PieceEvent movePiece(ChessPiece piece, int x_pos, int y_pos) {
        ChessMove move = new ChessMove(piece.x_pos, piece.y_pos, x_pos, y_pos);
        return movePiece(move);
    }

    public PieceEvent movePiece(ChessPiece piece, Pair<Integer, Integer> end) {
        return movePiece(piece, end.first, end.second);
    }

    public void promotePiece(int x_pos, int y_pos, PieceType type) {
        pieces.getRefAt(x_pos, y_pos).type = type;
    }

    public void promotePiece(Pair<Integer, Integer> pos, PieceType type) {
        promotePiece(pos.first, pos.second, type);
    }
}
