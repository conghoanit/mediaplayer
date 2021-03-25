package com.hoanpc.mediaplayer;

import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

public class MainViewModel extends ViewModel {
    private static final String TAG = MainViewModel.class.getName();
    private List<SongEntity> listSong;

    public void loadMusicFromStorage() {
        listSong = new ArrayList<>();

        Uri musicTable = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor c = App.getInstance().getContentResolver()
                .query(musicTable,
                        null, null, null, null);
        c.moveToFirst();
        int idIndex = c.getColumnIndex(MediaStore.Audio.Media._ID);
        int nameIndex = c.getColumnIndex(MediaStore.Audio.Media.TITLE);
        int artistIndex = c.getColumnIndex(MediaStore.Audio.Media.ARTIST);
        int albumIndex = c.getColumnIndex(MediaStore.Audio.Media.ALBUM);
        int dataIndex = c.getColumnIndex(MediaStore.Audio.Media.DATA);

        while (!c.isAfterLast()) {
            String id = c.getString(idIndex);
            String name = c.getString(nameIndex);
            String artist = c.getString(artistIndex);
            String album = c.getString(albumIndex);
            String path = c.getString(dataIndex);

            listSong.add(new SongEntity(id, name, artist, album,  path));
            c.moveToNext();
        }
        Log.i(TAG, listSong.toString());
        c.close();
    }

    public List<SongEntity> getListSong() {
        return listSong;
    }
}
