package lyra.model;

/**
 * Genre represents information about a musical genre.
 */
public class Genre {
    /** Attributes */
    private int genreId;
    private String name;

    /** Genre constructor with id */
    public Genre(int genreId, String name) {
        this.genreId = genreId;
        this.name = name;
    }

    /** Genre constructor without id (for inserts) */
    public Genre(String name) {
        this.name = name;
    }

    /** Getters and setters */
    public int getGenreId() {
        return genreId;
    }

    public void setGenreId(int genreId) {
        this.genreId = genreId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /** Override the toString method to print out a genre */
    @Override
    public String toString() {
        return "Genre{id: " + genreId + ", name: '" + name + "'}";
    }
}
