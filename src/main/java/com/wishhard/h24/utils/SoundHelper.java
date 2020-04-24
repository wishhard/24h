package com.wishhard.h24.utils;

import android.app.Activity;
import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;

import com.wishhard.h24.R;

public class SoundHelper {



    private SoundPool mSoundPool;
    private int tick_id,tick2_id;
    private boolean mLoaded;
    private float mVolume;

    public SoundHelper(Activity activity) {
        AudioManager audioManager = (AudioManager) activity.getSystemService(Context.AUDIO_SERVICE);
        float actVolum = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        float maxVolum = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        mVolume = actVolum / maxVolum;

        activity.setVolumeControlStream(AudioManager.STREAM_MUSIC);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            AudioAttributes audioAttrib = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();
            mSoundPool = new SoundPool.Builder().setAudioAttributes(audioAttrib).setMaxStreams(2).build();
        } else {
            mSoundPool = new SoundPool(2, AudioManager.STREAM_MUSIC, 0);
        }

        tick_id = mSoundPool.load(activity, R.raw.tick_1, 1);
        tick2_id = mSoundPool.load(activity, R.raw.tick_2, 1);

        mSoundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                mLoaded = true;
            }
        });

    }

    public void playSound(int sec) {
        if (mLoaded) {
            if(sec%2 == 0) {
                mSoundPool.play(tick_id, mVolume, mVolume, 1, 0, 1f);
            } else {
                mSoundPool.play(tick2_id, mVolume, mVolume, 1, 0, 1f);
            }

        }
    }


    public void releaseSoundPool() {
        mSoundPool.release();
    }





}
