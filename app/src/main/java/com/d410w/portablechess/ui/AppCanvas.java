package com.d410w.portablechess.ui;

import android.content.Context;
import android.graphics.*;
import android.view.MotionEvent;
import android.view.View;
import com.d410w.portablechess.engine.ChessPiece;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class AppCanvas extends View {

    Rect board;
    Paint board_paint;

    ArrayList<ChessPiece> pieces_info;
    PiecesImages p_images;
    public int x_pos;
    public int y_pos;

    public AppCanvas(Context context) {
        super(context);

        pieces_info = new ArrayList<>();
        p_images = new PiecesImages(context);

        board_paint = new Paint();
        board_paint.setColor(Color.rgb(129, 84, 56));
        board_paint.setStrokeWidth(10);
        board_paint.setStyle(Paint.Style.FILL);

        x_pos = -1000;
        y_pos = -1000;
        centerRectangle();
    }

    public void centerRectangle() {
        centerRectangle(x_pos, y_pos);
    }
    public void centerRectangle(int x, int y) {
        x_pos = x;
        y_pos = y;
        board = new Rect(x_pos - 100, y_pos - 100, x_pos + 100, y_pos + 100);
        this.invalidate();
    }

    public void setPieces(List<ChessPiece> pieces) {
        this.pieces_info.clear();
        this.pieces_info.addAll(pieces);
        this.invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                centerRectangle((int)event.getX(), (int)event.getY());
                System.out.println("Touch down: x: " + event.getX() + ", y: " + event.getY());
                return true;
            case MotionEvent.ACTION_UP:
                break;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void onSizeChanged(int w, int h, int oldw, int oldh) {
        p_images.resizeImages(w, h);
    }

    @Override
    protected void onDraw(@NotNull Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.GRAY);
        canvas.drawRect(board, board_paint);
        for (ChessPiece p : pieces_info) {
            Bitmap piece_bmp = p_images.getPieceImage(p);
            int cell_width = piece_bmp.getWidth();
            canvas.drawBitmap(piece_bmp, board.left + p.x_pos * cell_width, board.top + p.y_pos * cell_width, board_paint);
        }
    }
}
