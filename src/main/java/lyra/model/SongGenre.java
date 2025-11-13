package lyra.model;

/**
 * SongGenre represents a many-to-many relationship between Song and Genre.
 */
public class SongGenre {
    /** Attributes */
    private int songId;
    private int genreId;

    /** SongGenre constructor */
    public SongGenre(int songId, int genreId) {
        this.songId = songId;
        this.genreId = genreId;
    }

    /** Getters and setters */
    public int getSongId() {
        return songId;
    }

    public void setSongId(int songId) {
        this.songId = songId;
    }

    public int getGenreId() {
        return genreId;
    }

    public void setGenreId(int genreId) {
        this.genreId = genreId;
    }

    /** Override the toString method to print out a song-genre mapping */
    @Override
    public String toString() {
        return "SongGenre{songId: " + songId + ", genreId: " + genreId + "}";
    }
}
