package com.d410w.portablechess.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import com.d410w.portablechess.engine.ChessPiece;
import com.d410w.portablechess.R;

import java.util.Vector;

import static java.lang.Math.min;

public class PiecesImages {

    Vector<Bitmap> unsized_images;
    Vector<Bitmap> images;

    PiecesImages(Context context) {
        unsized_images = new Vector<>();
        images = new Vector<>();

        int[] pieces = new int[]{
                R.drawable.black_rook,
                R.drawable.black_knight,
                R.drawable.black_bishop,
                R.drawable.black_queen,
                R.drawable.black_king,
                R.drawable.black_pawn,
                R.drawable.white_rook,
                R.drawable.white_knight,
                R.drawable.white_bishop,
                R.drawable.white_queen,
                R.drawable.white_king,
                R.drawable.white_pawn,
        };

        for (int piece : pieces) {
            Bitmap bmp = BitmapFactory.decodeResource(context.getResources(), piece);
            unsized_images.add(bmp);
            images.add(bmp);
        }
    }

    public Bitmap getPieceImage(ChessPiece p) {
        int idx = 0;
        switch (p.color) {
            case WHITE: {
                idx += 6;
            } break;
            case BLACK: {
                idx += 0;
            } break;
        }
        switch (p.type) {
            case ROOK: {
                idx += 0;
            } break;
            case KNIGHT: {
                idx += 1;
            } break;
            case BISHOP: {
                idx += 2;
            } break;
            case QUEEN: {
                idx += 3;
            } break;
            case KING: {
                idx += 4;
            } break;
            case PAWN: {
                idx += 5;
            } break;
        }
        return images.get(idx);
    }

    public void resizeImages(int swidth, int sheight) {
        int size = min(swidth, sheight) / 8;

        for (int i = 0; i < unsized_images.size(); ++i) {
            Bitmap resized = Bitmap.createScaledBitmap(
                    unsized_images.get(i),
                    size,
                    size,
                    true
            );

            images.set(i, resized);
        }
    }
}
