package com.hoanpc.mediaplayer;

import android.annotation.SuppressLint;
import android.media.MediaPlayer;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class MediaManager {
    public static int STATE_IDLE = 1;
    public static int STATE_PLAYING = 2;
    public static int STATE_PAUSED = 3;
    private static MediaManager instance;
    private final MediaPlayer mPlayer;
    private int state = STATE_IDLE;
    private List<SongEntity> mData;
    private int index;
    private OnStateMediaCallBack callBack;

    private MediaManager() {
        //for singleton
        mPlayer = new MediaPlayer();
        index = 0;
    }

    public static MediaManager getInstance() {
        if (instance == null) {
            instance = new MediaManager();
        }
        return instance;
    }

    public void play() {
        try {
            if (state == STATE_PAUSED) {
                mPlayer.start();
                state = STATE_PLAYING;
                callBack.stateChange(state);
            } else if (state == STATE_PLAYING) {
                mPlayer.pause();
                state = STATE_PAUSED;
                callBack.stateChange(state);
            } else {
                mPlayer.reset();
                mPlayer.setDataSource(mData.get(index).getPath());
                mPlayer.prepare();
                mPlayer.start();
                state = STATE_PLAYING;
                callBack.stateChange(state);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void next() {
        index++;
        if (index >= mData.size()) {
            index = 0;
        }
        state = STATE_IDLE;
        play();
    }

    public void previous() {
        index--;
        if (index < 0) {
            index = mData.size() - 1;
        }
        state = STATE_IDLE;
        play();
    }

    public void stop() {
        mPlayer.reset();
        mPlayer.release();
        state = STATE_IDLE;
        instance = null;
    }

    public void forcePlay(SongEntity item) {
        this.index = mData.indexOf(item);
        state = STATE_IDLE;
        play();
    }

    public int getState() {
        return state;
    }

    public SongEntity getCurrentSong() {
        return mData.get(index);
    }

    public void setData(List<SongEntity> mData) {
        this.mData = mData;
    }

    public void setCallBack(OnStateMediaCallBack callBack) {
        this.callBack = callBack;
    }

    public int getTotalTime() {
        return mPlayer.getDuration();
    }

    public int getCurrentTime() {
        return mPlayer.getCurrentPosition();
    }

    @SuppressLint("SimpleDateFormat")
    public String getTotalTimeText() {
        return new SimpleDateFormat("mm:ss")
                .format(new Date(mPlayer.getDuration()));
    }

    @SuppressLint("SimpleDateFormat")
    public String getCurrentTimeText() {
        return new SimpleDateFormat("mm:ss")
                .format(new Date(mPlayer.getCurrentPosition()));
    }

    public void seekTo(int progress) {
        mPlayer.seekTo(progress);
    }

    public int getIndex() {
        return index;
    }

    public interface OnStateMediaCallBack {
        void stateChange(int state);
    }
}
