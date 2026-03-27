package com.d410w.portablechess.ui;

import android.content.Context;
import android.media.MediaPlayer;
import com.d410w.portablechess.R;
import com.d410w.portablechess.engine.PieceEvent;

public class ChessAudio {

    MediaPlayer move_self_audio;

    public ChessAudio(Context context) {
        move_self_audio = MediaPlayer.create(context, R.raw.move);
    }

    public void playSound(PieceEvent type) {
        if (type == PieceEvent.MOVE_SELF) {
            move_self_audio.start();
        }
    }
}
