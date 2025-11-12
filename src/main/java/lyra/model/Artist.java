package lyra.model;

/**
 * Artist represents information about a musical artist.
 */
public class Artist {
    /** Attributes */
    private int artistId;
    private String name;

    /** Artist constructor with id */
    public Artist(int artistId, String name) {
        this.artistId = artistId;
        this.name = name;
    }

    /** Artist constructor without id (for inserts) */
    public Artist(String name) {
        this.name = name;
    }

    /** Getters and setters */
    public int getArtistId() {
        return artistId;
    }

    public void setArtistId(int artistId) {
        this.artistId = artistId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /** Override toString for easy printing */
    @Override
    public String toString() {
        return "Artist{id: " + artistId + ", name: '" + name + "'}";
    }
}
