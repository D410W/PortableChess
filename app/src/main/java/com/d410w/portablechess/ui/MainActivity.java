package com.d410w.portablechess.ui;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.d410w.portablechess.engine.BoardState;
import com.d410w.portablechess.engine.ChessPiece;
import com.d410w.portablechess.engine.PieceEvent;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements BoardCallback {

    AppCanvas canvas;
    BoardState board_state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        canvas = new AppCanvas(this);
        board_state = new BoardState(8, 8);
        canvas.setPieces(board_state.getPieces(), PieceEvent.NONE);
        setContentView(canvas);
    }

    @Override
    public void selectedPiece(ChessPiece p) {
        System.out.println("index: " + (p.x_pos + p.y_pos * 8));

        canvas.is_highlighted[p.x_pos + p.y_pos * 8] = true;

        ArrayList<Integer> highlights = board_state.getMovesFromPiece(p);
        canvas.highlightSquares(highlights);
        canvas.invalidate();
    }

    @Override
    public void movedPiece(ChessPiece start, int x_pos, int y_pos) {
        if (!board_state.isValid(start, x_pos, y_pos))
            return;

        PieceEvent event = board_state.movePiece(start, x_pos, y_pos);
        if (event == PieceEvent.PROMOTE) {
            board_state.promotePiece(x_pos, y_pos, canvas.getPromotionChoice());
        }
        canvas.setPieces(board_state.getPieces(), event);
    }
}