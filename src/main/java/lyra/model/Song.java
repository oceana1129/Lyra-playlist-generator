package lyra.model;

import java.sql.Date;

/**
 * Song represents a track in the Spotify dataset.
 */
public class Song {
    /** Attributes */
    private int songId;
    private String title;
    private String album;
    private Date releaseDate;
    private int lengthSeconds;
    private Integer emotionId;   // can be null
    private String keySignature;
    private Double tempo;
    private Double loudness;
    private String timeSignature;
    private Boolean explicit;

    /** Full constructor with songId */
    public Song(int songId, String title, String album, Date releaseDate,
                int lengthSeconds, Integer emotionId, String keySignature,
                Double tempo, Double loudness, String timeSignature,
                Boolean explicit) {
        this.songId = songId;
        this.title = title;
        this.album = album;
        this.releaseDate = releaseDate;
        this.lengthSeconds = lengthSeconds;
        this.emotionId = emotionId;
        this.keySignature = keySignature;
        this.tempo = tempo;
        this.loudness = loudness;
        this.timeSignature = timeSignature;
        this.explicit = explicit;
    }

    /** Constructor without id (for inserts) */
    public Song(String title, String album, Date releaseDate,
                int lengthSeconds, Integer emotionId, String keySignature,
                Double tempo, Double loudness, String timeSignature,
                Boolean explicit) {
        this.title = title;
        this.album = album;
        this.releaseDate = releaseDate;
        this.lengthSeconds = lengthSeconds;
        this.emotionId = emotionId;
        this.keySignature = keySignature;
        this.tempo = tempo;
        this.loudness = loudness;
        this.timeSignature = timeSignature;
        this.explicit = explicit;
    }

    /** Getters and setters */
    public int getSongId() {
        return songId;
    }

    public void setSongId(int songId) {
        this.songId = songId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    public int getLengthSeconds() {
        return lengthSeconds;
    }

    public void setLengthSeconds(int lengthSeconds) {
        this.lengthSeconds = lengthSeconds;
    }

    public Integer getEmotionId() {
        return emotionId;
    }

    public void setEmotionId(Integer emotionId) {
        this.emotionId = emotionId;
    }

    public String getKeySignature() {
        return keySignature;
    }

    public void setKeySignature(String keySignature) {
        this.keySignature = keySignature;
    }

    public Double getTempo() {
        return tempo;
    }

    public void setTempo(Double tempo) {
        this.tempo = tempo;
    }

    public Double getLoudness() {
        return loudness;
    }

    public void setLoudness(Double loudness) {
        this.loudness = loudness;
    }

    public String getTimeSignature() {
        return timeSignature;
    }

    public void setTimeSignature(String timeSignature) {
        this.timeSignature = timeSignature;
    }

    public Boolean getExplicit() {
        return explicit;
    }

    public void setExplicit(Boolean explicit) {
        this.explicit = explicit;
    }

    /** Override toString to print out a song */
    @Override
    public String toString() {
        return "Song{id: " + songId +
                ", title: '" + title + '\'' +
                ", album: '" + album + '\'' +
                ", lengthSeconds: " + lengthSeconds +
                ", emotionId: " + emotionId +
                ", explicit: " + explicit +
                "}";
    }
}
