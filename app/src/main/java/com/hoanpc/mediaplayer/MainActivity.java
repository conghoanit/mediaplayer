package com.hoanpc.mediaplayer;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, MediaManager.OnStateMediaCallBack, MTask.OnMTaskCallBack {
    private static final int LEVEL_PAUSED = 1;
    private static final int LEVEL_PLAY = 0;
    private MainViewModel model;
    private ImageView ivPlay;
    private TextView tvSong, tvSinger, tvCurrentTime, tvTotalTime;
    private SeekBar seekBar;
    private RecyclerView rvSong;
    private MTask mTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        findViewById(R.id.iv_next).setOnClickListener(this);
        findViewById(R.id.iv_previous).setOnClickListener(this);
        tvSong = findViewById(R.id.tv_song);
        tvSinger = findViewById(R.id.tv_singer);
        seekBar = findViewById(R.id.seek_bar);
        seekBar.setOnSeekBarChangeListener(new SeekBarChangeAdapter() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                MediaManager.getInstance().seekTo(seekBar.getProgress());
            }
        });
        tvCurrentTime = findViewById(R.id.tv_current_time);
        tvTotalTime = findViewById(R.id.tv_total_time);

        ivPlay = findViewById(R.id.iv_play);
        ivPlay.setOnClickListener(this);

        model = new ViewModelProvider(this).get(MainViewModel.class);

        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE
            }, 101);
            return;
        }
        model.loadMusicFromStorage();
        initListSongUI();
    }

    private void initListSongUI() {
        model.getListSong().add(new SongEntity("onl",
            "Gặp nhưng không ở lại", "Hiền Hồ",
                "Qua đêm",
                "https://data25.chiasenhac.com/download2/2135/1/2134648-25a034b3/128/Gap%20Nhung%20Khong%20O%20Lai%20-%20Hien%20Ho.mp3"));
        MediaManager.getInstance().setData(model.getListSong());
        MediaManager.getInstance().setCallBack(this);

        SongAdapter adapter = new SongAdapter(model.getListSong(), this);
        adapter.setCallBack(this::playCurrentSong);
        rvSong = findViewById(R.id.rv_song);
        rvSong.setLayoutManager(new LinearLayoutManager(this));
        rvSong.setAdapter(adapter);
        mTask = new MTask(null, this);
        mTask.startAsync(null);

        updateSongUI();
    }

    private void playCurrentSong(SongEntity item) {
        Toast.makeText(this, item.getName(), Toast.LENGTH_SHORT).show();
        MediaManager.getInstance().forcePlay(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        for (int rs : grantResults) {
            if (rs != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission not allow!", Toast.LENGTH_SHORT).show();
                finish();
                return;
            }
        }
        model.loadMusicFromStorage();
        initListSongUI();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_next:
                MediaManager.getInstance().next();
                break;
            case R.id.iv_play:
                MediaManager.getInstance().play();
                break;
            case R.id.iv_previous:
                MediaManager.getInstance().previous();
                break;
            default:
                break;
        }
    }

    @Override
    public void stateChange(int state) {
        if (state == MediaManager.STATE_PLAYING) {
            ivPlay.setImageLevel(LEVEL_PAUSED);
            updateSongUI();
        } else if (state == MediaManager.STATE_PAUSED) {
            ivPlay.setImageLevel(LEVEL_PLAY);
        }
    }

    private void updateSongUI() {
        SongEntity song = MediaManager.getInstance().getCurrentSong();
        tvSong.setText(song.getName());
        tvSinger.setText(song.getAlbum());
        seekBar.setMax(MediaManager.getInstance().getTotalTime());
        tvTotalTime.setText(MediaManager.getInstance().getTotalTimeText());
        ((SongAdapter) rvSong.getAdapter()).updateCurrent(song);
        rvSong.scrollToPosition(MediaManager.getInstance().getIndex());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mTask != null) mTask.stop();
        MediaManager.getInstance().stop();
    }

    @Override
    public Object executeTask(MTask task, String key, Object data) {
        while (true) {
            try {
                Thread.sleep(200);
                task.requestUI(null);
            } catch (InterruptedException e) {
                e.printStackTrace();
                return false;
            }
        }
    }

    @Override
    public void updateUI(MTask task, String key, Object data) {
        try {
            seekBar.setProgress(MediaManager.getInstance().getCurrentTime());
            tvCurrentTime.setText(MediaManager.getInstance().getCurrentTimeText());
        } catch (Exception ignored) {
            //do nothing
        }
    }
}
