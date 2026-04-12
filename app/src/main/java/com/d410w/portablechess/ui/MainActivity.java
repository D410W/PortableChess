package com.d410w.portablechess.ui;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.d410w.portablechess.R;
import com.d410w.portablechess.engine.BoardState;
import com.d410w.portablechess.engine.ChessMove;
import com.d410w.portablechess.engine.ChessPiece;
import com.d410w.portablechess.engine.PieceEvent;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements BoardCallback {

    AppCanvas canvas;
    BoardState board_state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        canvas = findViewById(R.id.chessBoardCanvas);
        board_state = new BoardState(8, 8);
        canvas.setPieces(board_state.getPieces(), PieceEvent.NONE);

        findViewById(R.id.undo_move).setOnClickListener(v -> {
            if (board_state.undoMove()) {
                updateCanvas(PieceEvent.MOVE);
            }
        });
        findViewById(R.id.redo_move).setOnClickListener(v -> {
            PieceEvent event = board_state.redoMove();
            if (event != PieceEvent.NONE) {
                updateCanvas(event);
            }
        });
    }

    void updateCanvas(PieceEvent event) {
        canvas.setPieces(board_state.getPieces(), event);
        canvas.unselectPiece();
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
    public void movedPiece(ChessMove move) {
        if (!board_state.isValid(move))
            return;

        PieceEvent event = board_state.movePiece(move);
        if (event.isPromotion()) {
            board_state.promotePiece(move.end(), canvas.getPromotionChoice());
        }

        if (move.getCaptured() != null) {
            System.out.println(move.getEvent() + ", " + move.getCaptured().type);
        } else {
            System.out.println(move.getEvent() + ", ");
        }

        canvas.setPieces(board_state.getPieces(), event);
    }
}