package lyra.model;

/**
 * Recommendation represents a similarity relationship between two songs.
 */
public class Recommendation {
    /** Attributes */
    private int recommendationId;
    private int songId;
    private int similarSongId;
    private double similarityScore;

    /** Recommendation constructor with id */
    public Recommendation(int recommendationId, int songId, int similarSongId, double similarityScore) {
        this.recommendationId = recommendationId;
        this.songId = songId;
        this.similarSongId = similarSongId;
        this.similarityScore = similarityScore;
    }

    /** Recommendation constructor without id (for inserts) */
    public Recommendation(int songId, int similarSongId, double similarityScore) {
        this.songId = songId;
        this.similarSongId = similarSongId;
        this.similarityScore = similarityScore;
    }

    /** Getters and setters */
    public int getRecommendationId() {
        return recommendationId;
    }

    public void setRecommendationId(int recommendationId) {
        this.recommendationId = recommendationId;
    }

    public int getSongId() {
        return songId;
    }

    public void setSongId(int songId) {
        this.songId = songId;
    }

    public int getSimilarSongId() {
        return similarSongId;
    }

    public void setSimilarSongId(int similarSongId) {
        this.similarSongId = similarSongId;
    }

    public double getSimilarityScore() {
        return similarityScore;
    }

    public void setSimilarityScore(double similarityScore) {
        this.similarityScore = similarityScore;
    }

    /** Override the toString method to print out a recommendation */
    @Override
    public String toString() {
        return "Recommendation{id: " + recommendationId +
                ", songId: " + songId +
                ", similarSongId: " + similarSongId +
                ", similarityScore: " + similarityScore + "}";
    }
}
