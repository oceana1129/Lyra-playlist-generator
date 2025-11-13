package lyra.model;

public class AudioFeatures {
    private int songId;
    private int popularity;
    private int energy;
    private int danceability;
    private int positiveness;
    private int speechiness;
    private int liveness;
    private int acousticness;
    private int instrumentalness;

    public AudioFeatures(int songId) {
        this.songId = songId;
    }

    public AudioFeatures(int songId, int popularity, int energy, int danceability, int positiveness,
                         int speechiness, int liveness, int acousticness, int instrumentalness) {
        this.songId = songId;
        this.popularity = popularity;
        this.energy = energy;
        this.danceability = danceability;
        this.positiveness = positiveness;
        this.speechiness = speechiness;
        this.liveness = liveness;
        this.acousticness = acousticness;
        this.instrumentalness = instrumentalness;
    }

    public int getSongId() { return songId; }
    public int getPopularity() { return popularity; }
    public int getEnergy() { return energy; }
    public int getDanceability() { return danceability; }
    public int getPositiveness() { return positiveness; }
    public int getSpeechiness() { return speechiness; }
    public int getLiveness() { return liveness; }
    public int getAcousticness() { return acousticness; }
    public int getInstrumentalness() { return instrumentalness; }

    public void setPopularity(int popularity) { this.popularity = popularity; }
    public void setEnergy(int energy) { this.energy = energy; }
    public void setDanceability(int danceability) { this.danceability = danceability; }
    public void setPositiveness(int positiveness) { this.positiveness = positiveness; }
    public void setSpeechiness(int speechiness) { this.speechiness = speechiness; }
    public void setLiveness(int liveness) { this.liveness = liveness; }
    public void setAcousticness(int acousticness) { this.acousticness = acousticness; }
    public void setInstrumentalness(int instrumentalness) { this.instrumentalness = instrumentalness; }

    @Override
    public String toString() {
        return "AudioFeatures{songId=" + songId +
                ", popularity=" + popularity +
                ", energy=" + energy +
                ", danceability=" + danceability +
                ", positiveness=" + positiveness +
                ", speechiness=" + speechiness +
                ", liveness=" + liveness +
                ", acousticness=" + acousticness +
                ", instrumentalness=" + instrumentalness + "}";
    }
}
