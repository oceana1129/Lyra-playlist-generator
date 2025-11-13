package lyra.tools;

import lyra.dal.*;
import lyra.model.*;

import java.sql.Date;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Inserter {

    private static Emotion ensureEmotion(EmotionDao emotionDao, String name) throws SQLException {
        try {
            return emotionDao.create(new Emotion(name));
        } catch (SQLIntegrityConstraintViolationException e) {
            for (Emotion e2 : emotionDao.getAllEmotions()) {
                if (e2.getName().equalsIgnoreCase(name)) {
                    return e2;
                }
            }
            throw e;
        }
    }

    private static Artist ensureArtist(ArtistDao artistDao, String name) throws SQLException {
        try {
            return artistDao.create(new Artist(name));
        } catch (SQLIntegrityConstraintViolationException e) {
            for (Artist a : artistDao.getAllArtists()) {
                if (a.getName().equalsIgnoreCase(name)) {
                    return a;
                }
            }
            throw e;
        }
    }

    private static Genre ensureGenre(GenreDao genreDao, String name) throws SQLException {
        try {
            return genreDao.create(new Genre(name));
        } catch (SQLIntegrityConstraintViolationException e) {
            for (Genre g : genreDao.getAllGenres()) {
                if (g.getName().equalsIgnoreCase(name)) {
                    return g;
                }
            }
            throw e;
        }
    }

    private static AudioFeatures ensureAudioFeatures(
            AudioFeaturesDao dao,
            int songId, int popularity, int energy, int danceability, int positiveness,
            int speechiness, int liveness, int acousticness, int instrumentalness) throws SQLException {
        try {
            return dao.create(new AudioFeatures(
                    songId, popularity, energy, danceability, positiveness,
                    speechiness, liveness, acousticness, instrumentalness
            ));
        } catch (SQLIntegrityConstraintViolationException e) {
            AudioFeatures existing = dao.getBySongId(songId);
            if (existing != null) return existing;
            throw e;
        }
    }

    private static ContextFilters ensureContextFilters(
            ContextFiltersDao dao,
            int songId, boolean party, boolean study, boolean relaxation, boolean exercise,
            boolean running, boolean yoga, boolean driving, boolean social, boolean morning) throws SQLException {
        try {
            return dao.create(new ContextFilters(
                    songId, party, study, relaxation, exercise, running, yoga, driving, social, morning
            ));
        } catch (SQLIntegrityConstraintViolationException e) {
            ContextFilters existing = dao.getBySongId(songId);
            if (existing != null) return existing;
            throw e;
        }
    }

    private static Lyrics ensureLyrics(LyricsDao dao, int songId, String content) throws SQLException {
        try {
            return dao.create(new Lyrics(songId, content));
        } catch (SQLIntegrityConstraintViolationException e) {
            Lyrics existing = dao.getBySongId(songId);
            if (existing != null) return existing;
            throw e;
        }
    }

    private static SongArtist ensureSongArtist(SongArtistDao dao, int songId, int artistId) throws SQLException {
        try {
            return dao.create(new SongArtist(songId, artistId));
        } catch (SQLIntegrityConstraintViolationException e) {
            for (SongArtist sa : dao.getBySongId(songId)) {
                if (sa.getSongId() == songId && sa.getArtistId() == artistId) return sa;
            }
            throw e;
        }
    }

    private static SongGenre ensureSongGenre(SongGenreDao dao, int songId, int genreId) throws SQLException {
        try {
            return dao.create(new SongGenre(songId, genreId));
        } catch (SQLIntegrityConstraintViolationException e) {
            for (SongGenre sg : dao.getAllSongGenres()) {
                if (sg.getSongId() == songId && sg.getGenreId() == genreId) return sg;
            }
            throw e;
        }
    }

    private static List<SongGenre> getSongGenresBySongId(SongGenreDao songGenreDao, int songId) throws SQLException {
        List<SongGenre> out = new ArrayList<>();
        for (SongGenre sg : songGenreDao.getAllSongGenres()) {
            if (sg.getSongId() == songId) out.add(sg);
        }
        return out;
    }

    private static List<Recommendation> getRecommendationsBySongId(RecommendationDao recommendationDao, int songId) throws SQLException {
        List<Recommendation> out = new ArrayList<>();
        for (Recommendation r : recommendationDao.getAllRecommendations()) {
            if (r.getSongId() == songId) out.add(r);
        }
        return out;
    }

    public static void main(String[] args) throws SQLException {
        GenreDao genreDao = GenreDao.getInstance();
        SongGenreDao songGenreDao = SongGenreDao.getInstance();
        RecommendationDao recommendationDao = RecommendationDao.getInstance();
        EmotionDao emotionDao = EmotionDao.getInstance();
        ArtistDao artistDao = ArtistDao.getInstance();
        SongDao songDao = SongDao.getInstance();
        SongArtistDao songArtistDao = SongArtistDao.getInstance();
        AudioFeaturesDao audioFeaturesDao = AudioFeaturesDao.getInstance();
        ContextFiltersDao contextFiltersDao = ContextFiltersDao.getInstance();
        LyricsDao lyricsDao = LyricsDao.getInstance();

        System.out.println("=== DB INFO ===");
        try (java.sql.Connection c = new ConnectionManager().getConnection();
             java.sql.PreparedStatement ps = c.prepareStatement(
                     "SELECT DATABASE() AS db, @@hostname AS host, @@port AS port, @@datadir AS datadir"
             );
             java.sql.ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                System.out.println("[DB] db=" + rs.getString("db")
                        + " host=" + rs.getString("host")
                        + " port=" + rs.getString("port")
                        + " datadir=" + rs.getString("datadir"));
            }
            System.out.println("[DB] autocommit=" + c.getAutoCommit());
        }
        System.out.println();

        Emotion happy = ensureEmotion(emotionDao, "happy");
        Emotion sad   = ensureEmotion(emotionDao, "sad");
        Emotion angry = ensureEmotion(emotionDao, "angry");

        Emotion emotionTemp = emotionDao.create(new Emotion("scared"));
        System.out.println("Emotion Read: " + emotionDao.getEmotionById(emotionTemp.getEmotionId()).getName());
        emotionTemp = emotionDao.updateEmotionName(emotionTemp, "SCARED");
        System.out.println("Emotion Update: " + emotionTemp.getName());
        System.out.println("Emotions Before Delete: " + Arrays.toString(emotionDao.getAllEmotions().toArray()));
        emotionDao.delete(emotionTemp);
        System.out.println("Emotions After Delete:  " + Arrays.toString(emotionDao.getAllEmotions().toArray()));
        System.out.println();

        System.out.println("=== Artist CRUD ===");
        Artist artist1 = ensureArtist(artistDao, "Ed Sheeran");
        Artist artist2 = ensureArtist(artistDao, "Billie Eilish");
        Artist artist3 = ensureArtist(artistDao, "Drake");

        System.out.println("Artist Read: " + artistDao.getArtistById(artist1.getArtistId()).getName());
        artist1 = artistDao.updateArtistName(artist1, "ED SHEERAN");
        System.out.println("Artist Updated: " + artist1.getName());

        Artist artistTemp = artistDao.create(new Artist("Temp Artist"));
        System.out.println("Artists Before Delete: " + Arrays.toString(artistDao.getAllArtists().toArray()));
        artistDao.delete(artistTemp);
        System.out.println("Artists After Delete:  " + Arrays.toString(artistDao.getAllArtists().toArray()));
        System.out.println();

        System.out.println("=== Song CRUD ===");
        Song song1 = songDao.create(new Song(
                "Shape of You", "Divide", Date.valueOf("2017-01-06"),
                240, happy.getEmotionId(), "C#", 96.0, -3.5, "4/4", false
        ));
        Song song2 = songDao.create(new Song(
                "Bad Guy", "When We All Fall Asleep", Date.valueOf("2019-03-29"),
                194, angry.getEmotionId(), "G", 135.0, -4.2, "4/4", true
        ));

        System.out.println("Song Read: " + songDao.getSongById(song1.getSongId()).getTitle());
        song1 = songDao.updateSongTitle(song1, "Shape Of You (Remastered)");
        System.out.println("Song Updated: " + song1.getTitle());

        Song songTemp = songDao.create(new Song(
                "Temp Song", "Temp Album", Date.valueOf("2020-01-01"),
                123, sad.getEmotionId(), "A", 120.0, -6.0, "4/4", false
        ));
        songDao.delete(songTemp);
        System.out.println();

        System.out.println("=== SongArtist CRUD ===");
        SongArtist sa1 = ensureSongArtist(songArtistDao, song1.getSongId(), artist1.getArtistId());
        SongArtist sa2 = ensureSongArtist(songArtistDao, song1.getSongId(), artist2.getArtistId());
        System.out.println("Artists for Song " + song1.getTitle() + ": " +
                Arrays.toString(songArtistDao.getBySongId(song1.getSongId()).toArray()));
        sa2 = songArtistDao.updateArtistForSong(sa2, artist3.getArtistId());
        System.out.println("Mapping Updated: " + sa2);
        songArtistDao.delete(sa2);
        System.out.println();

        System.out.println("=== Genre CRUD ===");
        Genre pop = ensureGenre(genreDao, "Pop");
        Genre hiphop = ensureGenre(genreDao, "Hip-Hop");
        Genre edm = ensureGenre(genreDao, "EDM");
        pop = genreDao.updateGenreName(pop, "POP");
        System.out.println("Genres: " + Arrays.toString(genreDao.getAllGenres().toArray()));
        System.out.println();

        System.out.println("=== SongGenre CRUD ===");
        SongGenre sg1 = ensureSongGenre(songGenreDao, song1.getSongId(), pop.getGenreId());
        SongGenre sg2 = ensureSongGenre(songGenreDao, song1.getSongId(), hiphop.getGenreId());
        System.out.println("Genres for Song " + song1.getTitle() + ": " +
                Arrays.toString(getSongGenresBySongId(songGenreDao, song1.getSongId()).toArray()));
        sg2 = songGenreDao.updateGenreId(sg2, edm.getGenreId());
        System.out.println("SongGenre Updated: " + sg2);
        songGenreDao.delete(sg2);
        System.out.println();

        System.out.println("=== AudioFeatures CRUD ===");
        AudioFeatures af1 = ensureAudioFeatures(audioFeaturesDao,
                song1.getSongId(), 95, 80, 75, 70, 10, 12, 20, 0);
        System.out.println("AudioFeatures Read: " + audioFeaturesDao.getBySongId(song1.getSongId()));

        af1 = audioFeaturesDao.updatePopularity(af1, 97);
        System.out.println("AudioFeatures Updated: " + af1);

        AudioFeatures afTemp = ensureAudioFeatures(audioFeaturesDao,
                song2.getSongId(), 60, 65, 55, 50, 8, 15, 30, 5);
        System.out.println("AudioFeatures Before Delete: " + Arrays.toString(audioFeaturesDao.getAll().toArray()));
        audioFeaturesDao.delete(afTemp);
        System.out.println("AudioFeatures After  Delete: " + Arrays.toString(audioFeaturesDao.getAll().toArray()));
        System.out.println();

        System.out.println("=== ContextFilters CRUD ===");
        ContextFilters cf1 = ensureContextFilters(contextFiltersDao,
                song1.getSongId(), true, false, true, false, false, false, true, true, true);
        System.out.println("ContextFilters Read: " + contextFiltersDao.getBySongId(song1.getSongId()));

        cf1 = contextFiltersDao.updateParty(cf1, false);
        System.out.println("ContextFilters Updated: " + contextFiltersDao.getBySongId(song1.getSongId()));

        ContextFilters cfTemp = ensureContextFilters(contextFiltersDao,
                song2.getSongId(), false, true, false, true, true, false, false, false, false);
        System.out.println("ContextFilters Before Delete: " + Arrays.toString(contextFiltersDao.getAll().toArray()));
        contextFiltersDao.delete(cfTemp);
        System.out.println("ContextFilters After  Delete: " + Arrays.toString(contextFiltersDao.getAll().toArray()));
        System.out.println();

        System.out.println("=== Lyrics CRUD ===");
        Lyrics lyr1 = ensureLyrics(lyricsDao,
                song1.getSongId(), "The club isn't the best place to find a lover...");
        System.out.println("Lyrics Read: " + lyricsDao.getBySongId(song1.getSongId()));

        lyr1 = lyricsDao.updateContent(lyr1, "The club isn't the best place to find a lover (edit)...");
        System.out.println("Lyrics Updated: " + lyricsDao.getBySongId(song1.getSongId()));

        Lyrics lyrTemp = ensureLyrics(lyricsDao,
                song2.getSongId(), "[temp lyrics to be removed]");
        System.out.println("Lyrics Before Delete: " + Arrays.toString(lyricsDao.getAll().toArray()));
        lyricsDao.delete(lyrTemp);
        System.out.println("Lyrics After  Delete: " + Arrays.toString(lyricsDao.getAll().toArray()));
        System.out.println();

        System.out.println("=== Recommendation CRUD ===");
        Recommendation r1 = recommendationDao.create(
                new Recommendation(song1.getSongId(), song2.getSongId(), 0.92));
        System.out.println("Recommendation Created: " + r1);

        Recommendation readR1 = recommendationDao.getRecommendationById(r1.getRecommendationId());
        System.out.println("Recommendation Read: " + readR1);

        r1 = recommendationDao.updateSimilarityScore(r1, 0.95);
        System.out.println("Recommendation Updated: " + r1);

        Recommendation r2 = recommendationDao.create(
                new Recommendation(song1.getSongId(), song2.getSongId(), 0.50));
        System.out.println("Recommendations Before Delete: " +
                Arrays.toString(recommendationDao.getAllRecommendations().toArray()));

        recommendationDao.delete(r2);
        System.out.println("Recommendations After  Delete: " +
                Arrays.toString(recommendationDao.getAllRecommendations().toArray()));
        System.out.println();
    }
}
