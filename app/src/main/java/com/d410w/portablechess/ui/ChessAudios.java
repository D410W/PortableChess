package com.d410w.portablechess.ui;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.SoundPool;
import com.d410w.portablechess.R;
import com.d410w.portablechess.engine.PieceEvent;

public class ChessAudios {

    SoundPool sound_pool;
    int move_sound_id;
    int capture_sound_id;
    boolean is_loaded = false;

    public ChessAudios(Context context) {
        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();

        sound_pool = new SoundPool.Builder()
                .setMaxStreams(5) // Number of sounds that can play simultaneously
                .setAudioAttributes(audioAttributes)
                .build();

        sound_pool.setOnLoadCompleteListener((soundPool1, sampleId, status) -> {
            if (status == 0) {
                is_loaded = true;
            }
        });

        move_sound_id = sound_pool.load(context, R.raw.move, 1);
        capture_sound_id = sound_pool.load(context, R.raw.capture, 1);
    }

    public void playSound(PieceEvent event) {
        if (!is_loaded) return;

        if (event == PieceEvent.MOVE_SELF) {
            sound_pool.play(move_sound_id, 1.0f, 1.0f, 1, 0, 1.0f);
        } else if (event == PieceEvent.CASTLE) {
            sound_pool.play(move_sound_id, 1.0f, 1.0f, 1, 0, 1.0f);
        } else if (event == PieceEvent.CAPTURE) {
            sound_pool.play(capture_sound_id, 1.0f, 1.0f, 1, 0, 1.0f);
        }
    }
}
