package com.d410w.portablechess.ui;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.d410w.portablechess.engine.BoardState;

public class MainActivity extends AppCompatActivity {

    AppCanvas canvas;
    BoardState board_state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        canvas = new AppCanvas(this);
        board_state = new BoardState();
        canvas.setPieces(board_state.getPieces());
        setContentView(canvas);
    }
}