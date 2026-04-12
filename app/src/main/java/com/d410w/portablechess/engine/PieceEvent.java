package com.d410w.portablechess.engine;

public enum PieceEvent {
    NONE,
    CASTLE,
    CAPTURE,
    MOVE,
    MOVE_CHECK,
    PROMOTE,
    PROMOTE_CAPTURE;

    public PieceEvent promote() {
        if (this == MOVE) {
            return PROMOTE;
        } else if (this == CAPTURE) {
            return PROMOTE_CAPTURE;
        } else {
            return NONE;
        }
    }

    public boolean isPromotion() {
        return this == PROMOTE || this == PROMOTE_CAPTURE;
    }

    public boolean isCapture() {
        return this == CAPTURE || this == PROMOTE_CAPTURE;
    }
}
