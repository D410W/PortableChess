package com.d410w.portablechess.engine;

public class BoardSimulation extends BoardState {

    public BoardSimulation(int b_width, int b_height) {
        super(b_width, b_height);
    }

    public BoardSimulation(BoardState bs) {
        this(bs.width, bs.height);

        pieces = bs.pieces.clone();
    }


}
