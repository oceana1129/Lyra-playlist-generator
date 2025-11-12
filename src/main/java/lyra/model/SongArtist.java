package lyra.model;

/**
 * SongArtist represents the many-to-many relationship
 * between Song and Artist.
 */
public class SongArtist {
    /** Attributes */
    private int songId;
    private int artistId;

    /** Constructor */
    public SongArtist(int songId, int artistId) {
        this.songId = songId;
        this.artistId = artistId;
    }

    /** Getters and setters */
    public int getSongId() {
        return songId;
    }

    public void setSongId(int songId) {
        this.songId = songId;
    }

    public int getArtistId() {
        return artistId;
    }

    public void setArtistId(int artistId) {
        this.artistId = artistId;
    }

    /** Override toString to print mapping */
    @Override
    public String toString() {
        return "SongArtist{songId: " + songId +
                ", artistId: " + artistId + "}";
    }
}
