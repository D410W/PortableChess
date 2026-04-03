package com.d410w.portablechess.engine;

import android.util.Pair;
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

    public void add(ChessPiece new_p) {
        pieces_unordered.removeIf(p -> p.x_pos == new_p.x_pos && p.y_pos == new_p.y_pos);

        pieces_unordered.add(new_p);
        pieces_placed.set(new_p.x_pos + new_p.y_pos * width, new_p);
    }

    public void addAll(Collection<ChessPiece> collection) {
        for (ChessPiece p : collection) {
            add(p);
        }
    }

    public ChessPiece getAt(Pair<Integer, Integer> pos) {
        return getAt(pos.first + pos.second * width);
    }

    public ChessPiece getAt(int x, int y) {
        return getAt(x + y * width);
    }

    public ChessPiece getAt(int idx) {
        if (pieces_placed.get(idx) == null)
            return null;
        else
            return pieces_placed.get(idx).clone();
    }

    public void remove(int idx) {
        remove(idx % width, idx / width);
    }

    public void remove(int x, int y) {
        System.out.println("Removing " + x + ", " + y);
        pieces_unordered.removeIf(p -> p.x_pos == x && p.y_pos == y);

        pieces_placed.set(x + y * width, null);
    }

    @NotNull
    @Override
    public Iterator<ChessPiece> iterator() {
        return pieces_unordered.iterator();
    }
}
