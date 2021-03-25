package com.hoanpc.mediaplayer;

public class SongEntity {
    private final String id;
    private final String name;
    private final String artist;
    private final String album;
    private final String path;

    public SongEntity(String id, String name, String artist, String album, String path) {
        this.id = id;
        this.name = name;
        this.artist = artist;
        this.album = album;
        this.path = path;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getArtist() {
        return artist;
    }

    public String getAlbum() {
        return album;
    }

    public String getPath() {
        return path;
    }

    @Override
    public String toString() {
        return "SongEntity{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", artist='" + artist + '\'' +
                ", album='" + album + '\'' +
                ", author='"  + '\'' +
                ", path='" + path + '\'' +
                '}';
    }
}
