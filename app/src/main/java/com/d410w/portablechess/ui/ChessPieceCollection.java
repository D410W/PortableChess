package com.d410w.portablechess.ui;

import com.d410w.portablechess.engine.ChessPiece;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class ChessPieceCollection implements Iterable<ChessPiece> {

    ArrayList<ChessPiece> pieces_placed;
    ArrayList<ChessPiece> pieces_unordered;
    int width;
    int height;

    public ChessPieceCollection(int width, int height) {
        this.width = width;
        this.height = height;
        pieces_placed = new ArrayList<>(Collections.nCopies(width * height, null));
        pieces_unordered = new ArrayList<>();
    }

    public void clear() {
        Collections.fill(pieces_placed, null);
        pieces_unordered.clear();
    }

    public void addAll(Collection<ChessPiece> collection) {
        pieces_unordered.addAll(collection);
        for (ChessPiece p : collection) {
            pieces_placed.set(p.y_pos * width + p.x_pos, p);
        }
    }

    public ChessPiece getAt(int x, int y) {
        return pieces_placed.get(y * width + x);
    }

    public ChessPiece getAt(int idx) {
        return pieces_placed.get(idx);
    }

    @NotNull
    @Override
    public Iterator<ChessPiece> iterator() {
        return pieces_unordered.iterator();
    }
}
