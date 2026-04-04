package com.d410w.portablechess.ui;

import android.content.Context;
import android.graphics.*;
import android.view.MotionEvent;
import android.view.View;
import com.d410w.portablechess.engine.ChessPiece;
import com.d410w.portablechess.engine.ChessPieceCollection;
import com.d410w.portablechess.engine.PieceEvent;
import com.d410w.portablechess.engine.PieceType;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.min;

public class AppCanvas extends View {

    // pieces drawing
    Rect board;
    Rect[] squares;
    boolean[] is_highlighted;
    ChessImages chess_images;

    // pieces audio
    ChessAudios chess_audios;

    // paints
    Paint board_paint;
    Paint light_paint;
    Paint dark_paint;
    Paint highlight_paint;

    // pieces info
    ChessPieceCollection pieces_info;
    ChessPiece selected_piece;

    // main activity
    BoardCallback boardcallback;

    public AppCanvas(Context context) {
        super(context);

        // pieces info
        pieces_info = new ChessPieceCollection(8, 8);
        selected_piece = null;

        // pieces drawing
        squares = new Rect[64];
        is_highlighted = new boolean[64];
        chess_images = new ChessImages(context);
        chess_audios = new ChessAudios(context);

        // paints
        board_paint = new Paint();
        board_paint.setColor(Color.rgb(129, 84, 56));
        light_paint = new Paint();
        light_paint.setColor(Color.rgb(200, 200, 200));
        dark_paint = new Paint();
        dark_paint.setColor(Color.rgb(56, 56, 56));
        highlight_paint = new Paint();
        highlight_paint.setColor(Color.argb(100, 0, 255, 0));

        // main activity
        boardcallback = (BoardCallback)context;

        for (int i = 0; i < 64; ++i) {
            squares[i] = new Rect();
            is_highlighted[i] = false;
        }

    }

    public void setPieces(List<ChessPiece> pieces, PieceEvent event) {
        pieces_info.clear();
        pieces_info.addAll(pieces);
        chess_audios.playSound(event);
        this.invalidate();
    }

    void boardClick(float x, float y) {
        if (!board.contains((int)x, (int)y)) return;

        unHighlightSquares();

        if (selected_piece == null) {
            for (int i = 0; i < squares.length; ++i) {
                if (squares[i].contains((int) x, (int) y)) {
                    ChessPiece p = pieces_info.getAt(i);
                    if (p != null) {
                        boardcallback.selectedPiece(p);
                        selected_piece = p;
                    }
                    break;
                }
            }
        } else { // has a piece. probably wants to move
            for (int i = 0; i < squares.length; ++i) {
                if (squares[i].contains((int) x, (int) y)) {
                    boardcallback.movedPiece(selected_piece, i%8, i/8);
                    break;
                }
            }
            selected_piece = null;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // System.out.println("Touch down: x: " + event.getX() + ", y: " + event.getY());
                boardClick(event.getX(), event.getY());
                return true;
            case MotionEvent.ACTION_UP:
                break;
        }
        return super.onTouchEvent(event);
    }

    public PieceType getPromotionChoice() {
        return PieceType.QUEEN;
    }

    public void updateChessBackground(int w, int h) {
        int board_size = min(w, h);
        board = new Rect(0, 0, board_size, board_size);

        int square_size = board_size / 8;
        for (int i = 0; i < 64; ++i) {
            int x = i % 8;
            int y = i / 8;


            squares[i].set(x * square_size, y * square_size,
                    (x+1) * square_size, (y+1) * square_size);
        }

        this.invalidate();
    }

    @Override
    public void onSizeChanged(int w, int h, int oldw, int oldh) {
        chess_images.resizeImages(w, h);
        updateChessBackground(w, h);
    }

    public void highlightSquares(ArrayList<Integer> squares) {
        for (int i : squares) {
            is_highlighted[i] = true;
        }
        this.invalidate();
    }
    public void unHighlightSquares() {
        for (int i = 0; i < 64; ++i) {
            is_highlighted[i] = false;
        }
        this.invalidate();
    }

    public void drawChessBackground(@NotNull Canvas canvas) {
        canvas.drawRect(board, board_paint);
        for (int i = 0; i < 64; ++i) {

            boolean is_dark = ((i + i/8) % 2 == 1);
            Paint p = is_dark ? dark_paint : light_paint;

            canvas.drawRect(squares[i], p);

            if (is_highlighted[i]) {
                canvas.drawRect(squares[i], highlight_paint);
            }
        }
    }

    @Override
    protected void onDraw(@NotNull Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.GRAY);

        System.out.println("drawing. selected: " + selected_piece);

        drawChessBackground(canvas);

        for (ChessPiece p : pieces_info) {
            if (p == null) continue;
            Bitmap piece_bmp = chess_images.getPieceImage(p);
            int cell_width = piece_bmp.getWidth();
            canvas.drawBitmap(piece_bmp, board.left + p.x_pos * cell_width, board.top + p.y_pos * cell_width, board_paint);
        }
    }
}
